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

import java.util.LinkedHashMap;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.ChooseTypeStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.EditLayerSettingsStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.ShapefileUploadStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.VectorChooseLayerStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.VectorEditLayerAttributesStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.VectorEditLayerStyleStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.WfsCapabilitiesStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.WmsCapabilitiesStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.WmsChooseLayerStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps.WmsPreviewLayerStep;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.domain.dto.LayerConfiguration;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class NewLayerModelWizardWindow extends Window implements Wizard {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	public static final String STEP_CHOOSE_TYPE = "choose_type";

	public static final String STEP_WMS_PROPS = "wms_props";

	public static final String STEP_WFS_PROPS = "wfs_props";

	public static final String STEP_SHAPEFILE_UPLOAD = "shapefile_upload";

	public static final String STEP_DATABASE_PROPS = "database_props";

	public static final String STEP_VECTOR_CHOOSE_LAYER = "vector_choose_layer";

	public static final String STEP_VECTOR_EDIT_LAYER_ATTRIBUTES = "vector_edit_layer_attributes_layer";

	public static final String STEP_VECTOR_EDIT_LAYER_STYLE = "vector_edit_style";

	public static final String STEP_WMS_CHOOSE_LAYER = "raster_choose_layer";

	public static final String STEP_WMS_PREVIEW_LAYER = "raster_preview_layer";

	public static final String STEP_EDIT_LAYER_SETTINGS = "edit_layer_settings";

	public static final String WINDOW_TITLE = "Nieuwe datalaag aanmaken";

	private static final int WIDTH = 650;

	private static final int HEIGHT = 400;

	private DataCallback<LayerConfiguration> callback;

	private Canvas container;

	private IButton save;

	private IButton cancel;

	private IButton prev;

	private IButton next;

	private LinkedHashMap<String, WizardStepPanel> steps = new LinkedHashMap<String, WizardStepPanel>();

	private WizardStepPanel startStep;

	private EditLayerSettingsStep lastStep;

	private WizardStepPanel currentStep;

	public NewLayerModelWizardWindow(DataCallback<LayerConfiguration> callback) {
		this.callback = callback;

		setHeight(HEIGHT);
		setWidth(WIDTH);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		setPadding(10);

		container = new Canvas();
		container.setWidth100();
		container.setHeight("*");

		// -------------------------------------------------

		startStep = new ChooseTypeStep(this);
		addStep(startStep);
		addStep(new WfsCapabilitiesStep(this));
		addStep(new WmsCapabilitiesStep(this));
		addStep(new VectorChooseLayerStep(this));
		addStep(new WmsChooseLayerStep(this));
		addStep(new WmsPreviewLayerStep(this));
		addStep(new VectorEditLayerAttributesStep(this));
		addStep(new VectorEditLayerStyleStep(this));
		addStep(new ShapefileUploadStep(this));
		lastStep = new EditLayerSettingsStep(this);
		addStep(lastStep);

		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		buttons.setWidth100();
		buttons.setHeight(25);
		prev = new IButton("Vorige");
		prev.setIcon(WidgetLayout.iconZoomLast);
		prev.setAutoFit(true);
		prev.setDisabled(true);
		prev.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				setStep(steps.get(currentStep.getPreviousStep()));
			}
		});

		next = new IButton("Volgende");
		next.setIcon(WidgetLayout.iconZoomNext);
		next.setAutoFit(true);
		next.setDisabled(true);
		next.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				currentStep.stepFinished();
				WizardStepPanel wsp = steps.get(currentStep.getNextStep());
				if (wsp != null) {
					wsp.initialize();
				}
				setStep(wsp);
			}
		});

		save = new IButton("Aanmaken");
		save.setIcon(WidgetLayout.iconAdd);
		save.setAutoFit(true);
		save.setDisabled(true);
		save.setVisible(false);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});

		cancel = new IButton(MESSAGES.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.setAutoFit(true);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				cancelled();
			}
		});

		LayoutSpacer ls = new LayoutSpacer();
		ls.setWidth("*");
		buttons.addMember(ls);
		buttons.addMember(prev);
		buttons.addMember(next);
		buttons.addMember(save);
		buttons.addMember(cancel);

		// ----------------------------------------------------------

		VLayout vl = new VLayout(10);
		vl.setWidth100();
		vl.setMargin(10);
		vl.addMember(container);
		vl.addMember(buttons);
		addItem(vl);

		// -------------------------------------------------

		setStep(startStep);
	}

	// -------------------------------------------------

	private void saved() {
		this.hide();
		if (callback != null) {
			callback.execute(lastStep.getData());
		}
		this.destroy();
	}

	private void addStep(WizardStepPanel step) {
		step.setVisible(false);
		container.addChild(step);
		steps.put(step.getName(), step);
	}

	private void setStep(WizardStepPanel step) {
		if (step == null) {
			SC.say("Gelieve een panel op te geven!");
			return;
		}

		if (currentStep != null) {
			currentStep.setVisible(false);
		}
		currentStep = step;
		if (currentStep.getWindowTitle() != null) {
			setTitle(WINDOW_TITLE + ": " + currentStep.getWindowTitle());
		} else {
			setTitle(WINDOW_TITLE);
		}
		updateState();
		currentStep.setVisible(true);
	}

	private void cancelled() {
		hide();
		if (callback != null) {
			callback.execute(null);
		}
		destroy();
	}

	private void updateState() {
		if (currentStep.isLastStep()) {
			if (!save.isVisible()) {
				next.setVisible(false);
				save.setVisible(true);
			}
			save.setDisabled(!currentStep.isValid());
			prev.setDisabled((currentStep.getPreviousStep() == null));

		} else {
			if (!next.isVisible()) {
				save.setVisible(false);
				next.setVisible(true);
			}
			next.setDisabled(!(currentStep.isValid() && currentStep.getNextStep() != null));
		}
		prev.setDisabled((currentStep.getPreviousStep() == null));
	}

	// -- Wizard --

	public void onChanged(WizardStepPanel step) {
		if (currentStep != step) {
			SC.logWarn("Step changing while not current, is this ok? -- " + step.getName());
		}
		updateState();
	}

	public WizardStepPanel getStep(String name) {
		return steps.get(name);
	}

	public void fireNextStepEvent() {
		if (currentStep.isValid()) {
			setStep(steps.get(currentStep.getNextStep()));
		}
	}
}
