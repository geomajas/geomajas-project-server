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
package org.geomajas.rest.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.GeoService;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Oliver May
 * 
 */
@Component
public class GeotoolsConvertorServiceImpl implements GeotoolsConvertorService {

	private static String NAMESPACE_URI = "http://www.geomajas.org";

	@Autowired
	private GeoService geoservice;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.rest.server.GeotoolsConvertorServiceInterface#toSimpleFeatureType(org.geomajas.configuration.
	 * VectorLayerInfo)
	 */
	public SimpleFeatureType toSimpleFeatureType(VectorLayerInfo vectorLayerInfo) throws LayerException {

		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

		builder.setName(vectorLayerInfo.getFeatureInfo().getDataSourceName());
		builder.setNamespaceURI(NAMESPACE_URI);
		builder.setCRS(geoservice.getCrs(vectorLayerInfo.getCrs()));

		for (AttributeInfo a : vectorLayerInfo.getFeatureInfo().getAttributes()) {
			if (a instanceof PrimitiveAttributeInfo) {
				PrimitiveAttributeInfo attr = (PrimitiveAttributeInfo) a;
				switch (attr.getType()) {
					case BOOLEAN:
						builder.add(attr.getName(), Boolean.class);
						break;
					case SHORT:
						builder.add(attr.getName(), Short.class);
						break;
					case INTEGER:
						builder.add(attr.getName(), Integer.class);
						break;
					case LONG:
						builder.add(attr.getName(), Long.class);
						break;
					case FLOAT:
						builder.add(attr.getName(), Float.class);
						break;
					case DOUBLE:
						builder.add(attr.getName(), Double.class);
						break;
					case CURRENCY:
						builder.add(attr.getName(), String.class);
						break;
					case STRING:
						builder.add(attr.getName(), String.class);
						break;
					case DATE:
						builder.add(attr.getName(), Date.class);
						break;
					case URL:
						builder.add(attr.getName(), String.class);
						break;
					case IMGURL:
						builder.add(attr.getName(), String.class);
						break;
				}
			}
		}
		builder.add(vectorLayerInfo.getFeatureInfo().getGeometryType().getName(), Geometry.class);
		builder.setDefaultGeometry(vectorLayerInfo.getFeatureInfo().getGeometryType().getName());

		return builder.buildFeatureType();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.geomajas.rest.server.GeotoolsConvertorServiceInterface#toSimpleFeature(
	 * org.geomajas.layer.feature.InternalFeature
	 * , org.opengis.feature.simple.SimpleFeatureType)
	 */
	public SimpleFeature toSimpleFeature(InternalFeature feature, SimpleFeatureType featureType) {
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
		List<Object> attr = new ArrayList<Object>();

		for (AttributeDescriptor ads : featureType.getAttributeDescriptors()) {
			if (!ads.equals(featureType.getGeometryDescriptor())) {
				Attribute a = feature.getAttributes().get(ads.getName().getLocalPart());
				if (null != a) {
					attr.add(a.getValue());
				} else {
					attr.add(null);
				}
			} else {
				attr.add(feature.getGeometry());
			}
		}

		return builder.buildFeature(feature.getId(), attr.toArray());

	}
}
