/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.annotation.Api;
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

	/**
	 * Create base object.
	 *
	 * @param tileUrls tile URLs
	 * @param tileSize tile size in pixels
	 * @param maxZoomLevel maximum zoom level
	 */
	public TiledRasterLayerServiceState(List<String> tileUrls, int tileSize, int maxZoomLevel) {
		setTileUrls(tileUrls);
		this.tileSize = tileSize;
		this.maxZoomLevel = maxZoomLevel;
	}

	/**
	 * Get layer id.
	 *
	 * @return layer id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set layer id.
	 *
	 * @param id layer id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get layer configuration.
	 *
	 * @return layer configuration
	 */
	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	/**
	 * Set layer configuration.
	 *
	 * @param layerInfo layer configuration
	 */
	public void setLayerInfo(RasterLayerInfo layerInfo) {
		this.layerInfo = layerInfo;
	}

	/**
	 * Get layer CRS.
	 *
	 * @return CRS
	 */
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * Set layer CRS.
	 *
	 * @param crs CRS
	 */
	public void setCrs(CoordinateReferenceSystem crs) {
		this.crs = crs;
	}

	/**
	 * Get maximum zoom level.
	 *
	 * @return maximum zoom level
	 */
	public int getMaxZoomLevel() {
		return maxZoomLevel;
	}

	/**
	 * Set maximum zoom level.
	 *
	 * @param maxZoomLevel maximum zoom level
	 */
	public void setMaxZoomLevel(int maxZoomLevel) {
		this.maxZoomLevel = maxZoomLevel < TiledRasterLayerService.MAX_ZOOM_LEVEL ? maxZoomLevel :
				TiledRasterLayerService.MAX_ZOOM_LEVEL;
	}

	/**
	 * Get URL selection strategy.
	 *
	 * @return URL selection strategy
	 */
	public UrlSelectionStrategy getUrlSelectionStrategy() {
		return urlStrategy;
	}

	/**
	 * Set URL selection strategy.
	 *
	 * @param urlStrategy URL selection strategy
	 */
	public void setUrlSelectionStrategy(UrlSelectionStrategy urlStrategy) {
		this.urlStrategy = urlStrategy;
		urlStrategy.setUrls(tileUrls);
	}

	/**
	 * Get possible tile URLs.
	 *
	 * @return tile URLs
	 */
	public List<String> getTileUrls() {
		return tileUrls;
	}

	/**
	 * Set possible tile URLs.
	 *
	 * @param tileUrls tile URLs
	 */
	public void setTileUrls(List<String> tileUrls) {
		this.tileUrls = tileUrls;
		if (null != urlStrategy) {
			urlStrategy.setUrls(tileUrls);
		}
	}

	/**
	 * Get the layer maximum bounds.
	 *
	 * @return layer maximum bounds
	 */
	public Envelope getMaxBounds() {
		return maxBounds;
	}

	/**
	 * Set the layer maximum bounds.
	 *
	 * @return layer maximum bounds
	 */
	public double[] getResolutions() {
		return resolutions;
	}

	/**
	 * Finish initialization of state object.
	 *
	 * @param geoService geo service
	 * @param converterService converter service
	 * @throws GeomajasException oops
	 */
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
