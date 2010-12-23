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

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.map.MapView;
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

	private static final PictureStyle OPAQUE_PICTURE_STYLE = new PictureStyle(1);

	private static final Matrix NO_TRANSFORMATION = new Matrix(1, 0, 0, 1, 0, 0);

	private MapView mapView;

	public VectorTilePainter(MapView mapView) {
		this.mapView = mapView;
	}

	public String getPaintableClassName() {
		return VectorTile.class.getName();
	}

	/**
	 * The actual painting function. Draws the groups.
	 * 
	 * @param paintable
	 *            A {@link VectorTile} object.
	 * @param group
	 *            The group where the object resides in (optional).
	 * @param context
	 *            A MapContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		VectorTile tile = (VectorTile) paintable;

		// Paint the feature content:
		if (tile.getFeatureContent().isLoaded()) {
			drawContent(tile.getCache().getLayer().getFeatureGroup(), tile, tile.getFeatureContent(), context);
		}

		// Paint the label content:
		if (tile.getLabelContent().isLoaded()) {
			drawContent(tile.getCache().getLayer().getLabelGroup(), tile, tile.getLabelContent(), context);
		}
	}

	private void drawContent(PaintableGroup group, VectorTile tile, ContentHolder holder, MapContext context) {
		switch (tile.getContentType()) {
			case STRING_CONTENT:
				context.getVectorContext().drawData(group, holder, holder.getContent(), NO_TRANSFORMATION);
				break;
			case URL_CONTENT:
				context.getRasterContext().drawImage(tile.getCache().getLayer(), tile.getCode().toString(),
						holder.getContent(), getPanBounds(tile), OPAQUE_PICTURE_STYLE);
		}
	}

	/**
	 * Delete a {@link Paintable} object from the given {@link GraphicsContext}. It the object does not exist, nothing
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
		VectorTile tile = (VectorTile) paintable;
		context.getVectorContext().deleteGroup(tile.getFeatureContent());
		context.getVectorContext().deleteGroup(tile.getLabelContent());
		context.getRasterContext().deleteElement(tile.getCache().getLayer(), tile.getCode().toString());
	}

	private Bbox getPanBounds(VectorTile tile) {
		// Bounds should have integer coordinate values after transforming
		// We shift the bbox to the next integer value point
		Coordinate panOrigin = mapView.getPanOrigin();
		double scale = mapView.getCurrentScale();
		// calculate the normal shift of the origin tile's corner and extract the fractional part
		double dx = Math.round((tile.getCache().getLayerBounds().getX() - panOrigin.getX()) * scale) / scale;
		double dy = Math.round((tile.getCache().getLayerBounds().getY() - panOrigin.getY()) * scale) / scale;
		// this gives the difference between integer positions and floating positions
		dx = dx  - (tile.getCache().getLayerBounds().getX() - panOrigin.getX());
		dy = dy  - (tile.getCache().getLayerBounds().getY() - panOrigin.getY());
		// now apply to our bounds
		Bbox  worldBounds = new Bbox(tile.getBounds());
		worldBounds.translate(-dx, -dy);
		// transform to pan space and round
		Bbox panBounds = mapView.getWorldViewTransformer().worldToPan(worldBounds);
		return new Bbox(Math.round(panBounds.getX()), Math.round(panBounds.getY()), Math.round(panBounds.getWidth()),
				Math.round(panBounds.getHeight()));
	}
}
