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
package org.geomajas.rendering;

import com.vividsolutions.jts.geom.Coordinate;

import java.text.DecimalFormat;


/**
 * <p>
 * Document interface (DOM) that supports serial writing.
 * </p>
 *
 * @author Jan De Moerloose
 */
public interface GraphicsDocument {

	/**
	 * Writes a text-node to the document.
	 *
	 * @param text The text to write into the text-node.
	 * @throws RenderException writing failed
	 */
	void writeTextNode(String text) throws RenderException;

	/**
	 * Writes a general object to the document, potentially as a child element
	 * of the last element written. Throws an exception if the object cannot be
	 * written.
	 *
	 * TODO: constrain this to objects with a specified interface ?
	 *
	 * @param o object to write
	 * @param asChild when true, write as child element
	 * @throws RenderException the object cannot be written
	 */
	void writeObject(Object o, boolean asChild) throws RenderException;

	/**
	 * Close the last element.
	 *
	 * @throws RenderException closing the document failed.
	 */
	void closeElement() throws RenderException;

	/**
	 * Writes an XML element either as a child element of the last element
	 * written or as the next element. Make sure the current start tag is
	 * finished !
	 *
	 * @param name tag to write
	 * @param asChild when true, write as child element
	 * @throws RenderException write failed
	 */
	void writeElement(String name, boolean asChild) throws RenderException;

	/**
	 * Writes an attribute with the specified value. An element must be created
	 * first.
	 *
	 * @param name name of attribute
	 * @param value value of attribute
	 * @throws RenderException write failed
	 */
	void writeAttribute(String name, double value) throws RenderException;

	/**
	 * Writes an attribute with the specified value. An element must be created
	 * first.
	 *
	 * @param name name of attribute
	 * @param value value of attribute
	 * @throws RenderException write failed
	 */
	void writeAttribute(String name, String value) throws RenderException;

	/**
	 * Write the start part of an attribute up to the opening value quote. The
	 * value should be written after this quote by calling other methods.
	 *
	 * @param name attribute name
	 * @throws RenderException write failed
	 */
	void writeAttributeStart(String name) throws RenderException;

	/**
	 * Writes the closing value quote of an attribute.
	 *
	 * @throws RenderException write failed
	 */
	void writeAttributeEnd() throws RenderException;

	/**
	 * Write the content of the <b>d</b> attribute in a <i>path</i> element.
	 *
	 * @param coordinates coordinates
	 * @throws RenderException write failed
	 */
	void writePathContent(Coordinate[] coordinates) throws RenderException;

	/**
	 * Write an array of coordinates as a closing "d" attribute of an path
	 * element.
	 *
	 * @param coordinates coordinates
	 * @throws RenderException write failed
	 */
	void writeClosedPathContent(Coordinate[] coordinates) throws RenderException;

	/**
	 * Write the remaining state if any. Closes elements if needed.
	 *
	 * @throws RenderException write failed
	 */
	void flush() throws RenderException;

	/**
	 * Set the maximum number of digits after the fraction. This can be
	 * important to save memory in the resulting string, when many floating
	 * point numbers have to be written.
	 *
	 * @param numDigits maximum number of fraction digits
	 */
	void setMaximumFractionDigits(int numDigits);

	/**
	 * Where there is a maximum, there is a minimum (no?). Usually defaults to
	 * 0.
	 *
	 * @param numDigits minimum number of fraction digits
	 */
	void setMinimumFractionDigits(int numDigits);

	/**
	 * Adds an id attribute to the last element written using a '.' to
	 * concatenate the current id prefix with the specified id.
	 *
	 * TODO: auto-manage id's as elements are added
	 *
	 * @param id id attribute to add
	 * @throws RenderException write failed
	 */
	void writeId(String id) throws RenderException;

	/**
	 * Set the id of the id prefix of the first element written.
	 *
	 * @param id root id
	 */
	void setRootId(String id);

	/**
	 * Return the coordinate formatter.
	 *
	 * @return decimal formatter
	 */
	DecimalFormat getFormatter();

	/**
	 * Return the current id.
	 *
	 * @return current id
	 */
	String getCurrentId();
}
