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
package org.geomajas.configuration.validation;

import org.geomajas.annotation.Api;

/**
 * The constrained string attribute must match the following regular expression. The regular expression follows the Java
 * regular expression conventions see java.util.regex.Pattern. Accepts String. null elements are considered valid.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class PatternConstraintInfo implements ConstraintInfo {

	private static final long serialVersionUID = 190L;

	private String regexp;

	/**
	 * Get the regular expression to match.
	 *
	 * @return regular expression
	 */
	public String getRegexp() {
		return regexp;
	}

	/**
	 * Set the regular expression to match.
	 *
	 * @param regexp regular expression
	 */
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
}
