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

package org.geomajas.internal.rendering.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.rendering.image.TileImageCreator;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.painter.TilePaintContext;
import org.geomajas.rendering.painter.image.FeatureImagePainter;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * This is the central image creation class. When an image has to be created for a tile, this is the class that will do
 * it.
 * </p>
 *
 * @author Pieter De Graef
 */
public class TileImageCreatorImpl implements TileImageCreator {

	private final Logger log = LoggerFactory.getLogger(TileImageCreatorImpl.class);

	/**
	 * The rendering hints to be used when the graphics object is instantiated.
	 */
	private RenderingHints renderingHints;

	/**
	 * The graphics object on which to draw. This will be passed to all painter.
	 */
	private Graphics2D graphics;

	/**
	 * The graphics object is part of this BufferedImage. It is this Image that will be returned with the paint method.
	 */
	private BufferedImage image;

	/**
	 * Should the created image support transparency or not?
	 */
	private boolean transparent;

	/**
	 * The full list of registered painter. Implementations paint a Feature in a specific way. For example: it's
	 * geometry or it's label.
	 */
	private List<FeatureImagePainter> painters;

	/**
	 * The tile object for whom we are creating a rendering.
	 */
	protected InternalTile tile;

	private FilterService filterService;

	private VectorLayerService layerService;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public TileImageCreatorImpl(InternalTile tile, boolean transparent, FilterService filterService,
			VectorLayerService layerService) {
		this.tile = tile;
		this.transparent = transparent;
		painters = new ArrayList<FeatureImagePainter>();
		this.layerService = layerService;
		this.filterService = filterService;
	}

	// -------------------------------------------------------------------------
	// Class specific functions
	// -------------------------------------------------------------------------

	/**
	 * Adds a painter to the list. Make sure you add them in the right order!
	 */
	public void registerPainter(FeatureImagePainter painter) {
		if (painter != null) {
			this.painters.add(painter);
		}
	}

	/**
	 * Paint an image, given a certain map context and bounding box. What show be painted, and how rendering should
	 * happen, is all defined in the map context object.
	 *
	 * @param paintArea paint area
	 * @param tileContext tile context
	 * @return {@link RenderedImage}
	 */
	public RenderedImage paint(Rectangle paintArea, TilePaintContext tileContext) {
		createImage(paintArea); // Create the RenderedImage.
		graphics = getGraphics(); // Create the Graphics2D with rendering hints.

		// ---------------------------------------------------------------------
		// Check to see if all the needed arguments can be found.
		// ---------------------------------------------------------------------
		Envelope mapArea = tileContext.getAreaOfInterest();

		if (graphics == null || paintArea == null) {
			log.error("renderer passed null arguments");
			throw new NullPointerException("renderer passed null arguments");
		} else if (mapArea == null && paintArea == null) {
			log.error("renderer passed null arguments");
			throw new NullPointerException("renderer passed null arguments");
		} else if (mapArea == null) {
			log.error("renderer passed null arguments");
			throw new NullPointerException("renderer passed null arguments");
		}

		// ---------------------------------------------------------------------
		// Processing all the layer paint contexts in the map context.
		// ---------------------------------------------------------------------
		final List<LayerPaintContext> layerContexts = tileContext.getLayerPaintContexts();
		LayerPaintContext layerContext;
		for (int i = 0; i < tileContext.getLayerCount(); i++) {
			layerContext = layerContexts.get(i);
			try {
				paintLayer(graphics, layerContext, tileContext);
			} catch (Throwable t) {
				log.error(t.getLocalizedMessage());
			}
		}

		return image;
	}

