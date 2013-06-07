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

package org.geomajas.puregwt.client.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.util.Browser;
import org.geomajas.puregwt.client.controller.MapController;
import org.geomajas.puregwt.client.controller.MapEventParserFactory;
import org.geomajas.puregwt.client.controller.NavigationController;
import org.geomajas.puregwt.client.controller.TouchNavigationController;
import org.geomajas.puregwt.client.event.FeatureDeselectedEvent;
import org.geomajas.puregwt.client.event.FeatureSelectedEvent;
import org.geomajas.puregwt.client.event.FeatureSelectionHandler;
import org.geomajas.puregwt.client.event.LayerHideEvent;
import org.geomajas.puregwt.client.event.LayerShowEvent;
import org.geomajas.puregwt.client.event.LayerVisibilityHandler;
import org.geomajas.puregwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapResizedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.gfx.CanvasContainer;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.map.feature.FeatureService;
import org.geomajas.puregwt.client.map.feature.FeatureServiceFactory;
import org.geomajas.puregwt.client.map.layer.LayersModel;
import org.geomajas.puregwt.client.map.render.MapRenderer;
import org.geomajas.puregwt.client.map.render.MapRendererFactory;
import org.geomajas.puregwt.client.service.CommandService;
import org.geomajas.puregwt.client.widget.PanningWidget;
import org.geomajas.puregwt.client.widget.ScalebarWidget;
import org.geomajas.puregwt.client.widget.SimpleZoomWidget;
import org.geomajas.puregwt.client.widget.TouchZoomWidget;
import org.geomajas.puregwt.client.widget.Watermark;
import org.geomajas.puregwt.client.widget.ZoomStepWidget;
import org.geomajas.puregwt.client.widget.ZoomToRectangleWidget;
import org.vaadin.gwtgraphics.client.Transformable;
import org.vaadin.gwtgraphics.client.shape.Path;
import com.google.gwt.event.dom.client.HasAllGestureHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.HasTouchCancelHandlers;
import com.google.gwt.event.dom.client.HasTouchEndHandlers;
import com.google.gwt.event.dom.client.HasTouchMoveHandlers;
import com.google.gwt.event.dom.client.HasTouchStartHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Default implementation of the map presenter interface. In other words this is the default GWT map object.
 * 
 * @author Pieter De Graef
 */
public final class MapPresenterImpl implements MapPresenter {

	/**
	 * Map view definition.
	 * 
	 * @author Pieter De Graef
	 */
	public interface MapWidget extends HasMouseDownHandlers, HasMouseUpHandlers, HasMouseOutHandlers,
			HasMouseOverHandlers, HasMouseMoveHandlers, HasMouseWheelHandlers, HasDoubleClickHandlers, IsWidget,
			RequiresResize, HasTouchStartHandlers, HasTouchEndHandlers, HasTouchCancelHandlers, HasTouchMoveHandlers,
			HasAllGestureHandlers {

		/**
		 * Returns the HTML container of the map. This is a normal HTML container that contains the images of rasterized
		 * tiles (both vector and raster layers).
		 * 
		 * @return the container
		 */
		HtmlContainer getMapHtmlContainer();

		/**
		 * Returns the vector container that contains the vectorized tiles (SVG/VML) of vector layers.
		 * 
		 * @return the container
		 */
		VectorContainer getMapVectorContainer();

		/**
		 * Returns the list of user-defined vector containers for world-space objects.
		 * 
		 * @return the container
		 */
		List<VectorContainer> getWorldVectorContainers();

		/**
		 * Returns the list of user-defined containers (vector + canvas) for world-space objects.
		 * 
		 * @return the container
		 */
		List<Transformable> getWorldTransformables();

		/**
		 * Returns a new user-defined container for screen space objects.
		 * 
		 * @return the container
		 */
		VectorContainer getNewScreenContainer();

		/**
		 * Returns a new user-defined container for world space objects.
		 * 
		 * @return the container
		 */
		VectorContainer getNewWorldContainer();

		/**
		 * Removes a user-defined container.
		 * 
		 * @param container
		 *            container
		 * @return true if removed, false if unknown
		 */
		boolean removeVectorContainer(VectorContainer container);

		/**
		 * Brings the user-defined container to the front (relative to its world-space or screen-space peers!).
		 * 
		 * @param container
		 *            container
		 * @return true if successful
		 */
		boolean bringToFront(VectorContainer container);

		/**
		 * Returns a new user-defined container of map gadgets.
		 * 
		 * @return the container
		 */
		AbsolutePanel getWidgetContainer();

		/**
		 * Get the total width of the view.
		 * 
		 * @return width in pixels
		 */
		int getWidth();

		/**
		 * Get the total height of the view.
		 * 
		 * @return height in pixels
		 */
		int getHeight();

		/**
		 * Set the total size of the view.
		 * 
		 * @param width
		 *            width
		 * @param height
		 *            height
		 */
		void setPixelSize(int width, int height);

		/**
		 * Schedules an animated scaling operation.
		 * 
		 * @param xx
		 *            the new x-axis scale factor
		 * @param yy
		 *            the new y-axis scale factor
		 * @param animationMillis
		 *            the animation time in millis
		 */
		void scheduleScale(double xx, double yy, int animationMillis);

		CanvasContainer getNewWorldCanvas();

		void scheduleTransform(double xx, double yy, double dx, double dy, int animationMillis);
	}

