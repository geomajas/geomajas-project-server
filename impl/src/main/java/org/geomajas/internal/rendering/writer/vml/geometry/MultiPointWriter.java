/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
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

/**
 * MultiPointWriter
 *
 * <p>
 * Writer for <code>MultiPoint</code> objects. Extends the
 * <code>PointWriter</code>.
 * </p>
 *
 * TODO: check what happens with style definition in the enclosing group !!!!
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class MultiPointWriter implements GraphicsWriter {

	/**
	 * Does nothing at the moment, since geomajas does not support MultiPoint.
	 */
	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		// MultiPoint mp = (MultiPoint) o;
		// for (int i = 0; i < mp.getNumGeometries(); i++) {
		// document.writeElement("use", i == 0 ? asChild : false);
		// Point p = (Point) mp.getGeometryN(i);
		// document.writeAttribute("x", p.getX());
		// document.writeAttribute("y", p.getY());
		// }
	}

}
