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

import org.geomajas.geometry.Bbox;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * <p>
 * SVG writer for <code>bbox</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 */
public class BboxWriter implements GraphicsWriter {

	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		document.writeElement("rect", asChild);
		Bbox env = (Bbox) o;
		document.writeAttribute("x", env.getX());
		document.writeAttribute("y", env.getY());
		document.writeAttribute("width", env.getWidth());
		document.writeAttribute("height", env.getHeight());
		document.writeAttribute("style", "fill-opacity:0;stroke:#FF0000;stroke-width:5;stroke-opacity:1;");
	}
}