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

package org.geomajas.gwt.client.gfx.context;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.context.DomHelper.Namespace;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.DOM;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;

/**
 * Implementation of the GraphicsContext interface using the VML language for Internet Explorer.
 * 
 * @author Pieter De Graef
 */
public class VmlGraphicsContext implements GraphicsContext {

	private int width;

	private int height;

	private String id;

	private Map<String, SymbolDefinition> symbolDefs = new HashMap<String, SymbolDefinition>();

	private DomHelper helper;

	private Widget parent;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Constructs an image context, appending the root element to the specified parent widget.
	 * 
	 * @param parent
	 *            parent widget
	 */
	public VmlGraphicsContext(Widget parent) {
		this.parent = parent;
		
		// Initialize the VML namespace:
		DOM.initVMLNamespace();
		
		// the root VML node 
		Element rootNode = DOM.createElementNS(DOM.NS_HTML, "div");
		id = DOM.createUniqueId();
		rootNode.setId(id);
		DOM.setStyleAttribute(rootNode, "position", "absolute");
		DOM.setStyleAttribute(rootNode, "width", "100%");
		DOM.setStyleAttribute(rootNode, "height", "100%");
		DOM.setStyleAttribute(rootNode, "clip", "rect(0 " + width + "px " + height + "px 0)");
		DOM.setStyleAttribute(rootNode, "overflow", "hidden");
		helper = new DomHelper(rootNode, Namespace.VML);
		
		// Append to parent: we need a top div or the vml is blocked by any peer div !!!
		parent.getElement().appendChild(rootNode);
	}

	/**
	 * Delete this element from the graphics DOM structure.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The element's name.
	 */
	public void deleteElement(Object parent, String name) {
		if (isAttached()) {
			helper.deleteElement(parent, name);
		}
	}

