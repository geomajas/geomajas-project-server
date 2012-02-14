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


package org.geomajas.sld.client.model;

import org.geomajas.sld.editor.client.GeometryType;

/**
 * 
 * @author An Buyle
 *
 */

public class SldGeneralInfo {

	private String nameOfLayer;

	private String styleTitle;

	private final GeometryType geomType; // Cannot be updated

	public SldGeneralInfo(GeometryType geomType) {
		this.geomType = geomType;
	}

	public String getNameOfLayer() {
		return nameOfLayer;
	}

	public void setNameOfLayer(String nameOfLayer) {
		this.nameOfLayer = nameOfLayer;
	}

	public String getStyleTitle() {
		return styleTitle;
	}

	public void setStyleTitle(String styleTitle) {
		this.styleTitle = styleTitle;
	}

	public GeometryType getGeomType() {
		return geomType;
	}

}
