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

package org.geomajas.puregwt.client.controller;

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
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
public abstract class AbstractMapController implements MapController {

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