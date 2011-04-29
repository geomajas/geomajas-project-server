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
package org.geomajas.command.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;

/**
 * Response object for {@link org.geomajas.command.geometry.TransformGeometryCommand}.
 * 
 * @author Jan De Moerloose
 */
public class TransformGeometryResponse extends CommandResponse {

	private static final long serialVersionUID = 190L;

	private Geometry geometry;

	private List<Geometry> geometryCollection = new ArrayList<Geometry>();

	private Bbox bounds;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public List<Geometry> getGeometryCollection() {
		return geometryCollection;
	}

	public void setGeometryCollection(List<Geometry> geometryCollection) {
		this.geometryCollection = geometryCollection;
	}

	public Bbox getBounds() {
		return bounds;
	}

	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

}
