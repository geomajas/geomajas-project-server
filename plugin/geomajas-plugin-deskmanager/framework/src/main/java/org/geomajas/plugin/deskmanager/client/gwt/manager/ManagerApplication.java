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

import org.geomajas.plugin.deskmanager.client.gwt.common.DeskmanagerTokenRequestHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.RolesWindow;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.LoadingScreen;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

import com.smartgwt.client.widgets.layout.Layout;

/**
 * Entry point and main class for deskmanager management application. This entrypoint will show a loading screen and
 * will load the management application, if it's needed asking for a login role.
 * 
 * @author Oliver May
 */
public class ManagerApplication {

	public static final int LOADING_Z_INDEX = 300000;

	private static ManagerApplication instance;

	public static ManagerApplication getInstance() {
		return instance;
	}

	private LoadingScreen loadScreen;

	private ProfileDto profile;

	public void loadManager(final Layout layout) {
		loadScreen = new LoadingScreen();
		loadScreen.setZIndex(LOADING_Z_INDEX);
		loadScreen.draw();

		instance = this;

		ManagerInitializer initializer = new ManagerInitializer();
		initializer.loadManagerApplication(new DeskmanagerTokenRequestHandler(RetrieveRolesRequest.MANAGER_ID,
				new RolesWindow(true)));
		initializer.addHandler(new ManagerInitializationHandler() {

			public void initialized(ProfileDto pr) {
				profile = pr;
				layout.addMember(new ManagerLayout());
				loadScreen.fadeOut();
			}
		});

	}

	public ProfileDto getUserProfile() {
		return profile;
	}

}
