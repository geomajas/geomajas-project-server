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
package org.geomajas.configuration.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;

/**
 * Scale configuration of the map. The map needs a maximum scale (minimum is determined by maximum bounds) and
 * optionally a set of zoom levels.
 * 
 * @author Jan De Moerloose
 * @since 1.7.0
 */
@Api(allMethods = true)
public class ScaleConfigurationInfo implements IsInfo {

	private static final long serialVersionUID = 170L;

	private ScaleInfo maximumScale = new ScaleInfo(ScaleInfo.MAXIMUM_PIXEL_PER_UNIT);

	private List<ScaleInfo> zoomLevels = new ArrayList<ScaleInfo>();

	
	/**
	 * Returns the maximum scale of the map.
	 * 
	 * @return the maximum scale of the map
	 */
	public ScaleInfo getMaximumScale() {
		return maximumScale;
	}

	/**
	 * Sets the maximum scale of the map.
	 * 
	 * @param maximumScale
	 *            the maximum scale of the map
	 */
	public void setMaximumScale(ScaleInfo maximumScale) {
		this.maximumScale = maximumScale;
	}

	/**
	 * Returns a list of predefined zoom levels allowed by the map.
	 * 
	 * @return list of levels (scales)
	 */
	public List<ScaleInfo> getZoomLevels() {
		return zoomLevels;
	}

	/**
	 * Sets a list of predefined zoom levels allowed by the map.
	 * 
	 * @param zoomLevels
	 *            list of levels (scales)
	 */
	public void setZoomLevels(List<ScaleInfo> zoomLevels) {
		this.zoomLevels = zoomLevels;
	}

}
