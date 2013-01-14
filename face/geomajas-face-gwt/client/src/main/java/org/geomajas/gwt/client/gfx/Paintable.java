/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * <p>
 * Definition of an object that can be painted on a <code>GraphicsContext</code> . The painting is orchestrated by a
 * <code>PainterVisitor</code> which visits registered painter, who in turn know when to call the paintable's accept
 * functions.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api
public interface Paintable {

	/**
	 * The accept function. Usually it calls the visitor's visit function. But sometimes, more needs to be done.
	 * 
	 * @param visitor
	 *            The visitor
	 * @param group
	 *            The group in which to draw
	 * @param bounds
	 *            The bounds that should be redrawn
	 * @param recursive
	 *            Should the painting be a recursive operation (accept -> visit -> paint -> accept, ...)
	 */
	void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive);
}
