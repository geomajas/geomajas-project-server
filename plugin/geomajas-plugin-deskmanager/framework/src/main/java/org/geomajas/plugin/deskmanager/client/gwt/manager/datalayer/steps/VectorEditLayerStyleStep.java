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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * @author Kristof Heirwegh
 */
public class VectorEditLayerStyleStep extends WizardStepPanel {

	private VectorLayerConfiguration layerConfig;

	private ColorPickerItem picker;

	private DynamicForm form;

	public VectorEditLayerStyleStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_STYLE, "4) Vector stijl aanpassen", false, parent);
		setWindowTitle("Vector stijl aanpassen");

		form = new DynamicForm();
		picker = new ColorPickerItem("selectColor", "Selecteer kleur: ");
		picker.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				FeatureStyleInfo fs = layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().getFeatureStyles()
						.get(0);
				fs.setFillColor(picker.getValueAsString());
				fs.setStrokeColor(picker.getValueAsString());
			}
		});
		form.setFields(picker);
		addMember(form);
	}

	@Override
	public void initialize() {
		// TODO
	}

	public void setData(VectorLayerConfiguration layerConfig) {
		this.layerConfig = layerConfig;
		layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().applyDefaults();
		FeatureStyleInfo fs = layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().getFeatureStyles().get(0);
		picker.setValue(fs.getFillColor());
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
		EditLayerSettingsStep nextStep = (EditLayerSettingsStep) parent
				.getStep(NewLayerModelWizardWindow.STEP_EDIT_LAYER_SETTINGS);
		if (nextStep != null) {
			nextStep.setData(layerConfig, NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_STYLE);
		} else {
			SC.warn("Kon Editeer Laag eigenschappen stap niet vinden ?!");
		}
	}
}
