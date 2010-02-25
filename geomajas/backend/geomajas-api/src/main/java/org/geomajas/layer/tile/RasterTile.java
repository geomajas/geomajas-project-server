/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.layer.tile;

import org.geomajas.geometry.Bbox;

import java.io.Serializable;

/**
 * <p>
 * A raster image represents all the meta-data needed to put an image on a screen. The bounds in the meta-data are
 * expressed in application coordinates, the indices are expressed in view coordinates (this means that the y-axis is
 * flipped)
 * </p>
 * 
 * @author Jan De Moerloose
 */
public class RasterTile implements Serializable {

	private static final long serialVersionUID = 151L;

	private String id;

	private TileCode code;

	private String url;

	private Bbox bounds;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor - does nothing.
	 */
	public RasterTile() {
	}

	/**
	 * Constructor setting the tile's unique ID and bounds.
	 * 
	 * @param bounds
	 *            Bounds for the image on the client side.
	 * @param id
	 *            Unique identifier for this tile (normally, id = <layer id>+"."+<tile level>+"."+<x-index>,<y-index>).
	 */
	public RasterTile(Bbox bounds, String id) {
		this.bounds = bounds;
		this.id = id;
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Convert to readable string.
	 *
	 * @return readable string
	 */
	public String toString() {
		if (code == null) {
			return "[bounds=" + bounds + ",url=" + url + "]";
		}
		return "[z=" + code.getTileLevel() + ",x=" + code.getX() + ",y=" + code.getY() + ",bounds=" + bounds + ",url="
				+ url + "]";
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Return the unique identifier for this tile (normally, id = <layer id>+"."+<tile level>+"."+<x-index>,<y-index>).
	 *
	 * @return tile id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set a new unique identifier for this tile.
	 * 
	 * @param id tile id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the unique code for this tile. Consider this it's unique identifier within a raster layer.
	 *
	 * @return tile code
	 */
	public TileCode getCode() {
		return code;
	}

	/**
	 * Set the unique code for this tile. Consider this it's unique identifier within a raster layer.
	 * 
	 * @param code
	 *            The tile's code.
	 */
	public void setCode(TileCode code) {
		this.code = code;
	}

	/**
	 * Returns the bounds for the image on the client side.
	 *
	 * @return tile bounding box
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Sets the bounds for the image on the client side.
	 * 
	 * @param bounds
	 *            The image bounds.
	 */
	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	/**
	 * Return the URL to the actual image for this raster tile. It is that image that will really display the rendered
	 * tile.
	 *
	 * @return URL for the raster image
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the URL to the actual image for this raster tile. It is that image that will really display the rendered
	 * tile.
	 * 
	 * @param url
	 *            The location of the actual image.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
