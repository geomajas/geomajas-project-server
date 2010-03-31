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

import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.gfx.style.Style;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

import com.google.gwt.user.client.Element;

/**
 * Context to draw cross-browser vector graphics. The context is based on DOM tree manipulation and makes the following
 * assumptions:
 * <ul>
 * <li>The DOM tree has a single root group (DIV or SVG/VML group)</li>
 * <li>DOM tree branches are groups as well (DIV or SVG/VML group)</li>
 * <li>Groups can be uniquely mapped to GWT objects</li>
 * <li>DOM tree leafs are all other elements (line, rectangle, polygon)</li>
 * <li>The context is responsible for generating unique id's for each group</li>
 * <li>Appending DOM children (groups/elements) is done by providing the parent group object as an extra parameter or
 * null for appending to the root group</li>
 * <li>For non-group elements a name can be provided for later reference</li>
 * <li>Deleting/updating groups is done by providing the group object</li>
 * <li>Deleting/updating non-group elements is done by providing the parent group object and the name</li>
 * </ul>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public interface GraphicsContext {

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
	 * Draw a circle on the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The circle's name.
	 * @param position
	 *            The center position as a coordinate.
	 * @param radius
	 *            The circle's radius.
	 * @param style
	 *            The styling object by which the circle should be drawn.
	 */
	void drawCircle(Object parent, String name, Coordinate position, double radius, ShapeStyle style);

	/**
	 * Draw inner group data directly (implementation-specific shortcut). This method can only be called once, creating
	 * the group. Delete the group first to redraw with different data.
	 * 
	 * @param parent
	 *            The parent group's object
	 * @param object
	 *            The group's object
	 * @param data
	 *            fragment of data type supported by this context (SVG, VML, ...)
	 * @param transformation
	 *            transformation to apply to the group
	 */
	void drawData(Object parent, Object object, String data, Matrix transformation);

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context. A group is meant to group other elements
	 * together.
	 * 
	 * @param parent
	 *            parent group object
	 * @param object
	 *            group object
	 */
	void drawGroup(Object parent, Object object);

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context. A group is meant to group other elements
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
	void drawGroup(Object parent, Object object, Style style);

	/**
	 * Creates a group element in the technology (SVG/VML/...) of this context. A group is meant to group other elements
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
	 * Draw an image onto the the <code>GraphicsContext</code>.
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
	 * Draw a {@link LineString} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The LineString's name.
	 * @param line
	 *            The LineString to be drawn.
	 * @param style
	 *            The styling object for the LineString. Watch out for fill colors! If the fill opacity is not 0, then
	 *            the LineString will have a fill surface.
	 */
	void drawLine(Object parent, String name, LineString line, ShapeStyle style);

	/**
	 * Draw a {@link Polygon} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The Polygon's name.
	 * @param polygon
	 *            The Polygon to be drawn.
	 * @param style
	 *            The styling object for the Polygon.
	 */
	void drawPolygon(Object parent, String name, Polygon polygon, ShapeStyle style);

	/**
	 * Draw a rectangle onto the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The rectangle's name.
	 * @param rectangle
	 *            The rectangle to be drawn. The bounding box's origin, is the rectangle's upper left corner on the
	 *            screen.
	 * @param style
	 *            The styling object for the rectangle.
	 */
	void drawRectangle(Object parent, String name, Bbox rectangle, ShapeStyle style);

	/**
	 * Draw a type (def/symbol for svg, shapetype for vml).
	 * 
	 * @param parent
	 *            the parent of the shapetype for vml, null for svg
	 * @param id
	 *            the types's unique identifier TODO: how can we ensure this is unique (it is defined on the server) ?
	 * @param symbol
	 *            the symbol information
	 * @param style
	 *            The style to apply on the symbol.
	 * @param transformation
	 *            the transformation to apply on the symbol
	 */
	void drawSymbolDefinition(Object parent, String id, SymbolInfo symbol, ShapeStyle style, Matrix transformation);

	/**
	 * Draw a symbol, using some predefined ShapeType.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The symbol's name.
	 * @param position
	 *            The symbol's (X,Y) location on the graphics.
	 * @param style
	 *            The style to apply on the symbol.
	 * @param shapeTypeId
	 *            The name of the predefined ShapeType. This symbol will create a reference to this predefined type and
	 *            take on it's characteristics.
	 */
	void drawSymbol(Object parent, String name, Coordinate position, ShapeStyle style, String shapeTypeId);

	/**
	 * Draw a string of text onto the <code>GraphicsContext</code>.
	 * 
	 * @param parent
	 *            parent group object
	 * @param name
	 *            The text's name.
	 * @param text
	 *            The actual string content.
	 * @param position
	 *            The upper left corner for the text to originate.
	 * @param style
	 *            The styling object for the text.
	 */
	void drawText(Object parent, String name, String text, Coordinate position, FontStyle style);

	/**
	 * Return the current graphics height.
	 *
	 * @return graphics height
	 */
	int getHeight();

	/**
	 * Return the element name for the specified id.
	 * 
	 * @param id id
	 * @return the name of the element
	 */
	String getNameById(String id);

	/**
	 * Return the (enclosing) group for the specified element id.
	 * 
	 * @param id id
	 * @return the group object
	 */
	Object getGroupById(String id);

	/**
	 * Return the current graphics width.
	 *
	 * @return graphics width
	 */
	int getWidth();

	/**
	 * Hide the specified group. If the group does not exist, nothing will happen.
	 * 
	 * @param object
	 *            The group object.
	 */
	void hide(Object object);

	/**
	 * The initialization function for the GraphicsContext. It will create the initial DOM structure setup.
	 * 
	 * @param parent
	 *            The parent element, onto whom to attach the initial DOM structure.
	 */
	void initialize(Element parent);

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
	 * Set a new size for the graphics.
	 * 
	 * @param width
	 *            The new width.
	 * 
	 * @param height
	 *            The new height.
	 */
	void setSize(int width, int height);

	/**
	 * Hide the specified group. If the group does not exist, nothing will happen.
	 * 
	 * @param object
	 *            The group object.
	 */
	void unhide(Object object);
}