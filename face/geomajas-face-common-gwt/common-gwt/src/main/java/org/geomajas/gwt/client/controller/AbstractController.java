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

package org.geomajas.gwt.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.gwt.client.handler.MapDragHandler;
import org.geomajas.gwt.client.handler.MapUpHandler;
import org.geomajas.gwt.client.map.RenderSpace;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;

/**
 * <p>
 * Base implementation of the {@link Controller} interface that tries to align mouse and touch behavior. It does this by
 * providing extra methods through the {@link MapDownHandler}, {@link MapUpHandler} and {@link MapDragHandler}
 * interfaces. When using this class a base (which is recommended), you can chose whether to support mouse events only,
 * touch events only or both simultaneously.
 * </p>
 * <p>
 * In short, here are your three options:
 * <ul>
 * <li>Supporting mouse events only: Override the mouse handler methods (onMouseDown, onMouseUp, ....)</li>
 * <li>Supporting touch events only: Override the touch handler methods (onTouchStart, onTouchMove, ...)</li>
 * <li>Supporting both (recommended): Override the onDown, onUp and onDrag methods. By default both the onMouseDown and
 * the onTouchStart will invoke the onDown method. The same goes for the onUp and onDrag. So by implementing those
 * methods you will have both mobile and desktop support.</li>
 * </ul>
 * </p>
 * <p>
 * One extra note to point out is that by default the touch event methods will stop any further propagation and prevent
 * the default behavior of the events. This is done because, by default, browsers on mobile devices tend to scroll all
 * over the place - creating unwanted behavior.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public abstract class AbstractController implements Controller, MapDownHandler, MapUpHandler, MapDragHandler {

	protected boolean dragging;

	protected MapEventParser eventParser;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Construct controller.
	 *
	 * @param dragging are we dragging?
	 */
	public AbstractController(boolean dragging) {
		this.dragging = dragging;
	}

	/**
	 * Construact controller.
	 *
	 * @param eventParser event parser
	 * @param dragging are we dragging
	 */
	public AbstractController(MapEventParser eventParser, boolean dragging) {
		this.dragging = dragging;
		this.eventParser = eventParser;
	}

	// ------------------------------------------------------------------------
	// MapEventParser implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
		return eventParser.getLocation(event, renderSpace);
	}

	/** {@inheritDoc} */
	public Element getTarget(HumanInputEvent<?> event) {
		return eventParser.getTarget(event);
	}

	protected void setMapEventParser(MapEventParser eventParser) {
		this.eventParser = eventParser;
	}

	/** {@inheritDoc} */
	public boolean isRightMouseButton(HumanInputEvent<?> event) {
		if (event instanceof MouseEvent<?>) {
			return ((MouseEvent<?>) event).getNativeButton() == NativeEvent.BUTTON_RIGHT;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// Methods for aligning mouse and touch events:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void onDown(HumanInputEvent<?> event) {
	}

	/** {@inheritDoc} */
	public void onUp(HumanInputEvent<?> event) {
	}

	/** {@inheritDoc} */
	public void onDrag(HumanInputEvent<?> event) {
	}

	/** {@inheritDoc} */
	public boolean isDragging() {
		return dragging;
	}

	// ------------------------------------------------------------------------
	// Mouse Handler implementations:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void onMouseDown(MouseDownEvent event) {
		dragging = true;
		onDown(event);
	}

	/** {@inheritDoc} */
	public void onMouseUp(MouseUpEvent event) {
		dragging = false;
		onUp(event);
	}

	/** {@inheritDoc} */
	public void onMouseMove(MouseMoveEvent event) {
		if (dragging) {
			onDrag(event);
		}
	}

	/** {@inheritDoc} */
	public void onMouseOut(MouseOutEvent event) {
	}

	/** {@inheritDoc} */
	public void onMouseOver(MouseOverEvent event) {
	}

	/** {@inheritDoc} */
	public void onMouseWheel(MouseWheelEvent event) {
	}

	/** {@inheritDoc} */
	public void onDoubleClick(DoubleClickEvent event) {
	}

	// ------------------------------------------------------------------------
	// Touch Handler implementations:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void onTouchStart(TouchStartEvent event) {
		onDown(event);
		event.stopPropagation();
		event.preventDefault();
	}

	/** {@inheritDoc} */
	public void onTouchMove(TouchMoveEvent event) {
		onDrag(event);
		event.stopPropagation();
		event.preventDefault();
	}

	/** {@inheritDoc} */
	public void onTouchEnd(TouchEndEvent event) {
		onUp(event);
		event.stopPropagation();
		event.preventDefault();
	}

	/** {@inheritDoc} */
	public void onTouchCancel(TouchCancelEvent event) {
		onUp(event);
		event.stopPropagation();
		event.preventDefault();
	}
}