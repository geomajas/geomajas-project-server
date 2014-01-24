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
package org.geomajas.plugin.printing.component.dto;

import org.geomajas.annotation.Api;

/**
 * DTO object for {@link org.geomajas.plugin.printing.component.impl.LegendViaUrlComponentImpl}.
 * 
 * @author An Buyle
 * @since 2.4.0
 */
@Api(allMethods = true)
public class LegendViaUrlComponentInfo extends PrintComponentInfo {

	private static final long serialVersionUID = 240L;

	private String clientLayerId;

	private String legendImageServiceUrl;

	/**
	 * Get client layer id.
	 * 
	 * @return client layer id
	 */
	public String getClientLayerId() {
		return clientLayerId;
	}

	/**
	 * Set the client layer id.
	 * 
	 * @param clientLayerId
	 *            id
	 */
	public void setClientLayerId(String clientLayerId) {
		this.clientLayerId = clientLayerId;
	}

	/**
	 * @return Legend image service URL
	 */
	public String getLegendImageServiceUrl() {
		return legendImageServiceUrl;
	}

	/**
	 * @param legendImageServiceUrl
	 *            Legend image service URL
	 */
	public void setLegendImageServiceUrl(String legendImageServiceUrl) {
		this.legendImageServiceUrl = legendImageServiceUrl;
	}
}