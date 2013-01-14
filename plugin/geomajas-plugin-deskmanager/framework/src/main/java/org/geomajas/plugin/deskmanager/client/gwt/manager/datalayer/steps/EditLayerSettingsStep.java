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

import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.LayerSettingsForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels.MaxBoundsForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class EditLayerSettingsStep extends WizardStepPanel {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private LayerSettingsForm form;
	private MaxBoundsForm maxBoundsForm;

	private boolean first = true;

	private String prevStepName;

	private DynamicLayerConfiguration layerConfig;

	public EditLayerSettingsStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_EDIT_LAYER_SETTINGS, 
				MESSAGES.editLayerSettingsStepNumbering() + " " + MESSAGES.editLayerSettingsStepTitle(),
				true, parent);
		setWindowTitle(MESSAGES.editLayerSettingsStepTitle());

		// -- layersettings --
		form = new LayerSettingsForm();
		form.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				fireChangedEvent();
			}
		});
		addMember(form);
		
		// -- maxbounds --
		maxBoundsForm = new MaxBoundsForm();
		maxBoundsForm.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				fireChangedEvent();
			}
		});
		
		// -------------------------------------------------

//		HLayout formLayout = new HLayout();
//		LayoutSpacer ls = new LayoutSpacer();
//		ls.setWidth(100);
//		formLayout.addMember(ls);
//		formLayout.addMember(maxBoundsForm);
//		formLayout.addMember(new LayoutSpacer());
		
		VLayout root = new VLayout();
		root.addMember(form);
		Label l = new Label("<b>" + MESSAGES.editLayerSettingsStepVisibleArea() + " : </b>");
		l.setPadding(10);
		l.setWidth100();
		l.setAutoHeight();
		root.addMember(l);
		root.addMember(maxBoundsForm);
		root.addMember(new LayoutSpacer());
		addMember(root);
	}

	/**
	 * previous step can be vector or raster.
	 * 
	 * @param layerConfig
	 * @param previousStep
	 */
	public void setData(DynamicLayerConfiguration layerConfig, String previousStepName) {
		this.prevStepName = previousStepName;
		this.layerConfig = layerConfig;
		ShapefileUploadStep sfup = 
			(ShapefileUploadStep) parent.getStep(NewLayerModelWizardWindow.STEP_SHAPEFILE_UPLOAD);
		if (sfup != null && sfup.getFileName() != null) {
			layerConfig.getClientLayerInfo().setLabel(sfup.getFileName());
		}
	}

	public DynamicLayerConfiguration getData() {
		if (isValid()) {
			form.getData();
			maxBoundsForm.getData();
			return this.layerConfig;
		} else {
			return null;
		}
	}

	@Override
	public void initialize() {
		form.setData(layerConfig);
		maxBoundsForm.setData(layerConfig);
	}

	@Override
	public boolean isValid() {
		// don't check first time, otherwise errors are immediately shown
		if (first) {
			first = !first;
			return false;
		} else {
			return form.validate() && maxBoundsForm.validate();
		}
	}

	@Override
	public String getNextStep() {
		return null;
	}

	@Override
	public String getPreviousStep() {
		return prevStepName;
	}

	@Override
	public void reset() {
		form.reset();
	}

	@Override
	public void stepFinished() {
	}
}
