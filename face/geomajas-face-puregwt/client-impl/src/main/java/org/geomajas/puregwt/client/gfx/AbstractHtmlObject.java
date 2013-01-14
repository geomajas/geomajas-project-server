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
package org.geomajas.puregwt.client.gfx;

import org.geomajas.puregwt.client.service.DomService;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * Basic HTML element definition used in map rendering. Note that all HtmlObjects are actually GWT widgets.This class
 * will create an HTML element in the constructor and set as many style info as is provided. Also all HtmlObjects will
 * have an absolute positioning.
 * </p>
 * <p>
 * All getters and setters work immediately on the underlying HTML element for performance reasons. Extensions of this
 * class can represent individual HTML tags such as images, but also groups (DIV). In this case of a group (see
 * HtmlContainer) it is possible to attach multiple other HtmlObjects to create a tree structure.
 * </p>
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractHtmlObject extends Widget implements HtmlObject {

	private Widget parent;

	private double opacity = 1;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create an HtmlObject widget that represents a certain HTML element.
	 * 
	 * @param tagName
	 *            The tag-name of the HTML that should be created (DIV, IMG, ...).
	 */
	public AbstractHtmlObject(String tagName) {
		Element element = DOM.createElement(tagName);
		DOM.setStyleAttribute(element, "position", "absolute");
		DOM.setStyleAttribute(element, "width", "100%");
		DOM.setStyleAttribute(element, "height", "100%");
		setElement(element);
	}

	/**
	 * Create an HtmlObject widget that represents a certain HTML element.
	 * 
	 * @param tagName
	 *            The tag-name of the HTML that should be created (DIV, IMG, ...).
	 * @param width
	 *            The width for this element, expressed in pixels.
	 * @param height
	 *            The height for this element, expressed in pixels.
	 */
	public AbstractHtmlObject(String tagName, int width, int height) {
		Element element = DOM.createElement(tagName);
		DOM.setStyleAttribute(element, "position", "absolute");
		DOM.setStyleAttribute(element, "width", width + "px");
		DOM.setStyleAttribute(element, "height", height + "px");
		setElement(element);
	}

	/**
	 * Create an HtmlObject widget that represents a certain HTML element.
	 * 
	 * @param tagName
	 *            The tag-name of the HTML that should be created (DIV, IMG, ...).
	 * @param width
	 *            The width for this element, expressed in pixels.
	 * @param height
	 *            The height for this element, expressed in pixels.
	 * @param top
	 *            How many pixels should this object be placed from the top (relative to the parent origin).
	 * @param left
	 *            How many pixels should this object be placed from the left (relative to the parent origin).
	 */
	public AbstractHtmlObject(String tagName, int width, int height, int top, int left) {
		Element element = DOM.createElement(tagName);
		DOM.setStyleAttribute(element, "position", "absolute");
		DOM.setStyleAttribute(element, "width", width + "px");
		DOM.setStyleAttribute(element, "height", height + "px");
		DomService.setTop(element, top);
		DomService.setLeft(element, left);
		setElement(element);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public Widget getParent() {
		return parent;
	}

	public void setParent(Widget parent) {
		Widget oldParent = this.parent;
		if (parent == null) {
			if (oldParent != null && oldParent.isAttached()) {
				onDetach();
				assert !isAttached() : "Failure of " + this.getClass().getName() + " to call super.onDetach()";
			}
			this.parent = null;
		} else {
			if (oldParent != null) {
				throw new IllegalStateException("Cannot set a new parent without first clearing the old parent");
			}
			this.parent = parent;
			if (parent.isAttached()) {
				onAttach();
				assert isAttached() : "Failure of " + this.getClass().getName() + " to call super.onAttach()";
			}
		}
	}

	public int getWidth() {
		return sizeToInt(DOM.getStyleAttribute(getElement(), "width"));
	}

	public void setWidth(int width) {
		DOM.setStyleAttribute(getElement(), "width", width + "px");
	}

	public int getHeight() {
		return sizeToInt(DOM.getStyleAttribute(getElement(), "height"));
	}

	public void setHeight(int height) {
		DOM.setStyleAttribute(getElement(), "height", height + "px");
	}

	public int getLeft() {
		return sizeToInt(DOM.getStyleAttribute(getElement(), "left"));
	}

	public void setLeft(int left) {
		DomService.setLeft(getElement(), left);
	}

	public int getTop() {
		return sizeToInt(DOM.getStyleAttribute(getElement(), "top"));
	}

	public void setTop(int top) {
		DomService.setTop(getElement(), top);
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
		DOM.setStyleAttribute(getElement(), "filter", "alpha(opacity=" + (opacity * 100) + ")");
		DOM.setStyleAttribute(getElement(), "opacity", Double.toString(opacity));
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private int sizeToInt(String size) {
		int position = size.indexOf('p');
		if (position < 0) {
			return Integer.parseInt(size);
		}
		return Integer.parseInt(size.substring(0, position));
	}
}