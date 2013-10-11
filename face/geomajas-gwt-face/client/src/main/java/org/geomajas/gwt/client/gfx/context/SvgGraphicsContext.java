/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.gfx.context;

import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.context.DomHelper.Namespace;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.Dom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Implementation of the GraphicsContext interface using the SVG language. This class is used in all browsers except
 * Internet Explorer.
 * 
 * @see org.geomajas.gwt.client.gfx.context.VmlGraphicsContext
 * 
 * @author Pieter De Graef
 */
public class SvgGraphicsContext implements GraphicsContext {

	private Composite defsGroup;

	private Element defs;

	private int width;

	private int height;

	private String id;

	private DomHelper helper;

	private Widget parent;

	/**
	 * Constructs . It will create the initial DOM structure setup.
	 * 
	 * @param parent
	 *            The parent element, onto whom to attach the initial DOM structure.
	 */
	public SvgGraphicsContext(Widget parent) {
		this.parent = parent;
		// the root SVG node
		Element rootNode = Dom.createElementNS(Dom.NS_SVG, "svg");
		// needed for IE9
		rootNode.getStyle().setOverflow(Overflow.HIDDEN);
		String sWidth = Integer.toString(width);
		String sHeight = Integer.toString(height);
		Dom.setElementAttribute(rootNode, "width", sWidth);
		Dom.setElementAttribute(rootNode, "height", sHeight);
		Dom.setElementAttribute(rootNode, "viewBox", "0 0 " + sWidth + " " + sHeight);
		Dom.setElementAttribute(rootNode, "xml:base", GWT.getHostPageBaseURL());
		helper = new DomHelper(rootNode, Namespace.SVG);

		// Point style definitions:
		defsGroup = new Composite("style_defs");
		defs = helper.drawGroup(null, defsGroup, "defs");

		// Append to parent: we need a top div or the svg is blocked by any peer div !!!
		Element divNode = Dom.createElementNS(Dom.NS_HTML, "div");
		Dom.setStyleAttribute(divNode, "position", "absolute");
		Dom.setStyleAttribute(divNode, "width", "100%");
		Dom.setStyleAttribute(divNode, "height", "100%");
		id = Dom.createUniqueId();
		divNode.setId(id);

		parent.getElement().appendChild(divNode);
		divNode.appendChild(rootNode);
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
			Element circle = helper.createOrUpdateElement(parent, name, "circle", style);
			Dom.setElementAttribute(circle, "cx", Integer.toString((int) position.getX()));
			Dom.setElementAttribute(circle, "cy", Integer.toString((int) position.getY()));
			Dom.setElementAttribute(circle, "r", Integer.toString((int) radius));
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
	 *            SVG fragment
	 * @param transformation
	 *            transformation to apply to the group
	 */
	public void drawData(Object parent, Object object, String data, Matrix transformation) {
		if (isAttached()) {
			Element group = helper.getGroup(object);
			if (group == null) {
				group = helper.createOrUpdateGroup(parent, object, transformation, null);
				Dom.setInnerSvg(group, data);
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
	 * @return element which was drawn
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
			Dom.setElementAttribute(image, "x", Integer.toString((int) bounds.getX()));
			Dom.setElementAttribute(image, "y", Integer.toString((int) bounds.getY()));
			Dom.setElementAttribute(image, "width", Integer.toString((int) bounds.getWidth()));
			Dom.setElementAttribute(image, "height", Integer.toString((int) bounds.getHeight()));
			Dom.setElementAttributeNS(Dom.NS_XLINK, image, "xlink:href", Dom.makeUrlAbsolute(href));
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
			Element element = helper.createOrUpdateElement(parent, name, "path", style);
			if (line != null) {
				Dom.setElementAttribute(element, "d", SvgPathDecoder.decode(line));
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
			Element element = helper.createOrUpdateElement(parent, name, "path", style);
			if (polygon != null) {
				Dom.setElementAttribute(element, "d", SvgPathDecoder.decode(polygon));
				Dom.setElementAttribute(element, "fill-rule", "evenodd");
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
			Dom.setElementAttribute(element, "x", Double.toString(rectangle.getX()));
			Dom.setElementAttribute(element, "y", Double.toString(rectangle.getY()));
			Dom.setElementAttribute(element, "width", Double.toString(rectangle.getWidth()));
			Dom.setElementAttribute(element, "height", Double.toString(rectangle.getHeight()));
		}
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
	public void drawSymbolDefinition(Object parent, String id, SymbolInfo symbol, ShapeStyle style,
			Matrix transformation) {
		if (isAttached()) {
			if (symbol == null) {
				return;
			}

			// Step1: get or create the symbol element:
			// check existence
			Element def = Dom.getElementById(id);
			boolean isNew = (def == null);
			// create or update
			def = helper.createOrUpdateElement(defsGroup, id, "symbol", null, false);
			Dom.setElementAttribute(def, "overflow", "visible");

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
				node = Dom.createElementNS(Dom.NS_SVG, "rect");
				Dom.setElementAttribute(node, "width", Long.toString(width));
				Dom.setElementAttribute(node, "height", Long.toString(height));
				Dom.setElementAttribute(node, "x", Long.toString(-Math.round(width / 2)));
				Dom.setElementAttribute(node, "y", Long.toString(-Math.round(height / 2)));
			} else if (symbol.getCircle() != null) {
				// Create the circle symbol:
				long radius = (long) symbol.getCircle().getR();
				if (transformation != null && transformation.getXx() != 0) {
					double scale = transformation.getXx();
					radius = Math.round(radius / scale);
				}
				node = Dom.createElementNS(Dom.NS_SVG, "circle");
				Dom.setElementAttribute(node, "cx", "0");
				Dom.setElementAttribute(node, "cy", "0");
				Dom.setElementAttribute(node, "r", Long.toString(radius));
			} else if (symbol.getImage() != null) {
				// Create the image symbol:
				node = Dom.createElementNS(Dom.NS_SVG, "image");

				Dom.setElementAttributeNS(Dom.NS_XLINK, node, "xlink:href",
						Dom.makeUrlAbsolute(symbol.getImage().getHref()));

				long width = (long) symbol.getImage().getWidth();
				long height = (long) symbol.getImage().getHeight();
				if (transformation != null && transformation.getXx() != 0) {
					double scale = transformation.getXx();
					width = Math.round(width / scale);
					height = Math.round(height / scale);
				}
				Dom.setElementAttribute(node, "width", Long.toString(width));
				Dom.setElementAttribute(node, "height", Long.toString(height));
				Dom.setElementAttribute(node, "x", Long.toString(-Math.round(width / 2)));
				Dom.setElementAttribute(node, "y", Long.toString(-Math.round(height / 2)));

				if (isNew) {
					Element node2 = Dom.createElementNS(Dom.NS_SVG, "image");
					Dom.setElementAttributeNS(Dom.NS_XLINK, node2, "xlink:href",
							Dom.makeUrlAbsolute(symbol.getImage().getSelectionHref()));
					Dom.setElementAttribute(node2, "width", Long.toString(width));
					Dom.setElementAttribute(node2, "height", Long.toString(height));
					Dom.setElementAttribute(node2, "x", Long.toString(-Math.round(width / 2)));
					Dom.setElementAttribute(node2, "y", Long.toString(-Math.round(height / 2)));

					Element def2 = helper.createOrUpdateElement(defsGroup, id + "-selection", "symbol", null, false);
					Dom.setElementAttribute(def2, "overflow", "visible");
					def2.appendChild(node2);
					defs.appendChild(def2);
				}
			}

			// Step3: Append the symbol definition:
			if (isNew) {
				def.appendChild(node);
				defs.appendChild(def);
			} else {
				while (def.hasChildNodes()) {
					Dom.removeChild(def, (com.google.gwt.user.client.Element) def.getFirstChildElement());
				}
				def.appendChild(node);
			}
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
			Element useElement = helper.createOrUpdateElement(parent, name, "use", style);
			Dom.setElementAttributeNS(Dom.NS_XLINK, useElement, "xlink:href", "#" + shapeTypeId);
			if (position != null) {
				Dom.setElementAttribute(useElement, "x", Double.toString(position.getX()));
				Dom.setElementAttribute(useElement, "y", Double.toString(position.getY()));
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
			Element element = helper.createOrUpdateElement(parent, name, "text", style);
			if (text != null) {
				element.setInnerText(text);
			}

			if (position != null) {
				int fontSize = 12;
				if (style != null) {
					fontSize = style.getFontSize();
				}
				Dom.setElementAttribute(element, "x", Double.toString(position.getX()));
				Dom.setElementAttribute(element, "y", Double.toString(position.getY() + fontSize));
			}
		}
	}

	/**
	 * Return the (enclosing) group for the specified element id.
	 * 
	 * @param id
	 *            group id
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
	 * @param group
	 *            the group object
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
	 *            element id
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
	 * Hide the specified element in the specified group. If the element does not exist, nothing will happen.
	 * 
	 * @param group
	 *            The group object.
	 * @param name
	 *            The element name.
	 */
	public void hide(Object group, String name) {
		if (isAttached()) {
			Element element = helper.getElement(group, name);
			if (element != null) {
				Dom.setElementAttribute(element, "display", "none");
			}
		}
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
				Dom.setElementAttribute(element, "display", "none");
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
			String sWidth = Integer.toString(newWidth);
			String sHeight = Integer.toString(newHeight);
			Dom.setElementAttribute(helper.getRootElement(), "width", sWidth);
			Dom.setElementAttribute(helper.getRootElement(), "height", sHeight);
			Dom.setElementAttribute(helper.getRootElement(), "viewBox", "0 0 " + sWidth + " " + sHeight);
		}
	}

	/**
	 * Show the specified element in the specified group. If the element does not exist, nothing will happen.
	 * 
	 * @param group
	 *            The group object.
	 * @param name
	 *            The element name.
	 */
	public void unhide(Object group, String name) {
		if (isAttached()) {
			Element element = helper.getElement(group, name);
			if (element != null) {
				Dom.setElementAttribute(element, "display", "inline");
			}
		}
	}

	/**
	 * Show the specified group. If the group does not exist, nothing will happen.
	 * 
	 * @param group
	 *            The group object.
	 */
	public void unhide(Object group) {
		if (isAttached()) {
			Element element = helper.getGroup(group);
			if (element != null) {
				Dom.setElementAttribute(element, "display", "inline");
			}
		}
	}

	private boolean isAttached() {
		return parent != null && parent.isAttached();
	}

	/**
	 * Move an element from on group to another. The elements name will remain the same.
	 * 
	 * @param name
	 *            The name of the element within the sourceParent group.
	 * @param sourceParent
	 *            The original parent object associated with the element.
	 * @param targetParent
	 *            The target parent object to be associated with the element.
	 * @since 1.8.0
	 */
	public void moveElement(String name, Object sourceParent, Object targetParent) {
		helper.moveElement(name, sourceParent, targetParent);
	}

	@Override
	public void bringToFront(final Object object, final String name) {
		helper.bringToFront(object, name);
	}

	@Override
	public void moveToBack(Object object, String name) {
		helper.moveToBack(object, name);
	}
}