	/**
	 * Delete this group from the graphics DOM structure.
	 * 
	 * @param object
	 *            The group's object.
	 */
	public void deleteGroup(Object object) {
		if (isAttached()) {
			helper.deleteGroup(object);
		}
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
		if (isAttached()) {
			Element circle = helper.createOrUpdateElement(parent, name, "oval", style);

			// Real position is the upper left corner of the circle:
			applyAbsolutePosition(circle, new Coordinate(position.getX() - radius, position.getY() - radius));

			// width and height are both radius*2
			int size = (int) (2 * radius);
			applyElementSize(circle, size, size, false);
		}
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
		if (isAttached()) {
			Element group = helper.getGroup(object);
			if (group == null) {
				group = helper.createOrUpdateGroup(parent, object, transformation, null);
				DOM.setInnerHTML(group, data);
			}
		}
	}

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context. A group is meant to group other elements
	 * together.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 */
	public void drawGroup(Object parent, Object object) {
		if (isAttached()) {
			helper.createOrUpdateGroup(parent, object, null, null);
		}
	}

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context with the specified tag name.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 * @param tagName
	 *            the tag name
	 */
	public Element drawGroup(Object parent, Object object, String tagName) {
		if (isAttached()) {
			return helper.drawGroup(parent, object, tagName);
		} else {
			return null;
		}
	}

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context. A group is meant to group other elements
	 * together, possibly applying a transformation upon them.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element.
	 */
	public void drawGroup(Object parent, Object object, Matrix transformation) {
		if (isAttached()) {
			helper.drawGroup(parent, object, transformation);
		}
	}

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context. A group is meant to group other elements
	 * together, and in this case applying a style on them.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 * @param style
	 *            Add a style to a group.
	 */
	public void drawGroup(Object parent, Object object, Style style) {
		if (isAttached()) {
			helper.drawGroup(parent, object, style);
		}
	}

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context. A group is meant to group other elements
	 * together, possibly applying a transformation upon them.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element.
	 * @param style
	 *            Add a style to a group.
	 */
	public void drawGroup(Object parent, Object object, Matrix transformation, Style style) {
		if (isAttached()) {
			helper.createOrUpdateGroup(parent, object, transformation, style);
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
		if (isAttached()) {
			Element image = helper.createOrUpdateElement(parent, name, "image", style);
			applyAbsolutePosition(image, bounds.getOrigin());
			applyElementSize(image, (int) bounds.getWidth(), (int) bounds.getHeight(), true);
			DOM.setElementAttribute(image, "src", href);
		}
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
		if (isAttached()) {
			Element element = helper.createOrUpdateElement(parent, name, "shape", style);
			if (line != null) {
				DOM.setElementAttribute(element, "path", VmlPathDecoder.decode(line));
				DOM.setStyleAttribute(element, "position", "absolute");
				applyElementSize(element, width, height, false);
			}
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
		if (isAttached()) {
			Element element = helper.createOrUpdateElement(parent, name, "shape", style);
			if (polygon != null) {
				DOM.setStyleAttribute(element, "position", "absolute");
				DOM.setElementAttribute(element, "fill-rule", "evenodd");
				DOM.setElementAttribute(element, "path", VmlPathDecoder.decode(polygon));
				applyElementSize(element, getWidth(), getHeight(), false);
			}
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
		if (isAttached()) {
			Element element = helper.createOrUpdateElement(parent, name, "rect", style);
			applyAbsolutePosition(element, rectangle.getOrigin());
			applyElementSize(element, (int) rectangle.getWidth(), (int) rectangle.getHeight(), false);
		}
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
		if (isAttached()) {
			if (symbol == null) {
				return;
			}
			symbolDefs.put(id, new SymbolDefinition(symbol, style));
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
		if (isAttached()) {
			SymbolDefinition definition = symbolDefs.get(shapeTypeId);
			if (position == null) {
				return;
			}
			if (style == null) {
				style = definition.getStyle();
			}
			SymbolInfo symbol = definition.getSymbol();
			if (symbol.getRect() != null) {
				Element rect = helper.createOrUpdateElement(parent, name, "rect", style);

				// Real position is the upper left corner of the rectangle:
				float w = symbol.getRect().getW();
				float h = symbol.getRect().getH();
				applyAbsolutePosition(rect, new Coordinate(position.getX() - 0.5 * w, position.getY() - 0.5 * h));

				// width and height
				applyElementSize(rect, (int) w, (int) h, false);

			} else if (symbol.getCircle() != null) {
				Element circle = helper.createOrUpdateElement(parent, name, "oval", style);

				// Real position is the upper left corner of the circle:
				float radius = symbol.getCircle().getR();
				applyAbsolutePosition(circle, new Coordinate(position.getX() - radius, position.getY() - radius));

				// width and height are both radius*2
				int size = (int) (2 * radius);
				applyElementSize(circle, size, size, false);
			}

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
		if (isAttached()) {
			Element element = helper.createOrUpdateElement(parent, name, "textbox", style);
			if (element != null) {
				// Set position, style and content:
				applyAbsolutePosition(element, position);
				VmlStyleUtil.applyStyle(element, style);

				// Set width, because this may change otherwise...
				int textWidth = width - (int) position.getX();
				if (textWidth <= 0) {
					textWidth = 10;
				}
				DOM.setStyleAttribute(element, "width", textWidth + "px");
				element.setInnerText(text);
			}
		}
	}

	/**
	 * Return the (enclosing) group for the specified element id.
	 * 
	 * @param id
	 * @return the group object
	 */
	public Object getGroupById(String id) {
		if (isAttached()) {
			return helper.getGroupById(id);
		} else {
			return null;
		}
	}

	/**
	 * Return the id of the specified group.
	 * 
	 * @param group the group object
	 * @return the corresponding element id or null if the group has not been drawn.
	 */
	public String getId(Object group) {
		return helper.getId(group);
	}

	/**
	 * Return the unique id of the container div of this context.
	 * 
	 * @return the unique id of the container div.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return the element name for the specified id.
	 * 
	 * @param id
	 * @return the name of the element
	 */
	public String getNameById(String id) {
		if (isAttached()) {
			return helper.getNameById(id);
		} else {
			return null;
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
	 * Hide the specified group. If the group does not exist, nothing will happen.
	 * 
	 * @param group
	 *            The group object.
	 */
	public void hide(Object group) {
		if (isAttached()) {
			Element element = helper.getGroup(group);
			if (element != null) {
				DOM.setStyleAttribute(element, "visibility", "hidden");
			}
		}
	}

	/**
	 * Set the controller on an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param object
	 *            the element on which the controller should be set.
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 */
	public void setController(Object object, GraphicsController controller) {
		if (isAttached()) {
			helper.setController(object, controller);
		}
	}

	/**
	 * Set the controller on an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param parent
	 *            the parent of the element on which the controller should be set.
	 * @param name
	 *            the name of the child element on which the controller should be set
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 */
	public void setController(Object parent, String name, GraphicsController controller) {
		if (isAttached()) {
			helper.setController(parent, name, controller);
		}
	}

	/**
	 * Set the controller on an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param object
	 *            the element on which the controller should be set.
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 * @param eventMask
	 *            a bitmask to specify which events to listen for {@link com.google.gwt.user.client.Event}
	 */
	public void setController(Object object, GraphicsController controller, int eventMask) {
		if (isAttached()) {
			helper.setController(object, controller, eventMask);
		}
	}

	/**
	 * Set the controller on an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param parent
	 *            the parent of the element on which the controller should be set.
	 * @param name
	 *            the name of the child element on which the controller should be set
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 * @param eventMask
	 *            a bitmask to specify which events to listen for {@link com.google.gwt.user.client.Event}
	 */
	public void setController(Object parent, String name, GraphicsController controller, int eventMask) {
		if (isAttached()) {
			helper.setController(parent, name, controller, eventMask);
		}
	}

	/**
	 * Set a specific cursor on an element of this <code>GraphicsContext</code>.
	 * 
	 * @param object
	 *            the element on which the controller should be set.
	 * @param cursor
	 *            The string representation of the cursor to use.
	 */
	public void setCursor(Object object, String cursor) {
		if (isAttached()) {
			helper.setCursor(object, cursor);
		}
	}

	/**
	 * Set a specific cursor on an element of this <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            the parent of the element on which the cursor should be set.
	 * @param name
	 *            the name of the child element on which the cursor should be set
	 * @param cursor
	 *            The string representation of the cursor to use.
	 */
	public void setCursor(Object parent, String name, String cursor) {
		if (isAttached()) {
			helper.setCursor(parent, name, cursor);
		}
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

		if (helper.getRootElement() != null) {
			applyElementSize(helper.getRootElement(), newWidth, newHeight, false);
			DOM.setStyleAttribute(helper.getRootElement(), "clip", "rect(0 " + newWidth + "px " + newHeight + "px 0)");
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
		if (isAttached()) {
			Element element = helper.getGroup(group);
			if (element != null) {
				DOM.setStyleAttribute(element, "visibility", "inherit");
			}
		}
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

	private boolean isAttached() {
		return parent != null && parent.isAttached();
	}

	/**
	 * Symbol definition data.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class SymbolDefinition extends SymbolInfo {

		private static final long serialVersionUID = 154L;

		private SymbolInfo symbol;

		private ShapeStyle style;

		public SymbolDefinition(SymbolInfo symbol, ShapeStyle style) {
			this.symbol = symbol;
			this.style = style;
		}

		public SymbolInfo getSymbol() {
			return symbol;
		}

		public ShapeStyle getStyle() {
			return style;
		}

	}

}