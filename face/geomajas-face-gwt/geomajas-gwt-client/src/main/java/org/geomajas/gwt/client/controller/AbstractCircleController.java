/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * <p>
 * Base controller which handles the display of circles, by dragging the mouse. When creating extensions of this class,
 * one should always implement the <code>onCircleReady</code> method. It is called when a circle has been successfully
 * drawn (on the mouse up event).
 * </p>
 * <p>
 * This class also has a few protected methods that may assist in getting crucial data regarding the circle that is
 * being or has been drawn. See <code>getScreenCenter</code>, <code>getScreenRadius</code>, <code>getWorldCenter</code>,
 * <code>getWorldRadius</code>.
 * </p>
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractCircleController extends AbstractGraphicsController {

	private PaintableGroup circleGroup = new Composite("circle");

	private Coordinate center; // In screen coordinates.

	private double radius;

	protected ShapeStyle circleStyle = new ShapeStyle("#FF9900", 0.2f, "#FF9900", 0.8f, 2);

	protected boolean dragging;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public AbstractCircleController(MapWidget mapWidget) {
		super(mapWidget);
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	/**
	 * Register center point for the circle, and start dragging and rendering.
	 * 
	 * @param event
	 *            event
	 */
	public void onMouseDown(MouseDownEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			dragging = true;
			center = getScreenPosition(event);

			LineString radiusLine = mapWidget.getMapModel().getGeometryFactory().createLineString(
					new Coordinate[] { center, center });
			mapWidget.getVectorContext().drawGroup(mapWidget.getGroup(RenderGroup.SCREEN), circleGroup);
			mapWidget.getVectorContext().drawCircle(circleGroup, "outer", center, 1.0f, circleStyle);
			mapWidget.getVectorContext().drawCircle(circleGroup, "center", center, 2.0f, circleStyle);
			mapWidget.getVectorContext().drawLine(circleGroup, "radius", radiusLine, circleStyle);
		}
	}

	/**
	 * Finish drawing the circle. At this point, the circle is removed from the map, but not before the
	 * <code>onCircleReady</code> method is called. This abstract method should be implemented by extending classes.
	 * Therein, one can call all the protected methods in this class (<code>getScreenCenter</code>,
	 * <code>getScreenRadius</code>, <code>getWorldCenter</code>, <code>getWorldRadius</code>).
	 */
	public void onMouseUp(MouseUpEvent event) {
		if (dragging) { // Assure dragging or clicking started inside this widget.
			updateGraphics(event);
			onCircleReady();
			stopDragging();
		}
	}

	/** Update the rendering of the circle. */
	public void onMouseMove(MouseMoveEvent event) {
		if (dragging) { // Assure dragging or clicking started inside this widget.
			updateGraphics(event);
		}
	}

	/** Remove the circle from the map. */
	public void onMouseOut(MouseOutEvent event) {
		stopDragging();
	}

	// -------------------------------------------------------------------------
	// Protected methods:
	// -------------------------------------------------------------------------

	/**
	 * Method one should implement when using this class. It is called when a circle has been successfully drawn. (on
	 * the mouse up event)
	 */
	protected abstract void onCircleReady();

	/** Return the center position of the circle in screen coordinates. */
	protected Coordinate getScreenCenter() {
		return center;
	}

	/** Return the circle's radius in screen pixels. */
	protected double getScreenRadius() {
		return radius;
	}

	/** Return the center position of the circle in world coordinates. */
	protected Coordinate getWorldCenter() {
		if (center != null) {
			return mapWidget.getMapModel().getMapView().getWorldViewTransformer().viewToWorld(center);
		}
		return null;
	}

	/** Return the circle's radius in world length units. */
	protected double getWorldRadius() {
		if (center != null) {
			Coordinate screenEndPoint = new Coordinate(center.getX() + radius, center.getY());
			Coordinate worldEndPoint = mapWidget.getMapModel().getMapView().getWorldViewTransformer().viewToWorld(
					screenEndPoint);
			double deltaX = worldEndPoint.getX() - getWorldCenter().getX();
			double deltaY = worldEndPoint.getY() - getWorldCenter().getY();
			return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
		}
		return 0;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void stopDragging() {
		if (dragging) {
			dragging = false;
			mapWidget.getVectorContext().deleteGroup(circleGroup);
			center = null;
			radius = 0;
		}
	}

	private void updateGraphics(MouseEvent<?> event) {
		Coordinate position = getScreenPosition(event);
		double deltaX = position.getX() - getScreenCenter().getX();
		double deltaY = position.getY() - getScreenCenter().getY();
		radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

		LineString radiusLine = mapWidget.getMapModel().getGeometryFactory().createLineString(
				new Coordinate[] { center, position });
		mapWidget.getVectorContext().drawCircle(circleGroup, "outer", center, radius, circleStyle);
		mapWidget.getVectorContext().drawLine(circleGroup, "radius", radiusLine, circleStyle);
	}
}
