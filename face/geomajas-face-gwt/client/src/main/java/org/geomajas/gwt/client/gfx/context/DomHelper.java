/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt.client.util.Log;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
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
	}

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
		Element element;
		// check existence
		if (name != null) {
			if (!generateId) {
				element = Dom.getElementById(name);
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
	 * @return element for the group
	 */
	public Element drawGroup(Object parent, Object object, String tagName) {
		switch (namespace) {
			case SVG:
				return createGroup(Dom.NS_SVG, parent, object, tagName);
			case VML:
				return createGroup(Dom.NS_VML, parent, object, tagName);
			case HTML:
			default:
				return createGroup(Dom.NS_HTML, parent, object, tagName);
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
	 * Move an element from on group to another. The elements name will remain the same.
	 * 
	 * @param name
	 *            The name of the element within the sourceParent group.
	 * @param sourceParent
	 *            The original parent group of the element.
	 * @param targetParent
	 *            The target parent group for the element.
	 * @return true when move was successful
	 */
	public boolean moveElement(String name, Object sourceParent, Object targetParent) {
		Element sourceGroup = null;
		Element targetGroup = null;
		Element element = null;
		if (sourceParent != null) {
			sourceGroup = getGroup(sourceParent);
			element = getElement(sourceParent, name);
		}
		if (targetParent != null) {
			targetGroup = getGroup(targetParent);
		}
		if (sourceGroup == null || targetGroup == null) {
			return false;
		}
		if (Dom.isOrHasChild(sourceGroup, element)) {
			Dom.removeChild(sourceGroup, element);
			String newId = Dom.assembleId(targetGroup.getId(), name);
			elementToName.remove(element.getId());
			elementToName.put(newId, name);
			Dom.setElementAttribute(element, "id", newId);
			Dom.appendChild(targetGroup, element);
			return true;
		}
		return false;
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
				return createVmlGroup(parent, object, transformation);
			case HTML:
			default:
				return createHtmlGroup(parent, object, transformation, style);
		}
	}

	/**
	 * Within a certain group, bring an element to the front. This will make sure it's visible (within that group).
	 * 
	 * @param object
	 *            The group wherein to search for the element.
	 * @param name
	 *            The name of the element to bring to the front.
	 * @since 1.10.0
	 */
	public void bringToFront(Object parent, String name) {
		Element parentElement = getGroup(parent);
		if (parentElement == null) {
			throw new IllegalArgumentException("bringToFront failed: could not find parent group.");
		}

		Element element = getElement(parent, name);
		if (element == null) {
			throw new IllegalArgumentException("bringToFront failed: could not find element within group.");
		}

		parentElement.removeChild(element);
		parentElement.appendChild(element);
	}

	/**
	 * Within a certain group, move an element to the back. All siblings will be rendered after this one.
	 * 
	 * @param object
	 *            The group wherein to search for the element.
	 * @param name
	 *            The name of the element to move to the back.
	 * @since 1.10.0
	 */
	public void moveToBack(Object parent, String name) {
		Element parentElement = getGroup(parent);
		if (parentElement == null) {
			throw new IllegalArgumentException("moveToBack failed: could not find parent group.");
		}

		Element element = getElement(parent, name);
		if (element == null) {
			throw new IllegalArgumentException("moveToBack failed: could not find element within group.");
		}

		if (parentElement.getFirstChildElement() != element) {
			parentElement.insertFirst(element);
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
			group = createGroup(Dom.NS_SVG, parent, object, "g");
		}
		// Apply transformation on the element:
		if (transformation != null) {
			Dom.setElementAttribute(group, "transform", parse(transformation));
		}
		// SVG style is just CSS, so ok for both
		if (style != null) {
			applyStyle(group, style);
		}
		return group;
	}

	private Element createVmlGroup(Object parent, Object object, Matrix transformation) {
		Element group = null;
		// check existence
		if (object != null) {
			group = getGroup(object);
		}
		// create if necessary
		if (group == null) {
			group = createGroup(Dom.NS_VML, parent, object, "group");
		}

		if (group != null) {
			// Get the parent element:
			Element parentElement;
			if (parent == null) {
				parentElement = getRootElement();
			} else {
				parentElement = getGroup(parent);
			}

			// Inherit size from parent if not specified
			if (parentElement != null) {
				String width = Dom.getStyleAttribute(parentElement, "width");
				String height = Dom.getStyleAttribute(parentElement, "height");
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
			group = createGroup(Dom.NS_HTML, parent, object, "div");
		}
		// Apply width, height default 100%
		Dom.setStyleAttribute(group, "width", "100%");
		Dom.setStyleAttribute(group, "height", "100%");
		// Apply transformation on the element:
		if (transformation != null) {
			applyAbsolutePosition(group, new Coordinate(transformation.getDx(), transformation.getDy()));
		} else {
			applyAbsolutePosition(group, new Coordinate(0, 0));
		}
		// SVG style is just CSS, so ok for both
		if (style != null) {
			applyStyle(group, style);
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
		doSetController(getElement(parent, name), controller, Event.TOUCHEVENTS | Event.MOUSEEVENTS | Event.ONDBLCLICK
				| Event.ONMOUSEWHEEL);
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
			// int offsetX = element.getAbsoluteLeft() - rootElement.getParentElement().getAbsoluteLeft();
			// int offsetY = element.getAbsoluteTop() - rootElement.getParentElement().getAbsoluteTop();
			// controller.setOffsetX(offsetX);
			// controller.setOffsetY(offsetY);
			Dom.setEventListener(element, new EventListenerHelper(element, controller, eventMask));
			Dom.sinkEvents(element, eventMask);
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
			Dom.setStyleAttribute(getRootElement(), "cursor", cursor);
		} else {
			Element element = getGroup(object);
			if (element != null) {
				Dom.setStyleAttribute(element, "cursor", cursor);
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
			Dom.setStyleAttribute(element, "cursor", cursor);
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
			if ((Event.ONTOUCHSTART & eventMask) > 0) {
				hm.addHandler(TouchStartEvent.getType(), gc);
			}
			if ((Event.ONTOUCHEND & eventMask) > 0) {
				hm.addHandler(TouchEndEvent.getType(), gc);
			}
			if ((Event.ONTOUCHMOVE & eventMask) > 0) {
				hm.addHandler(TouchMoveEvent.getType(), gc);
			}
			if ((Event.ONTOUCHCANCEL & eventMask) > 0) {
				hm.addHandler(TouchCancelEvent.getType(), gc);
			}
		}

		@SuppressWarnings("deprecation")
		public void onBrowserEvent(Event event) {
			// copied from Widget class to mimic behaviour of other widgets
			switch (Dom.eventGetType(event)) {
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
	 *            group object
	 * @return the group or null if it does not exist
	 */
	public Element getGroup(Object object) {
		return Dom.getElementById(groupToId.get(object));
	}

	/**
	 * Return the (enclosing) group for the specified element id.
	 * 
	 * @param id
	 *            element id
	 * @return the group object
	 */
	public Object getGroupById(String id) {
		String name = elementToName.get(id);
		if (name != null) {
			return idToGroup.get(Dom.disAssembleId(id, name));
		} else {
			return null;
		}
	}

	/**
	 * Return the element name for the specified id.
	 * 
	 * @param id
	 *            element id
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
	 * @param group
	 *            group
	 * @param type
	 *            type
	 * @return the newly created group element or null if creation failed
	 */
	protected Element createGroup(String namespace, Object parent, Object group, String type) {
		Element parentElement;
		if (parent == null) {
			parentElement = getRootElement();
		} else {
			parentElement = getGroup(parent);
		}
		if (parentElement == null) {
			return null;
		} else {
			Element element;
			String id = Dom.createUniqueId();
			if (group instanceof PaintableGroup) {
				id = Dom.assembleId(id, ((PaintableGroup) group).getGroupName());
			}
			groupToId.put(group, id);
			idToGroup.put(id, group);
			if (Dom.NS_HTML.equals(namespace)) {
				element = Dom.createElement("div", id);
			} else {
				element = Dom.createElementNS(namespace, type, id);
			}
			parentElement.appendChild(element);
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
		String id;
		if (parent == null) {
			id = getRootElement().getId();
		} else {
			id = groupToId.get(parent);
		}
		return Dom.getElementById(Dom.assembleId(id, name));
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
		if (null == name) {
			return null;
		}
		Element parentElement;
		if (parent == null) {
			parentElement = getRootElement();
		} else {
			parentElement = getGroup(parent);
		}
		if (parentElement == null) {
			return null;
		} else {
			Element element;
			String id = generateId ? Dom.assembleId(parentElement.getId(), name) : name;
			elementToName.put(id, name);
			switch (namespace) {
				case SVG:
					element = Dom.createElementNS(Dom.NS_SVG, type, id);
					if (style != null) {
						applyStyle(element, style);
					}
					break;
				case VML:
					element = Dom.createElementNS(Dom.NS_VML, type, id);
					Element stroke = Dom.createElementNS(Dom.NS_VML, "stroke");
					element.appendChild(stroke);
					Element fill = Dom.createElementNS(Dom.NS_VML, "fill");
					element.appendChild(fill);
					if ("shape".equals(name)) {
						// Set the size .....if the parent has a coordsize defined, take it over:
						String coordsize = parentElement.getAttribute("coordsize");
						if (coordsize != null && coordsize.length() > 0) {
							element.setAttribute("coordsize", coordsize);
						}
					}
					Dom.setStyleAttribute(element, "position", "absolute");
					VmlStyleUtil.applyStyle(element, style);
					break;
				case HTML:
				default:
					element = Dom.createElementNS(Dom.NS_HTML, type, id);
					if (style != null) {
						applyStyle(element, style);
					}
			}
			parentElement.appendChild(element);
			Dom.setElementAttribute(element, "id", id);
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
				deleteRecursively(group, element);
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
				deleteRecursively(parent, element);
			}
		}
	}

	private void deleteRecursively(Element parent, Element element) {
		for (int i = element.getChildCount() - 1; i >= 0; i--) {
			Node node = element.getChild(i);
			if (node instanceof Element) {
				deleteRecursively(element, (Element) node);
			}
		}
		try {
			groupToId.remove(element);
			if (element.getId() != null) {
				Log.logDebug("Removing element " + element.getId());
				elementToName.remove(element.getId());
			}
			Dom.setEventListener(element, null);
			Dom.removeChild(parent, element);
		} catch (Exception e) {
			Log.logError("Problem during recursive delete of " + element.getId() + " from " + parent.getId() + ", " +
					e.getMessage());
		}
	}

	/**
	 * Apply the style.
	 * 
	 * @param element
	 *            DOM element
	 * @param style
	 *            style
	 */
	public void applyStyle(Element element, Style style) {
		if (element != null && style != null) {
			switch (namespace) {
				case VML:
					VmlStyleUtil.applyStyle(element, style);
					break;
				case SVG:
					if (style instanceof ShapeStyle) {
						applySvgStyle(element, (ShapeStyle) style);
					} else if (style instanceof FontStyle) {
						applySvgStyle(element, (FontStyle) style);
					} else if (style instanceof PictureStyle) {
						applySvgStyle(element, (PictureStyle) style);
					}
					break;
				case HTML:
					if (style instanceof ShapeStyle) {
						applyHtmlStyle(element, (ShapeStyle) style);
					} else if (style instanceof FontStyle) {
						applyHtmlStyle(element, (FontStyle) style);
					} else if (style instanceof PictureStyle) {
						applyHtmlStyle(element, (PictureStyle) style);
					}
					break;
			}
		}
	}

	// -------------------------------------------------------------------------
	// Private decode methods for each Style class:
	// -------------------------------------------------------------------------

	private void applyHtmlStyle(Element element, ShapeStyle style) {
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			Dom.setStyleAttribute(element, "fillColor", style.getFillColor());
		}
		Dom.setStyleAttribute(element, "fillOpacity", Float.toString(style.getFillOpacity()));
		if (style.getStrokeColor() != null && !"".equals(style.getStrokeColor())) {
			Dom.setStyleAttribute(element, "stroke", style.getStrokeColor());
		}
		Dom.setStyleAttribute(element, "strokeOpacity", Float.toString(style.getStrokeOpacity()));
		if (style.getStrokeWidth() >= 0) {
			Dom.setStyleAttribute(element, "strokeWidth", Float.toString(style.getStrokeWidth()));
		}
	}

	private void applyHtmlStyle(Element element, FontStyle style) {
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			Dom.setStyleAttribute(element, "color", style.getFillColor());
		}
		if (style.getFontFamily() != null && !"".equals(style.getFontFamily())) {
			Dom.setStyleAttribute(element, "fontFamily", style.getFontFamily());
		}
		if (style.getFontStyle() != null && !"".equals(style.getFontStyle())) {
			Dom.setStyleAttribute(element, "fontStyle", style.getFontStyle());
		}
		if (style.getFontWeight() != null && !"".equals(style.getFontWeight())) {
			Dom.setStyleAttribute(element, "fontWeight", style.getFontWeight());
		}
	}

	private void applyHtmlStyle(Element element, PictureStyle style) {
		double opacity = style.getOpacity();
		if (opacity >= 0.0 && opacity < 1.0) {
			Dom.setStyleAttribute(element, "filter", "alpha(opacity=" + Double.toString((opacity * 100)) + ")");
			Dom.setStyleAttribute(element, "opacity", Double.toString(opacity));
		} else if (opacity == 1.0) {
			Dom.setStyleAttribute(element, "filter", "");
			Dom.setStyleAttribute(element, "opacity", "");
		}
		if (style.getDisplay() != null) {
			Dom.setStyleAttribute(element, "display", style.getDisplay());
		}
	}

	private void applySvgStyle(Element element, ShapeStyle style) {
		StringBuilder css = new StringBuilder();
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			css.append("fill:");
			css.append(style.getFillColor());
			css.append(";");
		}
		css.append("fill-opacity:");
		css.append(style.getFillOpacity());
		css.append(";");
		if (style.getStrokeColor() != null && !"".equals(style.getStrokeColor())) {
			css.append("stroke:");
			css.append(style.getStrokeColor());
			css.append(";");
		}
		css.append("stroke-opacity:");
		css.append(style.getStrokeOpacity());
		css.append(";");
		if (style.getStrokeWidth() >= 0) {
			css.append("stroke-width:");
			css.append(style.getStrokeWidth());
			css.append(";");
		}
		Dom.setElementAttribute(element, "style", css.toString());
	}

	private void applySvgStyle(Element element, FontStyle style) {
		StringBuilder css = new StringBuilder();
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			css.append("fill:");
			css.append(style.getFillColor());
			css.append(";");
		}
		if (style.getFontFamily() != null && !"".equals(style.getFontFamily())) {
			css.append("font-family:");
			css.append(style.getFontFamily());
			css.append(";");
		}
		if (style.getFontStyle() != null && !"".equals(style.getFontStyle())) {
			css.append("font-style:");
			css.append(style.getFontStyle());
			css.append(";");
		}
		if (style.getFontWeight() != null && !"".equals(style.getFontWeight())) {
			css.append("font-weight:");
			css.append(style.getFontWeight());
			css.append(";");
		}
		if (style.getFontSize() >= 0) {
			css.append("stroke-width:");
			css.append(style.getFontSize());
			css.append(";");
		}
		Dom.setElementAttribute(element, "style", css.toString());
	}

	private void applySvgStyle(Element element, PictureStyle style) {
		StringBuilder css = new StringBuilder();
		css.append("opacity:");
		css.append(style.getOpacity());
		css.append(";");
		if (style.getDisplay() != null) {
			css.append("display:");
			css.append(style.getDisplay());
		}
		Dom.setElementAttribute(element, "style", css.toString());
	}

}
