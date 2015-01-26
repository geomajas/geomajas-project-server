/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import org.geomajas.command.LayerIdsCommandRequest;

/**
 * Request object for {@link org.geomajas.command.configuration.UserMaximumExtentCommand}.
 * 
 * @author Joachim Van der Auwera
 */
public class UserMaximumExtentRequest extends LayerIdsCommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.configuration.UserMaximumExtent";

	private boolean excludeRasterLayers;

	private String crs;

	public UserMaximumExtentRequest() {
	}

	public boolean isExcludeRasterLayers() {
		return excludeRasterLayers;
	}

	public void setExcludeRasterLayers(boolean excludeRasterLayers) {
		this.excludeRasterLayers = excludeRasterLayers;
	}

	/**
	 * Get the coordinate reference space which should be used for the returned bounding box.
	 * 
	 * @return crs
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference space which should be used for the returned bounding box.
	 * 
	 * @param crs
	 *            crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	@Override
	public String toString() {
		return "UserMaximumExtentRequest{" +
				"excludeRasterLayers=" + excludeRasterLayers +
				", crs='" + crs + '\'' +
				'}';
	}
}
