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

package org.geomajas.geometry;

import org.geomajas.annotation.Api;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Implementation of a {@link CoordinateReferenceSystem} containing an id and delegating to another implementation.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public interface Crs extends CoordinateReferenceSystem {

	/**
	 * Get id or code for this CRS. For example "EPSG:900913".
	 *
	 * @return crs id
	 * */
	String getId();
}