	/*
	 * Set extra rendering parameters. See the public static fields in this class for more info.
	 */
	/*
	public void setRenderingHints(List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			// Antialiasing:
			if (parameter.getDataSourceName().equalsIgnoreCase(ANTIALIAS)) {
				if (parameter.getValue().equalsIgnoreCase("true")) {
					renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				} else {
					renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);
				}
			} else if (renderingHints == null) {
				renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);
			}

			// Text-antialias:
			if (parameter.getDataSourceName().equalsIgnoreCase(TEXT_ANTIALIAS)) {
				if (parameter.getValue().equalsIgnoreCase("true")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
				}
			}

			// INTERPOLATION
			if (parameter.getDataSourceName().equalsIgnoreCase(INTERPOLATION)) {
				if (parameter.getValue().equalsIgnoreCase("nearest_neighbor")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR));
				} else if (parameter.getValue().equalsIgnoreCase("bilinear")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR));
				} else if (parameter.getValue().equalsIgnoreCase("bicubic")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BICUBIC));
				}
			}

			// INTERPOLATION_QUALITY
			if (parameter.getDataSourceName().equalsIgnoreCase(ALPHA_INTERPOLATION)) {
				if (parameter.getValue().equalsIgnoreCase("speed")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
							RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED));
				} else if (parameter.getValue().equalsIgnoreCase("default")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
							RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT));
				} else if (parameter.getValue().equalsIgnoreCase("quality")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
							RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
				}
			}

			// INTERPOLATION_QUALITY
			if (parameter.getDataSourceName().equalsIgnoreCase(RENDER_QUALITY)) {
				if (parameter.getValue().equalsIgnoreCase("speed")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_SPEED));
				} else if (parameter.getValue().equalsIgnoreCase("default")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_DEFAULT));
				} else if (parameter.getValue().equalsIgnoreCase("quality")) {
					renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_QUALITY));
				}
			}
		}

		// Apply on the graphics object if possible:
		if (graphics != null && renderingHints != null) {
			graphics.setRenderingHints(renderingHints);
		}
	}
	*/

	// -------------------------------------------------------------------------
	// Private functions
	// -------------------------------------------------------------------------
	private void createImage(Rectangle paintArea) {
		if (transparent) {
			image = new BufferedImage(paintArea.width, paintArea.height, BufferedImage.TYPE_4BYTE_ABGR);
		} else {
			// don't use alpha channel if the image is not transparent
			image = new BufferedImage(paintArea.width, paintArea.height, BufferedImage.TYPE_3BYTE_BGR);
		}
	}

	private Graphics2D getGraphics() {
		if (graphics == null) {
			graphics = image.createGraphics();
			Color bgColor = Color.WHITE;
			if (transparent) {
				int composite = AlphaComposite.DST;
				graphics.setComposite(AlphaComposite.getInstance(composite));
				Color c = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 0);
				graphics.setBackground(bgColor);
				graphics.setColor(c);
				graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
				composite = AlphaComposite.DST_OVER;
				graphics.setComposite(AlphaComposite.getInstance(composite));
			} else {
				graphics.setColor(bgColor);
				graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
			}
			if (renderingHints != null) {
				graphics.setRenderingHints(renderingHints);
			}
		}
		return graphics;
	}

	private void paintLayer(final Graphics2D graphics2D, LayerPaintContext layerContext,
			TilePaintContext tileContext) throws GeomajasException {

		// ---------------------------------------------------------------------
		// Step1: fetch all the LayerModel objects that need to be drawn:
		// ---------------------------------------------------------------------
		VectorLayer layer = layerContext.getLayer();
		VectorLayerInfo layerInfo = layer.getLayerInfo();
		String geomName = layer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
		Filter filter = filterService.createBboxFilter(tileContext.getCoordinateReferenceSystem().getIdentifiers()
				.iterator().next().toString(), tileContext.getAreaOfInterest(), geomName);
		if (layerContext.getFilter() != null) {
			filter = filterService.createLogicFilter(filter, "AND", layerContext.getFilter());
		}
		List<InternalFeature> features = layerService.getFeatures(layerInfo.getId(), null, filter,
				layerInfo.getStyleDefinitions(), VectorLayerService.FEATURE_INCLUDE_ALL);
		// ---------------------------------------------------------------------
		// Step2: Transform the LayerModel objects to features:
		// ---------------------------------------------------------------------
		for (InternalFeature feature : features) {
			tile.addFeature(feature);
		}

		// ---------------------------------------------------------------------
		// Step3: Loop over the painter, then over the features, and PAINT!
		// ---------------------------------------------------------------------
		for (FeatureImagePainter painter : painters) {
			painter.setTileContext(tileContext);
			for (InternalFeature feature : tile.getFeatures()) {
				painter.paint(graphics2D, feature);
			}
		}
	}
}