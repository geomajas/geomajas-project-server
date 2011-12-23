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
package org.geomajas.plugin.jsapi.smartgwt.client.exporter.map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.jsapi.client.event.JsEventBus;
import org.geomajas.plugin.jsapi.client.map.ExportableFunction;
import org.geomajas.plugin.jsapi.client.map.LayersModel;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.ViewPort;
import org.geomajas.plugin.jsapi.client.map.controller.MapController;
import org.geomajas.plugin.jsapi.client.map.feature.FeatureSearchService;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.event.JsEventBusImpl;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.feature.FeatureSearchServiceImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.DOM;

/**
 * Map for the GWT face.
 * 
 * @author Pieter De Graef
 * @author Oliver May
 */
@Export("Map")
@ExportPackage("org.geomajas.jsapi.map")
public class MapImpl implements Exportable, Map {

	private JsEventBus eventBus;

	private MapWidget mapWidget;

	private ViewPort viewPort;

	private LayersModel layersModel;

	private FeatureSearchService featureSearchService;

	private String htmlElementId;

	// If this is removed, we get errors from the GWT exporter...
	public MapImpl() {
	}

	/**
	 * Create a facade for the given {@link MapWidget}, available trough JavaScript.
	 * 
	 * @param mapWidget
	 *            the {@link MapWidget} object.
	 * @since 1.0.0
	 */
	@Api
	public MapImpl(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		eventBus = new JsEventBusImpl(this);
		viewPort = new ViewPortImpl(mapWidget.getMapModel().getMapView());
		layersModel = new LayersModelImpl(mapWidget.getMapModel());
		featureSearchService = new FeatureSearchServiceImpl(this);
	}

	// ------------------------------------------------------------------------
	// Map implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void setHtmlElementId(String id) {
		htmlElementId = id;
		mapWidget.setHtmlElement(DOM.getElementById(id));
		mapWidget.draw();
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
	public void setMapController(MapController mapController) {
		if (mapController != null) {
			mapController.setMap(this);
			mapWidget.setController(new JsController(mapWidget, mapController));
		} else {
			mapWidget.setController(null);
		}
	}

	/** {@inheritDoc} */
	public MapController getMapController() {
		final GraphicsController controller = mapWidget.getController();
		MapController mapController = new MapController(this, controller);
		mapController.setActivationHandler(new ExportableFunction() {

			public void execute() {
				controller.onActivate();
			}
		});
		mapController.setDeactivationHandler(new ExportableFunction() {

			public void execute() {
				controller.onDeactivate();
			}
		});
		return mapController;
	}

	/** {@inheritDoc} */
	public void setSize(int width, int height) {
		mapWidget.setWidth(width);
		mapWidget.setHeight(height);
	}

	/** {@inheritDoc} */
	public void setCursor(String cursor) {
		mapWidget.setCursorString(cursor);
	}

	public String getHtmlElementId() {
		return htmlElementId;
	}

	/** {@inheritDoc} */
	public JsEventBus getEventBus() {
		return eventBus;
	}

	/** {@inheritDoc} */
	public FeatureSearchService getFeatureSearchService() {
		return featureSearchService;
	}

	// ------------------------------------------------------------------------
	// Other public methods:
	// ------------------------------------------------------------------------

	@NoExport
	public MapWidget getMapWidget() {
		return mapWidget;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * JavaScript to GWT controller wrapper.
	 * 
	 * @author Pieter De Graef
	 */
	private class JsController extends AbstractGraphicsController {

		private MapController mapController;

		public JsController(MapWidget mapWidget, MapController mapController) {
			super(mapWidget);
			this.mapController = mapController;
		}

		public void onActivate() {
			if (mapController.getActivationHandler() != null) {
				mapController.getActivationHandler().execute();
			}
		}

		public void onDeactivate() {
			if (mapController.getDeactivationHandler() != null) {
				mapController.getDeactivationHandler().execute();
			}
		}

		public void onMouseMove(MouseMoveEvent event) {
			if (mapController.getMouseMoveHandler() != null) {
				mapController.getMouseMoveHandler().onMouseMove(event);
			}
		}

		public void onMouseOver(MouseOverEvent event) {
			if (mapController.getMouseOverHandler() != null) {
				mapController.getMouseOverHandler().onMouseOver(event);
			}
		}

		public void onMouseOut(MouseOutEvent event) {
			if (mapController.getMouseOutHandler() != null) {
				mapController.getMouseOutHandler().onMouseOut(event);
			}
		}

		public void onDown(HumanInputEvent<?> event) {
			if (mapController.getDownHandler() != null) {
				mapController.getDownHandler().onDown(event);
			}
		}

		public void onUp(HumanInputEvent<?> event) {
			if (mapController.getUpHandler() != null) {
				mapController.getUpHandler().onUp(event);
			}
		}

		public void onDrag(HumanInputEvent<?> event) {
			if (mapController.getDragHandler() != null) {
				mapController.getDragHandler().onDrag(event);
			}
		}

		public void onDoubleClick(DoubleClickEvent event) {
			if (mapController.getDoubleClickHandler() != null) {
				mapController.getDoubleClickHandler().onDoubleClick(event);
			}
		}
	}
}