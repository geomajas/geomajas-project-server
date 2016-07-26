/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.rendering.writer.vml.geometry;

import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * <p>
 * VML writer for <code>MultiPolygon</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class MultiPolygonWriter implements GraphicsWriter {

	/**
	 * Writes the body for a <code>MultiPolygon</code> object. MultiPolygons are
	 * encoded into SVG path elements. This function writes the different
	 * polygons in one d-attribute of an SVG path element, separated by an 'M'
	 * character. (in other words, it calls the super.writeBody for each
	 * polygon).
	 *
	 * @param o The <code>MultiPolygon</code> to be encoded.
	 */
	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		document.writeElement("vml:shape", asChild);
		document.writeAttribute("fill-rule", "evenodd");
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