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

package org.geomajas.global;

/**
 * Constants which are used in Geomajas (and which are not local to a class).
 * <p/>
 * This is specified as normal class (instead of an interface), to enable GWT use.
 *
 * @author Joachim Van der Auwera
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
	 * Dummy interface implementation to keep GWT happy.
	 */
	public static class GeomajasConstantImpl implements GeomajasConstant {

		/**
		 * Dummy method to keep checkstyle from complaining.
		 */
		public void dummy() {
			// nothing to do, just to keep checkstyle busy
		}

	}
}
