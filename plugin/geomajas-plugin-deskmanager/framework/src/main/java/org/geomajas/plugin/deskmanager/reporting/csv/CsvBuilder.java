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
package org.geomajas.plugin.deskmanager.reporting.csv;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Not threadsafe!
 * 
 * @author oji
 * 
 */
public class CsvBuilder {

	private static final String EOL = "\r\n";

	private final SimpleDateFormat dtformatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private final StringBuilder sb = new StringBuilder();

	private final String delimiter;

	private final String escapeChar;

	private boolean first = true;

	// ------------------------------------------------------------------

	/**
	 * default values: delimiter = ",", escapeChar = """.
	 */
	public CsvBuilder() {
		this(",", "\"");
	}

	public CsvBuilder(String delimiter, String escapeChar) {
		if (delimiter == null || "".equals(delimiter)) {
			this.delimiter = ",";
		} else {
			this.delimiter = delimiter;
		}

		if (escapeChar == null || "".equals(escapeChar)) {
			this.escapeChar = "\"";
		} else {
			if (escapeChar.length() > 1) {
				throw new IllegalArgumentException("Escapechar should only be one character");
			}
			this.escapeChar = escapeChar;
		}
	}

	// ------------------------------------------------------------------

	public void addField(String value) {
		addDelimiter();
		sb.append(escape(value));
	}

	public void addField(boolean value) {
		addDelimiter();
		sb.append(Boolean.toString(value));
	}

	public void addField(long value) {
		addDelimiter();
		sb.append(value);
	}

	public void addField(double value) {
		addDelimiter();
		sb.append(value);
	}

	public void addField(Date value) {
		addDelimiter();
		if (value != null) {
			sb.append(dtformatter.format(value));
		}
	}

	public void addField(Calendar value) {
		addDelimiter();
		if (value != null) {
			sb.append(dtformatter.format(value.getTime()));
		}
	}

	public void endRecord() {
		sb.append(EOL);
		first = true;
	}

	public StringBuilder getBuffer() {
		return sb;
	}

	// ------------------------------------------------------------------

	private void addDelimiter() {
		if (first) {
			first = false;
		} else {
			sb.append(delimiter);
		}
	}

	private String escape(String value) {
		if (value == null) {
			return "";
		} else if ("".equals(value)) {
			return escapeChar + escapeChar;
		} else {
			String tmp = value.replaceAll("\\" + escapeChar, escapeChar + escapeChar);
			if (tmp.contains(escapeChar) || tmp.contains(delimiter) || tmp.contains("\n")) {
				return escapeChar + tmp + escapeChar;
			} else {
				return value;
			}
		}
	}
}
