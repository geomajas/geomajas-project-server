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

import com.google.gwt.user.client.Element;
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
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * ???
 * 
 * @author check subversion
 */
public interface GraphicsContext {

	/**
	 * The initialization function for the GraphicsContext. It will create the initial DOM structure setup.
	 * 
	 * @param parent
	 *            The parent element, onto whom to attach the initial DOM structure.
	 */
	void initialize(Element parent);

	/**
	 * Draw directly (implementation-specific shortcut).
	 */
	void drawData(String id, String data);

	/**
	 * Creates either a SVG/VML group or a HTML DIV element, depending on the name-space. A group is meant to group
	 * other elements together.
	 * 
	 * @param id
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 */
	void drawGroup(String id, String namespace);

	/**
	 * Creates either a SVG/VML group or a HTML DIV element, depending on the name-space. A group is meant to group
	 * other elements together. Also this method gives you the opportunity to specify a specific width and height.
	 * 
	 * @param id
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 * @param width
	 *            A fixed width for the group.
	 * @param height
	 *            A fixed height for the group.
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 */
	void drawGroup(String id, String namespace, int width, int height, Matrix transformation);

	/**
	 * Creates either a SVG/VML group or a HTML DIV element, depending on the name-space. A group is meant to group
	 * other elements together, possibly applying a transformation upon them.
	 * 
	 * @param id
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 */
	void drawGroup(String id, String namespace, Matrix transformation);

	/**
	 * Creates either a SVG/VML group or a HTML DIV element, depending on the name-space. A group is meant to group
	 * other elements together, and in this case applying a style on them.
	 * 
	 * @param id
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 * @param style
	 *            Add a style to a group.
	 */
	void drawGroup(String id, String namespace, Style style);

	/**
	 * Creates either a SVG/VML group or a HTML DIV element, depending on the name-space. A group is meant to group
	 * other elements together, possibly applying a transformation upon them.
	 * 
	 * @param id
	 *            The group's identifier.
	 * @param namespace
	 *            The name-space wherein the group is to be created. If the name-space is null, a HTML DIV element is
	 *            created.
	 * @param transformation
	 *            On each group, it is possible to apply a matrix transformation (currently translation only). This is
	 *            the real strength of a group element. Never apply transformations on any other kind of element.
	 * @param style
	 *            Add a style to a group.
	 */
	void drawGroup(String id, String namespace, Matrix transformation, Style style);

	/**
	 * Draw a {@link LineString} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param id
	 *            The LineString's identifier.
	 * @param line
	 *            The LineString to be drawn.
	 * @param style
	 *            The styling object for the LineString. Watch out for fill colors! If the fill opacity is not 0, then
	 *            the LineString will have a fill surface.
	 */
	void drawLine(String id, LineString line, ShapeStyle style);

	/**
	 * Draw a {@link Polygon} geometry onto the <code>GraphicsContext</code>.
	 * 
	 * @param id
	 *            The Polygon's identifier.
	 * @param polygon
	 *            The Polygon to be drawn.
	 * @param style
	 *            The styling object for the Polygon.
	 */
	void drawPolygon(String id, Polygon polygon, ShapeStyle style);

	/**
	 * Draw a rectangle onto the <code>GraphicsContext</code>.
	 * 
	 * @param id
	 *            The Rectangle's identifier.
	 * @param rectangle
	 *            The rectangle to be drawn. The bounding box's origin, is the rectangle's upper left corner on the
	 *            screen.
	 * @param style
	 *            The styling object for the rectangle.
	 */
	void drawRectangle(String id, Bbox rectangle, ShapeStyle style);

	/**
	 * Draw a circle on the <code>GraphicsContext</code>.
	 * 
	 * @param id
	 *            The circle's ID.
	 * @param position
	 *            The center position as a coordinate.
	 * @param radius
	 *            The circle's radius.
	 * @param style
	 *            The styling object by which the circle should be drawn.
	 */
	void drawCircle(String id, Coordinate position, double radius, ShapeStyle style);

	/**
	 * Draw a symbol/point object.
	 */
	void drawSymbol(String id, Point symbol, ShapeStyle style, String shapeTypeId);

	/**
	 * Draw an image onto the the <code>GraphicsContext</code>.
	 * 
	 * @param id
	 *            The image's identifier.
	 * @param href
	 *            The image's location (URL).
	 * @param bounds
	 *            The bounding box that sets the image's origin (x and y), it's width and it's height.
	 * @param style
	 *            A styling object to be passed along with the image. Can be null.
	 * @param asDiv
	 *            Draw the image as a DIV HTML object. This can only be done in Internet Explorer.
	 */
	void drawImage(String id, String href, Bbox bounds, PictureStyle style, boolean asDiv);

	/**
	 * Draw a string of text onto the <code>GraphicsContext</code>.
	 * 
	 * @param id
	 *            The text's identifier.
	 * @param text
	 *            The actual string content.
	 * @param position
	 *            The upper left corner for the text to originate.
	 * @param style
	 *            The styling object for the text.
	 */
	void drawText(String id, String text, Coordinate position, FontStyle style);

	/**
	 * Draw a type (def/symbol for svg, shapetype for vml).
	 */
	void drawShapeType(String id, SymbolInfo symbol, ShapeStyle style, Matrix transformation);

	/**
	 * Delete in the graphics DOM structure one or more elements. Which elements are to be removed is specified by the
	 * parameters you give here.
	 * 
	 * @param id
	 *            The ID of the element in question. Either this element and everything under it is removed, or this
	 *            element's children are removed.
	 * @param childrenOnly
	 *            If this value of false, the element with id will be removed. If this value is true, then only the
	 *            children of element id are removed, but element id itself remains.
	 */
	void deleteShape(String id, boolean childrenOnly);

	/**
	 * Set a specific cursor type on a specific element.
	 * 
	 * @param id
	 *            optional. If not used, the cursor will be applied on the entire <code>GraphicsContext</code>.
	 * @param cursor
	 *            The string representation of the cursor to use.
	 */
	void setCursor(String id, String cursor);

	/**
	 * Set the controller of an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param id
	 *            The id of the element of which the controller should be set.
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 */
	void setController(String id, GraphicsController controller);

	/**
	 * Set the controller of an element of this <code>GraphicsContext</code> so it can react to events.
	 * 
	 * @param id
	 *            The id of the element of which the controller should be set.
	 * @param controller
	 *            The new <code>GraphicsController</code>
	 * @param eventMask
	 *            a bitmask to specify which events to listen for {@link com.google.gwt.user.client.Event}
	 */
	void setController(String id, GraphicsController controller, int eventMask);

	/**
	 * Set the background color of the entire GraphicsContext.
	 * 
	 * @param color
	 *            An HTML color code (i.e. #FF0000).
	 */
	void setBackgroundColor(String color);

	/**
	 * Hide the DOM element with the given id. If the element does not exist, nothing will happen.
	 * 
	 * @param id
	 *            The identifier of the element to hide.
	 */
	void hide(String id);

	/**
	 * Unhide (show) the DOM element with the given id. If the element does not exist, nothing will happen.
	 * 
	 * @param id
	 *            The identifier of the element to show again.
	 */
	void unhide(String id);

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
	 * Return the current graphics width.
	 */
	int getWidth();

	/**
	 * Return the current graphics height.
	 */
	int getHeight();
}