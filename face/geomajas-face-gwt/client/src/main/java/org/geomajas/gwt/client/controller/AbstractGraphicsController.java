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

package org.geomajas.gwt.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.util.GwtEventUtil;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.TouchEvent;

/**
 * <p>
 * Abstraction of the <code>GraphicsController</code> that implements all methods as empty methods. By using this as a
 * starting point for your own controllers, you don't have to clutter your code with empty methods that you don't use
 * anyway.
 * </p>
 * <p>
 * What makes this class special is that it provides a few protected methods for easily acquiring information from the
 * mouse events. You can for example get the event's position, or target DOM element.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class AbstractGraphicsController extends AbstractController implements GraphicsController {

	private int offsetX;

	private int offsetY;

	protected MapWidget mapWidget;

	/**
	 * Constructor.
	 *
	 * @param mapWidget map widget
	 */
	protected AbstractGraphicsController(MapWidget mapWidget) {
		super(false);
		this.mapWidget = mapWidget;
		setMapEventParser(new GwtMapEventParser());
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	@Override
	public void onActivate() {
	}

	@Override
	public void onDeactivate() {
	}

	// -------------------------------------------------------------------------
	// Keeping the API check happy - Mouse handling methods:
	// -------------------------------------------------------------------------

	@Override
	public void onMouseDown(MouseDownEvent event) {
		super.onMouseDown(event);
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		super.onMouseUp(event);
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		super.onMouseMove(event);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		super.onMouseOut(event);
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		super.onMouseOver(event);
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		super.onMouseWheel(event);
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		super.onDoubleClick(event);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * An offset along the X-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @since 1.8.0
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354. No longer used.
	 */
	@Deprecated
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
	 * 
	 * @since 1.8.0
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354. No longer used.
	 */
	@Deprecated
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * An offset along the Y-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @since 1.8.0
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354. No longer used.
	 */
	@Deprecated
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
	 * @since 1.8.0
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354. No longer used.
	 */
	@Deprecated
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	// -------------------------------------------------------------------------
	// Helper functions on mouse events:
	// -------------------------------------------------------------------------

	protected WorldViewTransformer getTransformer() {
		return mapWidget.getMapModel().getMapView().getWorldViewTransformer();
	}

	// @extract-start AbstractGraphicsController, Extract from AbstractGraphicsController

	/**
	 * Use the <code>getLocation</code> method instead.
	 * 
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354.
	 */
	@Deprecated
	protected Coordinate getScreenPosition(MouseEvent<?> event) {
		return getLocation(event, RenderSpace.SCREEN);
	}

	protected Coordinate getClientPosition(MouseEvent<?> event) {
		return new Coordinate(event.getClientX(), event.getClientY());
	}

	protected Coordinate getPanPosition(MouseEvent<?> event) {
		Element element = mapWidget.getDOM();
		double offsetX = ((MouseEvent<?>) event).getRelativeX(element);
		double offsetY = ((MouseEvent<?>) event).getRelativeY(element);
		return getTransformer().viewToPan(new Coordinate(offsetX, offsetY));
	}

	/**
	 * Use the <code>getLocation</code> method instead.
	 * 
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354.
	 */
	@Deprecated
	protected Coordinate getWorldPosition(MouseEvent<?> event) {
		return getLocation(event, RenderSpace.WORLD);
	}

	protected Element getTarget(MouseEvent<?> event) {
		return GwtEventUtil.getTarget(event);
	}

	protected String getTargetId(MouseEvent<?> event) {
		return GwtEventUtil.getTargetId(event);
	}

	// @extract-end

	/**
	 * Specific implementation of the MapEventParser interface for this face.
	 * 
	 * @author Pieter De Graef
	 */
	private class GwtMapEventParser implements MapEventParser {

		@Override
		public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
			switch (renderSpace) {
				case WORLD:
					Coordinate screen = getLocation(event, RenderSpace.SCREEN);
					return mapWidget.getMapModel().getMapView().getWorldViewTransformer().viewToWorld(screen);
				case SCREEN:
				default:
					if (event instanceof MouseEvent<?>) {
						Element element = mapWidget.getDOM();
						double offsetX = ((MouseEvent<?>) event).getRelativeX(element);
						double offsetY = ((MouseEvent<?>) event).getRelativeY(element);
						return new Coordinate(offsetX, offsetY);
					} else if (event instanceof TouchEvent<?>) {
						Touch touch = ((TouchEvent<?>) event).getTouches().get(0);
						return new Coordinate(touch.getClientX(), touch.getClientY());
					}
					return new Coordinate(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
			}
		}

		@Override
		public Element getTarget(HumanInputEvent<?> event) {
			EventTarget target = event.getNativeEvent().getEventTarget();
			if (Element.is(target)) {
				return Element.as(target);
			}
			return null;
		}
	}
}