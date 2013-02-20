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
package org.geomajas.plugin.deskmanager.client.gwt.manager.blueprints;

import java.util.Map;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplication;
import org.geomajas.plugin.deskmanager.client.gwt.common.UserApplicationRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractWoaHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.BaseGeodeskWidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactory;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactoryRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.BlueprintHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.GeodeskDtoUtil;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Detail panel for a blueprint, contains the different configuration tabs and automatically adds widget editors that
 * are supported by the user application.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 */
public class BlueprintDetail extends HLayout implements SelectionChangedHandler, EditSessionHandler, BlueprintHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private static final String ID_ATTRIBUTE = "id";

	private BlueprintDto blueprint;

	private TabSet tabset;

	private BlueprintSettings settings;

	private BlueprintLayers layers;

	private BlueprintAccessRights accessrights;

	private Label loadingLabel;

	private VLayout loadingLayout;

	private TabSet widgetTabset;

	public BlueprintDetail() {
		super();
		setMargin(10);

		tabset = new TabSet();
		tabset.setTabBarPosition(Side.TOP);
		tabset.setWidth100();
		tabset.setHeight100();
		tabset.setOverflow(Overflow.HIDDEN);

		// This must become more generic...
		settings = new BlueprintSettings();
		Whiteboard.registerHandler(settings);
		Tab tab = new Tab(MESSAGES.blueprintDetailTabSettings());
		tabset.addTab(tab);
		tab.setPane(settings);

		layers = new BlueprintLayers();
		Whiteboard.registerHandler(layers);
		tab = new Tab(MESSAGES.blueprintDetailTabDataLayers());
		tabset.addTab(tab);
		tab.setPane(layers);

		accessrights = new BlueprintAccessRights();
		Whiteboard.registerHandler(accessrights);
		tab = new Tab(MESSAGES.blueprintDetailTabAccessRights());
		tabset.addTab(tab);
		tab.setPane(accessrights);

		//Widget tabs
		tab = new Tab(MESSAGES.geodeskDetailTabWidgets());
		widgetTabset = new TabSet();
		widgetTabset.setTabBarPosition(Side.LEFT);
		widgetTabset.setWidth100();
		widgetTabset.setHeight100();
		widgetTabset.setOverflow(Overflow.HIDDEN);
		widgetTabset.setTabBarThickness(100);
		tab.setPane(widgetTabset);
		
		tabset.addTab(tab);
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
			if (record != null && record.getAttributeAsString(ID_ATTRIBUTE) != null
					&& record.getAttributeAsString(ID_ATTRIBUTE).length() != 0) {
				loadRecord(record.getAttributeAsString(BlueprintGrid.FLD_ID));
			} else {
				setBlueprint(null);
			}
		}
	}

	private void setLoading() {
		loadingLabel.setContents("<B><i>" + MESSAGES.blueprintDetailLoadingConfig() + "</i> " + "<img src='"
				+ Geomajas.getIsomorphicDir() + "/images/circle.gif' style='height: 1em' /></B>");
		loadingLayout.animateShow(AnimationEffect.FADE);
	}

	private void setLoaded() {
		loadingLayout.animateHide(AnimationEffect.FADE);
		loadingLabel.setContents("");
	}

	private void loadRecord(final String id) {
		clearWidgetTabs();
		setLoading(); /* Clear edit form */
		ManagerCommandService.getBlueprint(id, new DataCallback<BlueprintDto>() {

			public void execute(BlueprintDto result) {
				loadWidgetTabs(result);
				setBlueprint(result);
			}
		});
	}

	/**
	 * Clear all custom widget tabs from the last blueprint.
	 */
	private void clearWidgetTabs() {
		for (Tab tab : widgetTabset.getTabs()) {
			widgetTabset.removeTab(tab);
		}
	}

	/**
	 * Load all widget editors that are available on this blueprints user application, and add them to the tabset.
	 * 
	 * @param bp
	 *            the blueprint.
	 */
	private void loadWidgetTabs(BaseGeodeskDto bp) {
		UserApplication ua = UserApplicationRegistry.getInstance().get(bp.getUserApplicationInfo().getKey());
		for (String key : ua.getSupportedApplicationWidgetKeys()) {
			addWidgetTab(WidgetEditorFactoryRegistry.getMapRegistry().get(key), bp.getApplicationClientWidgetInfos(),
					GeodeskDtoUtil.getApplicationClientWidgetInfo(bp), bp);
		}
		for (String key : ua.getSupportedMainMapWidgetKeys()) {
			addWidgetTab(WidgetEditorFactoryRegistry.getMapRegistry().get(key), bp.getMainMapClientWidgetInfos(),
					GeodeskDtoUtil.getMainMapClientWidgetInfo(bp), bp);
		}
		for (String key : ua.getSupportedOverviewMapWidgetKeys()) {
			addWidgetTab(WidgetEditorFactoryRegistry.getMapRegistry().get(key), bp.getOverviewMapClientWidgetInfos(),
					GeodeskDtoUtil.getOverviewMapClientWidgetInfo(bp), bp);
		}
	}

	/**
	 * Add a widget editor tab to the tabset for a given editor factory, set of widget info's (where one of will be
	 * edited by the editor) and a base geodesk that could provide extra context to the editor.
	 * 
	 * @param editorFactory
	 *            the editor factory
	 * @param blueprintWidgetInfos
	 *            the blueprint widget infos
	 * @param widgetInfos
	 *            all the widget infos (including inherited)
	 * @param geodesk
	 *            the geodesk
	 */
	private void addWidgetTab(final WidgetEditorFactory editorFactory,
			final Map<String, ClientWidgetInfo> blueprintWidgetInfos, final Map<String, ClientWidgetInfo> widgetInfos,
			BaseGeodeskDto geodesk) {
		if (editorFactory != null) {
			Tab tab = new Tab(editorFactory.getName());
			final WidgetEditor editor = editorFactory.createEditor();
			if (editor instanceof BaseGeodeskWidgetEditor) {
				((BaseGeodeskWidgetEditor) editor).setBaseGeodesk(geodesk);
			}
			editor.setWidgetConfiguration(widgetInfos.get(editorFactory.getKey()));
			editor.setDisabled(true);

			// Create tab layout
			VLayout layout = new VLayout();
			layout.setMargin(5);

			AbstractWoaHandler editWidgetHandler = new AbstractWoaHandler() {

				@Override
				public boolean onSaveClick(ClickEvent event) {
					blueprintWidgetInfos.put(editorFactory.getKey(), editor.getWidgetConfiguration());
					ManagerCommandService.saveBlueprint(blueprint, SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO);
					editor.setDisabled(true);
					return true;
				}

				@Override
				public boolean onResetClick(ClickEvent event) {
					blueprintWidgetInfos.remove(editorFactory.getKey());
					ManagerCommandService.saveBlueprint(blueprint, SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO);
					return true;
				}

				@Override
				public boolean onEditClick(ClickEvent event) {
					editor.setDisabled(false);
					return true;
				}

				@Override
				public boolean onCancelClick(ClickEvent event) {
					editor.setWidgetConfiguration(blueprintWidgetInfos.get(editorFactory.getKey()));
					editor.setDisabled(true);
					return true;
				}

				@Override
				public boolean isDefault() {
					return !blueprintWidgetInfos.containsKey(editorFactory.getKey());
				}
			};

			SaveButtonBar buttonBar = new SaveButtonBar(editWidgetHandler, layout);
			layout.addMember(buttonBar);
			layout.addMember(editor.getCanvas());
			tab.setPane(layout);

			widgetTabset.addTab(tab);

			// Always caused by a blueprint change, so fire the changed handler.
			editWidgetHandler.fireChangedHandler();
		}
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
	// Wherefore is this still used?
	public void onEditSessionChange(EditSessionEvent ese) {
		boolean disabled = ese.isSessionStart();
		for (Tab tab : tabset.getTabs()) {
			if (tab.getPane() == null || !ese.isParentOfRequestee(tab.getPane())) {
				tab.setDisabled(disabled);
			}
		}
		for (Tab tab : widgetTabset.getTabs()) {
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
