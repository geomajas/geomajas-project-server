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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.rasterizing.api.RenderingService;
import org.geotools.map.DirectLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.springframework.stereotype.Component;

/**
 * Rendering service based on StreamingRenderer. To be refactored when StreamingRenderer supports DirectLayer.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class RenderingServiceImpl implements RenderingService {

	public void paint(MapContext context, Graphics2D graphics) {
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
	 * 
	 */
	public class DirectRenderRequest implements RenderRequest {

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
	 * 
	 */
	public class VectorRenderRequest implements RenderRequest {

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
		}

		public MapContext getMapContext() {
			return mapContext;
		}

		public void setMapContext(MapContext mapContext) {
			this.mapContext = mapContext;
		}

	}

}