	private final MapEventBus eventBus;

	@Inject
	private CommandService commandService;

	private final MapEventParser mapEventParser;

	private List<HandlerRegistration> handlers;

	private final Map<MapController, List<HandlerRegistration>> listeners;

	private MapController mapController;

	private MapController fallbackController;

	@Inject
	private LayersModel layersModel;

	@Inject
	private ViewPort viewPort;

	private MapRenderer mapRenderer;

	private WorldContainerRenderer worldContainerRenderer;

	@Inject
	private MapWidget display;

	@Inject
	private GfxUtil gfxUtil;

	private MapConfiguration configuration;

	private final MapRendererFactory mapRendererFactory;

	private FeatureService featureService;

	private boolean isMobileBrowser;

	@Inject
	private MapPresenterImpl(final FeatureServiceFactory featureServiceFactory,
			final MapEventParserFactory mapEventParserFactory, final MapRendererFactory mapRendererFactory,
			final EventBus eventBus) {
		this.mapRendererFactory = mapRendererFactory;
		handlers = new ArrayList<HandlerRegistration>();
		listeners = new HashMap<MapController, List<HandlerRegistration>>();
		featureService = featureServiceFactory.create(this);
		mapEventParser = mapEventParserFactory.create(this);
		this.eventBus = new MapEventBusImpl(this, eventBus);
		this.configuration = new MapConfigurationImpl();
	}

	// ------------------------------------------------------------------------
	// MapPresenter implementation:
	// ------------------------------------------------------------------------

	@Override
	public void initialize(String applicationId, String id) {
		mapRenderer = mapRendererFactory.create(layersModel, viewPort, configuration, display.getMapHtmlContainer());
		isMobileBrowser = Browser.isMobile();

		eventBus.addViewPortChangedHandler(mapRenderer);
		eventBus.addMapResizedHandler(mapRenderer);
		eventBus.addLayerOrderChangedHandler(mapRenderer);
		eventBus.addMapCompositionHandler(mapRenderer);
		eventBus.addLayerVisibilityHandler(mapRenderer);
		eventBus.addLayerStyleChangedHandler(mapRenderer);
		eventBus.addLayerRefreshedHandler(mapRenderer);

		final FeatureSelectionRenderer selectionRenderer = new FeatureSelectionRenderer();
		eventBus.addLayerVisibilityHandler(selectionRenderer);
		eventBus.addFeatureSelectionHandler(selectionRenderer);

		worldContainerRenderer = new WorldContainerRenderer();
		eventBus.addViewPortChangedHandler(worldContainerRenderer);

		if (isMobileBrowser) {
			fallbackController = new TouchNavigationController();

		} else {
			fallbackController = new NavigationController();
		}

		setMapController(fallbackController);

		GwtCommand commandRequest = new GwtCommand(GetMapConfigurationRequest.COMMAND);
		commandRequest.setCommandRequest(new GetMapConfigurationRequest(id, applicationId));
		commandService.execute(commandRequest, new AbstractCommandCallback<GetMapConfigurationResponse>() {

			public void execute(GetMapConfigurationResponse response) {
				// Initialize the MapModel and ViewPort:
				ClientMapInfo mapInfo = response.getMapInfo();
				((MapConfigurationImpl) configuration).setServerConfiguration(mapInfo);

				// Configure the ViewPort. This will immediately zoom to the initial bounds:
				viewPort.setMapSize(display.getWidth(), display.getHeight());
				layersModel.initialize(mapInfo, viewPort, eventBus);
				viewPort.initialize(mapInfo, eventBus);

				// Immediately zoom to the initial bounds as configured:
				viewPort.applyBounds(mapInfo.getInitialBounds());

				// Initialize the FeatureSelectionRenderer:
				selectionRenderer.initialize(mapInfo);

				// Adding the default map widgets:
				if (getWidgetPane() != null) {

					if (isMobileBrowser) {
						getWidgetPane().add(new TouchZoomWidget(MapPresenterImpl.this));
					} else {
						getWidgetPane().add(new Watermark(MapPresenterImpl.this));
						getWidgetPane().add(new ScalebarWidget(MapPresenterImpl.this));
						getWidgetPane().add(new PanningWidget(MapPresenterImpl.this));

						List<ScaleInfo> zoomLevels = mapInfo.getScaleConfiguration().getZoomLevels();
						if (zoomLevels != null && mapInfo.getScaleConfiguration().getZoomLevels().size() > 0) {
							// Zoom steps:
							getWidgetPane().add(new ZoomToRectangleWidget(MapPresenterImpl.this));
							getWidgetPane().add(new ZoomStepWidget(MapPresenterImpl.this, 60, 18));
						} else {
							// Simple zooming:
							getWidgetPane().add(new ZoomToRectangleWidget(MapPresenterImpl.this));
							getWidgetPane().add(new SimpleZoomWidget(MapPresenterImpl.this, 60, 20));
						}
					}

				}
				// Fire initialization event
				eventBus.fireEvent(new MapInitializationEvent());
			}
		});
	}

