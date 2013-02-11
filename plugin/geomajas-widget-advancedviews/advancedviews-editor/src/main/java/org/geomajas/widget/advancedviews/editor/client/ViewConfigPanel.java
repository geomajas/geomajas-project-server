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
package org.geomajas.widget.advancedviews.editor.client;

import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.util.ScaleInfoUtil;
import org.geomajas.widget.advancedviews.configuration.client.themes.RangeConfig;
import org.geomajas.widget.advancedviews.configuration.client.themes.ViewConfig;
import org.geomajas.widget.advancedviews.editor.client.ThemeConfigurationPanel.State;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
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
 * @author Oliver May
 * 
 */
public class ViewConfigPanel extends Layout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private static final int FORMITEM_WIDTH = 300;

	private DynamicForm form;

	private ThemeConfigurationPanel themeConfigurationPanel;

	private TextItem name;

	private TextAreaItem description;

	private FileUploadForm uploadForm;

	private ViewGrid grid;

	/**
	 * @param themeConfigurationPanel
	 */
	public ViewConfigPanel(ThemeConfigurationPanel themeConfigurationPanel) {
		super();

		this.themeConfigurationPanel = themeConfigurationPanel;

		layout();
	}

	private void layout() {
		// Left layout

		HLayout layout = new HLayout();

		form = new DynamicForm();
		// form.setWidth(ThemeConfigurationPanel.LEFT_WIDTH);
		form.setAutoFocus(true);
		form.setWidth(FORMITEM_WIDTH + 100);
		form.setValidateOnChange(true);
		// form.setWrapItemTitles(false);

		name = new TextItem("name");
		name.setTitle(MESSAGES.themeConfigViewName());
		name.setRequired(true);
		name.setWidth(FORMITEM_WIDTH);
		name.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				themeConfigurationPanel.getState().getViewConfig().setTitle(name.getValueAsString());
			}
		});

		description = new TextAreaItem("viewDescription");
		description.setTitle(MESSAGES.themeConfigViewDescription());
		description.setWidth(FORMITEM_WIDTH);
		description.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				themeConfigurationPanel.getState().getViewConfig().setDescription(description.getValueAsString());
			}
		});

		uploadForm = new FileUploadForm();
		CanvasItem uploadItem = new CanvasItem();
		uploadItem.setCanvas(uploadForm);
		uploadItem.setWidth(FORMITEM_WIDTH);
		uploadItem.setTitle(MESSAGES.themeConfigThemeImage());
		uploadForm.addChangedHandler(new FileUploadForm.ChangedHandler() {
			public void onChange(org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm.ChangedEvent event) {
				themeConfigurationPanel.getState().getViewConfig().setIcon(uploadForm.getUrl());
			}
		});

		form.setFields(name, description, uploadItem);

		layout.addMember(form);

		VLayout gridLayout = new VLayout();
		grid = new ViewGrid();
		
		gridLayout.addMember(grid);
		
		Layout addImgContainer = new Layout();
		addImgContainer.setWidth(64 + 16); //16 from scroller in grid
		addImgContainer.setAlign(Alignment.CENTER);
		addImgContainer.setHeight(16);
		addImgContainer.setLayoutAlign(Alignment.RIGHT);
		
		ImgButton addImg = new ImgButton();
		addImg.setSrc(WidgetLayout.iconAdd);
		addImg.setShowDown(false);
		addImg.setShowRollOver(false);
		addImg.setPrompt(MESSAGES.themeConfigRangeAdd());
		addImg.setHeight(16);
		addImg.setWidth(16);
		addImg.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				double pixPerUnit = themeConfigurationPanel.getMainMap().getUnitLength() / 
					themeConfigurationPanel.getMainMap().getPixelLength();
				ViewConfig viewConfig = themeConfigurationPanel.getState().getViewConfig();
				ScaleInfo maximumScale = ScaleInfoUtil.createScaleInfo(1, 1, pixPerUnit);

				ScaleInfo minimumScale = ScaleInfoUtil.createScaleInfo(1, 1000000000, pixPerUnit);
				
				for (RangeConfig config : viewConfig.getRangeConfigs()) {
					if (config.getMaximumScale().getPixelPerUnit() >= minimumScale.getPixelPerUnit()
							&& config.getMaximumScale().getPixelPerUnit() <= maximumScale.getPixelPerUnit()) {
						minimumScale = new ScaleInfo(config.getMaximumScale());
					}
				}

				RangeConfig config = new RangeConfig();
				config.setMinimumScale(minimumScale);
				config.setMaximumScale(maximumScale);

				viewConfig.getRangeConfigs().add(config);
				grid.fillGrid(themeConfigurationPanel.getState().getViewConfig());
			}
		});
		addImgContainer.addMember(addImg);
		gridLayout.addMember(addImgContainer);


		HLayout group = new HLayout();
		group.setPadding(10);
		group.setIsGroup(true);
		group.setGroupTitle(MESSAGES.themeConfigViewConfigGroup());
		group.addMember(form);
		group.addMember(gridLayout);
		group.setOverflow(Overflow.AUTO);

		addMember(group);
	}

	/**
	 * @param state
	 */
	public void update(State state) {
		if (state.getViewConfig() != null) {
			name.setValue(state.getViewConfig().getTitle());
			description.setValue(state.getViewConfig().getDescription());
			grid.fillGrid(state.getViewConfig());
			uploadForm.setUrl(state.getViewConfig().getIcon());
		}
	}

	/**
	 * 
	 * @author Oliver May
	 * 
	 */
	private class ViewGrid extends ListGrid {

		private static final String FLD_MINSCALE = "minscale";

		private static final String FLD_MINSCALE_PPU = "minscale-ppu";

		private static final String FLD_MAXSCALE = "maxscale";

		private static final String FLD_MAXSCALE_PPU = "maxscale-ppu";

		private static final String FLD_DEL = "delete";

		private static final String FLD_OBJECT = "object";

		private ViewConfig viewConfig;

		public ViewGrid() {
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

			ListGridField minScale = new ListGridField(FLD_MINSCALE, MESSAGES.themeConfigViewGridMinScaleField());
			minScale.setWidth("*");
			minScale.setType(ListGridFieldType.TEXT);
			minScale.setRequired(true);

			ListGridField maxScale = new ListGridField(FLD_MAXSCALE, MESSAGES.themeConfigViewGridMaxScaleField());
			maxScale.setWidth("*");
			maxScale.setType(ListGridFieldType.TEXT);
			maxScale.setRequired(true);

			ListGridField minScalePpu = new ListGridField(FLD_MINSCALE_PPU, "");
			minScalePpu.setHidden(true);
			minScalePpu.setType(ListGridFieldType.FLOAT);

			ListGridField maxScalePpu = new ListGridField(FLD_MAXSCALE_PPU, "");
			maxScalePpu.setHidden(true);
			maxScalePpu.setType(ListGridFieldType.FLOAT);

			ListGridField delete = new ListGridField(FLD_DEL, MESSAGES.configAddDelete());
			delete.setWidth(64);
			delete.setAlign(Alignment.CENTER);

			setFields(minScalePpu, maxScalePpu, minScale, maxScale, delete);

			addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					ListGridRecord record = getSelectedRecord();
					if (record != null) {
						RangeConfig rangeConfig = (RangeConfig) record.getAttributeAsObject(FLD_OBJECT);
						themeConfigurationPanel.selectRangeConfig(rangeConfig);
					}
				}
			});

		}

		private void fillGrid(ViewConfig viewConfig) {
			// clear
			grid.deselectAllRecords();
			grid.setData(new ListGridRecord[] {});
			this.viewConfig = viewConfig;
			// fill
			for (RangeConfig config : viewConfig.getRangeConfigs()) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute(FLD_MINSCALE,
						NumberFormat.getDecimalFormat().format(config.getMinimumScale().getNumerator()) + ":"
								+ NumberFormat.getDecimalFormat().format(config.getMinimumScale().getDenominator()));

				record.setAttribute(FLD_MAXSCALE,
						NumberFormat.getDecimalFormat().format(config.getMaximumScale().getNumerator()) + ":"
								+ NumberFormat.getDecimalFormat().format(config.getMaximumScale().getDenominator()));

				record.setAttribute(FLD_MINSCALE_PPU, config.getMinimumScale().getPixelPerUnit());

				record.setAttribute(FLD_MAXSCALE_PPU, config.getMaximumScale().getPixelPerUnit());

				record.setAttribute(FLD_OBJECT, config);
				grid.addData(record);
			}
			addSort(new SortSpecifier(FLD_MINSCALE_PPU, SortDirection.ASCENDING));
			addSort(new SortSpecifier(FLD_MAXSCALE_PPU, SortDirection.ASCENDING));
		}

		protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
			if (FLD_DEL.equals(grid.getFieldName(colNum))) {
				HLayout layout = new HLayout(2);
				layout.setHeight(16);
				layout.setWidth(1);

				ImgButton addImg = new ImgButton();
				addImg.setSrc(WidgetLayout.iconAdd);
				addImg.setShowDown(false);
				addImg.setShowRollOver(false);
				addImg.setPrompt(MESSAGES.themeConfigRangeAddInline());
				addImg.setHeight(16);
				addImg.setWidth(16);
				addImg.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						RangeConfig range = (RangeConfig) record.getAttributeAsObject(FLD_OBJECT);
						ScaleInfo maximumScale = new ScaleInfo(range.getMinimumScale());

						double pixPerUnit = themeConfigurationPanel.getMainMap().getUnitLength() / 
							themeConfigurationPanel.getMainMap().getPixelLength();

						ScaleInfo minimumScale = ScaleInfoUtil.createScaleInfo(1, 1000000000, pixPerUnit);
						
						for (RangeConfig config : viewConfig.getRangeConfigs()) {
							if (config.getMaximumScale().getPixelPerUnit() >= minimumScale.getPixelPerUnit()
									&& config.getMaximumScale().getPixelPerUnit() <= maximumScale.getPixelPerUnit()) {
								minimumScale = new ScaleInfo(config.getMaximumScale());
							}
						}

						RangeConfig config = new RangeConfig();
						config.setMinimumScale(minimumScale);
						config.setMaximumScale(maximumScale);

						viewConfig.getRangeConfigs().add(config);
						fillGrid(viewConfig);
					}
				});

				layout.addMember(addImg);

				ImgButton deleteImg = new ImgButton();
				deleteImg.setSrc(WidgetLayout.iconRemove);
				deleteImg.setShowDown(false);
				deleteImg.setShowRollOver(false);
				deleteImg.setPrompt(MESSAGES.themeConfigRangeRemove());
				deleteImg.setHeight(16);
				deleteImg.setWidth(16);
				deleteImg.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						SC.ask(MESSAGES.themeConfigRangeRemoveConfirm(), new BooleanCallback() {

							public void execute(Boolean value) {
								if (value) {
									viewConfig.getRangeConfigs().remove(record.getAttributeAsObject(FLD_OBJECT));
									fillGrid(viewConfig);
								}
							}
						});
					}
				});

				layout.addMember(deleteImg);

				return layout;
			}
			return super.createRecordComponent(record, colNum);
		}
	}

}
