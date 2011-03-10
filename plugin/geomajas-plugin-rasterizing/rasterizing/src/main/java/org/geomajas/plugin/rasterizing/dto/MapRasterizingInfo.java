/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.dto;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.geometry.Bbox;

/**
 * Extension of client map to carry extra rendering information.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MapRasterizingInfo implements ClientWidgetInfo, RasterizingConstants {

	private Bbox bounds;

	private double scale;

	// default to true (users should provide a background if necessary)
	private boolean transparent = true;

	/**
	 * Returns true if the underlying medium is transparent.
	 * 
	 * @return true if transparent, false otherwise
	 */
	public boolean isTransparent() {
		return transparent;
	}

	/**
	 * Sets whether the rendering should be done on a transparent medium.
	 * 
	 * @param transparent
	 *            true if rendering should be done on transparent medium
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * Returns the world bounds of the map.
	 * 
	 * @return bbox of world coordinates in the map's crs
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Set the bounds of this map.
	 * 
	 * @param bounds
	 *            bbox of world coordinates in the map's crs
	 */
	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	/**
	 * Returns the scale (pixel/map unit) of this map.
	 * 
	 * @return scale value
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Sets the scale (pixel/map unit) of this map.
	 * 
	 * @param scale the new scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

}
