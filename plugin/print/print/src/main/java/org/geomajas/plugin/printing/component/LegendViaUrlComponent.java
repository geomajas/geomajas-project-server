/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.printing.component;

import org.geomajas.plugin.printing.component.dto.LegendViaUrlComponentInfo;

/**
 * Component representing a legend image specified via a URL.
 * 
 * @author An Buyle
 *
 */
public interface LegendViaUrlComponent extends PrintComponent<LegendViaUrlComponentInfo> {

	/**
	 * Get the legend image service URL.
	 * 
	 * @return Legend image service URL as a String
	 */
	String getLegendImageServiceUrl();
	
	/**
	 * Set the legend image service URL.
	 * 
	 * @param legendImageServiceUrlAsString  Legend image service URL as a String
	 */
	void setLegendImageServiceUrl(String legendImageServiceUrlAsString);
	
	/**
	 * Check if the legend visible (in other words is there a legend image?). 
	 * 
	 * @return True if image could be successfully retrieved, or if image hasn't been retrieved yet
	 * 					(calling calculateSize() will trigger the retrieval attempt).
	 * 			False if the image retrieval failed. Possible reason: the layer is invisible for the specified scale. 
	 */
	boolean isVisible();

}