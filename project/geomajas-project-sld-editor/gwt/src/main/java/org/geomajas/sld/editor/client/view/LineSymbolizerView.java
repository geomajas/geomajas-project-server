package org.geomajas.sld.editor.client.view;

import java.util.List;

import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.SldConstant;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.inject.Inject;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;


public class LineSymbolizerView {
	
	/** private members for line symbolizer **/
	private DynamicForm lineSymbolizerForm;

	private boolean isLineSymbolizerFormFirstSetup = true;

	// lineSymbolizerForm form items
	private ColorPickerItem lineStrokeColorPicker;

	private SpinnerItem strokeWidthItem;

	private LineSymbolizerInfo currentLineSymbolizerInfo;

	private SpinnerItem strokeOpacityItem;

	protected FillInfo prevFillInfo;
	
	private final ViewUtil viewUtil;

	private final SldEditorMessages sldEditorMessages;

	@Inject
	public LineSymbolizerView(final ViewUtil viewUtil, final SldEditorMessages sldEditorMessages) {
		this.viewUtil = viewUtil;
		this.sldEditorMessages = sldEditorMessages;
		setupLineSymbolizerForm();
	}

	
	public void modelToView(LineSymbolizerInfo lineSymbolizerInfo) {
		currentLineSymbolizerInfo = lineSymbolizerInfo;
		if (isLineSymbolizerFormFirstSetup) {
			setupLineSymbolizerForm();
			isLineSymbolizerFormFirstSetup = false;
		} else {
			lineSymbolizerForm.hide();
			lineSymbolizerForm.clearValues();
		}

		if (null == lineSymbolizerInfo.getStroke()) {
			lineSymbolizerInfo.setStroke(new StrokeInfo());
		}

		List<CssParameterInfo> cssParameterList = lineSymbolizerInfo.getStroke().getCssParameterList();

		if (null != cssParameterList) {
			for (CssParameterInfo cssParameterInfo : cssParameterList) {
				if (SldConstant.STROKE.equals(cssParameterInfo.getName())) {
					lineStrokeColorPicker.setValue(cssParameterInfo.getValue());
				} else if (SldConstant.STROKE_WIDTH.equals(cssParameterInfo.getName())) {
					strokeWidthItem.setValue(Float.parseFloat(cssParameterInfo.getValue()));
				} else if (SldConstant.STROKE_OPACITY.equals(cssParameterInfo.getName())) {
					strokeOpacityItem.setValue(viewUtil.factorToPercentage(cssParameterInfo.getValue()));
				} else if ("stroke-dasharray".equals(cssParameterInfo.getName())) {
					// TODO
				}
			}
		}

	}

	private void setupLineSymbolizerForm() {
		lineStrokeColorPicker = new ColorPickerItem();
		lineStrokeColorPicker.setTitle(sldEditorMessages.strokeColorTitle());
		lineStrokeColorPicker.setDefaultValue(SldConstant.DEFAULT_FILL_FOR_LINE);

		lineStrokeColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstant.DEFAULT_FILL_FOR_LINE;
				}

				currentLineSymbolizerInfo.getStroke().setStrokeColor(newValue);

				// Debugging: updateStyleDesc();
			}
		});

		strokeWidthItem = new SpinnerItem();
		strokeWidthItem.setTitle(sldEditorMessages.borderWidthTitle());
		strokeWidthItem.setTooltip(sldEditorMessages.borderWidthTooltip());
		strokeWidthItem.setDefaultValue(SldConstant.DEFAULT_STROKE_WIDTH_FOR_LINE);
		strokeWidthItem.setMin(0);
		strokeWidthItem.setMax(100);
		strokeWidthItem.setStep(1.0f);
		strokeWidthItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				Integer newValue = (Integer) event.getValue();

				if (newValue == null) {
					newValue = SldConstant.DEFAULT_STROKE_WIDTH_FOR_LINE;
				}

				currentLineSymbolizerInfo.getStroke().setStrokeWidth(newValue/100f);

				// Debugging: updateStyleDesc();

			}
		});
		IntegerRangeValidator rangeValidatorWidth = new IntegerRangeValidator();
		rangeValidatorWidth.setMin(0);
		rangeValidatorWidth.setMax(100);
		rangeValidatorWidth.setErrorMessage(sldEditorMessages.borderWidthTooltip());

		strokeWidthItem.setValidators(rangeValidatorWidth);
		strokeWidthItem.setValidateOnChange(true);

		/** Stroke opacity **/
		strokeOpacityItem = new SpinnerItem();
		strokeOpacityItem.setTitle(sldEditorMessages.opacityTitleInSymbologyTab());

		strokeOpacityItem.setDefaultValue(SldConstant.DEFAULT_STROKE_OPACITY_PERCENTAGE);
		strokeOpacityItem.setMin(0);
		strokeOpacityItem.setMax(100);
		strokeOpacityItem.setStep(10.0f);
		strokeOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				setSldHasChangedTrue();

				Integer newValue = (Integer) event.getValue();

				if (null == newValue) {
					newValue = SldConstant.DEFAULT_STROKE_OPACITY_PERCENTAGE;
				}

				currentLineSymbolizerInfo.getStroke().setStrokeOpacity(newValue/100f);
				// Debugging: updateStyleDesc();

			}

		});
		IntegerRangeValidator rangeValidator = new IntegerRangeValidator();
		rangeValidator.setMin(0);
		rangeValidator.setMax(100);

		strokeOpacityItem.setValidators(rangeValidator);
		strokeOpacityItem.setValidateOnChange(true);
		strokeOpacityItem.setTooltip(sldEditorMessages.opacityTooltipInSymbologyTab());
		lineSymbolizerForm.hide();
		lineSymbolizerForm.setItems(lineStrokeColorPicker, strokeWidthItem, strokeOpacityItem);
	}

	private void setSldHasChangedTrue() {
		// TODO Auto-generated method stub
		
	}

}
