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

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.StyleFactoryService;
import org.geomajas.plugin.rasterizing.command.dto.ClientGeometryLayerInfo;
import org.geomajas.service.DtoConverterService;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.styling.Style;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This factory creates a Geotools layer that is capable of writing geometries.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class GeometryLayerFactory implements LayerFactory {

	@Autowired
	private StyleFactoryService styleFactoryService;

	@Autowired
	private DtoConverterService converterService;

	public boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof ClientGeometryLayerInfo;
	}

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		ClientGeometryLayerInfo layerInfo = (ClientGeometryLayerInfo) clientLayerInfo;
		LayerType layerType = layerInfo.getLayerType();
		Style style = styleFactoryService.createStyle(layerType, layerInfo.getStyle());
		GeometryDirectLayer layer = new GeometryDirectLayer(style, converterService.toInternal(layerType));
		for (Geometry geom : layerInfo.getGeometries()) {
			layer.getGeometries().add(converterService.toInternal(geom));
		}
		layer.getUserData().put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
		layer.setTitle(layerInfo.getLabel());
		Map<String, FeatureStyleInfo> styles = new HashMap<String, FeatureStyleInfo>();
		styles.put(layerInfo.getStyle().getName(), layerInfo.getStyle());
		layer.getUserData().put(USERDATA_KEY_STYLES, styles);
		return layer;
	}

}
