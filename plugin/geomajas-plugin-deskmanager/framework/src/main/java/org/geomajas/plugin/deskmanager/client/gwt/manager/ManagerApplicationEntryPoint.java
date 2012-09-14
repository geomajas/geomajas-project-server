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
package org.geomajas.plugin.deskmanager.client.gwt.manager;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.client.gwt.common.RolesWindow;
import org.geomajas.plugin.deskmanager.client.gwt.common.RolesWindow.AskRoleCallback;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.LoadingScreen;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetManagerUserProfileRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetManagerUserProfileResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.Layout;

/**
 * 
 * @author Oliver May
 *
 */
public class ManagerApplicationEntryPoint {

	public static final int LOADING_Z_INDEX = 300000;

	private static ManagerApplicationEntryPoint instance;

	public static ManagerApplicationEntryPoint getInstance() {
		return instance;
	}

	private LoadingScreen loadScreen;

	private ProfileDto profile;

	public void loadManager(Layout layout) {
		loadScreen = new LoadingScreen();
		loadScreen.setZIndex(LOADING_Z_INDEX);
		loadScreen.draw();

		instance = this;
		loadBeheersModule(layout);
	}

	private void loadBeheersModule(final Layout parentLayout) {
		GwtCommand request = new GwtCommand(GetManagerUserProfileRequest.COMMAND);

		final AbstractCommandCallback<GetManagerUserProfileResponse> openLoketCallback =
				new AbstractCommandCallback<GetManagerUserProfileResponse>() {

			public void execute(GetManagerUserProfileResponse response) {
				profile = response.getProfile();
				loadScreen.destroy();
				final Canvas layout = new ManagerLayout();
				parentLayout.addMember(layout);
			}

			public void onCommandException(CommandResponse response) {
				// Vraag welke rol
				RolesWindow panel = new RolesWindow(true);

				panel.askRole(new AskRoleCallback() {

					public void execute(String token) {
						// Laad loket met nieuwe rol
						GwtCommandDispatcher.getInstance().setUserToken(token);
						loadBeheersModule(parentLayout);
					}
				});
			}
		};

		GwtCommandDispatcher.getInstance().execute(request, openLoketCallback);
	}

	// ----------------------------------------------------------

	public ProfileDto getUserProfile() {
		return profile;
	}

}
