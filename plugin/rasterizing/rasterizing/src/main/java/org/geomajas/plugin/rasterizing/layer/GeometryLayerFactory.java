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
package org.geomajas.plugin.rasterizing.layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.command.dto.ClientGeometryLayerInfo;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.styling.Style;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This factory creates a GeoTools layer that is capable of writing geometries.
 * 
 * @author Jan De Moerloose
 */
@Component
public class GeometryLayerFactory implements LayerFactory {

	@Autowired
	private StyleConverterService styleConverterService;

	@Autowired
	private DtoConverterService converterService;

	public boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof ClientGeometryLayerInfo;
	}

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		if (!(clientLayerInfo instanceof ClientGeometryLayerInfo)) {
			throw new IllegalArgumentException(
					"GeometryLayerFactory.createLayer() should only be called using ClientGeometryLayerInfo");
		}
		ClientGeometryLayerInfo layerInfo = (ClientGeometryLayerInfo) clientLayerInfo;
		LayerType layerType = layerInfo.getLayerType();
		Style style = styleConverterService.convert(layerInfo.getStyle());
		GeometryDirectLayer layer = new GeometryDirectLayer(style, converterService.toInternal(layerType));
		for (Geometry geom : layerInfo.getGeometries()) {
			layer.getGeometries().add(converterService.toInternal(geom));
		}
		layer.getUserData().put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
		layer.setTitle(layerInfo.getLabel());
		List<RuleInfo> ruleInfos = new ArrayList<RuleInfo>();
		// all rules are needed for map/legend
		for (FeatureTypeStyleInfo fts : layerInfo.getStyle().getFeatureTypeStyleList()) {
			for (RuleInfo rule : fts.getRuleList()) {
				ruleInfos.add(rule);
			}
		}
		layer.getUserData().put(USERDATA_KEY_STYLE_RULES, ruleInfos);
		return layer;
	}

	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		Map<String, Object> userData = new HashMap<String, Object>();
		ClientGeometryLayerInfo layerInfo = (ClientGeometryLayerInfo) clientLayerInfo;
		userData.put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
		return userData;
	}

}
