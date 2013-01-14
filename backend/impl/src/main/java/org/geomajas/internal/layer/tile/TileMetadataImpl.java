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

package org.geomajas.internal.layer.tile;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.global.Json;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;

/**
 * Implementation class for {@link TileMetadata}.
 *
 * @author Joachim Van der Auwera
 */
public class TileMetadataImpl implements TileMetadata {

	private String layerId;
	private String crs;
	private TileCode code;
	private double scale;
	private Coordinate panOrigin;
	private String renderer;
	private String filter;
	private NamedStyleInfo styleInfo;
	private boolean paintGeometries;
	private boolean paintLabels;

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public TileCode getCode() {
		return code;
	}

	public void setCode(TileCode code) {
		this.code = code;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public Coordinate getPanOrigin() {
		return panOrigin;
	}

	public void setPanOrigin(Coordinate panOrigin) {
		this.panOrigin = panOrigin;
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public NamedStyleInfo getStyleInfo() {
		return styleInfo;
	}

	public void setStyleInfo(NamedStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

	public boolean isPaintGeometries() {
		return paintGeometries;
	}

	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	public boolean isPaintLabels() {
		return paintLabels;
	}

	public void setPaintLabels(boolean paintLabels) {
		this.paintLabels = paintLabels;
	}

	@Json(serialize = false)
	public int getFeatureIncludes() {
		return GeomajasConstant.FEATURE_INCLUDE_ALL; // for backward compatibility
	}

	public void setFeatureIncludes(int featureIncludes) {
		// no longer needed, deprecated
	}
}
