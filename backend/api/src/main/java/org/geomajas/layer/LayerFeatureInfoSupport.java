/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer;

import java.util.List;

import org.geomajas.global.Api;
import org.geomajas.global.UserImplemented;
import org.geomajas.layer.feature.Feature;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Extension for any type of layer that supports retrieving feature info. 
 * 
 * @author Oliver May
 * @since 1.9.0
 */
@Api(allMethods = true)
@UserImplemented
public interface LayerFeatureInfoSupport {
	
	/**
	 * Return the list of features that lay within a given coordinate and buffer for the given viewscale.
	 * @param coordinate coordinate used to search for features in the layer coordinate space
	 * @param scale the scale of the layer
	 * @param buffer a buffer round the coordinate in layer units
	 * @return features a list of features
	 * @throws LayerException oops
	 */
	List<Feature> getFeaturesByLocation(Coordinate coordinate, double layerScale, double buffer) throws LayerException;
	
}
