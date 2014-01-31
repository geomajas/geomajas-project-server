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

package org.geomajas.internal.rendering.writer.svg.geometry;

import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * <p>
 * SVG writer for <code>GeometryCollection</code> objects.
 * </p>
 *
 * @author Jan De Moerloose
 */
public class GeometryCollectionWriter implements GraphicsWriter {

	/**
	 * Writes a <code>GeometryCollection</code> object.
	 *
	 * @param o The <code>LineString</code> to be encoded.
	 */
	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		GeometryCollection coll = (GeometryCollection) o;
		document.writeElement("path", asChild);
		document.writeAttribute("fill-rule", "evenodd");
		document.writeAttributeStart("d");
		for (int i = 0; i < coll.getNumGeometries(); i++) {
			document.writeObject(coll.getGeometryN(i), true); // TODO delegate to appropriate writers, is this correct?
		}
		document.writeAttributeEnd();
	}
}
