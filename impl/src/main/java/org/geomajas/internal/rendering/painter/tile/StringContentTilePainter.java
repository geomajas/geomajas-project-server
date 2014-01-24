/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering.painter.tile;

import java.awt.geom.AffineTransform;
import java.io.StringWriter;
import java.util.Collections;

import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.internal.rendering.DefaultSvgDocument;
import org.geomajas.internal.rendering.DefaultVmlDocument;
import org.geomajas.internal.rendering.writer.svg.SvgFeatureWriter;
import org.geomajas.internal.rendering.writer.svg.SvgLabelTileWriter;
import org.geomajas.internal.rendering.writer.svg.SvgTileWriter;
import org.geomajas.internal.rendering.writer.vml.VmlFeatureWriter;
import org.geomajas.internal.rendering.writer.vml.VmlLabelTileWriter;
import org.geomajas.internal.rendering.writer.vml.VmlTileWriter;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.service.GeoService;
import org.geomajas.service.TextService;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * <p>
 * TilePainter implementation for {@link InternalTile} objects.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class StringContentTilePainter implements TilePainter {

	private final Logger log = LoggerFactory.getLogger(StringContentTilePainter.class);

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
	private final VectorLayer layer;

	/**
	 * The layer style.
	 */
	private final NamedStyleInfo style;

	/**
	 * Rendering output type. This can be either "SVG" or "VML". This depends on what the client requests.
	 */
	private final String renderer;

	/**
	 * The tile to render.
	 */
	private InternalTile tile;

	/**
	 * The current client-side scale.
	 */
	private final double scale;

	/**
	 * Transformer that transforms the feature's geometries from world to view space.
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
	 * The current origin may differ, depending on whether or not the client has been panning.
	 */
	private final Coordinate panOrigin;

	private final GeoService geoService;

	private final TextService textService;
	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize this painter with all the info it needs.
	 * 
	 * @param layer
	 *            The vector layer wherein the tiles lie.
	 * @param style
	 *            The rendering style.
	 * @param renderer
	 *            Rendering output type: "SVG" or "VML".
	 * @param scale
	 *            The current client-side scale. Needed for creating the world to view space coordinate transformer.
	 * @param panOrigin
	 *            The current origin may differ, depending on whether or not the client has been panning.Needed for
	 *            creating the world to view space coordinate transformer.
	 * @param geoService
	 *            geo service for geometry conversions
	 * @param textService
	 *            text service for string sizes
	 */
	public StringContentTilePainter(VectorLayer layer, NamedStyleInfo style, String renderer, double scale,
			Coordinate panOrigin, GeoService geoService, TextService textService) {
		this.layer = layer;
		// @todo: duplicate code, can we just depend on the VectorLayerService ?
		if (style == null) {
			// no style specified, take the first
			style = layer.getLayerInfo().getNamedStyleInfos().get(0);
		} else if (style.getFeatureStyles().isEmpty()) {
			// only name specified, find it
			style = layer.getLayerInfo().getNamedStyleInfo(style.getName());
		}

		this.style = style;
		this.renderer = renderer;
		this.scale = scale;
		this.panOrigin = panOrigin;
		this.geoService = geoService;
		this.textService = textService;
	}

	// -------------------------------------------------------------------------
	// TilePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint the tile! The tile must be an instance of {@link InternalTile}. This function will create 2 DOM documents
	 * for geometries and labels. In case the renderer says "SVG" these documents will be of the type
	 * {@link org.geomajas.internal.rendering.DefaultSvgDocument}, otherwise
	 * {@link org.geomajas.internal.rendering.DefaultVmlDocument}. These documents in turn are built using
	 * {@link org.geomajas.internal.rendering.writer.GraphicsWriter} classes.
	 * 
	 * @param tileToPaint
	 *            The instance of {@link InternalTile}. Using the DOM documents, the tile's "featureFragment" and
	 *            "labelFragment" will be created.
	 * @return Returns a fully rendered vector tile.
	 */
	public InternalTile paint(InternalTile tileToPaint) throws RenderException {
		if (tileToPaint != null && null != tileToPaint.getFeatures()) {
			tile = tileToPaint;
			Collections.sort(tile.getFeatures()); // make sure features are sorted by style, needed for grouping

			// Create the SVG / VML feature fragment:
			if (paintGeometries && featureDocument == null) {
				StringWriter writer = new StringWriter();
				try {
					featureDocument = createFeatureDocument(writer);
					featureDocument.setRootId(layer.getId());
					featureDocument.writeObject(tile, false);
					featureDocument.flush();
				} catch (RenderException e) {
					log.error("Unable to write this tile's feature fragment", e);
				}
				tile.setFeatureContent(writer.toString());
			}

			// Create the SVG / VML label fragment:
			if (paintLabels && labelDocument == null) {
				StringWriter writer = new StringWriter();
				try {
					labelDocument = createLabelDocument(writer, style.getLabelStyle());
					labelDocument.setRootId(layer.getId());
					labelDocument.writeObject(tileToPaint, false);
					labelDocument.flush();
				} catch (RenderException e) {
					log.error("Unable to write this tile's label fragment", e);
				}
				tile.setLabelContent(writer.toString());
			}
			return tile;
		}

		return tileToPaint;
	}

	/**
	 * Enables or disabled the use of painter that paint the geometries of the features in the tile.
	 * 
	 * @param paintGeometries
	 *            true or false.
	 */
	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	/**
	 * Enables or disabled the use of painter that paint the labels of the features in the tile.
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
	 * Create a document that parses the tile's featureFragment, using GraphicsWriter classes.
	 * 
	 * @param writer
	 *            writer
	 * @return document
	 * @throws RenderException
	 *             oops
	 */
	private GraphicsDocument createFeatureDocument(StringWriter writer) throws RenderException {
		if (TileMetadata.PARAM_SVG_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultSvgDocument document = new DefaultSvgDocument(writer, false);
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			document.registerWriter(InternalFeatureImpl.class, new SvgFeatureWriter(getTransformer()));
			document.registerWriter(InternalTileImpl.class, new SvgTileWriter());
			return document;
		} else if (TileMetadata.PARAM_VML_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultVmlDocument document = new DefaultVmlDocument(writer);
			int coordWidth = tile.getScreenWidth();
			int coordHeight = tile.getScreenHeight();
			document.registerWriter(InternalFeatureImpl.class, new VmlFeatureWriter(getTransformer(), coordWidth,
					coordHeight));
			document.registerWriter(InternalTileImpl.class, new VmlTileWriter(coordWidth, coordHeight));
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			return document;
		} else {
			throw new RenderException(ExceptionCode.RENDERER_TYPE_NOT_SUPPORTED, renderer);
		}
	}

	/**
	 * Create a document that parses the tile's labelFragment, using GraphicsWriter classes.
	 * 
	 * @param writer
	 *            writer
	 * @param labelStyleInfo
	 *            label style info
	 * @return graphics document
	 * @throws RenderException
	 *             cannot render
	 */
	private GraphicsDocument createLabelDocument(StringWriter writer, LabelStyleInfo labelStyleInfo)
			throws RenderException {

		if (TileMetadata.PARAM_SVG_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultSvgDocument document = new DefaultSvgDocument(writer, false);
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			document.registerWriter(InternalTileImpl.class, new SvgLabelTileWriter(getTransformer(), labelStyleInfo,
					geoService, textService));
			return document;
		} else if (TileMetadata.PARAM_VML_RENDERER.equalsIgnoreCase(renderer)) {
			DefaultVmlDocument document = new DefaultVmlDocument(writer);
			int coordWidth = tile.getScreenWidth();
			int coordHeight = tile.getScreenHeight();
			document.registerWriter(InternalFeatureImpl.class, new VmlFeatureWriter(getTransformer(), coordWidth,
					coordHeight));
			document.registerWriter(InternalTileImpl.class, new VmlLabelTileWriter(coordWidth, coordHeight,
					getTransformer(), labelStyleInfo, geoService, textService));
			document.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
			return document;
		} else {
			throw new RenderException(ExceptionCode.RENDERER_TYPE_NOT_SUPPORTED, renderer);
		}
	}

	/**
	 * Get transformer to use.
	 * 
	 * @return transformation to apply
	 */
	private GeometryCoordinateSequenceTransformer getTransformer() {
		if (unitToPixel == null) {
			unitToPixel = new GeometryCoordinateSequenceTransformer();
			unitToPixel.setMathTransform(ProjectiveTransform.create(new AffineTransform(scale, 0, 0, -scale, -scale
					* panOrigin.x, scale * panOrigin.y)));
		}
		return unitToPixel;
	}

}