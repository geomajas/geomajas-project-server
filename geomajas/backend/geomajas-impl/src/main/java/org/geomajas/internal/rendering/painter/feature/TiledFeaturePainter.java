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

package org.geomajas.internal.rendering.painter.feature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.layer.feature.VectorFeature;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.tile.RenderedTile;
import org.geomajas.rendering.tile.TileCode;
import org.geomajas.service.BboxService;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;

import java.util.List;

/**
 * <p>
 * This <code>FeaturePainter</code> paints features for a specific
 * <code>RenderedTile</code> object. When rendering features for a tile, not all
 * features are rendered as they normally would.
 * </p>
 * <p>
 * For example, when features are way too big, they are clipped. (note: since
 * the normal <code>RenderedFeature</code> object does not support clipped
 * features, an extension, called <code>VectorFeature</code> is used instead).
 * </p>
 * <p>
 * This painter will not just store it's features in a simple list, but directly
 * in a <code>RenderedTile</code> object. It also keeps track of dependency
 * between tiles. Tiles in Geomajas are dependent in the sense that each feature
 * lies in only 1 tile, even if it's geometry crosses the bounds of the tile. To
 * discern what tile a feature belongs to, the position of the first coordinate
 * is used. The other tiles that the geometry in question spans, are considered
 * dependent tiles.
 * </p>
 *
 * @author Pieter De Graef
 */
public class TiledFeaturePainter extends AbstractFeaturePainter {

	private BboxService bboxService;

	/**
	 * Helps determine when a feature is too big and must therefore be clipped.
	 */
	private static int MAXIMUM_TILE_COORDINATE = 10000;

	/**
	 * layer
	 */
	private VectorLayer layer;

	/**
	 * The tile's code. Needed to uniquely identify it.
	 */
	private TileCode code;

	/**
	 * Feature tile to render
	 */
	private RenderedTile tile;

	/**
	 * Reference to the feature model. Saves some time to have it here directly.
	 */
	private FeatureModel featureModel;

	/**
	 * The current client-side scale. Needed for clipping calculations.
	 */
	private double scale;

	/**
	 * The tile's maximum bounds in screen space. Needed for clipping
	 * calculations.
	 */
	private Bbox maxScreenBbox;

