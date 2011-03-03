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

import org.geomajas.geometry.Bbox;

/**
 * Metadata DTO class that carries sufficient information to render a complete map.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MapMetadata {

	private Bbox bounds;

	private String crs;

	private List<LayerMetadata> layers = new ArrayList<LayerMetadata>();

	private double scale;

	private boolean transparent;

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public Bbox getBounds() {
		return bounds;
	}

	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public List<LayerMetadata> getLayers() {
		return layers;
	}

	public void setLayers(List<LayerMetadata> layers) {
		this.layers = layers;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

}
