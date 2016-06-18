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
package org.geomajas.internal.rendering.writer.svg.geometry;

import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

import com.vividsolutions.jts.geom.LineString;

/**
 * <p>
 * SVG writer for <code>LineString</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class LineStringWriter implements GraphicsWriter {

	/**
	 * Writes a {@link LineString} object. LineStrings are encoded into SVG
	 * path elements. This function writes: "&lt;path d=".
	 *
	 * @param o The {@link LineString} to be encoded.
	 */
	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		document.writeElement("path", asChild);
		document.writeAttributeStart("d");
		document.writePathContent(((LineString) o).getCoordinates());
		document.writeAttributeEnd();
	}
}