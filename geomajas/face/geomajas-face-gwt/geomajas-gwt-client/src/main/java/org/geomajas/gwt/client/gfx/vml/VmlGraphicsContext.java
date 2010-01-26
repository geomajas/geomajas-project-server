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

import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.util.SC;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.AbstractGraphicsContext;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.gfx.style.SymbolStyle;
import org.geomajas.gwt.client.gfx.vml.decoder.VmlPathDecoder;
import org.geomajas.gwt.client.gfx.vml.decoder.VmlStyleUtil;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.DOM;

import java.util.Map;

/**
 * Implementation of the GraphicsContext interface using the VML language for Internet Explorer.
 * 
 * @author Pieter De Graef
 */
public class VmlGraphicsContext extends AbstractGraphicsContext {

	private String id;

	private Element clipNode;

	private Element backgroundNode;

	private Element mapNode;

	private Element screenNode;

	private String backgroundColor = "#FFFFFF";

	private int width;

	private int height;

	private double precisionScale = 1.0; // TODO: Can't we get rid of this?

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor for the VML Graphics Context. It will initialize the VML name space if needed.
	 */
	public VmlGraphicsContext(String id) {
		this.id = id;

		// Initialize the VML namespace:
		DOM.initVMLNamespace();
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	public Element getElement() {
		return clipNode;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void initialize(Element parent) {
		String content = "<DIV id=\"" + this.id + "_CLIP\" style=\"LEFT: 0px; TOP: 0px; WIDTH: " + width
				+ "px; HEIGHT: " + height + "px; POSITION: absolute; OVERFLOW: hidden; CLIP : rect(0 " + width + "px "
				+ height + "px 0)\">" + "   <DIV id=\"" + this.id + "_root\">" + "      <DIV id=\"" + this.id
				+ "_background\" style=\"WIDTH: " + width + "px; HEIGHT: " + height
				+ "px; POSITION: absolute; BACKGROUND-COLOR: " + backgroundColor + ";\">" + "      </DIV>"
				+ "      <DIV id=\"" + this.id + "_map\" style=\"WIDTH: " + width + "px; HEIGHT: " + height
				+ "px; POSITION: absolute;\">" + "      </DIV>" + "      <DIV id=\"" + this.id
				+ "_screen\" style=\"WIDTH: " + width + "px; HEIGHT: " + height + "px; POSITION: absolute;\">"
				+ "      </DIV>" + "   </DIV>" + "</DIV>";
		parent.setInnerHTML(content);
		// return content;
	}

	/**
	 * Apply a new size on the graphics context.
	 * 
	 * @param newWidth
	 *            The new width in pixels for this graphics context.
	 * @param newHeight
	 *            The new height in pixels for this graphics context.
	 */
	public void setSize(int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;

		// If the base elements don't exist yet, create them:
		if (clipNode == null) {
			clipNode = DOM.getElementById(id + "_CLIP");
			backgroundNode = DOM.getElementById(id + "_background");
			mapNode = DOM.getElementById(id + "_map");
			screenNode = DOM.getElementById(id + "_screen");
		}

		// Now set the correct size on each base element:
		if (clipNode != null) {
			applyElementSize(clipNode, newWidth, newHeight, false);
			DOM.setStyleAttribute(clipNode, "clip", "rect(0 " + newWidth + "px " + newHeight + "px 0)");
			applyElementSize(backgroundNode, newWidth, newHeight, false);
			applyElementSize(mapNode, newWidth, newHeight, false);
			applyElementSize(screenNode, newWidth, newHeight, true);
		} else {
			SC.logWarn("problems");
		}
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
		Element element = DOM.getElementById(elementId);
		if (element != null) {
			if (childrenOnly) {
				while (element.hasChildNodes()) {
					element.removeChild(element.getFirstChild());
				}
			} else {
				Element parent = getParentForId(elementId);
				if (parent != null) {
					parent.removeChild(element);
				}
			}
		}
	}

	public void drawData(String elementId, String data) {
		Element element = DOM.getElementById(elementId);
		if (element == null) {
			element = findOrCreateElement(elementId, DOM.NS_VML, "group", null, null, null);
			element.setInnerHTML(data); // TODO: in javascript we used outerHTML
		}
	}

	public void drawShapeType(String elementId, SymbolInfo symbol, ShapeStyle style, Matrix transformation) {
		boolean isNew = (DOM.getElementById(elementId) == null) ? true : false;
		Element shapeType = findOrCreateElement(elementId, DOM.NS_VML, "shapetype", style, transformation, null);
		DOM.setStyleAttribute(shapeType, "visibility", "hidden");

		// If it is a new shape-type, define the necessary elements:
		if (isNew) {
			Element formulas = DOM.createElementNS(DOM.NS_VML, "formulas");
			shapeType.appendChild(formulas);

			// prepare 4 formulas TODO: how to extend this for complex geometries????
			for (int i = 0; i < 4; i++) {
				Element formula = DOM.createElementNS(DOM.NS_VML, "f");
				formulas.appendChild(formula);
			}
		}

		// Check what type of symbol:
		if (symbol.getRect() != null) {

			// Define a rectangle:
			NodeList<com.google.gwt.dom.client.Element> formulas = shapeType.getElementsByTagName("f");
			float width = symbol.getRect().getW();
			float height = symbol.getRect().getH();

			// Scale if the transformation says so:
			if (transformation != null && transformation.getXx() != 0) {
				double scale = transformation.getXx();
				width = (int) (width * precisionScale / scale);
				height = (int) (height * precisionScale / scale);
			}

			// Create the rectangle definition:
			Element formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #0 " + "0 " + (int) (width / 2));
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(0));

			formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #1 " + "0 " + (int) (height / 2));
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(1));

			formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #0 " + (int) (width / 2) + " 0");
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(2));

			formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #1 " + (int) (height / 2) + " 0");
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(3));

