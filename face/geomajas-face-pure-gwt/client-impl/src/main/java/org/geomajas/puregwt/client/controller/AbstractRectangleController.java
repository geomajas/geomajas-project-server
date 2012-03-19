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
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;

/**
 * <p>
 * Abstract map controller implementation that lets the user drag a rectangle on the map. When the user lets go of the
 * mouse button, the "execute" methods will be triggered, providing it the rectangle bounds in world space.
 * </p>
 * <p>
 * This class serves as a base for other map controllers that have a need for a rectangle to be drawn (dragged) on the
 * map, so that behaviour is always the samen. An example implementation can be found in the
 * {@link ZoomToRectangleController}.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@UserImplemented
@Api(allMethods = true)
public abstract class AbstractRectangleController extends AbstractMapController {

	protected Rectangle rectangle;

	protected String fillColor = "#FF9900";

	protected float fillOpacity = 0.2f;

	protected String strokeColor = "#FF9900";

	protected float strokeOpacity = 1f;

	protected int strokeWidth = 2;

	protected Coordinate begin;

	protected boolean shift;

	protected VectorContainer container;
	
	protected boolean dragging;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public AbstractRectangleController() {
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
			rectangle = new Rectangle((int) begin.getX(), (int) begin.getY(), 0, 0);
			rectangle.setFillColor(fillColor);
			rectangle.setFillOpacity(fillOpacity);
			rectangle.setStrokeColor(strokeColor);
			rectangle.setStrokeOpacity(strokeOpacity);
			rectangle.setStrokeWidth(strokeWidth);
			getContainer().add(rectangle);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		// Assure dragging or clicking started inside this widget
		if (dragging) {
			shift |= event.isShiftKeyDown(); // shift is used when depressed either at beginning or end
			updateRectangle(event);

			Bbox bounds = new Bbox(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
			execute(mapPresenter.getViewPort().transform(bounds, RenderSpace.SCREEN, RenderSpace.WORLD));

			stopDragging();
			dragging = false;
		}
	}

	public void onDrag(HumanInputEvent<?> event) {
		updateRectangle(event);
	}

	public void onMouseOut(MouseOutEvent event) {
		stopDragging();
		dragging = false;
	}

	/**
	 * Overwrite this method to handle the actual selection. The bounds variable contains the selected area.
	 * 
	 * @param worldBounds
	 *            bounds in world coordinates
	 */
	public abstract void execute(Bbox worldBounds);

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
			getContainer().remove(rectangle);
		}
	}

	private void updateRectangle(HumanInputEvent<?> event) {
		Coordinate pos = getLocation(event, RenderSpace.SCREEN);
		double x = begin.getX();
		double y = begin.getY();
		double width = pos.getX() - x;
		double height = pos.getY() - y;
		if (width < 0) {
			x = pos.getX();
			width = -width;
		}
		if (height < 0) {
			y = pos.getY();
			height = -height;
		}
		rectangle.setX((int) x);
		rectangle.setY((int) y);
		rectangle.setWidth((int) width);
		rectangle.setHeight((int) height);
	}

	private VectorContainer getContainer() {
		if (container == null) {
			container = mapPresenter.addScreenContainer();
		}
		return container;
	}
}