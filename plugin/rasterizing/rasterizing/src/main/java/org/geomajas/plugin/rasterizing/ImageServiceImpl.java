/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.api.RasterException;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ImageService}. Uses a pipeline to render the images.
 * 
 * @author Jan De Moerloose
 */
@Component
@Transactional
public class ImageServiceImpl implements ImageService {

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private PipelineService<RasterizingContainer> pipelineService;

	public void writeMap(Graphics2D graphics, ClientMapInfo clientMapInfo) throws GeomajasException {
		PipelineContext context = pipelineService.createContext();
		context.put(RasterizingPipelineCode.GRAPHICS_2D, graphics);
		callPipeline(clientMapInfo, context, RasterizingPipelineCode.PIPELINE_RASTERIZING_GET_MAP_IMAGE);
	}

	public void writeMap(OutputStream stream, ClientMapInfo clientMapInfo) throws GeomajasException {
		PipelineContext context = pipelineService.createContext();
		RasterizingContainer container = callPipeline(clientMapInfo, context,
				RasterizingPipelineCode.PIPELINE_RASTERIZING_GET_MAP_IMAGE);
		if (container.getImage().length != 0) {
			try {
				stream.write(container.getImage());
			} catch (IOException e) {
				throw new RasterException(e, RasterException.IMAGE_WRITING_FAILED);
			}
		}
	}

	public void writeLegend(OutputStream stream, ClientMapInfo clientMapInfo) throws GeomajasException {
		PipelineContext context = pipelineService.createContext();
		RasterizingContainer container = callPipeline(clientMapInfo, context,
				RasterizingPipelineCode.PIPELINE_RASTERIZING_GET_LEGEND_IMAGE);
		if (container.getImage().length != 0) {
			try {
				stream.write(container.getImage());
			} catch (IOException e) {
				throw new RasterException(e, RasterException.IMAGE_WRITING_FAILED);
			}
		}
	}

	private RasterizingContainer callPipeline(ClientMapInfo clientMapInfo, PipelineContext context, String pipelineKey)
			throws GeomajasException {
		DefaultMapContext mapContext = new DefaultMapContext();
		mapContext.setCoordinateReferenceSystem(geoService.getCrs2(clientMapInfo.getCrs()));
		MapRasterizingInfo mapInfo = (MapRasterizingInfo) clientMapInfo.getWidgetInfo(MapRasterizingInfo.WIDGET_KEY);
		mapContext.setAreaOfInterest(new ReferencedEnvelope(dtoConverterService.toInternal(mapInfo.getBounds()),
				mapContext.getCoordinateReferenceSystem()));
		RenderingHints renderingHints = new Hints();
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RasterizingContainer response = new RasterizingContainer();
		context.put(RasterizingPipelineCode.CLIENT_MAP_INFO_KEY, clientMapInfo);
		context.put(RasterizingPipelineCode.RENDERING_HINTS, renderingHints);
		Map<Object, Object> rendererHints = new HashMap<Object, Object>();
		if (mapInfo.getDpi() > 0) {
			rendererHints.put(StreamingRenderer.DPI_KEY, mapInfo.getDpi());
		}
		context.put(RasterizingPipelineCode.RENDERER_HINTS, rendererHints);
		context.put(RasterizingPipelineCode.MAP_CONTEXT_KEY, mapContext);
		pipelineService.execute(pipelineKey, null, context, response);
		mapContext.dispose();
		return response;
	}

}
