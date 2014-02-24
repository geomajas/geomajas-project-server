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
package org.geomajas.plugin.jmeter;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;

/**
 * Performance command service. Wraps {@link org.geomajas.command.CommandDispatcher} and filters out
 * {@link org.geomajas.configuration.client.ClientWidgetInfo} of configuration.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface PerformanceCommandService {

	/**
	 * General command execution function.
	 * 
	 * @param commandName name of command to execute
	 * @param commandRequest {@link CommandRequest} object for the command (if any)
	 * @param userToken token to identify user
	 * @param locale which should be used for the error messages in the response
	 * @return {@link CommandResponse} command response
	 */
	@Api
	CommandResponse execute(String commandName, CommandRequest commandRequest, String userToken, String locale);

}
