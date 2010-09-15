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
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

/**
 * State container for {@link TiledRasterLayerService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public class TiledRasterLayerServiceState {

	private String id;

	private int tileSize;

	private RasterLayerInfo layerInfo;

	private CoordinateReferenceSystem crs;

	private int maxZoomLevel;

	private UrlSelectionStrategy urlStrategy = new RoundRobinUrlSelectionStrategy();

	private List<String> tileUrls;

	private Envelope maxBounds;

	protected double[] resolutions;


	public TiledRasterLayerServiceState(List<String> tileUrls, int tileSize, int maxZoomLevel) {
		this.tileUrls = tileUrls;
		this.tileSize = tileSize;
		this.maxZoomLevel = maxZoomLevel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public UrlSelectionStrategy getUrlSelectionStrategy() {
		return urlStrategy;
	}

	public void setUrlSelectionStrategy(UrlSelectionStrategy urlStrategy) {
		this.urlStrategy = urlStrategy;
	}

	public List<String> getTileUrls() {
		return tileUrls;
	}

	public void setTileUrls(List<String> tileUrls) {
		this.tileUrls = tileUrls;
	}

	public Envelope getMaxBounds() {
		return maxBounds;
	}

	public double[] getResolutions() {
		return resolutions;
	}

	public void postConstruct(GeoService geoService, DtoConverterService converterService) throws GeomajasException {
		if (null == layerInfo) {
			layerInfo = new RasterLayerInfo();
		}
		layerInfo.setCrs(TiledRasterLayerService.MERCATOR);
		crs = geoService.getCrs(TiledRasterLayerService.MERCATOR);
		layerInfo.setTileWidth(tileSize);
		layerInfo.setTileHeight(tileSize);
		Bbox bbox = new Bbox(-20026376.393709917, -20026376.393709917, 40052752.787419834, 40052752.787419834);
		layerInfo.setMaxExtent(bbox);
		maxBounds = converterService.toInternal(bbox);

		urlStrategy.setUrls(tileUrls);

		resolutions = new double[maxZoomLevel + 1];
		double powerOfTwo = 1;
		for (int zoomLevel = 0; zoomLevel <= maxZoomLevel; zoomLevel++) {
			double resolution = (TiledRasterLayerService.EQUATOR_IN_METERS) / (tileSize * powerOfTwo);
			resolutions[zoomLevel] = resolution;
			powerOfTwo *= 2;
		}
	}

}
