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
import org.geomajas.plugin.deskmanager.configuration.client.GeodeskLayoutInfo;
import org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid;

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

	public AbstractUserApplication() {
		handlerManager = new HandlerManager(this);
		geodeskLayout = new GeodeskLayoutInfo();
		geodeskLayout.setTitle("[ClientApplicationInfo is niet gezet!]");

		GwtCommandCallback cb = new DeskmanagerGwtCommandCallback();
		GwtCommandDispatcher.getInstance().setCommandExceptionCallback(cb);
		GwtCommandDispatcher.getInstance().setCommunicationExceptionCallback(cb);
	}

	private HandlerManager handlerManager;

	private ClientApplicationInfo clientApplicationInfo;

	private GeodeskLayoutInfo geodeskLayout;

	private String geodeskId;

	private FeatureSelectionInfoWindow fsiw = new FeatureSelectionInfoWindow();

	private Window searchResult;

	private MultiFeatureListGrid searchResultGrid;

	public HandlerRegistration addUserApplicationLoadedHandler(final UserApplicationHandler userApplicationHandler) {
		return handlerManager.addHandler(UserApplicationEvent.TYPE, userApplicationHandler);
	}

	public void fireUserApplicationEvent() {
		handlerManager.fireEvent(new UserApplicationEvent(this));
		initDefaultTools();
	}

	public String getName() {
		return geodeskLayout.getTitle();
	}

	public String getBannerUrl() {
		return geodeskLayout.getBannerUrl();
	}

	public void setClientApplicationInfo(ClientApplicationInfo clientAppInfo) {
		clientApplicationInfo = clientAppInfo;
		geodeskLayout = (GeodeskLayoutInfo) getClientApplicationInfo().getWidgetInfo().get(
				GeodeskLayoutInfo.IDENTIFIER);
	}

	protected ClientApplicationInfo getClientApplicationInfo() {
		return clientApplicationInfo;
	}

	public GeodeskLayoutInfo getVaudi() {
		return geodeskLayout;
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
	 * @param searchResultGrid
	 *            the searchResultGrid to set
	 */
	protected void setSearchResultGrid(MultiFeatureListGrid searchResultGrid) {
		this.searchResultGrid = searchResultGrid;
	}

	/**
	 * @return the searchResultGrid
	 */
	protected MultiFeatureListGrid getSearchResultGrid() {
		return searchResultGrid;
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
}
