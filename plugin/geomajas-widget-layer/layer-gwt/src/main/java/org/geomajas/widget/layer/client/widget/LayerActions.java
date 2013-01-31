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

package org.geomajas.widget.layer.client.widget;

import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.layer.client.LayerMessages;
import org.geomajas.widget.layer.client.util.LayerIconUtil;
import org.geomajas.widget.layer.configuration.client.ClientExtraLayerInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A simple layer actions window.
 * 
 * @author Kristof Heirwegh
 */
public class LayerActions extends Window {

	private static final String BTN_SHOWLEGEND_IMG = "[ISOMORPHIC]/geomajas/silk/information.png";

	private static final String BTN_REMOVEFILTER_IMG = "[SKIN]/actions/remove.png";

	private static final int WINDOW_WIDTH = 375;

	private static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	private Img layerImg;

	private Img layerLabelOverlay;

	private Slider opacitySlider;

	private CheckboxItem layerlabels;

	private final CheckboxItem layerShow;

	private final Layer<?> layer;

	private VectorLayer vectorLayer;

	private RasterLayer rasterLayer;

	public LayerActions(Layer<?> layer) {
		super();
		this.layer = layer;
		if (layer instanceof VectorLayer) {
			this.vectorLayer = (VectorLayer) layer;
		} else if (layer instanceof RasterLayer) { // handle unchecked cast below
			this.rasterLayer = (RasterLayer) layer;
		}
		setTitle(MESSAGES.layerActionsWindowTitle());
		setAutoCenter(true);
		setWidth(WINDOW_WIDTH);
		setAutoSize(true);
		setKeepInParentRect(true);

		VLayout layout = new VLayout();
		layout.setPadding(5);
		layout.setWidth100();
		layout.setMembersMargin(5);

		// ----------------------------------------------------------

		// -- create header --
		HLayout header = new HLayout(5);
		header.setWidth100();
		header.setHeight(90);
		header.addMember(createImage());
		header.addMember(createTitle());
		layout.addMember(header);

		// -- create info --
		VLayout infoLayout = new VLayout(5);
		infoLayout.setPadding(5);
		infoLayout.setAutoHeight();
		infoLayout.setIsGroup(true);
		infoLayout.setGroupTitle(MESSAGES.layerInfoLayerInfo());

		infoLayout.addMember(new LayerInfoListGrid());

		final ClientExtraLayerInfo eli = (ClientExtraLayerInfo) layer.getLayerInfo().getWidgetInfo(
				ClientExtraLayerInfo.IDENTIFIER);
		if (eli != null) {

			if (eli.getLegendUrl() != null) {
				final Anchor a = new Anchor(eli.getLegendUrl());
				a.setHeight("5");
				if (eli.getLegendUrlTitle() != null) {
					a.setText(eli.getLegendUrlTitle());
				}
				a.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
					
					@Override
					public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
						Window window = new Window();
						window.setTitle(a.getText());
						window.setAutoCenter(true);
						window.setWidth("60%");
						window.setHeight("60%");
						window.setSrc(eli.getLegendUrl());
						window.setContentsType("page");
						window.show();
					}
				});
				infoLayout.addMember(a);
			}

			if (eli.getLegendImageUrl() != null || layer instanceof VectorLayer) {
				IButton legendInfo = new IButton(vectorLayer != null ? MESSAGES.layerActionsShowLegendAndFields()
						: MESSAGES.layerActionsShowLegend());
				legendInfo.setIcon(BTN_SHOWLEGEND_IMG);
				legendInfo.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						showLegend();
					}
				});
				infoLayout.addMember(legendInfo);
			}
		} else if (layer instanceof VectorLayer) {
			IButton legendInfo = new IButton(vectorLayer != null ? MESSAGES.layerActionsShowLegendAndFields()
					: MESSAGES.layerActionsShowLegend());
			legendInfo.setIcon(BTN_SHOWLEGEND_IMG);
			legendInfo.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					showLegend();
				}
			});
			infoLayout.addMember(legendInfo);
		}

		// -- create actions --
		VLayout actiesLayout = new VLayout(5);
		actiesLayout.setPadding(5);
		actiesLayout.setIsGroup(true);
		actiesLayout.setAutoHeight();
		actiesLayout.setGroupTitle(MESSAGES.layerInfoLayerActions());

		DynamicForm form = new DynamicForm();
		form.setTitleOrientation(TitleOrientation.TOP);
		form.setHeight(30);
		form.setColWidths(0, WINDOW_WIDTH - 50);

		layerShow = new CheckboxItem();
		initLayerShow();

		if (vectorLayer != null) {
			layerlabels = new CheckboxItem();
			initLabels();
			form.setFields(layerlabels, layerShow);

		} else {
			opacitySlider = new Slider(MESSAGES.layerActionsOpacity());
			String raw = rasterLayer.getLayerInfo().getStyle();
			double opacity = 1d;
			if (raw != null && !"".equals(raw)) {
				try {
					opacity = Double.parseDouble(raw);
				} catch (NumberFormatException e) {
					// ignore
				}
			}
			initSlider((int) Math.round(opacity * 100));
			actiesLayout.addMember(opacitySlider);
			form.setFields(layerShow);
		}
		actiesLayout.addMember(form);

		// ----------------------------------------------------------

		if (vectorLayer != null && vectorLayer.getFilter() != null && !"".equals(vectorLayer.getFilter())) {
			final IButton removeFilter = new IButton(MESSAGES.layerActionsRemoveFilter());
			removeFilter.setIcon(BTN_REMOVEFILTER_IMG);
			String tooltip = vectorLayer.getFilter();
			if (tooltip.length() > 1000) {
				tooltip = tooltip.substring(0, 1000);
			}
			removeFilter.setTooltip(tooltip);
			removeFilter.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					vectorLayer.setFilter(null);
					removeFilter.setVisible(false);
				}
			});
			actiesLayout.addMember(removeFilter);
		}

		// ----------------------------------------------------------

		layout.addMember(infoLayout);
		layout.addMember(actiesLayout);

		addItem(layout);
	}

	private Canvas createTitle() {
		HTMLPane title = new HTMLPane();
		StringBuffer html = new StringBuffer("<span style='font-size: 1.2em; font-weight: bold;'>" + layer.getLabel()
				+ "</span><br /><br />");
		if (vectorLayer != null) {
			html.append(MESSAGES.layerInfoLayerInfoFldLayerTypeVector());
			html.append("<br />" + MESSAGES.layerInfoLayerInfoFldLayerType() + ": "
					+ vectorLayer.getLayerInfo().getLayerType().name());
		} else {
			html.append(MESSAGES.layerInfoLayerInfoFldLayerTypeRaster());
		}
		title.setContents(html.toString());
		return title;
	}

	private Canvas createImage() {
		layerImg = LayerIconUtil.getLargeLayerIcon(layer);
		layerImg.setImageType(ImageStyle.NORMAL);

		Canvas sampleMap = new Canvas();
		sampleMap.setAutoHeight();
		sampleMap.setAutoWidth();
		// sampleMap.setSize("89px", "89px");

		if (vectorLayer != null) {
			layerLabelOverlay = LayerIconUtil.getLabelOverlayImg();
			layerLabelOverlay.setImageType(ImageStyle.NORMAL);
			sampleMap.addChild(layerImg);
			sampleMap.addChild(layerLabelOverlay);
		} else {
			Img layerOpacityUnderlay = LayerIconUtil.getOpacityUnderlayImg();
			layerOpacityUnderlay.setImageType(ImageStyle.NORMAL);
			layerImg.setUseOpacityFilter(true);
			sampleMap.addChild(layerOpacityUnderlay);
			sampleMap.addChild(layerImg);
		}

		return sampleMap;
	}

	private void initLayerShow() {
		layerShow.setTitle(MESSAGES.layerActionsShowLayer());
		layerShow.setTooltip(MESSAGES.layerActionsShowLayerToolTip());
		layerShow.setTitleOrientation(TitleOrientation.LEFT);
		if (vectorLayer != null) {
			layerShow.setValue(vectorLayer.isVisible());
		} else {
			layerShow.setValue(rasterLayer.isVisible());
		}
		layerShow.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (vectorLayer != null) {
					vectorLayer.setVisible(layerShow.getValueAsBoolean());
				} else {
					rasterLayer.setVisible(layerShow.getValueAsBoolean());
				}
			}
		});
	}

	private void initLabels() {
		layerlabels.setTitle(MESSAGES.layerActionsLabels());
		layerlabels.setTooltip(MESSAGES.layerActionsLabelsToolTip());
		layerlabels.setTitleOrientation(TitleOrientation.LEFT);
		layerlabels.setValue(vectorLayer.isLabelsVisible());
		Boolean val = vectorLayer.isLabelsVisible();
		layerLabelOverlay.setVisible(val);
		layerlabels.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				Boolean val = layerlabels.getValueAsBoolean();
				layerLabelOverlay.setVisible(val);
				vectorLayer.setLabeled(val);
			}
		});
	}

	private void initSlider(int initialValue) {
		opacitySlider.setValue(initialValue);
		opacitySlider.setMinValue(0);
		opacitySlider.setMaxValue(100);
		opacitySlider.setNumValues(101);
		opacitySlider.setMaxValueLabel("100%");
		opacitySlider.setVertical(false);
		opacitySlider.setWidth(230);
		opacitySlider.setLabelWidth(Integer.parseInt(MESSAGES.layerActionsOpacitySliderLabelWidth()));
		opacitySlider.addValueChangedHandler(new ValueChangedHandler() {

			public void onValueChanged(ValueChangedEvent event) {
				double val = opacitySlider.getValue();
				layerImg.setOpacity((int) val);
				if (val > 0) {
					val /= 100;
				}
				rasterLayer.setOpacity(val);
			}
		});
	}

	private void showLegend() {
		LayerInfo li = new LayerInfo(layer);
		li.draw();
	}

	// -------------------------------------------------

	/**
	 * Listgrid for layerinfo.
	 */
	private class LayerInfoListGrid extends ListGrid {

		private static final String KEY_FLD = "keyField";

		private static final String VALUE_FLD = "valueField";

		public LayerInfoListGrid() {
			super();
			setWidth100();
			setHeight(50);
			setCanEdit(false);
			setShowSelectedStyle(false);
			setShowRollOver(false);
			setShowHeader(false);
			setShowAllRecords(true);
			setBodyOverflow(Overflow.VISIBLE);
			setOverflow(Overflow.VISIBLE);
			setLeaveScrollbarGap(false);
			setWrapCells(true);
			setFixedRecordHeights(false);

			// -- FIELDS
			ListGridField keyField = new ListGridField(KEY_FLD, "Veldnaam", 150);
			ListGridField valueField = new ListGridField(VALUE_FLD, "Waarde");
			valueField.setWidth("*");
			setFields(keyField, valueField);

			// -- VALUES
			final RecordList recordList = new RecordList();

			ClientExtraLayerInfo eli = (ClientExtraLayerInfo) layer.getLayerInfo().getWidgetInfo(
					ClientExtraLayerInfo.IDENTIFIER);
			if (eli != null) {
				if (eli.getSource() != null && !"".equals(eli.getSource())) {
					recordList.add(createRecord(MESSAGES.layerInfoLayerInfoSource() + ":", eli.getSource()));
				}
				if (eli.getDate() != null) {
					recordList.add(createRecord(MESSAGES.layerInfoLayerInfoDate() + ":", eli.getDate()));
					// recordList.add(createRecord(messages.layerInfoLayerInfoDate() + ":",
					// DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG).format(eli.getDate())));
				}
			}

			String layerMax, layerMin;
			if (layer instanceof VectorLayer) {
				VectorLayer vl = (VectorLayer) layer;
				layerMax = buildScale(vl.getLayerInfo().getMaximumScale());
				layerMin = buildScale(vl.getLayerInfo().getMinimumScale());

			} else {
				RasterLayer rl = (RasterLayer) layer;
				layerMax = buildScale(rl.getLayerInfo().getMaximumScale());
				layerMin = buildScale(rl.getLayerInfo().getMinimumScale());
			}

			recordList.add(createRecord(MESSAGES.layerInfoLayerInfoFldMaxViewScale() + ":", layerMax));
			recordList.add(createRecord(MESSAGES.layerInfoLayerInfoFldMinViewScale() + ":", layerMin));

			setData(recordList);
		}

		private ListGridRecord createRecord(String key, String value) {
			ListGridRecord r = new ListGridRecord();
			r.setAttribute(KEY_FLD, key);
			r.setAttribute(VALUE_FLD, "<b>" + (value == null ? "-" : value) + "</b>");
			return r;
		}

		private String buildScale(ScaleInfo si) {
			// double PIXELPERUNIT = 3779.527559055;
			// long denominator = Math.round(PIXELPERUNIT * (1D / si.getPixelPerUnit()));
			return ((int) si.getNumerator()) + " : "
					+ NumberFormat.getFormat("#,##0").format(Math.round(si.getDenominator()));
		}
	}
}