			DOM.setElementAttribute(shapeType, "path", "m@0@1 l@2@1 @2@3 @0@3xe");
		} else if (symbol.getCircle() != null) {

			// Define a circle:
			NodeList<com.google.gwt.dom.client.Element> formulas = shapeType.getElementsByTagName("f");
			float radius = symbol.getCircle().getR();

			// Scale if the transformation says so:
			if (transformation != null && transformation.getXx() != 0) {
				double scale = transformation.getXx();
				radius = (int) (radius * precisionScale / scale);
			}

			// Create the circle definition:
			Element formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #0 0 0");
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(0));

			formula = DOM.createElementNS(DOM.NS_VML, "f");
			DOM.setElementAttribute(formula, "eqn", "sum #1 0 0");
			formulas.getItem(0).getParentNode().replaceChild(formula, formulas.getItem(1));

			DOM.setElementAttribute(shapeType, "path", "al @0 @1 " + radius + " " + radius + " 0 23592600x");
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
		Element element = findOrCreateElement(elementId, DOM.NS_VML, "oval", style, null, null);

		// Real position is the upper left corner of the circle:
		applyAbsolutePosition(element, new Coordinate(position.getX() - radius, position.getY() - radius));

		// width and height are both radius*2
		int size = (int) (2 * radius);
		applyElementSize(element, size, size, false);
	}

	/**
	 * Creates either a VML group or a HTML DIV element, depending on the name-space. A group is meant to group other
	 * elements together.
	 * 
	 * @param elementId
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 */
	public void drawGroup(String elementId, String namespace) {
		if (namespace == null) {
			findOrCreateElement(elementId, null, "div", null, null, null);
		} else if (namespace.equalsIgnoreCase(DOM.NS_VML)) {
			findOrCreateElement(elementId, DOM.NS_VML, "group", null, null, null);
		}
	}

	/**
	 * Creates either a VML group or a HTML DIV element, depending on the name-space. A group is meant to group other
	 * elements together. Also this method gives you the opportunity to specify a specific width and height.
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
		Element element = null;
		if (namespace == null) {
			element = findOrCreateElement(id, null, "div", null, transformation, null);
		} else if (namespace.equalsIgnoreCase(DOM.NS_VML)) {
			element = findOrCreateElement(id, DOM.NS_VML, "group", null, transformation, null);
		}
		if (element != null) {
			applyElementSize(element, width, height, true);
		}
	}

	/**
	 * Creates either a VML group or a HTML DIV element, depending on the name-space. A group is meant to group other
	 * elements together, possibly applying a transformation upon them.
	 * 
	 * @param elementId
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 */
	public void drawGroup(String elementId, String namespace, Matrix transformation) {
		if (namespace == null) {
			findOrCreateElement(elementId, null, "div", null, transformation, null);
		} else if (namespace.equalsIgnoreCase(DOM.NS_VML)) {
			findOrCreateElement(elementId, DOM.NS_VML, "group", null, transformation, null);
		}
	}

	/**
	 * Creates either a VML group or a HTML DIV element, depending on the name-space. A group is meant to group other
	 * elements together, and in this case applying a style upon them.
	 * 
	 * @param elementId
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 * @param style
	 *            Add a style to a group.
	 */
	public void drawGroup(String elementId, String namespace, Style style) {
		if (namespace == null) {
			findOrCreateElement(elementId, null, "div", style, null, null);
		} else if (namespace.equalsIgnoreCase(DOM.NS_VML)) {
			findOrCreateElement(elementId, DOM.NS_VML, "group", style, null, null);
		}
	}

	/**
	 * Creates either a VML group or a HTML DIV element, depending on the name-space. A group is meant to group other
	 * elements together, possibly applying a transformation upon them.
	 * 
	 * @param elementId
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 * @param style
	 *            Add a style to a group.
	 */
	public void drawGroup(String elementId, String namespace, Matrix transformation, Style style) {
		if (namespace == null) {
			findOrCreateElement(elementId, null, "div", style, transformation, null);
		} else if (namespace.equalsIgnoreCase(DOM.NS_VML)) {
			findOrCreateElement(elementId, DOM.NS_VML, "group", style, transformation, null);
		}
	}

	/**
	 * ??? TODO: we have diverted from the Dojo implementation here! Transformation groups should be the responsibility
	 * of the painter, not the GraphicsContext.
	 */
	public void drawImage(String elementId, String href, Bbox bounds, PictureStyle style, boolean asDiv) {
		if (asDiv) {
			// First create a surrounding DIV, and position it:
			Element element = findOrCreateElement(elementId, null, "div", style, null, null);
			applyAbsolutePosition(element, bounds.getOrigin());
			applyElementSize(element, (int) bounds.getWidth(), (int) bounds.getHeight(), false);
			DOM.setStyleAttribute(element, "border", "0");
			DOM.setStyleAttribute(element, "display", "inline");

			// Then create the actual IMG element:
			Element image = findOrCreateElement(elementId + ".img", null, "img", style, null, null);
			DOM.setElementAttribute(image, "src", href);
			DOM.setStyleAttribute(image, "width", "100%");
			DOM.setStyleAttribute(image, "height", "100%");
			if (style != null) {
				VmlStyleUtil.applyStyle(element, style);
			}
		} else {
			// If there is a transform object, create a group first???
			// NO NO NO: let the painter create the group !!!
			Element element = findOrCreateElement(elementId, DOM.NS_VML, "image", style, null, null);
			applyAbsolutePosition(element, bounds.getOrigin());
			applyElementSize(element, (int) bounds.getWidth(), (int) bounds.getHeight(), false);
			DOM.setElementAttribute(element, "src", href);
			if (style != null) {
				VmlStyleUtil.applyStyle(element, style);
			}
		}
	}

	/**
	 * Draw a {@link LineString} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param id
	 *            The LineString's identifier.
	 * @param line
	 *            The LineString to be drawn.
	 * @param style
	 *            The styling object for the LineString. Watch out for fill colors! If the fill opacity is not 0, then
	 *            the LineString will have a fill surface.
	 */
	public void drawLine(String id, LineString line, ShapeStyle style) {
		if (line != null && line instanceof LineString) {
			Element element = findOrCreateElement(id, DOM.NS_VML, "shape", style, null, null);
			DOM.setElementAttribute(element, "path", VmlPathDecoder.decode(line));
			DOM.setStyleAttribute(element, "position", "absolute");
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
		if (polygon != null && polygon instanceof Polygon) {
			Element element = findOrCreateElement(elementId, DOM.NS_VML, "shape", style, null, null);
			DOM.setStyleAttribute(element, "position", "absolute");
			DOM.setElementAttribute(element, "fill-rule", "evenodd");
			DOM.setElementAttribute(element, "path", VmlPathDecoder.decode(polygon));
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
		Element element = findOrCreateElement(elementId, DOM.NS_VML, "rect", style, null, null);
		applyAbsolutePosition(element, rectangle.getOrigin());
		applyElementSize(element, (int) rectangle.getWidth(), (int) rectangle.getHeight(), false);
	}

	/**
	 * Draw a symbol onto the <code>GraphicsContext</code>.
	 * 
	 * @param elementId
	 *            The symbol's identifier.
	 * @param symbol
	 *            A {@link Point} geometry that indicates where the symbol is to be drawn.
	 * @param options
	 *            List of additional options. Should at least contain an ID. If it also contains a "styleId", then this
	 *            style id is used for styling. Otherwise, we turn to the parent element to find a style.
	 */
	public void drawSymbol(String elementId, Point symbol, ShapeStyle style, String shapeTypeId) {
		Element element = findOrCreateElement(elementId, DOM.NS_VML, "shape", style, null, null);
		if (shapeTypeId != null) {
			element.setAttribute("type", "#" + shapeTypeId);
		} else if (element.getParentNode() instanceof Element) {
			Element parent = (Element) element.getParentNode();
			element.setAttribute("type", "#" + parent.getId() + ".style");
		}

		if (symbol != null) {
			applyAbsolutePosition(element, symbol.getCoordinate());
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
		Element element = findOrCreateElement(elementId, DOM.NS_VML, "textbox", style, null, null);
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
	 * Hide the DOM element with the given id. If the element does not exist, nothing will happen.
	 * 
	 * @param elementId
	 *            The identifier of the element to hide.
	 */
	public void hide(String elementId) {
		Element element = DOM.getElementById(elementId);
		if (element != null) {
			DOM.setStyleAttribute(element, "visibility", "hidden");
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
			DOM.setStyleAttribute(element, "visibility", "inherit");
		}
	}

	/**
	 * Set the background color for the entire GraphicsContext.
	 * 
	 * @param color
	 *            An HTML color code (i.e. #FF0000).
	 */
	public void setBackgroundColor(String color) {
		backgroundColor = color;
		if (backgroundNode != null) {
			DOM.setStyleAttribute(backgroundNode, "backgroundColor", color);
		}
	}

	/**
	 * Set a specific cursor type on a specific element.
	 * 
	 * @param elementId
	 *            optional. If not used, the cursor will be applied on the entire <code>GraphicsContext</code>.
	 * @param cursor
	 *            The string representation of the cursor to use.
	 */
	public void setCursor(String elementId, String cursor) {
		Element element = (elementId == null) ? clipNode : DOM.getElementById(elementId);
		if (element != null) {
			DOM.setStyleAttribute(element, "cursor", cursor);
		}
	}

	// -------------------------------------------------------------------------
	// Private functions.
	// -------------------------------------------------------------------------

	private Element findOrCreateElement(String nodeId, String namespace, String name, Style style,
			Matrix transformation, Map<String, ?> options) {

		// Part 1: Find or create the element:
		if (nodeId == null) {
			return null;
		}
		Element element = null;
		boolean isNew = false;
		if (nodeId.equals(id)) {
			element = mapNode;
		} else {
			element = DOM.getElementById(nodeId);
		}
		Element parent = getParentForId(nodeId);
		if (element == null) {
			element = DOM.createElementNS(namespace, name);
			element.setId(nodeId);
			parent.appendChild(element);
			isNew = true;
		}

		// Part 2: Apply styling and/or transformation on the element:
		if (namespace != null && namespace.equals(DOM.NS_VML)) {
			// For VML elements:
			if ("group".equals(name)) {
				updateVmlGroup(nodeId, element, parent, style, transformation);
			} else {
				if (isNew) {
					Element stroke = DOM.createElementNS(namespace, "stroke");
					element.appendChild(stroke);
					Element fill = DOM.createElementNS(namespace, "fill");
					element.appendChild(fill);
				}
				if ("shape".equals(name)) {
					updateVmlShape(nodeId, element, parent, style);
				} else {
					updateVmlOther(nodeId, element, parent, style);
				}
			}
		} else {
			// For HTML elements:
			updateHtmlElement(nodeId, element, style, transformation);
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
					parent = findOrCreateElement(groupId, null, "div", null, null, null);
					DOM.setStyleAttribute(parent, "position", "absolute");
					return parent;
				} else {
					return parent;
				}
			}
		} else {
			return screenNode;
		}
	}

	// -------------------------------------------------------------------------
	// Private VML update functions, setting style and transformations:
	// -------------------------------------------------------------------------

	private void updateVmlGroup(String nodeId, Element element, Element parent, Style style, Matrix transformation) {
		// Determine element size:
		String width = DOM.getStyleAttribute(parent, "width");
		String height = DOM.getStyleAttribute(parent, "height");
		int w = Integer.parseInt(width.substring(0, width.indexOf('p')));
		int h = Integer.parseInt(height.substring(0, height.indexOf('p')));
		applyElementSize(element, w, h, true);

		// Determine element position:
		if (transformation != null) {
			applyAbsolutePosition(element, new Coordinate(transformation.getDx(), transformation.getDy()));
		} else {
			applyAbsolutePosition(element, new Coordinate(0, 0));
		}

		// Style: create a shape-type for the children. ??
		if (style != null && !(style instanceof SymbolStyle)) {
			Element shape = findOrCreateElement(nodeId, "vml", "shapetype", style, null, null);
			DOM.setStyleAttribute(shape, "visibility", "hidden");
		}
	}

	private void updateVmlShape(String nodeId, Element element, Element parent, Style style) {
		// Set the size .....if the parent has a coordsize defined, take it over:
		String coordsize = parent.getAttribute("coordsize");
		if (coordsize != null && coordsize.length() > 0) {
			element.setAttribute("coordsize", coordsize);
			DOM.setStyleAttribute(element, "width", "100%"); // dangerous! coordsize of the next element will fail...
			DOM.setStyleAttribute(element, "height", "100%"); // better use absolute px.
		} else {
			applyElementSize(element, getWidth(), getHeight(), true);
		}

		updateVmlOther(nodeId, element, parent, style);
	}

	private void updateVmlOther(String nodeId, Element element, Element parent, Style style) {
		DOM.setStyleAttribute(element, "position", "absolute");

		// Try to copy the parent style first, in case it's a ShapeType (point symbol)
		String groupId = getStyleId(nodeId);
		if (!groupId.equals(nodeId)) { // prevent style copy to self
			Element shapetypeElement = DOM.getElementById(groupId);
			if (shapetypeElement != null) {
				ShapeStyle shapeStyle = VmlStyleUtil.retrieveShapeStyle(shapetypeElement);
				VmlStyleUtil.applyStyle(element, shapeStyle);
			}
		}

		// Possibly override with own style:
		if (style != null && (style instanceof ShapeStyle || style instanceof FontStyle)) {
			VmlStyleUtil.applyStyle(element, style);
		}
	}

	// -------------------------------------------------------------------------
	// Private HTML update functions, setting style and transformations:
	// -------------------------------------------------------------------------

	private void updateHtmlElement(String nodeId, Element element, Style style, Matrix transformation) {
		DOM.setStyleAttribute(element, "position", "absolute");
		if (style != null && style instanceof FontStyle) {
			VmlStyleUtil.applyStyle(element, style);
		}

		if (element.getTagName().equalsIgnoreCase("div") && transformation != null) {
			// Transform: translation only.
			applyAbsolutePosition(element, new Coordinate(transformation.getDx(), transformation.getDy()));
			// applyElementSize(element, getWidth(), getHeight(), false);
		}
		applyElementSize(element, getWidth(), getHeight(), false);
	}

	// -------------------------------------------------------------------------
	// Private attribute handling functions:
	// -------------------------------------------------------------------------

	private String getStyleId(String nodeId) {
		return nodeId + ".style";
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