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

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.ImageInfo;
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
import org.geomajas.gwt.client.util.Dom;

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
		Dom.initVMLNamespace();

		// the root VML node
		Element rootNode = Dom.createElementNS(Dom.NS_HTML, "div");
		id = Dom.createUniqueId();
		rootNode.setId(id);
		Dom.setStyleAttribute(rootNode, "position", "absolute");
		Dom.setStyleAttribute(rootNode, "width", "100%");
		Dom.setStyleAttribute(rootNode, "height", "100%");
		Dom.setStyleAttribute(rootNode, "clip", "rect(0 " + width + "px " + height + "px 0)");
		Dom.setStyleAttribute(rootNode, "overflow", "hidden");
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
				Dom.setInnerHTML(group, data);
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
	 * @return HTML element which was drawn
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
			Dom.setElementAttribute(image, "src", Dom.makeUrlAbsolute(href));
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
				Dom.setElementAttribute(element, "path", VmlPathDecoder.decode(line));
				Dom.setStyleAttribute(element, "position", "absolute");
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
				Dom.setStyleAttribute(element, "position", "absolute");
				Dom.setElementAttribute(element, "fill-rule", "evenodd");
				Dom.setElementAttribute(element, "path", VmlPathDecoder.decode(polygon));
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
			if (symbol.getImage() != null) {
				// When it's an image symbol, add an extra definition for it's selection:
				SymbolInfo selected = new SymbolInfo();
				ImageInfo selectedImage = new ImageInfo();
				selectedImage.setHref(symbol.getImage().getSelectionHref());
				selectedImage.setWidth(symbol.getImage().getWidth());
				selectedImage.setHeight(symbol.getImage().getHeight());
				selected.setImage(selectedImage);
				symbolDefs.put(id + "-selection", new SymbolDefinition(selected, null));
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
	 *            The style to apply on the symbol. When the symbol is an image, the style will be ignored!
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
			} else if (symbol.getImage() != null) {
				// Creating an image; ignoring style....
				Element image = helper.createOrUpdateElement(parent, name, "image", null);
				Dom.setElementAttribute(image, "src", Dom.makeUrlAbsolute(symbol.getImage().getHref()));
				int width = symbol.getImage().getWidth();
				int height = symbol.getImage().getHeight();
				applyElementSize(image, width, height, false);
				applyAbsolutePosition(image, new Coordinate(position.getX() - Math.round(width / 2), position.getY()
						- Math.round(height / 2)));
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
			Element element = helper.createOrUpdateElement(parent, name, "shape", null);
			if (element != null) {
				// Set position, style and content:
				applyAbsolutePosition(element, position);
				Element textbox = helper.createOrUpdateSingleChild(element, "textbox");
				VmlStyleUtil.applyStyle(textbox, style);
				// hook to upper-left corner
				textbox.setPropertyString("inset", "0px, 0px, 0px, 0px");
				textbox.setInnerHTML(text);
				// Set width, because this may change otherwise...
				applyElementSize(element, getWidth(), getHeight(), false);
			}
		}
	}

	/**
	 * Return the (enclosing) group for the specified element id.
	 * 
	 * @param id group id
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
	 * @param id element id
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
				Dom.setStyleAttribute(element, "visibility", "hidden");
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
				Dom.setStyleAttribute(element, "visibility", "hidden");
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
			Dom.setStyleAttribute(helper.getRootElement(), "clip", "rect(0 " + newWidth + "px " + newHeight + "px 0)");
		} else {
			SC.logWarn("problems");
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
				Dom.setStyleAttribute(element, "visibility", "inherit");
			}
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
				Dom.setStyleAttribute(element, "visibility", "inherit");
			}
		}
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
	public void bringToFront(Object object, String name) {
		helper.bringToFront(object, name);
	}

	@Override
	public void moveToBack(Object object, String name) {
		helper.moveToBack(object, name);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Apply an absolute position on an element.
	 * 
	 * @param element
	 *            The element that needs an absolute position.
	 * @param position
	 *            The position as a Coordinate.
	 */
	private void applyAbsolutePosition(Element element, Coordinate position) {
		Dom.setStyleAttribute(element, "position", "absolute");
		Dom.setStyleAttribute(element, "left", (int) position.getX() + "px");
		Dom.setStyleAttribute(element, "top", (int) position.getY() + "px");
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
				Dom.setElementAttribute(element, "coordsize", width + " " + height);
			}
			Dom.setStyleAttribute(element, "width", width + "px");
			Dom.setStyleAttribute(element, "height", height + "px");
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