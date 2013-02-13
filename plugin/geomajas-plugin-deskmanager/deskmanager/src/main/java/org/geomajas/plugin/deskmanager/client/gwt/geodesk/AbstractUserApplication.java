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

import java.util.Collections;
import java.util.List;

import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.gwt.client.GwtCommandCallback;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplication;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.service.DeskmanagerGwtCommandCallback;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.FeatureSelectionInfoWindow;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.event.UserApplicationEvent;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.event.UserApplicationHandler;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.Window;

/**
 * Abstract deskmanager user application.
 * 
 * @author Oliver May
 * 
 */
public abstract class AbstractUserApplication implements UserApplication {

	// private static final GeodeskMessages MESSAGES = GWT.create(GeodeskMessages.class);

	public AbstractUserApplication() {
		handlerManager = new HandlerManager(this);

		GwtCommandCallback cb = new DeskmanagerGwtCommandCallback();
		GwtCommandDispatcher.getInstance().setCommandExceptionCallback(cb);
		GwtCommandDispatcher.getInstance().setCommunicationExceptionCallback(cb);
	}

	private HandlerManager handlerManager;

	private ClientApplicationInfo clientApplicationInfo;


	private String geodeskId;

	private FeatureSelectionInfoWindow fsiw = new FeatureSelectionInfoWindow();

	private Window searchResult;

	public HandlerRegistration addUserApplicationLoadedHandler(final UserApplicationHandler userApplicationHandler) {
		return handlerManager.addHandler(UserApplicationEvent.TYPE, userApplicationHandler);
	}

	public void fireUserApplicationEvent() {
		handlerManager.fireEvent(new UserApplicationEvent(this));
		initDefaultTools();
	}

	public void setClientApplicationInfo(ClientApplicationInfo clientAppInfo) {
		clientApplicationInfo = clientAppInfo;
	}

	protected ClientApplicationInfo getClientApplicationInfo() {
		return clientApplicationInfo;
	}

	private void initDefaultTools() {
		getMainMapWidget().getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {

			public void onMapModelChanged(MapModelChangedEvent event) {
				// // -- get default action so it gets initialized
				// defaultToolAction = ToolbarRegistry.getToolbarAction(DEFAULT_TOOL_ID, getMapWidget());
				fsiw.initialize(getMainMapWidget());
				fsiw.setLeftPosition(75);
			}
		});
	}

	// ----------------------------------------------------------
	// -- Search -- // needs to be initialized in child classes
	// ----------------------------------------------------------

	public void showSearchResultWindow() {
		searchResult.show();
		searchResult.bringToFront();
	}

	/**
	 * @param geodeskId
	 *            the geodeskId to set
	 */
	public void setApplicationId(String applicationId) {
		this.geodeskId = applicationId;
	}

	/**
	 * @return the geodeskId
	 */
	protected String getApplicationId() {
		return geodeskId;
	}

	/**
	 * @return the searchResult
	 */

	protected Window getSearchResult() {
		return searchResult;
	}

	/**
	 * @param searchResult
	 *            the searchResult to set
	 */
	protected void setSearchResult(Window searchResult) {
		this.searchResult = searchResult;
	}

	@Override
	public List<String> getSupportedApplicationWidgetKeys() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getSupportedMainMapWidgetKeys() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getSupportedOverviewMapWidgetKeys() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getSupportedLayerWidgetKeys() {
		return Collections.emptyList();
	}

}
