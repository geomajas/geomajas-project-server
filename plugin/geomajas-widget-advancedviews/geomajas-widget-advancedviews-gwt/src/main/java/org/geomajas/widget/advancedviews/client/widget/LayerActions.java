/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews.client.widget;

import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.widget.advancedviews.client.AdvancedViewsMessages;
import org.geomajas.widget.advancedviews.client.util.LayerIconHelper;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
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
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A simple layeractions window.
 * @author Kristof Heirwegh
 *
 */
public class LayerActions extends Window {

	private static final String BTN_SHOWLEGEND_IMG = "[ISOMORPHIC]/geomajas/silk/information.png";

	private AdvancedViewsMessages messages = GWT.create(AdvancedViewsMessages.class);

	private Img layerImg;
	private Img layerLabelOverlay;
	private Img layerTransparencyUnderlay;
	private Slider transparencySlider;
	private CheckboxItem labels;
	private VectorLayer vectorLayer;
	private RasterLayer rasterLayer;

	public LayerActions(Layer<?> layer) {
		super();
		if (layer instanceof VectorLayer) {
			this.vectorLayer = (VectorLayer) layer;
		} else {
			this.rasterLayer = (RasterLayer) layer;
		}

		setTitle(messages.layerActionsWindowTitle() + " - " + layer.getLabel());
		setAutoCenter(true);
		setAutoSize(true);

		VLayout layout = new VLayout();
		layout.setPadding(5);
		layout.setMembersMargin(5);

		// ----------------------------------------------------------

		layerImg = LayerIconHelper.getLargeLayerIcon(layer);
		layerImg.setImageType(ImageStyle.NORMAL);
		layerLabelOverlay = LayerIconHelper.getLabelOverlayImg();
		layerLabelOverlay.setImageType(ImageStyle.NORMAL);
		layerTransparencyUnderlay = LayerIconHelper.getTransparencyUnderlayImg();
		layerTransparencyUnderlay.setImageType(ImageStyle.NORMAL);

		// ----------------------------------------------------------

		if (vectorLayer != null) {
			Canvas sampleMap = new Canvas();
			sampleMap.setAutoHeight();
			sampleMap.setAutoWidth();
			// sampleMap.setSize("89px", "89px");
			sampleMap.addChild(layerImg);
			sampleMap.addChild(layerLabelOverlay);

			labels = new CheckboxItem();
			initLabels();

			DynamicForm	form = new DynamicForm();
			form.setTitleOrientation(TitleOrientation.TOP);
			form.setHeight(40);
			form.setFields(labels);

			layout.addMember(sampleMap);
			layout.addMember(form);

		} else {
			Canvas sampleMap = new Canvas();
			sampleMap.setAutoHeight();
			sampleMap.setAutoWidth();
			// sampleMap.setSize("89px", "89px");
			layerImg.setUseOpacityFilter(true);
			sampleMap.addChild(layerTransparencyUnderlay);
			sampleMap.addChild(layerImg);

			transparencySlider = new Slider(messages.layerActionsOpacity());
			String raw = rasterLayer.getLayerInfo().getStyle();
			double opacity = 1d;
			if (raw != null && !"".equals(raw)) {
				try {
					opacity = Double.parseDouble(raw);
				} catch (Exception e) {
					// ignore
				}
			}
			initSlider((int) Math.round(opacity * 100));

			layout.addMember(sampleMap);
			layout.addMember(transparencySlider);
		}

		// ----------------------------------------------------------

		IButton legendInfo = new IButton(messages.layerActionsShowLegend());
		legendInfo.setIcon(BTN_SHOWLEGEND_IMG);
		legendInfo.setWidth(230);
		legendInfo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showLegend();
			}
		});

		// ----------------------------------------------------------

		layout.addMember(legendInfo);
		addItem(layout);
	}

	private void initLabels() {
		labels.setTitle(messages.layerActionsLabels());
		labels.setTooltip(messages.layerActionsLabelsToolTip());
		labels.setTitleOrientation(TitleOrientation.LEFT);
		labels.setValue(vectorLayer.isLabeled());
		Boolean val = vectorLayer.isLabeled();
		layerLabelOverlay.setVisible(val);
		labels.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				Boolean val = labels.getValueAsBoolean();
				layerLabelOverlay.setVisible(val);
				vectorLayer.setLabeled(val);
			}
		});
	}

	private void initSlider(int initialValue) {
		transparencySlider.setValue(initialValue);
		transparencySlider.setMinValue(0);
		transparencySlider.setMaxValue(100);
		transparencySlider.setNumValues(101);
		transparencySlider.setMaxValueLabel("100%");
		transparencySlider.setVertical(false);
		transparencySlider.setWidth(230);
		transparencySlider.setLabelWidth(Integer.parseInt(messages.layerActionsOpacitySliderLabelWidth()));
		transparencySlider.addValueChangedHandler(new ValueChangedHandler() {
			public void onValueChanged(ValueChangedEvent event) {
				double val = transparencySlider.getValue();
				layerImg.setOpacity((int) val);
				if (val > 0) {
					val /= 100;
				}
				rasterLayer.setOpacity(val);
			}
		});
	}

	private void showLegend() {
		LayerInfo li = new LayerInfo((vectorLayer != null ? vectorLayer : rasterLayer));
		li.draw();
	}
}
