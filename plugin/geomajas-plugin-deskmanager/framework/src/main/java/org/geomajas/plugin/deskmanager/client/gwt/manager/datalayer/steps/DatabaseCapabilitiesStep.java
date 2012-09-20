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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.FormElement;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.KeyValueForm;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.domain.dto.LayerConfiguration;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;

/**
 * @author Kristof Heirwegh
 */
public class DatabaseCapabilitiesStep extends WizardStepPanel {

	private KeyValueForm form;

	private boolean first = true;

	public DatabaseCapabilitiesStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_DATABASE_PROPS, "2) Postgis Database Connectieparameters", false, parent);
		setWindowTitle("Postgis Database Connectieparameters");

		List<FormElement> fields = new ArrayList<FormElement>();
		fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_HOST, "Host", true, "localhost"));
		fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_PORT, "Port", true, "5432"));
		fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_NAMESPACE, "Schema", true, "public"));
		fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_DATABASE, "Database", true));
		fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_USER, "Gebruikersnaam", 150));
		fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_PASSWD, "Wachtwoord",
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
		values.put(LayerConfiguration.PARAM_SOURCE_TYPE, LayerConfiguration.SOURCE_TYPE_DATABASE);
		values.put(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_DBTYPE, "postgis"); // always postgis
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
			nextStep.setPreviousStep(NewLayerModelWizardWindow.STEP_DATABASE_PROPS);
			nextStep.setData(getData());
		} else {
			SC.warn("Kon Kies Vector Laag stap niet vinden ?!");
		}
	}

	public Map<String, String> getData() {
		return form.getData(true);
	}
}
