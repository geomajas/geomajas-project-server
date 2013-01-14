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
package org.geomajas.widget.searchandfilter.command.dto;

/**
 * Settings for how the csvfile should be rendered.
 *
 * @author Kristof Heirwegh
 */
public interface CsvMetadata {

	String DEFAULT_QUOTE_CHAR = "\"";
	String DEFAULT_SEPARATOR_CHAR = ",";
	String DEFAULT_LOCALE = "EN";
	String DEFAULT_ENCODING = "UTF-8";

	String getSeparatorChar();
	String getQuoteChar();
	String getEncoding();
	String getLocale();
	String getFilename();
	boolean useIdentifyingAttributesOnly();
}
