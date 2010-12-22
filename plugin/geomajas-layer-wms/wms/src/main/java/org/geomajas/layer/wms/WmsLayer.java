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
package org.geomajas.layer.wms;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.service.DispatcherUrlService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Layer model for accessing raster data from WMS servers. It has support for most WMS versions: up to 1.3.0. When using
 * this layer, note that the following fields are required:
 * <ul>
 * <li><b>baseWmsUrl</b>: The base URL to the WMS server.</li>
 * <li><b>format</b>: The format for the returned images.</li>
 * <li><b>version</b>: The version of WMS to use.</li>
 * <li><b>styles</b>: The styles to use when rendering the WMS images.</li>
 * <li><b>useProxy</b>: Set to true to use a proxy for rendering the WMS, hiding the URL from the client.
 * This automatically happend when setting the authentication object.</li>
 * </ul>
 * There always is the option of adding additional parameters to the WMS GetMap requests, by filling the
 * <code>parameters</code> list. Such parameters could include optional WMS GetMap parameters, such as "transparency",
 * but also "user" and "password".
 * </p>
 * <p>
 * This layer also supports BASIC and DIGEST authentication. To use this functionality, set the
 * <code>authentication</code> field.
 * </p>
 *
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Oliver May
 * @author Joachim Van der Auwera
 * @since 1.7.1
 */
@Api
public class WmsLayer implements RasterLayer {

	private List<Resolution> resolutions = new ArrayList<Resolution>();

	private final Logger log = LoggerFactory.getLogger(WmsLayer.class);

	private String baseWmsUrl, format, version, styles = "";

	private List<Parameter> parameters;


	@Autowired
	private GeoService geoService;

	private RasterLayerInfo layerInfo;

	private CoordinateReferenceSystem crs;

	private String id;

	private WmsAuthentication authentication;

	private boolean useProxy;

	@Autowired
	private DtoConverterService converterService;

	@Autowired(required = false)
	private DispatcherUrlService dispatcherUrlService;

	/**
	 * Return the layers identifier.
	 *
	 * @return layer id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the layer identifier.
	 *
	 * @param id layer id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Return the layer info object.
	 *
	 * @since 1.7.1
	 */
	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	/**
	 * Return this layers coordinate reference system (CRS). This value is initialized when the <code>LayerInfo</code>
	 * object is set.
	 */
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	@PostConstruct
	private void postConstruct() throws GeomajasException {
		crs = geoService.getCrs(getLayerInfo().getCrs());

		// calculate resolutions
		List<ScaleInfo> scales = layerInfo.getZoomLevels();
		if (scales != null) {
			List<Double> r = new ArrayList<Double>();
			for (ScaleInfo scale : scales) {
				r.add(1. / scale.getPixelPerUnit());
			}

			// sort in decreasing order !!!
			Collections.sort(r);
			Collections.reverse(r);

			int level = 0;
			for (double resolution : r) {
				resolutions.add(
						new Resolution(resolution, level++, layerInfo.getTileWidth(), layerInfo.getTileHeight()));
			}
		}
	}

	/**
	 * Set the layer configuration.
	 *
	 * @param layerInfo layer information
	 * @throws LayerException oops
	 * @since 1.7.1
	 */
	@Api
	public void setLayerInfo(RasterLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
	}


	/**
	 * Paints the specified bounds optimized for the specified scale in pixel/unit.
	 *
	 * @param targetCrs Coordinate reference system used for bounds
	 * @param bounds bounds to request images for
	 * @param scale scale or zoom level (unit?)
	 * @return a list of raster images that covers the bounds
	 * @throws GeomajasException oops
	 */
	public List<RasterTile> paint(CoordinateReferenceSystem targetCrs, Envelope bounds, double scale)
			throws GeomajasException {
		Envelope layerBounds = bounds;
		double layerScale = scale;
		MathTransform layerToMap = null;
		boolean needTransform = !crs.equals(targetCrs);

		try {
			// We don't necessarily need to split into same CRS and different CRS cases, the latter implementation uses
			// identity transform if crs's are equal for map and layer but might introduce bugs in rounding and/or
			// conversions.
			if (needTransform) {
				layerToMap = geoService.findMathTransform(crs, targetCrs);
				MathTransform mapToLayer = layerToMap.inverse();

				// Translate the map coordinates to layer coordinates, assumes equal x-y orientation
				layerBounds = geoService.transform(bounds, mapToLayer);
				layerScale = bounds.getWidth() * scale / layerBounds.getWidth();
			}
		} catch (MismatchedDimensionException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_DIMENSION_MISMATCH);
		} catch (TransformException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
		}
		layerBounds = clipBounds(layerBounds);
		if (layerBounds.isNull()) {
			return new ArrayList<RasterTile>(0);
		}

