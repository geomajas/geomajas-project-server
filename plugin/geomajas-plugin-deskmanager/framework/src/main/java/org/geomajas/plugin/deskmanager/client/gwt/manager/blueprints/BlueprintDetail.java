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
package org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

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
 * @author Kristof Heirwegh
 */
public class BlueprintDetail extends VLayout implements SelectionChangedHandler, EditSessionHandler, BlueprintHandler {
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private BlueprintDto blueprint;

	private TabSet tabset;

	private BlueprintSettings settings;

	private BlueprintLayers layers;

	private BlueprintLayerTree layerTree;

	private BlueprintAccessRights accessrights;

	private BlueprintGeodeskLayout loketLayout;

	private Label loadingLabel;

	private VLayout loadingLayout;

	private BlueprintThemeConfig themeConfig;

	public BlueprintDetail() {
		super(10);

		
		
		tabset = new TabSet();
		tabset.setTabBarPosition(Side.TOP);
		tabset.setWidth100();
		tabset.setHeight100();
		tabset.setOverflow(Overflow.HIDDEN);

		//This must become more generic...
		settings = new BlueprintSettings();
		Whiteboard.registerHandler(settings);
		Tab tab = new Tab("Instellingen");
		tabset.addTab(tab);
		tab.setPane(settings);

		layers = new BlueprintLayers();
		Whiteboard.registerHandler(layers);
		tab = new Tab("Datalagen");
		tabset.addTab(tab);
		tab.setPane(layers);

		layerTree = new BlueprintLayerTree();
		Whiteboard.registerHandler(layerTree);
		tab = new Tab("Boomstructuur");
		tabset.addTab(tab);
		tab.setPane(layerTree);

		accessrights = new BlueprintAccessRights();
		Whiteboard.registerHandler(accessrights);
		tab = new Tab("Gebruiksrechten");
		tabset.addTab(tab);
		tab.setPane(accessrights);

		loketLayout = new BlueprintGeodeskLayout();
		Whiteboard.registerHandler(loketLayout);
		tab = new Tab("Opmaak");
		tabset.addTab(tab);
		tab.setPane(loketLayout);

		themeConfig = new BlueprintThemeConfig();
		Whiteboard.registerHandler(themeConfig);
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
		Whiteboard.registerHandler((BlueprintHandler) this);
	}

	public void destroy() {
		Whiteboard.unregisterHandler((EditSessionHandler) this);
		Whiteboard.unregisterHandler((BlueprintHandler) this);
		super.destroy();
	}

	public void onSelectionChanged(SelectionEvent event) {
		setDisabled(true);
		if (event.getState()) { // true == selected
			ListGridRecord record = (ListGridRecord) event.getRecord();
			if (record != null && record.getAttributeAsString("id") != null
					&& record.getAttributeAsString("id").length() != 0) {
				loadRecord(record.getAttributeAsString(BlueprintGrid.FLD_ID));
			} else {
				setBlueprint(null);
			}
		}
	}

	private void setLoading() {
		loadingLabel.setContents("<B><i>Configuratie wordt opgehaald...</i> " + "<img src='"
				+ Geomajas.getIsomorphicDir() + "/images/circle.gif' style='height: 1em' /></B>");
		loadingLayout.animateShow(AnimationEffect.FADE);
	}

	private void setLoaded() {
		loadingLayout.animateHide(AnimationEffect.FADE);
		loadingLabel.setContents("");
	}

	private void loadRecord(final String id) {
		setLoading(); /* Clear edit form */
		CommService.getBlueprint(id, new DataCallback<BlueprintDto>() {

			public void execute(BlueprintDto result) {
				setBlueprint(result);
			}
		});
	}

	public void setBlueprint(BlueprintDto blueprint) {
		this.blueprint = blueprint;

		if (blueprint == null) {
			setDisabled(true);
		} else {
			setDisabled(false);
		}

		Whiteboard.fireChangeEvent(new BlueprintEvent(blueprint));
		setLoaded();
	}

	// -- EditSessionHandler --------------------------------------------------------
	public void onEditSessionChange(EditSessionEvent ese) {
		boolean disabled = ese.isSessionStart();
		for (Tab tab : tabset.getTabs()) {
			if (tab.getPane() == null || !ese.isParentOfRequestee(tab.getPane())) {
				tab.setDisabled(disabled);
			}
		}
	}

	// -- BlueprintHandler ---------------------------------------------------------
	public void onBlueprintChange(BlueprintEvent bpe) {
		// refresh data if it's the same record
		if (blueprint != null && blueprint.getId().equals(bpe.getBlueprint().getId())) {
			if (bpe.isDeleted()) {
				setBlueprint(null);
			} else {
				loadRecord(blueprint.getId());
			}
		}
	}
}
