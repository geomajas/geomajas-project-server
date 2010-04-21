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
import java.util.IdentityHashMap;
import java.util.Map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.util.DOM;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * Helper class that provides a mapping between a DOM tree - consisting of groups (DIV, SVG or VML group) and elements
 * (HTML, SVG or VML) - and a set of Java objects. Shields the caller from id management by mapping object references to
 * unique document ids.
 * 
 * @see org.geomajas.gwt.client.gfx.GraphicsContext
 * 
 * @author Kristof Heirwegh
 * @author Jan De Moerloose
 */
public class DomHelper {

	/**
	 * Mapping between objects and DOM id's.
	 */
	private Map<Object, String> groupToId = new IdentityHashMap<Object, String>();

	/**
	 * Mapping between DOM id's and objects.
	 */
	private Map<String, Object> idToGroup = new HashMap<String, Object>();

	/**
	 * Mapping between objects and DOM id's.
	 */
	private Map<String, String> elementToName = new HashMap<String, String>();

	/**
	 * The root element.
	 */
	private Element rootElement;

	/**
	 * The name space.
	 */
	private Namespace namespace;

	/**
	 * Namespace enumeration.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public enum Namespace {
		HTML, VML, SVG
	};

	public DomHelper(Element rootElement, Namespace namespace) {
		this.rootElement = rootElement;
		this.namespace = namespace;
	}

	/**
	 * Create or update an element in the DOM. The id will be generated.
	 * 
	 * @param parent
	 *            the parent group
	 * @param name
	 *            the local group name of the element (should be unique within the group)
	 * @param type
	 *            the type of the element (tag name, e.g. 'image')
	 * @param style
	 *            The style to apply on the element.
	 * @return the created or updated element or null if creation failed
	 */
	public Element createOrUpdateElement(Object parent, String name, String type, Style style) {
		return createOrUpdateElement(parent, name, type, style, true);
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
	 * @param generateId
	 *            true if a unique id may be generated. If false, the name will be used as id and should therefore be
	 *            unique
	 * @return the created or updated element or null if creation failed or name was null
	 */
	public Element createOrUpdateElement(Object parent, String name, String type, Style style, boolean generateId) {
		Element element = null;
		// check existence
		if (name != null) {
			if (!generateId) {
				element = DOM.getElementById(name);
			} else {
				element = getElement(parent, name);
			}
		} else {
			return null;
		}
		if (element == null) {
			// Element was not found, so create it:
			element = createElement(parent, name, type, style, generateId);
		} else {
			// Element was found, so update it:
			applyStyle(element, style);
		}
		// no luck !
		if (element == null) {
			return null;
		}
		return element;
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
		createOrUpdateGroup(parent, object, null, null);
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
		switch (namespace) {
			case SVG:
				return createGroup(DOM.NS_SVG, parent, object, tagName);
			case VML:
				return createGroup(DOM.NS_VML, parent, object, tagName);
			case HTML:
			default:
				return createGroup(DOM.NS_HTML, parent, object, tagName);
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
		createOrUpdateGroup(parent, object, transformation, null);
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
		createOrUpdateGroup(parent, object, null, style);
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
		createOrUpdateGroup(parent, object, transformation, style);
	}

	/**
	 * Return the id of the specified group.
	 * 
	 * @param group
	 *            the group object
	 * @return the corresponding element id or null if the group has not been drawn.
	 */
	public String getId(Object group) {
		return groupToId.get(group);
	}

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context. A group is meant to group other elements
	 * together. Also this method gives you the opportunity to specify a specific width and height.
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
	public Element createOrUpdateGroup(Object parent, Object object, Matrix transformation, Style style) {
		switch (namespace) {
			case SVG:
				return createSvgGroup(parent, object, transformation, style);
			case VML:
				return createVmlGroup(parent, object, transformation, style);
			case HTML:
			default:
				return createHtmlGroup(parent, object, transformation, style);
		}
	}

	private Element createSvgGroup(Object parent, Object object, Matrix transformation, Style style) {
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

	private Element createVmlGroup(Object parent, Object object, Matrix transformation, Style style) {
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
			} else {
				applyAbsolutePosition(group, new Coordinate(0, 0));
			}
		}
		return group;
	}

	private Element createHtmlGroup(Object parent, Object object, Matrix transformation, Style style) {
		Element group = null;
		// check existence
		if (object != null) {
			group = getGroup(object);
		}
		// create if necessary
		if (group == null) {
			group = createGroup(DOM.NS_HTML, parent, object, "div");
		}
		// Apply width, height default 100%
		DOM.setStyleAttribute(group, "width", "100%");
		DOM.setStyleAttribute(group, "height", "100%");
		// Apply transformation on the element:
		if (transformation != null) {
			applyAbsolutePosition(group, new Coordinate(transformation.getDx(), transformation.getDy()));
		} else {
			applyAbsolutePosition(group, new Coordinate(0, 0));
		}
		// SVG style is just CSS, so ok for both
		if (style != null) {
			DOM.setElementAttribute(group, "style", decode(style));
		}
		return group;
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

	/**
	 * Set the controller on an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param object
	 *            the element on which the controller should be set.
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 */
	public void setController(Object object, GraphicsController controller) {
		// set them all
		doSetController(getGroup(object), controller, Event.MOUSEEVENTS | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);
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
		// set them all
		doSetController(getElement(parent, name), controller, Event.MOUSEEVENTS | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);
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
		doSetController(getGroup(object), controller, eventMask);
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
		doSetController(getElement(parent, name), controller, eventMask);
	}

	/**
	 * Set the controller on an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param element
	 *            the element on which the controller should be set
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 * @param eventMask
	 *            a bitmask to specify which events to listen for {@link com.google.gwt.user.client.Event}
	 */
	protected void doSetController(Element element, GraphicsController controller, int eventMask) {
		if (element != null) {
			DOM.setEventListener(element, new EventListenerHelper(element, controller, eventMask));
			DOM.sinkEvents(element, eventMask);
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
		if (object == null) {
			DOM.setStyleAttribute(getRootElement(), "cursor", cursor);
		} else {
			Element element = getGroup(object);
			if (element != null) {
				DOM.setStyleAttribute(element, "cursor", cursor);
			}
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
		Element element = getElement(parent, name);
		if (element != null) {
			DOM.setStyleAttribute(element, "cursor", cursor);
		}
	}

	// ----------------------------------------------------------

	/**
	 * Internal class to pass DOM-events to a GraphicsController
	 * 
	 * @author Kristof Heirwegh
	 */
	private class EventListenerHelper implements EventListener {

		private final Element e;

		private final HandlerManager hm;

		public EventListenerHelper(Element e, GraphicsController gc, int eventMask) {
			this.e = e;
			this.hm = new HandlerManager(e);
			if ((Event.ONMOUSEDOWN & eventMask) > 0) {
				hm.addHandler(MouseDownEvent.getType(), gc);
			}
			if ((Event.ONMOUSEUP & eventMask) > 0) {
				hm.addHandler(MouseUpEvent.getType(), gc);
			}
			if ((Event.ONMOUSEOUT & eventMask) > 0) {
				hm.addHandler(MouseOutEvent.getType(), gc);
			}
			if ((Event.ONMOUSEOVER & eventMask) > 0) {
				hm.addHandler(MouseOverEvent.getType(), gc);
			}
			if ((Event.ONMOUSEMOVE & eventMask) > 0) {
				hm.addHandler(MouseMoveEvent.getType(), gc);
			}
			if ((Event.ONMOUSEWHEEL & eventMask) > 0) {
				hm.addHandler(MouseWheelEvent.getType(), gc);
			}
			if ((Event.ONDBLCLICK & eventMask) > 0) {
				hm.addHandler(DoubleClickEvent.getType(), gc);
			}
		}

		@SuppressWarnings("deprecation")
		public void onBrowserEvent(Event event) {
			// copied from Widget class to mimic behaviour of other widgets
			switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEOVER:
					// Only fire the mouse over event if it's coming from outside this
					// widget.
				case Event.ONMOUSEOUT:
					// Only fire the mouse out event if it's leaving this
					// widget.
					com.google.gwt.dom.client.Element related = event.getRelatedTarget();
					if (related != null && e.isOrHasChild(related)) {
						return;
					}
					break;
			}

			DomEvent.fireNativeEvent(event, new HasHandlers() {

				public void fireEvent(GwtEvent<?> event) {
					hm.fireEvent(event);
				}
			}, e);
		}
	}

	/**
	 * Returns the root element of this context.
	 * 
	 * @return the root element
	 */
	public Element getRootElement() {
		return rootElement;
	}

	/**
	 * Returns the group that corresponds with this group object.
	 * 
	 * @param object
	 * @return the group or null if it does not exist
	 */
	public Element getGroup(Object object) {
		return DOM.getElementById(groupToId.get(object));
	}

	/**
	 * Return the (enclosing) group for the specified element id.
	 * 
	 * @param id
	 * @return the group object
	 */
	public Object getGroupById(String id) {
		String name = elementToName.get(id);
		if (name != null) {
			return idToGroup.get(DOM.disAssembleId(id, name));
		} else {
			return null;
		}
	}

	/**
	 * Return the element name for the specified id.
	 * 
	 * @param id
	 * @return the name of the element
	 */
	public String getNameById(String id) {
		return elementToName.get(id);
	}

	/**
	 * Generic creation method for group elements.
	 * 
	 * @param namespace
	 *            the name space (HTML, SVG or VML,...)
	 * @param parent
	 *            the parent group
	 * @return the newly created group element or null if creation failed
	 */
	protected Element createGroup(String namespace, Object parent, Object group, String type) {
		Element parentElement = null;
		if (parent == null) {
			parentElement = getRootElement();
		} else {
			parentElement = getGroup(parent);
		}
		if (parentElement == null) {
			return null;
		} else {
			Element element = null;
			if (namespace == DOM.NS_HTML) {
				element = DOM.createElement("div");
			} else {
				element = DOM.createElementNS(namespace, type);
			}
			parentElement.appendChild(element);
			String id = DOM.createUniqueId();
			if (group instanceof PaintableGroup) {
				id = DOM.assembleId(id, ((PaintableGroup) group).getGroupName());
			}
			groupToId.put(group, id);
			idToGroup.put(id, group);
			DOM.setElementAttribute(element, "id", id);
			return element;
		}
	}

	/**
	 * Returns the element that is a child of the specified parent and has the specified local group name.
	 * 
	 * @param parent
	 *            the parent group
	 * @param name
	 *            the local group name
	 * @return the element or null if it does not exist or the name was null
	 */
	protected Element getElement(Object parent, String name) {
		if (name == null) {
			return null;
		}
		String id = null;
		if (parent == null) {
			id = getRootElement().getId();
		} else {
			id = groupToId.get(parent);
		}
		return DOM.getElementById(DOM.assembleId(id, name));
	}

	/**
	 * Generic creation method for non-group elements.
	 * 
	 * @param parent
	 *            the parent group
	 * @param name
	 *            local group name of the element (should be unique within the group)
	 * @param type
	 *            the type of the element (tag name, e.g. 'image')
	 * @param style
	 *            The style to apply on the element.
	 * @param generateId
	 *            true if a unique id may be generated, otherwise the name will be used as id
	 * @return the newly created element or null if creation failed or the name was null
	 */
	protected Element createElement(Object parent, String name, String type, Style style, boolean generateId) {
		if (name == null) {
			return null;
		}
		Element parentElement = null;
		if (parent == null) {
			parentElement = getRootElement();
		} else {
			parentElement = getGroup(parent);
		}
		if (parentElement == null) {
			return null;
		} else {
			Element element = null;
			switch (namespace) {
				case SVG:
					element = DOM.createElementNS(DOM.NS_SVG, type);
					if (style != null) {
						DOM.setElementAttribute(element, "style", decode(style));
					}
					break;
				case VML:
					element = DOM.createElementNS(DOM.NS_VML, type);
					Element stroke = DOM.createElementNS(DOM.NS_VML, "stroke");
					element.appendChild(stroke);
					Element fill = DOM.createElementNS(DOM.NS_VML, "fill");
					element.appendChild(fill);
					if ("shape".equals(name)) {
						// Set the size .....if the parent has a coordsize defined, take it over:
						String coordsize = parentElement.getAttribute("coordsize");
						if (coordsize != null && coordsize.length() > 0) {
							element.setAttribute("coordsize", coordsize);
						}
					}
					DOM.setStyleAttribute(element, "position", "absolute");
					VmlStyleUtil.applyStyle(element, style);
					break;
				case HTML:
				default:
					element = DOM.createElementNS(DOM.NS_HTML, type);
					if (style != null) {
						DOM.setElementAttribute(element, "style", decode(style));
					}
			}
			parentElement.appendChild(element);
			if (name != null) {
				String id = generateId ? DOM.assembleId(parentElement.getId(), name) : name;
				elementToName.put(id, name);
				DOM.setElementAttribute(element, "id", id);
			}
			return element;
		}
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
		Element element = getElement(parent, name);
		if (element != null) {
			Element group = (Element) element.getParentElement();
			if (group != null) {
				try {
					DOM.removeChild(group, element);
					elementToName.remove(element.getId());
				} catch (Exception e) {

				}
			}
		}
	}

	/**
	 * Delete this group from the graphics DOM structure.
	 * 
	 * @param object
	 *            The group's object.
	 */
	public void deleteGroup(Object object) {
		Element element = getGroup(object);
		if (element != null) {
			Element parent = (Element) element.getParentElement();
			if (parent != null) {
				try {
					DOM.removeChild(parent, element);
					groupToId.remove(object);
				} catch (Exception e) {
					// do something...
				}
			}
		}
	}

	public void applyStyle(Element element, Style style) {
		if (element != null && style != null) {
			switch (namespace) {
				case VML:
					VmlStyleUtil.applyStyle(element, style);
					break;
				case SVG:
				case HTML:
					DOM.setElementAttribute(element, "style", decode(style));
			}
		}
	}

	/**
	 * Return the CSS equivalent of the Style object.
	 * 
	 * @param style
	 * @return
	 */
	public String decode(Style style) {
		if (style != null) {
			if (style instanceof ShapeStyle) {
				return decode((ShapeStyle) style);
			} else if (style instanceof FontStyle) {
				return decode((FontStyle) style);
			} else if (style instanceof PictureStyle) {
				return decode((PictureStyle) style);
			}
		}
		return "";
	}

	// -------------------------------------------------------------------------
	// Private decode methods for each Style class:
	// -------------------------------------------------------------------------

	private String decode(ShapeStyle style) {
		String css = "";
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			css += "fill:" + style.getFillColor() + ";";
		}
		css += "fill-opacity:" + style.getFillOpacity() + ";";
		if (style.getStrokeColor() != null && !"".equals(style.getStrokeColor())) {
			css += "stroke:" + style.getStrokeColor() + ";";
		}
		css += "stroke-opacity:" + style.getStrokeOpacity() + ";";
		if (style.getStrokeWidth() >= 0) {
			css += "stroke-width:" + style.getStrokeWidth() + ";";
		}
		return css;
	}

	private String decode(FontStyle style) {
		String css = "";
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			css += "fill:" + style.getFillColor() + ";";
		}
		if (style.getFontFamily() != null && !"".equals(style.getFontFamily())) {
			css += "font-family:" + style.getFontFamily() + ";";
		}
		if (style.getFontStyle() != null && !"".equals(style.getFontStyle())) {
			css += "font-style:" + style.getFontStyle() + ";";
		}
		if (style.getFontWeight() != null && !"".equals(style.getFontWeight())) {
			css += "font-weight:" + style.getFontWeight() + ";";
		}
		if (style.getFontSize() >= 0) {
			css += "stroke-width:" + style.getFontSize() + ";";
		}
		return css;
	}

	private String decode(PictureStyle style) {
		return "opacity:" + style.getOpacity() + ";";
	}

}
