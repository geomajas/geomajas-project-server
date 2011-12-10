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
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.puregwt.client.map.controller.ListenerController;
import org.geomajas.puregwt.client.map.controller.MapController;
import org.geomajas.puregwt.client.map.controller.MapListener;
import org.geomajas.puregwt.client.map.controller.NavigationController;
import org.geomajas.puregwt.client.map.event.EventBus;
import org.geomajas.puregwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.puregwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.puregwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.puregwt.client.map.event.LayerHideEvent;
import org.geomajas.puregwt.client.map.event.LayerOrderChangedHandler;
import org.geomajas.puregwt.client.map.event.LayerShowEvent;
import org.geomajas.puregwt.client.map.event.LayerStyleChangedHandler;
import org.geomajas.puregwt.client.map.event.LayerVisibilityHandler;
import org.geomajas.puregwt.client.map.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.map.event.MapCompositionHandler;
import org.geomajas.puregwt.client.map.event.MapInitializationEvent;
import org.geomajas.puregwt.client.map.event.MapResizedEvent;
import org.geomajas.puregwt.client.map.event.MapResizedHandler;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.map.gadget.NavigationGadget;
import org.geomajas.puregwt.client.map.gadget.ScalebarGadget;
import org.geomajas.puregwt.client.map.gadget.WatermarkGadget;
import org.geomajas.puregwt.client.map.gfx.GfxUtil;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.gfx.VectorContainer;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.GeometryFactory;
import org.geomajas.puregwt.client.spatial.LineString;
import org.geomajas.puregwt.client.spatial.LinearRing;
import org.geomajas.puregwt.client.spatial.Matrix;
import org.geomajas.puregwt.client.spatial.MultiLineString;
import org.geomajas.puregwt.client.spatial.MultiPoint;
import org.geomajas.puregwt.client.spatial.MultiPolygon;
import org.geomajas.puregwt.client.spatial.Point;
import org.geomajas.puregwt.client.spatial.Polygon;
import org.vaadin.gwtgraphics.client.shape.Path;

