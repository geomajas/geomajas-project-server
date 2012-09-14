/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

/**
 * Only singlechar quote and separator allowed. Doubles needs to be '.' as decimalchar, not configurable atm
 * 
 * @author Oliver May
 * 
 * @param <T> returntype
 */
public class CsvReader<T> extends Reader {

	private char quoteChar = '"';

	private char separatorChar = ',';

	private char[] searchChars = new char[] { quoteChar, separatorChar, CharUtils.CR, CharUtils.LF };

	private DateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy"); // American order

	private BufferedReader reader;

	private ObjectBuilder<T> builder;

	private boolean eof;

	/**
	 * @param reader for instance a fileReader, will be wrapped in a bufferedReader if not already passed as one
	 * @param builder class that knows the content of the csv files and can read the necessary fields
	 * @param quoteChar
	 * @param separatorChar
	 * @param ignoreLines
	 * @param dateFormatter
	 */
	public CsvReader(Reader reader, ObjectBuilder<T> builder, String quoteChar, String separatorChar, int ignoreLines,
			DateFormat dateFormatter) {
		this(reader, builder);
		if (quoteChar == null || separatorChar == null) {
			throw new IllegalArgumentException("Please provide quote and separatorChar");
		}
		if (quoteChar.length() != 1 || separatorChar.length() != 1) {
			throw new IllegalArgumentException("Only singleChar quotes and separator are allowed");
		}
		this.quoteChar = quoteChar.charAt(0);
		this.separatorChar = separatorChar.charAt(0);
		this.searchChars[0] = quoteChar.charAt(0);
		this.searchChars[1] = separatorChar.charAt(0);
		this.dateFormatter = dateFormatter;

		for (int i = 0; i < ignoreLines; i++) {
			try {
				if (this.reader.readLine() == null) {
					break;
				}
			} catch (Exception e) {
				throw new RuntimeException("Fout bij lezen van CSV-bestand: (" + e.getMessage() + ")");
			}
		}
	}

	/**
	 * @param reader for instance a fileReader
	 * @param builder class that knows the content of the csv files and can read the necessary fields
	 */
	public CsvReader(Reader reader, ObjectBuilder<T> builder) {
		this(reader);
		this.builder = builder;
	}

	public CsvReader(Reader reader) {
		if (reader == null) {
			throw new IllegalArgumentException("Please provide a reader");
		}
		if (reader instanceof BufferedReader) {
			this.reader = (BufferedReader) reader;
		} else {
			this.reader = new BufferedReader(reader);
		}
	}

	// ------------------------------------------------------------------

	@Override
	public void close() throws IOException {
		reader.close();
		eof = true;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		throw new NotImplementedException();
	}

	// ------------------------------------------------------------------

	public T readObject() throws IOException {
		if (builder == null) {
			throw new IllegalStateException("No objectbuilder specified");
		}
		return (eof ? null : builder.readObject(this));
	}

	/**
	 * @return "" if empty field, null if EOF
	 * @throws IOException
	 */
	public String readString() throws IOException {
		if (eof) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		char cur = readChar();
		if (!eof) {
			// -- escaped
			if (cur == quoteChar) {
				// read until ending quote
				while (true) {
					cur = readChar();
					if (!eof) {
						if (cur == quoteChar) {
							cur = readChar();
							if (eof || cur == separatorChar) {
								return sb.toString();
							} else if (cur == CharUtils.CR || cur == CharUtils.LF) {
								reader.mark(2);
								cur = readChar();
								if (!eof && cur != CharUtils.CR && cur != CharUtils.LF) {
									reader.reset();
								}
								return sb.toString();
							} else {
								sb.append(cur);
							}
						} else {
							sb.append(cur);
						}
					} else {
						return sb.toString();
					}
				}

				// -- not escaped
			} else {
				// Don't lose already read char;
				if (cur == separatorChar) {
					return "";
				} else if (cur == CharUtils.CR || cur == CharUtils.LF) {
					reader.mark(2);
					cur = readChar();
					if (!eof && cur != CharUtils.CR && cur != CharUtils.LF) {
						reader.reset();
					}
					return "";
				} else {
					sb.append(cur);
				}

				// read until breakchar
				while (true) {
					cur = readChar();
					if (!eof) {
						if (cur == separatorChar) {
							return sb.toString();
						} else if (cur == CharUtils.CR || cur == CharUtils.LF) {
							reader.mark(2);
							cur = readChar();
							if (!eof && cur != CharUtils.CR && cur != CharUtils.LF) {
								reader.reset();
							}
							return sb.toString();
						} else {
							sb.append(cur);
						}
					} else {
						return sb.toString();
					}
				}
			}
		} else {
			return "";
		}
	}

