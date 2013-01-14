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

package org.geomajas.internal.layer;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Implementation of {@link org.geomajas.layer.RasterLayerService}, a service which allows accessing data from a
 * raster layer.
 * <p/>
 * All access to vector layers should be done through this service, not by accessing the layer directly as this
 * adds possible caching, security etc. These services are implemented using pipelines (see
 * {@link org.geomajas.service.pipeline.PipelineService}) to make them configurable.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class RasterLayerServiceImpl extends LayerServiceImpl implements RasterLayerService {

	private final Logger log = LoggerFactory.getLogger(RasterLayerServiceImpl.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private PipelineService pipelineService;

	private RasterLayer getRasterLayer(String layerId) throws GeomajasException {
		if (!securityContext.isLayerVisible(layerId)) {
			throw new GeomajasSecurityException(ExceptionCode.LAYER_NOT_VISIBLE, layerId, securityContext.getUserId());
		}
		RasterLayer layer = configurationService.getRasterLayer(layerId);
		if (null == layer) {
			throw new GeomajasException(ExceptionCode.RASTER_LAYER_NOT_FOUND, layerId);
		}
		return layer;
	}

	public List<RasterTile> getTiles(String layerId, CoordinateReferenceSystem crs, Envelope bounds, double scale)
			throws GeomajasException {
		log.debug("getTiles start on layer {}", layerId);
		long ts = System.currentTimeMillis();
		PipelineContext context = pipelineService.createContext();
		context.put(PipelineCode.LAYER_ID_KEY, layerId);
		RasterLayer layer = getRasterLayer(layerId);
		context.put(PipelineCode.LAYER_KEY, layer);
		context.put(PipelineCode.CRS_KEY, crs);
		context.put(PipelineCode.BOUNDS_KEY, bounds);
		context.put(PipelineCode.SCALE_KEY, scale);
		List<RasterTile> response = new ArrayList<RasterTile>();
		pipelineService.execute(PipelineCode.PIPELINE_GET_RASTER_TILES, layerId, context, response);
		log.debug("getTiles done on layer {}, time {}s", layerId, (System.currentTimeMillis() - ts) / 1000.0);
		return response;
	}
}
