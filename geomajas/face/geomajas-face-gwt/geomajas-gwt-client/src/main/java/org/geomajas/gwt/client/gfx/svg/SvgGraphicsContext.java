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

import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.AbstractGraphicsContext;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.gfx.svg.decoder.SvgPathDecoder;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.DOM;

import com.google.gwt.user.client.Element;

/**
 * Implementation of the GraphicsContext interface using the SVG language. This class is used in all browsers except
 * Internet Explorer.
 * 
 * @see org.geomajas.gwt.client.gfx.vml.VmlGraphicsContext
 * 
 * @author Pieter De Graef
 */
public class SvgGraphicsContext extends AbstractGraphicsContext {

	private Element rootNode;

	private Composite defsGroup;

	private Element defs;

	private int width;

	private int height;
	
	private String id;

	/**
	 * Draw a circle on the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The circle's name.
	 * @param position
	 *            The center position as a coordinate.
	 * @param radius
	 *            The circle's radius.
	 * @param style
	 *            The styling object by which the circle should be drawn.
	 */
	public void drawCircle(Object parent, String name, Coordinate position, double radius, ShapeStyle style) {
		Element circle = createOrUpdateElement(DOM.NS_SVG, parent, name, "circle", style, null);
		DOM.setElementAttribute(circle, "cx", (int) position.getX() + "");
		DOM.setElementAttribute(circle, "cy", (int) position.getY() + "");
		DOM.setElementAttribute(circle, "r", (int) radius + "");
	}

	/**
	 * Draw inner group data directly (implementation-specific shortcut). This method can only be called once, creating
	 * the group. Delete the group first to redraw with different data.
	 * 
	 * @param parent
	 *            The parent group's object
	 * @param object
	 *            The group's object
	 * @param data
	 *            SVG fragment
	 * @param transformation
	 *            transformation to apply to the group
	 */
	public void drawData(Object parent, Object object, String data, Matrix transformation) {
		Element group = getGroup(object);
		if (group == null) {
			group = createOrUpdateGroup(parent, object, transformation, null);
			DOM.setInnerSvg(group, data);
		}
	}

	/**
	 * Draw an image onto the the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The image's name.
	 * @param href
	 *            The image's location (URL).
	 * @param bounds
	 *            The bounding box that sets the image's origin (x and y), it's width and it's height.
	 * @param style
	 *            A styling object to be passed along with the image. Can be null.
	 */
	public void drawImage(Object parent, String name, String href, Bbox bounds, PictureStyle style) {
		Element image = createOrUpdateElement(DOM.NS_SVG, parent, name, "image", style, null);
		DOM.setElementAttribute(image, "x", (int) bounds.getX() + "");
		DOM.setElementAttribute(image, "y", (int) bounds.getY() + "");
		DOM.setElementAttribute(image, "width", (int) bounds.getWidth() + "");
		DOM.setElementAttribute(image, "height", (int) bounds.getHeight() + "");
		DOM.setElementAttributeNS(DOM.NS_XLINK, image, "xlink:href", href);
	}

	/**
	 * Draw a {@link LineString} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The LineString's name.
	 * @param line
	 *            The LineString to be drawn.
	 * @param style
	 *            The styling object for the LineString. Watch out for fill colors! If the fill opacity is not 0, then
	 *            the LineString will have a fill surface.
	 */
	public void drawLine(Object parent, String name, LineString line, ShapeStyle style) {
		Element element = createOrUpdateElement(DOM.NS_SVG, parent, name, "path", style, null);
		if (line != null) {
			DOM.setElementAttribute(element, "d", SvgPathDecoder.decode(line));
		}
	}

	/**
	 * Draw a {@link Polygon} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The Polygon's name.
	 * @param polygon
	 *            The Polygon to be drawn.
	 * @param style
	 *            The styling object for the Polygon.
	 */
	public void drawPolygon(Object parent, String name, Polygon polygon, ShapeStyle style) {
		Element element = createOrUpdateElement(DOM.NS_SVG, parent, name, "path", style, null);
		if (polygon != null) {
			DOM.setElementAttribute(element, "d", SvgPathDecoder.decode(polygon));
			DOM.setElementAttribute(element, "fill-rule", "evenodd");
		}
	}

