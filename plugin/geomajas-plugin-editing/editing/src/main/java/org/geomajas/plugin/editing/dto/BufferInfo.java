/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.dto;

/**
 * DTO object that holds  additional information needed to perform a buffer operation.
 * 
 * @author Jan De Moerloose
 */
public class BufferInfo {

	private double distance;
	private int quadrantSegments;

	/**
	 * Get distance to buffer.
	 *
	 * @return distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Set distance to buffer.
	 *
	 * @param distance distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Get the number of line segments used to represent a quadrant of a circle.
	 * @return quadrantSegments
	 * 			the number of line segments used to represent a quadrant of a circle
	 */
	public int getQuadrantSegments() {
		return quadrantSegments;
	}
	
	/**
	 * Set the number of line segments used to represent a quadrant of a circle.
	 * @param quadrantSegments
	 * 			the number of line segments used to represent a quadrant of a circle
	 */
	public void setQuadrantSegments(int quadrantSegments) {
		this.quadrantSegments = quadrantSegments;
	}

}
