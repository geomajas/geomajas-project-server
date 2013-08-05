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
package org.geomajas.smartgwt.client.gfx.painter;

import org.geomajas.smartgwt.client.gfx.MapContext;
import org.geomajas.smartgwt.client.gfx.Paintable;
import org.geomajas.smartgwt.client.gfx.Painter;
import org.geomajas.smartgwt.client.map.cache.tile.RasterTile;

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

	/**
	 * The actual painting function. Draws the groups.
	 * 
	 * @param paintable
	 *            A {@link RasterTile} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		RasterTile tile = (RasterTile) paintable;
		context.getRasterContext().drawImage(tile.getStore().getLayer(), 
				tile.getCode().toString(), tile.getUrl(), tile.getBounds(), tile.getStyle());
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link MapContext}. It the object does not exist, nothing
	 * will be done.
	 * 
	 * @param paintable
	 *            The object to be painted.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		RasterTile tile = (RasterTile) paintable;
		context.getRasterContext().deleteElement(tile.getStore().getLayer(), tile.getCode().toString());
	}
}
