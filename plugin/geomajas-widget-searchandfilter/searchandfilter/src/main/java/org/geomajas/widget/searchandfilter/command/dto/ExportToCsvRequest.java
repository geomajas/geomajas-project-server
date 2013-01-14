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

import org.geomajas.command.LayerIdCommandRequest;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchFeatureRequest;

/**
 * Request for {@link org.geomajas.widget.searchandfilter.command.searchandfilter.ExportToCsvCommand}.
 * 
 * @author Kristof Heirwegh
 */
public class ExportToCsvRequest extends LayerIdCommandRequest implements CsvMetadata {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND = "command.searchandfilter.ExportToCsv";
	
	private SearchByLocationRequest searchByLocationRequest;
	private SearchFeatureRequest searchFeatureRequest;
	private FeatureSearchRequest searchByCriterionRequest;

	private String separatorChar;
	private String quoteChar;
	private String locale;
	private String filename;
	private boolean identifyingAttributesOnly;

	/**
	 * Encoding of CSV File. For instance: UTF-8 or, ISO8859_1. Default is
	 * UTF-8.
	 */
	private String encoding;

	public String getSeparatorChar() {
		return (separatorChar == null ? CsvMetadata.DEFAULT_SEPARATOR_CHAR : separatorChar);
	}

	public void setSeparatorChar(String separatorChar) {
		this.separatorChar = separatorChar;
	}

	public String getQuoteChar() {
		return (quoteChar == null ? CsvMetadata.DEFAULT_QUOTE_CHAR : quoteChar);
	}

	public void setQuoteChar(String quoteChar) {
		this.quoteChar = quoteChar;
	}

	public String getEncoding() {
		return (encoding == null ? CsvMetadata.DEFAULT_ENCODING : encoding);
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getLocale() {
		return (locale == null ? CsvMetadata.DEFAULT_LOCALE : locale);
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean useIdentifyingAttributesOnly() {
		return identifyingAttributesOnly;
	}

	public void setIdentifyingAttributesOnly(boolean identifyingAttributesOnly) {
		this.identifyingAttributesOnly = identifyingAttributesOnly;
	}

	public SearchByLocationRequest getSearchByLocationRequest() {
		return searchByLocationRequest;
	}

	public void setSearchByLocationRequest(SearchByLocationRequest searchByLocationRequest) {
		this.searchByLocationRequest = searchByLocationRequest;
	}

	public SearchFeatureRequest getSearchFeatureRequest() {
		return searchFeatureRequest;
	}

	public void setSearchFeatureRequest(SearchFeatureRequest searchFeatureRequest) {
		this.searchFeatureRequest = searchFeatureRequest;
	}

	public FeatureSearchRequest getSearchByCriterionRequest() {
		return searchByCriterionRequest;
	}

	public void setSearchByCriterionRequest(FeatureSearchRequest searchByCriterionRequest) {
		this.searchByCriterionRequest = searchByCriterionRequest;
	}
}
