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

import org.geomajas.annotation.Api;

/**
 * <p>
 * Container for {@link HtmlObject}s. This class is an extension of an {@link HtmlObject} that in turn is used to store
 * a list of child {@link HtmlObject}s. In other words instances of this class represent the nodes in an HTML tree
 * structure.
 * </p>
 * <p>
 * The main goal for this class is to provide a container for child {@link HtmlObject}s. Through it's methods these
 * children can be managed. Note though that this class is itself also an {@link HtmlObject}, which means that this
 * class too has a fixed size and position. Child positions are always relative to this class' position.
 * </p>
 * <p>
 * Another important factor concerning this definition is that HtmlContainers can be transformed using a matrix
 * notation. This way it is possible to translate or zoom this container object. Rotation is not necessarily supported.
 * Implementations will specify which kinds of transformations are supported.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface HtmlContainer extends HtmlObject {

	/**
	 * Zoom this container in or out to a given transformation origin. This transformation is taken literally, it does
	 * not stack onto a current transformation should there be one.
	 * 
	 * @param scale
	 *            The zooming factor.
	 * @param x
	 *            The X ordinate of the origin to where we want this container to zoom.
	 * @param y
	 *            The Y ordinate of the origin to where we want this container to zoom.
	 */
	void applyScale(double scale, int x, int y);

	/**
	 * Add a new child HtmlObject to the list. Note that using this method children are added to the back of the list,
	 * which means that they are added on top of the visibility stack and thus may obscure other children. Take this
	 * order into account when adding children.
	 * 
	 * @param child
	 *            The actual child to add.
	 */
	void add(HtmlObject child);

	/**
	 * Insert a new child HtmlObject in the list at a certain index. The position will determine it's place in the
	 * visibility stack, where the first element lies at the bottom and the last element on top.
	 * 
	 * @param child
	 *            THe actual child to add.
	 * @param beforeIndex
	 *            The position in the list where this child should end up.
	 */
	void insert(HtmlObject child, int beforeIndex);

	/**
	 * Remove a child element from the list.
	 * 
	 * @param child
	 *            The actual child to remove.
	 * @return Returns true or false indicating whether or not removal was successful.
	 */
	boolean remove(HtmlObject child);

	/**
	 * Bring a certain child to the front. In reality this child is moved to the back of the list.
	 * 
	 * @param child
	 *            The child to bring to the front.
	 */
	void bringToFront(HtmlObject child);

	/** Remove all children from this container. */
	void clear();

	/**
	 * Get the total number of children in this container.
	 * 
	 * @return The total number of children in this container.
	 */
	int getChildCount();

	/**
	 * Get the child at a certain index.
	 * 
	 * @param index
	 *            The index to look for a child.
	 * @return The actual child if it was found.
	 */
	HtmlObject getChild(int index);
}