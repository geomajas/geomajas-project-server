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
package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import java.util.List;

import org.geomajas.gwt.client.controller.ZoomSliderController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.user.client.Event;
import com.smartgwt.client.types.Cursor;

/**
 * <p>Based on the number of usable scales, the dimensions of the slider area is defined. 
 * On this rectangle the {@link ZoomSliderController} is set.</p>
 * 
 * @author Emiel Ackermann
 */
public class SliderArea extends SingleMapAddon {

	private List<Bbox> partBounds;
	private static final String PART = "part";
	private static final String MAP_AREA = "mapArea";
	
	protected SliderArea(String id, int width, int height, 
			Image sliderIcon, Image backgroundPart, List<Bbox> partBounds, 
			MapWidget mapWidget, ZoomSliderController zoomSliderController) {
		super(id, sliderIcon, width, height, mapWidget, zoomSliderController);
		this.setBackground(backgroundPart);
		this.partBounds = partBounds;
	}
	
	@Override
	public void accept(PainterVisitor visitor, Object group, Bbox bounds,
			boolean recursive) {
		GraphicsContext vectorContext = mapWidget.getVectorContext();
		this.group = group;
		// Manually set the map sizes and the internal margins before applying them on the background parts.
		setMapSize(mapWidget.getWidth(), mapWidget.getHeight());
		for (int i = 0 ; i < partBounds.size() ; i++) {
			vectorContext.drawImage(group, PART + i, getBackground().getHref(),
					applyMargins(partBounds.get(i)), getBackground().getStyle()); 
		}
		/*
		 * Slider icon
		 */
		drawImage(applyMargins(getIcon().getBounds()));
		/*
		 * Slider rectangle
		 */
		drawStartRectangle(((ZoomSlider) group).getSliderUnit().getBounds().getWidth());
		vectorContext.setController(group, MAP_AREA, getController(), Event.MOUSEEVENTS);
	}
	
	/**
	 * Provides a rectangle over the area over which the user can slide. 
	 * An onDown event redraws this rectangle into one that covers the map with {@link SliderArea#drawMapRectangle()}.
	 */
	public void drawStartRectangle(Double width) {
		GraphicsContext vectorContext = mapWidget.getVectorContext();
		vectorContext.drawRectangle(group, MAP_AREA, 
				applyMargins(new Bbox(0, 0, width, getHeight())), 
				// IE9 does not draw an empty shapestyle (new ShapeStyle()), but it does draw one with opacity = 0...
				new ShapeStyle("#FF0000", 0, "#FF0000", 0, 1)); 
//				new ShapeStyle("#FF0000", 0.1f, "#FF0000", 0.5f, 1));
		vectorContext.setCursor(group, MAP_AREA, Cursor.POINTER.getValue());
	}

	/**
	 * Provides a rectangle over the map, on which the onDrag event of {@link ZoomSliderController} is listening.
	 * An onUp event redraws this rectangle into the start rectangle with {@link SliderArea#drawStartRectangle()}.
	 */
	public void drawMapRectangle() {
		mapWidget.getVectorContext().drawRectangle(group, MAP_AREA, 
				new Bbox(0, 0, mapWidget.getWidth(), mapWidget.getHeight()), 
				// IE9 does not draw an empty shapestyle (new ShapeStyle()), but it does draw one with opacity = 0...
				new ShapeStyle("#00FF00", 0, "#00FF00", 0, 1));
//				new ShapeStyle("#00FF00", 0.1f, "#00FF00", 0.5f, 1));
	}
}
