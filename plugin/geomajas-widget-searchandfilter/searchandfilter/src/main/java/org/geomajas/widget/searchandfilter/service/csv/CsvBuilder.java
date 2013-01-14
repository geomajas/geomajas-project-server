/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.service.csv;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.geomajas.widget.searchandfilter.command.dto.CsvMetadata;

/**
 * Not threadsafe!
 *
 * @author Kristof Heirwegh
 */
public class CsvBuilder {

	private static final String EOL = "\r\n";

	private final DateFormat dateFormatter;
	private final NumberFormat numberFormatter;
	private final StringBuilder sb = new StringBuilder();
	private final String separatorChar;
	private final String escapeChar;

	private boolean first = true;

	// ------------------------------------------------------------------

	/**
	 * Default values: delimiter = ";", locale = "EN", escapeChar = "\"".
	 */
	public CsvBuilder() {
		this.separatorChar = ";";
		this.escapeChar = "\"";
		this.dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.ENGLISH);
		this.numberFormatter = DecimalFormat.getNumberInstance(Locale.ENGLISH);
	}

	public CsvBuilder(CsvMetadata meta) {
		if (meta == null) {
			throw new IllegalArgumentException("Meta cannot be null");
		}
		this.separatorChar =  meta.getSeparatorChar();
		this.escapeChar = meta.getQuoteChar();
		Locale locale = new Locale(meta.getLocale());
		this.dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
		this.numberFormatter = DecimalFormat.getNumberInstance(locale);
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
		sb.append(numberFormatter.format(value));
	}

	public void addField(Date value) {
		addDelimiter();
		if (value != null) {
			sb.append(dateFormatter.format(value));
		}
	}

	public void addField(Calendar value) {
		addDelimiter();
		if (value != null) {
			sb.append(dateFormatter.format(value.getTime()));
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
			sb.append(separatorChar);
		}
	}

	private String escape(String value) {
		if (value == null) {
			return "";
		} else if ("".equals(value)) {
			return escapeChar + escapeChar;
		} else {
			String tmp = value.replaceAll("\\" + escapeChar, escapeChar + escapeChar);
			if (tmp.contains(escapeChar) || tmp.contains(separatorChar) || tmp.contains("\n")) {
				return escapeChar + tmp + escapeChar;
			} else {
				return value;
			}
		}
	}
}
