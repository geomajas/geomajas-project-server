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

package org.geomajas.plugin.wmsclient.printing.server.dto;

import java.io.Serializable;

import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;

/**
 * Necessary info representing a selected feature.
 * 
 * @author An Buyle
 */
public class InfoSelectedFeature implements Serializable {

	private static final long serialVersionUID = 100L;

	private Geometry geometry;

	private String id;

	public InfoSelectedFeature() {
		geometry = new Geometry();
	}

	public InfoSelectedFeature(Geometry geometry, String id) {
		this.geometry = GeometryService.clone(geometry);
		this.id = id;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = GeometryService.clone(geometry);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}