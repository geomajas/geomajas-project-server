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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.SensibleScaleConverter;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class LayerConfigurationWindow extends DockableWindow {
	
	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private static final int FORMITEM_WIDTH = 300;
	public static final String FLD_NAME = "Name";

	private LayerDto layer;

	private BooleanCallback callback;

	private DynamicForm form;

	private TextItem label;

	private CheckboxItem publicLayer;

	private CheckboxItem defaultVisible;

	private TextItem minScale;

	private TextItem maxScale;


	/**
	 * @param layer
	 * @param callback
	 *            returns true if saved, false if cancelled.
	 */
	public LayerConfigurationWindow(LayerDto layerDto, BooleanCallback callback) {
		this.layer = layerDto;
		this.callback = callback;
		
		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle(MESSAGES.layerConfigurationConfigureLayer());
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);

		form = new DynamicForm();
		form.setIsGroup(true);
		form.setGroupTitle(MESSAGES.layerConfigurationLayerProperties());
		form.setPadding(5);
		form.setAutoWidth();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(2);
		form.setTitleOrientation(TitleOrientation.LEFT);

		label = new TextItem(FLD_NAME );
		label.setTitle(MESSAGES.layerConfigurationName());
		label.setRequired(true);
		label.setWidth(FORMITEM_WIDTH);
		label.setWrapTitle(false);
		label.setTooltip(MESSAGES.layerConfigurationNameTooltip());

		publicLayer = new CheckboxItem();
		publicLayer.setTitle(MESSAGES.layerConfigurationPublicLayer());
		publicLayer.setDisabled(true); // altijd readonly hier
		publicLayer.setWrapTitle(false);

		defaultVisible = new CheckboxItem();
		defaultVisible.setTitle(MESSAGES.layerConfigurationLayerVisibleByDefault());
		defaultVisible.setWrapTitle(false);
		defaultVisible.setTooltip(MESSAGES.layerConfigurationLayerVisibleByDefaultTooltip());

		minScale = new TextItem();
		minScale.setTitle(MESSAGES.layerConfigurationMinimumScale());
		minScale.setWidth(FORMITEM_WIDTH / 2);
		minScale.setWrapTitle(false);
		minScale.setTooltip(MESSAGES.layerConfigurationMinimumScaleTooltip());
		minScale.setValidators(new ScaleValidator());

		maxScale = new TextItem();
		maxScale.setTitle(MESSAGES.layerConfigurationMaximumScale());
		maxScale.setWidth(FORMITEM_WIDTH / 2);
		maxScale.setWrapTitle(false);
		maxScale.setValidators(new ScaleValidator());
		maxScale.setTooltip(MESSAGES.layerConfigurationMaximumScaleTooltip());

		form.setFields(label, publicLayer, defaultVisible, minScale, maxScale);

		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		IButton save = new IButton(MESSAGES.saveButtonText());
		save.setIcon(WidgetLayout.iconSave);
		save.setAutoFit(true);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});
		IButton cancel = new IButton(MESSAGES.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.setAutoFit(true);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				cancelled();
			}
		});

		IButton restore = new IButton(MESSAGES.resetButtonText());
		restore.setIcon(WidgetLayout.iconReset);
		restore.setAutoFit(true);
		restore.setTooltip(MESSAGES.resetButtonTooltip());
		restore.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				restored();
			}
		});

		buttons.addMember(save);
		buttons.addMember(cancel);
		buttons.addMember(new LayoutSpacer());
		buttons.addMember(restore);

		// ----------------------------------------------------------

		VLayout vl = new VLayout(10);
		vl.setMargin(10);
		vl.addMember(form);
		vl.addMember(buttons);
		addItem(vl);

		// ----------------------------------------------------------

	}

	public void show() {
		form.clearValues();
		publicLayer.setValue(layer.getLayerModel().isPublic());
		ClientLayerInfo cli = layer.getClientLayerInfo();
		if (cli == null) {
			// If layerInfo not set (yet), copy from model.
			cli = layer.getReferencedLayerInfo();
		}
		label.setValue(cli.getLabel());
		defaultVisible.setValue(cli.isVisible());
		minScale.setValue(SensibleScaleConverter.scaleToString(cli.getMinimumScale()));
		maxScale.setValue(SensibleScaleConverter.scaleToString(cli.getMaximumScale()));

		super.show();
	}

	private void cancelled() {
		hide();
		if (callback != null) {
			callback.execute(false);
		}
	}

	private void saved() {
		if (form.validate()) {
			if (layer.getClientLayerInfo() == null) {
				layer.setCLientLayerInfo(layer.getReferencedLayerInfo());
			}
			ClientLayerInfo cli = layer.getClientLayerInfo();
			cli.setLayerInfo(null);
			cli.setVisible(defaultVisible.getValueAsBoolean());
			cli.setLabel(label.getValueAsString());
			cli.setMinimumScale(SensibleScaleConverter.stringToScale(minScale.getValueAsString()));
			cli.setMaximumScale(SensibleScaleConverter.stringToScale(maxScale.getValueAsString()));

			hide();
			destroy();
			if (callback != null) {
				callback.execute(true);
			}
		}
	}

	private void restored() {
		if (layer.getClientLayerInfo() != null) {
			SC.ask(MESSAGES.layerConfigConfirmRestoreTitle(), 
					MESSAGES.layerConfigConfirmRestoreText(), new BooleanCallback() {

						public void execute(Boolean value) {
							if (value) {
								layer.setCLientLayerInfo(null);
								hide();
								callback.execute(true);
							}
						}
					});
		}
	}
}
