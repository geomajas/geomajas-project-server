/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;

/**
 * Zooming strategy that only allows a fixed number of scales to be used. These fixed steps are taken from the map
 * configuration object during construction.<br/>
 * TODO getIndex and checkScale behave differently. How should zooming behave here???? Discussion needed before
 * finishing this class...
 * 
 * @author Pieter De Graef
 */
public class FixedStepZoomStrategy extends FreeForAllZoomStrategy {

	private List<Double> scales;

	protected FixedStepZoomStrategy(ClientMapInfo mapInfo, Bbox maxBounds) {
		super(mapInfo, maxBounds);

		// Check for the existence of resolutions:
		if (mapInfo.getScaleConfiguration().getZoomLevels() == null
				|| mapInfo.getScaleConfiguration().getZoomLevels().size() == 0) {
			throw new IllegalArgumentException("When using the FixedStepZoomStrategy, make sure that"
					+ " the map configuration has defined at least one zoom level.");
		}

		// Get the full set of resolutions:
		scales = new ArrayList<Double>();
		for (ScaleInfo scale : mapInfo.getScaleConfiguration().getZoomLevels()) {
			scales.add(scale.getPixelPerUnit());
		}
	}

	public double getMaximumScale() {
		return scales.get(scales.size() - 1);
	}

	public double getMinimumScale() {
		return scales.get(0);
	}

	public int getZoomStepCount() {
		return scales.size();
	}

	public double getZoomStepScale(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Index must be a value between 0 and the (zoom step count - 1)");
		}
		if (index >= scales.size()) {
			throw new IllegalArgumentException("Index must be a value between 0 and the (zoom step count - 1)");
		}
		return scales.get(scales.size() - index - 1);
	}

	public int getZoomStepIndex(double scale) {
		double minimumScale = getMinimumScale();
		if (scale <= minimumScale) {
			return scales.size() - 1;
		}
		double maximumScale = getMaximumScale();
		if (scale >= maximumScale) {
			return 0;
		}

		for (int i = 0; i < scales.size(); i++) {
			double lower = scales.get(i);
			double upper = scales.get(i + 1);
			if (scale <= upper && scale > lower) {
				if (Math.abs(upper - scale) >= Math.abs(lower - scale)) {
					return scales.size() - 1 - i;
				} else {
					return scales.size() - 1 - (i + 1);
				}
			}
		}
		return 0;
	}

	public double checkScale(double scale, ZoomOption option) {
		double allowedScale = scale;
		double minimumScale = getMinimumScale();
		if (scale < minimumScale) {
			allowedScale = minimumScale;
		} else if (scale > getMaximumScale()) {
			allowedScale = getMaximumScale();
		}

		for (int i = 0; i < scales.size() - 1; i++) {
			double lower = scales.get(i);
			double upper = scales.get(i + 1);
			
			if (allowedScale == upper) {
				return upper;
			} else if (allowedScale == lower) {
				return lower;
			} else if (allowedScale < upper && allowedScale > lower) {
				if (option == ZoomOption.LEVEL_FIT) {
					return lower;
				} else {
					if (Math.abs(upper - allowedScale) < Math.abs(allowedScale - lower)) {
						return upper;
					} else {
						return lower;
					}
				}
			}
		}

		return allowedScale;
	}
}