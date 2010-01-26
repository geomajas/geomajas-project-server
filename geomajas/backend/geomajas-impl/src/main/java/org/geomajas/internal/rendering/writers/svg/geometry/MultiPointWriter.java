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
package org.geomajas.internal.rendering.writers.svg.geometry;

import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * MultiPointWriter
 *
 * <p>
 * Writer for <code>MultiPoint</code> objects. Extends the
 * <code>PointWriter</code>.
 * </p>
 *
 * TODO: check what happens with style defintion in the enclosing group !!!!
 *
 * @author Pieter De Graef, Jan De Moerloose
 */
public class MultiPointWriter implements GraphicsWriter {

	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		MultiPoint mp = (MultiPoint) o;
		for (int i = 0; i < mp.getNumGeometries(); i++) {
			document.writeElement("use", i == 0 && asChild);
			Point p = (Point) mp.getGeometryN(i);
			document.writeAttribute("x", p.getX());
			document.writeAttribute("y", p.getY());
		}
	}
}