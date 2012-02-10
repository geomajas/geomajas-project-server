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

package org.geomajas.puregwt.client.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.Matrix;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.controller.ListenerController;
import org.geomajas.puregwt.client.controller.MapController;
import org.geomajas.puregwt.client.controller.MapEventParserImpl;
import org.geomajas.puregwt.client.controller.MapListener;
import org.geomajas.puregwt.client.controller.NavigationController;
import org.geomajas.puregwt.client.event.FeatureDeselectedEvent;
import org.geomajas.puregwt.client.event.FeatureSelectedEvent;
import org.geomajas.puregwt.client.event.FeatureSelectionHandler;
import org.geomajas.puregwt.client.event.LayerHideEvent;
import org.geomajas.puregwt.client.event.LayerOrderChangedHandler;
import org.geomajas.puregwt.client.event.LayerRefreshedHandler;
import org.geomajas.puregwt.client.event.LayerShowEvent;
import org.geomajas.puregwt.client.event.LayerStyleChangedHandler;
import org.geomajas.puregwt.client.event.LayerVisibilityHandler;
import org.geomajas.puregwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.event.MapCompositionHandler;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapResizedEvent;
import org.geomajas.puregwt.client.event.MapResizedHandler;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.map.feature.FeatureService;
import org.geomajas.puregwt.client.map.feature.FeatureServiceImpl;
import org.geomajas.puregwt.client.map.gadget.PanningGadget;
import org.geomajas.puregwt.client.map.gadget.ScalebarGadget;
import org.geomajas.puregwt.client.map.gadget.SimpleZoomGadget;
import org.geomajas.puregwt.client.map.gadget.WatermarkGadget;
import org.geomajas.puregwt.client.map.gadget.ZoomStepGadget;
import org.geomajas.puregwt.client.map.gadget.ZoomToRectangleGadget;
import org.geomajas.puregwt.client.map.render.MapRenderer;
import org.geomajas.puregwt.client.map.render.MapRendererImpl;
import org.vaadin.gwtgraphics.client.shape.Path;

