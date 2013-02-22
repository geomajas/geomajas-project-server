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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.configuration.Parameter;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.FormElement;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.KeyValueForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.ManagerCommandService;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveLayerModelRequest;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.runtimeconfig.service.factory.WmsLayerBeanFactory;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class DatalayerConnectionParameters extends AbstractConfigurationLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private LayerModelDto lmd;

	private KeyValueForm form;

	public DatalayerConnectionParameters() {
		super();
		setMargin(5);
		setWidth100();

		SaveButtonBar buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		form = new KeyValueForm();
		form.setWidth100();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setDisabled(true);

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.datalayerConnectionParametersGroup());
		group.addMember(form);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setLayerModel(LayerModelDto lmd) {
		form.clearValues();
		this.lmd = lmd;

		if (lmd != null) {
			form.updateFields(getFieldList(lmd.getParameterValue(DynamicLayerConfiguration.PARAM_SOURCE_TYPE)));
			form.setData(paramsToString(lmd.getLayerConfiguration().getParameters()));
		} else {
			form.updateFields(new ArrayList<FormElement>());
		}
		fireChangedHandler();
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		form.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		if (validate()) {
			lmd.getLayerConfiguration().getParameters().clear();
			lmd.getLayerConfiguration().getParameters().addAll(stringToParams(form.getData()));
			ManagerCommandService.saveLayerModel(lmd, SaveLayerModelRequest.SAVE_SETTINGS);
			form.setDisabled(true);
			return true;
		} else {
			return false;
		}
	}

	public boolean onCancelClick(ClickEvent event) {
		setLayerModel(lmd);
		form.setDisabled(true);
		return true;
	}

	public boolean validate() {
		if (!form.validate()) {
			SC.say(MESSAGES.formWarnNotvalid());
			return false;
		}
		return true;
	}

	// -------------------------------------------------

	private List<FormElement> getFieldList(String type) {
		List<FormElement> fields = new ArrayList<FormElement>();
		if (DynamicLayerConfiguration.SOURCE_TYPE_WFS.equals(type)) {
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_WFS_CAPABILITIESURL, MESSAGES
					.datalayerConnectionParametersCapabilitiesURL(), true));
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_WFS_USERNAME, MESSAGES
					.datalayerConnectionParametersUserName(), 150));
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_WFS_PASSWORD, MESSAGES
					.datalayerConnectionParametersPassword(), KeyValueForm.ITEMTYPE_PASSWORD, false, 150, null, null));

		} else if (DynamicLayerConfiguration.SOURCE_TYPE_DATABASE.equals(type)) {
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_HOST, MESSAGES
					.datalayerConnectionParametersHost(), true, "localhost"));
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_PORT, MESSAGES
					.datalayerConnectionParametersPort(), true, "5432"));
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_NAMESPACE, MESSAGES
					.datalayerConnectionParametersScheme(), true, "public"));
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_DATABASE, MESSAGES
					.datalayerConnectionParametersDatabase(), true));
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_USER, MESSAGES
					.datalayerConnectionParametersUserName(), 150));
			fields.add(new FormElement(GetVectorCapabilitiesRequest.PROPERTY_DATABASE_PASSWD, MESSAGES
					.datalayerConnectionParametersPassword(), KeyValueForm.ITEMTYPE_PASSWORD, false, 150, null, null));

		} else if (DynamicLayerConfiguration.SOURCE_TYPE_WMS.equals(type)) {
			fields.add(new FormElement(GetWmsCapabilitiesRequest.GET_CAPABILITIES_URL, MESSAGES
					.datalayerConnectionParametersCapabilitiesURL(), true));
			fields.add(new FormElement(WmsLayerBeanFactory.WMS_USERNAME, MESSAGES
					.datalayerConnectionParametersUserName(), 150));
			fields.add(new FormElement(WmsLayerBeanFactory.WMS_PASSWORD, MESSAGES
					.datalayerConnectionParametersPassword(), KeyValueForm.ITEMTYPE_PASSWORD, false, 150, null, null));
		}
		// else TODO
		return fields;
	}

	private Map<String, String> paramsToString(List<Parameter> params) {
		Map<String, String> vals = new LinkedHashMap<String, String>();
		for (Parameter p : params) {
			vals.put(p.getName(), p.getValue());
		}
		return vals;
	}

	private List<Parameter> stringToParams(Map<String, String> strings) {
		List<Parameter> params = new ArrayList<Parameter>();
		for (Entry<String, String> str : strings.entrySet()) {
			Parameter p = new Parameter();
			p.setName(str.getKey());
			p.setValue(str.getValue());
			params.add(p);
		}
		return params;
	}

	public boolean onResetClick(ClickEvent event) {
		return false;
	}

	public boolean isDefault() {
		return true;
	}
}
