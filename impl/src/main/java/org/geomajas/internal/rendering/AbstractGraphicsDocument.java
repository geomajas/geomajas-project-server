/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.internal.util.WebSafeStringEncoder;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Abstract base class for {@link GraphicsDocument} implementations.
 *
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
public abstract class AbstractGraphicsDocument implements GraphicsDocument {

	private static final Coordinate NULL_COORDINATE = new Coordinate(0, 0);

	/**
	 * A map of all the writers that are needed to transform objects to SVG
	 * code. These writers are implementations of <code>GraphicsWriter</code>
	 * interface
	 */
	protected Map<Class<?>, GraphicsWriter> writers = new HashMap<Class<?>, GraphicsWriter>();

	protected Writer writer;

	private Stack<ElementState> elements = new Stack<ElementState>();

	private boolean inAttribute;

	private String currentId;

	private String rootId;

	/** A formatter for floating point values. Used when writing */
	protected DecimalFormat formatter;

	/**
	 * Initialise the abstract graphics document, specifically meant to set the formatter.
	 *
	 * @param defaultMaxDigits default max digits
	 */
	public AbstractGraphicsDocument(int defaultMaxDigits) {
		Locale locale = new Locale("en", "US");
		DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols(locale);
		decimalSymbols.setDecimalSeparator('.');
		formatter = new DecimalFormat();
		formatter.setDecimalFormatSymbols(decimalSymbols);

		// do not group
		formatter.setGroupingSize(0);

		// do not show decimal SEPARATOR if it is not needed
		formatter.setDecimalSeparatorAlwaysShown(false);
		formatter.setGroupingUsed(false);

		// set default number of fraction digits
		formatter.setMaximumFractionDigits(defaultMaxDigits);

		// minimum fraction digits to 0 so they get not rendered if not needed
		formatter.setMinimumFractionDigits(0);
	}

	public String toString() {
		return writer.toString();
	}

	public void setMaximumFractionDigits(int numDigits) {
		formatter.setMaximumFractionDigits(numDigits);
	}

	public void setMinimumFractionDigits(int numDigits) {
		formatter.setMinimumFractionDigits(numDigits);
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
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
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
		writeAttribute(name, formatter.format(value));
	}

	public void writeAttribute(String name, String value) throws RenderException {
		try {
			checkState(false);
			writer.write(" " + name + "=" + "\"" + safeHtml(value) + "\"");
		} catch (IOException ioe) {
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
		}
	}

	public void writeAttributeEnd() throws RenderException {
		try {
			checkState(true);
			writer.write("\"");
			inAttribute = false;
		} catch (IOException ioe) {
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
		}
	}

	public void writeAttributeStart(String name) throws RenderException {
		try {
			checkState(false);
			writer.write(" " + name + "=\"");
			inAttribute = true;
		} catch (IOException ioe) {
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
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
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
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
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
		}
	}

	public void writePathContent(Coordinate[] coords, char path, char point) throws RenderException {
		try {
			checkState(true);
			writer.write(path);

			Coordinate curr = roundCoordinate(coords[0]);
			writeCoordinate(curr);
			Coordinate prev = curr;

			int nCoords = coords.length;
			if (nCoords > 1) {
				writer.write(point);
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
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
		}
	}

	public void flush() throws RenderException {
		try {
			while (!elements.empty()) {
				closeElement();
			}
			writer.flush();
		} catch (IOException ioe) {
			throw new RenderException(ioe, ExceptionCode.RENDER_DOCUMENT_IO_EXCEPTION);
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
		writer.write(formatter.format(c.x));
		writer.write(' ');
		writer.write(formatter.format(c.y));
	}

	protected double roundDouble(double d) {
		int frac = formatter.getMaximumFractionDigits();
		double scale = Math.pow(10, frac);
		double result = d * scale;
		return Math.round(result) / scale;
	}

	protected void checkState(boolean toCheck) throws RenderException {
		if (this.inAttribute != toCheck) {
			throw new RenderException(toCheck ? ExceptionCode.RENDER_DOCUMENT_EXPECTED_ATTRIBUTE_VALUE :
					ExceptionCode.RENDER_DOCUMENT_UNEXPECTED_ATTRIBUTE_END);
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
		return formatter;
	}

	protected String safeHtml(String value) {
		return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	/** State for an element in the stack. */
	protected static class ElementState {

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
}
