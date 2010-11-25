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
