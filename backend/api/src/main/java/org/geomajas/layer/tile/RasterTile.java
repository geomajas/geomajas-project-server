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
package org.geomajas.layer.tile;

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;

/**
 * <p>
 * A raster image represents all the meta-data needed to put an image on a screen. The bounds in the meta-data are
 * expressed in application coordinates, the indices are expressed in view coordinates (this means that the y-axis is
 * flipped)
 * </p>
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
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
