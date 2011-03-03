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
import org.geomajas.geometry.Geometry;
import org.geomajas.layer.LayerType;

/**
 * Metadata DTO class that carries sufficient information to render geometries in world space.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GeometryLayerMetadata implements LayerMetadata {

	private List<Geometry> geometries = new ArrayList<Geometry>();

	private FeatureStyleInfo style;

	private String layerId;

	private LayerType layerType;

	public FeatureStyleInfo getStyle() {
		return style;
	}

	public void setStyle(FeatureStyleInfo style) {
		this.style = style;
	}

	public List<Geometry> getGeometries() {
		return geometries;
	}

	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public LayerType getLayerType() {
		return layerType;
	}

	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

}
