package org.geomajas.sld.editor.client.view;

import java.util.List;

import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SldConstant;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.inject.Inject;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;

public class PolygonSymbolizerView {

	/** private members for polygon symbolizer **/
	private DynamicForm polygonSymbolizerForm;

	private boolean isPolygonSymbolizerFormFirstSetup = true;

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

	private final SldEditorMessages sldEditorMessages;

	@Inject
	public PolygonSymbolizerView(final ViewUtil viewUtil, final SldEditorMessages sldEditorMessages) {
		this.viewUtil = viewUtil;
		this.sldEditorMessages = sldEditorMessages;
		setupPolygonSymbolizerForm();
	}

	public void modelToView(PolygonSymbolizerInfo polygonSymbolizerInfo) {
		currentPolygonSymbolizerInfo = polygonSymbolizerInfo;

		if (isPolygonSymbolizerFormFirstSetup) {
			setupPolygonSymbolizerForm();
			isPolygonSymbolizerFormFirstSetup = false;
		} else {
			polygonSymbolizerForm.hide();
			polygonSymbolizerForm.clearValues();
		}

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
						polygonStrokeWidthItem.setValue(Float.parseFloat(cssParameterInfo.getValue()));
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

		polygonSymbolizerForm.show();
	}

	/**
	 * Construct and setup "polygon symbolizer" form.
	 */
	private void setupPolygonSymbolizerForm() {

		polygonFillCheckBoxItem = new CheckboxItem();
		polygonFillCheckBoxItem.setTitle(sldEditorMessages.enableFillInSymbologyTab());
		polygonFillCheckBoxItem.setDefaultValue(true);

		polygonFillCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

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

				setSldHasChangedTrue();
				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstant.DEFAULT_FILL_FOR_POLYGON;
				}
				if (null == currentPolygonSymbolizerInfo.getFill()) {
					currentPolygonSymbolizerInfo.setFill(new FillInfo());
				}
				currentPolygonSymbolizerInfo.getFill().setFillColor(newValue);

				// Debugging: updateStyleDesc();
			}

		});

		/** Construct and setup "polygon symbol fill opacity" form field **/

		polygonFillOpacityItem = new SpinnerItem();
		polygonFillOpacityItem.setTitle(sldEditorMessages.opacityTitleInSymbologyTab());
		polygonFillOpacityItem.setTooltip(sldEditorMessages.opacityTooltipInSymbologyTab());
		polygonFillOpacityItem.setDefaultValue(SldConstant.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_POLYGON);
		polygonFillOpacityItem.setMin(0);
		polygonFillOpacityItem.setMax(100);
		polygonFillOpacityItem.setStep(10.0f);
		polygonFillOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				float newValue = viewUtil.numericalToFloat(event.getValue(),
						SldConstant.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_POLYGON);

				if (null == currentPolygonSymbolizerInfo.getFill()) {
					currentPolygonSymbolizerInfo.setFill(new FillInfo());
				}
				currentPolygonSymbolizerInfo.getFill().setFillOpacity(newValue/100f);
				// Debugging: updateStyleDesc();
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
				setSldHasChangedTrue();

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
			}
		});

		polygonStrokeColorPicker = new ColorPickerItem();
		polygonStrokeColorPicker.setName("borderColor");
		polygonStrokeColorPicker.setTitle("Lijnkleur rand");
		polygonStrokeColorPicker.setDefaultValue(SldConstant.DEFAULT_FILL_FOR_LINE);

		polygonStrokeColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				setSldHasChangedTrue();
				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstant.DEFAULT_FILL_FOR_LINE;
				}
				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setStrokeColor(newValue);

				// Debugging: updateStyleDesc();
			}
		});

		polygonStrokeWidthItem = new SpinnerItem();
		polygonStrokeWidthItem.setName("borderWidth");
		polygonStrokeWidthItem.setTitle(sldEditorMessages.borderWidthTitle());
		polygonStrokeWidthItem.setTooltip(sldEditorMessages.borderWidthTooltip());
		polygonStrokeWidthItem.setDefaultValue(SldConstant.DEFAULT_STROKE_WIDTH);
		polygonStrokeWidthItem.setMin(0);
		polygonStrokeWidthItem.setMax(100);
		polygonStrokeWidthItem.setStep(1.0f);
		polygonStrokeWidthItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				float newValue = viewUtil.numericalToFloat(event.getValue(), (float) SldConstant.DEFAULT_STROKE_WIDTH);

				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setStrokeWidth(newValue);

				// Debugging: updateStyleDesc();

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
		polygonStrokeOpacityItem.setStep(10.0f);
		polygonStrokeOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				float newValue = viewUtil.numericalToFloat(event.getValue(), SldConstant.DEFAULT_STROKE_OPACITY_PERCENTAGE);

				if (null == currentPolygonSymbolizerInfo.getStroke()) {
					currentPolygonSymbolizerInfo.setStroke(new StrokeInfo());
				}

				currentPolygonSymbolizerInfo.getStroke().setStrokeOpacity(newValue/100f);

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

	}
	
	private void setSldHasChangedTrue() {
		// TODO Auto-generated method stub
		
	}


}
