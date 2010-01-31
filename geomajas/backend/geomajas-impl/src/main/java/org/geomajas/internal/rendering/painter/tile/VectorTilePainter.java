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

package org.geomajas.internal.rendering.painter.tile;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.application.tile.VectorTileJG;
import org.geomajas.internal.layer.feature.ClippedInternalFeature;
import org.geomajas.internal.rendering.DefaultSvgDocument;
import org.geomajas.internal.rendering.DefaultVmlDocument;
import org.geomajas.internal.rendering.writers.svg.SvgFeatureScreenWriter;
import org.geomajas.internal.rendering.writers.svg.SvgFeatureTileWriter;
import org.geomajas.internal.rendering.writers.svg.SvgLabelTileWriter;
import org.geomajas.internal.rendering.writers.vml.VmlFeatureScreenWriter;
import org.geomajas.internal.rendering.writers.vml.VmlLabelTileWriter;
import org.geomajas.internal.rendering.writers.vml.VmlVectorTileWriter;
import org.geomajas.layer.VectorLayer;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.rendering.tile.InternalTile;
import org.geomajas.rendering.tile.TileMetadata;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.AffineTransform;
import java.io.StringWriter;

/**
 * <p>
 * TilePainter implementation for {@link VectorTileJG} objects.
 * </p>
 *
 * @author Pieter De Graef
 */
public class VectorTilePainter implements TilePainter {

	private final Logger log = LoggerFactory.getLogger(VectorTilePainter.class);

	/**
	 * The maximum number of allowed fraction digits.
	 */
	protected static final int MAXIMUM_FRACTION_DIGITS = 0;

	/**
	 * Should this painter paint a feature's geometries or not?
	 */
	private boolean paintGeometries = true;

	/**
	 * Should this painter paint a feature's labels or not?
	 */
	private boolean paintLabels;

	/**
	 * The layer wherein the tiles lie.
	 */
	private VectorLayer layer;

	/**
	 * Rendering output type. This can be either "SVG" or "VML". This depends on
	 * what the client requests.
	 */
	private String renderer;

	/**
	 * The tile to render.
	 */
	private VectorTileJG tile;

	/**
	 * The current client-side scale.
	 */
	private double scale;

	/**
	 * The tile's bounding box.
	 */
	private Bbox bbox;

	/**
	 * Transformer that transforms the feature's geometries from world to view
	 * space.
	 */
	private GeometryCoordinateSequenceTransformer unitToPixel;

	/**
	 * A DOM document for parsing the tile's features.
	 */
	private GraphicsDocument featureDocument;

	/**
	 * A DOM document for parsing the tile's labels.
	 */
	private GraphicsDocument labelDocument;

	/**
	 * The current origin may differ, depending on whether or not the client has
	 * been panning.
	 */
	private Coordinate panOrigin;

	private GeoService geoService;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize this painter with all the info it needs.
	 *
	 * @param layer
	 *            The vector layer wherein the tiles lie.
	 * @param renderer
	 *            Rendering output type: "SVG" or "VML".
	 * @param scale
	 *            The current client-side scale. Needed for creating the world
	 *            to view space coordinate transformer.
	 * @param panOrigin
	 *            The current origin may differ, depending on whether or not the
	 *            client has been panning.Needed for creating the world to view
	 *            space coordinate transformer.
	 */
	public VectorTilePainter(VectorLayer layer, String renderer, double scale, Coordinate panOrigin,
			GeoService geoService) {
		this.layer = layer;
		this.renderer = renderer;
		this.scale = scale;
		this.panOrigin = panOrigin;
		this.geoService = geoService;
	}

	// -------------------------------------------------------------------------
	// TilePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint the tile! The tile must be an instance of {@link VectorTileJG}. This
	 * function will create 2 DOM documents for geometries and labels. In case
	 * the renderer says "SVG" these documents will be of the type
	 * {@link org.geomajas.internal.rendering.DefaultSvgDocument}, otherwise
	 * {@link org.geomajas.internal.rendering.DefaultVmlDocument}. These
	 * documents in turn are built using {@link org.geomajas.internal.rendering.writers.GraphicsWriter} classes.
	 *
	 * @param tileToPaint
	 *            The instance of {@link VectorTileJG}. Using the DOM documents,
	 *            the tile's "featureFragment" and "labelFragment" will be
	 *            created.
	 * @return Returns a fully rendered vector tile.
	 */
	public InternalTile paint(InternalTile tileToPaint) throws RenderException {
		if (tileToPaint instanceof VectorTileJG) {
			// VectorTile vTile = (VectorTile) tile;
			this.tile = (VectorTileJG) tileToPaint;
			if (paintGeometries && featureDocument == null) {
				// create the svg feature fragment
				StringWriter writer = new StringWriter();
				try {
					featureDocument = createFeatureDocument(writer);
					featureDocument.setRootId(layer.getLayerInfo().getId());
					featureDocument.writeObject(this.tile, false);
					featureDocument.flush();
				} catch (RenderException e) {
					log.error("Unable to write this tile's feature fragment", e);
				}
				this.tile.setFeatureFragment(writer.toString());
			}

			if (paintLabels && labelDocument == null) {
				// create the svg label fragment
				StringWriter writer = new StringWriter();
				try {
					labelDocument = createLabelDocument(writer);
					labelDocument.setRootId(layer.getLayerInfo().getId());
					labelDocument.writeObject(tileToPaint, false);
					labelDocument.flush();
				} catch (RenderException e) {
					log.error("Unable to write this tile's label fragment", e);
				}
				this.tile.setLabelFragment(writer.toString());
			}
			return this.tile;
		}

		return tileToPaint;
	}

