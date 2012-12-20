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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels;

import org.geomajas.plugin.deskmanager.configuration.client.DeskmanagerClientLayerInfoI;
import org.geomajas.plugin.deskmanager.domain.dto.LayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * @author Kristof Heirwegh
 */
public class LayerSettingsForm extends DynamicForm {

	private CheckboxItem publicLayer;

	private CheckboxItem active;

	private CheckboxItem visible;

	private TextItem name;

	private DeskmanagerClientLayerInfoI extraLayerInfo;

	private LayerConfiguration layerConfig;

	private LayerModelDto layerModel;

	public LayerSettingsForm() {

// 		setWidth100();
		setColWidths("125", "*");

		name = new TextItem();
		name.setTitle("Naam laag");
		name.setWidth(250);
		name.setRequired(true);

		publicLayer = new CheckboxItem();
		publicLayer.setTitle("Publiek");
		publicLayer.setTooltip("Mag deze laag mag gebruikt worden in publieke loketten?");

		active = new CheckboxItem();
		active.setTitle("Actief");
		active.setTooltip("Enkel actieve lagen kunnen gebruikt worden bij het configureren van loketten.");

		visible = new CheckboxItem();
		visible.setTitle("Standaard zichtbaar");
		visible.setTooltip("Laag is zichtbaar bij opstarten loket.");

		// -------------------------------------------------

		setFields(name, publicLayer, active, visible);
	}

	public void setData(LayerConfiguration layerConfig) {
		this.layerConfig = layerConfig;
		this.extraLayerInfo = (DeskmanagerClientLayerInfoI) layerConfig.getClientLayerInfo().getUserData();
		name.setValue(layerConfig.getClientLayerInfo().getLabel());
		publicLayer.setValue(extraLayerInfo.isPublic());
		active.setValue(extraLayerInfo.isActive());
		visible.setValue(layerConfig.getClientLayerInfo().isVisible());
	}

	public LayerConfiguration getData() {
		layerConfig.getClientLayerInfo().setLabel(name.getValueAsString());
		layerConfig.getClientLayerInfo().setVisible(visible.getValueAsBoolean());
		extraLayerInfo.setPublic(publicLayer.getValueAsBoolean());
		extraLayerInfo.setActive(active.getValueAsBoolean());
		return layerConfig;
	}

	public void setLayerModel(LayerModelDto model) {
		this.layerModel = model;
		name.setValue(model.getName());
		publicLayer.setValue(model.isPublic());
		active.setValue(model.isActive());
		visible.setValue(model.isDefaultVisible());
	}

	public LayerModelDto getLayerModel() {
		layerModel.setActive(active.getValueAsBoolean());
		layerModel.setDefaultVisible(visible.getValueAsBoolean());
		layerModel.setName(name.getValueAsString());
		layerModel.setPublic(publicLayer.getValueAsBoolean());
		return layerModel;
	}
}
