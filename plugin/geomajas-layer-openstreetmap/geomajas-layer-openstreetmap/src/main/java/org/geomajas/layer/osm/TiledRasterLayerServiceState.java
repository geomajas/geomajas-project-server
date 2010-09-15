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

package org.geomajas.layer.osm;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

/**
 * State container for {@link TiledRasterLayerService}.
 *
 * @author Joachim Van der Auwera
 */
public class TiledRasterLayerServiceState {

	private String id;

	private int tileWidth;

	private int tileHeight;

	private double maxWidth;

	private double maxHeight;

	private RasterLayerInfo layerInfo;

	private CoordinateReferenceSystem crs;

	private int maxZoomLevel;

	private TileUrlBuilder urlBuilder = new RoundRobinTileUrlBuilder();

	private List<String> baseUrls;

	private Envelope maxBounds;

	protected double[] resolutions;


	public TiledRasterLayerServiceState() {
	}

	public TiledRasterLayerServiceState(int tileSize, int maxZoomLevel) {
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.maxZoomLevel = maxZoomLevel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public double getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
	}

	public double getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}

	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public void setLayerInfo(RasterLayerInfo layerInfo) {
		this.layerInfo = layerInfo;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public void setCrs(CoordinateReferenceSystem crs) {
		this.crs = crs;
	}

	public int getMaxZoomLevel() {
		return maxZoomLevel;
	}

	public void setMaxZoomLevel(int maxZoomLevel) {
		this.maxZoomLevel = maxZoomLevel < TiledRasterLayerService.MAX_ZOOM_LEVEL ? maxZoomLevel :
				TiledRasterLayerService.MAX_ZOOM_LEVEL;
	}

	public TileUrlBuilder getUrlBuilder() {
		return urlBuilder;
	}

	public void setUrlBuilder(TileUrlBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

	public List<String> getBaseUrls() {
		return baseUrls;
	}

	public void setBaseUrls(List<String> baseUrls) {
		this.baseUrls = baseUrls;
	}

	public Envelope getMaxBounds() {
		return maxBounds;
	}

	public double[] getResolutions() {
		return resolutions;
	}

	public void postConstruct(GeoService geoService, DtoConverterService converterService) throws Exception {
		if (null == layerInfo) {
			layerInfo = new RasterLayerInfo();
		}
		layerInfo.setCrs(TiledRasterLayerService.MERCATOR);
		crs = geoService.getCrs(TiledRasterLayerService.MERCATOR);
		layerInfo.setTileWidth(tileWidth);
		layerInfo.setTileHeight(tileHeight);
		Bbox bbox = new Bbox(-20026376.393709917, -20026376.393709917, 40052752.787419834, 40052752.787419834);
		layerInfo.setMaxExtent(bbox);
		maxBounds = converterService.toInternal(bbox);

		if (null != baseUrls) {
			RoundRobinTileUrlBuilder rr = new RoundRobinTileUrlBuilder();
			rr.setBaseUrls(baseUrls);
			setUrlBuilder(rr);
		}

		resolutions = new double[maxZoomLevel + 1];
		double powerOfTwo = 1;
		for (int zoomLevel = 0; zoomLevel <= maxZoomLevel; zoomLevel++) {
			double resolution = (TiledRasterLayerService.EQUATOR_IN_METERS) / (tileWidth * powerOfTwo);
			resolutions[zoomLevel] = resolution;
			powerOfTwo *= 2;
		}

	}

}
