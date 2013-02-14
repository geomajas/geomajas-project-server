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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.EditSessionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.LayerModelEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.LayerModelHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.Whiteboard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

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
public class DatalayerDetail extends VLayout implements SelectionChangedHandler, EditSessionHandler, LayerModelHandler {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private static final String ID_ATTRIBUTE = "id";
	
	private LayerModelDto layerModel;

	private TabSet tabset;

	private DatalayerSettings settings;

	private DatalayerConnectionParameters connPar;

	private DatalayerShapeUpload upload;

	private DatalayerStyle style;

	private Tab settingsTab;

	private Tab uploadTab;

	private Tab styleTab;

	private Tab connParTab;

	private Label loadingLabel;

	private VLayout loadingLayout;

	public DatalayerDetail() {
		super(10);

		settings = new DatalayerSettings();
		connPar = new DatalayerConnectionParameters();
		upload = new DatalayerShapeUpload();
		style = new DatalayerStyle();

		tabset = new TabSet();
		tabset.setTabBarPosition(Side.TOP);
		tabset.setWidth100();
		tabset.setHeight100();
		tabset.setOverflow(Overflow.HIDDEN);

		settingsTab = new Tab(MESSAGES.datalayerDetailTabSettings());
		settingsTab.setPane(settings);
		tabset.addTab(settingsTab);

		uploadTab = new Tab(MESSAGES.datalayerDetailTabUpload());
		uploadTab.setPane(upload);
		tabset.addTab(uploadTab);

		connParTab = new Tab(MESSAGES.datalayerDetailTabConnectionparameters());
		connParTab.setPane(connPar);
		tabset.addTab(connParTab);

		styleTab = new Tab(MESSAGES.datalayerDetailTabStyle());
		styleTab.setPane(style);
		tabset.addTab(styleTab);

		// -------------------------------------------------

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
		Whiteboard.registerHandler((LayerModelHandler) this);
	}

	public void destroy() {
		Whiteboard.unregisterHandler((EditSessionHandler) this);
		Whiteboard.unregisterHandler((LayerModelHandler) this);
		super.destroy();
	}

	public void onSelectionChanged(SelectionEvent event) {
		setDisabled(true);
		if (event.getState()) { // true == selected
			ListGridRecord record = (ListGridRecord) event.getRecord();
			if (record != null && record.getAttributeAsString(ID_ATTRIBUTE) != null
					&& record.getAttributeAsString(ID_ATTRIBUTE).length() != 0) {
				loadRecord(record.getAttributeAsString(DatalayerGrid.FLD_ID));
			} else {
				setLayerModel(null);
			}
		}
	}

	private void setLoading() {
		loadingLabel.setContents("<B><i>" + MESSAGES.loadingConfig() + "</i> " + "<img src='"
				+ Geomajas.getIsomorphicDir() + "/images/circle.gif' style='height: 1em' /></B>");
		loadingLayout.animateShow(AnimationEffect.FADE);
	}

	private void setLoaded() {
		loadingLayout.animateHide(AnimationEffect.FADE);
		loadingLabel.setContents("");
	}

	private void loadRecord(final String id) {
		setLoading(); /* Clear edit form */
		ManagerCommandService.getLayerModel(id, new DataCallback<LayerModelDto>() {

			public void execute(LayerModelDto result) {
				setLayerModel(result);
			}
		});
	}

	public void setLayerModel(LayerModelDto layerModel) {
		this.layerModel = layerModel;

		settings.setLayerModel(layerModel);

		updateTabState();

		if (layerModel == null) {
			setDisabled(true);
		} else {
			setDisabled(false);
		}

		setLoaded();
	}

	private void updateTabState() {
		if (layerModel == null || layerModel.isReadOnly()) {
			settingsTab.setDisabled(true);
			uploadTab.setDisabled(true);
			styleTab.setDisabled(true);
			connParTab.setDisabled(true);

		} else {
			settingsTab.setDisabled(false);

			String sourceType = layerModel.getParameterValue(DynamicLayerConfiguration.PARAM_SOURCE_TYPE);
			if (DynamicLayerConfiguration.SOURCE_TYPE_SHAPE.equals(sourceType)) {
				connParTab.setDisabled(true);
				uploadTab.setDisabled(false);
				upload.setLayerModel(layerModel);

			} else if (sourceType != null) {
				uploadTab.setDisabled(true);
				connParTab.setDisabled(false);
				connPar.setLayerModel(layerModel);

			} else {
				uploadTab.setDisabled(true);
				connParTab.setDisabled(true);
			}

			if ("Raster".equals(layerModel.getLayerType())) {
				styleTab.setDisabled(true);
			} else {
				styleTab.setDisabled(false);
				style.setLayerModel(layerModel);
			}
		}
	}

	// -- EditSessionHandler --------------------------------------------------------
	public void onEditSessionChange(EditSessionEvent ese) {
		boolean disabled = ese.isSessionStart();
		if (disabled) {
			for (Tab tab : tabset.getTabs()) {
				if (tab.getPane() == null || !ese.isParentOfRequestee(tab.getPane())) {
					tab.setDisabled(true);
				}
			}
		} else {
			updateTabState();
		}
	}

	// -- LayerModelHandler ---------------------------------------------------------
	public void onLayerModelChange(LayerModelEvent lme) {
		// refresh data if it's the same record
		if (layerModel != null && layerModel.getId() == lme.getLayerModel().getId()) {
			if (lme.isDeleted()) {
				setLayerModel(null);
			} else {
				loadRecord(layerModel.getId());
			}
		}
	}
}
