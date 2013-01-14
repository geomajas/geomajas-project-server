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

package org.geomajas.plugin.staticsecurity.gwt.example.server.command.dto;

import org.geomajas.command.dto.GetConfigurationRequest;

/**
 * Request object for {@link org.geomajas.plugin.staticsecurity.gwt.example.server.command.AppConfigurationCommand}.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start AppConfigurationRequest, Request object for replacement command
public class AppConfigurationRequest extends GetConfigurationRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND = "command.AppConfiguration";

	public AppConfigurationRequest(String applicationId) {
		super();
		setApplicationId(applicationId);
	}

	public AppConfigurationRequest() {
		// for deserialization
		super();
	}
}
// @extract-end
