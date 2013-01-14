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

//import org.geomajas.configuration.FeatureStyleInfo;
//import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
//import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
//import com.smartgwt.client.widgets.form.DynamicForm;
//import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
//import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class DatalayerStyle extends AbstractConfigurationLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private LayerModelDto lmd;

	public DatalayerStyle() {
		super();
		setMargin(5);
		setWidth100();

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.datalayerStyleFormGroup());
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setLayerModel(LayerModelDto lmd) {
		fireChangedHandler();
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		return validate();
	}

	public boolean onCancelClick(ClickEvent event) {
		setLayerModel(lmd);
		return true;
	}

	public boolean validate() {
		return true;
	}

	public boolean onResetClick(ClickEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDefault() {
		// TODO Auto-generated method stub
		return true;
	}
}
