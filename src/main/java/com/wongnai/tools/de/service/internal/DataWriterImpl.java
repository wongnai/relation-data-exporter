package com.wongnai.tools.de.service.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.wongnai.tools.de.ExceptionUtils;
import com.wongnai.tools.de.model.Table;
import com.wongnai.tools.de.service.DataWriter;
import com.wongnai.tools.de.service.SqlData;

/**
 * The default implementation of {@link DataWriter}.
 *
 * @author Suparit Krityakien
 */
public class DataWriterImpl implements DataWriter {
	private Map<Table, SqlData> sqlDataMap = new HashMap<>();
	private String output;
	private OutputStream os;

	/**
	 * Constructs an instance.
	 *
	 * @param output
	 *            output
	 */
	public DataWriterImpl(String output) {
		this.output = output;
	}

	@Override
	public void writeRow(Table table, Object[] row) {
		SqlData sqlData = sqlDataMap.get(table);

		if (sqlData == null) {
			sqlData = new SqlDataImpl(table, getOutputStream());
			sqlDataMap.put(table, sqlData);
		}

		sqlData.addRow(row);
	}

	private OutputStream getOutputStream() {
		if (os == null) {
			if (output == null) {
				return System.out;
			} else {
				try {
					File f = new File(output);
					if (!f.exists()) {
						File p = f.getParentFile();
						if (!p.exists()) {
							p.mkdirs();
						}
					}
					os = new FileOutputStream(f);

					writeHeader();
				} catch (FileNotFoundException e) {
					throw ExceptionUtils.wrap(null, e);
				}
			}
		}
		return os;
	}

	private void writeHeader() {
		PrintStream ps = new PrintStream(os);

		ps.println("SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";");
		ps.println("SET time_zone = \"+00:00\";");
		ps.println("SET NAMES utf8mb4;");
		ps.println("SET FOREIGN_KEY_CHECKS=0;");
	}

	@Override
	public void close() throws IOException {
		sqlDataMap.values().forEach((sqlDataFile) -> {
			try {
				sqlDataFile.flush();
			} catch (IOException e) {
				throw ExceptionUtils.wrap(null, e);
			}
		});

		if (os != null) {
			writeFooter();
			os.close();
		}
	}

	private void writeFooter() {
		PrintStream ps = new PrintStream(os);

		ps.println("SET FOREIGN_KEY_CHECKS=1;");
	}

}
