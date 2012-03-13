/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.puregwt.client.service;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;

/**
 * The central client side dispatcher service for all commands. Use the {@link #execute(GwtCommand, CommandCallback...)}
 * function to execute an asynchronous command on the server.
 * <p/>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CommandService {

	/**
	 * The execution function. Executes a server side command.
	 * 
	 * @param command The command to be executed. This command is a wrapper around the actual request object.
	 * @param callback A <code>CommandCallback</code> function to be executed when the command successfully returns. The
	 *        callbacks may implement CommunicationExceptionCallback or CommandExceptionCallback to allow error
	 *        handling.
	 * @return deferred object which can be used to add extra callbacks
	 * @since 1.0.0
	 */
	Deferred execute(final GwtCommand command, final CommandCallback... callback);


}
