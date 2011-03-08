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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.api.RasterException;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.dto.LegendMetadata;
import org.geomajas.plugin.rasterizing.dto.MapMetadata;
import org.geomajas.plugin.rasterizing.legend.LegendBuilder;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link ImageService}. Uses a pipeline to render the images.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class ImageServiceImpl implements ImageService {

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private PipelineService<RasterizingContainer> pipelineService;

	public void writeMap(OutputStream stream, MapMetadata mapMetadata) throws GeomajasException {
		PipelineContext context = pipelineService.createContext();
		DefaultMapContext mapContext = new DefaultMapContext();
		mapContext.setCoordinateReferenceSystem(geoService.getCrs2(mapMetadata.getCrs()));
		mapContext.setAreaOfInterest(new ReferencedEnvelope(dtoConverterService.toInternal(mapMetadata.getBounds()),
				mapContext.getCoordinateReferenceSystem()));
		RenderingHints renderingHints = new Hints();
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RasterizingContainer response = new RasterizingContainer();
		context.put(RasterizingPipelineCode.MAP_CONTEXT_METADATA_KEY, mapMetadata);
		context.put(RasterizingPipelineCode.RENDERING_HINTS, renderingHints);
		context.put(RasterizingPipelineCode.MAP_CONTEXT_KEY, mapContext);
		pipelineService.execute(RasterizingPipelineCode.PIPELINE_RASTERIZING_GET_IMAGE, null, context, response);
		mapContext.dispose();
		try {
			stream.write(response.getImage());
		} catch (IOException e) {
			throw new RasterException(RasterException.IMAGE_WRITING_FAILED, e);
		}
	}

	public void writeLegend(OutputStream stream, LegendMetadata legendMetadata) throws GeomajasException {
		LegendBuilder renderer = new LegendBuilder();
		JComponent c = renderer.buildComponentTree(legendMetadata);
		BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();
		RenderingHints renderingHints = new Hints();
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHints(renderingHints);
		c.print(graphics);
		try {
			ImageIO.write(image, "PNG", stream);
		} catch (IOException e) {
			throw new RasterException(RasterException.IMAGE_WRITING_FAILED, e);
		}

	}

}
