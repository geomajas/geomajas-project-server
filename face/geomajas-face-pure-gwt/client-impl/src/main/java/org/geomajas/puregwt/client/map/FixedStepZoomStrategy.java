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

package org.geomajas.puregwt.client.map;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.puregwt.client.spatial.Bbox;

/**
 * Zooming strategy that only allows a fixed number of scales to be used. These fixed steps are taken from the map
 * configuration object during construction.<br/>
 * TODO getIndex and checkScale behave differently. How should zooming behave here???? Discussion needed before
 * finishing this class...
 * 
 * @author Pieter De Graef
 */
public class FixedStepZoomStrategy extends FreeForAllZoomStrategy {

	private static final double MAX_RESOLUTION = Float.MAX_VALUE;

	private List<Double> resolutions;

	private List<Double> scales;

	public FixedStepZoomStrategy(ClientMapInfo mapInfo, Bbox maxBounds) {
		super(mapInfo, maxBounds);

		// Check for the existence of resolutions:
		if (mapInfo.getScaleConfiguration().getZoomLevels() == null
				|| mapInfo.getScaleConfiguration().getZoomLevels().size() == 0) {
			throw new IllegalArgumentException("When using the FixedStepZoomStrategy, make sure that"
					+ " the map configuration has defined at least one zoom level.");
		}

		// Get the full set of resolutions:
		resolutions = new ArrayList<Double>();
		scales = new ArrayList<Double>();
		for (ScaleInfo scale : mapInfo.getScaleConfiguration().getZoomLevels()) {
			resolutions.add(1. / scale.getPixelPerUnit());
			scales.add(scale.getPixelPerUnit());
		}
	}

	public double getMaximumScale() {
		return scales.get(scales.size() - 1);
		// return 1 / resolutions.get(resolutions.size() - 1);
	}

	public double getMinimumScale() {
		return scales.get(0);
		// return 1 / resolutions.get(0);
	}

	public int getZoomStepCount() {
		return scales.size();
		// return resolutions.size();
	}

	public double getZoomStepScale(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Index must be a value between 0 and the (zoom step count - 1)");
		}
		if (index >= scales.size()) {
			throw new IllegalArgumentException("Index must be a value between 0 and the (zoom step count - 1)");
		}
		// return 1 / resolutions.get(resolutions.size() - index - 1);
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

		if (resolutions != null) {
			IndexRange indexes = getResolutionRange();
			if (!indexes.isValid()) { // Cannot snap to resolutions
				return allowedScale;
			} else {
				// find the new index
				int newResolutionIndex = 0;
				double screenResolution = 1.0 / allowedScale;
				if (screenResolution >= resolutions.get(indexes.getMin())) {
					newResolutionIndex = indexes.getMin();
				} else if (screenResolution <= resolutions.get(indexes.getMax())) {
					newResolutionIndex = indexes.getMax();
				} else {
					for (int i = indexes.getMin(); i < indexes.getMax(); i++) {
						double upper = resolutions.get(i);
						double lower = resolutions.get(i + 1);
						if (screenResolution <= upper && screenResolution > lower) {
							if (option == ZoomOption.LEVEL_FIT) {
								newResolutionIndex = i;
								break;
							} else {
								if ((upper / screenResolution) > (screenResolution / lower)) {
									newResolutionIndex = i + 1;
									break;
								} else {
									newResolutionIndex = i;
									break;
								}
							}
						}
					}
				}
				return 1.0 / resolutions.get(newResolutionIndex);
			}
		} else {
			return scale;
		}
	}

	private IndexRange getResolutionRange() {
		IndexRange range = new IndexRange();
		double max = MAX_RESOLUTION;
		double minimumScale = getMinimumScale();
		if (minimumScale > 0) {
			max = 1.0 / getMinimumScale();
		}
		double min = 1.0 / getMaximumScale();
		for (int i = 0; i < resolutions.size(); i++) {
			Double resolution = resolutions.get(i);
			if (resolution >= min && resolution <= max) {
				range.setMin(i);
				range.setMax(i);
			}
		}
		return range;
	}

	/**
	 * A range of indexes.
	 * 
	 * @author Pieter De Graef
	 */
	private class IndexRange {

		private Integer min;

		private Integer max;

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			if (this.max == null || max > this.max) {
				this.max = max;
			}
		}

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			if (this.min == null || min < this.min) {
				this.min = min;
			}
		}

		public boolean isValid() {
			return min != null && max != null && min <= max;
		}
	}
}