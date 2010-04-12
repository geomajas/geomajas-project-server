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

import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.ImageContext;
import org.geomajas.gwt.client.gfx.context.DomHelper.Namespace;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.util.DOM;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * An HTML based image context.
 * 
 * @author Jan De Moerloose
 *
 */
public class DefaultImageContext implements ImageContext {

	private DomHelper helper;

	private Widget parent;

	private String id;
	/**
	 * Constructs an image context. The context will be appended to the specified parent widget.
	 * 
	 * @param parent
	 *            The parent element, onto whom to attach the initial DOM structure.
	 */
	public DefaultImageContext(Widget parent) {
		this.parent = parent;
		// root node
		Element rootNode = DOM.createElementNS(DOM.NS_HTML, "div");
		id = DOM.createUniqueId();
		rootNode.setId(id);
		DOM.setStyleAttribute(rootNode, "position", "absolute");
		DOM.setStyleAttribute(rootNode, "width", "100%");
		DOM.setStyleAttribute(rootNode, "height", "100%");
		// prevents overflowing of images in IE
		DOM.setStyleAttribute(rootNode, "overflow", "hidden");
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
				DOM.setInnerHTML(group, data);
			}
		}
	}

	public void drawImage(Object parent, String name, String href, Bbox bounds, PictureStyle style) {
		if (isAttached()) {
			Element image = helper.createOrUpdateElement(parent, name, "img", style);
			DOM.setStyleAttribute(image, "position", "absolute");
			DOM.setStyleAttribute(image, "border", "0px");
			DOM.setStyleAttribute(image, "padding", "0px");
			DOM.setStyleAttribute(image, "margin", "0px");
			DOM.setStyleAttribute(image, "position", "absolute");
			DOM.setStyleAttribute(image, "left", (int) bounds.getX() + "px");
			DOM.setStyleAttribute(image, "top", (int) bounds.getY() + "px");
			DOM.setStyleAttribute(image, "width", (int) bounds.getWidth() + "px");
			DOM.setStyleAttribute(image, "height", (int) bounds.getHeight() + "px");
			DOM.setElementAttribute(image, "src", href);
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
				DOM.setStyleAttribute(element, "visibility", "hidden");
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
				DOM.setStyleAttribute(element, "visibility", "inherit");
			}
		}
	}
	
	private boolean isAttached() {
		return parent != null && parent.isAttached();
	}


}