	/**
	 * Draw a rectangle onto the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The rectangle's name.
	 * @param rectangle
	 *            The rectangle to be drawn. The bounding box's origin, is the rectangle's upper left corner on the
	 *            screen.
	 * @param style
	 *            The styling object for the rectangle.
	 */
	public void drawRectangle(Object parent, String name, Bbox rectangle, ShapeStyle style) {
		Element element = createOrUpdateElement(DOM.NS_SVG, parent, name, "rect", style, null);
		DOM.setElementAttribute(element, "x", rectangle.getX() + "");
		DOM.setElementAttribute(element, "y", rectangle.getY() + "");
		DOM.setElementAttribute(element, "width", rectangle.getWidth() + "");
		DOM.setElementAttribute(element, "height", rectangle.getHeight() + "");
	}

	/**
	 * Draw a type (def/symbol for svg).
	 * 
	 * @param parent
	 *            All shape-types are placed in the "defs" group. This parameter is therefore ignored.
	 * @param id
	 *            the types's unique identifier
	 * @param symbol
	 *            the symbol information
	 * @param style
	 *            No default style is allowed at the moment in SVG. This parameter is ignored.
	 * @param transformation
	 *            the transformation to apply on the symbol
	 */
	public void drawShapeType(Object parent, String id, SymbolInfo symbol, ShapeStyle style, Matrix transformation) {
		if (symbol == null) {
			return;
		}

		// Step1: get or create the symbol element:
		// check existence
		Element def = DOM.getElementById(id);
		boolean isNew = (def == null);
		// create or update
		def = createOrUpdateElement(DOM.NS_SVG, defsGroup, id, "symbol", null, transformation, false);
		DOM.setElementAttribute(def, "overflow", "visible");

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
		if (isNew) {
			def.appendChild(node);
			defs.appendChild(def);
		} else {
			while (def.hasChildNodes()) {
				DOM.removeChild(def, (com.google.gwt.user.client.Element) def.getFirstChildElement());
			}
			def.appendChild(node);
		}
	}

	/**
	 * Draw a symbol, using some predefined ShapeType.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The symbol's name.
	 * @param position
	 *            The symbol's (X,Y) location on the graphics.
	 * @param style
	 *            The style to apply on the symbol.
	 * @param shapeTypeId
	 *            The name of the predefined ShapeType. This symbol will create a reference to this predefined type and
	 *            take on it's characteristics.
	 */
	public void drawSymbol(Object parent, String name, Coordinate position, ShapeStyle style, String shapeTypeId) {
		Element useElement = createOrUpdateElement(DOM.NS_SVG, parent, name, "use", style, null);
		DOM.setElementAttributeNS(DOM.NS_XLINK, useElement, "xlink:href", "#" + shapeTypeId);
		if (position != null) {
			DOM.setElementAttribute(useElement, "x", position.getX() + "");
			DOM.setElementAttribute(useElement, "y", position.getY() + "");
		}
	}

	/**
	 * Draw a string of text onto the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The text's name.
	 * @param text
	 *            The actual string content.
	 * @param position
	 *            The upper left corner for the text to originate.
	 * @param style
	 *            The styling object for the text.
	 */
	public void drawText(Object parent, String name, String text, Coordinate position, FontStyle style) {
		Element element = createOrUpdateElement(DOM.NS_SVG, parent, name, "text", style, null);
		if (text != null) {
			element.setInnerText(text);
		}

		if (position != null) {
			int fontSize = 12;
			if (style != null) {
				fontSize = style.getFontSize();
			}
			DOM.setElementAttribute(element, "x", position.getX() + "");
			DOM.setElementAttribute(element, "y", position.getY() + fontSize + "");
		}
	}

	/**
	 * Return the current graphics height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Return the current graphics width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the root element of this context.
	 * 
	 * @return the root element
	 */
	protected Element getRootElement() {
		return rootNode;
	}

	/**
	 * Hide the specified group. If the group does not exist, nothing will happen.
	 * 
	 * @param group
	 *            The group object.
	 */
	public void hide(Object group) {
		Element element = getGroup(group);
		if (element != null) {
			DOM.setElementAttribute(element, "display", "none");
		}
	}

