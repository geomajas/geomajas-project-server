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

package org.geomajas.plugin.editing.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.gwt.client.handler.MapEventParser;
import org.geomajas.gwt.client.handler.MapHandler;
import org.geomajas.gwt.client.handler.MapMoveHandler;
import org.geomajas.gwt.client.handler.MapOutHandler;
import org.geomajas.gwt.client.handler.MapOverHandler;
import org.geomajas.gwt.client.handler.MapUpHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.handler.GeometryIndexMapHandler;
import org.geomajas.plugin.editing.client.handler.GwtMapEventParser;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class CompositeGeometryIndexController extends AbstractGraphicsController implements MapHandler {

	private List<MapDownHandler> downHandlers = new ArrayList<MapDownHandler>();

	private List<MapUpHandler> upHandlers = new ArrayList<MapUpHandler>();

	private List<MapMoveHandler> moveHandlers = new ArrayList<MapMoveHandler>();

	private List<MapOverHandler> overHandlers = new ArrayList<MapOverHandler>();

	private List<MapOutHandler> outHandlers = new ArrayList<MapOutHandler>();

	private List<DoubleClickHandler> doubleClickHandlers = new ArrayList<DoubleClickHandler>();

	private GeometryEditingService service;

	private GeometryIndex index;
	
	private GwtMapEventParser eventParser;

	public CompositeGeometryIndexController(MapWidget map, GeometryEditingService service, GeometryIndex index) {
		super(map);
		this.service = service;
		this.index = index;
		
		eventParser = new GwtMapEventParser(map, getOffsetX(), getOffsetY());
	}
	
	public MapEventParser getEventParser() {
		return eventParser;
	}

	public void addMapHandler(GeometryIndexMapHandler handler) {
		handler.setIndex(index);
		handler.setService(service);
		handler.setMapWidget(mapWidget);
		handler.setEventParser(eventParser);
		if (handler instanceof MapDownHandler) {
			downHandlers.add((MapDownHandler) handler);
		}
		if (handler instanceof MapUpHandler) {
			upHandlers.add((MapUpHandler) handler);
		}
		if (handler instanceof MapMoveHandler) {
			moveHandlers.add((MapMoveHandler) handler);
		}
		if (handler instanceof MapOverHandler) {
			overHandlers.add((MapOverHandler) handler);
		}
		if (handler instanceof MapOutHandler) {
			outHandlers.add((MapOutHandler) handler);
		}
	}

	public void addMapDownHandler(MapDownHandler handler) {
		downHandlers.add((MapDownHandler) handler);
	}

	public void addMapUpHandler(MapUpHandler handler) {
		upHandlers.add((MapUpHandler) handler);
	}

	public void addMapMoveHandler(MapMoveHandler handler) {
		moveHandlers.add((MapMoveHandler) handler);
	}

	public void addMapOverHandler(MapOverHandler handler) {
		overHandlers.add((MapOverHandler) handler);
	}

	public void addMapOutHandler(MapOutHandler handler) {
		outHandlers.add((MapOutHandler) handler);
	}

	public void onMouseDown(MouseDownEvent event) {
		for (MapDownHandler handler : downHandlers) {
			handler.onDown(event);
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		for (MapMoveHandler handler : moveHandlers) {
			handler.onMove(event);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		for (MapOutHandler handler : outHandlers) {
			handler.onOut(event);
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		for (MapOverHandler handler : overHandlers) {
			handler.onOver(event);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		for (MapUpHandler handler : upHandlers) {
			handler.onUp(event);
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		for (DoubleClickHandler handler : doubleClickHandlers) {
			handler.onDoubleClick(event);
		}
	}

	public void onDeactivate() {
		// Clean up handlers???
	}
	
	@Override
	public void setOffsetX(int offsetX) {
		super.setOffsetX(offsetX);
		eventParser.setOffsetX(offsetX);
	}
	
	@Override
	public void setOffsetY(int offsetY) {
		super.setOffsetY(offsetY);
		eventParser.setOffsetY(offsetY);
	}
}