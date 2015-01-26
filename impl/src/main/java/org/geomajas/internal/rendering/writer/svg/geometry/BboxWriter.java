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
package org.geomajas.internal.rendering.writer.svg.geometry;

import org.geomajas.geometry.Bbox;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * <p>
 * SVG writer for <code>bbox</code> objects.
 * </p>
 *
 * @author Pieter De Graef
 */
public class BboxWriter implements GraphicsWriter {

	public void writeObject(Object o, GraphicsDocument document, boolean asChild) throws RenderException {
		document.writeElement("rect", asChild);
		Bbox env = (Bbox) o;
		document.writeAttribute("x", env.getX());
		document.writeAttribute("y", env.getY());
		document.writeAttribute("width", env.getWidth());
		document.writeAttribute("height", env.getHeight());
		document.writeAttribute("style", "fill-opacity:0;stroke:#FF0000;stroke-width:5;stroke-opacity:1;");
	}
}