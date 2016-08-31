package com.wongnai.tools.de.service.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.wongnai.tools.de.model.Column;
import com.wongnai.tools.de.model.Table;
import com.wongnai.tools.de.service.SqlData;

/**
 * The default implementation of {@link SqlData}.
 *
 * @author Suparit Krityakien
 */
public class SqlDataImpl implements SqlData {
	private Table table;
	private PrintStream ps;
	private List<Function<Object, String>> formatters;
	private List<Object[]> buffer;

	public SqlDataImpl(Table table, OutputStream os) {
		this.table = table;
		this.ps = new PrintStream(os);
		this.formatters = createFormatters();
		this.buffer = new ArrayList<>();
	}

	private List<Function<Object, String>> createFormatters() {
		ArrayList<Function<Object, String>> all = new ArrayList<>();

		table.getColumns().forEach(c -> all.add(getFormatter(c)));

		return all;
	}

	private Function<Object, String> getFormatter(Column c) {
		switch (c.getType()) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.CLOB:
		case Types.LONGVARCHAR:
		case Types.NCHAR:
		case Types.NVARCHAR:
		case Types.NCLOB:
			return Formatters.STRING;
		case Types.DATE:
		case Types.TIMESTAMP:
			return Formatters.DATE;
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.BIGINT:
		case Types.FLOAT:
		case Types.REAL:
		case Types.DOUBLE:
		case Types.NUMERIC:
		case Types.DECIMAL:
		case Types.BIT:
		case Types.BOOLEAN:
		default:
			return Formatters.NORMAL;
		}
	}

	@Override
	public void addRow(Object[] row) {
		buffer.add(row);

		if (buffer.size() == 200) {
			write();
		}
	}

	private void write() {
		int count = buffer.size();

		if (count > 0) {
			ps.println("INSERT IGNORE INTO " + table.getName() + "(" + table.getColumns().getSelectionText("")
					+ ") VALUES");

			for (int i = 0; i < count; i++) {
				writeRow(buffer.get(i));
				if (i == count - 1) {
					ps.println(";");
				} else {
					ps.println(",");
				}
			}
			buffer.clear();
		}
	}

	private void writeRow(Object[] row) {
		ps.print("(");
		ps.print(formatters.get(0).apply(row[0]));
		for (int i = 1; i < row.length; i++) {
			ps.print(",");
			ps.print(formatters.get(i).apply(row[i]));
		}
		ps.print(")");
	}

	@Override
	public void flush() throws IOException {
		write();
		ps.flush();
	}

	private static class Formatters {
		public static final Function<Object, String> NORMAL = o -> {
			if (o != null) {
				return o.toString();
			} else {
				return null;
			}
		};
		public static final Function<Object, String> STRING = new Function<Object, String>() {
			@Override
			public String apply(Object o) {
				if (o != null) {
					String text = o.toString();
					StringBuilder sb = new StringBuilder(text.length());
					sb.append("'");
					for (int i = 0; i < text.length(); i++) {
						char c = text.charAt(i);
						switch (c) {
						case '\\':
						case '%':
						case '_':
							sb.append("\\");
							sb.append(c);
							break;
						case '\'':
							sb.append("''");
							break;
						case '\r':
							sb.append("\\r");
							break;
						case '\t':
							sb.append("\\t");
							break;
						case '\n':
							sb.append("\\n");
							break;
						case '\0':
							sb.append("\\0");
							break;
						default:
							sb.append(c);
						}
					}
					sb.append("'");
					return sb.toString();
				} else {
					return null;
				}
			}
		};
		public static final Function<Object, String> DATE = new Function<Object, String>() {
			private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

			@Override
			public String apply(Object o) {
				if (o != null) {
					return "'"
							+ LocalDateTime.ofInstant(((Date) o).toInstant(), ZoneId.systemDefault()).format(formatter)
							+ "'";
				} else {
					return null;
				}
			}
		};
	}
}
