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

import java.util.Map;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow.NotificationWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.SensibleScaleConverter;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayerTreeNodeResponse;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerViewDto;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;

import com.google.gwt.user.client.Timer;
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

	private static final int FORMITEM_WIDTH = 300;

	private LayerTreeNodeDto layer;

	private BooleanCallback callback;

	private DynamicForm form;

	private Map<String, LayerModelDto> layerModels;

	private TextItem label;

	private CheckboxItem publicLayer;

	private CheckboxItem defaultVisible;

	private CheckboxItem showInLegend;

	private TextItem minScale;

	private TextItem maxScale;

	/**
	 * @param layer
	 * @param callback returns true if saved, false if cancelled.
	 */
	public LayerConfigurationWindow() {
		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle("Configureer Laag");
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);

		form = new DynamicForm();
		form.setIsGroup(true);
		form.setGroupTitle("Laag Eigenschappen");
		form.setPadding(5);
		form.setAutoWidth();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(2);
		form.setTitleOrientation(TitleOrientation.LEFT);

		label = new TextItem("Naam");
		label.setTitle("Naam");
		label.setRequired(true);
		label.setWidth(FORMITEM_WIDTH);
		label.setWrapTitle(false);
		label.setTooltip("Aangepaste naam voor de laag.");

		publicLayer = new CheckboxItem();
		publicLayer.setTitle("Publieke laag");
		publicLayer.setDisabled(true); // altijd readonly hier
		publicLayer.setWrapTitle(false);

		defaultVisible = new CheckboxItem();
		defaultVisible.setTitle("Laag staat standaard aan");
		defaultVisible.setWrapTitle(false);
		defaultVisible.setTooltip("Aangevinkt: De laag wordt standaard weergegeven.");

		showInLegend = new CheckboxItem();
		showInLegend.setTitle("Laag wordt getoond in legende");
		showInLegend.setWrapTitle(false);
		showInLegend.setTooltip("Aangevinkt: De laag wordt opgenomen in de legende.");

		minScale = new TextItem();
		minScale.setTitle("Minimum schaal");
		minScale.setWidth(FORMITEM_WIDTH / 2);
		minScale.setWrapTitle(false);
		minScale.setTooltip("Minimum schaal waarop de laag zichtbaar is.");
		minScale.setValidators(new ScaleValidator());

		maxScale = new TextItem();
		maxScale.setTitle("Maximum schaal");
		maxScale.setWidth(FORMITEM_WIDTH / 2);
		maxScale.setWrapTitle(false);
		maxScale.setValidators(new ScaleValidator());
		maxScale.setTooltip("Maximum schaal waarop de laag zichtbaar is.");

		form.setFields(label, publicLayer, defaultVisible, showInLegend, minScale, maxScale);

		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		IButton save = new IButton("Opslaan");
		save.setIcon(WidgetLayout.iconSave);
		save.setAutoFit(true);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});
		IButton cancel = new IButton("Annuleren");
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.setAutoFit(true);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				cancelled();
			}
		});

		IButton restore = new IButton("Herstel");
		restore.setIcon(WidgetLayout.iconReset);
		restore.setAutoFit(true);
		restore.setTooltip("Herstel naar standaardwaarden");
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

		CommService.getSystemLayerTreeNode(new DataCallback<GetSystemLayerTreeNodeResponse>() {

			public void execute(GetSystemLayerTreeNodeResponse result) {
				layerModels = result.getLayerModels();
			}
		});
	}

	public void show(final LayerTreeNodeDto layer, final BooleanCallback callback) {
		if (layer == null || !layer.isLeaf()) {
			throw new IllegalArgumentException("Please provide a leaf LayerTreeNode");
		}

		if (layerModels == null) {
			NotificationWindow.showInfoMessage("Bezig met ophalen van eigenschappen...");
			new Timer() {

				public void run() {
					show(layer, callback);
				}
			} .schedule(2000);
			return;
		}

		this.callback = callback;
		this.layer = layer;

		form.clearValues();
		publicLayer.setValue(layer.isPublicLayer());
		if (layer.getView() != null) {
			LayerViewDto lvd = layer.getView();

			label.setValue(lvd.getLabel());
			defaultVisible.setValue(lvd.isDefaultVisible());
			showInLegend.setValue(lvd.isShowInLegend());
			minScale.setValue(SensibleScaleConverter.scaleToString(lvd.getMinimumScale()));
			maxScale.setValue(SensibleScaleConverter.scaleToString(lvd.getMaximumScale()));

		} else {
			label.setValue(layer.getName());
			LayerModelDto lmd = layerModels.get(layer.getClientLayerId());
			if (lmd != null) {
				defaultVisible.setValue(lmd.isDefaultVisible());
				showInLegend.setValue(lmd.isShowInLegend());
				minScale.setValue(SensibleScaleConverter.scaleToString(lmd.getMinScale()));
				maxScale.setValue(SensibleScaleConverter.scaleToString(lmd.getMaxScale()));
			} else {
				SC.logWarn("LayerModel not found ?!");
			}
		}

		show();
	}

	private void cancelled() {
		hide();
		if (callback != null) {
			callback.execute(false);
		}
	}

	private void saved() {
		if (form.validate()) {
			if (layer.getView() == null) {
				layer.setView(new LayerViewDto());
			}
			LayerViewDto lvd = layer.getView();
			lvd.setDefaultVisible(defaultVisible.getValueAsBoolean());
			lvd.setLabel(label.getValueAsString());
			lvd.setShowInLegend(showInLegend.getValueAsBoolean());
			lvd.setMinimumScale(SensibleScaleConverter.stringToScale(minScale.getValueAsString()));
			lvd.setMaximumScale(SensibleScaleConverter.stringToScale(maxScale.getValueAsString()));

			hide();
			if (callback != null) {
				callback.execute(true);
			}
		}
	}

	private void restored() {
		if (layer.getView() != null) {
			SC.ask("Wijzigingen verwerpen", "Gemaakte wijzigingen verwerpen en opnieuw instellen op default waarden?",
					new BooleanCallback() {

						public void execute(Boolean value) {
							if (value) {
								layer.setView(null);
								hide();
								callback.execute(true);
							}
						}
					});
		}
	}
}
