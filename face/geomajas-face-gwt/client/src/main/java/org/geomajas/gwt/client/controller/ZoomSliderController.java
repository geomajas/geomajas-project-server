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
package org.geomajas.gwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.SliderArea;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.ZoomSlider;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.spatial.Bbox;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Controller that redraws the drag-able slider icon and zooms to the level the icon was placed on.
 * <ol>
 * <li>
 * Starts with the rectangle on which the zoomslider handler can move. See {@link SliderArea#drawStartRectangle()}.
 * </li>
 * <li>
 * After a onDown event on the start rectangle, this rectangle is drawn again, 
 * but now with width and height of the map. See {@link SliderArea#drawMapRectangle()}.
 * </li>
 * <li>
 * After a onUp event on the map rectangle, this rectangle returns to its starting dimensions. 
 * The last Y position is processed into a scale, which is used to zoom.
 * </li>
 * </ol>
 * 
 * @author Emiel Ackermann
 */
public class ZoomSliderController extends AbstractGraphicsController {

	private ZoomSlider zoomSlider;
	private SliderArea sliderArea;
	
	public ZoomSliderController(ZoomSlider zoomSlider) {
		super(zoomSlider.getMapWidget());
		this.zoomSlider = zoomSlider;
		this.sliderArea = zoomSlider.getSliderArea();
	}
	
	@Override
	public void onDown(HumanInputEvent<?> event) {
		event.stopPropagation();
		double y = getLocation(event, RenderSpace.SCREEN).getY();
		double x = getLocation(event, RenderSpace.SCREEN).getX();
		System.out.println("Down event.getY() = " + y);
		System.out.println("Down event.getX() = " + x);
		Bbox sliderAreaBounds = sliderArea.getAddonBounds();
		Coordinate origin = sliderAreaBounds.getOrigin();
		Coordinate endPoint = sliderAreaBounds.getEndPoint();
		if (origin.getX() < x && endPoint.getX() > x &&
			origin.getY() < y && endPoint.getY() > y) {
			dragging = true;
			sliderArea.drawMapRectangle();
		}
	}
	
	@Override
	public void onUp(HumanInputEvent<?> event) {
		event.stopPropagation();
		dragging = false;
		double lastY = getLocation(event, RenderSpace.SCREEN).getY();
		System.out.println("Up event.getY() = " + lastY);
		lastY = validateY(lastY);
		zoom(lastY);
		drawSliderIcon(lastY);
		sliderArea.drawStartRectangle(zoomSlider.getSliderUnit().getBounds().getWidth());
	}
	
	@Override
	public void onDrag(HumanInputEvent<?> event) {
		event.stopPropagation();
		double y = getLocation(event, RenderSpace.SCREEN).getY();
		if (dragging) {
			System.out.println("Move event.getY() = " + y);
			y = validateY(y);
			drawSliderIcon(y);
			System.out.println("Move validated = " + y);
		}
	}
	
	private double validateY(double y) {
		y -= 2 * (zoomSlider.getVerticalMargin() + sliderArea.getVerticalMargin()) + 5;
		double startY = 0;
		double endY = sliderArea.getHeight() - zoomSlider.getSliderUnit().getBounds().getHeight();
		if (y > endY) {
			y = endY;
		} else if (y < startY) {
			y = startY;
		}
		return y;
	}
	
	private void drawSliderIcon(double y) {
		Bbox bounds = (Bbox) sliderArea.getIcon().getBounds();
		bounds.setY(y);
		sliderArea.drawImage(sliderArea.applyMargins(bounds));
	}
	
	private void zoom(double y) {
		int index = (int) (y / zoomSlider.getSliderUnit().getBounds().getHeight());
		Double targetScale = zoomSlider.getCurrentScaleList().get(index);
		System.out.println("Zoomed to: " + targetScale);
		mapWidget.getMapModel().getMapView().setCurrentScale(targetScale, ZoomOption.LEVEL_CLOSEST);
	}
}