	/**
	 * always "." as decimal separator.
	 * 
	 * @return
	 * @throws IOException
	 */
	public Double readDouble() throws IOException {
		String res = readString();
		if (res == null) {
			return null;
		}
		res = res.trim();
		if ("".equals(res)) {
			return 0D;
		}
		return Double.valueOf(res);
	}

	public Long readLong() throws IOException {
		String res = readString();
		if (res == null) {
			return null;
		}
		res = res.trim();
		if ("".equals(res)) {
			return 0L;
		}
		return Long.valueOf(res);
	}

	public Integer readInt() throws IOException {
		String res = readString();
		if (res == null) {
			return null;
		}
		res = res.trim();
		if ("".equals(res)) {
			return 0;
		}
		return Integer.valueOf(res);
	}

	public Date readDate() throws IOException {
		if (dateFormatter == null) {
			throw new IllegalStateException("Dateformatter is not set");
		}
		String res = readString();
		if (res == null) {
			return null;
		}
		try {
			return dateFormatter.parse(res);
		} catch (Exception e) {
			throw new IOException("Could not parse datevalue. (" + res + ")");
		}
	}

	public String readLine() throws IOException {
		String tmp = reader.readLine();
		if (tmp == null) {
			eof = true;
		}
		return tmp;
	}

	public char peekChar() throws IOException {
		reader.mark(10);
		char next = readChar();
		reader.reset();
		return next;
	}

	public boolean isEol() throws IOException {
		char next = peekChar();
		if (eof) {
			return true;
		}

		return (next == CharUtils.CR || next == CharUtils.LF);
	}

	private char readChar() throws IOException {
		int c = reader.read();
		if (c == -1) {
			eof = true;
		}
		return (char) c;
	}

	// ------------------------------------------------------------------

	/**
	 * read the necessary fields WARNING: Don't forget to read unused fields!
	 */
	public interface ObjectBuilder<T> {

		T readObject(CsvReader<T> reader) throws IOException;
	}

	// ------------------------------------------------------------------
	// sadly StringEscapeUtils does not allow for a custom separator
	// ------------------------------------------------------------------

	void unescapeCsv(Writer out, String str) throws IOException {
		if (str == null) {
			return;
		}
		if (str.length() < 2) {
			out.write(str);
			return;
		}
		if (str.charAt(0) != quoteChar || str.charAt(str.length() - 1) != quoteChar) {
			out.write(str);
			return;
		}

		// strip quotes
		String quoteless = str.substring(1, str.length() - 1);

		if (StringUtils.containsAny(quoteless, searchChars)) {
			str = StringUtils.replace(quoteless, "" + quoteChar + quoteChar, "" + quoteChar);
		}

		out.write(str);
	}

	void escapeCsv(Writer out, String str) throws IOException {
		if (StringUtils.containsNone(str, searchChars)) {
			if (str != null) {
				out.write(str);
			}
			return;
		}
		out.write(quoteChar);
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == quoteChar) {
				out.write(quoteChar); // escape double quote
			}
			out.write(c);
		}
		out.write(quoteChar);
	}

	public String unescapeCsv(String str) {
		if (str == null) {
			return null;
		}
		try {
			StringWriter writer = new StringWriter();
			unescapeCsv(writer, str);
			return writer.toString();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	public String escapeCsv(String str) {
		if (StringUtils.containsNone(str, searchChars)) {
			return str;
		}
		try {
			StringWriter writer = new StringWriter();
			escapeCsv(writer, str);
			return writer.toString();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

}
