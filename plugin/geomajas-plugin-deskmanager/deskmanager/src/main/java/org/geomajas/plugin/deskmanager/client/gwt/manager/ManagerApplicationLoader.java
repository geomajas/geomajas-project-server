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
package org.geomajas.plugin.deskmanager.client.gwt.manager;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerTokenRequestHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.RolesWindow;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.impl.LoadingScreen;
import org.geomajas.plugin.deskmanager.client.gwt.manager.impl.ManagerInitializer;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

import com.smartgwt.client.widgets.layout.Layout;

/**
 * Entry point and main class for deskmanager management application. This entrypoint will show a loading screen and
 * will load the management application, if it's needed asking for a login role.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public final class ManagerApplicationLoader {

	public static final int LOADING_Z_INDEX = 300000;

	private static final ManagerApplicationLoader INSTANCE = new ManagerApplicationLoader();

	// Hide default constructor.
	private ManagerApplicationLoader() {
	}

	/**
	 * Get instance of this loader.
	 * 
	 * @return the loader.
	 */
	public static ManagerApplicationLoader getInstance() {
		return INSTANCE;
	}

	private LoadingScreen loadScreen;

	private ProfileDto profile;

	/**
	 * Loads the manager application, asks for the correct role, and adds it to the given layout. Calls the
	 * initialization handler when finished;
	 * 
	 * @param parentLayout
	 *            the layout.
	 */
	public void loadManager(final Layout parentLayout, ManagerInitializationHandler handler) {
		loadScreen = new LoadingScreen();
		loadScreen.setZIndex(LOADING_Z_INDEX);
		loadScreen.draw();

		ManagerInitializer initializer = new ManagerInitializer();
		initializer.loadManagerApplication(new DeskmanagerTokenRequestHandler(RetrieveRolesRequest.MANAGER_ID,
				new RolesWindow(true)));
		if (handler != null) {
			initializer.addHandler(handler);
		}
		initializer.addHandler(new ManagerInitializationHandler() {

			public void initialized(ProfileDto pr) {
				profile = pr;
				parentLayout.addMember(new ManagerLayout());
				loadScreen.fadeOut();
			}
		});
	}

	/**
	 * Loads the manager application, asks for the correct role, and adds it to the given layout.
	 * 
	 * @param parentLayout
	 *            the layout.
	 */
	public void loadManager(final Layout parentLayout) {
		loadManager(parentLayout, null);
	}

	/**
	 * Return the currently logged in user profile.
	 * 
	 * @return the user profile.
	 */
	public ProfileDto getUserProfile() {
		return profile;
	}

}
