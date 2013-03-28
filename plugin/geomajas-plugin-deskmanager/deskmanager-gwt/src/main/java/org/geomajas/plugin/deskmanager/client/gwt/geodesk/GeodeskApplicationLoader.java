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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplication;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplicationRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerTokenRequestHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.RolesWindow;
import org.geomajas.plugin.deskmanager.client.gwt.common.util.GeodeskUrlUtil;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.event.UserApplicationEvent;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.event.UserApplicationHandler;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.i18n.GeodeskMessages;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.impl.LoadingScreen;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * Main class for deskmanager applications. This entrypoint will show a loading screen and will load the deskmanager
 * application, if it's needed asking for a login role.
 * 
 * The entrypoint listens to Mapwidget and MapModel events to set some generic configuration options.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api (allMethods = true)
public class GeodeskApplicationLoader {

	private static final GeodeskMessages MESSAGES = GWT.create(GeodeskMessages.class);

	private UserApplication geodesk;

	private LoadingScreen loadScreen;

	/**
	 * Constructor for the GeodeskApplicationLoader.
	 */
	public GeodeskApplicationLoader() {
	}

	/**
	 * Load a geodesk application. If needed this will first ask for the correct user role and then load the 
	 * application.
	 * 
	 * The presentation is added to the layout using a {@link UserApplication}, the key for this application is
	 * loaded from the configuration. User application must be registered to the {@link UserApplicationRegistry}.
	 * 
	 * @param callback called when everything is drawn and ready to add to the application
	 */
	public void loadApplication(final LoadingCallback callback) {
		loadApplication(callback, null);
	}
	
	/**
	 * Load a geodesk application. If needed this will first ask for the correct user role and then load the 
	 * application.
	 * 
	 * The presentation is added to the layout using a {@link UserApplication}, the key for this application is
	 * loaded from the configuration. User application must be registered to the {@link UserApplicationRegistry}.
	 * 
	 * You can add a user application handler that is called once the user application is loaded and added to the 
	 * layout.
	 * 
	 * @param callback called when everything is drawn and ready to add to the layout
	 * @param handler the user application handler
	 */
	public void loadApplication(final LoadingCallback callback, final UserApplicationHandler handler) {
		// First Install a loading screen
		// FIXME: i18n
		loadScreen = new LoadingScreen();
		loadScreen.setZIndex(GdmLayout.loadingZindex);
		loadScreen.draw();

		String geodeskId = GeodeskUrlUtil.getGeodeskId();
		if (geodeskId == null) {
			Window.alert(MESSAGES.noGeodeskIdGivenError());
			return;
		}

		GeodeskInitializer initializer = new GeodeskInitializer();
		initializer.addHandler(new GeodeskInitializationHandler() {

			public void initialized(InitializeGeodeskResponse response) {
				GdmLayout.version = response.getDeskmanagerVersion();
				GdmLayout.build = response.getDeskmanagerBuild();

				// Load geodesk from registry
				geodesk = UserApplicationRegistry.getInstance().get(response.getUserApplicationKey());

				geodesk.setApplicationId(response.getGeodeskIdentifier());
				geodesk.setClientApplicationInfo(response.getClientApplicationInfo());

				// Register the geodesk to the loading screen (changes banner, and name), and set the page title
				loadScreen.registerGeodesk(geodesk);
				if (null != Document.get()) {
					Document.get().setTitle(geodesk.getName());
				}

				// Load the geodesk
				// Build main layout
				callback.loaded(geodesk.loadGeodesk());
				
				if (handler != null) {
					handler.onUserApplicationLoad(new UserApplicationEvent(geodesk));
				}
			}
		});

		// Get application info for the geodesk
		initializer.loadApplication(geodeskId, new DeskmanagerTokenRequestHandler(geodeskId, new RolesWindow(false)));
	}
	
	/**
	 * Callback that is called when the geodesk is loaded.
	 * 
	 * @author Oliver May
	 *
	 */
	public interface LoadingCallback {
		void loaded(Widget w);
	}

}
