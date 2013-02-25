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
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.GeodeskDtoUtil;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.dto.BaseGeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

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
public class GeodeskDetail extends VLayout implements SelectionChangedHandler, EditSessionHandler, GeodeskHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private static final String ID_ATTRIBUTE = "id";

	private GeodeskDto geodesk;

	private TabSet tabset;

	private GeodeskSettings settings;

	private GeodeskLayers layers;

	private GeodeskAccessRights accessrights;

	private Tab accessrightsTab;

	private Label loadingLabel;

	private VLayout loadingLayout;

	private TabSet widgetTabset;

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

		accessrights = new GeodeskAccessRights();
		accessrightsTab = new Tab(MESSAGES.geodeskDetailTabAccessRights());
		tabset.addTab(accessrightsTab);
		accessrightsTab.setPane(accessrights);
		accessrightsTab.setDisabled(true);
		Whiteboard.registerHandler(accessrights);

		// Widget tabs
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
		loadingLabel.setContents("<B><i>" + MESSAGES.loadingConfig() + "</i> <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></B>");
		loadingLayout.animateShow(AnimationEffect.FADE);
	}

	private void setLoaded() {
		loadingLayout.animateHide(AnimationEffect.FADE);
		loadingLabel.setContents("");
	}

	private void loadRecord(final String id) {
		clearWidgetTabs();
		setLoading(); /* Clear edit form */
		ManagerCommandService.getGeodesk(id, new DataCallback<GeodeskDto>() {

			public void execute(GeodeskDto result) {
				setGeodesk(result);
				loadWidgetTabs(result);
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
			// Always select first tab
			tabset.selectTab(0);
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
		for (Tab tab : widgetTabset.getTabs()) {
			if (tab.getPane() == null || !ese.isParentOfRequestee(tab.getPane())) {
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
	 * @param bgd
	 *            the basegeodesk.
	 */
	private void loadWidgetTabs(BaseGeodeskDto bgd) {
		UserApplication ua = UserApplicationRegistry.getInstance().get(bgd.getUserApplicationInfo().getKey());
		for (String key : ua.getSupportedApplicationWidgetKeys()) {
			addWidgetTab(WidgetEditorFactoryRegistry.getApplicationRegistry().get(key),
					bgd.getApplicationClientWidgetInfos(), GeodeskDtoUtil.getApplicationClientWidgetInfo(bgd), bgd);
		}
		for (String key : ua.getSupportedMainMapWidgetKeys()) {
			addWidgetTab(WidgetEditorFactoryRegistry.getMapRegistry().get(key), bgd.getMainMapClientWidgetInfos(),
					GeodeskDtoUtil.getMainMapClientWidgetInfo(bgd), bgd);
		}
		for (String key : ua.getSupportedOverviewMapWidgetKeys()) {
			addWidgetTab(WidgetEditorFactoryRegistry.getMapRegistry().get(key), bgd.getOverviewMapClientWidgetInfos(),
					GeodeskDtoUtil.getOverviewMapClientWidgetInfo(bgd), bgd);
		}
	}

	/**
	 * Add a widget editor tab to the tabset for a given editor factory, set of widget info's (where one of will be
	 * edited by the editor) and a base geodesk that could provide extra context to the editor.
	 * 
	 * @param editorFactory
	 *            the editor factory
	 * @param geodeskWidgetInfos
	 *            the geodesk widget infos
	 * @param widgetInfos
	 *            all the widget infos (including inherited)
	 * @param geodesk
	 *            the geodesk
	 */
	private void addWidgetTab(final WidgetEditorFactory editorFactory,
			final Map<String, ClientWidgetInfo> geodeskWidgetInfos, final Map<String, ClientWidgetInfo> widgetInfos,
			BaseGeodeskDto baseGeodesk) {
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
					geodeskWidgetInfos.put(editorFactory.getKey(), editor.getWidgetConfiguration());
					ManagerCommandService.saveGeodesk(geodesk, SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO);
					editor.setDisabled(true);
					return true;
				}

				@Override
				public boolean onResetClick(ClickEvent event) {
					geodeskWidgetInfos.remove(editorFactory.getKey());
					ManagerCommandService.saveGeodesk(geodesk, SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO);
					return true;
				}

				@Override
				public boolean onEditClick(ClickEvent event) {
					editor.setDisabled(false);
					return true;
				}

				@Override
				public boolean onCancelClick(ClickEvent event) {
					editor.setWidgetConfiguration(geodeskWidgetInfos.get(editorFactory.getKey()));
					editor.setDisabled(true);
					return true;
				}

				@Override
				public boolean isDefault() {
					return !geodeskWidgetInfos.containsKey(editorFactory.getKey());
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

}
