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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Layer model for accessing raster data from WMS servers.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @since 1.7.1
 */
@Api
public class WmsLayer implements RasterLayer {

	protected List<Resolution> resolutions = new ArrayList<Resolution>();

	private final Logger log = LoggerFactory.getLogger(WmsLayer.class);

	private DecimalFormat decimalFormat = new DecimalFormat();

	private String baseWmsUrl, format, version, styles = "";

	private List<Parameter> parameters;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	private RasterLayerInfo layerInfo;

	private CoordinateReferenceSystem crs;

	private String id;

	private String layers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	private void initCrs() throws LayerException {
		try {
			crs = CRS.decode(layerInfo.getCrs());
		} catch (NoSuchAuthorityCodeException e) {
			throw new LayerException(e, ExceptionCode.LAYER_CRS_UNKNOWN_AUTHORITY, getId(), getLayerInfo().getCrs());
		} catch (FactoryException exception) {
			throw new LayerException(exception, ExceptionCode.LAYER_CRS_PROBLEMATIC, getId(), getLayerInfo().getCrs());
		}
	}

	public Envelope getMaxBounds() {
		return converterService.toInternal(layerInfo.getMaxExtent());
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
		initCrs();
		decimalFormat.setDecimalSeparatorAlwaysShown(false);
		decimalFormat.setGroupingUsed(false);
		decimalFormat.setMinimumFractionDigits(0);
		decimalFormat.setMaximumFractionDigits(100);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(symbols);
		layers = getId();
		if (layerInfo.getDataSourceName() != null) {
			layers = layerInfo.getDataSourceName();
		}
		List<Double> r = layerInfo.getResolutions();
		if (r != null) {
			calculatePredefinedResolutions(r);
		}
	}

	private void calculatePredefinedResolutions(List<Double> r) {
		// sort in decreasing order !!!
		Collections.sort(r);
		Collections.reverse(r);
		int level = 0;
		for (double resolution : r) {
			resolutions.add(new Resolution(resolution, level++, getTileWidth(), getTileHeight()));
		}
	}

