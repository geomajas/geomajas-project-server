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
package org.geomajas.gwt2.client.gfx;

import org.geomajas.gwt2.client.service.DomService;

import com.google.gwt.user.client.DOM;
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
public abstract class AbstractHtmlObject implements HtmlObject {

	private Widget widget;

	private double opacity = 1;

	private int left;

	private int top;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create an HtmlObject widget that represents a certain HTML element.
	 * 
	 * @param tagName
	 *            The tag-name of the HTML that should be created (DIV, IMG, ...).
	 */
	public AbstractHtmlObject(Widget widget) {
		this(widget, 100, 100);
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
	public AbstractHtmlObject(Widget widget, int width, int height) {
		this.widget = widget;
		DOM.setStyleAttribute(widget.getElement(), "position", "absolute");
		setWidth(width);
		setHeight(height);
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
	public AbstractHtmlObject(Widget widget, int width, int height, int top, int left) {
		this.widget = widget;
		DOM.setStyleAttribute(widget.getElement(), "position", "absolute");
		setWidth(width);
		setHeight(height);
		setTop(top);
		setLeft(left);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public Widget getParent() {
		return widget.getParent();
	}

	public int getWidth() {
		return sizeToInt(DOM.getStyleAttribute(widget.getElement(), "width"));
	}

	public void setWidth(int width) {
		DOM.setStyleAttribute(widget.getElement(), "width", width + "px");
	}

	public int getHeight() {
		return sizeToInt(DOM.getStyleAttribute(widget.getElement(), "height"));
	}

	public void setHeight(int height) {
		DOM.setStyleAttribute(widget.getElement(), "height", height + "px");
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
		int leftShift = 0;
		if (widget.getParent() != null) {
			leftShift = widget.getParent().getElement().getPropertyInt(HtmlGroup.LEFT_SHIFT);
		}
		DomService.setLeft(widget.getElement(), left + leftShift);
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
		int topShift = 0;
		if (widget.getParent() != null) {
			topShift = widget.getParent().getElement().getPropertyInt(HtmlGroup.TOP_SHIFT);
		}
		DomService.setTop(widget.getElement(), top + topShift);
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
		DOM.setStyleAttribute(widget.getElement(), "filter", "alpha(opacity=" + (opacity * 100) + ")");
		DOM.setStyleAttribute(widget.getElement(), "opacity", Double.toString(opacity));
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setVisible(boolean visible) {
		widget.setVisible(visible);
	}

	@Override
	public boolean isVisible() {
		return widget.isVisible();
	}

	private int sizeToInt(String size) {
		int position = size.indexOf('p');
		if (position < 0) {
			return Integer.parseInt(size);
		}
		return Integer.parseInt(size.substring(0, position));
	}
}