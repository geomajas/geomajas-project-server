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
package org.geomajas.widget.searchandfilter.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request for {@link org.geomajas.widget.searchandfilter.command.searchandfilter.GeometryUtilsCommand}.
 *
 * @author Kristof Heirwegh
 */
public class GeometryUtilsRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND = "command.searchandfilter.GeometryUtils";

	public static final int ACTION_MERGE = 1;

	public static final int ACTION_BUFFER = 2;

	private Geometry[] geometries;

	private double buffer;

	private int bufferQuadrantSegments = 4;

	private int actionFlags;

	/**
	 * Should intermediate results be returned ?
	 * <p>eg. if you set actionFlags to ACTION_MERGE | ACTION_BUFFER:
	 * <p>intermediateResults==false will return only the final merged+buffered geometry.
	 * <p>intermediateResults==true will return the merged geometry + a merged&buffered geometry
	 */
	private boolean intermediateResults;

	// ----------------------------------------------------------

	public boolean isIntermediateResults() {
		return intermediateResults;
	}

	public void setIntermediateResults(boolean intermediateResults) {
		this.intermediateResults = intermediateResults;
	}

	public int getActionFlags() {
		return actionFlags;
	}

	public void setActionFlags(int actionFlags) {
		this.actionFlags = actionFlags;
	}

	public Geometry[] getGeometries() {
		return geometries;
	}

	public void setGeometries(Geometry[] geometries) {
		this.geometries = geometries;
	}

	public double getBuffer() {
		return buffer;
	}

	public void setBuffer(double buffer) {
		this.buffer = buffer;
	}

	public int getBufferQuadrantSegments() {
		return bufferQuadrantSegments;
	}

	public void setBufferQuadrantSegments(int bufferQuadrantSegments) {
		this.bufferQuadrantSegments = bufferQuadrantSegments;
	}

}
