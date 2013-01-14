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

package org.geomajas.puregwt.client.map.feature;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.puregwt.client.map.layer.VectorLayer;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Simple getter for features within a vector layer. For test purposes only!
 * 
 * @author Pieter De Graef
 */
public class FeatureServiceStub {

	@Autowired
	private VectorLayerService service;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService converter;

	@SuppressWarnings("deprecation")
	public List<Feature> getAllFeatures(VectorLayer layer) {
		List<Feature> result = new ArrayList<Feature>();
		try {
			CoordinateReferenceSystem crs = geoService.getCrs(layer.getLayerInfo().getCrs());
			List<InternalFeature> features = service.getFeatures(layer.getServerLayerId(), crs, Filter.INCLUDE, layer
					.getLayerInfo().getNamedStyleInfo(), 11);
			for (InternalFeature feature : features) {
				org.geomajas.layer.feature.Feature dto = converter.toDto(feature);
				result.add(new FeatureImpl(dto, layer));
			}
		} catch (LayerException e) {
			Assert.fail();
		} catch (GeomajasException e) {
			Assert.fail();
		}
		return result;
	}
}