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

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.puregwt.client.command.Command;
import org.geomajas.puregwt.client.command.CommandCallback;
import org.geomajas.puregwt.client.command.CommandService;
import org.geomajas.puregwt.client.map.controller.ListenerController;
import org.geomajas.puregwt.client.map.controller.MapController;
import org.geomajas.puregwt.client.map.controller.MapListener;
import org.geomajas.puregwt.client.map.controller.NavigationController;
import org.geomajas.puregwt.client.map.event.EventBus;
import org.geomajas.puregwt.client.map.event.LayerOrderChangedHandler;
import org.geomajas.puregwt.client.map.event.LayerStyleChangedHandler;
import org.geomajas.puregwt.client.map.event.LayerVisibilityHandler;
import org.geomajas.puregwt.client.map.event.MapCompositionHandler;
import org.geomajas.puregwt.client.map.event.MapInitializationEvent;
import org.geomajas.puregwt.client.map.event.MapResizedEvent;
import org.geomajas.puregwt.client.map.event.MapResizedHandler;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.gadget.NavigationGadget;
import org.geomajas.puregwt.client.map.gadget.ScalebarGadget;
import org.geomajas.puregwt.client.map.gadget.WatermarkGadget;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.gfx.ScreenContainer;
import org.geomajas.puregwt.client.map.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.gfx.WorldContainer;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.GeometryFactory;

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
			HasMouseOverHandlers, HasMouseMoveHandlers, HasMouseWheelHandlers, HasDoubleClickHandlers, IsWidget {

		HtmlContainer getMapHtmlContainer();

		VectorContainer getMapVectorContainer();

		ScreenContainer getNewScreenContainer();

		WorldContainer getNewWorldContainer();

		List<WorldContainer> getWorldContainers();

		boolean removeVectorContainer(VectorContainer container);

		boolean bringToFront(VectorContainer container);

		VectorContainer getMapGadgetContainer();

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

	private Map<MapGadget, ScreenContainer> gadgets;

	@Inject
	private EventBus eventBus;

	@Inject
	private MapWidget display;

	@Inject
	private GeometryFactory factory;

	@Inject
	private MapPresenterImpl() {
		handlers = new ArrayList<HandlerRegistration>();
		listeners = new HashMap<MapListener, List<HandlerRegistration>>();
		gadgets = new HashMap<MapGadget, ScreenContainer>();
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

		worldContainerRenderer = new WorldContainerRenderer();
		eventBus.addHandler(ViewPortChangedHandler.TYPE, worldContainerRenderer);

		MapGadgetRenderer mapGadgetRenderer = new MapGadgetRenderer();
		eventBus.addHandler(ViewPortChangedHandler.TYPE, mapGadgetRenderer);
		eventBus.addHandler(MapResizedEvent.TYPE, mapGadgetRenderer);

		setFallbackController(new NavigationController());

		Command commandRequest = new Command(GetMapConfigurationRequest.COMMAND);
		commandRequest.setCommandRequest(new GetMapConfigurationRequest(id, applicationId));
		CommandService cmdService = new CommandService();
		cmdService.execute(commandRequest, new CommandCallback() {

			public void onSuccess(CommandResponse response) {
				if (response instanceof GetMapConfigurationResponse) {
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
					for (Entry<MapGadget, ScreenContainer> entry : gadgets.entrySet()) {
						entry.getKey().onDraw(viewPort, entry.getValue());
					}

					addMapGadget(new ScalebarGadget(r.getMapInfo()));
					addMapGadget(new WatermarkGadget());
					addMapGadget(new NavigationGadget());

					// Fire initialization event:
					eventBus.fireEvent(new MapInitializationEvent());
				}
			}

			public void onFailure(Throwable error) {
			}
		});
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

	public WorldContainer addWorldContainer() {
		WorldContainer worldContainer = display.getNewWorldContainer();
		worldContainer.transform(viewPort);
		return worldContainer;
	}

	public ScreenContainer addScreenContainer() {
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

	public void addMapGadget(MapGadget mapGadget) {
		ScreenContainer container = addScreenContainer();
		gadgets.put(mapGadget, container);
		if (layersModel != null && viewPort != null) {
			mapGadget.onDraw(viewPort, container);
		}
	}

	public boolean removeMapGadget(MapGadget mapGadget) {
		if (gadgets.containsValue(mapGadget)) {
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
			for (WorldContainer worldContainer : display.getWorldContainers()) {
				worldContainer.transform(viewPort);
			}
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			for (WorldContainer worldContainer : display.getWorldContainers()) {
				worldContainer.transform(viewPort);
			}
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			for (WorldContainer worldContainer : display.getWorldContainers()) {
				worldContainer.transform(viewPort);
			}
		}
	}
}