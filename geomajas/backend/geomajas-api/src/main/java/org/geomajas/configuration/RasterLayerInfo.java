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

import org.geomajas.layer.LayerType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Information about a raster layer.
 *
 * @author Joachim Van der Auwera
 */
public class RasterLayerInfo extends LayerInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 151L;
	private String dataSourceName;
	private int tileWidth;
	private int tileHeight;
	private String style = "";
	private List<Double> resolutions;

	public RasterLayerInfo() {
		setLayerType(LayerType.RASTER);
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
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

	public RasterLayerInfo clone() {
		/* this is what the method should look like, but GWT cannot handle this (grmbl)
				return (RasterLayerInfo) super.clone();
				*/
		RasterLayerInfo res = (RasterLayerInfo) super.clone(new RasterLayerInfo());
		res.setDataSourceName(dataSourceName);
		res.setTileWidth(tileWidth);
		res.setTileHeight(tileHeight);
		res.setStyle(style);
		res.setResolutions(resolutions);
		return res;
	}

}