import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
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

	private List<HandlerRegistration> handlers;

	private MapController mapController;

	private MapController fallbackController;

	private Map<MapListener, List<HandlerRegistration>> listeners;

	@Inject
	private LayersModel layersModel;

	@Inject
	private ViewPort viewPort;

	private MapRenderer mapRenderer;

	private WorldContainerRenderer worldContainerRenderer;

	private Map<MapGadget, VectorContainer> gadgets;

	@Inject
	private EventBus eventBus;

	@Inject
	private MapWidget display;

	@Inject
	private GeometryFactory factory;

	@Inject
	private GfxUtil gfxUtil;

	@Inject
	private MapPresenterImpl() {
		handlers = new ArrayList<HandlerRegistration>();
		listeners = new HashMap<MapListener, List<HandlerRegistration>>();
		gadgets = new HashMap<MapGadget, VectorContainer>();
	}

	// ------------------------------------------------------------------------
	// MapPresenter implementation:
	// ------------------------------------------------------------------------

	public void initialize(String applicationId, String id) {
		mapRenderer = new DelegatingMapRenderer(layersModel, viewPort);
		mapRenderer.setHtmlContainer(display.getMapHtmlContainer());
		mapRenderer.setVectorContainer(display.getMapVectorContainer());

		eventBus.addHandler(ViewPortChangedHandler.TYPE, mapRenderer);
		eventBus.addHandler(LayerOrderChangedHandler.TYPE, mapRenderer);
		eventBus.addHandler(MapCompositionHandler.TYPE, mapRenderer);
		eventBus.addHandler(LayerVisibilityHandler.TYPE, mapRenderer);
		eventBus.addHandler(LayerStyleChangedHandler.TYPE, mapRenderer);

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
				Bbox initialBounds = factory.createBbox(r.getMapInfo().getInitialBounds());
				viewPort.applyBounds(initialBounds);

				// If there are already some MapGadgets registered, draw them now:
				for (Entry<MapGadget, VectorContainer> entry : gadgets.entrySet()) {
					entry.getKey().onDraw(viewPort, entry.getValue());
				}

				// Initialize the FeatureSelectionRenderer:
				selectionRenderer.initialize(r.getMapInfo());

				addMapGadget(new ScalebarGadget(r.getMapInfo()));
				addMapGadget(new WatermarkGadget());
				addMapGadget(new NavigationGadget());

				// Fire initialization event:
				eventBus.fireEvent(new MapInitializationEvent());
			}
		});
		setSize(640, 480);
	}

	public Widget asWidget() {
		return display.asWidget();
	}

	public void setMapRenderer(MapRenderer mapRenderer) {
		this.mapRenderer = mapRenderer;
	}

	public void setSize(int width, int height) {
		display.asWidget().setSize(width + "px", height + "px");
		if (viewPort != null) {
			viewPort.setMapSize(width, height);
		}
		eventBus.fireEvent(new MapResizedEvent(width, height));
	}

	public VectorContainer addWorldContainer() {
		VectorContainer container = display.getNewWorldContainer();
		// set transform parameters once, after that all is handled by WorldContainerRenderer
		Matrix matrix = viewPort.getTransformationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		container.setScale(matrix.getXx(), matrix.getYy());
		container.setTranslation(matrix.getDx(), matrix.getDy());
		return container;
	}

	public VectorContainer addScreenContainer() {
		return display.getNewScreenContainer();
	}

	public boolean removeVectorContainer(VectorContainer container) {
		return display.removeVectorContainer(container);
	}

	public boolean bringToFront(VectorContainer container) {
		return display.bringToFront(container);
	}

	public LayersModel getLayersModel() {
		return layersModel;
	}

	public ViewPort getViewPort() {
		return viewPort;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

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

	public MapController getMapController() {
		return mapController;
	}

	public void setFallbackController(MapController fallbackController) {
		boolean fallbackActive = (mapController == this.fallbackController);
		this.fallbackController = fallbackController;
		if (mapController == null || fallbackActive) {
			setMapController(fallbackController);
		}
	}

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

	public Collection<MapListener> getMapListeners() {
		return listeners.keySet();
	}

	public Set<MapGadget> getMapGadgets() {
		return gadgets.keySet();
	}

	public void addMapGadget(MapGadget mapGadget) {
		VectorContainer container = addScreenContainer();
		gadgets.put(mapGadget, container);
		if (layersModel != null && viewPort != null) {
			mapGadget.onDraw(viewPort, container);
		}
	}

	public boolean removeMapGadget(MapGadget mapGadget) {
		if (gadgets.containsKey(mapGadget)) {
			mapGadget.onDestroy();
			display.removeVectorContainer(gadgets.get(mapGadget));
			gadgets.remove(mapGadget);
			return true;
		}
		return false;
	}

	public void setCursor(String cursor) {
		DOM.setStyleAttribute(display.asWidget().getElement(), "cursor", cursor);
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

		private VectorContainer container;

		private FeatureStyleInfo pointStyle;

		private FeatureStyleInfo lineStyle;

		private FeatureStyleInfo ringStyle;

		public FeatureSelectionRenderer() {
			container = addWorldContainer();
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
		}

		public void onShow(LayerShowEvent event) {
		}

		public void onHide(LayerHideEvent event) {
		}

		public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
		}

		private void render(Feature f) {
			Path path = gfxUtil.toPath(f.getGeometry());
			if (f.getGeometry() instanceof Point || f.getGeometry() instanceof MultiPoint) {
				gfxUtil.applyStyle(path, pointStyle);
			} else if (f.getGeometry() instanceof LineString || f.getGeometry() instanceof MultiLineString) {
				gfxUtil.applyStyle(path, lineStyle);
			} else if (f.getGeometry() instanceof Polygon || f.getGeometry() instanceof MultiPolygon
					|| f.getGeometry() instanceof LinearRing) {
				gfxUtil.applyStyle(path, ringStyle);
			}
			container.add(path);
		}
	}
}
