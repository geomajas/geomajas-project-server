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

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.TransformGeometryCommand}.
 * 
 * @author Jan De Moerloose
 */
public class TransformGeometryRequest implements CommandRequest {

	private Geometry geometry;

	private List<Geometry> geometryCollection = new ArrayList<Geometry>();

	private Bbox bounds;

	private String sourceCrs;

	private String targetCrs;

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

	public String getSourceCrs() {
		return sourceCrs;
	}

	public void setSourceCrs(String sourceCrs) {
		this.sourceCrs = sourceCrs;
	}

	public String getTargetCrs() {
		return targetCrs;
	}

	public void setTargetCrs(String targetCrs) {
		this.targetCrs = targetCrs;
	}

}
