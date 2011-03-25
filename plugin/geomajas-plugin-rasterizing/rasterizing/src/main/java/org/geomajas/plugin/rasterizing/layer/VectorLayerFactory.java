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
package org.geomajas.plugin.rasterizing.layer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.StyleFactoryService;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * This factory creates a Geotools layer that is capable of writing vector layers.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class VectorLayerFactory implements LayerFactory {

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private StyleFactoryService styleFactoryService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService dtoConverterService;

	public boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof ClientVectorLayerInfo;
	}

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		ClientVectorLayerInfo vectorInfo = (ClientVectorLayerInfo) clientLayerInfo;
		VectorLayerRasterizingInfo extraInfo = (VectorLayerRasterizingInfo) vectorInfo
				.getWidgetInfo(VectorLayerRasterizingInfo.WIDGET_KEY);
		ReferencedEnvelope areaOfInterest = mapContext.getAreaOfInterest();
		VectorLayer layer = configurationService.getVectorLayer(vectorInfo.getServerLayerId());
		Envelope layerBounds = geoService.transform(areaOfInterest,
				(Crs) areaOfInterest.getCoordinateReferenceSystem(), (Crs) layer.getCrs());
		Filter filter = filterService.createBboxFilter((Crs) layer.getCrs(), layerBounds, layer.getLayerInfo()
				.getFeatureInfo().getGeometryType().getName());
		if (extraInfo.getFilter() != null) {
			filter = filterService.createAndFilter(filter, filterService.parseFilter(extraInfo.getFilter()));
		}
		List<InternalFeature> features = vectorLayerService.getFeatures(vectorInfo.getServerLayerId(),
				mapContext.getCoordinateReferenceSystem(), filter, extraInfo.getStyle(),
				VectorLayerService.FEATURE_INCLUDE_ALL);
		FeatureLayer featureLayer = new FeatureLayer(createCollection(features, layer,
				mapContext.getCoordinateReferenceSystem()), styleFactoryService.createStyle(layer, extraInfo));
		featureLayer.getUserData().put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		LinkedHashMap<String, FeatureStyleInfo> styles = new LinkedHashMap<String, FeatureStyleInfo>();
		for (InternalFeature feature : features) {
			FeatureStyleInfo info = feature.getStyleInfo();
			styles.put(info.getName(), info);
		}
		featureLayer.getUserData().put(USERDATA_KEY_STYLES, styles);
		return featureLayer;
	}

	private FeatureCollection<SimpleFeatureType, SimpleFeature> createCollection(List<InternalFeature> features,
			VectorLayer layer, CoordinateReferenceSystem mapCrs) {
		SimpleFeatureType type = createFeatureType(layer, mapCrs);
		ListFeatureCollection result = new ListFeatureCollection(type);
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
		for (InternalFeature internalFeature : features) {
			Object[] values = new Object[internalFeature.getAttributes().size() + 1];
			int i = 0;
			for (AttributeInfo attrInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
				values[i++] = internalFeature.getAttributes().get(attrInfo.getName()).getValue();
			}
			values[internalFeature.getAttributes().size()] = internalFeature.getGeometry();
			result.add(builder.buildFeature(internalFeature.getId(), values));
		}
		return result;
	}

	private SimpleFeatureType createFeatureType(VectorLayer layer, CoordinateReferenceSystem mapCrs) {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		VectorLayerInfo info = layer.getLayerInfo();
		builder.setName(info.getFeatureInfo().getDataSourceName());
		builder.setCRS(mapCrs);
		for (AttributeInfo attrInfo : info.getFeatureInfo().getAttributes()) {
			if (attrInfo instanceof PrimitiveAttributeInfo) {
				PrimitiveAttributeInfo prim = (PrimitiveAttributeInfo) attrInfo;
				switch (prim.getType()) {
					case BOOLEAN:
						builder.add(prim.getName(), Boolean.class);
						break;
					case CURRENCY:
						builder.add(prim.getName(), BigDecimal.class);
						break;
					case DATE:
						builder.add(prim.getName(), Date.class);
						break;
					case DOUBLE:
						builder.add(prim.getName(), Double.class);
						break;
					case FLOAT:
						builder.add(prim.getName(), Float.class);
						break;
					case IMGURL:
						builder.add(prim.getName(), String.class);
						break;
					case INTEGER:
						builder.add(prim.getName(), Integer.class);
						break;
					case LONG:
						builder.add(prim.getName(), Long.class);
						break;
					case SHORT:
						builder.add(prim.getName(), Short.class);
						break;
					case STRING:
						builder.add(prim.getName(), String.class);
						break;
					case URL:
						builder.add(prim.getName(), String.class);
						break;

				}
			} else if (attrInfo instanceof AssociationAttributeInfo) {
				AssociationAttributeInfo ass = (AssociationAttributeInfo) attrInfo;
				switch (ass.getType()) {
					case MANY_TO_ONE:
						builder.add(ass.getName(), Object.class);
						break;
					case ONE_TO_MANY:
						builder.add(ass.getName(), Collection.class);
						break;
				}
			}
		}
		GeometryAttributeInfo geom = info.getFeatureInfo().getGeometryType();
		builder.add(geom.getName(), dtoConverterService.toInternal(info.getLayerType()), mapCrs);
		builder.setDefaultGeometry(geom.getName());
		return builder.buildFeatureType();
	}

}
