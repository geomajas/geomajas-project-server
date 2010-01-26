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
package org.geomajas.internal.rendering;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.internal.rendering.writers.svg.geometry.BboxWriter;
import org.geomajas.internal.rendering.writers.svg.geometry.GeometryCollectionWriter;
import org.geomajas.internal.rendering.writers.svg.geometry.LineStringWriter;
import org.geomajas.internal.rendering.writers.svg.geometry.MultiLineStringWriter;
import org.geomajas.internal.rendering.writers.svg.geometry.MultiPointWriter;
import org.geomajas.internal.rendering.writers.svg.geometry.MultiPolygonWriter;
import org.geomajas.internal.rendering.writers.svg.geometry.PointWriter;
import org.geomajas.internal.rendering.writers.svg.geometry.PolygonWriter;
import org.geomajas.internal.util.WebSafeStringEncoder;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

/**
 * <p>
 * GraphicsDocument implementation for writing VML documents. This document is
 * used when painting a tile's SVG when the <code>VectorRendering</code>
 * rendering strategy is used, and the renderer specifies SVG.
 * </p>
 *
 * @author Pieter De Graef
 */
public class DefaultSvgDocument implements GraphicsDocument {

	/**
	 * Default value for the number of digits after the fraction.
	 */
	protected static final int DEFAULT_MAX_DIGITS = 5;

	/**
	 * A formatter for floating point values. Used when writing
	 */
	protected static DecimalFormat FORMATTER;
	/**
	 * Little piece of code that sets initializes the formatter.
	 */
	static {
		Locale locale = new Locale("en", "US");
		DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols(locale);
		decimalSymbols.setDecimalSeparator('.');
		FORMATTER = new DecimalFormat();
		FORMATTER.setDecimalFormatSymbols(decimalSymbols);

		// do not group
		FORMATTER.setGroupingSize(0);

		// do not show decimal SEPARATOR if it is not needed
		FORMATTER.setDecimalSeparatorAlwaysShown(false);
		FORMATTER.setDecimalFormatSymbols(null);

		// set default number of fraction digits
		FORMATTER.setMaximumFractionDigits(DEFAULT_MAX_DIGITS);

		// minimun fraction digits to 0 so they get not rendered if not needed
		FORMATTER.setMinimumFractionDigits(0);
	}

	private static Coordinate NULL_COORDINATE = new Coordinate(0, 0);

	/**
	 * A map of all the writers that are needed to transform objects to SVG
	 * code. These writers are implementations of <code>GraphicsWriter</code>
	 * interface
	 */
	protected Map<Class<?>, GraphicsWriter> writers = new HashMap<Class<?>, GraphicsWriter>();

	private Writer writer;

	private Stack<ElementState> elements = new Stack<ElementState>();

	private boolean inAttribute;

	private String currentId;

	private String rootId;

	public DefaultSvgDocument(Writer writer) throws RenderException {
		this(writer, true);
	}

	public DefaultSvgDocument(Writer writer, boolean withSvgTag) throws RenderException {
		this.writer = writer;
		initDefaultWriters();
		if (withSvgTag) {
			writeElement("svg", false);
			writeAttribute("xmlns", "http://www.w3.org/2000/svg");
			writeAttribute("xmlns:hlink", "http://www.w3.org/1999/xlink");
		}
	}

	public String toString() {
		return writer.toString();
	}

	public void setMaximumFractionDigits(int numDigits) {
		FORMATTER.setMaximumFractionDigits(numDigits);
	}

	public void setMinimumFractionDigits(int numDigits) {
		FORMATTER.setMinimumFractionDigits(numDigits);
	}

