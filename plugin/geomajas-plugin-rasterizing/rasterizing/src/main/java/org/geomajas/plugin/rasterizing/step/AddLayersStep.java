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
package org.geomajas.plugin.rasterizing.step;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactoryService;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.dto.LayerMetadata;
import org.geomajas.plugin.rasterizing.dto.MapMetadata;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineContext;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step which adds all the layers to the map context and prepares the context for rendering.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AddLayersStep extends AbstractRasterizingStep {

	@Autowired
	private LayerFactoryService layerFactoryService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	public void execute(PipelineContext context, RasterizingContainer response) throws GeomajasException {
		MapMetadata mapMetadata = context.get(RasterizingPipelineCode.MAP_CONTEXT_METADATA_KEY, MapMetadata.class);
		MapContext mapContext = context.get(RasterizingPipelineCode.MAP_CONTEXT_KEY, MapContext.class);
		// prepare the context
		RenderingHints renderingHints = context.get(RasterizingPipelineCode.RENDERING_HINTS, RenderingHints.class);
		Crs mapCrs = geoService.getCrs2(mapMetadata.getCrs());
		ReferencedEnvelope mapArea = new ReferencedEnvelope(converterService.toInternal(mapMetadata.getBounds()),
				mapCrs);
		Rectangle paintArea = new Rectangle((int) (mapMetadata.getScale() * mapArea.getWidth()),
				(int) (mapMetadata.getScale() * mapArea.getHeight()));
		mapContext.getViewport().setBounds(mapArea);
		mapContext.getViewport().setCoordinateReferenceSystem(mapCrs);
		mapContext.getViewport().setScreenArea(paintArea);
		// add the layers
		for (LayerMetadata layerMetadata : mapMetadata.getLayers()) {
			Layer layer = layerFactoryService.createLayer(mapContext, layerMetadata);
			mapContext.addLayer(layer);
		}
		BufferedImage image = createImage(paintArea.width, paintArea.height, mapMetadata.isTransparent());
		Graphics2D graphics = getGraphics(image, mapMetadata.isTransparent(), renderingHints);
		context.put(RasterizingPipelineCode.GRAPHICS2D, graphics);
		context.put(RasterizingPipelineCode.BUFFERED_IMAGE, image);
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
