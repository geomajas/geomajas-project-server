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

import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.ImageContext;
import org.geomajas.gwt.client.gfx.context.DomHelper.Namespace;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.util.Dom;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * An HTML based image context.
 * 
 * @author Jan De Moerloose
 */
public class DefaultImageContext implements ImageContext {

	private DomHelper helper;

	private Widget parent;

	/**
	 * Constructs an image context. The context will be appended to the specified parent widget.
	 * 
	 * @param parent
	 *            The parent element, onto whom to attach the initial DOM structure.
	 */
	public DefaultImageContext(Widget parent) {
		this.parent = parent;
		// root node
		Element rootNode = Dom.createElementNS(Dom.NS_HTML, "div");
		String id = Dom.createUniqueId();
		rootNode.setId(id);
		Dom.setStyleAttribute(rootNode, "position", "absolute");
		Dom.setStyleAttribute(rootNode, "width", "100%");
		Dom.setStyleAttribute(rootNode, "height", "100%");
		// prevents overflowing of images in IE
		Dom.setStyleAttribute(rootNode, "overflow", "hidden");
		helper = new DomHelper(rootNode, Namespace.HTML);
		// Append to parent:
		parent.getElement().appendChild(rootNode);
	}

	public void deleteElement(Object parent, String name) {
		if (isAttached()) {
			helper.deleteElement(parent, name);
		}
	}

	public void deleteGroup(Object object) {
		if (isAttached()) {
			helper.deleteGroup(object);
		}
	}

	public void drawGroup(Object parent, Object object, Matrix transformation, Style style) {
		if (isAttached()) {
			helper.drawGroup(parent, object, transformation, style);
		}
	}

	public void drawGroup(Object parent, Object object, Matrix transformation) {
		if (isAttached()) {
			helper.drawGroup(parent, object, transformation);
		}
	}

	public Element drawGroup(Object parent, Object object, String tagName) {
		if (isAttached()) {
			return helper.drawGroup(parent, object, tagName);
		} else {
			return null;
		}
	}

	public void drawGroup(Object parent, Object object, Style style) {
		if (isAttached()) {
			helper.drawGroup(parent, object, style);
		}
	}

	public void drawGroup(Object parent, Object object) {
		if (isAttached()) {
			helper.drawGroup(parent, object);
		}
	}

	public Element getGroup(Object object) {
		if (isAttached()) {
			return helper.getGroup(object);
		} else {
			return null;
		}
	}

	public Object getGroupById(String id) {
		if (isAttached()) {
			return helper.getGroupById(id);
		} else {
			return null;
		}
	}

	public String getNameById(String id) {
		if (isAttached()) {
			return helper.getNameById(id);
		} else {
			return null;
		}
	}

	public Element getRootElement() {
		return helper.getRootElement();
	}

	public void setController(Object object, GraphicsController controller, int eventMask) {
		if (isAttached()) {
			helper.setController(object, controller, eventMask);
		}
	}

	public void setController(Object object, GraphicsController controller) {
		if (isAttached()) {
			helper.setController(object, controller);
		}
	}

	public void setController(Object parent, String name, GraphicsController controller, int eventMask) {
		if (isAttached()) {
			helper.setController(parent, name, controller, eventMask);
		}
	}

	public void setController(Object parent, String name, GraphicsController controller) {
		if (isAttached()) {
			helper.setController(parent, name, controller);
		}
	}

	public void setCursor(Object parent, String name, String cursor) {
		if (isAttached()) {
			helper.setCursor(parent, name, cursor);
		}
	}

	public void setCursor(Object object, String cursor) {
		if (isAttached()) {
			helper.setCursor(object, cursor);
		}
	}

	public void drawData(Object parent, Object object, String data, Matrix transformation) {
		if (isAttached()) {
			Element group = helper.getGroup(object);
			if (group == null) {
				group = helper.createOrUpdateGroup(parent, object, transformation, null);
				Dom.setInnerHTML(group, data);
			}
		}
	}

	public void drawImage(Object parent, final String name, final String href, Bbox bounds, PictureStyle style) {
		if (isAttached()) {
			// initially set display to none to avoid broken image
			if (helper.getElement(parent, name) == null) {
				style = (PictureStyle) style.clone();
				style.setDisplay("none");
			}
			final Element image = helper.createOrUpdateElement(parent, name, "img", style);
			Dom.setStyleAttribute(image, "position", "absolute");
			Dom.setStyleAttribute(image, "border", "0px");
			Dom.setStyleAttribute(image, "padding", "0px");
			Dom.setStyleAttribute(image, "margin", "0px");
			Dom.setStyleAttribute(image, "left", (int) bounds.getX() + "px");
			Dom.setStyleAttribute(image, "top", (int) bounds.getY() + "px");
			Dom.setStyleAttribute(image, "width", (int) bounds.getWidth() + "px");
			Dom.setStyleAttribute(image, "height", (int) bounds.getHeight() + "px");
			String oldHref = Dom.getElementAttribute(image, "src");
			// if href has changed, attach a listener to show the image on load
			if (href != null && !href.equals(oldHref)) {
				Dom.sinkEvents(image, Event.ONLOAD | Event.ONERROR);
				Dom.setEventListener(image, new EventListener() {

					private int retries = 5;

					public void onBrowserEvent(Event event) {
						switch (Dom.eventGetType(event)) {
							case Event.ONLOAD:
								Dom.setStyleAttribute(image, "display", "block");
								break;
							case Event.ONERROR:
								retries--;
								if (retries > 0) {
									Dom.setElementAttribute(image, "src", Dom.makeUrlAbsolute(href));
								}
								break;
						}
					}
				});
				Dom.setElementAttribute(image, "src", Dom.makeUrlAbsolute(href));
			}
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
	 * Hide the specified group. If the group does not exist, nothing will happen.
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

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------
	
	private boolean isAttached() {
		return parent != null && parent.isAttached();
	}
}