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
package org.geomajas.internal.rendering.writer.vml.geometry;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * <p>
 * Writes just the path-attribute for <code>LineString</code>,
 * <code>Polygon</code>, <code>MultiLineString</code> and
 * <code>MultiPolygon</code> objects to VML.
 * </p>
 *
 * @author Jan De Moerloose
 */
public class PathWriter implements GraphicsWriter {

	/**
	 * Writes the path-attribute for a <code>LineString</code>,
	 * <code>Polygon</code>, <code>MultiLineString</code> or
	 * <code>MultiPolygon</code> object.
	 *
	 * @param o The geometry to be encoded.
	 */
	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		if (o instanceof LineString) {
			document.writeAttributeStart("path");
			document.writePathContent(((LineString) o).getCoordinates());
			document.writeAttributeEnd();
		} else if (o instanceof Polygon) {
			document.writeAttributeStart("path");
			Polygon poly = (Polygon) o;
			LineString shell = poly.getExteriorRing();
			int nHoles = poly.getNumInteriorRing();
			document.writeClosedPathContent(shell.getCoordinates());
			for (int j = 0; j < nHoles; j++) {
				document.writeClosedPathContent(poly.getInteriorRingN(j).getCoordinates());
			}
			document.writeAttributeEnd();
		} else if (o instanceof MultiLineString) {
			document.writeAttributeStart("path");
			MultiLineString ml = (MultiLineString) o;
			for (int i = 0; i < ml.getNumGeometries(); i++) {
				document.writePathContent(ml.getGeometryN(i).getCoordinates());
			}
			document.writeAttributeEnd();
		} else if (o instanceof MultiPolygon) {
			document.writeAttributeStart("path");
			MultiPolygon mpoly = (MultiPolygon) o;
			for (int i = 0; i < mpoly.getNumGeometries(); i++) {
				Polygon poly = (Polygon) mpoly.getGeometryN(i);
				LineString shell = poly.getExteriorRing();
				int nHoles = poly.getNumInteriorRing();
				document.writeClosedPathContent(shell.getCoordinates());

				for (int j = 0; j < nHoles; j++) {
					document.writeClosedPathContent(poly.getInteriorRingN(j).getCoordinates());
				}
			}
			document.writeAttributeEnd();
		}
	}

}
