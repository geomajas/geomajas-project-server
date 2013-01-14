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
package org.geomajas.configuration.client;

import org.geomajas.annotation.Api;

/**
 * Unit type.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public enum UnitType {
	/**
	 * Metric Units.
	 * Meters(m) -- Kilometers(km)
	 */
	METRIC,

	/**
	 * English Units.
	 * Yards(yd) -- Miles(mi)
	 */
	ENGLISH,

	/**
	 * English Units.
	 * Feet(ft) -- Miles(mi)
	 */
	ENGLISH_FOOT,

	/**
	 * Coordinate Reference System Units.
	 * Units(u)
	 */
	CRS
}
