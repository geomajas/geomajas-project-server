/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.client.view;

import java.util.List;

import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.SldConstant;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.common.client.presenter.PolygonSymbolizerPresenter;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent.SldContentChangedHandler;
import org.geomajas.sld.editor.common.client.view.ViewUtil;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Default implementation of {@link PolygonSymbolizerPresenter.MyView}.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 *
 */
public class PolygonSymbolizerView extends ViewImpl implements PolygonSymbolizerPresenter.MyView {

	/** private members for polygon symbolizer **/
	private VLayout polygonSymbolizerPane;

	private DynamicForm polygonSymbolizerForm;

	private PolygonSymbolizerInfo currentPolygonSymbolizerInfo;

	private CheckboxItem polygonFillCheckBoxItem;

	private ColorPickerItem polygonFillColorPicker;

	private SpinnerItem polygonFillOpacityItem;

	private CheckboxItem polygonBorderCheckBoxItem;

	private ColorPickerItem polygonStrokeColorPicker;

	private SpinnerItem polygonStrokeWidthItem;

	private SpinnerItem polygonStrokeOpacityItem;

	protected FillInfo prevFillInfo;

	private final ViewUtil viewUtil;

	private final EventBus eventBus;

	private final SldEditorMessages sldEditorMessages;

	@Inject
	public PolygonSymbolizerView(final EventBus eventBus, final ViewUtil viewUtil,
			final SldEditorMessages sldEditorMessages) {
		this.eventBus = eventBus;
		this.viewUtil = viewUtil;
		this.sldEditorMessages = sldEditorMessages;
		setupPolygonSymbolizerForm();
		hide();
	}

	public void modelToView(PolygonSymbolizerInfo polygonSymbolizerInfo) {
		currentPolygonSymbolizerInfo = polygonSymbolizerInfo;

		polygonSymbolizerForm.clearValues();

		if (null == currentPolygonSymbolizerInfo.getFill()) { /* no filling */
			// OGC 02-070: If the Fill element is omitted from its parent element, then no fill will be rendered.
			polygonFillCheckBoxItem.setValue(false);
			polygonFillColorPicker.setDisabled(true);
			polygonFillOpacityItem.setDisabled(true);
			prevFillInfo = new FillInfo(); /* default fill */
		} else {
			polygonFillCheckBoxItem.setValue(true);
			polygonFillColorPicker.setDisabled(false);
			polygonFillOpacityItem.setDisabled(false);
			prevFillInfo = currentPolygonSymbolizerInfo.getFill();
		}

		if (null != currentPolygonSymbolizerInfo.getFill()) {

			List<CssParameterInfo> cssParameterList = currentPolygonSymbolizerInfo.getFill().getCssParameterList();

			if (null != cssParameterList) {

				for (CssParameterInfo cssParameterInfo : cssParameterList) {
					if (cssParameterInfo.getName().equals(SldConstant.FILL)) {
						polygonFillColorPicker.setValue(cssParameterInfo.getValue());
					} else if (cssParameterInfo.getName().equals(SldConstant.FILL_OPACITY)) {
						polygonFillOpacityItem.setValue(viewUtil.factorToPercentage(cssParameterInfo.getValue()));
					}
				}
			}
		}

		/**
		 * OGC 02-070: A missing Stroke sub-element for a polygon symbolizer means that the geometry will not be stroked
		 * (ANB: so no separate border)
		 **/
		if (null != currentPolygonSymbolizerInfo.getStroke()) {
			polygonBorderCheckBoxItem.setValue(true);

			polygonSymbolizerForm.showItem("borderColor");
			polygonSymbolizerForm.showItem("borderOpacity");
			polygonSymbolizerForm.showItem("borderWidth");

			List<CssParameterInfo> cssParameterList = currentPolygonSymbolizerInfo.getStroke().getCssParameterList();

			if (null != cssParameterList) {
				for (CssParameterInfo cssParameterInfo : cssParameterList) {
					if (cssParameterInfo.getName().equals(SldConstant.STROKE)) {
						polygonStrokeColorPicker.setValue(cssParameterInfo.getValue());
					} else if (cssParameterInfo.getName().equals(SldConstant.STROKE_WIDTH)) {
						polygonStrokeWidthItem.setValue(viewUtil.numericalToInteger(cssParameterInfo.getValue()));
					} else if (cssParameterInfo.getName().equals(SldConstant.STROKE_OPACITY)) {
						polygonStrokeOpacityItem.setValue(viewUtil.factorToPercentage(cssParameterInfo.getValue()));
					} else if ("stroke-dasharray".equals(cssParameterInfo.getName())) {
						// TODO
					}
				}
			}
		} else {
			polygonBorderCheckBoxItem.setValue(false);
			polygonSymbolizerForm.hideItem("borderColor");
			polygonSymbolizerForm.hideItem("borderOpacity");
			polygonSymbolizerForm.hideItem("borderWidth");
		}
		polygonSymbolizerForm.setVisible(true);
	}

