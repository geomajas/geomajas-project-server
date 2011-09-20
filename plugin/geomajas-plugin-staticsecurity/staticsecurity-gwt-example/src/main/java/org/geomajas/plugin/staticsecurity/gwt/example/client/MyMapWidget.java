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

package org.geomajas.plugin.staticsecurity.gwt.example.client;

import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.staticsecurity.gwt.example.server.command.dto.AppConfigurationRequest;
import org.geomajas.plugin.staticsecurity.gwt.example.server.command.dto.AppConfigurationResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Map widget that overrides initialization to use a custom command for getting the configuration. The extended
 * configuration response can be consumed by callback functions.
 *
 * @author Joachim Van der Auwera
 */
public class MyMapWidget extends MapWidget {

	private List<MapCallback> callbacks = new ArrayList<MapCallback>();

	/**
	 * Constructs a referral map for the specified referral and task id.
	 *
	 * @param id map id
	 * @param applicationId application id
	 */
	public MyMapWidget(String id, String applicationId) {
		super(id, applicationId);
	}

	@Override
	public void init() {
		GwtCommand commandRequest = new GwtCommand(AppConfigurationRequest.COMMAND);
		commandRequest.setCommandRequest(new AppConfigurationRequest(id, applicationId));
		GwtCommandDispatcher.getInstance().execute(commandRequest,
				new AbstractCommandCallback<AppConfigurationResponse>() {

					public void execute(AppConfigurationResponse response) {
						initializationCallback(response.getMapInfo());
						for (MapCallback callback : callbacks) {
							callback.onResponse(response);
						}
					}

				});
	}

	/**
	 * Add a callback function to be processed after intialization.
	 *
	 * @param callback a callback function
	 */
	public void addMapCallback(MapCallback callback) {
		callbacks.add(callback);
	}

	/**
	 * Callback function that will be called when all response data is present and the map is initialized.
	 *
	 * @author Joachim Van der Auwera
	 */
	public interface MapCallback {

		/**
		 * Perform an action for the specified response.
		 *
		 * @param response the map response
		 */
		void onResponse(AppConfigurationResponse response);
	}

}
