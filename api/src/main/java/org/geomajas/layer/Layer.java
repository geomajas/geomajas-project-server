/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.configuration.LayerInfo;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A layer which is is normally used as part of a map.
 * 
 * @param <T> type of layer configuration info
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface Layer<T extends LayerInfo> {

	/**
	 * Get information about the layer.
	 * 
	 * @return layer info
	 */
	T getLayerInfo();

	/**
	 * Get coordinate reference system for this layer.
	 *
	 * @return Coordinate reference system for this layer.
	 * @deprecated use {@link org.geomajas.layer.LayerService#getCrs(Layer)}
	 */
	@Deprecated
	CoordinateReferenceSystem getCrs();
	
	/**
	 * Get the unique id of this layer.
	 * 
	 * @return Unique id of the layer (auto-set to name of bean in Spring context)
	 */
	String getId();

}
