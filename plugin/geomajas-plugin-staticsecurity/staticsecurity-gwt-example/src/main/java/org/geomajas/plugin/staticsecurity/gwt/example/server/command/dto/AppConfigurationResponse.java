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

package org.geomajas.plugin.staticsecurity.gwt.example.server.command.dto;

import org.geomajas.command.dto.GetConfigurationResponse;

/**
 * Response object for {@link org.geomajas.plugin.staticsecurity.gwt.example.server.command.AppConfigurationCommand}.
 *
 * @author Joachim Van der Auwera
 */
public class AppConfigurationResponse extends GetConfigurationResponse {

	private static final long serialVersionUID = 100L;

	private boolean blablaButtonAllowed;

	public boolean isBlablaButtonAllowed() {
		return blablaButtonAllowed;
	}

	public void setBlablaButtonAllowed(boolean blablaButtonAllowed) {
		this.blablaButtonAllowed = blablaButtonAllowed;
	}
}
