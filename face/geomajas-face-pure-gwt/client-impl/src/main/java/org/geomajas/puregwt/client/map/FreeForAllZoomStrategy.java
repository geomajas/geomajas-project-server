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

package org.geomajas.puregwt.client.map;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleConfigurationInfo;
import org.geomajas.geometry.Bbox;

/**
 * Zooming strategy that allows any zoom scale between the minimum and maximum scales to be used.
 * 
 * @author Pieter De Graef
 */
public class FreeForAllZoomStrategy implements ZoomStrategy {

	public static final double ZOOM_DELTA = 2;

	private int mapWidth;

	private int mapHeight;

	private double maximumScale;

	private Bbox maxBounds;

	protected FreeForAllZoomStrategy(ClientMapInfo mapInfo, Bbox maxBounds) {
		ScaleConfigurationInfo scaleConfigurationInfo = mapInfo.getScaleConfiguration();
		maximumScale = scaleConfigurationInfo.getMaximumScale().getPixelPerUnit();
		this.maxBounds = maxBounds;
	}

	public void setMapSize(int mapWidth, int mapHeight) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}

	public double getMinimumScale() {
		return getZoomStepScale(getZoomStepCount() - 1);
	}

	public double getMaximumScale() {
		return maximumScale;
	}

	public double checkScale(double scale, ZoomOption option) {
		double minimumScale = getMinimumScale();
		if (scale < minimumScale) {
			return minimumScale;
		} else if (scale > maximumScale) {
			return maximumScale;
		} else {
			return scale;
		}
	}

	public int getZoomStepCount() {
		int count = 0;
		double tempScale = getTheoreticalMinimumScale();
		while (tempScale <= maximumScale) {
			tempScale *= ZOOM_DELTA;
			count++;
		}
		return count;
	}

	public double getZoomStepScale(int index) {
		// Check index value first:
		if (index < 0 || index >= getZoomStepCount()) {
			throw new RuntimeException("Index out of bounds.");
		}

		// Start from the maximum scale (max zoom in), because that one is at least stable.
		return getMaximumScale() / Math.pow(ZOOM_DELTA, index);
	}

	public int getZoomStepIndex(double scale) {
		double minimumScale = getMinimumScale();
		if (scale <= minimumScale) {
			return getZoomStepCount() - 1;
		}

		double compareScale = maximumScale;
		int index = 0;
		while (scale < compareScale) {
			double delta = Math.abs(compareScale - scale);
			compareScale /= ZOOM_DELTA;
			double newDelta = Math.abs(compareScale - scale);
			if (delta >= newDelta) {
				// >=, means that if equal take the next index (= zoom out)
				index++;
			}
		}
		return index;
	}

	private double getTheoreticalMinimumScale() {
		// the minimum scale is determined by the maximum bounds and the pixel size of the map
		if (maxBounds != null) {
			double wRatio = mapWidth / maxBounds.getWidth();
			double hRatio = mapHeight / maxBounds.getHeight();
			// return the maximum to fit outside
			return wRatio > hRatio ? wRatio : hRatio;
		} else {
			return Double.MIN_VALUE;
		}
	}
}