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

package org.geomajas.smartgwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.gfx.PaintableGroup;
import org.geomajas.smartgwt.client.gfx.paintable.Composite;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.MapWidget.RenderGroup;

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