import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

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
			RequiresResize {

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
		 * Returns the list of user-defined containers for world-space objects.
		 * 
		 * @return the container
		 */
		List<VectorContainer> getWorldVectorContainers();

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
		 * Returns a new user-defined container of map gadgets (screen space).
		 * 
		 * @return the container
		 */
		VectorContainer getMapGadgetContainer();

		/**
		 * Removes a user-defined container of map gadgets.
		 * 
		 * @param mapGadgetContainer
		 * @return true if successful
		 */
		boolean removeMapGadgetContainer(VectorContainer mapGadgetContainer);
	}

	private final EventBus eventBus;

	private final MapEventParser mapEventParser;

	private List<HandlerRegistration> handlers;

	private Map<MapListener, List<HandlerRegistration>> listeners;

	private MapController mapController;

	private MapController fallbackController;

	@Inject
	private LayersModel layersModel;

	@Inject
	private ViewPort viewPort;

	private MapRenderer mapRenderer;

	private WorldContainerRenderer worldContainerRenderer;

	private Map<MapGadget, VectorContainer> gadgets;

	@Inject
	private MapWidget display;

	@Inject
	private GfxUtil gfxUtil;

	private FeatureService featureService;

	@Inject
	private MapPresenterImpl() {
		eventBus = new SimpleEventBus();
		handlers = new ArrayList<HandlerRegistration>();
		listeners = new HashMap<MapListener, List<HandlerRegistration>>();
		gadgets = new HashMap<MapGadget, VectorContainer>();
		featureService = new FeatureServiceImpl(this);
		mapEventParser = new MapEventParserImpl(this);
	}

	// ------------------------------------------------------------------------
	// MapPresenter implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void initialize(String applicationId, String id) {
		mapRenderer = new MapRendererImpl(layersModel, viewPort, display.getMapHtmlContainer());

		eventBus.addHandler(ViewPortChangedHandler.TYPE, mapRenderer);
		eventBus.addHandler(LayerOrderChangedHandler.TYPE, mapRenderer);
		eventBus.addHandler(MapCompositionHandler.TYPE, mapRenderer);
		eventBus.addHandler(LayerVisibilityHandler.TYPE, mapRenderer);
		eventBus.addHandler(LayerStyleChangedHandler.TYPE, mapRenderer);
		eventBus.addHandler(LayerRefreshedHandler.TYPE, mapRenderer);

		final FeatureSelectionRenderer selectionRenderer = new FeatureSelectionRenderer();
		eventBus.addHandler(LayerVisibilityHandler.TYPE, selectionRenderer);
		eventBus.addHandler(FeatureSelectionHandler.TYPE, selectionRenderer);

		worldContainerRenderer = new WorldContainerRenderer();
		eventBus.addHandler(ViewPortChangedHandler.TYPE, worldContainerRenderer);

		MapGadgetRenderer mapGadgetRenderer = new MapGadgetRenderer();
		eventBus.addHandler(ViewPortChangedHandler.TYPE, mapGadgetRenderer);
		eventBus.addHandler(MapResizedEvent.TYPE, mapGadgetRenderer);

		setFallbackController(new NavigationController());

		GwtCommand commandRequest = new GwtCommand(GetMapConfigurationRequest.COMMAND);
		commandRequest.setCommandRequest(new GetMapConfigurationRequest(id, applicationId));
		GwtCommandDispatcher dispatcher = GwtCommandDispatcher.getInstance();
		dispatcher.execute(commandRequest, new AbstractCommandCallback<GetMapConfigurationResponse>() {

			public void execute(GetMapConfigurationResponse response) {
				// Initialize the MapModel and ViewPort:
				GetMapConfigurationResponse r = (GetMapConfigurationResponse) response;

				// Configure the ViewPort. This will immediately zoom to the initial bounds:
				viewPort.setMapSize(display.asWidget().getOffsetWidth(), display.asWidget().getOffsetHeight());
				layersModel.initialize(r.getMapInfo(), viewPort, eventBus);
				viewPort.initialize(r.getMapInfo(), eventBus);

				// Immediately zoom to the initial bounds as configured:
				viewPort.applyBounds(r.getMapInfo().getInitialBounds());

				// If there are already some MapGadgets registered, draw them now:
				for (Entry<MapGadget, VectorContainer> entry : gadgets.entrySet()) {
					entry.getKey().onDraw(viewPort, entry.getValue());
				}

				// Initialize the FeatureSelectionRenderer:
				selectionRenderer.initialize(r.getMapInfo());

				addMapGadget(new ScalebarGadget(r.getMapInfo()));
				addMapGadget(new WatermarkGadget());
				// addMapGadget(new ZoomStepGadget());
				addMapGadget(new PanningGadget(5, 5));
				if (r.getMapInfo().getScaleConfiguration().getZoomLevels() != null
						&& r.getMapInfo().getScaleConfiguration().getZoomLevels().size() > 0) {
					// Zoom steps...
					addMapGadget(new ZoomToRectangleGadget(5, 60));
					addMapGadget(new ZoomStepGadget(60, 20));
				} else {
					// Simple zooming:
					addMapGadget(new SimpleZoomGadget(60, 20));
					addMapGadget(new ZoomToRectangleGadget(125, 20));
				}

				// Fire initialization event:
				eventBus.fireEvent(new MapInitializationEvent());
			}
		});
		setSize(640, 480);
	}

	/** {@inheritDoc} */
	public Widget asWidget() {
		return display.asWidget();
	}

	/** {@inheritDoc} */
	public void setMapRenderer(MapRenderer mapRenderer) {
		this.mapRenderer = mapRenderer;
	}

	/** {@inheritDoc} */
	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}

	/** {@inheritDoc} */
	public void setSize(int width, int height) {
		display.asWidget().setSize(width + "px", height + "px");
		if (viewPort != null) {
			viewPort.setMapSize(width, height);
		}
		eventBus.fireEvent(new MapResizedEvent(width, height));
	}

	/** {@inheritDoc} */
	public VectorContainer addWorldContainer() {
		VectorContainer container = display.getNewWorldContainer();
		// set transform parameters once, after that all is handled by WorldContainerRenderer
		Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		container.setScale(matrix.getXx(), matrix.getYy());
		container.setTranslation(matrix.getDx(), matrix.getDy());
		return container;
	}

	/** {@inheritDoc} */
	public VectorContainer addScreenContainer() {
		return display.getNewScreenContainer();
	}

	/** {@inheritDoc} */
	public boolean removeVectorContainer(VectorContainer container) {
		return display.removeVectorContainer(container);
	}

	/** {@inheritDoc} */
	public boolean bringToFront(VectorContainer container) {
		return display.bringToFront(container);
	}

	/** {@inheritDoc} */
	public LayersModel getLayersModel() {
		return layersModel;
	}

	/** {@inheritDoc} */
	public ViewPort getViewPort() {
		return viewPort;
	}

	/** {@inheritDoc} */
	public FeatureService getFeatureService() {
		return featureService;
	}

	/** {@inheritDoc} */
	public EventBus getEventBus() {
		return eventBus;
	}

	/** {@inheritDoc} */
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
			handlers.add(display.addMouseDownHandler(mapController));
			handlers.add(display.addMouseMoveHandler(mapController));
			handlers.add(display.addMouseOutHandler(mapController));
			handlers.add(display.addMouseOverHandler(mapController));
			handlers.add(display.addMouseUpHandler(mapController));
			handlers.add(display.addMouseWheelHandler(mapController));
			handlers.add(display.addDoubleClickHandler(mapController));
			this.mapController = mapController;
			mapController.onActivate(this);
		}
	}

	/** {@inheritDoc} */
	public MapController getMapController() {
		return mapController;
	}

	/** {@inheritDoc} */
	public void setFallbackController(MapController fallbackController) {
		boolean fallbackActive = (mapController == this.fallbackController);
		this.fallbackController = fallbackController;
		if (mapController == null || fallbackActive) {
			setMapController(fallbackController);
		}
	}

	/** {@inheritDoc} */
	public boolean addMapListener(MapListener mapListener) {
		if (mapListener != null && !listeners.containsKey(mapListener)) {
			List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();
			ListenerController listenerController = new ListenerController(mapListener);
			registrations.add(display.addMouseDownHandler(listenerController));
			registrations.add(display.addMouseMoveHandler(listenerController));
			registrations.add(display.addMouseOutHandler(listenerController));
			registrations.add(display.addMouseOverHandler(listenerController));
			registrations.add(display.addMouseUpHandler(listenerController));
			registrations.add(display.addMouseWheelHandler(listenerController));
			listenerController.onActivate(this);
			listeners.put(mapListener, registrations);
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	public boolean removeMapListener(MapListener mapListener) {
		if (mapListener != null && listeners.containsKey(mapListener)) {
			List<HandlerRegistration> registrations = listeners.get(mapListener);
			for (HandlerRegistration registration : registrations) {
				registration.removeHandler();
			}
			listeners.remove(mapListener);
			// deactivate not necessary, because the ListenerController does nothing when deactiving the listener.
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	public Collection<MapListener> getMapListeners() {
		return listeners.keySet();
	}

	/** {@inheritDoc} */
	public Set<MapGadget> getMapGadgets() {
		return gadgets.keySet();
	}

	/** {@inheritDoc} */
	public void addMapGadget(MapGadget mapGadget) {
		VectorContainer container = addScreenContainer();
		gadgets.put(mapGadget, container);
		if (layersModel != null && viewPort != null) {
			mapGadget.onDraw(viewPort, container);
		}
	}

	/** {@inheritDoc} */
	public boolean removeMapGadget(MapGadget mapGadget) {
		if (gadgets.containsKey(mapGadget)) {
			mapGadget.onDestroy();
			display.removeVectorContainer(gadgets.get(mapGadget));
			gadgets.remove(mapGadget);
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	public void setCursor(String cursor) {
		DOM.setStyleAttribute(display.asWidget().getElement(), "cursor", cursor);
	}

	/** {@inheritDoc} */
	public MapEventParser getMapEventParser() {
		return mapEventParser;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * ViewPortChangedHandler implementation that renders all the MapGadgets on the view port events.
	 * 
	 * @author Pieter De Graef
	 */
	private class MapGadgetRenderer implements ViewPortChangedHandler, MapResizedHandler {

		public void onMapResized(MapResizedEvent event) {
			for (MapGadget mapGadget : gadgets.keySet()) {
				mapGadget.onResize();
			}
		}

		public void onViewPortChanged(ViewPortChangedEvent event) {
			for (MapGadget mapGadget : gadgets.keySet()) {
				mapGadget.onScale();
				mapGadget.onTranslate();
			}
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			for (MapGadget mapGadget : gadgets.keySet()) {
				mapGadget.onScale();
			}
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			for (MapGadget mapGadget : gadgets.keySet()) {
				mapGadget.onTranslate();
			}
		}
	}

	/**
	 * Class that updates all the world containers when the view on the map changes.
	 * 
	 * @author Pieter De Graef
	 */
	private class WorldContainerRenderer implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
			for (VectorContainer vectorContainer : display.getWorldVectorContainers()) {
				vectorContainer.setScale(matrix.getXx(), matrix.getYy());
				vectorContainer.setTranslation(matrix.getDx(), matrix.getDy());
			}
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
			for (VectorContainer vectorContainer : display.getWorldVectorContainers()) {
				vectorContainer.setScale(matrix.getXx(), matrix.getYy());
				vectorContainer.setTranslation(matrix.getDx(), matrix.getDy());
			}
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
			for (VectorContainer vectorContainer : display.getWorldVectorContainers()) {
				vectorContainer.setTranslation(matrix.getDx(), matrix.getDy());
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