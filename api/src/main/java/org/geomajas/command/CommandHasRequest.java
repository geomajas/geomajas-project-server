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
package org.geomajas.command;

/**
 * Extension of {@link org.geomajas.command.Command} with method for getting an empty command request.
 *
 * @param <REQUEST> type of request object, see {@link org.geomajas.command.CommandRequest}
 * @param <RESPONSE> type of response object, see {@link org.geomajas.command.CommandResponse}
 *
 * @author Jan Venstermans
 */
public interface CommandHasRequest<REQUEST extends CommandRequest, RESPONSE extends CommandResponse>
		extends Command<REQUEST, RESPONSE> {

	/**
	 * Get an empty response object which will be filled when executing the command and partially by the dispatcher.
	 * @return response object, extends {@link org.geomajas.command.CommandResponse}
	 */
	REQUEST getEmptyCommandRequest();
}
