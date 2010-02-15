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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.RectInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.internal.rendering.image.Style2dFactory;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.painter.TilePaintContext;
import org.geomajas.rendering.painter.image.FeatureImagePainter;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.lite.LabelCacheDefault;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.Style2D;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * <p>
 * Implementation of the <code>FeatureImagePainter</code> that paints a feature's geometry on the graphics object.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryImagePainter implements FeatureImagePainter {

	private final Logger log = LoggerFactory.getLogger(FeatureImagePainter.class);

	private static final Decimator NULL_DECIMATOR = new Decimator(-1, -1);

	/**
	 * This is the underlying painter from the GeoTools library that actually renders geometry. To be more precise, it
	 * renders shapes. So we must first convert the JTS geometry into a shape before painting.
	 */
	private StyledShapePainter painter;

	/**
	 * The context object helping the painting process.
	 */
	private TilePaintContext tileContext;

	/**
	 * A transformation object to be used when converting the JTS geometry into a shape that can be rendered.
	 */
	private MathTransform transform;

	private double scale;

	private GeoService geoService;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * The default and only constructor. Initializes it's internal painter.
	 */
	public GeometryImagePainter(GeoService geoService) {
		this.geoService = geoService;
		painter = new StyledShapePainter(new LabelCacheDefault());
	}

	// -------------------------------------------------------------------------
	// FeatureImagePainter implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint the feature object on the graphics. It is the feature's geometry that this painter draws.
	 * 
	 * @param graphics
	 *            The AWT graphics object onto which we draw.
	 * @param feature
	 *            The feature object of which we draw the geometry.
	 */
	public void paint(Graphics2D graphics, InternalFeature feature) {
		if (tileContext == null) {
			return;
		}

		// Get the feature's geometry and turn it into a shape. This shape is
		// transformed from world to screen, and can be rendered using a
		// painter:
		Geometry geometry = feature.getGeometry();
		if (geometry instanceof Point) {
			geometry = convertPoint(feature.getStyleInfo(), (Point) geometry);
		}
		try {
			LiteShape2 shape = new LiteShape2(geometry, transform, NULL_DECIMATOR, false, false);
			Style2D style = Style2dFactory.createStyle(feature.getStyleInfo(), feature.getLayer().getLayerInfo()
					.getLayerType());
			painter.paint(graphics, shape, style, tileContext.getScale());
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		} catch (FactoryException e) {
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
		}
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	private MathTransform getMathTransform() {
		// find coords wrt to upper left corner (0,0) and scale
		// (pix/unit)
		scale = tileContext.getScale();
		Envelope env = tileContext.getAreaOfInterest();
		return ProjectiveTransform.create(new AffineTransform(scale, 0, 0, -scale, -scale * env.getMinX(), scale
				* (env.getMinY() + env.getHeight())));
	}

	private Geometry convertPoint(FeatureStyleInfo style, Point point) {
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