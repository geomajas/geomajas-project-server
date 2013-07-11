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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smartgwt.client.widgets.Slider;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.LayerWidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditor;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactory;
import org.geomajas.plugin.deskmanager.client.gwt.manager.editor.WidgetEditorFactoryRegistry;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.SensibleScaleConverter;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.ExpertSldEditorHelper;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Configuration window for individual layers.
 *
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class LayerConfigurationWindow extends Window {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private static final int TABSET_WIDTH = 600;

	private static final int TABSET_HEIGHT = 300;

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

	private TabSet tabset;

	private TabSet widgetTabset;

	private List<WidgetEditorHandler> widgetEditors = new ArrayList<WidgetEditorHandler>();

	private ExpertSldEditorHelper styleHelper;

	private Slider opacitySlider;

	/**
	 * Construct a layer configuration window.
	 *
	 * @param layerDto
	 * @param callback returns true if saved, false if canceled.
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

		tabset = new TabSet();
		tabset.setWidth(TABSET_WIDTH);
		tabset.setHeight(TABSET_HEIGHT);
		tabset.addTab(createSettingsTab());

		ClientLayerInfo config = layerDto.getClientLayerInfo() == null ? layer.getReferencedLayerInfo() : layer
				.getClientLayerInfo();

		if (config instanceof ClientVectorLayerInfo) {
			tabset.addTab(createVectorLayerStyleTab());
			styleHelper = new ExpertSldEditorHelper((ClientVectorLayerInfo) config);
		} else if (config instanceof ClientRasterLayerInfo) {
			tabset.addTab(createRasterStyleTab((ClientRasterLayerInfo) config));
		}

		widgetTabset = new TabSet();
		widgetTabset.setTabBarPosition(Side.LEFT);
		widgetTabset.setWidth100();
		widgetTabset.setHeight100();
		widgetTabset.setOverflow(Overflow.HIDDEN);
		widgetTabset.setTabBarThickness(100);
		Tab tab = new Tab(MESSAGES.geodeskDetailTabWidgets());
		tab.setPane(widgetTabset);
		tabset.addTab(tab);

		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		IButton save = new IButton(MESSAGES.saveButtonText());
		save.setIcon(WidgetLayout.iconSave);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				saved();
			}
		});
		IButton cancel = new IButton(MESSAGES.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				cancelled();
			}
		});

		IButton restore = new IButton(MESSAGES.resetButtonText());
		restore.setIcon(WidgetLayout.iconReset);
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
		vl.addMember(tabset);
		vl.addMember(buttons);
		addItem(vl);

		// ----------------------------------------------------------

	}

	private Tab createRasterStyleTab(final ClientRasterLayerInfo config) {
		Tab tab = new Tab(MESSAGES.layerConfigurationLayerStyle());
		VLayout vl = new VLayout(10);
		vl.setMargin(10);

		opacitySlider = new Slider();
		opacitySlider.setMinValue(0);
		opacitySlider.setMaxValue(1);
		if (config.getStyle() != null && !"".equals(config.getStyle())) {
			try {
				opacitySlider.setValue(Float.parseFloat(config.getStyle()));
			} catch (NumberFormatException e) {
				opacitySlider.setValue(1f);
			}
		}

		vl.addMember(opacitySlider);



		tab.setPane(vl);
		return tab;
	}

	private Tab createSettingsTab() {
		Tab tab = new Tab(MESSAGES.layerConfigurationLayerProperties());
		form = new DynamicForm();
		form.setIsGroup(true);
		form.setGroupTitle(MESSAGES.layerConfigurationLayerProperties());
		form.setPadding(5);
		form.setWidth100();
		form.setHeight100();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(2);
		form.setTitleOrientation(TitleOrientation.LEFT);

		label = new TextItem(FLD_NAME);
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

		tab.setPane(form);

		return tab;
	}

	private Tab createVectorLayerStyleTab() {
		Tab tab = new Tab(MESSAGES.layerConfigurationLayerStyle());
		VLayout vl = new VLayout(10);
		vl.setMargin(10);

		IButton openStyleEditor = new IButton(MESSAGES.layerConfigExpertEditorBtn());
		openStyleEditor.setIcon(WidgetLayout.iconEdit);
		openStyleEditor.setAutoFit(true);
		openStyleEditor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				styleHelper.showExpertStyleEditor();
			}
		});

		vl.addMember(openStyleEditor);

		tab.setPane(vl);
		return tab;
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

		clearWidgetTabs();
		loadWidgetTabs(layer);

		super.show();
	}

	private void cancelled() {
		hide();
		destroy();
		if (callback != null) {
			callback.execute(false);
		}
	}

	private void saved() {
		if (form.validate()) {
			if (layer.getClientLayerInfo() == null) {
				layer.setClientLayerInfo(layer.getReferencedLayerInfo()); // clone??
			}
			ClientLayerInfo cli = layer.getClientLayerInfo();
			cli.setLayerInfo(null);
			cli.setVisible(defaultVisible.getValueAsBoolean());
			cli.setLabel(label.getValueAsString());
			cli.setMinimumScale(SensibleScaleConverter.stringToScale(minScale.getValueAsString()));
			cli.setMaximumScale(SensibleScaleConverter.stringToScale(maxScale.getValueAsString()));

			if (cli instanceof ClientRasterLayerInfo && opacitySlider != null) {
				((ClientRasterLayerInfo) cli).setStyle(new Double(opacitySlider.getValue()).toString());
			} else if (cli instanceof ClientVectorLayerInfo && styleHelper != null) {
				styleHelper.apply((ClientVectorLayerInfo) cli);
			}

			for (WidgetEditorHandler h : widgetEditors) {
				h.save(layer);
			}

			hide();
			destroy();
			if (callback != null) {
				callback.execute(true);
			}
		}
	}

	@Override
	public void destroy() {
		styleHelper.destroy();
		super.destroy();
	}

	private void restored() {
		if (layer.getClientLayerInfo() != null || !layer.getWidgetInfo().isEmpty()) {
			SC.ask(MESSAGES.layerConfigConfirmRestoreTitle(), MESSAGES.layerConfigConfirmRestoreText(),
					new BooleanCallback() {
						public void execute(Boolean value) {
							if (value) {
								layer.setClientLayerInfo(null);
								layer.getWidgetInfo().clear();
								hide();
								callback.execute(true);
							}
						}
					});
		}
	}

	/** Clear all custom widget tabs from the last blueprint. */
	private void clearWidgetTabs() {
		for (Tab tab : widgetTabset.getTabs()) {
			widgetTabset.removeTab(tab);
		}
		widgetEditors.clear();
	}

	/**
	 * Load all widget editors that are available on this blueprints user application, and add them to the tabset.
	 *
	 * @param bgd the basegeodesk.
	 */
	private void loadWidgetTabs(LayerDto bgd) {
		for (String key : WidgetEditorFactoryRegistry.getLayerRegistry().getWidgetEditors().keySet()) {
			addWidgetTab(WidgetEditorFactoryRegistry.getMapRegistry().get(key), bgd.getWidgetInfo(), bgd);
		}
	}

	/**
	 * Add a widget editor tab to the tabset for a given editor factory, set of widget info's (where one of will be edited
	 * by the editor) and a base geodesk that could provide extra context to the editor.
	 *
	 * @param editorFactory the editor factory
	 * @param widgetInfos all the widget infos
	 * @param layerDto the layer model
	 */
	private void addWidgetTab(final WidgetEditorFactory editorFactory, final Map<String, ClientWidgetInfo> widgetInfos,
			final LayerDto layerDto) {
		if (editorFactory != null) {
			Tab tab = new Tab(editorFactory.getName());
			final WidgetEditor editor = editorFactory.createEditor();
			if (editor instanceof LayerWidgetEditor) {
				((LayerWidgetEditor) editor).setLayer(layerDto.getLayerModel());
			}
			editor.setWidgetConfiguration(widgetInfos.get(editorFactory.getKey()));

			// Create tab layout
			VLayout layout = new VLayout();
			layout.setMargin(5);

			widgetEditors.add(new WidgetEditorHandler() {

				@Override
				public void save(LayerDto layer) {
					layer.getWidgetInfo().put(editorFactory.getKey(), editor.getWidgetConfiguration());
				}
			});

			layout.addMember(editor.getCanvas());
			tab.setPane(layout);

			widgetTabset.addTab(tab);
		}
	}

	/**
	 * Interface for handling widget editors.
	 *
	 * @author Oliver May
	 */
	private interface WidgetEditorHandler {

		/**
		 * Set the correct information in the layer dto.
		 *
		 * @param layer the layer dto
		 */
		void save(LayerDto layer);
	}
}
