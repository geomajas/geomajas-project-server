/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.configuration;

import org.geomajas.geometry.Bbox;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Map configuration.
 *
 * @author Joachim Van der Auwera
 */
public class MapInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 151L;

	private String id;

	private String backgroundColor;

	private StyleInfo lineSelectStyle;

	private StyleInfo pointSelectStyle;

	private StyleInfo polygonSelectStyle;

	private String crs;

	private int precision = 2;

	private boolean scaleBarEnabled;

	private boolean panButtonsEnabled;

	private UnitType displayUnitType = UnitType.METRIC;

	private String overview;

	private float maximumScale;

	private Bbox initialBounds;

	private List<Double> resolutions;

	private boolean resolutionsRelative;

	private List<LayerInfo> layers;

	private LayerTreeInfo layerTree;

	private double unitLength = 1.0;

	private double pixelLength;

	private ToolbarInfo toolbar;

	private static double METER_PER_INCH = 0.0254;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public StyleInfo getLineSelectStyle() {
		return lineSelectStyle;
	}

	public void setLineSelectStyle(StyleInfo lineSelectStyle) {
		this.lineSelectStyle = lineSelectStyle;
	}

	public StyleInfo getPointSelectStyle() {
		return pointSelectStyle;
	}

	public void setPointSelectStyle(StyleInfo pointSelectStyle) {
		this.pointSelectStyle = pointSelectStyle;
	}

	public StyleInfo getPolygonSelectStyle() {
		return polygonSelectStyle;
	}

	public void setPolygonSelectStyle(StyleInfo polygonSelectStyle) {
		this.polygonSelectStyle = polygonSelectStyle;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isScaleBarEnabled() {
		return scaleBarEnabled;
	}

	public void setScaleBarEnabled(boolean scaleBarEnabled) {
		this.scaleBarEnabled = scaleBarEnabled;
	}

	public boolean isPanButtonsEnabled() {
		return panButtonsEnabled;
	}

	public void setPanButtonsEnabled(boolean panButtonsEnabled) {
		this.panButtonsEnabled = panButtonsEnabled;
	}

	public UnitType getDisplayUnitType() {
		return displayUnitType;
	}

	public void setDisplayUnitType(UnitType displayUnitType) {
		this.displayUnitType = displayUnitType;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public float getMaximumScale() {
		return maximumScale;
	}

	public void setMaximumScale(float maximumScale) {
		this.maximumScale = maximumScale;
	}

	public Bbox getInitialBounds() {
		return initialBounds;
	}

	public void setInitialBounds(Bbox initialBounds) {
		this.initialBounds = initialBounds;
	}

	public List<Double> getResolutions() {
		if (null == resolutions) {
			resolutions = new ArrayList<Double>();
		}
		return resolutions;
	}

	public void setResolutions(List<Double> resolutions) {
		this.resolutions = resolutions;
	}

	public boolean isResolutionsRelative() {
		return resolutionsRelative;
	}

	public void setResolutionsRelative(boolean resolutionsRelative) {
		this.resolutionsRelative = resolutionsRelative;
	}

	public LayerTreeInfo getLayerTree() {
		return layerTree;
	}

	public void setLayerTree(LayerTreeInfo layerTree) {
		this.layerTree = layerTree;
	}

	public ToolbarInfo getToolbar() {
		return toolbar;
	}

	public void setToolbar(ToolbarInfo toolbar) {
		this.toolbar = toolbar;
	}

	public List<LayerInfo> getLayers() {
		if (null == layers) {
			layers = new ArrayList<LayerInfo>();
		}
		return layers;
	}

	public void setLayers(List<LayerInfo> layers) {
		this.layers = layers;
	}

	public double getUnitLength() {
		return unitLength;
	}

	public void setUnitLength(double unitLength) {
		this.unitLength = unitLength;
	}

	public double getPixelLength() {
		return pixelLength;
	}

	public void setPixelLength(double pixelLength) {
		this.pixelLength = pixelLength;
	}

	public MapInfo clone() {
		/*
		 * this is what the method should look like, but GWT cannot handle this (grmbl) try { return (MapInfo)
		 * super.clone(); } catch (CloneNotSupportedException cns) { throw new
		 * RuntimeException("Oops, impossible exception LayerInfo is cloneable!"); }
		 */
		MapInfo res = new MapInfo();
		res.setId(id);
		res.setBackgroundColor(backgroundColor);
		res.setLineSelectStyle(lineSelectStyle);
		res.setPointSelectStyle(pointSelectStyle);
		res.setPolygonSelectStyle(polygonSelectStyle);
		res.setCrs(crs);
		res.setPrecision(precision);
		res.setScaleBarEnabled(scaleBarEnabled);
		res.setPanButtonsEnabled(panButtonsEnabled);
		res.setDisplayUnitType(displayUnitType);
		res.setOverview(overview);
		res.setMaximumScale(maximumScale);
		res.setInitialBounds(initialBounds);
		res.setResolutions(resolutions);
		res.setResolutionsRelative(resolutionsRelative);
		res.setLayers(layers);
		res.setLayerTree(layerTree);
		res.setToolbar(toolbar);
		res.setUnitLength(unitLength);
		return res;
	}

}
