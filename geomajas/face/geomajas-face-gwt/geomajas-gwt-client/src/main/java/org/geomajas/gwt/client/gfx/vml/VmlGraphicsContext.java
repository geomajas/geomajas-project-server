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

package org.geomajas.gwt.client.gfx.vml;

import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.AbstractGraphicsContext;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.gfx.vml.decoder.VmlPathDecoder;
import org.geomajas.gwt.client.gfx.vml.decoder.VmlStyleUtil;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.DOM;

import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.util.SC;

/**
 * Implementation of the GraphicsContext interface using the VML language for Internet Explorer.
 * 
 * @author Pieter De Graef
 */
public class VmlGraphicsContext extends AbstractGraphicsContext {

	private Element rootNode;

	private static final String DEFAULT_STYLE = "defaultstyle";

	private int width;

	private int height;

	private String id;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor for the VML Graphics Context. It will initialize the VML name space if needed.
	 */
	public VmlGraphicsContext() {
		// Initialize the VML namespace:
		DOM.initVMLNamespace();
	}

	public PaintableGroup getDefsGroup() {
		return null;
	}

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
		Element circle = createOrUpdateElement(DOM.NS_VML, parent, name, "oval", style, null);

		// Real position is the upper left corner of the circle:
		applyAbsolutePosition(circle, new Coordinate(position.getX() - radius, position.getY() - radius));

		// width and height are both radius*2
		int size = (int) (2 * radius);
		applyElementSize(circle, size, size, false);
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
	 *            VML fragment
	 * @param transformation
	 *            transformation to apply to the group
	 */
	public void drawData(Object parent, Object object, String data, Matrix transformation) {
		Element group = getGroup(object);
		if (group == null) {
			group = createOrUpdateGroup(parent, object, transformation, null);
			DOM.setInnerHTML(group, data);
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
		Element image = createOrUpdateElement(DOM.NS_VML, parent, name, "image", style, null);
		applyAbsolutePosition(image, bounds.getOrigin());
		applyElementSize(image, (int) bounds.getWidth(), (int) bounds.getHeight(), false);
		DOM.setElementAttribute(image, "src", href);
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
		Element element = createOrUpdateElement(DOM.NS_VML, parent, name, "shape", style, null);
		if (line != null) {
			DOM.setElementAttribute(element, "path", VmlPathDecoder.decode(line));
			DOM.setStyleAttribute(element, "position", "absolute");
			applyElementSize(element, width, height, false);
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
		Element element = createOrUpdateElement(DOM.NS_VML, parent, name, "shape", style, null);
		if (polygon != null) {
			DOM.setStyleAttribute(element, "position", "absolute");
			DOM.setElementAttribute(element, "fill-rule", "evenodd");
			DOM.setElementAttribute(element, "path", VmlPathDecoder.decode(polygon));
			applyElementSize(element, getWidth(), getHeight(), false);
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
		Element element = createOrUpdateElement(DOM.NS_VML, parent, name, "rect", style, null);
		applyAbsolutePosition(element, rectangle.getOrigin());
		applyElementSize(element, (int) rectangle.getWidth(), (int) rectangle.getHeight(), false);
	}

	/**
	 * Draw a type (shapetype for vml).
	 * 
	 * @param parent
	 *            the parent of the shapetype
	 * @param id
	 *            the types's unique identifier
	 * @param symbol
	 *            the symbol information
	 * @param style
	 *            The default style to apply on the shape-type. Can be overridden when a shape uses this shape-type.
	 * @param transformation
	 *            the transformation to apply on the symbol
	 */
	public void drawSymbolDefinition(Object parent, String id, SymbolInfo symbol, ShapeStyle style,
			Matrix transformation) {
		if (symbol == null) {
			return;
		}

		// Step1: get or create the shape-type element:
		Element shapeType = DOM.getElementById(id);
		boolean isNew = (shapeType == null);
		shapeType = createOrUpdateElement(DOM.NS_VML, null, id, "shapetype", style, transformation, false);

		// If it is a new shape-type, define the necessary elements:
		if (isNew) {
			Element formulas = DOM.createElementNS(DOM.NS_VML, "formulas");
			shapeType.appendChild(formulas);
			DOM.setElementAttribute(shapeType, "coordsize", "1000 1000");

			// Prepare 4 formulas TODO: how to extend this for complex geometries????
			for (int i = 0; i < 4; i++) {
				formulas.appendChild(DOM.createElementNS(DOM.NS_VML, "f"));
			}
		}

		// Check what type of symbol:
		if (symbol.getRect() != null) {

			// Define a rectangle:
			NodeList<com.google.gwt.dom.client.Element> formulas = shapeType.getElementsByTagName("f");
			float width = symbol.getRect().getW();
			float height = symbol.getRect().getH();

			// Create the rectangle definition:
			Element formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #0 " + "0 " + (int) (width / 2));
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(0));

			formula = (Element) formulas.getItem(1);
			DOM.setElementAttribute(formula, "eqn", "sum #1 " + "0 " + (int) (height / 2));
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(1));

			formula = (Element) formulas.getItem(2);
			DOM.setElementAttribute(formula, "eqn", "sum #0 " + (int) (width / 2) + " 0");
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(2));

			formula = (Element) formulas.getItem(3);
			DOM.setElementAttribute(formula, "eqn", "sum #1 " + (int) (height / 2) + " 0");
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(3));

			DOM.setElementAttribute(shapeType, "path", "m@0@1 l@2@1 @2@3 @0@3xe");
		} else if (symbol.getCircle() != null) {

			// Define a circle:
			NodeList<com.google.gwt.dom.client.Element> formulas = shapeType.getElementsByTagName("f");

			// Create the circle definition:
			Element formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #0 0 0");
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(0));

			formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #1 0 0");
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(1));

			float radius = symbol.getCircle().getR();
			DOM.setElementAttribute(shapeType, "path", "al @0 @1 " + radius + " " + radius + " 0 23592600x");
		}
	}

	/**
	 * Draw a symbol, using some predefined ShapeType.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The symbol's name.
	 * @param symbol
	 *            The symbol's (X,Y) location on the graphics.
	 * @param style
	 *            The style to apply on the symbol.
	 * @param shapeTypeId
	 *            The name of the predefined ShapeType. This symbol will create a reference to this predefined type and
	 *            take on it's characteristics.
	 */
	public void drawSymbol(Object parent, String name, Coordinate position, ShapeStyle style, String shapeTypeId) {
		Element shape = createOrUpdateElement(DOM.NS_VML, parent, name, "shape", style, null);
		if (shapeTypeId != null) {
			shape.setAttribute("type", "#" + shapeTypeId);
		} else if (shape.getParentNode() instanceof Element) {
			Element parentElement = (Element) shape.getParentNode();
			shape.setAttribute("type", "#" + parentElement.getId() + ".style");
		}
		if (position != null) {
			applyAbsolutePosition(shape, position);
			DOM.setStyleAttribute(shape, "width", "100%");
			DOM.setStyleAttribute(shape, "height", "100%");
			DOM.setElementAttribute(shape, "coordsize", width + " " + height);
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
		Element element = createOrUpdateElement(DOM.NS_VML, parent, name, "textbox", style, null);
		if (element != null) {
			// Set position, style and content:
			applyAbsolutePosition(element, position);
			VmlStyleUtil.applyStyle(element, style);

			// Set width, because this may change otherwise...
			int textWidth = width - (int) position.getX(); // @todo ??????
			if (textWidth <= 0) {
				textWidth = 10;
			}
			DOM.setStyleAttribute(element, "width", textWidth + "px");
			element.setInnerText(text);
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
			DOM.setStyleAttribute(element, "visibility", "hidden");
		}
	}

	/**
	 * The initialization function for the GraphicsContext. It will create the initial DOM structure setup.
	 * 
	 * @param parent
	 *            The parent element, onto whom to attach the initial DOM structure.
	 */
	public void initialize(Element parent) {
		rootNode = DOM.createElementNS(DOM.NS_HTML, "div");
		id = DOM.createUniqueId();
		rootNode.setId(id);
		applyElementSize(rootNode, width, height, false);
		DOM.setStyleAttribute(rootNode, "clip", "rect(0 " + width + "px " + height + "px 0)");
		DOM.setStyleAttribute(rootNode, "overflow", "hidden");
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
			applyElementSize(rootNode, newWidth, newHeight, false);
			DOM.setStyleAttribute(rootNode, "clip", "rect(0 " + newWidth + "px " + newHeight + "px 0)");
		} else {
			SC.logWarn("problems");
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
			DOM.setStyleAttribute(element, "visibility", "inherit");
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
			DOM.setStyleAttribute(element, "cursor", cursor);
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/**
	 * Creates either a VML group. A group is meant to group other elements together. Also this method gives you the
	 * opportunity to specify a specific width and height.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
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
			group = createGroup(DOM.NS_VML, parent, object, "group");
		}

		if (group != null) {
			// Get the parent element:
			Element parentElement = null;
			if (parent == null) {
				parentElement = getRootElement();
			} else {
				parentElement = getGroup(parent);
			}

			// Inherit size from parent if not specified
			if (parentElement != null) {
				String width = DOM.getStyleAttribute(parentElement, "width");
				String height = DOM.getStyleAttribute(parentElement, "height");
				// sizes should be numbers + px
				int w = Integer.parseInt(width.substring(0, width.indexOf('p')));
				int h = Integer.parseInt(height.substring(0, height.indexOf('p')));
				applyElementSize(group, w, h, true);
			}

			// Apply element transformation:
			if (transformation != null) {
				applyAbsolutePosition(group, new Coordinate(transformation.getDx(), transformation.getDy()));

				if (transformation.getXx() != 1) {
					int w = Math.abs((int) Math.ceil(width / transformation.getXx()));
					int h = Math.abs((int) Math.ceil(height / transformation.getYy()));
					DOM.setElementAttribute(group, "coordsize", w + " " + h);
				}
			} else {
				applyAbsolutePosition(group, new Coordinate(0, 0));
			}
			if (style instanceof ShapeStyle) {
				drawSymbolDefinition(object, DEFAULT_STYLE, null, (ShapeStyle) style, transformation);
			}
		}
		return group;
	}

	/**
	 * Create or update an element in the DOM. The id will be generated.
	 * 
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
	 * 
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

		boolean isNew = true;
		if (generateId) {
			isNew = (getElement(parent, name) == null);
		} else {
			isNew = (DOM.getElementById(name) == null);
		}

		Element element = createOrUpdateElement(namespace, parent, name, type, transformation, generateId);
		Element parentElement = getGroup(parent);

		// Part 2: Apply styling and/or transformation on the element:
		if (namespace != null && namespace.equals(DOM.NS_VML)) {
			if (isNew) {
				Element stroke = DOM.createElementNS(namespace, "stroke");
				element.appendChild(stroke);
				Element fill = DOM.createElementNS(namespace, "fill");
				element.appendChild(fill);
			}
			if ("shape".equals(name)) {
				// Set the size .....if the parent has a coordsize defined, take it over:
				String coordsize = parentElement.getAttribute("coordsize");
				if (coordsize != null && coordsize.length() > 0) {
					element.setAttribute("coordsize", coordsize);
					DOM.setStyleAttribute(element, "width", "100%"); // dangerous! coordsize of the next element will
					// fail...
					DOM.setStyleAttribute(element, "height", "100%"); // better use absolute px.
				} else {
					applyElementSize(element, getWidth(), getHeight(), true);
				}
			}
			DOM.setStyleAttribute(element, "position", "absolute");

			// Try to copy the parent style first, in case it's a ShapeType (point symbol)
			Element shapetypeElement = getElement(parentElement, DEFAULT_STYLE);
			if (shapetypeElement != null) {
				ShapeStyle shapeStyle = VmlStyleUtil.retrieveShapeStyle(shapetypeElement);
				VmlStyleUtil.applyStyle(element, shapeStyle);
			}
			// Possibly override with own style:
			if (style != null && (style instanceof ShapeStyle || style instanceof FontStyle)) {
				VmlStyleUtil.applyStyle(element, style);
			}

		} else {
			// Apply styling on the element:
			if (style != null) {
				DOM.setElementAttribute(element, "style", decode(style));
			}
		}
		return element;
	}

	/**
	 * Apply an absolute position on an element.
	 * 
	 * @param element
	 *            The element that needs an absolute position.
	 * @param position
	 *            The position as a Coordinate.
	 */
	private void applyAbsolutePosition(Element element, Coordinate position) {
		DOM.setStyleAttribute(element, "position", "absolute");
		DOM.setStyleAttribute(element, "left", (int) position.getX() + "px");
		DOM.setStyleAttribute(element, "top", (int) position.getY() + "px");
	}

	/**
	 * Apply a size on an element.
	 * 
	 * @param element
	 *            The element that needs sizing.
	 * @param width
	 *            The new width to apply on the element.
	 * @param height
	 *            The new height to apply on the element.
	 * @param addCoordSize
	 *            Should a coordsize attribute be added as well?
	 */
	private void applyElementSize(Element element, int width, int height, boolean addCoordSize) {
		if (width >= 0 && height >= 0) {
			if (addCoordSize) {
				DOM.setElementAttribute(element, "coordsize", width + " " + height);
			}
			DOM.setStyleAttribute(element, "width", width + "px");
			DOM.setStyleAttribute(element, "height", height + "px");
		}
	}
}