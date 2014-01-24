/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.tile;

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.global.CacheableObject;

/**
 * <p>
 * A unique spatial code determining the location of a tile. It implements the <code>Serializable</code> interface as it
 * is often used as a data transfer object.
 * </p>
 * <p>
 * Tiling mechanisms are usually build in several levels, where the top level has only one tile, and for each level that
 * you go deeper, the number of tiles doubles in both the X and the Y directions. All 3 parameters (tileLevel, X and Y)
 * must always be positive integers.
 * </p>
 * <p>
 * Note that at a certain tile level, X and Y must always be a value between 0 and <code>(2^tileLevel) - 1</code>. For
 * example:
 * <ul>
 * <li>Tile level = 0: X=0, Y=0</li>
 * <li>Tile level = 1: X=[0, 1], Y=[0, 1]</li>
 * <li>Tile level = 2: X=[0, 3], Y=[0, 3]</li>
 * <li>Tile level = 3: X=[0, 7], Y=[0, 7]</li>
 * <li>Tile level = N: X=[0, (2^N) - 1], Y=[0, (2^N) - 1]</li>
 * </ul>
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class TileCode implements Serializable, CacheableObject {

	private static final int PRIME = 31;

	private static final long serialVersionUID = 151L;

	private int x;

	private int y;

	private int tileLevel;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor.
	 */
	public TileCode() {
	}

	/**
	 * The only constructor available. It requires you to immediately set all it's values.
	 * 
	 * @param tileLevel
	 *            The tiling depth level. Where 0 means the top level. Make sure this is always a positive integer!
	 * @param x
	 *            The X-ordinate at the given level: X=[0, (2^tileLevel) - 1].
	 * @param y
	 *            The Y-ordinate at the given level: Y=[0, (2^tileLevel) - 1].
	 */
	public TileCode(int tileLevel, int x, int y) {
		this.x = x;
		this.y = y;
		this.tileLevel = tileLevel;
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Create a clone from this object.
	 *
	 * @return cloned tile code
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "CN_IDIOM_NO_SUPER_CALL",
			justification = "needed for GWT")
	public TileCode clone() { // NOSONAR super.clone() not supported by GWT
		return new TileCode(tileLevel, x, y);
	}

	/**
	 * Return the values as a readable text: <code>TileLevel-X-Y</code>.
	 *
	 * @return readable tile code
	 */
	public String toString() {
		return tileLevel + "-" + x + "-" + y;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Get the tiling depth level. Where 0 means the top level. Make sure this is always a positive integer!
	 *
	 * @return tile level
	 */
	public int getTileLevel() {
		return tileLevel;
	}

	/**
	 * set the tiling depth level. Where 0 means the top level. Make sure this is always a positive integer!
	 * 
	 * @param tileLevel
	 *            The new value.
	 */
	public void setTileLevel(int tileLevel) {
		this.tileLevel = tileLevel;
	}

	/**
	 * Get the X-ordinate at the given level: X=[0, (2^tileLevel) - 1].
	 *
	 * @return x-ordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * The X-ordinate at the given level: X=[0, (2^tileLevel) - 1].
	 * 
	 * @param x
	 *            The new value.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Get the Y-ordinate at the given level: Y=[0, (2^tileLevel) - 1].
	 *
	 * @return y-ordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * The Y-ordinate at the given level: Y=[0, (2^tileLevel) - 1].
	 * 
	 * @param y
	 *            The new value.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash
	 * codes (like MD5, SHA1, SHA2 etc) of (collections of) objects.
	 *
	 * @return cacheId
	 * @since 1.8.0
	 */
	public String getCacheId() {
		return tileLevel + "-" + x + "-" + y;
	}

	/**
	 * Are the two objects equal?
	 *
	 * @param o object to compare
	 * @return true when objects are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (!(o instanceof TileCode)) { return false; }

		TileCode tileCode = (TileCode) o;

		if (tileLevel != tileCode.tileLevel) { return false; }
		if (x != tileCode.x) { return false; }
		if (y != tileCode.y) { return false; }

		return true;
	}

	/**
	 * Calculate object hash code.
	 *
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		int result = x;
		result = PRIME * result + y;
		result = PRIME * result + tileLevel;
		return result;
	}
}
