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

import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.StyleFactoryService;
import org.geomajas.plugin.rasterizing.dto.GeometryLayerMetadata;
import org.geomajas.plugin.rasterizing.dto.LayerMetadata;
import org.geomajas.service.DtoConverterService;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
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

	public boolean canCreateLayer(MapContext mapContext, LayerMetadata metadata) {
		return metadata instanceof GeometryLayerMetadata;
	}

	public Layer createLayer(MapContext mapContext, LayerMetadata metadata) throws GeomajasException {
		GeometryLayerMetadata layerMetadata = (GeometryLayerMetadata) metadata;
		GeometryDirectLayer layer = new GeometryDirectLayer(styleFactoryService.createStyle(
				layerMetadata.getLayerType(), layerMetadata.getStyle()));
		for (Geometry geom : layerMetadata.getGeometries()) {
			layer.getGeometries().add(converterService.toInternal(geom));
		}
		return layer;
	}

}
