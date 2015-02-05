/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.rendering;

import java.io.IOException;
import java.io.Writer;

import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.rendering.writer.svg.geometry.BboxWriter;
import org.geomajas.internal.rendering.writer.svg.geometry.GeometryCollectionWriter;
import org.geomajas.internal.rendering.writer.svg.geometry.LineStringWriter;
import org.geomajas.internal.rendering.writer.svg.geometry.MultiLineStringWriter;
import org.geomajas.internal.rendering.writer.svg.geometry.MultiPointWriter;
import org.geomajas.internal.rendering.writer.svg.geometry.MultiPolygonWriter;
import org.geomajas.internal.rendering.writer.svg.geometry.PointWriter;
import org.geomajas.internal.rendering.writer.svg.geometry.PolygonWriter;
import org.geomajas.rendering.RenderException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * <p>
 * GraphicsDocument implementation for writing VML documents. This document is
 * used when painting a tile's SVG in {@link org.geomajas.internal.layer.vector.GetTileStringContentStep},
 * and the renderer specifies SVG.
 * </p>
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public class DefaultSvgDocument extends AbstractGraphicsDocument {

	/**
	 * Default value for the number of digits after the fraction.
	 */
	protected static final int DEFAULT_MAX_DIGITS = 5;

	public DefaultSvgDocument(Writer writer) throws RenderException {
		this(writer, true);
	}

	public DefaultSvgDocument(Writer writer, boolean withSvgTag) throws RenderException {
		super(DEFAULT_MAX_DIGITS);
		this.writer = writer;
		initDefaultWriters();
		if (withSvgTag) {
			writeElement("svg", false);
			writeAttribute("xmlns", "http://www.w3.org/2000/svg");
			writeAttribute("xmlns:hlink", "http://www.w3.org/1999/xlink");
		}
	}

	public void writeClosedPathContent(Coordinate[] coords) throws RenderException {
		try {
			checkState(true);
			writePathContent(coords);
			writer.write('Z');
		} catch (IOException ioe) {
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
		}
	}

	public void writePathContent(Coordinate[] coords) throws RenderException {
		writePathContent(coords, 'M', 'l');
	}

	private void initDefaultWriters() {
		registerWriter(Bbox.class, new BboxWriter());
		registerWriter(Point.class, new PointWriter());
		registerWriter(LineString.class, new LineStringWriter());
		registerWriter(LinearRing.class, new LineStringWriter());
		registerWriter(Polygon.class, new PolygonWriter());
		registerWriter(MultiPoint.class, new MultiPointWriter());
		registerWriter(MultiLineString.class, new MultiLineStringWriter());
		registerWriter(MultiPolygon.class, new MultiPolygonWriter());
		registerWriter(GeometryCollection.class, new GeometryCollectionWriter());
	}

}