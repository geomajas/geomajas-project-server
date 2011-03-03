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
package org.geomajas.plugin.rasterizing;

import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.dto.LayerMetadata;
import org.geomajas.plugin.rasterizing.dto.RasterLayerMetadata;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.map.MapViewport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This factory creates a Geotools layer that is capable of rendering raster layers.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component("RasterLayerFactory")
public class RasterLayerFactory implements LayerFactory {

	@Autowired
	private RasterLayerService rasterLayerService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private ConfigurationService configurationService;

	public boolean canCreateLayer(MapContext mapContext, LayerMetadata metadata) {
		return metadata instanceof RasterLayerMetadata;
	}

	public Layer createLayer(MapContext mapContext, LayerMetadata metadata) throws GeomajasException {
		RasterLayerMetadata layerMetadata = (RasterLayerMetadata) metadata;
		ReferencedEnvelope areaOfInterest = mapContext.getAreaOfInterest();
		RasterLayer layer = configurationService.getRasterLayer(metadata.getLayerId());
		MapViewport port = mapContext.getViewport();
		double rasterScale = port.getScreenArea().getWidth() / port.getBounds().getWidth();
		List<RasterTile> tiles = rasterLayerService.getTiles(metadata.getLayerId(),
				areaOfInterest.getCoordinateReferenceSystem(), areaOfInterest, rasterScale);
		RasterDirectLayer rasterLayer = new RasterDirectLayer(tiles, layer.getLayerInfo().getTileWidth(), layer
				.getLayerInfo().getTileHeight(), layerMetadata.getRasterStyle());
		return rasterLayer;
	}

}
