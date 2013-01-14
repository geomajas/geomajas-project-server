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

import java.util.LinkedHashMap;

import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;

/**
 * @author Kristof Heirwegh
 */
public class ChooseTypeStep extends WizardStepPanel {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private static final String TYPE_WMS = "WMS";

	private static final String TYPE_WFS = "WFS";

	private static final String TYPE_DB = "Database (Postgis)";

	private static final String TYPE_SHAPE = "Shapefile";

	private DynamicForm form;

	private RadioGroupItem radioGroup;

	public ChooseTypeStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_CHOOSE_TYPE, MESSAGES.chooseTypeStepNumbering() + " " +
				MESSAGES.chooseTypeStepTitle(), false, parent);
		setWindowTitle(MESSAGES.chooseTypeStepTitle());

		form = new DynamicForm();
		form.setWidth100();
		form.setColWidths("150", "*");

		radioGroup = new RadioGroupItem();
		radioGroup.setTitle(MESSAGES.chooseTypeStepRadioGroupTitle());
		 LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
			 typeMap.put(TYPE_WFS, MESSAGES.chooseTypeStepWFS());
			 typeMap.put(TYPE_SHAPE, MESSAGES.chooseTypeStepShapeFile());
			 typeMap.put(TYPE_WMS, MESSAGES.chooseTypeStepWMS());
			 typeMap.put(TYPE_DB, MESSAGES.chooseTypeStepDatabase());
			 
		radioGroup.setValueMap(typeMap);
		radioGroup.setDefaultValue(TYPE_WFS);
		radioGroup.setRequired(true);

		// -------------------------------------------------

		form.setFields(radioGroup);
		form.addItemChangedHandler(new ItemChangedHandler() {

			public void onItemChanged(ItemChangedEvent event) {
				fireChangedEvent();
			}
		});
		addMember(form);
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getNextStep() {
		if (TYPE_WMS.equals(radioGroup.getValueAsString())) {
			return NewLayerModelWizardWindow.STEP_WMS_PROPS;
		} else if (TYPE_WFS.equals(radioGroup.getValueAsString())) {
			return NewLayerModelWizardWindow.STEP_WFS_PROPS;
		} else if (TYPE_SHAPE.equals(radioGroup.getValueAsString())) {
			return NewLayerModelWizardWindow.STEP_SHAPEFILE_UPLOAD;
		} else if (TYPE_DB.equals(radioGroup.getValueAsString())) {
			return NewLayerModelWizardWindow.STEP_DATABASE_PROPS;
		} else {
			return null;
		}
	}

	@Override
	public String getPreviousStep() {
		return null;
	}

	@Override
	public void reset() {
		form.reset();
	}

	@Override
	public void stepFinished() {
	}
}
