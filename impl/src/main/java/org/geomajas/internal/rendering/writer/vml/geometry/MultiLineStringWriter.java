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

import com.vividsolutions.jts.geom.MultiLineString;

/**
 * <p>
 * VML writer for <code>MultiLineString</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class MultiLineStringWriter implements GraphicsWriter {

	/**
	 * Writes the body for a <code>MultiLineString</code> object.
	 * MultiLineStrings are encoded into SVG path elements. This function writes
	 * the different lines in one d-attribute of an SVG path element, separated
	 * by an 'M' character.
	 *
	 * @param o The <code>MultiLineString</code> to be encoded.
	 */
	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		document.writeElement("vml:shape", asChild);
		document.writeAttributeStart("path");
		MultiLineString ml = (MultiLineString) o;
		for (int i = 0; i < ml.getNumGeometries(); i++) {
			document.writePathContent(ml.getGeometryN(i).getCoordinates());
		}
		document.writeAttributeEnd();
	}
}