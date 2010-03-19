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
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.cache.tile.VectorTile.ContentHolder;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;

/**
 * Paints a vector tile.
 * 
 * @author Pieter De Graef
 */
public class VectorTilePainter implements Painter {

	public VectorTilePainter() {
	}

	public String getPaintableClassName() {
		return VectorTile.class.getName();
	}

	/**
	 * The actual painting function. Draws the groups.
	 * 
	 * @param object
	 *            A {@link org.geomajas.gwt.client.map.VectorTile} object.
	 * @param Object
	 *            The group where the object resides in (optional).
	 * @param graphics
	 *            A GraphicsContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, GraphicsContext graphics) {
		VectorTile tile = (VectorTile) paintable;

		// Paint the feature content:
		if (tile.getFeatureContent().isLoaded()) {
			drawContent(tile.getCache().getLayer().getFeatureGroup(), tile, tile.getFeatureContent(), graphics);
		}

		// Paint the label content:
		if (tile.getLabelContent().isLoaded()) {
			drawContent(tile.getCache().getLayer().getLabelGroup(), tile, tile.getLabelContent(), graphics);
		}
	}

	private void drawContent(PaintableGroup group, VectorTile tile, ContentHolder holder, GraphicsContext graphics) {
		switch (tile.getContentType()) {
			case STRING_CONTENT:
				graphics.drawData(group, holder, holder.getContent(), createTransformationMatrix(tile));
				break;
			case URL_CONTENT:
				graphics.drawGroup(group, holder, createTransformationMatrix(tile));
				graphics.drawImage(tile, "img", holder.getContent(), new Bbox(0, 0, tile.getScreenWidth(), tile
						.getScreenHeight()), new PictureStyle(1));
		}
	}

	/**
	 * Delete a <code>Paintable</code> object from the given <code>GraphicsContext</code>. It the object does not exist,
	 * nothing will be done.
	 * 
	 * @param paintable
	 *            The object to be painted.
	 * @param Object
	 *            The group where the object resides in (optional).
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, Object group, GraphicsContext graphics) {
		VectorTile tile = (VectorTile) paintable;
		graphics.deleteGroup(tile.getFeatureContent());
		graphics.deleteGroup(tile.getLabelContent());
	}

	private Matrix createTransformationMatrix(VectorTile tile) {
		// We assume the geometries are in screen space, beginning from a tile's upper-left corner.

		double dX = 0;
		double dY = 0;

		// clipped tiles have the pan origin as origin, so no need to translate:
		/*
		 * if (!tile.isClipped()) { // The map has already been translated by this, so we compensate again. Matrix trans
		 * = mapView.getPanToViewTranslation(); // To find the origin of the tile, we transform it's bounds to view
		 * space. Bbox viewBounds = transformer.worldToView(tile.getBounds()); dX = Math.round(viewBounds.getX() -
		 * trans.getDx()); dY = Math.round(viewBounds.getY() - trans.getDy()); }
		 */

		return new Matrix(1, 0, 0, 1, dX, dY);
	}
}
