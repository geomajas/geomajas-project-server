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

package org.geomajas.plugin.rasterizing;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.RectInfo;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.Style2D;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Service which rasterizes a vector tile based on the Geotools renderer.
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Component
@Api
public class GeotoolsRasterizingService implements RasterizingService {

	private final Logger log = LoggerFactory.getLogger(GeotoolsRasterizingService.class);

	private static final Decimator NULL_DECIMATOR = new Decimator(-1, -1);

	private boolean transparent = true;
	private RenderingHints renderingHints;

	private String fontName = "Arial";
	private int fontSize = 12;
	private int labelBorderDistanceX = 3;
	private int labelBorderDistanceY = 2;

	@Autowired
	private GeoService geoService;

	/**
	 * Should the image be created with a transparency.
	 *
	 * @param transparent should image support transparency?
	 */
	@Api
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * Set the rendering hints which should be applied.
	 *
	 * @param renderingHints rendering hints
	 */
	@Api
	public void setRenderingHints(RenderingHints renderingHints) {
		this.renderingHints = renderingHints;
	}

	/**
	 * Configure font name for labels. Default is "Arial".
	 *
	 * @param fontName font name
	 */
	@Api
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	/**
	 * Configure default font size in pixels for labels, default is 12.
	 *
	 * @param fontSize font size for labels
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * Set the horizontal blank space between the text and the label's borders.
	 *
	 * @param labelBorderDistanceX label border width
	 */
	@Api
	public void setLabelBorderDistanceX(int labelBorderDistanceX) {
		this.labelBorderDistanceX = labelBorderDistanceX;
	}

	/**
	 * Set the vertical blank space between the text and the label's borders.
	 *
	 * @param labelBorderDistanceY label border width
	 */
	@Api
	public void setLabelBorderDistanceY(int labelBorderDistanceY) {
		this.labelBorderDistanceY = labelBorderDistanceY;
	}

	/**
	 * Rasterize the given tile (which contains the features).
	 * @param stream output stream where the image should be put
	 * @param layer layer which is rasterized
	 * @param style style to apply
	 * @param tile tile containing the features
	 * @throws GeomajasException oops
	 */
	@Api
	public void rasterize(OutputStream stream, VectorLayer layer, NamedStyleInfo style, TileMetadata metadata,
			InternalTile tile) throws GeomajasException, IOException {
		BufferedImage image = createImage(tile.getScreenWidth(), tile.getScreenHeight());
		Graphics2D graphics = getGraphics(image);
		paintLayer(image, graphics, layer, style, metadata, tile);
		ImageIO.write(image, "PNG", stream);
	}

	private void paintLayer(BufferedImage image, final Graphics2D graphics2D, VectorLayer layer, NamedStyleInfo style,
			TileMetadata metadata, InternalTile tile) throws GeomajasException {
		MathTransform transform = getMathTransform(metadata, tile);
		if (metadata.isPaintGeometries()) {
			StyledShapePainter painter = new StyledShapePainter(new LabelCacheImpl());
			for (InternalFeature feature : tile.getFeatures()) {
				paintGeometry(metadata, transform, graphics2D, painter, feature);
			}
		}
		if (metadata.isPaintLabels()) {
			LabelStyle labelStyle = new LabelStyle(style.getLabelStyle());
			try {
				labelStyle.setStrokeWidth(style.getLabelStyle().getBackgroundStyle().getStrokeWidth());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				labelStyle.setStrokeWidth(0);
			}

			for (InternalFeature feature : tile.getFeatures()) {
				paintLabel(transform, graphics2D, labelStyle, feature);
			}
		}
	}

	private BufferedImage createImage(int width, int height) {
		if (transparent) {
			return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		} else {
			// don't use alpha channel if the image is not transparent
			return new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		}
	}

	private Graphics2D getGraphics(BufferedImage image) {
			Graphics2D graphics = image.createGraphics();
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
		return graphics;
	}

