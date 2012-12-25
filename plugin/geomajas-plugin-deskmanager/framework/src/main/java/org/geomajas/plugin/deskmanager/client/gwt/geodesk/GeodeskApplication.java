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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk;

import org.geomajas.plugin.deskmanager.client.gwt.common.DeskmanagerTokenRequestHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.GeodeskInitializationHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.GeodeskInitializer;
import org.geomajas.plugin.deskmanager.client.gwt.common.RolesWindow;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplication;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplicationRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.common.util.GeodeskUrlUtil;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.i18n.GeodeskMessages;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.LoadingScreen;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.event.UserApplicationEvent;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.event.UserApplicationHandler;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskResponse;
import org.geomajas.widget.searchandfilter.client.util.GsfLayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Main class for deskmanager applications. This entrypoint will show a loading screen and will load the deskmanager
 * application, if it's needed asking for a login role.
 * 
 * The entrypoint listens to Mapwidget and MapModel events to set some generic configuration options.
 * 
 * @author Oliver May
 */
public class GeodeskApplication implements UserApplicationHandler {

	private static final GeodeskMessages MESSAGES = GWT.create(GeodeskMessages.class);

	private UserApplication geodesk;

	private LoadingScreen loadScreen;

	/**
	 * Constructor for the GeodeskApplication.
	 */
	public GeodeskApplication() {
	}

	/**
	 * Ask for the correct user role and load the application. TODO: extract to interface, this is specific to the used
	 * security model.
	 * 
	 * The resentation is added to the layout using a {@link UserApplication}, the key for this user application is
	 * loaded from the configuration. User applications must be registered to the {@link UserApplicationRegistry}.
	 */
	public void loadApplication(final Layout parentLayout) {
		// First Install a loading screen
		// FIXME: i18n
		loadScreen = new LoadingScreen();
		loadScreen.setZIndex(GeodeskLayout.loadingZindex);
		loadScreen.draw();

		String geodeskId = GeodeskUrlUtil.getGeodeskId();
		if (geodeskId == null) {
			SC.warn(MESSAGES.noGeodeskIdGivenError());
			return;
		}

		GeodeskInitializer initializer = new GeodeskInitializer();
		initializer.addHandler(new GeodeskInitializationHandler() {

			public void initialized(InitializeGeodeskResponse response) {
				GeodeskLayout.version = response.getDeskmanagerVersion();
				GeodeskLayout.build = response.getDeskmanagerBuild();

				// Load geodesk from registry
				geodesk = UserApplicationRegistry.getInstance().get(response.getGeodeskTypeIdentifier());
				geodesk.addUserApplicationLoadedHandler(GeodeskApplication.this);

				geodesk.setApplicationId(response.getGeodeskIdentifier());
				geodesk.setClientApplicationInfo(response.getClientApplicationInfo());

				// Register the geodesk to the loading screen (changes banner, and name), and set the page title
				loadScreen.registerGeodesk(geodesk);
				if (null != Document.get()) {
					Document.get().setTitle(geodesk.getName());
				}

				// Load the geodesk
				// Build main layout
				VLayout layout = new VLayout();
				layout.setWidth100();
				layout.setHeight100();
				layout.setMargin(0);
				Layout loketLayout = geodesk.loadGeodesk();
				// Finaly add the geodesk to the main layout and draw
				layout.addMember(loketLayout);

				GsfLayout.searchWindowParentElement = layout;
				parentLayout.addMember(layout);
			}
		});

		// Get application info for the geodesk
		initializer.loadApplication(geodeskId, new DeskmanagerTokenRequestHandler(geodeskId, new RolesWindow(false)));
	}

	/**
	 * {@inheritDoc}
	 */
	public void onUserApplicationLoad(UserApplicationEvent event) {
		// Listen for mapModelChanges
		event.getUserApplication().getMainMapWidget().getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				onMapModelInitialized();
			}
		});
	}

	public void onMapModelInitialized() {
		if (geodesk != null) {
			// MapWidget mapWidget = geodesk.getMainMapWidget();
			// Register to Javascript api
			// FIXME: re enable javascript api
			// GeomajasServiceImpl.getInstance().registerMap(mapWidget.getApplicationId(), mapWidget.getID(),
			// new MapImpl(mapWidget));
		}
	}

}
