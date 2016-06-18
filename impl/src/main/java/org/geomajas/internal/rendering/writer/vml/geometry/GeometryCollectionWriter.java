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

import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * <p>
 * VML writer for <code>GeometryCollection</code> objects.
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
		document.writeElement("vml:shape", asChild);
		document.writeAttribute("fill-rule", "evenodd");
		document.writeAttributeStart("path");
		for (int i = 0; i < coll.getNumGeometries(); i++) {
		}
		document.writeAttributeEnd();
	}
}