	@Override
	public Widget asWidget() {
		return display.asWidget();
	}

	/** @todo javadoc unknown. */
	public void setMapRenderer(MapRenderer mapRenderer) {
		this.mapRenderer = mapRenderer;
	}

	/** @todo javadoc unknown. */
	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}

	@Override
	public MapConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public void setSize(int width, int height) {
		display.setPixelSize(width, height);
		if (viewPort != null) {
			viewPort.setMapSize(width, height);
		}
		eventBus.fireEvent(new MapResizedEvent(width, height));
	}

	@Override
	public VectorContainer addWorldContainer() {
		VectorContainer container = display.getNewWorldContainer();
		// set transform parameters once, after that all is handled by WorldContainerRenderer
		Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		container.setScale(matrix.getXx(), matrix.getYy());
		container.setTranslation(matrix.getDx(), matrix.getDy());
		return container;
	}

	@Override
	public CanvasContainer addWorldCanvas() {
		CanvasContainer container = display.getNewWorldCanvas();
		// set transform parameters once, after that all is handled by WorldContainerRenderer
		Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		container.setScale(matrix.getXx(), matrix.getYy());
		container.setTranslation(matrix.getDx(), matrix.getDy());
		return container;
	}

	@Override
	public VectorContainer addScreenContainer() {
		return display.getNewScreenContainer();
	}

	@Override
	public boolean removeVectorContainer(VectorContainer container) {
		return display.removeVectorContainer(container);
	}

	@Override
	public boolean bringToFront(VectorContainer container) {
		return display.bringToFront(container);
	}

	@Override
	public LayersModel getLayersModel() {
		return layersModel;
	}

	@Override
	public ViewPort getViewPort() {
		return viewPort;
	}

	@Override
	public FeatureService getFeatureService() {
		return featureService;
	}

	@Override
	public MapEventBus getEventBus() {
		return eventBus;
	}

	@Override
	public void setMapController(MapController mapController) {
		for (HandlerRegistration registration : handlers) {
			registration.removeHandler();
		}
		if (this.mapController != null) {
			this.mapController.onDeactivate(this);
			this.mapController = null;
		}
		handlers = new ArrayList<HandlerRegistration>();
		if (null == mapController) {
			mapController = fallbackController;
		}
		if (mapController != null) {
			if (isMobileBrowser) {
				handlers.add(display.addTouchStartHandler(mapController));
				handlers.add(display.addTouchMoveHandler(mapController));
				handlers.add(display.addTouchCancelHandler(mapController));
				handlers.add(display.addGestureStartHandler(mapController));
				handlers.add(display.addGestureChangeHandler(mapController));
				handlers.add(display.addGestureEndHandler(mapController));

			} else {
				handlers.add(display.addMouseDownHandler(mapController));
				handlers.add(display.addMouseMoveHandler(mapController));
				handlers.add(display.addMouseOutHandler(mapController));
				handlers.add(display.addMouseOverHandler(mapController));
				handlers.add(display.addMouseUpHandler(mapController));
				handlers.add(display.addMouseWheelHandler(mapController));
				handlers.add(display.addDoubleClickHandler(mapController));
			}

			this.mapController = mapController;
			mapController.onActivate(this);
		}
	}

	@Override
	public MapController getMapController() {
		return mapController;
	}

	@Override
	public boolean addMapListener(MapController mapListener) {
		if (mapListener != null && !listeners.containsKey(mapListener)) {
			List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

			if (isMobileBrowser) {
				registrations.add(display.addTouchStartHandler(mapListener));
				registrations.add(display.addTouchMoveHandler(mapListener));
				registrations.add(display.addTouchCancelHandler(mapListener));
				registrations.add(display.addGestureStartHandler(mapListener));
				registrations.add(display.addGestureChangeHandler(mapListener));
				registrations.add(display.addGestureEndHandler(mapListener));
			} else {
				registrations.add(display.addMouseDownHandler(mapListener));
				registrations.add(display.addMouseMoveHandler(mapListener));
				registrations.add(display.addMouseOutHandler(mapListener));
				registrations.add(display.addMouseOverHandler(mapListener));
				registrations.add(display.addMouseUpHandler(mapListener));
				registrations.add(display.addMouseWheelHandler(mapListener));
			}

			mapListener.onActivate(this);
			listeners.put(mapListener, registrations);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeMapListener(MapController mapListener) {
		if (mapListener != null && listeners.containsKey(mapListener)) {
			List<HandlerRegistration> registrations = listeners.get(mapListener);
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
			listeners.remove(mapListener);
			mapListener.onDeactivate(this);
			return true;
		}
		return false;
	}

	@Override
	public Collection<MapController> getMapListeners() {
		return listeners.keySet();
	}

	@Override
	public void setCursor(String cursor) {
		DOM.setStyleAttribute(display.asWidget().getElement(), "cursor", cursor);
	}

	@Override
	public MapEventParser getMapEventParser() {
		return mapEventParser;
	}

	@Override
	public AbsolutePanel getWidgetPane() {
		return display.getWidgetContainer();
	}

	private int getAnimationMillis() {
		Long time = (Long) configuration.getMapHintValue(MapConfiguration.ANIMATION_TIME);
		return time == null ? 0 : time.intValue();
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Class that updates all the world containers when the view on the map changes.
	 * 
	 * @author Pieter De Graef
	 */
	private class WorldContainerRenderer implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
			display.scheduleTransform(matrix.getXx(), matrix.getYy(), matrix.getDx(), matrix.getDy(),
					getAnimationMillis());
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
			// We must translate as well because zooming and keeping the same center point
			// also involves a translation (scale origin != center point) !!!
			display.scheduleTransform(matrix.getXx(), matrix.getYy(), matrix.getDx(), matrix.getDy(),
					getAnimationMillis());
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
			for (Transformable transformable : display.getWorldTransformables()) {
				transformable.setTranslation(matrix.getDx(), matrix.getDy());
			}
		}
	}

	/**
	 * This feature selection handler will render the selection on the map.
	 * 
	 * @author Pieter De Graef
	 */
	private class FeatureSelectionRenderer implements FeatureSelectionHandler, LayerVisibilityHandler {

		private final VectorContainer container;

		private final Map<String, Path> paths;

		private FeatureStyleInfo pointStyle;

		private FeatureStyleInfo lineStyle;

		private FeatureStyleInfo ringStyle;

		public FeatureSelectionRenderer() {
			container = addWorldContainer();
			paths = new HashMap<String, Path>();
		}

		public void initialize(ClientMapInfo mapInfo) {
			pointStyle = mapInfo.getPointSelectStyle();
			lineStyle = mapInfo.getLineSelectStyle();
			ringStyle = mapInfo.getPolygonSelectStyle();
		}

		public void onFeatureSelected(FeatureSelectedEvent event) {
			Feature feature = event.getFeature();
			if (feature.getLayer().isShowing()) {
				render(feature);
			}
		}

		public void onFeatureDeselected(FeatureDeselectedEvent event) {
			remove(event.getFeature());
		}

		public void onShow(LayerShowEvent event) {
		}

		public void onHide(LayerHideEvent event) {
		}

		public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
		}

		private void render(Feature f) {
			Path path = gfxUtil.toPath(f.getGeometry());
			String type = f.getGeometry().getGeometryType();
			if (Geometry.POINT.equals(type) || Geometry.MULTI_POINT.equals(type)) {
				gfxUtil.applyStyle(path, pointStyle);
			} else if (Geometry.LINE_STRING.equals(type) || Geometry.MULTI_LINE_STRING.equals(type)) {
				gfxUtil.applyStyle(path, lineStyle);
			} else {
				gfxUtil.applyStyle(path, ringStyle);
			}
			container.add(path);
			paths.put(f.getId(), path);
		}

		private void remove(Feature feature) {
			Path path = paths.get(feature.getId());
			if (path != null) {
				container.remove(path);
				paths.remove(feature.getId());
			}
		}
	}
}