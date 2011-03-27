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
import org.geomajas.puregwt.client.map.event.EventBusImpl;
import org.geomajas.puregwt.client.map.event.LayerOrderChangedHandler;
import org.geomajas.puregwt.client.map.event.MapInitializationEvent;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.map.event.ViewPortDraggedEvent;
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
import org.geomajas.puregwt.client.spatial.GeometryFactoryImpl;

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

/**
 * ..........
 * 
 * @author Pieter De Graef
 */
public class MapPresenterImpl implements MapPresenter {

	/**
	 * ....
	 * 
	 * @author Pieter De Graef
	 */
	public interface MapWidget extends HasMouseDownHandlers, HasMouseUpHandlers, HasMouseOutHandlers,
			HasMouseOverHandlers, HasMouseMoveHandlers, HasMouseWheelHandlers, HasDoubleClickHandlers, IsWidget {

		HtmlContainer getMapHtmlContainer();

		VectorContainer getMapVectorContainer();

		ScreenContainer getScreenContainer(String id);

		void removeScreenContainer(ScreenContainer container);

		WorldContainer getWorldContainer(String id);

		void removeWorldContainer(WorldContainer container);

		List<WorldContainer> getWorldContainers();
	}

	private String applicationId;

	private String id;

	private MapWidget display;

	private List<HandlerRegistration> handlers;

	private MapController mapController;

	private MapController fallbackController;

	private Map<MapListener, List<HandlerRegistration>> listeners;

	private LayersModel layersModel;

	private ViewPortImpl viewPort;

	private MapRenderer mapRenderer;

	private WorldContainerRenderer worldContainerRenderer;

	private Map<String, MapGadget> gadgets;

	private EventBus eventBus;

	public MapPresenterImpl(String applicationId, String id, MapWidget display) {
		this.applicationId = applicationId;
		this.id = id;
		this.display = display;
		handlers = new ArrayList<HandlerRegistration>();
		listeners = new HashMap<MapListener, List<HandlerRegistration>>();
		gadgets = new HashMap<String, MapGadget>();

		eventBus = new EventBusImpl();
		layersModel = new LayersModelImpl(eventBus);
		viewPort = new ViewPortImpl(eventBus);
	}

	public void initialize() {
		// Initialize the default map renderer:
		mapRenderer = new DelegatingMapRenderer(layersModel, viewPort);
		mapRenderer.setHtmlContainer(display.getMapHtmlContainer());
		// TODO temp code:
		mapRenderer.setVectorContainer(display.getMapVectorContainer());

		eventBus.addHandler(ViewPortChangedEvent.getType(), mapRenderer);
		eventBus.addHandler(ViewPortDraggedEvent.getType(), mapRenderer);
		eventBus.addHandler(ViewPortScaledEvent.getType(), mapRenderer);
		eventBus.addHandler(ViewPortTranslatedEvent.getType(), mapRenderer);
		eventBus.addHandler(LayerOrderChangedHandler.TYPE, mapRenderer);

		worldContainerRenderer = new WorldContainerRenderer();
		eventBus.addHandler(ViewPortChangedEvent.getType(), worldContainerRenderer);
		eventBus.addHandler(ViewPortDraggedEvent.getType(), worldContainerRenderer);
		eventBus.addHandler(ViewPortScaledEvent.getType(), worldContainerRenderer);
		eventBus.addHandler(ViewPortTranslatedEvent.getType(), worldContainerRenderer);

		eventBus.addHandler(ViewPortChangedEvent.getType(), new MapGadgetRenderer());
		eventBus.addHandler(ViewPortTranslatedEvent.getType(), new MapGadgetRenderer());
		eventBus.addHandler(ViewPortScaledEvent.getType(), new MapGadgetRenderer());

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
					layersModel.initialize(r.getMapInfo(), viewPort);
					viewPort.initialize(r.getMapInfo());

					// Immediately zoom to the initial bounds as configured:
					GeometryFactory factory = new GeometryFactoryImpl();
					Bbox initialBounds = factory.createBbox(r.getMapInfo().getInitialBounds());
					viewPort.applyBounds(initialBounds);

					// If there are already some MapGadgets registered, draw them now:
					for (String containerId : gadgets.keySet()) {
						MapGadget mapGadget = gadgets.get(containerId);
						mapGadget.onDraw(viewPort, getScreenContainer(containerId));
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

	public void setMapRenderer(MapRenderer mapRenderer) {
		this.mapRenderer = mapRenderer;
	}

	public void setSize(int width, int height) {
		display.asWidget().setSize(width + "px", height + "px");
		if (viewPort != null) {
			viewPort.setMapSize(width, height);
		}
	}

	public WorldContainer getWorldContainer(String id) {
		WorldContainer container = display.getWorldContainer(id);
		container.transform(viewPort);
		return container;
	}

	public ScreenContainer getScreenContainer(String id) {
		return display.getScreenContainer(id);
	}

	public LayersModel getLayersModel() {
		return layersModel;
	}

	public ViewPort getViewPort() {
		return viewPort;
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
		String id = DOM.createUniqueId();
		gadgets.put(id, mapGadget);
		if (layersModel != null && viewPort != null) {
			mapGadget.onDraw(viewPort, getScreenContainer(id));
		}
	}

	public void removeMapGadget(MapGadget mapGadget) {
		if (gadgets.containsValue(mapGadget)) {
			mapGadget.onDestroy();
			for (String containerId : gadgets.keySet()) {
				display.removeScreenContainer(getScreenContainer(containerId));
			}
			gadgets.remove(mapGadget);
		}
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
	private class MapGadgetRenderer implements ViewPortChangedHandler {

		// TODO catch resize events as well

		public void onViewPortChanged(ViewPortChangedEvent event) {
			for (MapGadget mapGadget : gadgets.values()) {
				mapGadget.onScale();
				mapGadget.onTranslate();
			}
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			for (MapGadget mapGadget : gadgets.values()) {
				mapGadget.onScale();
			}
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			for (MapGadget mapGadget : gadgets.values()) {
				mapGadget.onTranslate();
			}
		}

		public void onViewPortDragged(ViewPortDraggedEvent event) {
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

		public void onViewPortDragged(ViewPortDraggedEvent event) {
			for (WorldContainer worldContainer : display.getWorldContainers()) {
				worldContainer.transform(viewPort);
			}
		}
	}
}