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
package org.geomajas.internal.layer.tile;

import java.util.Comparator;

import org.geomajas.layer.tile.TileCode;

/**
 * Comparator that sorts {@link TileCode}s in a spiral manner. From center clockwise to the outside.
 * 
 * @author Oliver May
 * 
 */
public class TileCodeComparator implements Comparator<TileCode> {

	private int offsetX;;

	private int offsetY;

	/**
	 * Construct a tilecode comparator with the given offset so that the center tileCode is 0,0.
	 * 
	 * @param offsetX
	 *            the offset on the x axis
	 * @param offsetY
	 *            the offset on the y axis
	 */
	public TileCodeComparator(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	@Override
	public int compare(TileCode o1, TileCode o2) {
		// -1: o1 is closer to center
		//  0: same distance to center
		//  1: o2 is closer to center
		int x1 = o1.getX() - offsetX;
		int x2 = o2.getX() - offsetX;
		int y1 = o1.getY() - offsetY;
		int y2 = o2.getY() - offsetY;

		int o1d = Math.max(Math.abs(x1), Math.abs(y1));
		int o2d = Math.max(Math.abs(x2), Math.abs(y2));

		if (x1 == x2 && y1 == y2) { // points same position
			return 0;
		} else if (o1d != o2d) { // points on different sphere
			return (o1d < o2d) ? -1 : 1;
		} else { // points on same sphere
			if (y1 == o1d) { // Case: o1 on top row
				if (y1 == y2) {
					return (x1 < x2) ? -1 : 1;
				} else {
					return -1;
				}
			} else if (x1 == o1d) { // Case: o1 on right column
				if (y2 == o1d) {
					return 1;
				} else if (x1 == x2) {
					return (y1 > y2) ? -1 : 1;
				} else {
					return -1;
				}
			} else if (y1 == -1 * o1d) { // Case: o1 on bottom row
				if (y2 == o1d || x2 == o1d) {
					return 1;
				} else if (y1 == y2) {
					return (x1 > x2) ? -1 : 1;
				} else {
					return -1;
				}
			} else if (x1 == -1 * o1d) { // Case: o1 on left column
				if (y2 == o1d || x2 == o1d || y2 == -1 * o1d) {
					return 1;
				} else {
					return (y1 < y2) ? -1 : 1;
				}
			}
			return 0; // Should not reach this
		}
	}
}
