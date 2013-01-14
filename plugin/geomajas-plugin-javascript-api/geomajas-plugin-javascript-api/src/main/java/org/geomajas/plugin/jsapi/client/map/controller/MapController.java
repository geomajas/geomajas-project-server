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

package org.geomajas.plugin.jsapi.client.map.controller;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.Controller;
import org.geomajas.plugin.jsapi.client.map.ExportableFunction;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.user.client.DOM;

/**
 * General definition of a controller on the map. It defines a series of handlers to catch mouse/touch events, but also
 * has 2 extra handlers executed on activation and deactivation of this controller on the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
@Export
@ExportPackage("org.geomajas.jsapi.controller")
public class MapController implements Exportable {

	private Map map;

	private MouseMoveHandler mouseMoveHandler;

	private MouseOutHandler mouseOutHandler;

	private MouseOverHandler mouseOverHandler;

	private DownHandler downHandler;

	private UpHandler upHandler;

	private DragHandler dragHandler;

	private DoubleClickHandler doubleClickHandler;

	private ExportableFunction activationHandler;

	private ExportableFunction deactivationHandler;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public MapController() {
	}

	public MapController(Map map, final Controller controller) {
		this.map = map;
		mouseMoveHandler = new MouseMoveHandler() {

			public void onMouseMove(MouseMoveEvent event) {
				controller.onMouseMove(event);
			}
		};
		mouseOutHandler = new MouseOutHandler() {

			public void onMouseOut(MouseOutEvent event) {
				controller.onMouseOut(event);
			}
		};
		mouseOverHandler = new MouseOverHandler() {

			public void onMouseOver(MouseOverEvent event) {
				controller.onMouseOver(event);
			}
		};
		downHandler = new DownHandler() {

			public void onDown(HumanInputEvent<?> event) {
				if (event instanceof MouseDownEvent) {
					controller.onMouseDown((MouseDownEvent) event);
				} else if (event instanceof TouchStartEvent) {
					controller.onTouchStart((TouchStartEvent) event);
				}
			}
		};
		upHandler = new UpHandler() {

			public void onUp(HumanInputEvent<?> event) {
				if (event instanceof MouseUpEvent) {
					controller.onMouseUp((MouseUpEvent) event);
				} else if (event instanceof TouchEndEvent) {
					controller.onTouchEnd((TouchEndEvent) event);
				}
			}
		};
		dragHandler = new DragHandler() {

			public void onDrag(HumanInputEvent<?> event) {
				if (controller instanceof AbstractController) {
					((AbstractController) controller).onDrag(event);
				}
			}
		};
		doubleClickHandler = new DoubleClickHandler() {

			public void onDoubleClick(DoubleClickEvent event) {
				controller.onDoubleClick(event);
			}
		};
	}

	// ------------------------------------------------------------------------
	// Registering mouse event handlers:
	// ------------------------------------------------------------------------

	public void setMouseMoveHandler(MouseMoveHandler mouseMoveHandler) {
		this.mouseMoveHandler = mouseMoveHandler;
	}

	public void setMouseOutHandler(MouseOutHandler mouseOutHandler) {
		this.mouseOutHandler = mouseOutHandler;
	}

	public void setMouseOverHandler(MouseOverHandler mouseOverHandler) {
		this.mouseOverHandler = mouseOverHandler;
	}

	public void setDownHandler(DownHandler downHandler) {
		this.downHandler = downHandler;
	}

	public void setUpHandler(UpHandler upHandler) {
		this.upHandler = upHandler;
	}

	public void setDragHandler(DragHandler dragHandler) {
		this.dragHandler = dragHandler;
	}

	public void setDoubleClickHandler(DoubleClickHandler doubleClickHandler) {
		this.doubleClickHandler = doubleClickHandler;
	}

	public void setActivationHandler(ExportableFunction activationHandler) {
		this.activationHandler = activationHandler;
	}

	public void setDeactivationHandler(ExportableFunction deactivationHandler) {
		this.deactivationHandler = deactivationHandler;
	}

	public Coordinate getLocation(HumanInputEvent<?> event, String renderSpace) {
		Element el = DOM.getElementById(map.getHtmlElementId()).getFirstChildElement();
		Coordinate location = null;
		if (event instanceof MouseEvent) {
			MouseEvent<?> mEvent = (MouseEvent<?>) event;
			event.setRelativeElement(el);
			location = new Coordinate(mEvent.getX(), mEvent.getY());
		} else if (event instanceof TouchEvent) {
			TouchEvent<?> tEvent = (TouchEvent<?>) event;
			if (tEvent.getTouches().length() > 0) {
				location = new Coordinate(tEvent.getTouches().get(0).getRelativeX(el), tEvent.getTouches().get(0)
						.getRelativeY(el));
			}
		}
		if (location == null) {
			location = new Coordinate(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
		}
		if ("world".equalsIgnoreCase(renderSpace)) {
			return map.getViewPort().transform(location, "screen", renderSpace);
		}
		return location;
	}

	// ------------------------------------------------------------------------
	// Extra public methods:
	// ------------------------------------------------------------------------

	public MouseMoveHandler getMouseMoveHandler() {
		return mouseMoveHandler;
	}

	public MouseOutHandler getMouseOutHandler() {
		return mouseOutHandler;
	}

	public MouseOverHandler getMouseOverHandler() {
		return mouseOverHandler;
	}

	public DownHandler getDownHandler() {
		return downHandler;
	}

	public UpHandler getUpHandler() {
		return upHandler;
	}

	public DragHandler getDragHandler() {
		return dragHandler;
	}

	public DoubleClickHandler getDoubleClickHandler() {
		return doubleClickHandler;
	}

	public ExportableFunction getActivationHandler() {
		return activationHandler;
	}

	public ExportableFunction getDeactivationHandler() {
		return deactivationHandler;
	}

	@NoExport
	public Map getMap() {
		return map;
	}

	@NoExport
	public void setMap(Map map) {
		this.map = map;
	}
}