	/**
	 * The initialization function for the GraphicsContext. It will create the initial DOM structure setup.
	 * 
	 * @param parent
	 *            The parent element, onto whom to attach the initial DOM structure.
	 */
	public void initialize(Element parent) {
		rootNode = DOM.createElementNS(DOM.NS_SVG, "svg");
		id = DOM.createUniqueId();
		rootNode.setId(id);
		DOM.setElementAttribute(rootNode, "width", width + "");
		DOM.setElementAttribute(rootNode, "height", height + "");
		DOM.setElementAttribute(rootNode, "viewBox", "0 0 " + width + " " + height);

		// Point style definitions:
		defsGroup = new Composite("style_defs");
		defs = createGroup(DOM.NS_SVG, null, defsGroup, "defs");
		
		// Append to parent:
		parent.appendChild(rootNode);
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

		if (rootNode != null) {
			DOM.setElementAttribute(rootNode, "width", newWidth + "");
			DOM.setElementAttribute(rootNode, "height", newHeight + "");
			DOM.setElementAttribute(rootNode, "viewBox", "0 0 " + newWidth + " " + newHeight);
		}
	}

	/**
	 * Hide the specified group. If the group does not exist, nothing will happen.
	 * 
	 * @param group
	 *            The group object.
	 */
	public void unhide(Object group) {
		Element element = getGroup(group);
		if (element != null) {
			DOM.setElementAttribute(element, "display", "inline");
		}
	}

	/**
	 * Set a specific cursor on an element of this <code>GraphicsContext</code>.
	 * 
	 * @param element
	 *            the element on which the cursor should be set.
	 * @param cursor
	 *            The string representation of the cursor to use.
	 */
	protected void doSetCursor(Element element, String cursor) {
		if (element != null) {
			DOM.setElementAttribute(element, "cursor", cursor);
		}
	}
	
	/**
	 * Creates either a SVG group or a HTML DIV element, depending on the name-space. A group is meant to group other
	 * elements together. Also this method gives you the opportunity to specify a specific width and height.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 * @param asDiv
	 *            true if a HTML DIV element should be created, false otherwise
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 * @param style
	 *            Add a style to a group.
	 * @return the group element          
	 */
	protected Element createOrUpdateGroup(Object parent, Object object, Matrix transformation, Style style) {
		Element group = null;
		// check existence
		if (object != null) {
			group = getGroup(object);
		}
		// create if necessary
		if (group == null) {
			group = createGroup(DOM.NS_SVG, parent, object, "g");
		}
		// Apply transformation on the element:
		if (transformation != null) {
			DOM.setElementAttribute(group, "transform", parse(transformation));
		}
		// SVG style is just CSS, so ok for both
		if (style != null) {
			DOM.setElementAttribute(group, "style", decode(style));
		}
		return group;
	}

	/**
	 * Create or update an element in the DOM. The id will be generated.
	 * @param namespace
	 *            the name space (HTML or SVG)
	 * @param parent
	 *            the parent group
	 * @param name
	 *            the local group name of the element (should be unique within the group)
	 * @param type
	 *            the type of the element (tag name, e.g. 'image')
	 * @param style
	 *            The style to apply on the element.
	 * @param transformation
	 *            the transformation to apply on the element
	 * @return the created or updated element or null if creation failed
	 */
	private Element createOrUpdateElement(String namespace, Object parent, String name, String type, Style style,
			Matrix transformation) {
		return createOrUpdateElement(namespace, parent, name, type, style, transformation, true);
	}

	/**
	 * Create or update an element in the DOM. The id will be generated.
	 * @param namespace
	 *            the name space (HTML or SVG)
	 * @param parent
	 *            the parent group
	 * @param name
	 *            the local group name of the element (should be unique within the group)
	 * @param type
	 *            the type of the element (tag name, e.g. 'image')
	 * @param style
	 *            The style to apply on the element.
	 * @param transformation
	 *            the transformation to apply on the element
	 * @param generateId
	 *            true if a unique id may be generated, otherwise the name will be used as id
	 * @return the created or updated element or null if creation failed
	 */
	private Element createOrUpdateElement(String namespace, Object parent, String name, String type, Style style,
			Matrix transformation, boolean generateId) {
		Element element = createOrUpdateElement(namespace, parent, name, type, transformation, generateId);
		// Apply styling on the element:
		if (element != null && style != null) {
			DOM.setElementAttribute(element, "style", decode(style));
		}
		return element;
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

	
	public Composite getDefsGroup() {
		return defsGroup;
	}

}