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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.steps;

import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.WizardStepPanel;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;


import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;

/**
 * @author Kristof Heirwegh
 */
public class VectorEditLayerStyleStep extends WizardStepPanel {

	private VectorLayerConfiguration layerConfig;

	public VectorEditLayerStyleStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_VECTOR_EDIT_LAYER_STYLE, "4) Vector stijl aanpassen", false, parent);
		setWindowTitle("Vector stijl aanpassen");

		addMember(new Label("[TODO]"));
	}

	@Override
	public void initialize() {
		// TODO
	}

	public void setData(VectorLayerConfiguration layerConfig) {
		this.layerConfig = layerConfig;
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