		// Grid is in layer coordinate space!
		Resolution bestResolution = getResolutionForScale(layerScale);
		RasterGrid grid = getRasterGrid(layerBounds, bestResolution.getTileWidth(),
				bestResolution.getTileHeight(), layerScale);

		// We calculate the first tile's screenbox with this assumption
		List<RasterTile> result = new ArrayList<RasterTile>();
		for (int i = grid.getXmin(); i < grid.getXmax(); i++) {
			for (int j = grid.getYmin(); j < grid.getYmax(); j++) {
				double x = grid.getLowerLeft().x + (i - grid.getXmin()) * grid.getTileWidth();
				double y = grid.getLowerLeft().y + (j - grid.getYmin()) * grid.getTileHeight();
				// layer coordinates
				Bbox worldBox;
				Bbox layerBox;
				if (needTransform) {
					layerBox = new Bbox(x, y, grid.getTileWidth(), grid.getTileHeight());
					// Transforming back to map coordinates will only result in a proper grid if the transformation
					// is nearly affine
					worldBox = geoService.transform(layerBox, layerToMap);
				} else {
					worldBox = new Bbox(x, y, grid.getTileWidth(), grid.getTileHeight());
					layerBox = worldBox;
				}
				// Rounding to avoid white space between raster tiles lower-left becomes upper-left in inverted y-space
				Bbox screenbox = new Bbox(Math.round(scale * worldBox.getX()), -Math.round(scale * worldBox.getMaxY()),
						Math.round(scale * worldBox.getMaxX()) - Math.round(scale * worldBox.getX()), Math.round(scale
								* worldBox.getMaxY())
								- Math.round(scale * worldBox.getY()));

				RasterTile image = new RasterTile(screenbox, getId() + "." + bestResolution.getLevel() + "." + i
						+ "," + j);

				image.setCode(new TileCode(bestResolution.getLevel(), i, j));
				String url = formatUrl(bestResolution.getTileWidthPx(), bestResolution.getTileHeightPx(), layerBox);
				image.setUrl(url);
				result.add(image);
			}
		}

