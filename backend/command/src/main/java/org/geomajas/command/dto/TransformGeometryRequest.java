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
package org.geomajas.command.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.TransformGeometryCommand}.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
public class TransformGeometryRequest implements CommandRequest {

	private static final long serialVersionUID = 190L;

	/**
	 * Command name for this request.
	 * */
	public static final String COMMAND = "command.geometry.Transform";

	private Geometry geometry;

	private List<Geometry> geometryCollection = new ArrayList<Geometry>();

	private Bbox bounds;

	private String sourceCrs;

	private String targetCrs;

	/**
	 * Get geometry to transform.
	 *
	 * @return geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Set geometry to transform.
	 *
	 * @param geometry geometry
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Get list of geometries to transform.
	 *
	 * @return list of geometries
	 */
	public List<Geometry> getGeometryCollection() {
		return geometryCollection;
	}

	/**
	 * Set list of geometries to transform.
	 *
	 * @param geometryCollection collection of geometries
	 */
	public void setGeometryCollection(List<Geometry> geometryCollection) {
		this.geometryCollection = geometryCollection;
	}

	/**
	 * Get bounds to transform.
	 *
	 * @return bbox
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Set bounds to transform.
	 *
	 * @param bounds bbox
	 */
	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	/**
	 * Get source CRS.
	 *
	 * @return source CRS
	 */
	public String getSourceCrs() {
		return sourceCrs;
	}

	/**
	 * Set source CRS.
	 *
	 * @param sourceCrs source CRS
	 */
	public void setSourceCrs(String sourceCrs) {
		this.sourceCrs = sourceCrs;
	}

	/**
	 * Get target CRS.
	 *
	 * @return target CRS
	 */
	public String getTargetCrs() {
		return targetCrs;
	}

	/**
	 * Set target CRS.
	 *
	 * @param targetCrs target CRS
	 */
	public void setTargetCrs(String targetCrs) {
		this.targetCrs = targetCrs;
	}

	@Override
	public String toString() {
		return "TransformGeometryRequest{" +
				"geometry=" + geometry +
				", geometryCollection=" + geometryCollection +
				", bounds=" + bounds +
				", sourceCrs='" + sourceCrs + '\'' +
				", targetCrs='" + targetCrs + '\'' +
				'}';
	}
}