	/**
	 * When panning on the client, only this parameter changes. So we need to be
	 * aware of it as we calculate the maxScreenEnvelope.
	 */
	private Coordinate panOrigin;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * The only constructor. Needs quite a lot :-)
	 *
	 * @param tile tile to fill
	 * @param layer layer
	 * @param code tile code
	 * @param scale scale
	 * @param panOrigin pan origin
	 */
	public TiledFeaturePainter(RenderedTile tile, VectorLayer layer, TileCode code, double scale,
			Coordinate panOrigin, BboxService bboxService) {
		super();
		this.layer = layer;
		this.code = code;
		this.tile = tile;
		this.featureModel = layer.getLayerModel().getFeatureModel();
		this.scale = scale;
		this.panOrigin = panOrigin;
		this.bboxService = bboxService;
	}

	// -------------------------------------------------------------------------
	// FeaturePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint an individual feature. In other words transform the generic feature
	 * object into a <code>VectorFeature</code>, and prepare it for a certain
	 * tile.
	 *
	 * @param paintContext
	 *            The provided painting context. It helps to determine what
	 *            style a feature should receive.
	 * @param feature
	 *            A feature object that comes directly from the
	 *            <code>LayerModel</code>.
	 * @throws RenderException oops
	 */
	public void paint(LayerPaintContext paintContext, Object feature) throws RenderException {
		try {
			VectorFeature f = new VectorFeature(bboxService);
			f.setLayer(paintContext.getLayer());

			Geometry geometry = featureModel.getGeometry(feature);
			Geometry transformed;
			if (paintContext.getMathTransform() != null) {
				try {
					transformed = JTS.transform(geometry, paintContext.getMathTransform());
				} catch (MismatchedDimensionException e) {
					throw new RenderException(ExceptionCode.RENDER_TRANSFORMATION_FAILED, e);
				} catch (TransformException e) {
					throw new RenderException(ExceptionCode.RENDER_TRANSFORMATION_FAILED, e);
				}
			} else {
				transformed = geometry;
			}

			if (!bboxService.contains(tile.getBbox(layer), transformed.getCoordinate())) {
				addTileCode(transformed);
			} else {
				f.setId(paintContext.getLayer().getLayerInfo().getId() + "." + featureModel.getId(feature));

				// If allowed, add the geometry (transformed!) to the
				// RenderedFeature:
				if (getOption(OPTION_PAINT_GEOMETRY)) {
					f.setGeometry(transformed);
					if (exceedsScreenDimensions(f)) {
						tile.setClipped(true);
						f.setClipped(true);
						Geometry clipped = JTS.toGeometry(bboxService.toEnvelope(getMaxScreenBbox())).
								intersection(f.getGeometry());
						f.setClippedGeometry(clipped);
					}
				}

				// If allowed, add the style definition to the RenderedFeature:
				if (getOption(OPTION_PAINT_STYLE)) {
					f.setStyleDefinition(paintContext.findStyleFilter(feature).getStyleDefinition());
				}

				// If allowed, add the attributes to the RenderedFeature:
				if (getOption(OPTION_PAINT_ATTRIBUTES)) {
					f.setAttributes(featureModel.getAttributes(feature));
				}

				// If allowed, add the label to the RenderedFeature:
				if (getOption(OPTION_PAINT_LABEL)) {
					String labelAttr = paintContext.getLayer().getLayerInfo().getLabelAttribute()
							.getLabelAttributeName();
					Object attribute = featureModel.getAttribute(feature, labelAttr);
					if (attribute != null) {
						f.setLabel(attribute.toString());
					}
				}

				tile.addFeature(f);
			}
		} catch (LayerException fme) {
			throw new RenderException(ExceptionCode.RENDER_FEATURE_MODEL_PROBLEM, fme);
		}
	}

	/**
	 * The full list of <code>RenderedFeature</code> objects.
	 *
	 * @return list of features
	 */
	public List<RenderedFeature> getFeatures() {
		return tile.getFeatures();
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Add dependent tiles for this geometry.
	 *
	 * @param geom geometry
	 */
	private void addTileCode(Geometry geom) {
		Coordinate c = geom.getCoordinate();
		if (c != null && bboxService.contains(layer.getLayerInfo().getMaxExtent(), c)) {
			int i = (int) ((c.x - layer.getLayerInfo().getMaxExtent().getX()) / tile.getTileWidth());
			int j = (int) ((c.y - layer.getLayerInfo().getMaxExtent().getY()) / tile.getTileHeight());
			int level = tile.getCode().getTileLevel();
			tile.addCode(level, i, j);
		}
	}

	/**
	 * The test that checks if clipping is needed.
	 *
	 * @param f feature to test
	 * @return true if clipping is needed
	 */
	private boolean exceedsScreenDimensions(RenderedFeature f) {
		Bbox env = f.getBounds();
		if (env.getWidth() * scale > MAXIMUM_TILE_COORDINATE) {
			return true;
		} else {
			return env.getHeight() * scale > MAXIMUM_TILE_COORDINATE;
		}
	}

	/**
	 * What is the maximum bounds in screen space? Needed for correct clipping calculation.
	 *
	 * @return max screen bbox
	 */
	private Bbox getMaxScreenBbox() {
		if (maxScreenBbox == null) {
			Bbox max = layer.getLayerInfo().getMaxExtent();
			double div = Math.pow(2, code.getTileLevel());
			int tileWidthPx = (int) Math.ceil(scale * max.getWidth() / div);
			double tileWidth = tileWidthPx / scale;
			int tileHeightPx = (int) Math.ceil(scale * max.getWidth() / div);
			double tileHeight = tileHeightPx / scale;
			int nrOfTilesX = Math.max(1, MAXIMUM_TILE_COORDINATE / tileWidthPx);
			int nrOfTilesY = Math.max(1, MAXIMUM_TILE_COORDINATE / tileHeightPx);
			maxScreenBbox = new Bbox(panOrigin.x - nrOfTilesX * tileWidth, panOrigin.y - nrOfTilesY * tileHeight,
					nrOfTilesX * tileWidth * 2, nrOfTilesY * tileHeight * 2);
		}
		return maxScreenBbox;
	}
}