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
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;



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
	 * Return the list of features that lay on a given coordinate for the given viewscale.
	 * @param coord coordinate used to search for features
	 * @param targetCrs Coordinate reference system used for bounds
	 * @param mapBounds the bounds of the map
	 * @return features a list of features
	 * @throws LayerException oops
	 */
	List<Feature> getFeaturseByLocation(Coordinate coord, CoordinateReferenceSystem targetCrs, Envelope mapBounds, 
			double scale) throws LayerException;
	
}
