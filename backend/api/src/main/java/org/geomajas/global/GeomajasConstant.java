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

package org.geomajas.global;

import org.geomajas.annotation.Api;

/**
 * Constants which are used in Geomajas (and which are not local to a class).
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface GeomajasConstant {

	/**
	 * Value to use when all aspects of the Feature should be lazy loaded.
	 */
	int FEATURE_INCLUDE_NONE = 0;

	/**
	 * Include attributes in the {@link org.geomajas.layer.feature.Feature}. (speed issue)
	 */
	int FEATURE_INCLUDE_ATTRIBUTES = 1;

	/**
	 * Include geometries in the {@link org.geomajas.layer.feature.Feature}. (speed issue)
	 */
	int FEATURE_INCLUDE_GEOMETRY = 2;

	/**
	 * Include style definitions in the {@link org.geomajas.layer.feature.Feature}. (speed issue)
	 */
	int FEATURE_INCLUDE_STYLE = 4;

	/**
	 * Include label string in the {@link org.geomajas.layer.feature.Feature}. (speed issue)
	 */
	int FEATURE_INCLUDE_LABEL = 8;

	/**
	 * The Features should include all aspects.
	 */
	int FEATURE_INCLUDE_ALL = FEATURE_INCLUDE_ATTRIBUTES + FEATURE_INCLUDE_GEOMETRY + FEATURE_INCLUDE_STYLE +
			FEATURE_INCLUDE_LABEL;

	/**
	 * URL prefix for classpath resources.
	 *
	 * @since 1.11.0
	 */
	String CLASSPATH_URL_PREFIX = "classpath:";

	/**
	 * Dummy interface implementation to keep GWT happy.
	 */
	class GeomajasConstantImpl implements GeomajasConstant {

		/**
		 * Dummy method to keep checkstyle from complaining.
		 */
		public void dummy() {
			// nothing to do, just to keep checkstyle busy
		}

	}
}
