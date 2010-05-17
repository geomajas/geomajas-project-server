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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.global.Api;

import java.util.List;

/**
 * Attribute authorization info for a specific layer.
 * <p/>
 * You can specify a list a includes and excludes. Anything which is not included in not authorized. Anything which is
 * included is only authorized if it is not excluded.
 * <p/>
 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the feature
 * id (converted to a string, using a regex again).
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerAttributeAuthorizationInfo {
	private List<String> readableIncludes;
	private List<String> readableExcludes;
	private List<String> writableIncludes;
	private List<String> writableExcludes;

	public List<String> getReadableIncludes() {
		return readableIncludes;
	}

	/**
	 * Set the attributes to include for readable status.
	 * <p/>
	 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the
	 * feature id (converted to a string, using a regex again).
	 *
	 * @param readableIncludes readable attributes
	 */
	public void setReadableIncludes(List<String> readableIncludes) {
		this.readableIncludes = readableIncludes;
	}

	/**
	 * Get the attributes to include for readable status.
	 * <p/>
	 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the
	 * feature id (converted to a string, using a regex again).
	 *
	 * @return readable attributes
	 */
	public List<String> getReadableExcludes() {
		return readableExcludes;
	}

	/**
	 * Set the attributes to exclude from readable status.
	 * <p/>
	 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the
	 * feature id (converted to a string, using a regex again).
	 *
	 * @param readableExcludes attributes to exclude
	 */
	public void setReadableExcludes(List<String> readableExcludes) {
		this.readableExcludes = readableExcludes;
	}

	/**
	 * Get the attributes to include for writable status.
	 * <p/>
	 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the
	 * feature id (converted to a string, using a regex again).
	 *
	 * @return writable attributes
	 */
	public List<String> getWritableIncludes() {
		return writableIncludes;
	}

	/**
	 * Set the attributes to include for writable status.
	 * <p/>
	 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the
	 * feature id (converted to a string, using a regex again).
	 *
	 * @param writableIncludes attributes to include for writable status
	 */
	public void setWritableIncludes(List<String> writableIncludes) {
		this.writableIncludes = writableIncludes;
	}

	/**
	 * Get the attributes which should be excluded from writable status.
	 * <p/>
	 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the
	 * feature id (converted to a string, using a regex again).
	 *
	 * @return attributes to exclude from writable status
	 */
	public List<String> getWritableExcludes() {
		return writableExcludes;
	}

	/**
	 * Set the attributes which should be excluded from writable status.
	 * <p/>
	 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the
	 * feature id (converted to a string, using a regex again).
	 * 
	 * @param writableExcludes attributes to exclude from writing
	 */
	public void setWritableExcludes(List<String> writableExcludes) {
		this.writableExcludes = writableExcludes;
	}
}
