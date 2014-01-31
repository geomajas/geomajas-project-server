/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.rest.server.mvc;

import org.springframework.core.convert.converter.Converter;

/**
 * Converts a comma-separated string to an array of strings.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StringToStringArrayConverter implements Converter<String, String[]> {

	private String regexSplitter = "[\\s,]+";

	public String[] convert(String source) {

		return source.trim().split(regexSplitter);
	}

	public String getRegexSplitter() {
		return regexSplitter;
	}

	public void setRegexSplitter(String regexSplitter) {
		this.regexSplitter = regexSplitter;
	}

}
