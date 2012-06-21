/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.step;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.api.RenderingService;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.service.pipeline.PipelineContext;
import org.geotools.map.MapContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step which does the actual map rendering.
 * 
 * @author Jan De Moerloose
 */
public class RenderMapStep extends AbstractRasterizingStep {

	@Autowired
	private RenderingService renderingService;

	public void execute(PipelineContext context, RasterizingContainer response) throws GeomajasException {
		MapContext mapContext = context.get(RasterizingPipelineCode.MAP_CONTEXT_KEY, MapContext.class);
		RenderingHints renderingHints = context.get(RasterizingPipelineCode.RENDERING_HINTS, RenderingHints.class);
		@SuppressWarnings("unchecked")
		Map<Object, Object> rendererHints = context.get(RasterizingPipelineCode.RENDERER_HINTS, Map.class);
		Rectangle paintArea = mapContext.getViewport().getScreenArea();
		MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) mapContext.getUserData().get(
				LayerFactory.USERDATA_RASTERIZING_INFO);
		BufferedImage image = createImage(paintArea.width, paintArea.height, mapRasterizingInfo.isTransparent());
		Graphics2D graphics = getGraphics(image, mapRasterizingInfo.isTransparent(), renderingHints);
		renderingService.paintMap(mapContext, graphics, rendererHints);
		context.put(RasterizingPipelineCode.RENDERED_IMAGE, image);
	}

	private BufferedImage createImage(int width, int height, boolean transparent) {
		if (transparent) {
			return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		} else {
			// don't use alpha channel if the image is not transparent
			return new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		}
	}

	private Graphics2D getGraphics(BufferedImage image, boolean transparent, RenderingHints renderingHints) {
		Graphics2D graphics = image.createGraphics();
		Color bgColor = Color.WHITE;
		if (transparent) {
			int composite = AlphaComposite.DST;
			graphics.setComposite(AlphaComposite.getInstance(composite));
			Color c = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 0);
			graphics.setBackground(bgColor);
			graphics.setColor(c);
			graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
			composite = AlphaComposite.DST_OVER;
			graphics.setComposite(AlphaComposite.getInstance(composite));
		} else {
			graphics.setColor(bgColor);
			graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		}
		if (renderingHints != null) {
			graphics.setRenderingHints(renderingHints);
		}
		return graphics;
	}

}
