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

package org.geomajas.plugin.reporting.data;

import org.geomajas.layer.feature.Feature;
import org.geomajas.plugin.caching.step.CacheContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Cache container for report data. Contains map image and feature data source.
 *
 * @author Jan De Moerloose
 */
public class ReportingCacheContainer extends CacheContainer {

	private byte[] mapImageData;

	private byte[] legendImageData;

	private List<Feature> features = new ArrayList<Feature>();

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public byte[] getMapImageData() {
		return mapImageData;
	}

	public void setMapImageData(byte[] mapImageData) {
		this.mapImageData = mapImageData;
	}

	public byte[] getLegendImageData() {
		return legendImageData;
	}

	public void setLegendImageData(byte[] legendImageData) {
		this.legendImageData = legendImageData;
	}

}
