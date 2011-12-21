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

package org.geomajas.puregwt.client.map.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;

/**
 * <p>
 * Abstraction of the <code>MapController</code> that implements all methods as empty methods. By using this as a
 * starting point for your own controllers, you don't have to clutter your code with empty methods that you don't use
 * anyway.
 * </p>
 * <p>
 * What makes this class special is that it provides a few protected methods for easily acquiring information from the
 * mouse events. You can for example get the event's position, or target DOM element.
 * </p>
 * <p>
 * TODO Are the offset parameters still needed?
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
public abstract class AbstractMapController implements MapController {

	private int offsetX;

	private int offsetY;

	protected MapPresenter mapPresenter;

	protected AbstractMapController() {
	}

	// -------------------------------------------------------------------------
	// Empty implementations of all the event handler functions:
	// -------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
	}

	public void onMouseUp(MouseUpEvent event) {
	}

	public void onMouseMove(MouseMoveEvent event) {
	}

	public void onMouseOut(MouseOutEvent event) {
	}

	public void onMouseOver(MouseOverEvent event) {
	}

	public void onMouseWheel(MouseWheelEvent event) {
	}

	public void onDoubleClick(DoubleClickEvent event) {
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	public void onActivate(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	public void onDeactivate(MapPresenter mapPresenter) {
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * An offset along the X-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * An offset along the X-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @param offsetX
	 *            Set the actual offset value in pixels.
	 */
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * An offset along the Y-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * An offset along the Y-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @param offsetY
	 *            Set the actual offset value in pixels.
	 */
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	// -------------------------------------------------------------------------
	// Helper functions on mouse events:
	// -------------------------------------------------------------------------

	protected Coordinate getScreenPosition(MouseEvent<?> event) {
		return new Coordinate(event.getX(), event.getY());
	}

	protected Coordinate getWorldPosition(MouseEvent<?> event) {
		Coordinate coordinate = new Coordinate(event.getX(), event.getY());
		return mapPresenter.getViewPort().transform(coordinate, RenderSpace.SCREEN, RenderSpace.WORLD);
	}

	protected Element getTarget(MouseEvent<?> event) {
		EventTarget target = event.getNativeEvent().getEventTarget();
		if (Element.is(target)) {
			return Element.as(target);
		}
		return null;
	}

	protected String getTargetId(MouseEvent<?> event) {
		Element element = getTarget(event);
		if (element != null) {
			return element.getId();
		}
		return null;
	}
}