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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

import org.geomajas.plugin.deskmanager.client.gwt.manager.common.AbstractConfigurationLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.SaveButtonBar;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.UploadShapefileForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class DatalayerShapeUpload extends AbstractConfigurationLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	
	private UploadShapefileForm form;

	private SaveButtonBar buttonBar;

	public DatalayerShapeUpload() {
		super(5);
		setWidth100();

		buttonBar = new SaveButtonBar(this);
		addMember(buttonBar);

		// ----------------------------------------------------------

		form = new UploadShapefileForm();
		form.setWidth100();
		form.setDisabled(true);

		VLayout group = new VLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.datalayerShapeUploadFormGroup());
		group.addMember(form);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	public void setLayerModel(LayerModelDto lmd) {
		form.setData(lmd.getClientLayerId()); // update instead of new
		fireChangedHandler();
	}

	// -- SaveButtonBar events --------------------------------------------------------

	public boolean onEditClick(ClickEvent event) {
		form.setDisabled(false);
		return true;
	}

	public boolean onSaveClick(ClickEvent event) {
		if (validate()) {
			SC.ask(MESSAGES.datalayerShapeUploadOverwriteConfirmTitle(), 
					MESSAGES.datalayerShapeUploadOverwriteConfirmQuestion(), new BooleanCallback() {

				public void execute(Boolean value) {
					if (value) {
						form.upload(new DataCallback<String>() {

							public void execute(String result) {
								buttonBar.doCancelClick(null); // could do saveClick, but nothing changed so no
																// point in reloading everything
							}
						});
					}
				}
			});
		}
		return false; // not ending editsession yet
	}

	public boolean onCancelClick(ClickEvent event) {
		form.setDisabled(true);
		return true;
	}

	public boolean validate() {
		if (!form.validate()) {
			SC.say(MESSAGES.datalayerShapeUploadNoFileSelected());
			return false;
		}
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
