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
package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.map.cache.tile.RasterTile;

/**
 * Paints a raster tile.
 *
 * @author Jan De Moerloose
 */
public class RasterTilePainter implements Painter {

	public RasterTilePainter() {
	}

	public String getPaintableClassName() {
		return RasterTile.class.getName();
	}

	public void paint(Paintable paintable, Object group, GraphicsContext graphics) {
		RasterTile tile = (RasterTile) paintable;
		graphics.drawImage(tile.getStore().getLayer(), 
				tile.getCode().toString(), tile.getUrl(), tile.getBounds(), tile.getStyle());
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link GraphicsContext}. It the object does not exist,
	 * nothing will be done.
	 * 
	 * @param paintable
	 *            The object to be painted.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, GraphicsContext graphics) {
		RasterTile tile = (RasterTile) paintable;
		graphics.deleteElement(tile.getStore().getLayer(), tile.getCode().toString());
	}
}

