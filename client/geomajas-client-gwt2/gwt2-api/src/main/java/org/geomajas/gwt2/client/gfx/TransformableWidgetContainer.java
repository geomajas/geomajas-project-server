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

/**
 * Container of {@link TransformableWidget} elements.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface TransformableWidgetContainer extends TransformableWidget {

	/**
	 * Add a new child TransformableWidget to the list. Note that using this method children are added to the back of
	 * the list, which means that they are added on top of the visibility stack and thus may obscure other children.
	 * Take this order into account when adding children.
	 * 
	 * @param child The actual child to add.
	 */
	void add(TransformableWidget child);

	/**
	 * Insert a new child TransformableWidget in the list at a certain index. The position will determine it's place in
	 * the visibility stack, where the first element lies at the bottom and the last element on top.
	 * 
	 * @param widget THe actual widget to add.
	 * @param beforeIndex The position in the list where this widget should end up.
	 */
	void insert(TransformableWidget child, int beforeIndex);

	/**
	 * Remove a widget from the list.
	 * 
	 * @param widget The actual widget to remove.
	 * @return Returns true or false indicating whether or not removal was successful.
	 */
	boolean remove(TransformableWidget child);

	/**
	 * Get the index of a child widget.
	 * 
	 * @param widget
	 * @return the index or -1 if the widget is not part of this container.
	 */
	int indexOf(TransformableWidget child);

	/**
	 * Bring a certain widget to the front. In reality this widget is moved to the back of the list.
	 * 
	 * @param child The widget to bring to the front.
	 */
	void bringToFront(TransformableWidget child);

	/** Remove all widgets from this container. */
	void clear();

	/**
	 * Get the total number of widgets in this container.
	 * 
	 * @return The total number of widgets in this container.
	 */
	int getChildCount();

	/**
	 * Get the widget at a certain index.
	 * 
	 * @param index The index to look for a widget.
	 * @return The actual child if it was found.
	 */
	TransformableWidget getChild(int index);

}
