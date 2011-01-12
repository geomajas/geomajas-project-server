/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.rendering.writer.svg.geometry;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * <p>
 * Writes just the d-attribute for <code>LineString</code>, <code>Polygon</code>, <code>MultiLineString</code> and
 * <code>MultiPolygon</code> objects.
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
			document.writeAttributeStart("d");
			document.writePathContent(((LineString) o).getCoordinates());
			document.writeAttributeEnd();
		} else if (o instanceof Polygon) {
			document.writeAttributeStart("d");
			Polygon poly = (Polygon) o;
			LineString shell = poly.getExteriorRing();
			int nHoles = poly.getNumInteriorRing();
			document.writeClosedPathContent(shell.getCoordinates());
			for (int j = 0; j < nHoles; j++) {
				document.writeClosedPathContent(poly.getInteriorRingN(j).getCoordinates());
			}
			document.writeAttributeEnd();
		} else if (o instanceof MultiLineString) {
			document.writeAttributeStart("d");
			MultiLineString ml = (MultiLineString) o;
			for (int i = 0; i < ml.getNumGeometries(); i++) {
				document.writePathContent(ml.getGeometryN(i).getCoordinates());
			}
			document.writeAttributeEnd();
		} else if (o instanceof MultiPolygon) {
			document.writeAttributeStart("d");
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