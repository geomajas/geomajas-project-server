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

	private double[] resolutions;

	public TiledRasterLayerServiceState(List<String> tileUrls, int tileSize, int maxZoomLevel) {
		setTileUrls(tileUrls);
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
		urlStrategy.setUrls(tileUrls);
	}

	public List<String> getTileUrls() {
		return tileUrls;
	}

	public void setTileUrls(List<String> tileUrls) {
		this.tileUrls = tileUrls;
		if (null != urlStrategy) {
			urlStrategy.setUrls(tileUrls);
		}
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
		crs = geoService.getCrs2(TiledRasterLayerService.MERCATOR);
		layerInfo.setTileWidth(tileSize);
		layerInfo.setTileHeight(tileSize);
		Bbox bbox = new Bbox(-20026376.393709917, -20026376.393709917, 40052752.787419834, 40052752.787419834);
		layerInfo.setMaxExtent(bbox);
		maxBounds = converterService.toInternal(bbox);

		resolutions = new double[maxZoomLevel + 1];
		double powerOfTwo = 1;
		for (int zoomLevel = 0; zoomLevel <= maxZoomLevel; zoomLevel++) {
			double resolution = (TiledRasterLayerService.EQUATOR_IN_METERS) / (tileSize * powerOfTwo);
			resolutions[zoomLevel] = resolution;
			powerOfTwo *= 2;
		}
	}

}