	/**
	 * Paint the feature object on the graphics. It is the feature's label that this painter will draw.
	 *
	 * @param graphics
	 *            The AWT graphics object onto which we draw.
	 * @param feature
	 *            The feature object of which we draw the label.
	 */
	public void paintLabel(MathTransform transform, Graphics2D graphics, LabelStyle style, InternalFeature feature) {
		float strokeWidth = style.getStrokeWidth();

		GeometryCoordinateSequenceTransformer transformer = new GeometryCoordinateSequenceTransformer();
		transformer.setMathTransform(transform);

		// Get the label's position and transform it to screen space:
		Coordinate c = geoService.calcDefaultLabelPosition(feature);
		GeometryFactory f = feature.getGeometry().getFactory();
		Point p = f.createPoint(new com.vividsolutions.jts.geom.Coordinate(c.x, c.y));
		Point tp;

		try {
			tp = transformer.transformPoint(p, f);
			Font font = new Font(fontName, Font.PLAIN, fontSize);

			FontRenderContext frc = graphics.getFontRenderContext();
			GlyphVector gv = font.createGlyphVector(frc, feature.getLabel());
			TextLayout layout = new TextLayout(feature.getLabel(), font, frc);
			Rectangle2D bounds = layout.getBounds();
			double fontHeight = bounds.getHeight();
			double fontWidth = bounds.getWidth();
			double halfFontX = (float) (fontWidth / 2);
			double halfFontY = (float) (fontHeight / 2);

			// Calculating background. Given the feature's label coordinate,
			// make sure that the label is centered around it (both X and Y
			// axis). Also make sure you respect the labelBorderDistanceX and
			// labelBorderDistanceY. Then add some extra space for thick
			// borders.

			double bgX = bounds.getX() + tp.getX() - labelBorderDistanceX - strokeWidth / 2 - halfFontX;
			double bgY = bounds.getX() + tp.getY() - labelBorderDistanceY - strokeWidth / 2 - halfFontY;
			double bgWidth = bounds.getWidth() + labelBorderDistanceX * 2 + strokeWidth;
			double bgHeight = bounds.getHeight() + labelBorderDistanceY * 2 + strokeWidth;
			bounds.setRect(bgX, bgY, bgWidth, bgHeight);
			graphics.setPaint(style.getBackgroundColor());
			graphics.setComposite(style.getBackgroundComposite());
			graphics.fill(bounds);

			graphics.setPaint(style.getStrokeColor());
			graphics.setComposite(style.getStrokeComposite());
			graphics.setStroke(style.getStroke());
			graphics.draw(bounds);

			graphics.setPaint(style.getFontColor());
			graphics.setComposite(style.getFontComposite());
			graphics.drawGlyphVector(gv, (float) tp.getX() - (float) halfFontX, (float) tp.getY() + (float) halfFontY);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Find coordinates with respect to to upper left corner (0,0) and scale (pix/unit).
	 *
	 * @return
	 */
	private MathTransform getMathTransform(TileMetadata metadata, InternalTile tile) {
		double scale = metadata.getScale();
		Envelope env = tile.getBounds();
		return ProjectiveTransform.create(new AffineTransform(scale, 0, 0, -scale, -scale * env.getMinX(), scale
				* (env.getMinY() + env.getHeight())));
	}

	/**
	 * Paint the feature object on the graphics. It is the feature's geometry that this painter draws.
	 *
	 * @param graphics
	 *            The AWT graphics object onto which we draw.
	 * @param feature
	 *            The feature object of which we draw the geometry.
	 */
	public void paintGeometry(TileMetadata metadata, MathTransform transform, Graphics2D graphics,
			StyledShapePainter painter, InternalFeature feature) {
		// Get the feature's geometry and turn it into a shape. This shape is
		// transformed from world to screen, and can be rendered using a
		// painter:
		Geometry geometry = feature.getGeometry();
		if (geometry instanceof Point) {
			geometry = convertPoint(feature.getStyleInfo(), (Point) geometry, metadata.getScale());
		}
		try {
			LiteShape2 shape = new LiteShape2(geometry, transform, NULL_DECIMATOR, false, false);
			Style2D style = Style2dFactory.createStyle(feature.getStyleInfo(), feature.getLayer().getLayerInfo()
					.getLayerType());
			painter.paint(graphics, shape, style, metadata.getScale());
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		} catch (FactoryException e) {
			log.error(e.getMessage(), e);
		}
	}

	private Geometry convertPoint(FeatureStyleInfo style, Point point, double scale) {
		if (style.getSymbol() != null) {
			if (style.getSymbol().getRect() != null) {
				RectInfo rect = style.getSymbol().getRect();
				double w = (rect.getW() / 2) / scale;
				double h = (rect.getH() / 2) / scale;
				Coordinate c = point.getCoordinate();
				Coordinate[] coords = new Coordinate[5];
				coords[0] = new Coordinate(c.x - w, c.y - h);
				coords[1] = new Coordinate(c.x + w, c.y - h);
				coords[2] = new Coordinate(c.x + w, c.y + h);
				coords[3] = new Coordinate(c.x - w, c.y + h);
				coords[4] = coords[0];
				return point.getFactory().createLinearRing(coords);
			} else if (style.getSymbol().getCircle() != null) {
				CircleInfo circle = style.getSymbol().getCircle();
				double r = circle.getR() / scale;
				return geoService.createCircle(point, r, 16);
			}
		}
		return point;
	}


}