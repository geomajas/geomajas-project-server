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
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.command.dto.ClientWorldPaintableLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.WorldPaintableInfo;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.ResourceService;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.RuleInfo;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This factory creates a GeoTools layer that is capable of writing world paintables.
 * 
 * @author Jan De Moerloose
 */
@Component
public class WorldPaintableLayerFactory implements LayerFactory {

	@Autowired
	private StyleConverterService styleConverterService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private GeoService geoService;

	public boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof ClientWorldPaintableLayerInfo;
	}

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		if (!(clientLayerInfo instanceof ClientWorldPaintableLayerInfo)) {
			throw new IllegalArgumentException(
					"WorldPaintableLayerFactory.createLayer() should only be called" +
					" using ClientWorldPaintableLayerInfo");
		}
		ClientWorldPaintableLayerInfo layerInfo = (ClientWorldPaintableLayerInfo) clientLayerInfo;
		WorldPaintableDirectLayer layer = new WorldPaintableDirectLayer(layerInfo.getPaintables(), this);
		layer.getUserData().put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
		layer.setTitle(layerInfo.getLabel());
		List<RuleInfo> ruleInfos = new ArrayList<RuleInfo>();
		// all rules are needed for map/legend
		for (WorldPaintableInfo wp : layerInfo.getPaintables()) {
			if (wp.isShowInLegend()) {
				RuleInfo rule = new RuleInfo();
				rule.getSymbolizerList().add(wp.getGeometrySymbolizerInfo());
				rule.getSymbolizerList().add(wp.getLabelSymbolizerInfo());
				rule.setTitle(wp.getLegendTitle());
				ruleInfos.add(rule);
			}
		}
		layer.getUserData().put(USERDATA_KEY_STYLE_RULES, ruleInfos);
		return layer;
	}

	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		Map<String, Object> userData = new HashMap<String, Object>();
		ClientWorldPaintableLayerInfo layerInfo = (ClientWorldPaintableLayerInfo) clientLayerInfo;
		userData.put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
		return userData;
	}

	public StyleConverterService getStyleConverterService() {
		return styleConverterService;
	}

	public DtoConverterService getConverterService() {
		return converterService;
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public GeoService getGeoService() {
		return geoService;
	}

}
