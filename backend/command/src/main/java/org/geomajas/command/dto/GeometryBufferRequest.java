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
package org.geomajas.command.dto;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.GeometryBufferCommand}.
 * 
 * @author Emiel Ackermann
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometryBufferRequest implements CommandRequest {

	private static final long serialVersionUID = 1110L;

	/** Command name for this request. **/
	public static final String COMMAND = "command.geometry.Buffer";

	private List<Geometry> geometries;
	
	private double bufferDistance;
	
	private int quadrantSegments;

	/**
	 * Get geometries.
	 * @return geometries
	 */
	public List<Geometry> getGeometries() {
		return geometries;
	}

	/**
	 * Set geometries.
	 * @param geometries
	 */
	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

	/**
	 * Get the buffer distance.
	 * @return distance
	 */
	public double getBufferDistance() {
		return bufferDistance;
	}

	/**
	 * Set the buffer distance.
	 * @param bufferDistance
	 */
	public void setBufferDistance(double bufferDistance) {
		this.bufferDistance = bufferDistance;
	}

	/**
	 * Get the quadrant segments or the number of segments of one quarter of a circle.
	 * @return quadrant segments
	 */
	public int getQuadrantSegments() {
		return quadrantSegments;
	}

	/**
	 * Set the quadrant segments or the number of segments of one quarter of a circle.
	 * @param quadrantSegments
	 */
	public void setQuadrantSegments(int quadrantSegments) {
		this.quadrantSegments = quadrantSegments;
	}

	/**
	 * Get the string representation of this request.
	 * @return string representation of this request
	 */
	@Override
	public String toString() {
		return "GeometryBufferRequest{" +
				"geometries=" + geometries +
				", bufferDistance=" + bufferDistance +
				", quadrantSegments=" + quadrantSegments +
				'}';
	}
}