	@SuppressWarnings("unchecked")
	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope orgBounds, double scale)
			throws GeomajasException {
		List<RasterTile> result = new ArrayList<RasterTile>();
		Envelope bounds = orgBounds;

		// We don't necessarily need to split into same crs and different crs
		// cases,
		// the latter implementation uses identity transform if crs's are equal
		// for map and layer
		// but might introduce bugs in rounding and/or conversions.
		if (crs.equals(boundsCrs)) {
			bounds = clipBounds(bounds);
			if (bounds.isNull()) {
				return Collections.EMPTY_LIST;
			}
			Resolution bestResolution = calculateBestResolution(scale);

			RasterGrid grid = getRasterGrid(bounds, bestResolution.getTileWidth(), bestResolution.getTileHeight(),
					scale);

			for (int i = grid.getXmin(); i < grid.getXmax(); i++) {
				for (int j = grid.getYmin(); j < grid.getYmax(); j++) {
					double x = grid.getLowerLeft().x + (i - grid.getXmin()) * grid.getTileWidth();
					double y = grid.getLowerLeft().y + (j - grid.getYmin()) * grid.getTileHeight();
					Bbox worldBox = new Bbox(x, y, grid.getTileWidth(), grid.getTileHeight());
					// lower-left becomes upper-left in inverted y-space !!!
					Bbox screenbox = new Bbox(Math.round(scale * worldBox.getX()), -Math.round(scale
							* worldBox.getMaxY()), Math.round(scale * worldBox.getMaxX())
							- Math.round(scale * worldBox.getX()), Math.round(scale * worldBox.getMaxY())
							- Math.round(scale * worldBox.getY()));

					RasterTile image = new RasterTile(screenbox, getId() + "." + bestResolution.getLevel() + "." + i
							+ "," + j);

					image.setCode(new TileCode(bestResolution.getLevel(), i, j));
					resolveUrl(image, bestResolution.getTileWidthPx(), bestResolution.getTileHeightPx(), worldBox);
					result.add(image);
				}
			}
		} else {
			try {
				MathTransform layerToMap = geoService.findMathTransform(CRS.decode(getLayerInfo().getCrs()), boundsCrs);
				MathTransform mapToLayer = layerToMap.inverse();

				// Translate the map coordinates to layer coordinates, assumes equal x-y orientation
				Envelope layerBounds = transformBounds(bounds, mapToLayer);
				double layerScale = bounds.getWidth() * scale / layerBounds.getWidth();

				layerBounds = clipBounds(layerBounds);
				if (layerBounds.isNull()) {
					return Collections.EMPTY_LIST;
				}

				// Grid is in layer coordinate space!
				Resolution bestResolution = calculateBestResolution(layerScale);
				RasterGrid grid = getRasterGrid(layerBounds, bestResolution.getTileWidth(), bestResolution
						.getTileHeight(), layerScale);

				// We calculate the first tile's screenbox with this assumption

				for (int i = grid.getXmin(); i < grid.getXmax(); i++) {
					for (int j = grid.getYmin(); j < grid.getYmax(); j++) {
						double x = grid.getLowerLeft().x + (i - grid.getXmin()) * grid.getTileWidth();
						double y = grid.getLowerLeft().y + (j - grid.getYmin()) * grid.getTileHeight();
						// layer coordinates
						Bbox layerBox = new Bbox(x, y, grid.getTileWidth(), grid.getTileHeight());
						// Transforming back to map coordinates will only result in a proper grid if the transformation
						// is nearly affine
						Bbox worldBox = transformBounds(layerBox, layerToMap);
						// Rounding to avoid white space between raster tiles
						// lower-left becomes upper-left in inverted y-space !!!
						Bbox screenbox = new Bbox(Math.round(scale * worldBox.getX()), -Math.round(scale
								* worldBox.getMaxY()), Math.round(scale * worldBox.getMaxX())
								- Math.round(scale * worldBox.getX()), Math.round(scale * worldBox.getMaxY())
								- Math.round(scale * worldBox.getY()));

						RasterTile image = new RasterTile(screenbox, getId() + "." + bestResolution.getLevel() + "."
								+ i + "," + j);

						image.setCode(new TileCode(bestResolution.getLevel(), i, j));
						resolveUrl(image, bestResolution.getTileWidthPx(), bestResolution.getTileHeightPx(), layerBox);
						result.add(image);
					}
				}
			} catch (MismatchedDimensionException e) {
				throw new GeomajasException(e, ExceptionCode.RENDER_DIMENSION_MISMATCH);
			} catch (TransformException e) {
				throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
			} catch (FactoryException e) {
				throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
			}
		}
		return result;
	}

	private Bbox transformBounds(Bbox box, MathTransform t) throws TransformException {
		DirectPosition ll = new DirectPosition2D(box.getX(), box.getY());
		DirectPosition ur = new DirectPosition2D(box.getMaxX(), box.getMaxY());
		t.transform(ll, ll);
		t.transform(ur, ur);
		Bbox result = new Bbox(ll.getCoordinate()[0], ll.getCoordinate()[1], ur.getCoordinate()[0]
				- ll.getCoordinate()[0], ur.getCoordinate()[1] - ll.getCoordinate()[1]);
		return result;
	}

	private Envelope transformBounds(Envelope bounds, MathTransform t) throws TransformException {
		DirectPosition leftTop = new DirectPosition2D(bounds.getMinX(), bounds.getMaxY());
		DirectPosition rightBottom = new DirectPosition2D(bounds.getMaxX(), bounds.getMinY());
		t.transform(leftTop, leftTop);
		t.transform(rightBottom, rightBottom);
		Envelope result = new Envelope(leftTop.getCoordinate()[0], rightBottom.getCoordinate()[0], leftTop
				.getCoordinate()[1], rightBottom.getCoordinate()[1]);
		return result;
	}

	protected void resolveUrl(RasterTile image, int width, int height, Bbox box) throws GeomajasException {
		String url = getBaseWmsUrl();
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
		url += "&layers=" + layers;
		url += "&srs=" + getLayerInfo().getCrs();
		url += "&WIDTH=" + width;
		url += "&HEIGHT=" + height;

		url += "&bbox=" + decimalFormat.format(box.getX()) + "," + decimalFormat.format(box.getY()) + ","
				+ decimalFormat.format(box.getMaxX()) + "," + decimalFormat.format(box.getMaxY());
		url += "&format=" + getFormat();
		url += "&version=" + getVersion();
		url += "&styles=" + getStyles();
		if (null != getParameters()) {
			for (Parameter p : getParameters()) {
				url += "&" + p.getName() + "=" + p.getValue();
			}
		}
		log.debug(url);
		image.setUrl(url);
	}

	protected Resolution calculateBestResolution(double scale) {
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

	protected Resolution calculateBestQuadTreeResolution(double scale) {
		double screenResolution = 1.0 / scale;
		// based on quad tree created by subdividing the maximum extent
		Bbox bbox = getLayerInfo().getMaxExtent();
		double maxWidth = bbox.getWidth();
		double maxHeight = bbox.getHeight();

		Resolution upper = new Resolution(Math.max(maxWidth / getTileWidth(), maxHeight / getTileHeight()), 0,
				getTileWidth(), getTileHeight());
		if (screenResolution >= upper.getResolution()) {
			return upper;
		} else {
			int level = 0;
			Resolution lower = null;
			while (screenResolution < upper.getResolution()) {
				lower = upper;
				level++;
				double width = maxWidth / Math.pow(2, level);
				double height = maxHeight / Math.pow(2, level);
				upper = new Resolution(Math.max(width / getTileWidth(), height / getTileHeight()), level,
						getTileWidth(), getTileHeight());
			}
			if ((upper.getResolution() - screenResolution) > 2 * (screenResolution - lower.getResolution())) {
				return lower;
			} else {
				return upper;
			}
		}

	}

	protected RasterGrid getRasterGrid(Envelope bounds, double width, double height, double scale) {
		// slightly adjust the width and height so it becomes integer for the
		// current scale
		double realWidth = ((int) (width * scale)) / scale;
		double realHeight = ((int) (height * scale)) / scale;

		Envelope bbox = converterService.toInternal(getLayerInfo().getMaxExtent());
		int ymin = (int) Math.floor((bounds.getMinY() - bbox.getMinY()) / realHeight);
		int ymax = (int) Math.floor((bounds.getMaxY() - bbox.getMinY()) / realHeight) + 1;
		int xmin = (int) Math.floor((bounds.getMinX() - bbox.getMinX()) / realWidth);
		int xmax = (int) Math.floor((bounds.getMaxX() - bbox.getMinX()) / realWidth) + 1;
		// same adjustment for corner
		double realXmin = ((int) (bbox.getMinX() * scale)) / scale;
		double realYmin = ((int) (bbox.getMinY() * scale)) / scale;
		Coordinate lowerLeft = new Coordinate(realXmin + xmin * realWidth, realYmin + ymin * realHeight);
		return new RasterGrid(lowerLeft, xmin, ymin, xmax, ymax, realWidth, realHeight);
	}

	protected int getTileWidth() {
		return getLayerInfo().getTileWidth();
	}

	protected int getTileHeight() {
		return getLayerInfo().getTileHeight();
	}

	private Envelope clipBounds(Envelope bounds) {
		return bounds.intersection(getMaxBounds());
	}

	public String getBaseWmsUrl() {
		return baseWmsUrl;
	}

	/**
	 *
	 * @param baseWmsUrl
	 * @since 1.7.1
	 */
	@Api
	public void setBaseWmsUrl(String baseWmsUrl) {
		this.baseWmsUrl = baseWmsUrl;
	}

	public String getFormat() {
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

	public String getVersion() {
		return version;
	}

	/**
	 * Set WMS version to use.
	 *
	 * @param version wms version. For allowed values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setVersion(String version) {
		this.version = version;
	}

	public String getStyles() {
		return styles;
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
	 * Set additional parameters to include in getCapabilities request.
	 *
	 * @param parameters parameters. For possible keys and values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * ???
	 */
	public class RasterGrid {

		private Coordinate lowerLeft;

		private int xmin;

		private int ymin;

		private int xmax;

		private int ymax;

		private double tileWidth;

		private double tileHeight;

		public RasterGrid(Coordinate lowerLeft, int xmin, int ymin, int xmax, int ymax, double tileWidth,
				double tileHeight) {
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
	 * ???
	 */
	class Resolution {

		private double resolution;

		private int level;

		private int tileWidth;

		private int tileHeight;

		public Resolution(double resolution, int level, int tileWidth, int tileHeight) {
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
