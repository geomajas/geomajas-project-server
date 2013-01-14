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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * <p>
 * Map add-on that displays a "powered by geomajas" text on the bottom right of the map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Watermark extends MapAddon {

	private MapWidget map;

	private Image image;

	private Rectangle dummy;

	// Constructor:

	public Watermark(String id, MapWidget map) {
		super(id, 125, 12);
		this.map = map;
		image = new Image(id + "-img");
		image.setBounds(new Bbox(0, 0, 125, 12));
		image.setHref(Geomajas.getIsomorphicDir() + "geomajas/mapaddon/powered_by_geomajas.gif");
		image.setStyle(new PictureStyle(1));

		dummy = new Rectangle(getId() + "-dummy");
		dummy.setStyle(new ShapeStyle("#FFFFFF", 0, "#FFFFFF", 0, 0));
		dummy.setBounds(new Bbox(0, 0, 1, 1));
	}

	// MapAddon implementation:

	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		map.getVectorContext().drawGroup(group, this);

		// Draw a dummy at 0,0 so that Internet Explorer knows where coordinate 0,0 is. If this is not drawn, the text
		// will disappear, because the parent group will have coordinate 0,0 at the upper left corner of the union of
		// all the rectangles that are drawn here.
		map.getVectorContext().drawRectangle(this, dummy.getId(), dummy.getBounds(), dummy.getStyle());

		image.getBounds().setX(getUpperLeftCorner().getX());
		image.getBounds().setY(getUpperLeftCorner().getY());

		map.getVectorContext().drawRectangle(this, getId() + "-bg", image.getBounds(),
				new ShapeStyle("#FFFFFF", 0.6f, "#FFFFFF", 0, 0));
		map.getVectorContext().drawImage(this, image.getId(), image.getHref(), image.getBounds(),
				(PictureStyle) image.getStyle());
	}

	public void onDraw() {
	}

	public void onRemove() {
	}
}
