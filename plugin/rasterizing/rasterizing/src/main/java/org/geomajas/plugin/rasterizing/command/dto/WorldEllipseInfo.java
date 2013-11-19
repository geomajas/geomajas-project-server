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
package org.geomajas.plugin.rasterizing.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;

/**
 * Metadata DTO class that carries extra metadata information about a world ellipse.
 * 
 * @author Jan De Moerloose
 * @since 1.2.0
 */
@Api(allMethods = true)
public class WorldEllipseInfo extends WorldPaintableInfo {

	private static final long serialVersionUID = 120L;

	private Bbox bbox;

	/**
	 * @see #setBbox()
	 * @return 
	 */
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * Set the bounding box of the ellipse.
	 * @param bbox the bounding box
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

}
