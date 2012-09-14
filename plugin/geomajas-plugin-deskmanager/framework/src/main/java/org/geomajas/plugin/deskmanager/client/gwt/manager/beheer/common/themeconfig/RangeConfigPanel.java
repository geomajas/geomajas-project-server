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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.themeconfig;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.themeconfig.LayerAddWindow.LayerAddCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.common.themeconfig.ThemeConfigurationPanel.State;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.ScaleInfoUtil;
import org.geomajas.widget.advancedviews.configuration.client.themes.LayerConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Panel to configura a theme range.
 * 
 * @author Oliver May
 * 
 */
public class RangeConfigPanel extends Layout {

	private static final int FORMITEM_WIDTH = 300;

	private ThemeConfigurationPanel themeConfigurationPanel;

	private DynamicForm form;

	private FileUploadForm uploadForm;

	private LayerGrid grid;

	private SpinnerItem minScale;

	private SpinnerItem maxScale;

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	/**
	 * @param themeConfigurationPanel
	 */
	public RangeConfigPanel(ThemeConfigurationPanel themeConfigurationPanel) {
		super();

		this.themeConfigurationPanel = themeConfigurationPanel;

		layout();
	}

	/**
	 * 
	 */
	private void layout() {
		// Left layout

		HLayout layout = new HLayout();

		form = new DynamicForm();
		// form.setWidth(ThemeConfigurationPanel.LEFT_WIDTH);
		form.setAutoFocus(true);
		form.setWidth(FORMITEM_WIDTH + 100);
		form.setWrapItemTitles(false);

		minScale = new SpinnerItem();
		minScale.setTitle(MESSAGES.themeConfigRangeMinScale());
		minScale.setRequired(true);
		minScale.setWidth(FORMITEM_WIDTH);
		minScale.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				ScaleInfo scale = themeConfigurationPanel.getState().getRangeConfig().getMinimumScale();
				ScaleInfoUtil.changeDenominator(scale, Double.parseDouble(minScale.getValueAsString()));
			}
		});

		maxScale = new SpinnerItem();
		maxScale.setTitle(MESSAGES.themeConfigRangeMaxScale());
		maxScale.setRequired(true);
		maxScale.setWidth(FORMITEM_WIDTH);
		maxScale.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				ScaleInfo scale = themeConfigurationPanel.getState().getRangeConfig().getMaximumScale();
				ScaleInfoUtil.changeDenominator(scale, Double.parseDouble(maxScale.getValueAsString()));
			}
		});

		uploadForm = new FileUploadForm();
		CanvasItem uploadItem = new CanvasItem();
		uploadItem.setCanvas(uploadForm);
		uploadItem.setTitle(MESSAGES.themeConfigThemeImage());
		uploadItem.setWidth(FORMITEM_WIDTH);
		uploadForm.addChangedHandler(new FileUploadForm.ChangedHandler() {
			public void onChange(org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm.ChangedEvent event) {
				themeConfigurationPanel.getState().getRangeConfig().setIcon(uploadForm.getUrl());
			}
		});

		form.setFields(minScale, maxScale, uploadItem);

		layout.addMember(form);

		VLayout gridLayout = new VLayout();
		grid = new LayerGrid();
		gridLayout.addMember(grid);

		Layout addImgContainer = new Layout();
		addImgContainer.setWidth(64 + 16); // 16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(16);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);

		ImgButton addImg = new ImgButton();
		addImg.setSrc(WidgetLayout.iconAdd);
		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt(MESSAGES.themeConfigLayerAdd());
		addImg.setHeight(16);
		addImg.setWidth(16);
		addImg.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				new LayerAddWindow(themeConfigurationPanel, new LayerAddCallback() {
					public void execute(ClientLayerInfo layer) {
						LayerConfig layerConfig = new LayerConfig();
						layerConfig.setLayer(layer);
						themeConfigurationPanel.getState().getRangeConfig().getLayerConfigs().add(layerConfig);
						themeConfigurationPanel.selectLayerConfig(layerConfig);
					}
				});
			}
		});

		addImgContainer.addMember(addImg);
		gridLayout.addMember(addImgContainer);

		HLayout group = new HLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.themeConfigThemeConfigGroup());
		group.addMember(form);
		group.addMember(gridLayout);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	/**
	 * @param state
	 */
	public void update(State state) {
		if (state.getRangeConfig() != null) {
			minScale.setValue(state.getRangeConfig().getMinimumScale().getDenominator());
			maxScale.setValue(state.getRangeConfig().getMaximumScale().getDenominator());
			grid.fillGrid(state.getRangeConfig());
		}
	}

	/**
	 * 
	 * @author Oliver May
	 * 
	 */
	private class LayerGrid extends ListGrid {

		private static final String FLD_LAYER = "layer";

		private static final String FLD_VISIBLE = "visible";

		private static final String FLD_OPACITY = "opacity";

		private static final String FLD_DEL = "delete";

		private static final String FLD_OBJECT = "object";

		public LayerGrid() {
			super();
			setWidth100();
			setHeight100();
			setAlternateRecordStyles(true);
			setSelectionType(SelectionStyle.SINGLE);
			setShowRollOverCanvas(true);
			setShowAllRecords(true);
			setAlternateRecordStyles(true);
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			ListGridField layer = new ListGridField(FLD_LAYER, MESSAGES.themeConfigLayerGridNameField());
			layer.setWidth("*");
			layer.setType(ListGridFieldType.TEXT);

			ListGridField visible = new ListGridField(FLD_VISIBLE, MESSAGES.themeConfigLayerGridVisibleField());
			visible.setWidth("*");
			visible.setType(ListGridFieldType.BOOLEAN);

			ListGridField opacity = new ListGridField(FLD_OPACITY, MESSAGES.themeConfigLayerGridOpacityField());
			opacity.setWidth("*");
			opacity.setType(ListGridFieldType.TEXT);

			ListGridField delete = new ListGridField(FLD_DEL, "Add/Delete");
			delete.setWidth(64);
			delete.setAlign(Alignment.CENTER);

			setFields(layer, visible, opacity, delete);

			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					ListGridRecord record = getSelectedRecord();
					if (record != null) {
						LayerConfig layerConfig = (LayerConfig) record.getAttributeAsObject(FLD_OBJECT);
						themeConfigurationPanel.selectLayerConfig(layerConfig);
					}
				}
			});

		}

		private void fillGrid(RangeConfig rangeConfig) {
			// clear
			grid.deselectAllRecords();
			grid.setData(new ListGridRecord[] {});
			// fill
			for (LayerConfig config : rangeConfig.getLayerConfigs()) {
				ListGridRecord record = new ListGridRecord();

				record.setAttribute(FLD_LAYER, config.getLayer().getLabel());
				record.setAttribute(FLD_VISIBLE, config.isVisible());
				record.setAttribute(FLD_OPACITY, NumberFormat.getPercentFormat().format(config.getOpacity()));

				record.setAttribute(FLD_OBJECT, config);
				grid.addData(record);
			}
		}

		protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
			if (FLD_DEL.equals(grid.getFieldName(colNum))) {
				ImgButton deleteImg = new ImgButton();
				deleteImg.setSrc(WidgetLayout.iconRemove);
				deleteImg.setShowDown(false);
				deleteImg.setShowRollOver(false);
				deleteImg.setPrompt(MESSAGES.themeConfigLayerRemove());
				deleteImg.setHeight(16);
				deleteImg.setWidth(16);
				deleteImg.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						SC.ask(MESSAGES.themeConfigLayerRemoveConfirm(), new BooleanCallback() {

							public void execute(Boolean value) {
								if (value) {
									themeConfigurationPanel.getState().getRangeConfig().getLayerConfigs()
											.remove(record.getAttributeAsObject(FLD_OBJECT));
									update(themeConfigurationPanel.getState());
								}
							}
						});
					}
				});

				return deleteImg;
			}
			return super.createRecordComponent(record, colNum);
		}
	}

}
