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
import org.geomajas.smartgwt.client.gfx.paintable.mapaddon.SliderArea;
import org.geomajas.smartgwt.client.gfx.paintable.mapaddon.ZoomSlider;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.smartgwt.client.spatial.Bbox;

import com.google.gwt.event.dom.client.HumanInputEvent;
import org.geomajas.smartgwt.client.map.MapView;

/**
 * Controller that redraws the drag-able slider icon and zooms to the level the icon was placed on.
 * <ol>
 * <li>
 * Starts with the rectangle on which the zoomslider handler can move. See {@link org.geomajas.smartgwt.client.gfx
 * .paintable.mapaddon.SliderArea#drawStartRectangle()}.
 * </li>
 * <li>
 * After a onDown event on the start rectangle, this rectangle is drawn again, 
 * but now with width and height of the map. See {@link org.geomajas.smartgwt.client.gfx.paintable.mapaddon
 * .SliderArea#drawMapRectangle()}.
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
	
	public ZoomSliderController(ZoomSlider zoomSlider) {
		super(zoomSlider.getMapWidget());
		this.zoomSlider = zoomSlider;
	}
	
	@Override
	public void onDown(HumanInputEvent<?> event) {
		SliderArea sliderArea = zoomSlider.getSliderArea();
		event.stopPropagation();
		double y = getLocation(event, RenderSpace.SCREEN).getY();
		double x = getLocation(event, RenderSpace.SCREEN).getX();
		Bbox sliderAreaBounds = sliderArea.getAddonBounds();
		Coordinate origin = sliderAreaBounds.getOrigin();
		Coordinate endPoint = sliderAreaBounds.getEndPoint();
		if (origin.getX() < x && endPoint.getX() > x &&
			origin.getY() < y && endPoint.getY() > y) {
			dragging = true;
			sliderArea.drawMapRectangle();
		}
		event.preventDefault();
	}
	
	@Override
	public void onUp(HumanInputEvent<?> event) {
		event.stopPropagation();
		dragging = false;
		double lastY = getLocation(event, RenderSpace.SCREEN).getY();
		lastY = validateY(lastY);
		zoom(lastY);
		drawSliderIcon(lastY);
		zoomSlider.getSliderArea().drawStartRectangle(zoomSlider.getSliderUnit().getBounds().getWidth());
		event.preventDefault();
	}
	
	@Override
	public void onDrag(HumanInputEvent<?> event) {
		event.stopPropagation();
		double y = getLocation(event, RenderSpace.SCREEN).getY();
		if (dragging) {
			y = validateY(y);
			drawSliderIcon(y);
		}
		event.preventDefault();
	}
	
	/**
	 * Checks if the given y is within the slider area's range. 
	 * If not the initial y is corrected to either the top y or bottom y.
	 * @param y initial y 
	 * @return validated y
	 */
	private double validateY(double y) {
		SliderArea sliderArea = zoomSlider.getSliderArea();
		y -= zoomSlider.getVerticalMargin() + sliderArea.getVerticalMargin() + 5;
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
		SliderArea sliderArea = zoomSlider.getSliderArea();
		Bbox bounds = (Bbox) sliderArea.getIcon().getBounds();
		bounds.setY(y);
		sliderArea.drawImage(sliderArea.applyMargins(bounds));
	}
	
	private void zoom(double y) {
		int index = (int) (y / zoomSlider.getSliderUnit().getBounds().getHeight());
		Double targetScale = zoomSlider.getCurrentScaleList().get(index);
		mapWidget.getMapModel().getMapView().setCurrentScale(targetScale, MapView.ZoomOption.LEVEL_CLOSEST);
	}
}
