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
package org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

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
 * @author Jan De Moerloose
 */
public class GeodeskDetail extends VLayout implements SelectionChangedHandler, EditSessionHandler, GeodeskHandler {

	private GeodeskDto geodesk;

	private TabSet tabset;

	private GeodeskSettings settings;

	private GeodeskLayers layers;

	private GeodeskAccessRights accessrights;

	private GeodeskLayout geodeskLayout;

	private GeodeskHtmlCode htmlCode;

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
		Tab tab = new Tab("Instellingen");
		tabset.addTab(tab);
		tab.setPane(settings);

		layers = new GeodeskLayers();
		tab = new Tab("Datalagen");
		tabset.addTab(tab);
		tab.setPane(layers);
		

		layerTree = new GeodeskLayerTree();
		tab = new Tab("Boomstructuur");
		tabset.addTab(tab);
		tab.setPane(layerTree);

		accessrights = new GeodeskAccessRights();
		accessrightsTab = new Tab("Gebruiksrechten");
		tabset.addTab(accessrightsTab);
		accessrightsTab.setPane(accessrights);
		accessrightsTab.setDisabled(true);

		notifications = new GeodeskNotifications();
		tab = new Tab("Notificaties");
		tabset.addTab(tab);
		tab.setPane(notifications);

		geodeskLayout = new GeodeskLayout();
		tab = new Tab("Opmaak");
		tabset.addTab(tab);
		tab.setPane(geodeskLayout);

		htmlCode = new GeodeskHtmlCode();
		tab = new Tab("HTML Code");
		tabset.addTab(tab);
		tab.setPane(htmlCode);

		themeConfig = new GeodeskThemeConfig();
		tab = new Tab("Themas");
		tabset.addTab(tab);
		tab.setPane(themeConfig);

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
			if (record != null && record.getAttributeAsString("id") != null
					&& record.getAttributeAsString("id").length() != 0) {
				loadRecord(record.getAttributeAsString(GeodeskGrid.FLD_ID));
			} else {
				setGeodesk(null);
			}
		}
	}

	private void setLoading() {
		loadingLabel.setContents("<B><i>Configuratie wordt opgehaald...</i> <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></B>");
		loadingLayout.animateShow(AnimationEffect.FADE);
	}

	private void setLoaded() {
		loadingLayout.animateHide(AnimationEffect.FADE);
		loadingLabel.setContents("");
	}

	private void loadRecord(final String id) {
		setLoading(); /* Clear edit form */
		CommService.getGeodesk(id, new DataCallback<GeodeskDto>() {

			public void execute(GeodeskDto result) {
				setGeodesk(result);
			}
		});
	}

	public void setGeodesk(GeodeskDto geodesk) {
		this.geodesk = geodesk;

		//FIXME: let panels listen for an event instead of these setters
		settings.setGeodesk(geodesk);
		layers.setGeodesk(geodesk);
		accessrights.setGeodesk(geodesk);
		geodeskLayout.setGeodesk(geodesk);
		htmlCode.setGeodesk(geodesk);
		notifications.setGeodesk(geodesk);
		themeConfig.setGeodesk(geodesk);
		

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
