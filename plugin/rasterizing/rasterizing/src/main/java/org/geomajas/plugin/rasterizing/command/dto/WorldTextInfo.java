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

import org.geomajas.geometry.Coordinate;

/**
 * Metadata DTO class that carries extra metadata information about a world text.
 * 
 * @author Jan De Moerloose
 * @since 1.2.0
 */
public class WorldTextInfo extends WorldPaintableInfo {

	private static final long serialVersionUID = 120L;

	private Coordinate anchor;

	public Coordinate getAnchor() {
		return anchor;
	}

	/**
	 * Sets the upper-left corner of the text.
	 * 
	 * @param anchor coordinate of upper-left corner.
	 */
	public void setAnchor(Coordinate anchor) {
		this.anchor = anchor;
	}

}
