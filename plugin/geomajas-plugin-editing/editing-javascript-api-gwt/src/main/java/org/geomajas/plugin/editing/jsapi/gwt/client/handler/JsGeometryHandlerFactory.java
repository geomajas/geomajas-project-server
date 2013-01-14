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
package org.geomajas.plugin.editing.jsapi.gwt.client.handler;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.gwt.client.handler.MapDragHandler;
import org.geomajas.gwt.client.handler.MapUpHandler;
import org.geomajas.plugin.editing.client.handler.AbstractGeometryIndexMapHandler;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.gwt.client.handler.EditingHandlerRegistry.GeometryHandlerFactory;
import org.geomajas.plugin.jsapi.client.map.controller.DoubleClickHandler;
import org.geomajas.plugin.jsapi.client.map.controller.DownHandler;
import org.geomajas.plugin.jsapi.client.map.controller.DragHandler;
import org.geomajas.plugin.jsapi.client.map.controller.MouseMoveHandler;
import org.geomajas.plugin.jsapi.client.map.controller.MouseOutHandler;
import org.geomajas.plugin.jsapi.client.map.controller.MouseOverHandler;
import org.geomajas.plugin.jsapi.client.map.controller.UpHandler;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;

/**
 * JavaScript wrapper of {@link GeometryHandlerFactory}. This factory creates a singleton object.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api
@Export("GeometryHandlerFactory")
@ExportPackage("org.geomajas.plugin.editing.handler")
public class JsGeometryHandlerFactory implements GeometryHandlerFactory, Exportable {

	private AbstractGeometryIndexMapHandler handler = new GeometryIndexMapAdapter();

	private MouseMoveHandler mouseMoveHandler = new MouseAdapter();

	private MouseOutHandler mouseOutHandler = new MouseAdapter();

	private MouseOverHandler mouseOverHandler = new MouseAdapter();

	private DownHandler downHandler = new MouseAdapter();

	private UpHandler upHandler = new MouseAdapter();

	private DragHandler dragHandler = new MouseAdapter();

	private DoubleClickHandler doubleClickHandler = new MouseAdapter();

	@NoExport
	public AbstractGeometryIndexMapHandler create() {
		return handler;
	}

	/**
	 * Set the closure that handles {@link MouseMoveEvent} events.
	 * 
	 * @param mouseMoveHandler the closure
	 */
	@Api
	public void setMouseMoveHandler(MouseMoveHandler mouseMoveHandler) {
		this.mouseMoveHandler = mouseMoveHandler;
	}

	/**
	 * Set the closure that handles {@link MouseOutEvent} events.
	 * 
	 * @param mouseOutHandler the closure
	 */
	@Api
	public void setMouseOutHandler(MouseOutHandler mouseOutHandler) {
		this.mouseOutHandler = mouseOutHandler;
	}

	/**
	 * Set the closure that handles {@link MouseOverEvent} events.
	 * 
	 * @param mouseOverHandler the closure
	 */
	@Api
	public void setMouseOverHandler(MouseOverHandler mouseOverHandler) {
		this.mouseOverHandler = mouseOverHandler;
	}

	/**
	 * Set the closure that handles mouse down or touch start events.
	 * 
	 * @param downHandler the closure
	 */
	@Api
	public void setDownHandler(DownHandler downHandler) {
		this.downHandler = downHandler;
	}

	/**
	 * Set the closure that handles mouse up or touch end events.
	 * 
	 * @param upHandler the closure
	 */
	@Api
	public void setUpHandler(UpHandler upHandler) {
		this.upHandler = upHandler;
	}

	/**
	 * Set the closure that handles drag events.
	 * 
	 * @param dragHandler the closure
	 */
	@Api
	public void setDragHandler(DragHandler dragHandler) {
		this.dragHandler = dragHandler;
	}

	/**
	 * Set the closure that handles {@link DoubleClickEvent} events.
	 * 
	 * @param doubleClickHandler the closure
	 */
	@Api
	public void setDoubleClickHandler(DoubleClickHandler doubleClickHandler) {
		this.doubleClickHandler = doubleClickHandler;
	}

	/**
	 * Returns the index of the geometry part on which the event occurs.
	 * 
	 * @return the index
	 */
	@Api
	public GeometryIndex getIndex() {
		return handler.getIndex();
	}

	/**
	 * Forwards events to the closure functions.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class GeometryIndexMapAdapter extends AbstractGeometryIndexMapHandler implements
			com.google.gwt.event.dom.client.MouseMoveHandler, com.google.gwt.event.dom.client.MouseOutHandler,
			com.google.gwt.event.dom.client.MouseOverHandler, MapDownHandler, MapUpHandler, MapDragHandler,
			com.google.gwt.event.dom.client.DoubleClickHandler {

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			doubleClickHandler.onDoubleClick(event);
		}

		@Override
		public void onDrag(HumanInputEvent<?> event) {
			dragHandler.onDrag(event);
		}

		@Override
		public void onUp(HumanInputEvent<?> event) {
			upHandler.onUp(event);
		}

		@Override
		public void onDown(HumanInputEvent<?> event) {
			downHandler.onDown(event);
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			mouseOverHandler.onMouseOver(event);
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			mouseOutHandler.onMouseOut(event);
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			mouseMoveHandler.onMouseMove(event);
		}

	}

	/**
	 * Noop implementation of all mouse events.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class MouseAdapter implements MouseMoveHandler, MouseOutHandler, MouseOverHandler, DownHandler, UpHandler,
			DragHandler, DoubleClickHandler {

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
		}

		@Override
		public void onDrag(HumanInputEvent<?> event) {
		}

		@Override
		public void onUp(HumanInputEvent<?> event) {
		}

		@Override
		public void onDown(HumanInputEvent<?> event) {
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
		}
	}

}
