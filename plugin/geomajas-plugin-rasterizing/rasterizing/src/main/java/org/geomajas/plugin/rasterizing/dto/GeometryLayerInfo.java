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
package org.geomajas.plugin.rasterizing.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.geometry.Geometry;

/**
 * Metadata DTO class that carries sufficient information to render geometries in world space.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryLayerInfo extends LayerInfo {

	private List<Geometry> geometries = new ArrayList<Geometry>();

	private FeatureStyleInfo style;

	/**
	 * Returns the style of the geometries.
	 * 
	 * @return the feature style
	 */
	public FeatureStyleInfo getStyle() {
		return style;
	}

	/**
	 * Sets the style of the geometries.
	 * 
	 * @param style
	 *            the new feature style to apply.
	 */
	public void setStyle(FeatureStyleInfo style) {
		this.style = style;
	}

	/**
	 * Returns the list of geometries to be rendered.
	 * 
	 * @return the list of geometries
	 */
	public List<Geometry> getGeometries() {
		return geometries;
	}

	/**
	 * Sets the list of geometries to be rendered.
	 * 
	 * @param geometries the list of geometries
	 */
	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

}
