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

package org.geomajas.gwt.client.gfx.svg;

import com.google.gwt.user.client.Element;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.AbstractGraphicsContext;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.gfx.svg.decoder.SvgPathDecoder;
import org.geomajas.gwt.client.gfx.svg.decoder.SvgStyleDecoder;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.DOM;

/**
 * Implementation of the GraphicsContext interface using the SVG language. This class is used in all browsers except
 * Internet Explorer.
 * 
 * @see org.geomajas.gwt.client.gfx.vml.VmlGraphicsContext
 * 
 * @author Pieter De Graef
 */
public class SvgGraphicsContext extends AbstractGraphicsContext {

	private String id;

	private Element clipNode;

	private Element defs;

	private Element backgroundNode;

	private Element mapNode;

	private Element screenNode;

	private String backgroundColor = "#FFFFFF";

	private int width;

	private int height;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor for the SVG Graphics Context.
	 */
	public SvgGraphicsContext(String id) {
		this.id = id;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	public void initialize(Element parent) {
		clipNode = DOM.createElementNS(DOM.NS_SVG, "svg");
		DOM.setElementAttribute(clipNode, "width", width + "");
		DOM.setElementAttribute(clipNode, "height", height + "");
		DOM.setElementAttribute(clipNode, "viewBox", "0 0 " + width + " " + height);

		// Point style definitions:
		defs = DOM.createElementNS(DOM.NS_SVG, "defs");
		clipNode.appendChild(defs);

		// Background:
		Element backgroundGroup = DOM.createElementNS(DOM.NS_SVG, "g");
		DOM.setElementAttribute(backgroundGroup, "style", "fill-opacity:1; stroke:" + backgroundColor
				+ "; stroke-width:0;");
		backgroundNode = DOM.createElementNS(DOM.NS_SVG, "rect");
		DOM.setElementAttribute(backgroundNode, "x", 0 + "");
		DOM.setElementAttribute(backgroundNode, "y", 0 + "");
		DOM.setElementAttribute(backgroundNode, "width", "100%");
		DOM.setElementAttribute(backgroundNode, "height", "100%");
		backgroundGroup.appendChild(backgroundNode);
		clipNode.appendChild(backgroundGroup);

		// Map group:
		mapNode = DOM.createElementNS(DOM.NS_SVG, "g");
		DOM.setElementAttribute(mapNode, "id", id + ".map");
		clipNode.appendChild(mapNode);

		// World space group:
		Element worldNode = DOM.createElementNS(DOM.NS_SVG, "g");
		DOM.setElementAttribute(worldNode, "id", id + "_world");
		clipNode.appendChild(worldNode);

		// Screen space group:
		screenNode = DOM.createElementNS(DOM.NS_SVG, "g");
		DOM.setElementAttribute(screenNode, "id", id + "_screen");
		clipNode.appendChild(screenNode);

		// Append to parent:
		parent.appendChild(clipNode);
	}

	/**
	 * Apply a new size on the graphics context.
	 * 
	 * @param newWidth
	 *            The new newWidth in pixels for this graphics context.
	 * @param newHeight
	 *            The new newHeight in pixels for this graphics context.
	 */
	public void setSize(int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;

		if (clipNode != null) {
			DOM.setElementAttribute(clipNode, "width", newWidth + "");
			DOM.setElementAttribute(clipNode, "height", newHeight + "");
			DOM.setElementAttribute(clipNode, "viewBox", "0 0 " + newWidth + " " + newHeight);
		}
	}

	public Element getElement() {
		return clipNode;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	// -------------------------------------------------------------------------
	// GraphicsContext implementation:
	// -------------------------------------------------------------------------

	/**
	 * Delete in the graphics DOM structure one or more elements. Which elements are to be removed is specified by the
	 * parameters you give here.
	 * 
	 * @param elementId
	 *            The ID of the element in question. Either this element and everything under it is removed, or this
	 *            element's children are removed.
	 * @param childrenOnly
	 *            If this value of false, the element with id will be removed. If this value is true, then only the
	 *            children of element id are removed, but element id itself remains.
	 */
	public void deleteShape(String elementId, boolean childrenOnly) {
		Element shape = DOM.getElementById(elementId);
		if (shape != null) {
			com.google.gwt.dom.client.Element parent = shape.getParentElement();
			try {
				if (childrenOnly) {
					while (shape.hasChildNodes()) {
						DOM.removeChild(shape, (com.google.gwt.user.client.Element) shape.getFirstChildElement());
					}
				} else if (parent != null) {
					DOM.removeChild((com.google.gwt.user.client.Element) parent, shape);
				}
			} catch (Exception e) {
				// do something...
			}
		}
	}

	/**
	 * Draw a circle on the <code>GraphicsContext</code>.
	 * 
	 * @param elementId
	 *            The circle's ID.
	 * @param position
	 *            The center position as a coordinate.
	 * @param radius
	 *            The circle's radius.
	 * @param style
	 *            The styling object by which the circle should be drawn.
	 */
	public void drawCircle(String elementId, Coordinate position, double radius, ShapeStyle style) {
		Element circle = findOrCreateElement(elementId, DOM.NS_SVG, "circle", style, null);
		DOM.setElementAttribute(circle, "cx", (int) position.getX() + "");
		DOM.setElementAttribute(circle, "cy", (int) position.getY() + "");
		DOM.setElementAttribute(circle, "r", (int) radius + "");
	}

	public void drawData(String elementId, String data) {
		Element g = DOM.getElementById(elementId);
		if (g == null) {
			g = findOrCreateElement(elementId, DOM.NS_SVG, "g", null, null);
			DOM.setInnerSvg(g, data);
		}
	}

	/**
	 * Creates a SVG group (G), ignoring the name-space. A group is meant to group other elements together.
	 * 
	 * @param elementId
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. The SvgGraphicsContext always creates SVG elements,
	 *            and therefore ignores this.
	 */
	public void drawGroup(String elementId, String namespace) {
		findOrCreateElement(elementId, DOM.NS_SVG, "g", null, null);
	}

	/**
	 * Creates a SVG group (G), ignoring the name-space. A group is meant to group other elements together. Also this
	 * method gives you the opportunity to specify a specific width and height.
	 * 
	 * @param id
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 * @param width
	 *            A fixed width for the group.
	 * @param height
	 *            A fixed height for the group.
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 */
	public void drawGroup(String id, String namespace, int width, int height, Matrix transformation) {
		Element element = findOrCreateElement(id, DOM.NS_SVG, "g", null, transformation);
		if (element != null) {
			DOM.setElementAttribute(element, "width", width + "");
			DOM.setElementAttribute(element, "height", height + "");
		}
	}

	/**
	 * Creates a SVG group (G), ignoring the name-space. A group is meant to group other elements together, and in this
	 * case applying a style upon them.
	 * 
	 * @param elementId
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. The SvgGraphicsContext always creates SVG elements,
	 *            and therefore ignores this.
	 * @param style
	 *            Add a style to a group.
	 */
	public void drawGroup(String elementId, String namespace, Style style) {
		findOrCreateElement(elementId, DOM.NS_SVG, "g", style, null);
	}

	/**
	 * Creates a SVG group (G), ignoring the name-space. A group is meant to group other elements together, possibly
	 * applying a transformation upon them.
	 * 
	 * @param elementId
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. The SvgGraphicsContext always creates SVG elements,
	 *            and therefore ignores this.
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 */
	public void drawGroup(String elementId, String namespace, Matrix transformation) {
		findOrCreateElement(elementId, DOM.NS_SVG, "g", null, transformation);
	}

	/**
	 * Creates a SVG group (G), ignoring the name-space. A group is meant to group other elements together, possibly
	 * applying a transformation upon them.
	 * 
	 * @param elementId
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. The SvgGraphicsContext always creates SVG elements,
	 *            and therefore ignores this.
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 * @param style
	 *            Add a style to a group.
	 */
	public void drawGroup(String elementId, String namespace, Matrix transformation, Style style) {
		findOrCreateElement(elementId, DOM.NS_SVG, "g", style, transformation);
	}

	/**
	 * Draw an image onto the the <code>GraphicsContext</code>.
	 * 
	 * @param elementId
	 *            The image's identifier.
	 * @param href
	 *            The image's location (URL).
	 * @param bounds
	 *            The bounding box that sets the image's origin (x and y), it's width and it's height.
	 * @param style
	 *            A styling object to be passed along with the image. Can be null.
	 * @param asDiv
	 *            Draw the image as a DIV HTML object. This can only be done in Internet Explorer. The
	 *            SvgGraphicsContext will ignore this parameter.
	 */
	public void drawImage(String elementId, String href, Bbox bounds, PictureStyle style, boolean asDiv) {
		Element image = findOrCreateElement(elementId, DOM.NS_SVG, "image", style, null);
		DOM.setElementAttribute(image, "x", (int) bounds.getX() + "");
		DOM.setElementAttribute(image, "y", (int) bounds.getY() + "");
		DOM.setElementAttribute(image, "width", (int) bounds.getWidth() + "");
		DOM.setElementAttribute(image, "height", (int) bounds.getHeight() + "");
		DOM.setElementAttributeNS(DOM.NS_XLINK, image, "xlink:href", href);
	}

	/**
	 * Draw a {@link LineString} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param elementId
	 *            The LineString's identifier.
	 * @param line
	 *            The LineString to be drawn.
	 * @param style
	 *            The styling object for the LineString. Watch out for fill colors! If the fill opacity is not 0, then
	 *            the LineString will have a fill surface.
	 */
	public void drawLine(String elementId, LineString line, ShapeStyle style) {
		Element element = findOrCreateElement(elementId, DOM.NS_SVG, "path", style, null);
		if (line != null) {
			DOM.setElementAttribute(element, "d", SvgPathDecoder.decode(line));
		}
	}

	/**
	 * Draw a {@link Polygon} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param elementId
	 *            The Polygon's identifier.
	 * @param polygon
	 *            The Polygon to be drawn.
	 * @param style
	 *            The styling object for the Polygon.
	 */
	public void drawPolygon(String elementId, Polygon polygon, ShapeStyle style) {
		Element element = findOrCreateElement(elementId, DOM.NS_SVG, "path", style, null);
		if (polygon != null) {
			DOM.setElementAttribute(element, "d", SvgPathDecoder.decode(polygon));
			DOM.setElementAttribute(element, "fill-rule", "evenodd");
		}
	}

	/**
	 * Draw a rectangle onto the <code>GraphicsContext</code>.
	 * 
	 * @param elementId
	 *            The Rectangle's identifier.
	 * @param rectangle
	 *            The rectangle to be drawn. The bounding box's origin, is the rectangle's upper left corner on the
	 *            screen.
	 * @param style
	 *            The styling object for the rectangle.
	 */
	public void drawRectangle(String elementId, Bbox rectangle, ShapeStyle style) {
		Element element = findOrCreateElement(elementId, DOM.NS_SVG, "rect", style, null);
		DOM.setElementAttribute(element, "x", rectangle.getX() + "");
		DOM.setElementAttribute(element, "y", rectangle.getY() + "");
		DOM.setElementAttribute(element, "width", rectangle.getWidth() + "");
		DOM.setElementAttribute(element, "height", rectangle.getHeight() + "");
	}

	public void drawShapeType(String elementId, SymbolInfo symbol, ShapeStyle style, Matrix transformation) {
		if (symbol == null) {
			return;
		}

		// Step1: get or create the symbol element:
		Element def = DOM.getElementById(elementId);
		boolean exists = false;
		if (def != null) {
			exists = true;
		} else {
			def = DOM.createElementNS(DOM.NS_SVG, "symbol");
			DOM.setElementAttribute(def, "id", elementId);
			DOM.setElementAttribute(def, "overflow", "visible");
		}

		// Step2: fill in the correct values:
		Element node = null;
		if (symbol.getRect() != null) {
			// Create the rectangle symbol:
			long width = (long) symbol.getRect().getW();
			long height = (long) symbol.getRect().getH();
			if (transformation != null && transformation.getXx() != 0) {
				double scale = transformation.getXx();
				width = Math.round(width / scale);
				height = Math.round(height / scale);
			}
			node = DOM.createElementNS(DOM.NS_SVG, "rect");
			DOM.setElementAttribute(node, "width", width + "");
			DOM.setElementAttribute(node, "height", height + "");
			DOM.setElementAttribute(node, "x", -Math.round(width / 2) + "");
			DOM.setElementAttribute(node, "y", -Math.round(height / 2) + "");
		} else if (symbol.getCircle() != null) {
			// Create the circle symbol:
			long radius = (long) symbol.getCircle().getR();
			if (transformation != null && transformation.getXx() != 0) {
				double scale = transformation.getXx();
				radius = Math.round(radius / scale);
			}
			node = DOM.createElementNS(DOM.NS_SVG, "circle");
			DOM.setElementAttribute(node, "cx", "0");
			DOM.setElementAttribute(node, "cy", "0");
			DOM.setElementAttribute(node, "r", radius + "");
		}

		// Step3: Append the symbol definition:
		if (exists) {
			while (def.hasChildNodes()) {
				DOM.removeChild(def, (com.google.gwt.user.client.Element) def.getFirstChildElement());
			}
			def.appendChild(node);
		} else {
			def.appendChild(node);
			defs.appendChild(def);
		}
	}

	/**
	 * Draw a symbol, using some predefined ShapeType.
	 * 
	 * @param elementId
	 *            The symbol's unique identifier.
	 * @param symbol
	 *            The symbol's (X,Y) location on the graphics.
	 * @param style
	 *            The style to apply on the symbol.
	 * @param shapeTypeId
	 *            The name of the predefined ShapeType. This symbol will create a reference to this predefined type and
	 *            take on it's characteristics.
	 */
	public void drawSymbol(String elementId, Point symbol, ShapeStyle style, String shapeTypeId) {
		// Step1: Create a group, and set the style:
		findOrCreateElement(elementId, DOM.NS_SVG, "g", style, null);

		// Step2: Create the "use" element, that refers to the ShapeType
		// definition:
		Element useElement = findOrCreateElement(elementId + ".use", DOM.NS_SVG, "use", null, null);
		if (shapeTypeId == null) {
			shapeTypeId = useElement.getParentElement().getId() + ".style";
		}
		DOM.setElementAttributeNS(DOM.NS_XLINK, useElement, "xlink:href", "#" + shapeTypeId);
		if (symbol != null) {
			Coordinate coordinate = symbol.getCoordinate();
			DOM.setElementAttribute(useElement, "x", coordinate.getX() + "");
			DOM.setElementAttribute(useElement, "y", coordinate.getY() + "");
		}
	}

	/**
	 * Draw a string of text onto the <code>GraphicsContext</code>.
	 * 
	 * @param elementId
	 *            The text's identifier.
	 * @param text
	 *            The actual string content.
	 * @param position
	 *            The upper left corner for the text to originate.
	 * @param style
	 *            The styling object for the text.
	 */
	public void drawText(String elementId, String text, Coordinate position, FontStyle style) {
		Element textElement = findOrCreateElement(elementId, DOM.NS_SVG, "text", style, null);
		textElement.setInnerText(text);

		if (position != null) {
			int fontSize = 12;
			if (style != null) {
				fontSize = style.getFontSize();
			}
			DOM.setElementAttribute(textElement, "x", position.getX() + "");
			DOM.setElementAttribute(textElement, "y", position.getY() + fontSize + "");
		}
	}

	/**
	 * Set the background color for the entire GraphicsContext.
	 * 
	 * @param color
	 *            An HTML color code (i.e. #FF0000).
	 */
	public void setBackgroundColor(String color) {
		DOM.setElementAttribute(backgroundNode, "style", "fill:" + color);
	}

	/**
	 * Set a specific cursor type on a specific element.
	 * 
	 * @param id
	 *            optional. If not used, the cursor will be applied on the entire <code>GraphicsContext</code>.
	 * @param cursor
	 *            The string representation of the cursor to use.
	 */
	public void setCursor(String id, String cursor) {
		Element element = null;
		if (id == null) {
			element = clipNode;
		} else {
			element = DOM.getElementById(id);
		}
		if (element != null) {
			DOM.setElementAttribute(element, "cursor", cursor);
		}
	}

	/**
	 * Hide the DOM element with the given id. If the element does not exist, nothing will happen.
	 * 
	 * @param elementId
	 *            The identifier of the element to hide.
	 */
	public void hide(String elementId) {
		Element element = DOM.getElementById(elementId);
		if (element != null) {
			DOM.setElementAttribute(element, "display", "none");
		}
	}

	/**
	 * Unhide (show) the DOM element with the given id. If the element does not exist, nothing will happen.
	 * 
	 * @param elementId
	 *            The identifier of the element to show again.
	 */
	public void unhide(String elementId) {
		Element element = DOM.getElementById(elementId);
		if (element != null) {
			DOM.setElementAttribute(element, "display", "inline");
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private Element findOrCreateElement(String elementId, String namespace, String name, Style style,
			Matrix transformation) {

		// Part 1: Find or create the element:
		if (elementId == null) {
			return null;
		}
		Element element = null;
		if (elementId.equals(id)) {
			element = mapNode;
		} else {
			element = DOM.getElementById(elementId);
		}
		Element parent = getParentForId(elementId);
		if (element == null) {
			element = DOM.createElementNS(namespace, name);
			if (elementId != null) {
				DOM.setElementAttribute(element, "id", elementId);
			}
			parent.appendChild(element);
			// TODO add image load listener.
		}

		// Part 2: Apply styling on the element:
		if (style != null) {
			DOM.setElementAttribute(element, "style", SvgStyleDecoder.decode(style));
		}

		// Part 3: Apply transformation on the element:
		if (transformation != null) {
			DOM.setElementAttribute(element, "transform", parse(transformation));
		}
		return element;
	}

	/**
	 * Will create if necessary. This is needed when a complex ID is to be created, when even the parents do not exist
	 * yet.
	 * 
	 * @param nodeId
	 * @return
	 */
	private Element getParentForId(String nodeId) {
		int position = nodeId.lastIndexOf(".");
		if (position >= 0) {
			String groupId = nodeId.substring(0, position);
			if (groupId.equals(id)) {
				return mapNode;
			} else {
				Element parent = DOM.getElementById(groupId);
				if (parent == null) {
					return findOrCreateElement(groupId, DOM.NS_SVG, "g", null, null);
				} else {
					return parent;
				}
			}
		} else {
			return screenNode;
		}
	}

	/**
	 * Parse a matrix object into a string, suitable for the SVG 'transform' attribute.
	 * 
	 * @param matrix
	 *            The matrix to parse.
	 * @return The transform string.
	 */
	private String parse(Matrix matrix) {
		String transform = "";
		if (matrix != null) {
			double dx = matrix.getDx();
			double dy = matrix.getDy();
			if (matrix.getXx() != 0 && matrix.getYy() != 0 && matrix.getXx() != 1 && matrix.getYy() != 1) {
				transform += "scale(" + matrix.getXx() + ", " + matrix.getYy() + ")"; // scale first
				// no space between 'scale' and '(' !!!
				dx /= matrix.getXx();
				dy /= matrix.getYy();
			}
			transform += " translate(" + (float) dx + ", " + (float) dy + ")";
			// no space between 'translate' and '(' !!!
		}
		return transform;
	}
}