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

import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.client.gwt.common.RolesWindow;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplication;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplicationRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.common.RolesWindow.AskRoleCallback;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.LoadingScreen;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.event.UserApplicationEvent;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.event.UserApplicationHandler;
import org.geomajas.plugin.deskmanager.command.common.dto.GetApplicationInfoResponse;
import org.geomajas.widget.searchandfilter.client.util.GsfLayout;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;
import org.geomajas.widget.searchandfilter.client.widget.search.DockableWindowSearchWidget.SearchWindowPositionType;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;


import com.google.gwt.dom.client.Document;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point and main class for deskmanager applications. This entrypoint will show a loading screen and will load
 * the deskmanager application, if it's needed asking for a login role.
 * 
 * The entrypoint listens to Mapwidget and MapModel events to set some generic configuration options.
 * 
 * @author Oliver May
 */
public class GeodeskApplication implements UserApplicationHandler {

	private UserApplication loket;

	private LoadingScreen loadScreen;

	/**
	 * Constructor for the GeodeskApplication, this will create a GWT entrypoint.
	 */
	public GeodeskApplication() {
		// register Global layout stuff
		GuwLayout.ribbonBarInternalMargin = 2;
		GuwLayout.ribbonGroupInternalMargin = 4;

		GuwLayout.DropDown.ribbonBarDropDownButtonDescriptionIconSize = 48;

		GuwLayout.ribbonBarOverflow = Overflow.HIDDEN;
		GuwLayout.ribbonTabOverflow = Overflow.HIDDEN;

		GsfLayout.searchWindowPositionType = SearchWindowPositionType.SNAPTO;
		GsfLayout.searchWindowPosSnapTo = "BR";
		GsfLayout.searchWindowPosLeft = -5;
		GsfLayout.searchWindowPosTop = -172;

		SearchCommService.searchResultSize = 500;

		
	}

	/**
	 * Ask for the correct user role and load the application. TODO: extract to interface, this is specific to the used
	 * security model.
	 */
	public void loadApplication(final Layout parentLayout) {
		// First Install a loading screen
		// FIXME: i18n
		loadScreen = new LoadingScreen();
		loadScreen.setZIndex(GeodeskLayout.loadingZindex);
		loadScreen.draw();

		// Ask for the correct user role. FIXME: extract to a specific class
		// FIXME: change to TokenRequestHandler (see GwtCommandDispatcher)
		RolesWindow panel = new RolesWindow();
		panel.askRole(new AskRoleCallback() {

			public void execute(String token) {
				// Load application with specific token
				GwtCommandDispatcher.getInstance().setUserToken(token);
				// Open het opgevraagde loket
				GwtCommand openLoketCommandRequest = new GwtCommand(GetApplicationInfoResponse.COMMAND);

				final AbstractCommandCallback<GetApplicationInfoResponse> openLoketCallback = 
					new AbstractCommandCallback<GetApplicationInfoResponse>() {

					public void execute(GetApplicationInfoResponse loketResponse) {
						GeodeskLayout.version = loketResponse.getDeskmanagerVersion();
						GeodeskLayout.build = loketResponse.getDeskmanagerBuild();

						// Load geodesk from registry
						loket = UserApplicationRegistry.getInstance().get(loketResponse.getGeodeskTypeIdentifier());
						loket.addUserApplicationLoadedHandler(GeodeskApplication.this);

						loket.setApplicationId(loketResponse.getGeodeskIdentifier());
						loket.setClientApplicationInfo(loketResponse.getClientApplicationInfo());
						
						// Register the geodesk to the loading screen (changes banner, and name), and set the page title
						loadScreen.registerLoket(loket);
						if (null != Document.get()) {
							Document.get().setTitle(loket.getName());
						}

						// Load the geodesk
						// Build main layout
						VLayout layout = new VLayout();
						layout.setWidth100();
						layout.setHeight100();
						layout.setMargin(0);
						Layout loketLayout = loket.loadGeodesk();
						// Finaly add the geodesk to the main layout and draw
						layout.addMember(loketLayout);

						GsfLayout.searchWindowParentElement = layout;
						parentLayout.addMember(layout);
					}

					public void onCommandException(CommandResponse response) {
						// Vraag welke rol
						for (ExceptionDto exception : response.getExceptions()) {
							if (GeodeskLayout.EXCEPTIONCODE_LOKETINACTIVE == exception.getExceptionCode()) {
								SC.warn(exception.getMessage());
							} else {
								SC.warn("Er is een fout opgetreden: " + exception.getMessage());
							}
						}
					}

				};

				GwtCommandDispatcher.getInstance().execute(openLoketCommandRequest, openLoketCallback);
			}
		});
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
		if (loket != null) {
			//MapWidget mapWidget = loket.getMainMapWidget();
			// Register to Javascript api
			// FIXME: re enable javascript api
			// GeomajasServiceImpl.getInstance().registerMap(mapWidget.getApplicationId(), mapWidget.getID(),
			// new MapImpl(mapWidget));
		}
	}

}
