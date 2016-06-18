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
package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.configuration.NamedStyleInfo;

/**
 * Request object for {@link org.geomajas.command.render.RegisterNamedStyleInfoCommand}.
 * 
 * @author Oliver May
 * @since 1.13.0
 */
@Api
public class RegisterNamedStyleInfoRequest implements CommandRequest {

	private static final long serialVersionUID = 115L;

	public static final String COMMAND = "command.render.RegisterNamedStyleInfo";

	private String layerId;

	private NamedStyleInfo namedStyleInfo;

	/**
	 * The named style info to register.
	 * 
	 * @param namedStyleInfo
	 *            the namedStyleInfo to set
	 */
	public void setNamedStyleInfo(NamedStyleInfo namedStyleInfo) {
		this.namedStyleInfo = namedStyleInfo;
	}

	/**
	 * The named style info to register.
	 * 
	 * @return the namedStyleInfo
	 */
	public NamedStyleInfo getNamedStyleInfo() {
		return namedStyleInfo;
	}

	/**
	 * The layerId to register.
	 * 
	 * @param layerId
	 *            the layerId to set
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	/**
	 * The layerId to register.
	 * 
	 * @return the layerId
	 */
	public String getLayerId() {
		return layerId;
	}

}
