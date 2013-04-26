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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.util.Notify;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.SldUtils;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicVectorLayerConfiguration;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * Set some simple options to style the layer.
 * 
 * @author Kristof Heirwegh
 */
public class VectorEditLayerStyleStep extends WizardStepPanel {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private DynamicVectorLayerConfiguration layerConfig;

	private ColorPickerItem picker;

	private DynamicForm form;

	private TextItem label;

	public VectorEditLayerStyleStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_STYLE, MESSAGES.vectorEditLayerStyleStepNumbering()
				+ MESSAGES.vectorEditLayerStyleStepTitle(), false, parent);
		setWindowTitle(MESSAGES.vectorEditLayerStyleStepTitle());

		form = new DynamicForm();
		picker = new ColorPickerItem("selectColor", MESSAGES.vectorEditLayerStyleStepSelectColor() + ": ");
		picker.setValue(SldUtils.DEFAULT_FILLCOLOR);
		label = new TextItem("styleLabel", MESSAGES.vectorEditLayerStyleStepStyleName() + ": ");

		form.setFields(label, picker);
		addMember(form);
	}

	@Override
	public void initialize() {
		// TODO ?
	}

	public void setData(DynamicVectorLayerConfiguration layerConfig) {
		this.layerConfig = layerConfig;
		Map<String, Object> props;
		if (layerConfig.getClientVectorLayerInfo().getNamedStyleInfo() != null) {
			props = SldUtils.getProperties(layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().getUserStyle());
		} else {
			props = new HashMap<String, Object>();
		}
		picker.setValue(SldUtils.getPropValue(SldUtils.FILLCOLOR, props, SldUtils.DEFAULT_FILLCOLOR));
		label.setValue(SldUtils.getPropValue(SldUtils.STYLENAME, props, "default"));
	}

	public void applyChanges(DynamicVectorLayerConfiguration layerConfig) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(SldUtils.FILLCOLOR, picker.getValueAsString());
		props.put(SldUtils.STROKECOLOR, picker.getValueAsString());
		props.put(SldUtils.FILLOPACITY, 0.5f);
		props.put(SldUtils.STROKEOPACITY, 1f);
		props.put(SldUtils.STYLENAME, label.getValueAsString() == "" ? "default" : label.getValueAsString());
		props.put(SldUtils.LABELFEATURENAME, layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().getLabelStyle()
				.getLabelAttributeName());
		
		layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().setUserStyle(SldUtils.createSimpleSldStyle(layerConfig, props));
		layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().getFeatureStyles().clear(); // don't use these!
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getNextStep() {
		return NewLayerModelWizardWindow.STEP_EDIT_LAYER_SETTINGS;
	}

	@Override
	public String getPreviousStep() {
		return NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_ATTRIBUTES;
	}

	@Override
	public void reset() {
	}

	@Override
	public void stepFinished() {
		applyChanges(layerConfig);
		EditLayerSettingsStep nextStep = (EditLayerSettingsStep) parent
				.getStep(NewLayerModelWizardWindow.STEP_EDIT_LAYER_SETTINGS);
		if (nextStep != null) {
			nextStep.setData(layerConfig, NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_STYLE);
		} else {
			Notify.error(MESSAGES.vectorEditLayerStyleStepNextStepNotFound());
		}
	}
}
