/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.tms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.layer.LayerException;
import org.geomajas.service.GeoService;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Data object that stores some necessary values for TMS layers.
 * 
 * @author Pieter De Graef
 */
public class TileServiceState {

	private final List<Double> resolutions;

	private final Envelope maxBounds;

	private final Crs crs;

	private final int tileWidth;

	private final int tileHeight;

	public TileServiceState(GeoService geoService, RasterLayerInfo layerInfo) {
		try {
			crs = geoService.getCrs2(layerInfo.getCrs());
		} catch (LayerException e) {
			throw new IllegalStateException(e);
		}
		maxBounds = new Envelope(layerInfo.getMaxExtent().getX(), layerInfo.getMaxExtent().getMaxX(), layerInfo
				.getMaxExtent().getY(), layerInfo.getMaxExtent().getMaxY());

		resolutions = new ArrayList<Double>();
		if (layerInfo.getZoomLevels() != null) {
			for (ScaleInfo scale : layerInfo.getZoomLevels()) {
				resolutions.add(1. / scale.getPixelPerUnit());
			}

			// Sort in decreasing order !!!
			Collections.sort(resolutions);
			Collections.reverse(resolutions);
		}
		tileWidth = layerInfo.getTileWidth();
		tileHeight = layerInfo.getTileHeight();
	}

	public List<Double> getResolutions() {
		return resolutions;
	}

	public Envelope getMaxBounds() {
		return maxBounds;
	}

	public Crs getCrs() {
		return crs;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}
}