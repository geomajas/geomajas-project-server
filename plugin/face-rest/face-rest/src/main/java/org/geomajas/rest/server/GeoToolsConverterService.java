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
package org.geomajas.rest.server;

import java.util.List;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.InternalFeature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 * @author Oliver May
 * 
 */
public interface GeoToolsConverterService {

	SimpleFeatureType toSimpleFeatureType(VectorLayerInfo vectorLayerInfo) throws LayerException;

	SimpleFeatureType toSimpleFeatureType(VectorLayerInfo vectorLayerInfo, List<String> attributeNames)
			throws LayerException;

	SimpleFeature toSimpleFeature(InternalFeature feature, SimpleFeatureType featureType);

}