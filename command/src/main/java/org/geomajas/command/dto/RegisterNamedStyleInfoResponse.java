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
import org.geomajas.command.CommandResponse;


/**
 * Response object for {@link org.geomajas.command.render.RegisterNamedStyleInfoCommand}.
 * @author Oliver May
 * @since 1.13.0
 */
@Api(allMethods = true)
public class RegisterNamedStyleInfoResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private String styleName;

	/**
	 * Set response style name.
	 * 
	 * @param styleName the styleName to set
	 */
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	/**
	 * Get response style name.
	 * 
	 * @return the styleName
	 */
	public String getStyleName() {
		return styleName;
	}
	
	
	
}
