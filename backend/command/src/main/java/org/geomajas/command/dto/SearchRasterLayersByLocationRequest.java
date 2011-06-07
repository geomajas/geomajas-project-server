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
package org.geomajas.command.dto;

import org.geomajas.geometry.Bbox;

/**
 * Request for {@link org.geomajas.command.feature.SearchRasterLayersByLocationCommand} that allows searching features
 * on raster layers that implement {@link org.geomajas.layer.LayerFeatureInfoSupport}.
 *
 * @author Oliver May
 */
public class SearchRasterLayersByLocationRequest extends SearchByLocationRequest {

	private static final long serialVersionUID = 190L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 */
	public static final String COMMAND = "command.feature.SearchRasterLayersByLocation";

	private double scale;
	private Bbox bbox;
	
	
	/**
	 * Get the scale for the search.
	 *
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}
	
	/**
	 * Set the scale for the search.
	 *
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Get the bounding box.
	 *
	 * @param bbox the bbox to set
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	/**
	 * Set the bounding box.
	 *
	 * @return the bbox
	 */
	public Bbox getBbox() {
		return bbox;
	}

}
