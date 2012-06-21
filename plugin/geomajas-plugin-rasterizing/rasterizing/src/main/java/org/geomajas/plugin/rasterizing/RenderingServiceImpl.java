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
package org.geomajas.plugin.rasterizing;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.RenderingService;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.layer.GeometryDirectLayer;
import org.geomajas.plugin.rasterizing.layer.RasterDirectLayer;
import org.geomajas.plugin.rasterizing.legend.LegendBuilder;
import org.geomajas.service.LegendGraphicService;
import org.geomajas.service.TextService;
import org.geomajas.service.legend.DefaultLegendGraphicMetadata;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;
import org.geotools.factory.Hints;
import org.geotools.map.DirectLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.map.MapViewport;
import org.geotools.renderer.lite.StreamingRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Rendering service based on StreamingRenderer. To be refactored when StreamingRenderer supports DirectLayer.
 * 
 * @author Jan De Moerloose
 */
@Component
public class RenderingServiceImpl implements RenderingService {

	@Autowired
	private TextService textService;

	@Autowired
	private LegendGraphicService legendGraphicService;

	private final Logger log = LoggerFactory.getLogger(RenderingServiceImpl.class);

	@SuppressWarnings("unchecked")
	public RenderedImage paintLegend(MapContext mapContext) {
		LegendBuilder builder = new LegendBuilder();
		LegendRasterizingInfo legendRasterizingInfo = getLegendInfo(mapContext);
		// set font and title
		Font font = textService.getFont(legendRasterizingInfo.getFont());
		builder.setTitle(legendRasterizingInfo.getTitle(), font);
		// set size
		if (legendRasterizingInfo.getWidth() > 0) {
			builder.setSize(legendRasterizingInfo.getWidth(), legendRasterizingInfo.getHeight());
		}
		// add an entry for each layer
		for (Layer layer : mapContext.layers()) {
			String layerId = (String) layer.getUserData().get(LayerFactory.USERDATA_KEY_LAYER_ID);
			if (layerId != null) {
				if (layer instanceof FeatureLayer) {
					FeatureLayer featureLayer = (FeatureLayer) layer;
					List<RuleInfo> rules = (List<RuleInfo>) featureLayer.getUserData().get(
							LayerFactory.USERDATA_KEY_STYLE_RULES);
					for (RuleInfo rule : rules) {
						if (!isTextOnly(rule)) {
							try {
								String title = rule.getTitle();
								if (title == null) {
									title = rule.getName();
								}
								if (title == null) {
									title = featureLayer.getTitle();
								}
								builder.addLayer(title, font, getImage(layerId, rule));
							} catch (GeomajasException e) {
								log.warn(
										"Cannot draw legend icon for rule " + rule.getTitle() + " of layer "
												+ layer.getTitle(), e);
							}
						}
					}
				} else if (layer instanceof RasterDirectLayer) {
					try {
						builder.addLayer(layer.getTitle(), font, getImage(layerId, null));
					} catch (GeomajasException e) {
						log.warn("Cannot draw legend icon for raster layer " + layer.getTitle(), e);
					}
				}
			} else if (layer instanceof GeometryDirectLayer) {
				List<RuleInfo> rules = (List<RuleInfo>) layer.getUserData().get(LayerFactory.USERDATA_KEY_STYLE_RULES);
				for (RuleInfo rule : rules) {
					if (!isTextOnly(rule)) {
						try {
							builder.addLayer(rule.getTitle(), font, getImage(null, rule));
						} catch (GeomajasException e) {
							log.warn("Cannot draw legend icon for rule " + rule.getTitle() + " of geometry layer", e);
						}
					}
				}
			}
		}
		// print the image
		JComponent c = builder.buildComponent();
		BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();
		RenderingHints renderingHints = new Hints();
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHints(renderingHints);
		c.print(graphics);
		return image;
	}

	private RenderedImage getImage(String layerId, RuleInfo rule) throws GeomajasException {
		DefaultLegendGraphicMetadata legendMetadata = new DefaultLegendGraphicMetadata();
		legendMetadata.setLayerId(layerId);
		legendMetadata.setRuleInfo(rule);
		return legendGraphicService.getLegendGraphic(legendMetadata);
	}

