package com.wongnai.tools.de.service.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wongnai.tools.de.ExceptionUtils;
import com.wongnai.tools.de.model.Column;
import com.wongnai.tools.de.model.Meta;
import com.wongnai.tools.de.model.Reference;
import com.wongnai.tools.de.model.Table;
import com.wongnai.tools.de.model.internal.MetaImpl;
import com.wongnai.tools.de.service.DataWriter;
import com.wongnai.tools.de.settings.Settings;

/**
 * Export context.
 */
public class Context {
	private static final Logger LOGGER = LoggerFactory.getLogger(Context.class);
	private Settings settings;
	private JdbcTemplate jdbcTemplate;
	private DataWriter dataWriter;
	private Map<Table, Data> datas;
	private Map<Reference, ReferenceSetting> referrings;
	private Map<Column, Transformer> transformersByCol;
	private List<TransformerMatcher> transformerMatchers;

	public Context(Settings settings, JdbcTemplate jdbcTemplate, DataWriter dataWriter) {
		this.settings = settings;
		this.jdbcTemplate = jdbcTemplate;
		this.dataWriter = dataWriter;
		this.datas = new LinkedHashMap<>();
		this.referrings = new HashMap<>();
		this.transformersByCol = new HashMap<>();
		this.transformerMatchers = new ArrayList<>();
	}

	/**
	 * Gets JDBC template.
	 *
	 * @return jdbc template
	 */
	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	/**
	 * Gets table's data.
	 *
	 * @param table
	 *            table
	 * @return data
	 */
	public Data get(Table table) {
		Data data = datas.get(table);
		if (data == null) {
			data = createData(table);
			datas.put(table, data);
		}
		return data;
	}

	/**
	 * Gets reference's setting.
	 *
	 * @param r
	 *            reference
	 * @return reference's setting
	 */
	public ReferenceSetting getReferenceSetting(Reference r) {
		ReferenceSetting rs = referrings.get(r);
		if (rs == null && settings.getDefaultValues() != null) {
			rs = ReferenceSetting.create(r, settings.getDefaultValues().getReferring());
			referrings.put(r, rs);
		}
		return rs;
	}

	/**
	 * Gets transformer chain by column.
	 *
	 * @param col
	 *            column
	 * @return transformer chain
	 */
	public Transformer getTransformer(Column col) {
		Transformer t = transformersByCol.get(col);

		if (t == null) {
			for (TransformerMatcher pm : transformerMatchers) {
				if (pm.matches(col.getFullName())) {
					t = pm.getTransformer();
					break;
				}
			}
			if (t == null) {
				t = Transformer.EMPTY;
			}

			transformersByCol.put(col, t);
		}

		return t;
	}

	private Data createData(Table table) {
		final Data data = new Data(this, table);

		data.getNewRows().subscribe(row -> dataWriter.writeRow(table, row), row -> {
		}, () -> {
		});

		return data;
	}

	/**
	 * Processes export.
	 *
	 * @param meta
	 *            meta data
	 */
	public void process(MetaImpl meta) {
		long start = System.currentTimeMillis();

		prepare(meta);

		if (settings.getTables() != null) {
			// Process the tables the same order as the settings
			settings.getTables().forEach(ts -> {
				if (ts.getSelectors() != null) {
					Data d = get(meta.getTable(ts.getName()));

					d.setSelectors(new ArrayList<>(ts.getSelectors()));
					d.process();
				}
			});
		}
		new ArrayList<>(datas.values()).forEach(Data::process);
		processRemaining();

		long end = System.currentTimeMillis();

		LOGGER.info("Finished in " + (end - start) + " ms.");
		datas.values().forEach(Data::logExportInfo);

		cleanUp();
	}

	private void prepare(Meta meta) {
		if (settings.getDefaultValues() != null) {
			if (settings.getDefaultValues().getSelectors() != null) {
				meta.getAllTables().forEach(t -> get(t).setSelectors(settings.getDefaultValues().getSelectors()));
			}
		}
		if (settings.getReferences() != null) {
			settings.getReferences().forEach(r -> {
				if (r.getFk() != null) {
					Reference ref = meta.getOrAddReference(r.getFk(), r.getPk());

					if (r.getReferring() != null) {
						referrings.put(ref, ReferenceSetting.create(ref, r.getReferring()));
					}
				} else if (r.getReferring() != null) {
					meta.getReferencesByKey(r.getPk())
							.forEach(ref -> referrings.put(ref, ReferenceSetting.create(ref, r.getReferring())));
				}
			});
		}
		if (settings.getTransformations() != null) {
			TransformerFactory tf = new TransformerFactory();
			settings.getTransformations().forEach(
					t -> transformerMatchers.add(new TransformerMatcher(Pattern.compile(t.getColumn()), tf.create(t))));
		}
	}

	private void processRemaining() {
		boolean hasUnloadKeys;
		do {
			hasUnloadKeys = false;

			for (Data d : new ArrayList<>(datas.values())) {
				hasUnloadKeys |= d.processUnloadedKeys();
				hasUnloadKeys |= d.processUnloadedReferenceKeys();
			}
		} while (hasUnloadKeys);
	}

	private void cleanUp() {
		try {
			dataWriter.close();
		} catch (IOException e) {
			throw ExceptionUtils.wrap(null, e);
		}
	}

	private class TransformerMatcher {
		private Pattern pattern;
		private Transformer transformer;

		TransformerMatcher(Pattern pattern, Transformer transformer) {
			this.pattern = pattern;
			this.transformer = transformer;
		}

		public Transformer getTransformer() {
			return transformer;
		}

		public boolean matches(String fullName) {
			return pattern.matcher(fullName).matches();
		}
	}
}
