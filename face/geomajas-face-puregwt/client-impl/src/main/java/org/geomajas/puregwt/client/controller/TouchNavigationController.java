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

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.event.dom.client.GestureChangeEvent;
import com.google.gwt.event.dom.client.GestureEndEvent;
import com.google.gwt.event.dom.client.GestureStartEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;

/**
 * Generic controller that is used on touch mobile devices. Note that gestures and multi touch are not supported by some
 * mobile browsers.
 * 
 * @author Dosi Bingov
 * @since 1.0.0
 */
public class TouchNavigationController extends AbstractMapController {

	protected Coordinate touchedOrigin;

	protected Coordinate lastTouchedPosition;

	protected boolean zooming;

	protected boolean dragging;

	// middle point of a gesture spike !only for multi-touch supported browsers
	private Coordinate midPoint;

	public TouchNavigationController() {
		super(false);
	}

	// ------------------------------------------------------------------------
	// Touch events:
	// ------------------------------------------------------------------------

	@Override
	public void onTouchStart(TouchStartEvent event) {
		event.preventDefault();
		lastTouchedPosition = getLocation(event, RenderSpace.WORLD);
		touchedOrigin = getLocation(event, RenderSpace.SCREEN);
	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {
		// TODO find out why not fired
		updateView(event, true);
	}

	@Override
	public void onTouchMove(TouchMoveEvent event) {
		event.preventDefault();

		if (event.getTouches().length() > 1) {
			Coordinate p1 = new Coordinate(event.getTouches().get(0).getClientX(), event.getTouches().get(0)
					.getClientY());
			Coordinate p2 = new Coordinate(event.getTouches().get(1).getClientX(), event.getTouches().get(1)
					.getClientY());
			midPoint = mapPresenter.getViewPort().transform(getMidPoint(p1, p2), RenderSpace.SCREEN, RenderSpace.WORLD);
			return; // don't pan if more than two fingers are used applicable only for multi-touch supported browsers
		}

		updateView(event, true); // TODO second argument should be false but onTouchEnd is not fired
	}

	@Override
	public void onTouchCancel(TouchCancelEvent event) {
	}

	// ------------------------------------------------------------------------
	// Gesture events:
	// ------------------------------------------------------------------------

	@Override
	public void onGestureStart(GestureStartEvent event) {
		event.preventDefault();
	}

	@Override
	public void onGestureEnd(GestureEndEvent event) {
		event.preventDefault();

		if (midPoint != null) {
			mapPresenter.getViewPort().applyScale(event.getScale() * mapPresenter.getViewPort().getScale(), midPoint);
		} else {
			mapPresenter.getViewPort().applyScale(event.getScale() * mapPresenter.getViewPort().getScale());
		}
	}

	@Override
	public void onGestureChange(GestureChangeEvent event) {
		event.preventDefault();
	}

	// ------------------------------------------------------------------------
	// Methods:
	// ------------------------------------------------------------------------

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		this.eventParser = mapPresenter.getMapEventParser();
	}

	@Override
	public void onDeactivate(MapPresenter mapPresenter) {
	}

	/**
	 * Method used to calculate exact middle point between two end points on a line segment. (gesture spike)
	 * 
	 * @param p1
	 * @param p2
	 * @return middle point
	 */
	private Coordinate getMidPoint(Coordinate p1, Coordinate p2) {
		double x = (p1.getX() + p2.getX()) / 2;
		double y = (p1.getY() + p2.getY()) / 2;

		return new Coordinate(x, y);
	}

	/**
	 * 
	 * Update the view of the map when touching and dragging .
	 * 
	 * @param event
	 */
	protected void updateView(HumanInputEvent<?> event, boolean isTouchEnded) {
		Coordinate end = getLocation(event, RenderSpace.SCREEN);
		Coordinate beginWorld = mapPresenter.getViewPort().transform(touchedOrigin, RenderSpace.SCREEN,
				RenderSpace.WORLD);
		Coordinate endWorld = mapPresenter.getViewPort().transform(end, RenderSpace.SCREEN, RenderSpace.WORLD);
		double x = mapPresenter.getViewPort().getPosition().getX() + beginWorld.getX() - endWorld.getX();
		double y = mapPresenter.getViewPort().getPosition().getY() + beginWorld.getY() - endWorld.getY();

		if (isTouchEnded) {
			mapPresenter.getViewPort().applyPosition(new Coordinate(x, y));
		} else {
			mapPresenter.getViewPort().dragToPosition(new Coordinate(x, y));
		}

		touchedOrigin = end;
	}

}
