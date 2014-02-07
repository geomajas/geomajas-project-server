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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;

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

	/**
	 * Set the attributes to include for readable status.
	 * <p/>
	 * Regex expressions can be used for the strings. You can make a rule feature specific by including "@" and the
	 * feature id (converted to a string, using a regex again).
	 *
	 * @return readable attributes
	 */
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
