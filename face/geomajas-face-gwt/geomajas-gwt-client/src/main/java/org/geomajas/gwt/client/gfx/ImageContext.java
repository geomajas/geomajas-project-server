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
package org.geomajas.gwt.client.gfx;

import org.geomajas.global.Api;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;

/**
 * A DOM based drawing context for images.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface ImageContext {

	/**
	 * Delete this element from the graphics DOM structure.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The element's name.
	 */
	void deleteElement(Object parent, String name);

	/**
	 * Delete this group from the graphics DOM structure.
	 * 
	 * @param object
	 *            The group's object.
	 */
	void deleteGroup(Object object);


	/**
	 * Draw inner group data directly (implementation-specific shortcut). This method can only be called once, creating
	 * the group. Delete the group first to redraw with different data.
	 * 
	 * @param parent
	 *            The parent group's object
	 * @param object
	 *            The group's object
	 * @param data
	 *            fragment of HTML data
	 * @param transformation
	 *            transformation to apply to the group
	 */
	void drawData(Object parent, Object object, String data, Matrix transformation);

	/**
	 * Creates a DIV element. A group is meant to group other elements
	 * together.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 */
	void drawGroup(Object parent, Object object);

	/**
	 * Creates a DIV element. A group is meant to group other elements
	 * together. Also this method gives you the opportunity to specify a transformation to the group. Warning: currently
	 * supports translation only !
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 * @param transformation
	 *            transformation to apply to the group
	 */
	void drawGroup(Object parent, Object object, Matrix transformation);

	/**
	 * Creates a DIV element. A group is meant to group other elements
	 * together, and in this case applying a style on them.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 * @param style
	 *            Add a style to a group.
	 */
	void drawGroup(Object parent, Object object, Style style);

	/**
	 * Creates a DIV element. A group is meant to group other elements
	 * together, possibly applying position and size to them. Warning: currently supports translation only !
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 * @param transformation
	 *            transformation to apply to the group
	 * @param style
	 *            Add a style to a group.
	 */
	void drawGroup(Object parent, Object object, Matrix transformation, Style style);

	/**
	 * Draw an image onto the the <code>HtmlContext</code>.
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
	void drawImage(Object parent, String name, String href, Bbox bounds, PictureStyle style);

	/**
	 * Return the element name for the specified id.
	 * 
	 * @param id
	 * @return the name of the element
	 */
	String getNameById(String id);

	/**
	 * Return the (enclosing) group for the specified element id.
	 * 
	 * @param id
	 * @return the group object
	 */
	Object getGroupById(String id);
	
	/**
	 * Return the id of the specified group.
	 * 
	 * @param group the group object
	 * @return the corresponding element id or null if the group has not been drawn.
	 */
	String getId(Object group);

	/**
	 * Hide the specified group. If the group does not exist, nothing will happen.
	 * 
	 * @param object
	 *            The group object.
	 */
	void hide(Object object);

	/**
	 * Set the controller on an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param object
	 *            the element on which the controller should be set.
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 */
	void setController(Object object, GraphicsController controller);

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
	void setController(Object parent, String name, GraphicsController controller);

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
	void setController(Object object, GraphicsController controller, int eventMask);

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
	void setController(Object parent, String name, GraphicsController controller, int eventMask);

	/**
	 * Set a specific cursor on an element of this <code>GraphicsContext</code>.
	 * 
	 * @param object
	 *            the element on which the controller should be set.
	 * @param cursor
	 *            The string representation of the cursor to use.
	 */
	void setCursor(Object object, String cursor);

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
	void setCursor(Object parent, String name, String cursor);

	/**
	 * Hide the specified group. If the group does not exist, nothing will happen.
	 * 
	 * @param object
	 *            The group object.
	 */
	void unhide(Object object);

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
	void moveElement(String name, Object sourceParent, Object targetParent);
}
