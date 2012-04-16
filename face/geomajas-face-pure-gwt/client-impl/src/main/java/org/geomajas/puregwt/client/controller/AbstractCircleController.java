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

package org.geomajas.puregwt.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ViewPort;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Circle;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;

/**
 * <p>
 * Abstract map controller implementation that lets the user drag a circle on the map. When the user lets go of the
 * mouse button, the "execute" methods will be triggered, providing it the Circle in world space.
 * </p>
 * <p>
 * This class serves as a base for other map controllers that have a need for a circle to be drawn (dragged) on the
 * map, so that behaviour is always the same.
 * </p>
 * 
 * @author Emiel Ackermann
 * @since 1.0.0
 */
@UserImplemented
@Api(allMethods = true)
public abstract class AbstractCircleController extends AbstractMapController {

	protected String fillColor = "#FF9900";

	protected float fillOpacity = 0.2f;

	protected String strokeColor = "#FF9900";

	protected float strokeOpacity = 1f;

	protected int strokeWidth = 2;

	protected Coordinate begin;
	
	private Circle circle;

	private double radius;

	protected boolean shift;

	protected VectorContainer container;
	
	protected boolean dragging;

	private Line line;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public AbstractCircleController() {
		super(false);
	}

	// ------------------------------------------------------------------------
	// MapController implementation:
	// ------------------------------------------------------------------------

	public void onActivate(MapPresenter mapPresenter) {
		super.onActivate(mapPresenter);
		getContainer();
	}

	public void onDown(HumanInputEvent<?> event) {
		if (!isRightMouseButton(event)) {
			dragging = true;
			begin = getLocation(event, RenderSpace.SCREEN);
			shift = event.isShiftKeyDown();
			
			circle = new Circle((int) begin.getX(), (int) begin.getY(), 0);
			circle.setFillColor(fillColor);
			circle.setFillOpacity(fillOpacity);
			circle.setStrokeColor(strokeColor);
			circle.setStrokeWidth(strokeWidth);
			getContainer().add(circle);
			
			line = new Line((int) begin.getX(), (int) begin.getY(), (int) begin.getX(), (int) begin.getY());
			line.setStrokeColor(strokeColor);
			line.setStrokeWidth(strokeWidth);
			getContainer().add(line);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		// Assure dragging or clicking started inside this widget
		if (dragging) {
			shift |= event.isShiftKeyDown(); // shift is used when depressed either at beginning or end
			updateCircle(event);
			
			Geometry geometry = new Geometry(Geometry.POINT, 0, -1);
			Coordinate[] coordinates = new Coordinate[]{new Coordinate(circle.getUserX(), circle.getUserY())};
			geometry.setCoordinates(coordinates);

			ViewPort viewPort = mapPresenter.getViewPort();
			execute(viewPort.transform(geometry, RenderSpace.SCREEN, RenderSpace.WORLD), 
					circle.getRadius() / viewPort.getScale());

			stopDragging();
			dragging = false;
		}
	}

	public void onDrag(HumanInputEvent<?> event) {
		updateCircle(event);
	}

	public void onMouseOut(MouseOutEvent event) {
		stopDragging();
		dragging = false;
	}

	/**
	 * Overwrite this method to handle the actual selection. 
	 * The {@link Geometry} variable contains the center point of the circle.
	 * 
	 * @param geometry
	 *            center point of circle in world coordinates
	 * @param radius
	 * 			  radius of the circle
	 */
	public abstract void execute(Geometry geometry, double radius);

	// ------------------------------------------------------------------------
	// Getters and setters for the style of the rectangle:
	// ------------------------------------------------------------------------

	public String getRectangleFillColor() {
		return fillColor;
	}

	public void setRectangleFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public float getRectangleFillOpacity() {
		return fillOpacity;
	}

	public void setRectangleFillOpacity(float fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public String getRectangleStrokeColor() {
		return strokeColor;
	}

	public void setRectangleStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	public float getRectangleStrokeOpacity() {
		return strokeOpacity;
	}

	public void setRectangleStrokeOpacity(float strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}

	public int getRectangleStrokeWidth() {
		return strokeWidth;
	}

	public void setRectangleStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void stopDragging() {
		if (dragging) {
			dragging = false;
			getContainer().remove(circle);
			getContainer().remove(line);
		}
	}

	private void updateCircle(HumanInputEvent<?> event) {
		Coordinate pos = getLocation(event, RenderSpace.SCREEN);
		double x = begin.getX();
		double y = begin.getY();
		double currentX = pos.getX();
		double currentY = pos.getY();
		
		double deltaX = currentX - x;
		double deltaY = currentY - y;
		radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
		
		circle.setRadius((int) radius);
		
		line.setUserX2(currentX);
		line.setUserY2(currentY);
	}

	private VectorContainer getContainer() {
		if (container == null) {
			container = mapPresenter.addScreenContainer();
		}
		return container;
	}
}