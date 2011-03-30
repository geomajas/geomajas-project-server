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

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.RenderingService;
import org.geomajas.plugin.rasterizing.command.dto.LegendRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.layer.GeometryDirectLayer;
import org.geomajas.plugin.rasterizing.layer.RasterDirectLayer;
import org.geomajas.plugin.rasterizing.legend.LegendBuilder;
import org.geomajas.service.TextService;
import org.geotools.factory.Hints;
import org.geotools.map.DirectLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Rendering service based on StreamingRenderer. To be refactored when StreamingRenderer supports DirectLayer.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class RenderingServiceImpl implements RenderingService {

	@Autowired
	private TextService textService;

	public RenderedImage paintLegend(MapContext mapContext) {
		LegendBuilder builder = new LegendBuilder();
		MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) mapContext.getUserData().get(
				LayerFactory.USERDATA_RASTERIZING_INFO);
		LegendRasterizingInfo legendRasterizingInfo = mapRasterizingInfo.getLegendRasterizingInfo();
		Font font = textService.getFont(legendRasterizingInfo.getFont());
		builder.setTitle(legendRasterizingInfo.getTitle(), font);
		if (legendRasterizingInfo.getWidth() > 0) {
			builder.setSize(legendRasterizingInfo.getWidth(), legendRasterizingInfo.getHeight());
		}
		for (Layer layer : mapContext.layers()) {
			if (layer instanceof RasterDirectLayer) {
				RasterDirectLayer rasterLayer = (RasterDirectLayer) layer;
				builder.addRasterLayer(rasterLayer.getTitle(), font);
			} else if (layer instanceof FeatureLayer) {
				FeatureLayer featureLayer = (FeatureLayer) layer;
				FeatureTypeStyle normalStyle = featureLayer.getStyle().featureTypeStyles().get(0);
				Map<String, FeatureStyleInfo> map = (Map<String, FeatureStyleInfo>) featureLayer.getUserData().get(
						LayerFactory.USERDATA_KEY_STYLES);
				for (Rule rule : normalStyle.rules()) {
					// hackish solution for dynamic legend support
					if (map.containsKey(rule.getDescription().getTitle().toString())) {
						if (normalStyle.rules().size() == 1) {
							builder.addVectorLayer(featureLayer.getTitle(), rule, font);
						} else {
							builder.addVectorLayer(rule.getDescription().getTitle().toString(), rule, font);
						}
					}
				}
			} else if (layer instanceof GeometryDirectLayer) {
				GeometryDirectLayer geometryLayer = (GeometryDirectLayer) layer;
				builder.addVectorLayer(geometryLayer.getTitle(), geometryLayer.getStyle().featureTypeStyles().get(0)
						.rules().get(0), font);
			}
		}
		JComponent c = builder.buildComponent();
		BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();
		RenderingHints renderingHints = new Hints();
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHints(renderingHints);
		c.print(graphics);
		return image;
	}

	public void paintMap(MapContext context, Graphics2D graphics) {
		List<RenderRequest> renderStack = new ArrayList<RenderRequest>();
		VectorRenderRequest vectorRequest = null;
		for (Layer layer : context.layers()) {
			if (layer instanceof DirectLayer) {
				renderStack.add(new DirectRenderRequest(graphics, context, (DirectLayer) layer));
			} else {
				if (vectorRequest == null) {
					vectorRequest = new VectorRenderRequest(graphics, context);
					renderStack.add(vectorRequest);
				}
				vectorRequest.getMapContext().addLayer(layer);
			}
		}

		for (RenderRequest renderRequest : renderStack) {
			renderRequest.execute();
		}
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

		private Graphics2D graphics;

		private MapContext mapContext;

		private DirectLayer layer;

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

		private Graphics2D graphics;

		private MapContext mapContext = new MapContext();

		public VectorRenderRequest(Graphics2D graphics, MapContext context) {
			this.graphics = graphics;
			this.mapContext.setAreaOfInterest(context.getAreaOfInterest());
			this.mapContext.getViewport().setBounds(context.getViewport().getBounds());
			this.mapContext.getViewport().setScreenArea(context.getViewport().getScreenArea());
			this.mapContext.getViewport().setCoordinateReferenceSystem(
					context.getViewport().getCoordianteReferenceSystem());
		}

		public void execute() {
			StreamingRenderer renderer = new StreamingRenderer();
			renderer.setContext(mapContext);
			Map<Object, Object> rendererParams = new HashMap<Object, Object>();
			rendererParams.put("optimizedDataLoadingEnabled", true);
			renderer.setRendererHints(rendererParams);
			renderer.paint(graphics, mapContext.getViewport().getScreenArea(), mapContext.getViewport().getBounds());
			mapContext.dispose();
		}

		public MapContext getMapContext() {
			return mapContext;
		}

		public void setMapContext(MapContext mapContext) {
			this.mapContext = mapContext;
		}

	}

}
