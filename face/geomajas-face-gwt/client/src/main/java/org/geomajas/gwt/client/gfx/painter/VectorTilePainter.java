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
		boolean labeled = tile.getCache().getLayer().isLabelsShowing();
		switch (tile.getContentType()) {
			case STRING_CONTENT:
				// Paint the feature content:
				if (tile.getFeatureContent().isLoaded()) {
					drawData(tile.getCache().getLayer().getFeatureGroup(), tile.getFeatureContent(), context);
				}
				// Paint the label content:
				if (tile.getLabelContent().isLoaded()) {
					drawData(tile.getCache().getLayer().getLabelGroup(), tile.getLabelContent(), context);
				}
				break;
			case URL_CONTENT:
				// paint the features url				
				if (tile.getFeatureContent().isLoaded()) {
					drawImage(tile.getCache().getLayer().getFeatureGroup(), tile, tile.getFeatureContent(), context);
				}
				// paint the label url
				if (labeled && tile.getLabelContent().isLoaded()) {
					drawImage(tile.getCache().getLayer().getLabelGroup(), tile, tile.getLabelContent(), context);
					// or Paint the feature url
				}
				break;
		}

	}
	
	private void drawData(PaintableGroup group, ContentHolder holder, MapContext context) {
		context.getVectorContext().drawData(group, holder, holder.getContent(), NO_TRANSFORMATION);
	}

	private void drawImage(PaintableGroup group, VectorTile tile, ContentHolder holder, MapContext context) {
		context.getRasterContext().drawGroup(group, holder);
		context.getRasterContext().drawImage(holder, tile.getCode().toString(),
				holder.getContent(), getPanBounds(tile), OPAQUE_PICTURE_STYLE);
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
		VectorTile tile = (VectorTile) paintable;
		context.getVectorContext().deleteGroup(tile.getFeatureContent());
		context.getVectorContext().deleteGroup(tile.getLabelContent());
		context.getRasterContext().deleteGroup(tile.getFeatureContent());
		context.getRasterContext().deleteGroup(tile.getLabelContent());
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
