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

package org.geomajas.gwt.client;

import org.geomajas.command.CommandResponse;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.GwtCommand;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <p>
 * Interface for the service object that is responsible for the communication between the geomajas client and server.
 * </p>
 *
 * @author Jan De Moerloose
 * @since 0.0.0
 */
@Api(allMethods = true)
public interface GeomajasServiceAsync {

	/**
	 * Execute a <code>GwtCommandRequest</code>, and return the answer as a <code>CommandResponse</code>.
	 *
	 * @param gwtCommand
	 *            The gwtCommand to be executed.
	 * @param callback callback which will be invoked with command response
	 */
	void execute(GwtCommand gwtCommand, AsyncCallback<CommandResponse> callback);
}