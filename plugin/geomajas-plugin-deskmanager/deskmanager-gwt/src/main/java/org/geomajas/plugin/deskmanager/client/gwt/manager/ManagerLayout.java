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

import org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints.Blueprints;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.ManagerTab;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Datalayers;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk.Geodesks;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * 
 * @author Oliver May
 *
 */
public class ManagerLayout extends VLayout implements EditSessionHandler {
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private TabSet tabSet;

	public ManagerLayout() {
		super();
		setWidth100();
		setHeight100();

		// ---- main section ----
		tabSet = new TabSet();
		tabSet.setWidth100();
		tabSet.setHeight100();

		Tab loketten = new Tab(MESSAGES.mainTabGeodesks());
		loketten.setPane(new Geodesks());
		tabSet.addTab(loketten);

		Tab lagenBeheerTab = new Tab(MESSAGES.mainTabDataLayers());
		lagenBeheerTab.setPane(new Datalayers());
		tabSet.addTab(lagenBeheerTab);
		if (Role.ADMINISTRATOR.equals(ManagerApplicationLoader.getInstance().getUserProfile()
				.getRole())) {
			Tab blueprintTab = new Tab(MESSAGES.mainTabBlueprints());
			blueprintTab.setPane(new Blueprints());
			tabSet.addTab(blueprintTab);
		}

		addMember(tabSet);
		
		tabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			public void onTabSelected(TabSelectedEvent event) {
				Canvas tab = event.getTab().getPane();
				if (tab != null) {
					((ManagerTab) tab).readData();
				}
			}
		});
		
		Whiteboard.registerHandler(this);
	}

	public void destroy() {
		Whiteboard.unregisterHandler(this);
		super.destroy();
	}


	// -- EditSessionHandler--------------------------------------------------------

	public void onEditSessionChange(EditSessionEvent ese) {
		boolean disabled = ese.isSessionStart();
		for (Tab tab : tabSet.getTabs()) {
			if (tab.getPane() == null || !ese.isParentOfRequestee(tab.getPane())) {
				tab.setDisabled(disabled);
			}
		}
	}

}
