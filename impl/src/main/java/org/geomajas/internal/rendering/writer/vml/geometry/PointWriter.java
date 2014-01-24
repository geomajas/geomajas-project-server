/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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

import com.vividsolutions.jts.geom.Point;

/**
 * <p>
 * VML writer for <code>Point</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class PointWriter implements GraphicsWriter {

	/**
	 * Writes the object to the specified document, optionally creating a child
	 * element. The object in this case should be a point.
	 *
	 * @param o the object (of type Point).
	 * @param document the document to write to.
	 * @param asChild create child element if true.
	 * @throws RenderException
	 */
	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		document.writeElement("vml:shape", asChild);
		Point p = (Point) o;
		String adj = document.getFormatter().format(p.getX()) + ","
				+ document.getFormatter().format(p.getY());
		document.writeAttribute("adj", adj);
	}
}