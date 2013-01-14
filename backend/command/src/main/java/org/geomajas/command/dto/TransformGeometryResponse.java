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
import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;

/**
 * Response object for {@link org.geomajas.command.geometry.TransformGeometryCommand}.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
public class TransformGeometryResponse extends CommandResponse {

	private static final long serialVersionUID = 190L;

	private Geometry geometry;

	private List<Geometry> geometryCollection = new ArrayList<Geometry>();

	private Bbox bounds;

	/**
	 * Get transformed geometry.
	 *
	 * @return transformed geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Set transformed geometry.
	 *
	 * @param geometry transformed geometry
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Get transformed geometry collection.
	 *
	 * @return transformed geometry collection
	 */
	public List<Geometry> getGeometryCollection() {
		return geometryCollection;
	}

	/**
	 * Set transformed geometry collection.
	 *
	 * @param geometryCollection transformed geometry collection
	 */
	public void setGeometryCollection(List<Geometry> geometryCollection) {
		this.geometryCollection = geometryCollection;
	}

	/**
	 * Get transformed bounds.
	 *
	 * @return transformed bounds
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Set transformed bounds.
	 *
	 * @param bounds transformed bounds.
	 */
	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	@Override
	public String toString() {
		return "TransformGeometryResponse{" +
				"geometry=" + geometry +
				", geometryCollection=" + geometryCollection +
				", bounds=" + bounds +
				'}';
	}
}
