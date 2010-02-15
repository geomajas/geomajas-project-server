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

package org.geomajas.internal.rendering.painter.image;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.internal.rendering.image.LabelStyle;
import org.geomajas.rendering.painter.TilePaintContext;
import org.geomajas.rendering.painter.image.FeatureImagePainter;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * <p>
 * This feature image painter draws the feature's labels on the graphics.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LabelImagePainter implements FeatureImagePainter {

	private final Logger log = LoggerFactory.getLogger(FeatureImagePainter.class);

	/**
	 * The context object helping the painting process.
	 */
	private TilePaintContext tileContext;

	/**
	 * The styling object to be used when painting labels.
	 */
	private LabelStyle style;

	/**
	 * The label's text font size.
	 */
	private int fontSize = 12;

	/**
	 * The label's text font.
	 */
	private Font font = new Font("Arial", Font.PLAIN, fontSize);

	/**
	 * Horizontal blank space between the text and the label's borders.
	 */
	private int labelBorderDistanceX = 3;

	/**
	 * Vertical blank space between the text and the label's borders.
	 */
	private int labelBorderDistanceY = 2;

	/**
	 * The label's border width.
	 */
	private float strokeWidth;

	/**
	 * Transformation definition that transforms the label's position from world to view space.
	 */
	private MathTransform transform;

	/**
	 * Transformer that transforms the label's position from world to view space, using the MathTransform field.
	 */
	private GeometryCoordinateSequenceTransformer transformer;

	private GeoService geoService;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize this painter with a label type definition from a certain layer. This label type definition contains
	 * styles etc.
	 */
	public LabelImagePainter(LabelStyleInfo labelAttributeInfo, GeoService geoService) {
		this.geoService = geoService;
		style = new LabelStyle(labelAttributeInfo.getFontStyle(), labelAttributeInfo.getBackgroundStyle());
		try {
			strokeWidth = labelAttributeInfo.getBackgroundStyle().getStrokeWidth();
		} catch (Exception e) {
			strokeWidth = 0;
		}
		transformer = new GeometryCoordinateSequenceTransformer();
	}

	// -------------------------------------------------------------------------
	// FeatureImagePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint the feature object on the graphics. It is the feature's label that this painter will draw.
	 * 
	 * @param graphics
	 *            The AWT graphics object onto which we draw.
	 * @param feature
	 *            The feature object of which we draw the label.
	 */
	public void paint(Graphics2D graphics, org.geomajas.layer.feature.InternalFeature feature) {
		if (transformer == null) {
			return;
		}

		// Get the label's position and transform it to screen space:
		Coordinate c = geoService.calcDefaultLabelPosition(feature);
		GeometryFactory f = feature.getGeometry().getFactory();
		Point p = f.createPoint(new com.vividsolutions.jts.geom.Coordinate(c.x, c.y));
		Point tp;
		try {
			tp = transformer.transformPoint(p, f);

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

	/**
	 * Set the map context object. This must be done before painting starts!
	 */
	public void setTileContext(TilePaintContext tileContext) {
		this.tileContext = tileContext;
		if (tileContext != null) {
			transform = getMathTransform();
			transformer.setMathTransform(transform);
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
	private MathTransform getMathTransform() {
		double scale = tileContext.getScale();
		Envelope env = tileContext.getAreaOfInterest();
		return ProjectiveTransform.create(new AffineTransform(scale, 0, 0, -scale, -scale * env.getMinX(), scale
				* (env.getMinY() + env.getHeight())));
	}
}