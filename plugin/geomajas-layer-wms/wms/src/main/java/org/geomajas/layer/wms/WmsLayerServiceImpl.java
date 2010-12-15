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
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.wms.WmsLayer.RasterGrid;
import org.geomajas.layer.wms.WmsLayer.Resolution;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * Implementation of the <code>WmsLayerService</code> interface.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public class WmsLayerServiceImpl implements WmsLayerService {

	private DecimalFormat decimalFormat = new DecimalFormat();

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public WmsLayerServiceImpl() {
		decimalFormat.setDecimalSeparatorAlwaysShown(false);
		decimalFormat.setGroupingUsed(false);
		decimalFormat.setMinimumFractionDigits(0);
		decimalFormat.setMaximumFractionDigits(100);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(symbols);
	}

	// ------------------------------------------------------------------------
	// WmsLayerService implementation:
	// ------------------------------------------------------------------------

	/**
	 * Paints the specified bounds optimized for the specified scale in pixel/unit.
	 * 
	 * @param layer
	 *            The WMS layer to paint for.
	 * @param targetCrs
	 *            Coordinate reference system used for bounds
	 * @param bounds
	 *            bounds to request images for
	 * @param scale
	 *            scale or zoom level (unit?)
	 * @return a list of raster images that covers the bounds
	 * @throws GeomajasException
	 *             oops
	 */
	public List<RasterTile> paint(WmsLayer layer, CoordinateReferenceSystem targetCrs, Envelope bounds, double scale)
			throws GeomajasException {
		Envelope layerBounds = bounds;
		double layerScale = scale;
		MathTransform layerToMap = null;

		try {
			// We don't necessarily need to split into same CRS and different CRS cases, the latter implementation uses
			// identity transform if crs's are equal for map and layer but might introduce bugs in rounding and/or
			// conversions.
			if (!layer.getCrs().equals(targetCrs)) {
				layerToMap = geoService.findMathTransform(CRS.decode(layer.getLayerInfo().getCrs()), targetCrs);
				MathTransform mapToLayer = layerToMap.inverse();

				// Translate the map coordinates to layer coordinates, assumes equal x-y orientation
				layerBounds = transformBounds(bounds, mapToLayer);
				layerScale = bounds.getWidth() * scale / layerBounds.getWidth();
			}
		} catch (MismatchedDimensionException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_DIMENSION_MISMATCH);
		} catch (TransformException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
		} catch (FactoryException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
		}
		layerBounds = clipBounds(layer, layerBounds);
		if (layerBounds.isNull()) {
			return new ArrayList<RasterTile>(0);
		}

		// Grid is in layer coordinate space!
		Resolution bestResolution = getResolutionForScale(layer, layerScale);
		Envelope maxExtent = converterService.toInternal(layer.getLayerInfo().getMaxExtent());
		RasterGrid grid = getRasterGrid(layer, maxExtent, layerBounds, bestResolution.getTileWidth(),
				bestResolution.getTileHeight(), layerScale);

		// We calculate the first tile's screenbox with this assumption
		List<RasterTile> result = new ArrayList<RasterTile>();
		for (int i = grid.getXmin(); i < grid.getXmax(); i++) {
			for (int j = grid.getYmin(); j < grid.getYmax(); j++) {
				double x = grid.getLowerLeft().x + (i - grid.getXmin()) * grid.getTileWidth();
				double y = grid.getLowerLeft().y + (j - grid.getYmin()) * grid.getTileHeight();
				// layer coordinates
				Bbox worldBox = null;
				Bbox layerBox = null;
				if (!layer.getCrs().equals(targetCrs)) {
					layerBox = new Bbox(x, y, grid.getTileWidth(), grid.getTileHeight());
					// Transforming back to map coordinates will only result in a proper grid if the transformation
					// is nearly affine
					try {
						worldBox = transformBounds(layerBox, layerToMap);
					} catch (TransformException e) {
						throw new GeomajasException(e, ExceptionCode.RENDER_TRANSFORMATION_FAILED);
					}
				} else {
					worldBox = new Bbox(x, y, grid.getTileWidth(), grid.getTileHeight());
					layerBox = worldBox;
				}
				// Rounding to avoid white space between raster tiles lower-left becomes upper-left in inverted y-space
				Bbox screenbox = new Bbox(Math.round(scale * worldBox.getX()), -Math.round(scale * worldBox.getMaxY()),
						Math.round(scale * worldBox.getMaxX()) - Math.round(scale * worldBox.getX()), Math.round(scale
								* worldBox.getMaxY())
								- Math.round(scale * worldBox.getY()));

				RasterTile image = new RasterTile(screenbox, layer.getId() + "." + bestResolution.getLevel() + "." + i
						+ "," + j);

				image.setCode(new TileCode(bestResolution.getLevel(), i, j));
				String url = formatUrl(layer, bestResolution.getTileWidthPx(), bestResolution.getTileHeightPx(),
						layerBox);
				image.setUrl(url);
				result.add(image);
			}
		}

		return result;
	}

	/**
	 * Calculate and apply the resolutions for a WMS layer.
	 * 
	 * @param layer
	 *            The WMS layer to apply resolutions for.
	 */
	public void calculateResolutions(WmsLayer layer) {
		List<ScaleInfo> scales = layer.getLayerInfo().getZoomLevels();
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
				layer.addResolution(resolution, level++, layer.getLayerInfo().getTileWidth(), layer.getLayerInfo()
						.getTileHeight());
			}
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private String getBaseWmsUrl(WmsLayer layer) {
		if (layer.getAuthentication() == null
				|| (layer.getAuthentication().getUser() == null && layer.getAuthentication().getPassword() == null)) {
			return layer.getBaseWmsUrl();
		} else if (layer.getAuthentication() != null && layer.getAuthentication().getApplicationUrl() != null) {
			if (layer.getAuthentication().getApplicationUrl().endsWith("/")) {
				return layer.getAuthentication().getApplicationUrl() + "d/wms/" + layer.getId() + "/";
			} else {
				return layer.getAuthentication().getApplicationUrl() + "/d/wms/" + layer.getId() + "/";
			}
		}
		return "./d/wms/" + layer.getId() + "/";
	}

	private String formatUrl(WmsLayer layer, int width, int height, Bbox box) throws GeomajasException {
		String url = getBaseWmsUrl(layer);
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

		String layers = layer.getId();
		if (layer.getLayerInfo().getDataSourceName() != null) {
			layers = layer.getLayerInfo().getDataSourceName();
		}
		url += "&layers=" + layers;
		url += "&WIDTH=" + width;
		url += "&HEIGHT=" + height;

		url += "&bbox=" + decimalFormat.format(box.getX()) + "," + decimalFormat.format(box.getY()) + ","
				+ decimalFormat.format(box.getMaxX()) + "," + decimalFormat.format(box.getMaxY());
		url += "&format=" + layer.getFormat();
		url += "&version=" + layer.getVersion();
		if ("1.3.0".equals(layer.getVersion())) {
			url += "&crs=" + layer.getLayerInfo().getCrs();
		} else {
			url += "&srs=" + layer.getLayerInfo().getCrs();
		}
		url += "&styles=" + layer.getStyles();
		if (null != layer.getParameters()) {
			for (Parameter p : layer.getParameters()) {
				url += "&" + p.getName() + "=" + p.getValue();
			}
		}
		// log.debug(url);
		return url;
	}

	private Resolution getResolutionForScale(WmsLayer layer, double scale) {
		List<Resolution> resolutions = layer.resolutions;
		if (null == resolutions || resolutions.size() == 0) {
			return calculateBestQuadTreeResolution(layer, scale);
		} else {
			double screenRes = 1.0 / scale;
			if (screenRes >= resolutions.get(0).getResolution()) {
				return resolutions.get(0);
			} else if (screenRes <= resolutions.get(resolutions.size() - 1).getResolution()) {
				return resolutions.get(resolutions.size() - 1);
			} else {
				for (int i = 0; i < resolutions.size() - 1; i++) {
					Resolution upper = resolutions.get(i);
					Resolution lower = resolutions.get(i + 1);
					if (screenRes <= upper.getResolution() && screenRes >= lower.getResolution()) {
						if ((upper.getResolution() - screenRes) > 2 * (screenRes - lower.getResolution())) {
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

	private RasterGrid getRasterGrid(WmsLayer layer, Envelope maxExtent, Envelope bounds, double width, double height,
			double scale) {
		// slightly adjust the width and height so it becomes integer for the
		// current scale
		double realWidth = ((int) (width * scale)) / scale;
		double realHeight = ((int) (height * scale)) / scale;

		// Envelope bbox = converterService.toInternal(getLayerInfo().getMaxExtent());
		int ymin = (int) Math.floor((bounds.getMinY() - maxExtent.getMinY()) / realHeight);
		int ymax = (int) Math.floor((bounds.getMaxY() - maxExtent.getMinY()) / realHeight) + 1;
		int xmin = (int) Math.floor((bounds.getMinX() - maxExtent.getMinX()) / realWidth);
		int xmax = (int) Math.floor((bounds.getMaxX() - maxExtent.getMinX()) / realWidth) + 1;
		// same adjustment for corner
		double realXmin = ((int) (maxExtent.getMinX() * scale)) / scale;
		double realYmin = ((int) (maxExtent.getMinY() * scale)) / scale;
		Coordinate lowerLeft = new Coordinate(realXmin + xmin * realWidth, realYmin + ymin * realHeight);
		return layer.createRasterGrid(lowerLeft, xmin, ymin, xmax, ymax, realWidth, realHeight);
	}

	private Resolution calculateBestQuadTreeResolution(WmsLayer layer, double scale) {
		double screenResolution = 1.0 / scale;
		// based on quad tree created by subdividing the maximum extent
		Bbox bbox = layer.getLayerInfo().getMaxExtent();
		double maxWidth = bbox.getWidth();
		double maxHeight = bbox.getHeight();

		int tileWidth = layer.getLayerInfo().getTileWidth();
		int tileHeight = layer.getLayerInfo().getTileHeight();

		Resolution upper = layer.createResolution(Math.max(maxWidth / tileWidth, maxHeight / tileHeight), 0, tileWidth,
				tileHeight);
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
				upper = layer.createResolution(Math.max(width / tileWidth, height / tileHeight), level, tileWidth,
						tileHeight);
			}
			if ((upper.getResolution() - screenResolution) > 2 * (screenResolution - lower.getResolution())) {
				return lower;
			} else {
				return upper;
			}
		}
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
		Envelope result = new Envelope(leftTop.getCoordinate()[0], rightBottom.getCoordinate()[0],
				leftTop.getCoordinate()[1], rightBottom.getCoordinate()[1]);
		return result;
	}

	private Envelope clipBounds(WmsLayer layer, Envelope bounds) {
		Envelope maxExtent = converterService.toInternal(layer.getLayerInfo().getMaxExtent());
		return bounds.intersection(maxExtent);
	}
}