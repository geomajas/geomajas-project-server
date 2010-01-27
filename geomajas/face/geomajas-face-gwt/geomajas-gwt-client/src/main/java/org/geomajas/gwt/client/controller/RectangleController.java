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

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.widgets.menu.Menu;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.transform.WorldViewTransformer;
import org.geomajas.gwt.client.widget.MapWidget;

import java.util.Date;

/**
 * Base controller which handles the display of the rectangle which allows selection.
 * 
 * @author Joachim Van der Auwera
 */
public abstract class RectangleController extends AbstractGraphicsController {

	protected Rectangle rectangle;

	protected boolean dragging;

	protected long timestamp;

	protected Coordinate begin;

	protected Bbox bounds;

	protected boolean shift;

	protected ShapeStyle rectangleStyle = new ShapeStyle("#FF9900", 0.2f, "#FF9900", 1f, 2);

	protected Menu menu;

	public RectangleController(MapWidget mapWidget) {
		super(mapWidget);
	}

	public ShapeStyle getRectangleStyle() {
		return rectangleStyle;
	}

	public void setRectangleStyle(ShapeStyle rectangleStyle) {
		this.rectangleStyle = rectangleStyle;
	}

	/**
	 * Start dragging, register base for selection rectangle.
	 * 
	 * @param event
	 *            event
	 */
	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			// no point trying to select when there is no active layer
			dragging = true;
			timestamp = new Date().getTime();
			begin = getScreenPosition(event);
			bounds = new Bbox(begin.getX(), begin.getY(), 0.0, 0.0);
			shift = event.isShiftKeyDown();
			rectangle = new Rectangle("selectionRectangle");
			rectangle.setStyle(rectangleStyle);
			rectangle.setBounds(bounds);
			mapWidget.render(rectangle, "update");
		}
	}

	/**
	 * Finish selection using rectangle.
	 * 
	 * @param event
	 *            event
	 */
	@Override
	public void onMouseUp(MouseUpEvent event) {
		// assure dragging or clicking started inside this widget
		if (dragging) {
			dragging = false;
			shift |= event.isShiftKeyDown(); // shift is used when depressed either at beginning or end
			updateRectangle(event);

			WorldViewTransformer transformer = new WorldViewTransformer(mapWidget.getMapModel().getMapView());
			Bbox worldBounds = transformer.viewToWorld(bounds);
			selectRectangle(worldBounds);

			mapWidget.render(rectangle, "delete");
		}
	}

	/**
	 * Overwrite this method to handle the actual selection. The bounds variable contains the selected area.
	 * 
	 * @param worldBounds
	 *            bounds in world coordinates
	 */
	protected abstract void selectRectangle(Bbox worldBounds);

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (dragging) {
			updateRectangle(event);
			mapWidget.render(rectangle, "update");
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		stopDragging();
	}

	protected void stopDragging() {
		if (dragging) {
			dragging = false;
			mapWidget.render(rectangle, "delete");
		}
	}

	private void updateRectangle(MouseEvent<?> event) {
		Coordinate pos = getScreenPosition(event);
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
		bounds.setX(x);
		bounds.setY(y);
		bounds.setWidth(width);
		bounds.setHeight(height);
	}
}
