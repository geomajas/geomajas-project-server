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
package org.geomajas.internal.rendering.writer.svg.geometry;

import com.vividsolutions.jts.geom.MultiLineString;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * <p>
 * SVG writer for <code>MultiLineString</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class MultiLineStringWriter implements GraphicsWriter {

	/**
	 * Writes the body for a <code>MultiLineString</code> object.
	 * MultiLineStrings are encoded into SVG path elements. This function writes
	 * the different lines in one d-attribute of an SVG path element, separated
	 * by an 'M' character.
	 *
	 * @param o The <code>MultiLineString</code> to be encoded.
	 */
	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		document.writeElement("path", asChild);
		document.writeAttributeStart("d");
		MultiLineString ml = (MultiLineString) o;
		for (int i = 0; i < ml.getNumGeometries(); i++) {
			document.writePathContent(ml.getGeometryN(i).getCoordinates());
		}
		document.writeAttributeEnd();
	}
}