	private LegendRasterizingInfo getLegendInfo(MapContext mapContext) {
		MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) mapContext.getUserData().get(
				LayerFactory.USERDATA_RASTERIZING_INFO);
		return mapRasterizingInfo.getLegendRasterizingInfo();
	}

	private boolean isTextOnly(RuleInfo rule) {
		for (SymbolizerTypeInfo symbolizer : rule.getSymbolizerList()) {
			if (!(symbolizer instanceof TextSymbolizerInfo)) {
				return false;
			}
		}
		return true;
	}

	public void paintMap(MapContext context, Graphics2D graphics, Map<Object, Object> hints) {
		List<RenderRequest> renderStack = new ArrayList<RenderRequest>();
		VectorRenderRequest vectorRequest = null;
		for (Layer layer : context.layers()) {
			if (layer instanceof DirectLayer) {
				renderStack.add(new DirectRenderRequest(graphics, context, (DirectLayer) layer));
			} else {
				if (vectorRequest == null) {
					vectorRequest = new VectorRenderRequest(graphics, context, hints);
					renderStack.add(vectorRequest);
				}
				vectorRequest.getMapContext().addLayer(layer);
			}
		}

		for (RenderRequest renderRequest : renderStack) {
			renderRequest.execute();
		}
	}

	public void paintMap(MapContext context, Graphics2D graphics) {
		paintMap(context, graphics, new HashMap<Object, Object>());
	}

	/**
	 * An executable render request.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface RenderRequest {

		/**
		 * execute rendering.
		 */
		void execute();
	}

	/**
	 * Request for {@link DirectLayer} rendering.
	 * 
	 * @author Jan De Moerloose
	 */
	public static class DirectRenderRequest implements RenderRequest {

		private final Graphics2D graphics;

		private final MapContext mapContext;

		private final DirectLayer layer;

		public DirectRenderRequest(Graphics2D graphics, MapContext mapContext, DirectLayer layer) {
			super();
			this.graphics = graphics;
			this.mapContext = mapContext;
			this.layer = layer;
		}

		public void execute() {
			layer.draw(graphics, mapContext, mapContext.getViewport());
		}

	}

	/**
	 * Request for rendering a map of vector layers.
	 * 
	 * @author Jan De Moerloose
	 */
	public static class VectorRenderRequest implements RenderRequest {

		private final Graphics2D graphics;

		private final MapContext mapContext = new MapContext();

		private final Map<Object, Object> hints;

		public VectorRenderRequest(Graphics2D graphics, MapContext context, Map<Object, Object> hints) {
			this.graphics = graphics;
			this.hints = hints;
			this.mapContext.setAreaOfInterest(context.getAreaOfInterest());
			MapViewport viewPort = this.mapContext.getViewport();
			viewPort.setBounds(context.getViewport().getBounds());
			viewPort.setScreenArea(context.getViewport().getScreenArea());
			viewPort.setCoordinateReferenceSystem(context.getViewport().getCoordianteReferenceSystem());
		}

		public void execute() {
			StreamingRenderer renderer = new StreamingRenderer();
			renderer.setContext(mapContext);
			if (!hints.containsKey(StreamingRenderer.OPTIMIZED_DATA_LOADING_KEY)) {
				hints.put(StreamingRenderer.OPTIMIZED_DATA_LOADING_KEY, true);
			}
			// we use OGC scale for predictable conversion between pix/m scale and relative scale
			if (!hints.containsKey(StreamingRenderer.SCALE_COMPUTATION_METHOD_KEY)) {
				hints.put(StreamingRenderer.SCALE_COMPUTATION_METHOD_KEY, StreamingRenderer.SCALE_OGC);
			}
			renderer.setRendererHints(hints);
			renderer.paint(graphics, mapContext.getViewport().getScreenArea(), mapContext.getViewport().getBounds());
			mapContext.dispose();
		}

		public MapContext getMapContext() {
			return mapContext;
		}

	}

}
