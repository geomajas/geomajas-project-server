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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.util.Notify;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.FormElement;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.KeyValueForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeotoolsVectorCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;

/**
 * @author Kristof Heirwegh
 */
public class WfsCapabilitiesStep extends WizardStepPanel {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private KeyValueForm form;

	private boolean first = true;

	public WfsCapabilitiesStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_WFS_PROPS, MESSAGES.wfsCapabilitiesStepNumbering() + " "  + 
				MESSAGES.wfsCapabilitiesStepTitle(), false, parent);
		setWindowTitle(MESSAGES.wfsCapabilitiesStepTitle());

		List<FormElement> fields = new ArrayList<FormElement>();
		fields.add(new FormElement(GetGeotoolsVectorCapabilitiesRequest.PROPERTY_WFS_CAPABILITIESURL,
				MESSAGES.wfsCapabilitiesStepParametersCapabilitiesURL(), 
				true));
		fields.add(new FormElement(GetGeotoolsVectorCapabilitiesRequest.PROPERTY_WFS_USERNAME,
				MESSAGES.wfsCapabilitiesStepParametersUserName(), 150));
		fields.add(new FormElement(GetGeotoolsVectorCapabilitiesRequest.PROPERTY_WFS_PASSWORD,
				MESSAGES.wfsCapabilitiesStepParametersPassword(),
				KeyValueForm.ITEMTYPE_PASSWORD, false, 150, null, null));

		form = new KeyValueForm();
		form.setWidth100();
		form.setColWidths("125", "*");
		form.updateFields(fields);

		form.addItemChangedHandler(new ItemChangedHandler() {

			public void onItemChanged(ItemChangedEvent event) {
				fireChangedEvent();
			}
		});
		addMember(form);
	}

	@Override
	public void initialize() {
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put(DynamicLayerConfiguration.PARAM_SOURCE_TYPE, DynamicLayerConfiguration.SOURCE_TYPE_WFS);
		form.setData(values);
	}

	@Override
	public boolean isValid() {
		// don't check first time, otherwise errors are immediately shown
		if (first) {
			first = !first;
			return false;
		} else {
			return form.validate();
		}
	}

	@Override
	public String getNextStep() {
		return NewLayerModelWizardWindow.STEP_VECTOR_CHOOSE_LAYER;
	}

	@Override
	public String getPreviousStep() {
		return NewLayerModelWizardWindow.STEP_CHOOSE_TYPE;
	}

	@Override
	public void reset() {
		form.reset();
	}

	@Override
	public void stepFinished() {
		VectorChooseLayerStep nextStep = (VectorChooseLayerStep) parent
				.getStep(NewLayerModelWizardWindow.STEP_VECTOR_CHOOSE_LAYER);
		if (nextStep != null) {
			nextStep.setPreviousStep(NewLayerModelWizardWindow.STEP_WFS_PROPS);
			nextStep.setData(getData());
		} else {
			Notify.error(MESSAGES.wfsCapabilitiesStepNextStepNotFound());
			//TODO: cleanup or turn into logging instruction?
		}
	}

	public Map<String, String> getData() {
		return form.getData(true);
	}
}
