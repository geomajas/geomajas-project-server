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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;

/**
 * Response of the WMS capabilities. Contains a list of rastercapabilities info and the default application crs.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 */
public class GetWmsCapabilitiesResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<RasterCapabilitiesInfo> rasterCapabilities = new ArrayList<RasterCapabilitiesInfo>();
	
	private String defaultCrs;

	public List<RasterCapabilitiesInfo> getRasterCapabilities() {
		return rasterCapabilities;
	}

	public void setRasterCapabilities(List<RasterCapabilitiesInfo> rasterCapabilities) {
		this.rasterCapabilities = rasterCapabilities;
	}

	/**
	 * Set the default deskmanager instance crs.
	 * 
	 * @param defaultCrs the defaultCrs to set
	 */
	public void setDefaultCrs(String defaultCrs) {
		this.defaultCrs = defaultCrs;
	}

	/**
	 * Get the default deskmanager instance crs.
	 * @return the defaultCrs
	 */
	public String getDefaultCrs() {
		return defaultCrs;
	}

}
