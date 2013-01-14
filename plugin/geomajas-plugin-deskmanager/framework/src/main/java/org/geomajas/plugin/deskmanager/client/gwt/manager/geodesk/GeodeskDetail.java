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
package org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * TODO.
 * 
 * FIXME: the panels should come from a factory, see GDM-13.
 * 
 * @author Jan De Moerloose
 */
public class GeodeskDetail extends VLayout implements SelectionChangedHandler, EditSessionHandler, GeodeskHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private static final String ID_ATTRIBUTE = "id";
	
	private GeodeskDto geodesk;

	private TabSet tabset;

	private GeodeskSettings settings;

	private GeodeskLayers layers;

	private GeodeskAccessRights accessrights;

	private GeodeskLayout geodeskLayout;

	private GeodeskNotifications notifications;

	private Tab accessrightsTab;

	private Label loadingLabel;

	private VLayout loadingLayout;

	private GeodeskThemeConfig themeConfig;

	private GeodeskLayerTree layerTree;

	public GeodeskDetail() {
		super(10);

		tabset = new TabSet();
		tabset.setTabBarPosition(Side.TOP);
		tabset.setWidth100();
		tabset.setHeight100();
		tabset.setOverflow(Overflow.HIDDEN);

		settings = new GeodeskSettings();
		Tab tab = new Tab(MESSAGES.geodeskDetailTabSettings());
		tabset.addTab(tab);
		tab.setPane(settings);
		Whiteboard.registerHandler(settings);

		layers = new GeodeskLayers();
		tab = new Tab(MESSAGES.geodeskDetailTabDataLayers());
		tabset.addTab(tab);
		tab.setPane(layers);
		Whiteboard.registerHandler(layers);
		

		layerTree = new GeodeskLayerTree();
		tab = new Tab(MESSAGES.geodeskDetailTabLayerTree());
		tabset.addTab(tab);
		tab.setPane(layerTree);
		Whiteboard.registerHandler(layerTree);

		
		accessrights = new GeodeskAccessRights();
		accessrightsTab = new Tab(MESSAGES.geodeskDetailTabAccessRights());
		tabset.addTab(accessrightsTab);
		accessrightsTab.setPane(accessrights);
		accessrightsTab.setDisabled(true);
		Whiteboard.registerHandler(accessrights);
		

		notifications = new GeodeskNotifications();
		tab = new Tab(MESSAGES.geodeskDetailTabNotifications());
		tabset.addTab(tab);
		tab.setPane(notifications);
		Whiteboard.registerHandler(notifications);
		
// FIXME: Disabled -> should be moved to custom project
		geodeskLayout = new GeodeskLayout();
		tab = new Tab(MESSAGES.geodeskDetailTabLayout());
		tabset.addTab(tab);
		tab.setPane(geodeskLayout);
		Whiteboard.registerHandler(geodeskLayout);
		

		themeConfig = new GeodeskThemeConfig();
		tab = new Tab(MESSAGES.geodeskDetailTabThemes());
		tabset.addTab(tab);
		tab.setPane(themeConfig);
		Whiteboard.registerHandler(themeConfig);
		

		// loading widget
		loadingLayout = new VLayout();
		loadingLayout.setWidth100();
		loadingLayout.setHeight100();
		loadingLayout.setOpacity(70);
		loadingLayout.setBackgroundColor("ffffff");

		loadingLabel = new Label();
		loadingLabel.setTop("30%");
		loadingLabel.setAlign(Alignment.CENTER);

		loadingLayout.addMember(loadingLabel);
		loadingLayout.hide();

		addMember(tabset);
		addChild(loadingLayout);
		setDisabled(true);

		Whiteboard.registerHandler((EditSessionHandler) this);
		Whiteboard.registerHandler((GeodeskHandler) this);
	}

	public void destroy() {
		Whiteboard.unregisterHandler((EditSessionHandler) this);
		Whiteboard.unregisterHandler((GeodeskHandler) this);
		super.destroy();
	}

	public void onSelectionChanged(SelectionEvent event) {
		setDisabled(true);
		if (event.getState()) { // true == selected
			ListGridRecord record = (ListGridRecord) event.getRecord();
			if (record != null && record.getAttributeAsString(ID_ATTRIBUTE) != null
					&& record.getAttributeAsString(ID_ATTRIBUTE).length() != 0) {
				loadRecord(record.getAttributeAsString(GeodeskGrid.FLD_ID));
			} else {
				setGeodesk(null);
			}
		}
	}

	private void setLoading() {
		loadingLabel.setContents("<B><i>" + MESSAGES.loadingConfig() + "</i> <img src='"
				+ Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></B>");
		loadingLayout.animateShow(AnimationEffect.FADE);
	}

	private void setLoaded() {
		loadingLayout.animateHide(AnimationEffect.FADE);
		loadingLabel.setContents("");
	}

	private void loadRecord(final String id) {
		setLoading(); /* Clear edit form */
		ManagerCommandService.getGeodesk(id, new DataCallback<GeodeskDto>() {

			public void execute(GeodeskDto result) {
				setGeodesk(result);
			}
		});
	}

	public void setGeodesk(GeodeskDto geodesk) {
		this.geodesk = geodesk;

		if (geodesk == null) {
			setDisabled(true);
		} else {
			// public geodesks don't have accessrights
			accessrightsTab.setDisabled(geodesk.isPublic());
			if (geodesk.isPublic() && tabset.getSelectedTab().equals(accessrightsTab)) {
				tabset.selectTab(0);
			}
			setDisabled(false);
		}
		setLoaded();
		Whiteboard.fireChangeEvent(new GeodeskEvent(geodesk));
	}

	// -- EditSessionHandler --------------------------------------------------------
	public void onEditSessionChange(EditSessionEvent ese) {
		boolean disabled = ese.isSessionStart();
		for (Tab tab : tabset.getTabs()) {
			if (tab.getPane() == null || !ese.isParentOfRequestee(tab.getPane())) {
				if (!disabled && accessrightsTab.equals(tab) && geodesk != null && geodesk.isPublic()) {
					tab.setDisabled(true);
				}
				tab.setDisabled(disabled);
			}
		}
	}

	// -- GeodeskHandler ---------------------------------------------------------
	public void onGeodeskChange(GeodeskEvent le) {
		// refresh data if it's the same record
		if (geodesk != null && geodesk.getId().equals(le.getGeodesk().getId())) {
			if (le.isDeleted()) {
				setGeodesk(null);
			} else {
				loadRecord(geodesk.getId());
			}
		}
	}
}
