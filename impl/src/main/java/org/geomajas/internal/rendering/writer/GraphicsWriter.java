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
package org.geomajas.internal.rendering.writer;

import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;

/**
 * Writers are responsible for writing a specific class of objects to a DOM document.
 *
 * @author Jan De Moerloose
 */
public interface GraphicsWriter {

	/**
	 * Writes the object to the specified document, optionally creating a child
	 * element.
	 *
	 * @param object the object
	 * @param document the document
	 * @param asChild create child element if true
	 * @throws RenderException oops
	 */
	void writeObject(Object object, GraphicsDocument document, boolean asChild)
			throws RenderException;
}