	/**
	 * Construct and setup "polygon symbolizer" form.
	 */
	private void setupPolygonSymbolizerForm() {
		polygonSymbolizerPane = new VLayout();
		polygonSymbolizerPane.setMembersMargin(5);
		polygonSymbolizerPane.setMargin(5);

		polygonSymbolizerForm = new DynamicForm();
		polygonFillCheckBoxItem = new CheckboxItem();
		polygonFillCheckBoxItem.setTitle(sldEditorMessages.enableFillInSymbologyTab());
		polygonFillCheckBoxItem.setDefaultValue(true);

		polygonFillCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				fireSldContentChanged();

				Boolean newValue = (Boolean) event.getValue();

				if (newValue == null) {
					newValue = true;
				}
				if (newValue) {
					/* restore prev info */
					currentPolygonSymbolizerInfo.setFill(prevFillInfo);
					polygonFillColorPicker.setDisabled(false);
					polygonFillOpacityItem.setDisabled(false);

				} else {

					polygonFillColorPicker.setDisabled(true);
					polygonFillOpacityItem.setDisabled(true);
					if (null != currentPolygonSymbolizerInfo.getFill()) {
						prevFillInfo = currentPolygonSymbolizerInfo.getFill();
					}
					currentPolygonSymbolizerInfo.setFill(null); /* No filling at the moment */
				}
			}
		});

		/** Construct and setup "polygon symbol fill color" form field **/

		polygonFillColorPicker = new ColorPickerItem();
		polygonFillColorPicker.setTitle(sldEditorMessages.fillColorInSymbologyTab());
		polygonFillColorPicker.setDefaultValue(SldConstant.DEFAULT_FILL_FOR_POLYGON);

		polygonFillColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstant.DEFAULT_FILL_FOR_POLYGON;
				}
				if (null == currentPolygonSymbolizerInfo.getFill()) {
					currentPolygonSymbolizerInfo.setFill(new FillInfo());
				}
				currentPolygonSymbolizerInfo.getFill().setFillColor(newValue);

				fireSldContentChanged();
			}

		});

		/** Construct and setup "polygon symbol fill opacity" form field **/

		polygonFillOpacityItem = new SpinnerItem();
		polygonFillOpacityItem.setTitle(sldEditorMessages.opacityTitleInSymbologyTab());
		polygonFillOpacityItem.setTooltip(sldEditorMessages.opacityTooltipInSymbologyTab());
		polygonFillOpacityItem.setDefaultValue(SldConstant.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_POLYGON);
		polygonFillOpacityItem.setMin(0);
		polygonFillOpacityItem.setMax(100);
		polygonFillOpacityItem.setStep(10);
		polygonFillOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				float newValue = viewUtil.numericalToFloat(event.getValue(),
						SldConstant.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_POLYGON);

				if (null == currentPolygonSymbolizerInfo.getFill()) {
					currentPolygonSymbolizerInfo.setFill(new FillInfo());
				}
				currentPolygonSymbolizerInfo.getFill().setFillOpacity(newValue / 100f);
				fireSldContentChanged();
			}
		});

		IntegerRangeValidator rangeValidator = new IntegerRangeValidator();
		rangeValidator.setMin(0);
		rangeValidator.setMax(100);

		polygonFillOpacityItem.setValidators(rangeValidator);
		polygonFillOpacityItem.setValidateOnChange(true);

		/*
		 * Border (stroke) attributes for a polygon symbol
		 */
		polygonBorderCheckBoxItem = new CheckboxItem();
		polygonBorderCheckBoxItem.setTitle(sldEditorMessages.enableBorderInSymbologyTab());
		polygonBorderCheckBoxItem.setDefaultValue(true);

		polygonBorderCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				Boolean newValue = (Boolean) event.getValue();

				if (newValue == null) {
					newValue = false;
				}
				if (newValue) {
					polygonSymbolizerForm.showItem("borderColor");
					polygonSymbolizerForm.showItem("borderOpacity");
					polygonSymbolizerForm.showItem("borderWidth");

					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo()); /* default border */

				} else {
					polygonSymbolizerForm.hideItem("borderColor");
					polygonSymbolizerForm.hideItem("borderOpacity");
					polygonSymbolizerForm.hideItem("borderWidth");
					currentPolygonSymbolizerInfo.setStroke(null); /* No border */
				}
				fireSldContentChanged();
			}
		});

		polygonStrokeColorPicker = new ColorPickerItem();
		polygonStrokeColorPicker.setName("borderColor");
		polygonStrokeColorPicker.setTitle("Lijnkleur rand");
		polygonStrokeColorPicker.setDefaultValue(SldConstant.DEFAULT_FILL_FOR_LINE);

		polygonStrokeColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstant.DEFAULT_FILL_FOR_LINE;
				}
				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setStrokeColor(newValue);

				fireSldContentChanged();
			}
		});

		polygonStrokeWidthItem = new SpinnerItem();
		polygonStrokeWidthItem.setName("borderWidth");
		polygonStrokeWidthItem.setTitle(sldEditorMessages.borderWidthTitle());
		polygonStrokeWidthItem.setTooltip(sldEditorMessages.borderWidthTooltip());
		polygonStrokeWidthItem.setDefaultValue(SldConstant.DEFAULT_STROKE_WIDTH);
		polygonStrokeWidthItem.setMin(0);
		polygonStrokeWidthItem.setMax(100);
		polygonStrokeWidthItem.setStep(1);
		polygonStrokeWidthItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				float newValue = viewUtil.numericalToFloat(event.getValue(), (float) SldConstant.DEFAULT_STROKE_WIDTH);

				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setStrokeWidth(newValue);

				fireSldContentChanged();

			}
		});
		/** Stroke opacity **/
		polygonStrokeOpacityItem = new SpinnerItem();
		polygonStrokeOpacityItem.setName("borderOpacity");
		polygonStrokeOpacityItem.setTitle(sldEditorMessages.opacityTitleInSymbologyTab());
		polygonStrokeOpacityItem.setTooltip(sldEditorMessages.opacityTooltipInSymbologyTab());
		polygonStrokeOpacityItem.setDefaultValue(SldConstant.DEFAULT_STROKE_OPACITY_PERCENTAGE);
		polygonStrokeOpacityItem.setMin(0);
		polygonStrokeOpacityItem.setMax(100);
		polygonStrokeOpacityItem.setStep(10);
		polygonStrokeOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				float newValue = viewUtil.numericalToFloat(event.getValue(),
						SldConstant.DEFAULT_STROKE_OPACITY_PERCENTAGE);

				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setStrokeOpacity(newValue / 100f);

				fireSldContentChanged();
			}
		});

		IntegerRangeValidator rangeValidatorStrokeOpacity = new IntegerRangeValidator();
		rangeValidatorStrokeOpacity.setMin(0);
		rangeValidatorStrokeOpacity.setMax(100);

		polygonStrokeOpacityItem.setValidators(rangeValidatorStrokeOpacity);
		polygonStrokeOpacityItem.setValidateOnChange(true);

		polygonSymbolizerForm.hide();
		polygonSymbolizerForm.setItems(polygonFillCheckBoxItem, polygonFillColorPicker, polygonFillOpacityItem,
				polygonBorderCheckBoxItem, polygonStrokeColorPicker, polygonStrokeWidthItem, polygonStrokeOpacityItem);

		polygonSymbolizerForm.hideItem("borderColor");
		polygonSymbolizerForm.hideItem("borderOpacity");
		polygonSymbolizerForm.hideItem("borderWidth");
		polygonSymbolizerPane.addMember(polygonSymbolizerForm);
	}

	private void fireSldContentChanged() {
		SldContentChangedEvent.fire(this);
	}

	public Widget asWidget() {
		return polygonSymbolizerPane;
	}

	public HandlerRegistration addSldContentChangedHandler(SldContentChangedHandler handler) {
		return eventBus.addHandler(SldContentChangedEvent.getType(), handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public void hide() {
		polygonSymbolizerPane.hide();
	}

	public void show() {
		if (!polygonSymbolizerPane.isDrawn()) {
			System.out.println();
		}
		polygonSymbolizerPane.show();
	}

	public void clear() {
		polygonSymbolizerForm.clearValues();
	}

}
