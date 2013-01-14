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

package org.geomajas.plugin.editing.puregwt.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.gwt.client.handler.MapDragHandler;
import org.geomajas.gwt.client.handler.MapUpHandler;
import org.geomajas.plugin.editing.client.handler.AbstractGeometryIndexMapHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.puregwt.client.controller.AbstractMapController;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class CompositeGeometryIndexController extends AbstractMapController {

	private List<MapDownHandler> downHandlers = new ArrayList<MapDownHandler>();

	private List<MapUpHandler> upHandlers = new ArrayList<MapUpHandler>();

	private List<MapDragHandler> dragHandlers = new ArrayList<MapDragHandler>();

	private List<MouseMoveHandler> moveHandlers = new ArrayList<MouseMoveHandler>();

	private List<MouseOverHandler> overHandlers = new ArrayList<MouseOverHandler>();

	private List<MouseOutHandler> outHandlers = new ArrayList<MouseOutHandler>();

	private List<DoubleClickHandler> doubleClickHandlers = new ArrayList<DoubleClickHandler>();

	private GeometryEditService service;

	private GeometryIndex index;

	public CompositeGeometryIndexController(GeometryEditService service, GeometryIndex index, boolean dragging) {
		super(dragging);
		this.service = service;
		this.index = index;
	}

	// ------------------------------------------------------------------------
	// Adding handlers:
	// ------------------------------------------------------------------------

	public void addMapHandler(AbstractGeometryIndexMapHandler handler) {
		handler.setIndex(index);
		handler.setService(service);
		handler.setEventParser(this);
		if (handler instanceof MapDownHandler) {
			downHandlers.add((MapDownHandler) handler);
		}
		if (handler instanceof MapUpHandler) {
			upHandlers.add((MapUpHandler) handler);
		}
		if (handler instanceof MapDragHandler) {
			dragHandlers.add((MapDragHandler) handler);
		}
		if (handler instanceof MouseOverHandler) {
			overHandlers.add((MouseOverHandler) handler);
		}
		if (handler instanceof MouseOutHandler) {
			outHandlers.add((MouseOutHandler) handler);
		}
		if (handler instanceof MouseMoveHandler) {
			moveHandlers.add((MouseMoveHandler) handler);
		}
		if (handler instanceof DoubleClickHandler) {
			doubleClickHandlers.add((DoubleClickHandler) handler);
		}
	}

	public void addMapDownHandler(MapDownHandler handler) {
		downHandlers.add(handler);
	}

	public void addMapUpHandler(MapUpHandler handler) {
		upHandlers.add(handler);
	}

	public void addMapDragHandler(MapDragHandler handler) {
		dragHandlers.add(handler);
	}

	public void addMouseMoveHandler(MouseMoveHandler handler) {
		moveHandlers.add(handler);
	}

	public void addMouseOverHandler(MouseOverHandler handler) {
		overHandlers.add(handler);
	}

	public void addMouseOutHandler(MouseOutHandler handler) {
		outHandlers.add(handler);
	}

	public void addDoubleClickHandler(DoubleClickHandler handler) {
		doubleClickHandlers.add(handler);
	}

	// ------------------------------------------------------------------------
	// Handler implementation methods:
	// ------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		super.onMouseDown(event);
	}

	public void onDown(HumanInputEvent<?> event) {
		if (service.getIndexStateService().isEnabled(index)) {
			for (MapDownHandler handler : downHandlers) {
				handler.onDown(event);
			}
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		if (service.getIndexStateService().isEnabled(index)) {
			for (MapUpHandler handler : upHandlers) {
				handler.onUp(event);
			}
		}
	}

	public void onDrag(HumanInputEvent<?> event) {
		if (service.getIndexStateService().isEnabled(index)) {
			for (MapDragHandler handler : dragHandlers) {
				handler.onDrag(event);
			}
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (service.getIndexStateService().isEnabled(index)) {
			super.onMouseMove(event); // Make sure we don't forget dragging.
			for (MouseMoveHandler handler : moveHandlers) {
				handler.onMouseMove(event);
			}
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (service.getIndexStateService().isEnabled(index)) {
			for (MouseOutHandler handler : outHandlers) {
				handler.onMouseOut(event);
			}
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		if (service.getIndexStateService().isEnabled(index)) {
			for (MouseOverHandler handler : overHandlers) {
				handler.onMouseOver(event);
			}
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		if (service.getIndexStateService().isEnabled(index)) {
			for (DoubleClickHandler handler : doubleClickHandlers) {
				handler.onDoubleClick(event);
			}
		}
	}
}