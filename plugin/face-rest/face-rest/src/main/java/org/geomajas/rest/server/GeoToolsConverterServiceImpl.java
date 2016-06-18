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
package org.geomajas.rest.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.SyntheticAttributeInfo;
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
import org.opengis.geometry.aggregate.MultiPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Service to convert from Geomajas to GeoTools objects.
 *
 * @author Oliver May
 * @author Jan De Moerloose
 */
@Component
public class GeoToolsConverterServiceImpl implements GeoToolsConverterService {

	private static final String NAMESPACE_URI = "http://www.geomajas.org";

	private final Logger log = LoggerFactory.getLogger(GeoToolsConverterServiceImpl.class);

	@Autowired
	private GeoService geoservice;

	@Override
	public SimpleFeatureType toSimpleFeatureType(VectorLayerInfo vectorLayerInfo) throws LayerException {
		return toSimpleFeatureType(vectorLayerInfo, null);
	}

	@Override
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

	@Override
	public SimpleFeatureType toSimpleFeatureType(VectorLayerInfo vectorLayerInfo, List<String> attributeNames)
			throws LayerException {

		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

		builder.setName(vectorLayerInfo.getFeatureInfo().getDataSourceName());
		builder.setNamespaceURI(NAMESPACE_URI);
		builder.setCRS(geoservice.getCrs2(vectorLayerInfo.getCrs()));

		// create a lookup map of attribute info's
		Map<String, AbstractAttributeInfo> attrs = vectorLayerInfo.getFeatureInfo().getAttributesMap();
		if (attributeNames == null) {
			attributeNames = new ArrayList<String>(attrs.keySet());
		}

		// now list 'm up
		for (String name : attributeNames) {
			if (attrs.containsKey(name)) {
				AbstractAttributeInfo a = attrs.get(name);
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
						case STRING:
						case URL:
						case IMGURL:
							builder.add(attr.getName(), String.class);
							break;
						case DATE:
							builder.add(attr.getName(), Date.class);
							break;
						default:
							log.error("Don't know how to convert attribute of type " + attr.getType() + ", skipped, " +
									attr);
							break;
					}
				} else if (a instanceof SyntheticAttributeInfo) {
					SyntheticAttributeInfo attr = (SyntheticAttributeInfo) a;
					builder.add(attr.getName(), String.class);
				}
			}
		}
		switch (vectorLayerInfo.getLayerType()) {
			case POINT:
				builder.add(vectorLayerInfo.getFeatureInfo().getGeometryType().getName(), Point.class);
				break;
			case LINESTRING:
				builder.add(vectorLayerInfo.getFeatureInfo().getGeometryType().getName(), LineString.class);
				break;
			case POLYGON:
				builder.add(vectorLayerInfo.getFeatureInfo().getGeometryType().getName(), Polygon.class);
				break;
			case MULTIPOINT:
				builder.add(vectorLayerInfo.getFeatureInfo().getGeometryType().getName(), MultiPoint.class);
				break;
			case MULTILINESTRING:
				builder.add(vectorLayerInfo.getFeatureInfo().getGeometryType().getName(), MultiLineString.class);
				break;
			case MULTIPOLYGON:
				builder.add(vectorLayerInfo.getFeatureInfo().getGeometryType().getName(), MultiPolygon.class);
				break;
			default:
				builder.add(vectorLayerInfo.getFeatureInfo().getGeometryType().getName(), Geometry.class);
		}
		builder.setDefaultGeometry(vectorLayerInfo.getFeatureInfo().getGeometryType().getName());

		return builder.buildFeatureType();
	}
}
