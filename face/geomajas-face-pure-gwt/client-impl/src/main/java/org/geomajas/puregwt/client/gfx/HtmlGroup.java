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

package org.geomajas.puregwt.client.gfx;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.util.Dom;

import com.google.gwt.dom.client.Node;

/**
 * <p>
 * Container for {@link AbstractHtmlObject}s. This class is an extension of an {@link AbstractHtmlObject} that in turn
 * is used to store a list of child {@link AbstractHtmlObject}s. In other words instances of this class represent the
 * nodes in an HTML tree structure.
 * </p>
 * <p>
 * The main goal for this class is to provide a container for child {@link AbstractHtmlObject}s. Through it's methods
 * these children can be managed. Note though that this class is itself also an {@link AbstractHtmlObject}, which means
 * that this class too has a fixed size and position. Child positions are always relative to this class' position.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class HtmlGroup extends AbstractHtmlObject implements HtmlContainer {

	private List<HtmlObject> children = new ArrayList<HtmlObject>();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create an HTML container widget that represents a DIV element. When using this constructor, width and height are
	 * set to 100%. We always set the width and height, because XHTML does not like DIV's without sizing attributes.
	 */
	public HtmlGroup() {
		super("div");
	}

	/**
	 * Create an HTML container widget that represents a DIV element.
	 * 
	 * @param width
	 *            The width for this container, expressed in pixels.
	 * @param height
	 *            The height for this container, expressed in pixels.
	 */
	public HtmlGroup(int width, int height) {
		super("div", width, height);
	}

	/**
	 * Create an HTML container widget that represents a DIV element.
	 * 
	 * @param width
	 *            The width for this container, expressed in pixels.
	 * @param height
	 *            The height for this container, expressed in pixels.
	 * @param top
	 *            How many pixels should this container be placed from the top (relative to the parent origin).
	 * @param left
	 *            How many pixels should this container be placed from the left (relative to the parent origin).
	 */
	public HtmlGroup(int width, int height, int top, int left) {
		super("div", width, height, top, left);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Add a new child HtmlObject to the list. Note that using this method children are added to the back of the list,
	 * which means that they are added on top of the visibility stack and thus may obscure other children. Take this
	 * order into account when adding children.
	 * 
	 * @param child
	 *            The actual child to add.
	 */
	public void add(HtmlObject child) {
		child.setParent(this);
		getElement().appendChild(child.getElement());
		children.add(child);
	}

	/**
	 * Insert a new child HtmlObject in the list at a certain index. The position will determine it's place in the
	 * visibility stack, where the first element lies at the bottom and the last element on top.
	 * 
	 * @param child
	 *            The actual child to add.
	 * @param beforeIndex
	 *            The position in the list where this child should end up.
	 */
	public void insert(HtmlObject child, int beforeIndex) {
		if (beforeIndex >= getChildCount()) {
			add(child);
			return;
		}
		Node beforeNode = getElement().getChild(beforeIndex);
		getElement().insertBefore(child.getElement(), beforeNode);
		child.setParent(this);

		List<HtmlObject> newChildList = new ArrayList<HtmlObject>();
		for (int i = 0; i < children.size(); i++) {
			if (i == beforeIndex) {
				newChildList.add(child);
			}
			newChildList.add(children.get(i));
		}
		children = newChildList;
	}

	/**
	 * Remove a child element from the list.
	 * 
	 * @param child
	 *            The actual child to remove.
	 * @return Returns true or false indicating whether or not removal was successful.
	 */
	public boolean remove(HtmlObject child) {
		int index = getIndex(child);
		if (index >= 0) {
			getElement().removeChild(getElement().getChild(index));
			children.remove(index);
			child.setParent(null);
			return true;
		}
		return false;
	}

	/**
	 * Bring a certain child to the front. In reality this child is moved to the back of the list.
	 * 
	 * @param child
	 *            The child to bring to the front.
	 */
	public void bringToFront(HtmlObject child) {
		if (remove(child)) {
			add(child);
		}
	}

	/** Remove all children from this container. */
	public void clear() {
		while (getElement().hasChildNodes()) {
			getElement().removeChild(getElement().getFirstChild());
		}
		children.clear();
	}

	/**
	 * Get the total number of children in this container.
	 * 
	 * @return The total number of children in this container.
	 */
	public int getChildCount() {
		return children.size();
	}

	/**
	 * Get the child at a certain index.
	 * 
	 * @param index
	 *            The index to look for a child.
	 * @return The actual child if it was found.
	 */
	public HtmlObject getChild(int index) {
		return children.get(index);
	}

	/**
	 * Apply the given matrix transformation onto this container object. This transformation is taken literally, it does
	 * not stack onto a current transformation should there be one.
	 * 
	 * @param scale
	 *            The zooming factor.
	 * @param x
	 *            The x origin to where we want this container to zoom.
	 * @param y
	 *            The y origin to where we want this container to zoom.
	 */
	public void applyScale(double scale, int x, int y) {
		Dom.setStyleAttribute(getElement(), "MozTransform", "scale(" + scale + ")");
		Dom.setStyleAttribute(getElement(), "MozTransformOrigin", x + "px " + y + "px");
		Dom.setStyleAttribute(getElement(), "WebkitTransform", "scale(" + scale + ")");
		Dom.setStyleAttribute(getElement(), "WebkitTransformOrigin", x + "px " + y + "px");

		if (Dom.isIE()) {
			Dom.setStyleAttribute(getElement(), "zoom", Double.toString(scale));
		}
		// Safari - if webkit stuff doesn't work, try: -o-transform: rotate(-90deg) translate(0px, -45px);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private int getIndex(HtmlObject child) {
		for (int i = 0; i < children.size(); i++) {
			HtmlObject img = children.get(i);
			if (img.equals(child)) {
				return i;
			}
		}
		return -1;
	}
}