	public void writeTextNode(String text) throws RenderException {
		try {
			checkState(false);
			if (!elements.empty()) {
				ElementState previous = elements.peek();
				if (!previous.isOpened()) {
					writer.write(">");
					previous.setOpened(true);
				}
				previous.setNeedsCloseTag(true);
			}
			writer.write(WebSafeStringEncoder.escapeHTML(text));
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void writeObject(Object o, boolean asChild) throws RenderException {
		Class<?> c = o.getClass();
		if (writers.containsKey(c)) {
			writers.get(c).writeObject(o, this, asChild);
		} else {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_NO_REGISTERED_WRITER, o.getClass().getName());
		}
	}

	public void writeAttribute(String name, double value) throws RenderException {
		writeAttribute(name, FORMATTER.format(value));
	}

	public void writeAttribute(String name, String value) throws RenderException {
		try {
			checkState(false);
			writer.write(" " + name + "=" + "\"" + value + "\"");
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void writeAttributeEnd() throws RenderException {
		try {
			checkState(true);
			writer.write("\"");
			inAttribute = false;
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void writeAttributeStart(String name) throws RenderException {
		try {
			checkState(false);
			writer.write(" " + name + "=\"");
			inAttribute = true;
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void writeElement(String name, boolean asChild) throws RenderException {
		try {
			checkState(false);
			if (!elements.empty()) {
				if (asChild) {
					ElementState previous = elements.peek();
					if (!previous.isOpened()) {
						writer.write(">");
						previous.setOpened(true);
					}
					previous.setNeedsCloseTag(true);
				} else {
					ElementState previous = elements.pop();
					unwindId();
					if (!previous.isOpened()) {
						writer.write(">");
					}
					writer.write("</" + previous.getName() + ">");
				}
			}
			writer.write("<" + name);
			elements.push(new ElementState(name));
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void closeElement() throws RenderException {
		try {
			checkState(false);
			if (!elements.empty()) {
				ElementState current = elements.pop();
				if (current.needsCloseTag()) {
					if (!current.isOpened()) {
						writer.write(">");
					}
					writer.write("</" + current.getName() + ">");
				} else {
					writer.write("/>");
				}
				unwindId();
			}
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void writeClosedPathContent(Coordinate[] coords) throws RenderException {
		try {
			checkState(true);
			writePathContent(coords);
			writer.write('Z');
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void writePathContent(Coordinate[] coords) throws RenderException {
		try {
			checkState(true);
			writer.write('M');

			Coordinate curr = roundCoordinate(coords[0]);
			writeCoordinate(curr);
			Coordinate prev = curr;

			int nCoords = coords.length;
			if (nCoords > 1) {
				writer.write('l');
				for (int i = 1; i < nCoords; i++) {
					curr = coords[i];
					Coordinate delta = new Coordinate(curr.x - prev.x, curr.y - prev.y);
					delta = roundCoordinate(delta);
					if (!delta.equals(NULL_COORDINATE) || i == 1) {
						writeCoordinate(delta);
						prev.x += delta.x;
						prev.y += delta.y;
						writer.write(' ');
					}

				}
			}
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void flush() throws RenderException {
		try {
			if (!elements.empty()) {
				ElementState previous = elements.pop();
				if (previous.needsCloseTag()) {
					writer.write("></" + previous.getName() + ">");
				} else {
					writer.write("/>");
				}
			}
			while (!elements.empty()) {
				ElementState previous = elements.pop();
				writer.write("</" + previous.getName() + ">");
			}
			unwindId();
			writer.flush();
		} catch (IOException ioe) {
			throw new RenderException(ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION, ioe);
		}
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public void registerWriter(Class<?> c, GraphicsWriter writerToRegister) {
		writers.put(c, writerToRegister);
	}

	protected Coordinate roundCoordinate(Coordinate c) throws RenderException {
		return new Coordinate(roundDouble(c.x), roundDouble(c.y));
	}

	protected void writeCoordinate(Coordinate c) throws IOException {
		writer.write(FORMATTER.format(c.x));
		writer.write(' ');
		writer.write(FORMATTER.format(c.y));
	}

	protected double roundDouble(double d) {
		int frac = FORMATTER.getMaximumFractionDigits();
		double scale = Math.pow(10, frac);
		double result = d * scale;
		return Math.round(result) / scale;
	}

	private void checkState(boolean toCheck) throws RenderException {
		if (this.inAttribute != toCheck) {
			throw new RenderException(toCheck ? ExceptionCode.RENDER_DOCUMENT_EXPECTED_ATTRIBUTE_VALUE :
					ExceptionCode.RENDER_DOCUMENT_UNEXPECTED_ATTRIBUTE_END);
		}
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

	/**
	 * ???
	 */
	private class ElementState {

		private String name;

		private String id;

		private boolean needsCloseTag;

		private boolean opened;

		public ElementState(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public boolean needsCloseTag() {
			return needsCloseTag;
		}

		public void setNeedsCloseTag(boolean needsCloseTag) {
			this.needsCloseTag = needsCloseTag;
		}

		public boolean isOpened() {
			return opened;
		}

		public void setOpened(boolean opened) {
			this.opened = opened;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}
	}

	public void writeId(String id) throws RenderException {
		if (currentId == null) {
			if (rootId != null) {
				currentId = rootId + "." + id;
			} else {
				currentId = id;
			}
		} else {
			currentId = currentId + "." + id;
		}
		writeAttribute("id", currentId);
		elements.peek().setId(currentId);
	}

	private void unwindId() {
		if (elements.size() > 0) {
			ElementState head = elements.peek();
			if (head.getId() != null) {
				currentId = head.getId();
			}
		} else {
			currentId = null;
		}
	}

	public String getCurrentId() {
		return currentId;
	}

	public DecimalFormat getFormatter() {
		return FORMATTER;
	}
}