	/**
	 * Enables or disabled the use of painter that paint the geometries of the
	 * features in the tile.
	 *
	 * @param paintGeometries
	 *            true or false.
	 */
	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	/**
	 * Enables or disabled the use of painter that paint the labels of the
	 * features in the tile.
	 *
	 * @param paintLabels
	 *            true or false.
	 */
	public void setPaintLabels(boolean paintLabels) {
		this.paintLabels = paintLabels;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Create a document that parses the tile's featureFragment, using
	 * GraphicsWriter classes.
	 *
	 * @param writer writer
	 * @return document
	 * @throws RenderException oops
	 */
	private GraphicsDocument createFeatureDocument(StringWriter writer) throws RenderException {
		if (TileMetadata.PARAM_SVG_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultSvgDocument document = new DefaultSvgDocument(writer, false);
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			document.registerWriter(ClippedInternalFeature.class, new SvgFeatureScreenWriter(getTransformer()));
			document.registerWriter(VectorTileJG.class, new SvgFeatureTileWriter());
			return document;
		} else if (TileMetadata.PARAM_VML_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultVmlDocument document = new DefaultVmlDocument(writer);
			int coordWidth = tile.getScreenWidth();
			int coordHeight = tile.getScreenHeight();
			document.registerWriter(ClippedInternalFeature.class, new VmlFeatureScreenWriter(getTransformer(),
					coordWidth, coordHeight));
			document.registerWriter(VectorTileJG.class, new VmlVectorTileWriter(coordWidth, coordHeight));
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			return document;
		} else {
			throw new RenderException(ExceptionCode.RENDERER_TYPE_NOT_SUPPORTED, renderer);
		}
	}

	/**
	 * Create a document that parses the tile's labelFragment, using
	 * GraphicsWriter classes.
	 */
	private GraphicsDocument createLabelDocument(StringWriter writer) throws RenderException {
		if (TileMetadata.PARAM_SVG_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultSvgDocument document = new DefaultSvgDocument(writer, false);
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			document.registerWriter(VectorTileJG.class, new SvgLabelTileWriter(getTransformer(), layer
					.getLayerInfo().getLabelAttribute().getBackgroundStyle(), geoService));
			return document;
		} else if (TileMetadata.PARAM_VML_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultVmlDocument document = new DefaultVmlDocument(writer);
			int coordWidth = (int) Math.round(scale * getTileBbox().getWidth());
			int coordHeight = (int) Math.round(scale * getTileBbox().getHeight());
			document.registerWriter(ClippedInternalFeature.class, new VmlFeatureScreenWriter(getTransformer(),
					coordWidth, coordHeight));
			document.registerWriter(VectorTileJG.class, new VmlLabelTileWriter(coordWidth, coordHeight,
					getTransformer(), layer.getLayerInfo().getLabelAttribute().getBackgroundStyle(), geoService));
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			return document;
		} else {
			throw new RenderException(ExceptionCode.RENDERER_TYPE_NOT_SUPPORTED, renderer);
		}
	}

	/**
	 * Can't we get this out of this class???
	 *
	 * @return
	 */
	private GeometryCoordinateSequenceTransformer getTransformer() {
		if (unitToPixel == null) {
			unitToPixel = new GeometryCoordinateSequenceTransformer();
			if (tile.isClipped()) {
				// find coords wrt to pan origin (0,0) and scale (pix/unit)
				unitToPixel.setMathTransform(ProjectiveTransform.create(new AffineTransform(scale, 0, 0,
						-scale, -scale * panOrigin.x, scale * panOrigin.y)));
			} else {
				// find coords wrt to upper left corner (0,0) and scale
				// (pix/unit)
				unitToPixel.setMathTransform(ProjectiveTransform.create(new AffineTransform(scale, 0, 0,
						-scale, -scale * getTileBbox().getX(), scale
								* (getTileBbox().getY() + getTileBbox().getHeight()))));
			}
		}
		return unitToPixel;
	}

	/**
	 * Convenience method for fetching the tile's bbox.
	 *
	 * @return bbox
	 */
	private Bbox getTileBbox() {
		if (bbox == null) {
			bbox = tile.getBbox(layer);
		}
		return bbox;
	}
}