		return result;
	}

	private String getWmsTargetUrl() {
		if (useProxy || null != authentication) {
			if (null != dispatcherUrlService) {
				String url = dispatcherUrlService.getDispatcherUrl();
				if (!url.endsWith("/")) {
					url += "/";
				}
				return url + "wms/" + getId() + "/";
			} else {
				return "./d/wms/" + getId() + "/";
			}
		} else {
			return baseWmsUrl;
		}
	}

	private String formatUrl(int width, int height, Bbox box) throws GeomajasException {
		String url = getWmsTargetUrl();
		if (null == url) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "baseWmsUrl");
		}
		int pos = url.lastIndexOf('?');
		if (pos > 0) {
			url += "&SERVICE=WMS";
		} else {
			url += "?SERVICE=WMS";
		}
		url += "&request=GetMap";

		String layers = getId();
		if (layerInfo.getDataSourceName() != null) {
			layers = layerInfo.getDataSourceName();
		}
		url += "&layers=" + layers;
		url += "&WIDTH=" + width;
		url += "&HEIGHT=" + height;

		DecimalFormat decimalFormat = new DecimalFormat(); // create new as this is not threadsafe
		decimalFormat.setDecimalSeparatorAlwaysShown(false);
		decimalFormat.setGroupingUsed(false);
		decimalFormat.setMinimumFractionDigits(0);
		decimalFormat.setMaximumFractionDigits(100);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(symbols);

		url += "&bbox=" + decimalFormat.format(box.getX()) + "," + decimalFormat.format(box.getY()) + ","
				+ decimalFormat.format(box.getMaxX()) + "," + decimalFormat.format(box.getMaxY());
		url += "&format=" + format;
		url += "&version=" + version;
		if ("1.3.0".equals(version)) {
			url += "&crs=" + layerInfo.getCrs();
		} else {
			url += "&srs=" + layerInfo.getCrs();
		}
		url += "&styles=" + styles;
		if (null != parameters) {
			for (Parameter p : parameters) {
				url += "&" + p.getName() + "=" + p.getValue();
			}
		}
		log.debug(url);
		return url;
	}

	private Resolution getResolutionForScale(double scale) {
		if (null == resolutions || resolutions.size() == 0) {
			return calculateBestQuadTreeResolution(scale);
		} else {
			double screenResolution = 1.0 / scale;
			if (screenResolution >= resolutions.get(0).getResolution()) {
				return resolutions.get(0);
			} else if (screenResolution <= resolutions.get(resolutions.size() - 1).getResolution()) {
				return resolutions.get(resolutions.size() - 1);
			} else {
				for (int i = 0; i < resolutions.size() - 1; i++) {
					Resolution upper = resolutions.get(i);
					Resolution lower = resolutions.get(i + 1);
					if (screenResolution <= upper.getResolution() && screenResolution >= lower.getResolution()) {
						if ((upper.getResolution() - screenResolution) >
							2 * (screenResolution - lower.getResolution())) {
							return lower;
						} else {
							return upper;
						}
					}
				}
			}
		}
		// should not occur !!!!
		return resolutions.get(resolutions.size() - 1);
	}

	private Resolution calculateBestQuadTreeResolution(double scale) {
		double screenResolution = 1.0 / scale;
		// based on quad tree created by subdividing the maximum extent
		Bbox bbox = layerInfo.getMaxExtent();
		double maxWidth = bbox.getWidth();
		double maxHeight = bbox.getHeight();

		int tileWidth = layerInfo.getTileWidth();
		int tileHeight = layerInfo.getTileHeight();

		Resolution upper = new Resolution(Math.max(maxWidth / tileWidth, maxHeight / tileHeight), 0, tileWidth,
				tileHeight);
		if (screenResolution >= upper.getResolution()) {
			return upper;
		} else {
			int level = 0;
			Resolution lower = upper; // set value to avoid possible NPE
			while (screenResolution < upper.getResolution()) {
				lower = upper;
				level++;
				double width = maxWidth / Math.pow(2, level);
				double height = maxHeight / Math.pow(2, level);
				upper = new Resolution(Math.max(width / tileWidth, height / tileHeight), level, tileWidth,
						tileHeight);
			}
			if ((upper.getResolution() - screenResolution) > 2 * (screenResolution - lower.getResolution())) {
				return lower;
			} else {
				return upper;
			}
		}
	}

	private RasterGrid getRasterGrid(Envelope bounds, double width, double height, double scale) {
		// slightly adjust the width and height so it becomes integer for the
		// current scale
		double realWidth = ((int) (width * scale)) / scale;
		double realHeight = ((int) (height * scale)) / scale;

		Bbox bbox = getLayerInfo().getMaxExtent();
		int ymin = (int) Math.floor((bounds.getMinY() - bbox.getY()) / realHeight);
		int ymax = (int) Math.floor((bounds.getMaxY() - bbox.getY()) / realHeight) + 1;
		int xmin = (int) Math.floor((bounds.getMinX() - bbox.getX()) / realWidth);
		int xmax = (int) Math.floor((bounds.getMaxX() - bbox.getX()) / realWidth) + 1;
		// same adjustment for corner
		double realXmin = ((int) (bbox.getX() * scale)) / scale;
		double realYmin = ((int) (bbox.getY() * scale)) / scale;
		Coordinate lowerLeft = new Coordinate(realXmin + xmin * realWidth, realYmin + ymin * realHeight);
		return new RasterGrid(lowerLeft, xmin, ymin, xmax, ymax, realWidth, realHeight);
	}

	private Envelope clipBounds(Envelope bounds) {
		Envelope maxExtent = converterService.toInternal(layerInfo.getMaxExtent());
		return bounds.intersection(maxExtent);
	}

	String getBaseWmsUrl() {
		return baseWmsUrl;
	}

	/**
	 * Set the base URL to the WMS server.
	 *
	 * @param baseWmsUrl base URL for the WMS server
	 * @since 1.7.1
	 */
	@Api
	public void setBaseWmsUrl(String baseWmsUrl) {
		this.baseWmsUrl = baseWmsUrl;
	}

	String getFormat() {
		return format;
	}

	/**
	 * Set file format to request.
	 *
	 * @param format file format. For allowed values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Set WMS version to use.
	 *
	 * @param version WMS version. For allowed values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Set the styles.
	 *
	 * @param styles styles. For allowed values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setStyles(String styles) {
		this.styles = styles;
	}

	/**
	 * Set additional parameters to include in all WMS <code>getMap</code> requests.
	 *
	 * @param parameters parameters. For possible keys and values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Get the authentication object.
	 *
	 * @return authentication object
	 */
	public WmsAuthentication getAuthentication() {
		return authentication;
	}

	/**
	 * <p>
	 * Set the authentication object. This configuration object provides support for basic and digest HTTP
	 * authentication on the WMS server. If no HTTP authentication is required, leave this empty.
	 * </p>
	 * <p>
	 * Note that there is still the option of adding a user name and password as HTTP parameters, as some WMS server
	 * support. To do that, just add <code>parameters</code>.
	 * </p>
	 *
	 * @param authentication authentication object
	 * @since 1.8.0
	 */
	@Api
	public void setAuthentication(WmsAuthentication authentication) {
		this.authentication = authentication;
	}

	/**
	 * Set whether the WMS request should use a proxy. This is automatically done when the authentication object is set.
	 * When the WMS request is proxied, the credentials and WMS base address are hidden from the client.
	 *
	 * @param useProxy true when request needs to use the proxy
	 * @since 1.8.0
	 */
	@Api
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	/**
	 * Grid definition for a WMS layer. It is used internally in the WMS layer.
	 *
	 * @author Jan De Moerloose
	 * @author Pieter De Graef
	 */
	private class RasterGrid {

		private Coordinate lowerLeft;

		private int xmin;

		private int ymin;

		private int xmax;

		private int ymax;

		private double tileWidth;

		private double tileHeight;

		RasterGrid(Coordinate lowerLeft, int xmin, int ymin, int xmax, int ymax, double tileWidth, double tileHeight) {
			super();
			this.lowerLeft = lowerLeft;
			this.xmin = xmin;
			this.ymin = ymin;
			this.xmax = xmax;
			this.ymax = ymax;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
		}

		public Coordinate getLowerLeft() {
			return lowerLeft;
		}

		public double getTileHeight() {
			return tileHeight;
		}

		public double getTileWidth() {
			return tileWidth;
		}

		public int getXmax() {
			return xmax;
		}

		public int getXmin() {
			return xmin;
		}

		public int getYmax() {
			return ymax;
		}

		public int getYmin() {
			return ymin;
		}
	}

	/**
	 * Single resolution definition for a WMS layer. This class is used internally in the WMS layer, and therefore has
	 * no public constructors.
	 *
	 * @author Jan De Moerloose
	 * @author Pieter De Graef
	 */
	private class Resolution {

		private double resolution;

		private int level;

		private int tileWidth;

		private int tileHeight;

		/**
		 * Constructor that immediately requires all fields.
		 *
		 * @param resolution The actual resolution value. This is the reverse of the scale.
		 * @param level The level in the quad tree.
		 * @param tileWidth The width of a tile at the given tile level.
		 * @param tileHeight The height of a tile at the given tile level.
		 */
		Resolution(double resolution, int level, int tileWidth, int tileHeight) {
			this.resolution = resolution;
			this.level = level;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
		}

		public int getLevel() {
			return level;
		}

		public int getTileHeightPx() {
			return tileHeight;
		}

		public int getTileWidthPx() {
			return tileWidth;
		}

		public double getTileHeight() {
			return tileHeight * resolution;
		}

		public double getTileWidth() {
			return tileWidth * resolution;
		}

		public double getResolution() {
			return resolution;
		}
	}
}