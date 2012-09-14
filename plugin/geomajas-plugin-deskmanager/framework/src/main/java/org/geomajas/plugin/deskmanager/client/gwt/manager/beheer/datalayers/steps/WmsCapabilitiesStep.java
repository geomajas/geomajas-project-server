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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.panels.FormElement;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.panels.KeyValueForm;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerConfiguration;
import org.geomajas.plugin.runtimeconfig.service.factory.WmsLayerBeanFactory;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;

/**
 * @author Kristof Heirwegh FIXME: is this deprecated?
 */
public class WmsCapabilitiesStep extends WizardStepPanel {

	private KeyValueForm form;

	private boolean first = true;

	public WmsCapabilitiesStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_WMS_PROPS, "2) WMS Connectieparameters", false, parent);
		setWindowTitle("WMS Connectieparameters");

		List<FormElement> fields = new ArrayList<FormElement>();
		fields.add(new FormElement(GetWmsCapabilitiesRequest.GET_CAPABILITIES_URL, "Capabilities URL", true));
		fields.add(new FormElement(WmsLayerBeanFactory.WMS_USERNAME, "Gebruikersnaam", 150));
		fields.add(new FormElement(WmsLayerBeanFactory.WMS_PASSWORD, "Wachtwoord",
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
		values.put(LayerConfiguration.PARAM_SOURCE_TYPE, LayerConfiguration.SOURCE_TYPE_WMS);
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
		return NewLayerModelWizardWindow.STEP_WMS_CHOOSE_LAYER;
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
		WmsChooseLayerStep nextStep = (WmsChooseLayerStep) parent
				.getStep(NewLayerModelWizardWindow.STEP_WMS_CHOOSE_LAYER);
		if (nextStep != null) {
			nextStep.setData(getData());
		} else {
			SC.warn("Kon Kies WMS Laag stap niet vinden ?!");
		}
	}

	public Map<String, String> getData() {
		return form.getData(true);
	}
}
