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

import java.util.Date;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.gfx.paintable.Rectangle;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.spatial.WorldViewTransformer;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.smartgwt.client.widget.MapWidget.RenderStatus;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.smartgwt.client.widgets.menu.Menu;

/**
 * Base controller which handles the display of the rectangle which allows selection.
 * 
 * @author Joachim Van der Auwera
 */
public abstract class AbstractRectangleController extends AbstractGraphicsController {

	protected Rectangle rectangle;

	protected boolean dragging;

	protected long timestamp;

	protected Coordinate begin;

	protected Bbox bounds;

	protected boolean shift;

	protected boolean leftWidget;

	protected ShapeStyle rectangleStyle = new ShapeStyle("#FF9900", 0.2f, "#FF9900", 1f, 2);

	protected Menu menu;

	public AbstractRectangleController(MapWidget mapWidget) {
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
	public void onDown(HumanInputEvent<?> event) {
		if (dragging && leftWidget) {
			// mouse was moved outside of widget
			doSelect(event);

		} else if (!isRightMouseButton(event)) {
			// no point trying to select when there is no active layer
			dragging = true;
			leftWidget = false;
			timestamp = new Date().getTime();
			begin = getLocation(event, RenderSpace.SCREEN);
			bounds = new Bbox(begin.getX(), begin.getY(), 0.0, 0.0);
			shift = event.isShiftKeyDown();
			rectangle = new Rectangle("selectionRectangle");
			rectangle.setStyle(rectangleStyle);
			rectangle.setBounds(bounds);
			mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.UPDATE);
		}
	}

	/**
	 * Finish selection using rectangle.
	 * 
	 * @param event
	 *            event
	 */
	@Override
	public void onUp(HumanInputEvent<?> event) {
		// assure dragging or clicking started inside this widget
		if (dragging) {
			doSelect(event);
		}
	}

	private void doSelect(HumanInputEvent<?> event) {
		dragging = false;
		shift |= event.isShiftKeyDown(); // shift is used when depressed either at beginning or end
		updateRectangle(event);

		WorldViewTransformer transformer = new WorldViewTransformer(mapWidget.getMapModel().getMapView());
		Bbox worldBounds = transformer.viewToWorld(bounds);
		selectRectangle(worldBounds);

		mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.DELETE);
	}

	/**
	 * Overwrite this method to handle the actual selection. The bounds variable contains the selected area.
	 * 
	 * @param worldBounds
	 *            bounds in world coordinates
	 */
	protected abstract void selectRectangle(Bbox worldBounds);

	@Override
	public void onDrag(HumanInputEvent<?> event) {
		if (dragging) {
			updateRectangle(event);
			mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.UPDATE);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		leftWidget = true;
	}

	protected void stopDragging() {
		if (dragging) {
			dragging = false;
			mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.DELETE);
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
		bounds.setX(x);
		bounds.setY(y);
		bounds.setWidth(width);
		bounds.setHeight(height);
	}

	@Override
	public void onDeactivate() {
		super.onDeactivate();
		// make sure to clean up
		stopDragging();
	}
}