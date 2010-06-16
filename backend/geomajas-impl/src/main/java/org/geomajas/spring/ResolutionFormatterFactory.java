/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.spring;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.geomajas.global.ResolutionFormat;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 * Factory that creates formatters for scales, so that values such as (1:x) are interpreted correctly.
 * 
 * @author Pieter De Graef
 * @since 1.7.0
 */
public class ResolutionFormatterFactory implements AnnotationFormatterFactory<ResolutionFormat> {

	@SuppressWarnings("unchecked")
	public Set<Class<?>> getFieldTypes() {
		HashSet<Class<?>> set = new HashSet<Class<?>>();
		Collections.addAll(set, Double.class);
		return set;
	}

	/**
	 * Create a printer that turns the given number object into a string.
	 * 
	 * @param annotation
	 *            The ScaleFormat annotation
	 * @param fieldType
	 *            The field type the returned printer should be able to handle.
	 * @return Returns a printer that simply calls the toString method.
	 */
	public Printer<Number> getPrinter(ResolutionFormat annotation, Class<?> fieldType) {
		return new Printer<Number>() {

			public String print(Number object, Locale locale) {
				return "1:" + object.toString();
			}
		};
	}

	/**
	 * Create a parser that parses the string scale representation (1:x) into a number (x).
	 * 
	 * @param annotation
	 *            The ScaleFormat annotation that is used to depict where this parser should be used.
	 * @param fieldType
	 *            The number field type - not used.
	 * @return Returns the parser
	 */
	public Parser<Number> getParser(ResolutionFormat annotation, Class<?> fieldType) {
		return new Parser<Number>() {

			public Number parse(String text, Locale locale) throws ParseException {
				return parseResolution(text);
			}
		};
	}

	/**
	 * Static method that parses a resolution (1:x format) as a double (1/x).
	 * 
	 * @param text
	 *            The resolution in 1:x format.
	 * @return Returns the double value of 1/x.
	 * @throws ParseException
	 *             Thrown when something is wrong in the expected format.
	 */
	public static double parseResolution(String text) throws ParseException {
		// Prepare the string; remove all spaces and all comma's:
		text = text.replaceAll(" ", "");
		text = text.replaceAll(",", "");

		int pos = text.indexOf(':');
		if (pos > 0) {
			try {
				double resolution = Double.parseDouble(text.substring(pos + 1));
				double scale = 0;
				if (resolution != 0) {
					scale = 1 / resolution;
				}
				return scale;
			} catch (Exception e) {
				throw new ParseException("Resolution could not be parsed. The following format was expected:"
						+ " (1:x). Underlying error: " + e.getMessage(), 1);
			}
		} else {
			try {
				// Not recommended....
				return Double.parseDouble(text);
			} catch (Exception e) {
				throw new ParseException("Resolution could not be parsed. The following format was expected:"
						+ " (1:x). Underlying error: " + e.getMessage(), 1);
			}
		}
	}
}