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
package org.geomajas.internal.rendering.writer.svg.geometry;

import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * <p>
 * SVG writer for <code>Polygon</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class PolygonWriter implements GraphicsWriter {

	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		document.writeElement("path", asChild);
		document.writeAttribute("fill-rule", "evenodd");
		document.writeAttributeStart("d");
		Polygon poly = (Polygon) o;
		LineString shell = poly.getExteriorRing();
		int nHoles = poly.getNumInteriorRing();
		document.writeClosedPathContent(shell.getCoordinates());
		for (int j = 0; j < nHoles; j++) {
			document.writeClosedPathContent(poly.getInteriorRingN(j).getCoordinates());
		}
		document.writeAttributeEnd();
	}
}