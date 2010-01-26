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

import org.geomajas.gwt.client.spatial.Bbox;

/**
 * <p>
 * Definition of an object that can be painted on a <code>GraphicsContext</code> . The painting is orchestrated by a
 * <code>PainterVisitor</code> which visits registered painter, who in turn know when to call the paintable's accept
 * functions.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface Paintable {

	/**
	 * A preferably unique ID that identifies the object even after it is painted. This can later be used to update or
	 * delete it from the <code>GraphicsContext</code>.
	 *
	 * @return
	 */
	String getId();

	/**
	 * The accept function. Usually it simply calls the visitor's visit function. But sometimes, more needs to be done.
	 *
	 * @param visitor
	 *            The visitor
	 * @param bounds
	 *            ????
	 * @param recursive
	 *            Should the painting be a recursive operation (accept -> visit -> paint -> accept, ...)
	 */
	void accept(PainterVisitor visitor, Bbox bounds, boolean recursive);
}
