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

package org.geomajas.puregwt.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ViewPort;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.DOM;

/**
 * Generic navigation map controller. This controller allows for panning and zooming on the map in many different ways.
 * Options are the following:
 * <ul>
 * <li><b>Panning</b>: Drag the map to pan.</li>
 * <li><b>Double click</b>: Double clicking on some location will see the map zoom in to that location.</li>
 * <li><b>Zoom to rectangle</b>: By holding shift or ctrl and dragging at the same time, a rectangle will appear on the
 * map. On mouse up, the map will than zoom to that rectangle.</li>
 * <li></li>
 * </ul>
 * For zooming using the mouse wheel there are 2 options, defined by the {@link ScrollZoomType} enum. These options are
 * the following:
 * <ul>
 * <li><b>ScrollZoomType.ZOOM_CENTER</b>: Zoom in/out so that the current center of the map remains.</li>
 * <li><b>ScrollZoomType.ZOOM_POSITION</b>: Zoom in/out so that the mouse world position remains the same. Can be great
 * for many subsequent double clicks, to make sure you keep zooming to the same location (wherever the mouse points to).
 * </li>
 * </ul>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class NavigationController extends AbstractMapController {

	/** Zooming types on mouse wheel scroll. */
	public static enum ScrollZoomType {
		/** When scroll zooming, retain the center of the map position. */
		ZOOM_CENTER,

		/** When scroll zooming, retain the mouse position. */
		ZOOM_POSITION
	}

	private final ZoomToRectangleController zoomToRectangleController;

	protected Coordinate dragOrigin;

	protected Coordinate lastClickPosition;

	protected boolean zooming;

	protected boolean dragging;

	private ScrollZoomType scrollZoomType = ScrollZoomType.ZOOM_POSITION;

	protected Rectangle rectangle;

	protected VectorContainer container;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a controller.
	 */
	public NavigationController() {
		super(false);
		zoomToRectangleController = new ZoomToRectangleController();
	}

	// ------------------------------------------------------------------------
	// MapController implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void onActivate(MapPresenter mapPresenter) {
		super.onActivate(mapPresenter);
		zoomToRectangleController.onActivate(mapPresenter);
	}

	/** {@inheritDoc} */
	public void onMouseDown(MouseDownEvent event) {
		super.onMouseDown(event);
		if (event.isControlKeyDown() || event.isShiftKeyDown()) {
			// Trigger the dragging on the zoomToRectangleController:
			zoomToRectangleController.onMouseDown(event);
		}
	}

	/** {@inheritDoc} */
	public void onDown(HumanInputEvent<?> event) {
		if (event.isControlKeyDown() || event.isShiftKeyDown()) {
			zooming = true;
		} else if (!isRightMouseButton(event)) {
			dragging = true;
			dragOrigin = getLocation(event, RenderSpace.SCREEN);
			mapPresenter.setCursor("move");
			// capture all events with an invisible rectangle (we need an element for setCapture())
			rectangle = new Rectangle(0, 0, 0, 0);
			rectangle.setFillOpacity(0);
			rectangle.setStrokeOpacity(0);
			// the rectangle captures all events, but we still have to register ourselves...
			rectangle.addMouseMoveHandler(this);
			rectangle.addMouseUpHandler(this);
			getContainer().add(rectangle);
			DOM.setCapture(rectangle.getElement());
		}
		lastClickPosition = getLocation(event, RenderSpace.WORLD);
	}

	/** {@inheritDoc} */
	public void onUp(HumanInputEvent<?> event) {
		if (zooming) {
			zoomToRectangleController.onUp(event);
			zooming = false;
		} else if (event.getSource() == rectangle) {
			// DOM.setCapture() is active !
			stopPanning(event);
		}
	}

	/** {@inheritDoc} */
	public void onMouseMove(MouseMoveEvent event) {
		if (zooming) {
			zoomToRectangleController.onMouseMove(event);
		} else if (event.getSource() == rectangle) {
			// DOM.setCapture() is active !
			super.onMouseMove(event);
		}
	}

	/** {@inheritDoc} */
	public void onDrag(HumanInputEvent<?> event) {
		updateView(event);
	}

	/** {@inheritDoc} */
	public void onMouseOut(MouseOutEvent event) {
		if (zooming) {
			zoomToRectangleController.onMouseOut(event);
		} else {
			stopPanning(null);
		}
	}

	/** {@inheritDoc} */
	public void onDoubleClick(DoubleClickEvent event) {
		// Zoom in on the event location:
		Bbox bounds = mapPresenter.getViewPort().getBounds();
		double x = lastClickPosition.getX() - (bounds.getWidth() / 4);
		double y = lastClickPosition.getY() - (bounds.getHeight() / 4);
		Bbox newBounds = new Bbox(x, y, bounds.getWidth() / 2, bounds.getHeight() / 2);
		mapPresenter.getViewPort().applyBounds(newBounds);
	}

	/** {@inheritDoc} */
	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		final boolean isNorth;
		if (event.getDeltaY() == 0) {
			isNorth = (getWheelDelta(event.getNativeEvent()) < 0);
		} else {
			isNorth = event.isNorth();
		}
		Coordinate location = getLocation(event, RenderSpace.WORLD);
		scrollZoomTo(isNorth, location);
	}

	protected native int getWheelDelta(NativeEvent evt) /*-{
		return Math.round(-evt.wheelDelta) || 0;
	}-*/;

	protected void scrollZoomTo(boolean isNorth, Coordinate location) {
		ViewPort viewPort = mapPresenter.getViewPort();
		int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
		if (isNorth) {
			if (index > 0) {
				if (scrollZoomType == ScrollZoomType.ZOOM_POSITION) {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index - 1), location);
				} else {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index - 1));
				}
			}
		} else {
			if (index < viewPort.getZoomStrategy().getZoomStepCount() - 1) {
				if (scrollZoomType == ScrollZoomType.ZOOM_POSITION) {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index + 1), location);
				} else {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index + 1));
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the scroll zoom type of this controller.
	 * 
	 * @return the scroll zoom type.
	 */
	public ScrollZoomType getScrollZoomType() {
		return scrollZoomType;
	}

	/**
	 * Set the scroll zoom type of this controller.
	 * 
	 * @param scrollZoomType the scroll zoom type.
	 */
	public void setScrollZoomType(ScrollZoomType scrollZoomType) {
		this.scrollZoomType = scrollZoomType;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected void stopPanning(HumanInputEvent<?> event) {
		dragging = false;
		mapPresenter.setCursor("default");
		// release capture
		if (rectangle != null) {
			DOM.releaseCapture(rectangle.getElement());
			getContainer().remove(rectangle);
		}
		if (null != event) {
			updateView(event);
		}
	}

	protected void updateView(HumanInputEvent<?> event) {
		Coordinate end = getLocation(event, RenderSpace.SCREEN);
		Coordinate beginWorld = mapPresenter.getViewPort().transform(dragOrigin, RenderSpace.SCREEN, RenderSpace.WORLD);
		Coordinate endWorld = mapPresenter.getViewPort().transform(end, RenderSpace.SCREEN, RenderSpace.WORLD);

		double x = mapPresenter.getViewPort().getPosition().getX() + beginWorld.getX() - endWorld.getX();
		double y = mapPresenter.getViewPort().getPosition().getY() + beginWorld.getY() - endWorld.getY();
		mapPresenter.getViewPort().applyPosition(new Coordinate(x, y));
		dragOrigin = end;
	}
	
	private VectorContainer getContainer() {
		if (container == null) {
			container = mapPresenter.addScreenContainer();
		}
		return container;
	}

}