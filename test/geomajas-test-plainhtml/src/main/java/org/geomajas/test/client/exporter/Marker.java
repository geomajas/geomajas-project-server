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
package org.geomajas.test.client.exporter;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.WorldPaintable;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;

/**
 * A Google like marker. Painted in world space.
 * 
 * @author Jan De Moerloose
 * 
 */
public class Marker implements WorldPaintable, Painter {

	private String id;

	private String imageSrc;

	private Coordinate location;

	private Coordinate transformed;

	private int width;

	private int height;

	private PictureStyle style;

	public Marker(String id, String imageSrc, Coordinate location, int width, int height) {
		this.id = id;
		this.imageSrc = imageSrc;
		this.location = location;
		this.width = width;
		this.height = height;
		style = new PictureStyle(1.0);
	}

	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	public String getId() {
		return id;
	}

	public Object getOriginalLocation() {
		return location;
	}

	public void transform(WorldViewTransformer transformer) {
		transformed = transformer.worldToPan((Coordinate) getOriginalLocation());

	}

	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		Marker marker = (Marker) paintable;
		context.getVectorContext().deleteElement(group, marker.getId());
	}

	public String getPaintableClassName() {
		return getClass().getName();
	}

	public void paint(Paintable paintable, Object group, MapContext context) {
		context.getVectorContext().drawImage(group, getId(), imageSrc,
				new Bbox(transformed.getX(), transformed.getY(), width, height), style);
	}

	public String getImageSrc() {
		return imageSrc;
	}

	public Coordinate getLocation() {
		return transformed;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public PictureStyle getStyle() {
		return style;
	}

}
