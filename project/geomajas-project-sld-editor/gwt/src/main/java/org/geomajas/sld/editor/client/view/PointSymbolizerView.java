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

import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.ExternalGraphicInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.FormatInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.MarkInfo;
import org.geomajas.sld.OnlineResourceInfo;
import org.geomajas.sld.RotationInfo;
import org.geomajas.sld.SizeInfo;
import org.geomajas.sld.SldConstant;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.WellKnownNameInfo;
import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.common.client.presenter.PointSymbolizerPresenter;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent.SldContentChangedHandler;
import org.geomajas.sld.editor.common.client.view.ViewUtil;
import org.geomajas.sld.xlink.SimpleLinkInfo.HrefInfo;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Default implementation of {@link PointSymbolizerPresenter.MyView}.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 *
 */
public class PointSymbolizerView extends ViewImpl implements PointSymbolizerPresenter.MyView {

	private DynamicForm typeOfGraphicForm;

	// SpecificFormPoint form item
	private SelectItem typeOfGraphicItem;

	private FillInfo prevFillInfo;

	/** private member for point layer **/
	private GraphicInfo currentGraphicInfo;

	/** private members for point symbolizer - type=marker **/
	private DynamicForm markerSymbolizerForm;

	private MarkInfo currentMark;

	// markerSymbolizerForm form items
	private SelectItem markerSymbolName;

	private SpinnerItem markerSizeItem;

	private SpinnerItem markerRotationItem;

	private CheckboxItem markerFillCheckBoxItem;

	private ColorPickerItem markerFillColorPicker;

	private SpinnerItem markerFillOpacityItem;

	private CheckboxItem markerBorderCheckBoxItem;

	private ColorPickerItem markerStrokeColorPicker;

	private SpinnerItem markerStrokeWidthItem;

	private SpinnerItem markerStrokeOpacityItem;

	/** private members for point symbolizer - type=external graphic **/
	private DynamicForm externalGraphicForm;

	// externalGraphicForm form items
	private SpinnerItem externalGraphicSizeItem;

	private SelectItem graphicFormatItem;

	private TextItem urlItem;

	private VLayout symbolPane;

	private final EventBus eventBus;

	private final ViewUtil viewUtil;

	private final SldEditorMessages sldEditorMessages;

	/**
	 * Type of {@link GraphicInfo}.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	private enum GraphicType {
		MARKER_TYPE, EXTERNAL_GRAPHICS_TYPE
	}

	@Inject
	public PointSymbolizerView(final EventBus eventBus, final ViewUtil viewUtil,
			final SldEditorMessages sldEditorMessages) {
		this.eventBus = eventBus;
		this.viewUtil = viewUtil;
		this.sldEditorMessages = sldEditorMessages;
		symbolPane = new VLayout();
		symbolPane.setMembersMargin(5);
		symbolPane.setMargin(5);
		setupGraphicTypeForm();
		setupMarkerForm();
		setupExternalGraphic();
		symbolPane.addMember(typeOfGraphicForm);
		symbolPane.addMember(markerSymbolizerForm);
		symbolPane.addMember(externalGraphicForm);
		hide();
	}

	public void modelToView(GraphicInfo graphicInfo) {
		currentGraphicInfo = graphicInfo;
		if (graphicInfo.getChoiceList().size() == 1) {
			GraphicInfo.ChoiceInfo choice = graphicInfo.getChoiceList().get(0);
			if (choice.ifExternalGraphic()) {
				typeOfGraphicItem.setValue(GraphicType.EXTERNAL_GRAPHICS_TYPE.name());
				externalGraphicToView();
			} else {
				typeOfGraphicItem.setValue(GraphicType.MARKER_TYPE.name());
				markerToView();
			}
		}
	}

	private void setupGraphicTypeForm() {
		typeOfGraphicForm = new DynamicForm();
		typeOfGraphicItem = new SelectItem();
		typeOfGraphicItem.setTitle(sldEditorMessages.typeofGraphicsTitleInSymbologyTab());

		final LinkedHashMap<String, String> typeOfGraphicList = new LinkedHashMap<String, String>();
		typeOfGraphicList.put(GraphicType.MARKER_TYPE.name(), sldEditorMessages.markerSelectTitle());
		typeOfGraphicList.put(GraphicType.EXTERNAL_GRAPHICS_TYPE.name(),
				sldEditorMessages.externalGraphicsSelectTitle());
		typeOfGraphicItem.setValueMap(typeOfGraphicList);

		typeOfGraphicItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				GraphicType type = GraphicType.valueOf(typeOfGraphicItem.getValueAsString());
				// clear existing !!!
				currentGraphicInfo.setRotation(null);
				currentGraphicInfo.setSize(null);
				GraphicInfo.ChoiceInfo choiceInfoGraphic = currentGraphicInfo.getChoiceList().get(0);
				choiceInfoGraphic.clearChoiceListSelect();
				switch (type) {
					case EXTERNAL_GRAPHICS_TYPE:
						choiceInfoGraphic.setExternalGraphic(new ExternalGraphicInfo());
						choiceInfoGraphic.getExternalGraphic().setFormat(new FormatInfo());
						choiceInfoGraphic.getExternalGraphic().setOnlineResource(new OnlineResourceInfo());
						choiceInfoGraphic.getExternalGraphic().getOnlineResource().setHref(new HrefInfo());
						markerSymbolizerForm.hide();
						externalGraphicForm.show();
						break;
					case MARKER_TYPE:
						choiceInfoGraphic.setMark(new MarkInfo());
						currentMark = choiceInfoGraphic.getMark();
						markerSymbolizerForm.show();
						externalGraphicForm.hide();
						break;
				}
				modelToView(currentGraphicInfo);
				fireSldContentChanged();
			}
		});
		typeOfGraphicForm.setItems(typeOfGraphicItem);
	}

	private void markerToView() {
		externalGraphicForm.hide();
		markerSymbolizerForm.clearValues();

		markerSizeItem.setValue(SldConstant.DEFAULT_SIZE_MARKER); /* init with default */
		if (null != currentGraphicInfo.getSize()) {
			String size = currentGraphicInfo.getSize().getValue();
			if (null != size) {
				markerSizeItem.setValue(viewUtil.numericalToInteger(size));
			}
		}

		if (null != currentGraphicInfo.getRotation()) {
			String rotation = currentGraphicInfo.getRotation().getValue();
			if (null != rotation) {
				markerRotationItem.setValue(Double.parseDouble(rotation));
			}
		}

		org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic = currentGraphicInfo.getChoiceList().get(0);

		if (!choiceInfoGraphic.ifMark()) {
			/* create a default marker with default fill, stroke and wellKnownName */
			MarkInfo mark = new MarkInfo();
			mark.setFill(new FillInfo());
			mark.setStroke(new StrokeInfo());
			mark.setWellKnownName(new WellKnownNameInfo());
			mark.getWellKnownName().setWellKnownName(SldConstant.DEFAULT_WKNAME_FOR_MARKER); // TODO!!!

			mark.setFill(new FillInfo());
			mark.getFill().setFillColor(SldConstant.DEFAULT_FILL_FOR_MARKER);
			mark.getFill().setFillOpacity(SldConstant.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_MARKER / 100f);

			mark.setStroke(new StrokeInfo());
			mark.getStroke().setStrokeColor(SldConstant.DEFAULT_FILL_FOR_LINE);
			mark.getStroke().setStrokeWidth(SldConstant.DEFAULT_STROKE_WIDTH);

			choiceInfoGraphic.setMark(mark);
			if (null == currentGraphicInfo.getSize()) {
				currentGraphicInfo.setSize(new SizeInfo());
				currentGraphicInfo.getSize().setValue(Integer.toString(SldConstant.DEFAULT_SIZE_MARKER));
			}
		}
		currentMark = choiceInfoGraphic.getMark();

		if (null == currentMark.getFill()) { /* no filling */
			markerFillCheckBoxItem.setValue(false);
			markerFillColorPicker.setDisabled(true);
			markerFillOpacityItem.setDisabled(true);
			prevFillInfo = new FillInfo(); /* default fill */
		} else {
			markerFillCheckBoxItem.setValue(true);
			markerFillColorPicker.setDisabled(false);
			markerFillOpacityItem.setDisabled(false);
			prevFillInfo = currentMark.getFill();
		}

		WellKnownNameInfo nameInfo = choiceInfoGraphic.getMark().getWellKnownName();

		if (null != nameInfo) {
			markerSymbolName.setValue(nameInfo.getWellKnownName());
		}

		if (null != choiceInfoGraphic.getMark().getFill()) {

			List<CssParameterInfo> cssParameterList = choiceInfoGraphic.getMark().getFill().getCssParameterList();

			if (null != cssParameterList) {

				for (CssParameterInfo cssParameterInfo : cssParameterList) {
					if (cssParameterInfo.getName().equals(SldConstant.FILL)) {
						markerFillColorPicker.setValue(cssParameterInfo.getValue());
					} else if (cssParameterInfo.getName().equals(SldConstant.FILL_OPACITY)) {
						markerFillOpacityItem.setValue(viewUtil.factorToPercentage(cssParameterInfo.getValue()));
					}
				}
			}
		}

		/** A missing Stroke sub-element for a Marker means that the geometry will not be stroked **/
		if (null != choiceInfoGraphic.getMark().getStroke()) {
			markerBorderCheckBoxItem.setValue(true);

			markerSymbolizerForm.showItem("borderColor");
			markerSymbolizerForm.showItem("borderOpacity");
			markerSymbolizerForm.showItem("borderWidth");

			List<CssParameterInfo> cssParameterList = choiceInfoGraphic.getMark().getStroke().getCssParameterList();

			if (null != cssParameterList) {
				for (CssParameterInfo cssParameterInfo : cssParameterList) {
					if (cssParameterInfo.getName().equals(SldConstant.STROKE)) {
						markerStrokeColorPicker.setValue(cssParameterInfo.getValue());
					} else if (cssParameterInfo.getName().equals(SldConstant.STROKE_WIDTH)) {
						markerStrokeWidthItem.setValue(viewUtil.numericalToInteger(cssParameterInfo.getValue()));
					} else if (cssParameterInfo.getName().equals(SldConstant.STROKE_OPACITY)) {
						markerStrokeOpacityItem.setValue(viewUtil.factorToPercentage(cssParameterInfo.getValue()));
					} else if ("stroke-dasharray".equals(cssParameterInfo.getName())) {
						// TODO
					}
				}
			}
		} else {
			markerBorderCheckBoxItem.setValue(false);
			markerSymbolizerForm.hideItem("borderColor");
			markerSymbolizerForm.hideItem("borderOpacity");
			markerSymbolizerForm.hideItem("borderWidth");
		}
		markerSymbolizerForm.setVisible(true);
	}

	private void externalGraphicToView() {
		markerSymbolizerForm.hide();
		externalGraphicForm.clearValues();

		if (null != currentGraphicInfo.getSize()) {
			String size = currentGraphicInfo.getSize().getValue();
			externalGraphicSizeItem.setValue(size);
		}
		final org.geomajas.sld.GraphicInfo.ChoiceInfo choiceInfoGraphic = currentGraphicInfo.getChoiceList().get(0);

		if (null != choiceInfoGraphic.getExternalGraphic().getFormat()) {
			String formatValue = choiceInfoGraphic.getExternalGraphic().getFormat().getFormat();
			graphicFormatItem.setValue(formatValue);
		}

		if (null != choiceInfoGraphic.getExternalGraphic().getOnlineResource()) {
			String urlValue = choiceInfoGraphic.getExternalGraphic().getOnlineResource().getHref().getHref();
			urlItem.setValue(urlValue);
		}
		externalGraphicForm.setVisible(true);
	}

	private void setupMarkerForm() {
		markerSymbolizerForm = new DynamicForm();
		/** Construct and setup "marker symbol size" form field **/

		markerSizeItem = new SpinnerItem();
		markerSizeItem.setTitle(sldEditorMessages.sizeOfGraphicInSymbologyTab());
		markerSizeItem.setDefaultValue(SldConstant.DEFAULT_SIZE_MARKER);
		markerSizeItem.setMin(0);
		markerSizeItem.setMax(200);
		markerSizeItem.setStep(1);
		markerSizeItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				String newValue = viewUtil.numericalToString(event.getValue(),
						Integer.toString(SldConstant.DEFAULT_SIZE_MARKER));

				if (newValue != null) {
					fireSldContentChanged();

					if (null == currentGraphicInfo.getSize()) {
						currentGraphicInfo.setSize(new SizeInfo());
					}
					currentGraphicInfo.getSize().setValue(newValue);
					// Debugging: updateStyleDesc();
				}
			}

		});

		/** Construct and setup "marker symbol rotation" form field **/

		markerRotationItem = new SpinnerItem();
		markerRotationItem.setTitle(sldEditorMessages.rotationOfGraphicInSymbologyTab());
		markerRotationItem.setTooltip(sldEditorMessages.rotationOfGraphicTooltipInSymbologyTab());
		markerRotationItem.setDefaultValue(SldConstant.DEFAULT_ROTATION_MARKER);
		markerRotationItem.setMin(-360);
		markerRotationItem.setMax(360);
		markerRotationItem.setStep(15);
		markerRotationItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				// ((FormItem) event.getSource()).validate();
				fireSldContentChanged();

				String newValue = viewUtil.numericalToString(event.getValue(),
						SldConstant.DEFAULT_ROTATION_MARKER_AS_STRING);

				if (null == currentGraphicInfo.getRotation()) {
					currentGraphicInfo.setRotation(new RotationInfo());
				}
				currentGraphicInfo.getRotation().setValue(newValue);
				// Debugging: updateStyleDesc();
			}
		});

		/** Construct and setup "marker symbol name" form field **/

		markerSymbolName = new SelectItem();
		markerSymbolName.setTitle(sldEditorMessages.nameOfSymbolTitleInSymbologyTab());
		markerSymbolName.setDefaultValue(SldConstant.DEFAULT_WKNAME_FOR_MARKER);

		final LinkedHashMap<String, String> markerSymbolList = new LinkedHashMap<String, String>();
		// See http://docs.geoserver.org/stable/en/user/styling/sld-extensions/pointsymbols.html
		// The SLD specification mandates the support of the following symbols:
		markerSymbolList.put("cross", "cross (+)"); // TODO i18n
		markerSymbolList.put("circle", "circle"); // TODO
		markerSymbolList.put("square", "square"); // TODO
		markerSymbolList.put("star", "star"); // TODO
		markerSymbolList.put("triangle", sldEditorMessages.triangleTitle());
		markerSymbolList.put("X", "X");

		markerSymbolName.setValueMap(markerSymbolList);
		markerSymbolName.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				fireSldContentChanged();

				String selected = null;
				ListGridRecord record = markerSymbolName.getSelectedRecord();
				if (record == null) {
					selected = SldConstant.DEFAULT_WKNAME_FOR_MARKER;
				} else {
					selected = (String) event.getValue();
				}
				WellKnownNameInfo nameInfo = currentMark.getWellKnownName();

				if (null == nameInfo && record != null) {
					nameInfo = new WellKnownNameInfo();
					currentMark.setWellKnownName(nameInfo);
				}
				if (null != nameInfo) {
					currentMark.getWellKnownName().setWellKnownName(selected);
				}
			}
		});

		/** Construct and setup "marker FillCheckBoxItem" form field **/
		markerFillCheckBoxItem = new CheckboxItem();
		markerFillCheckBoxItem.setTitle(sldEditorMessages.enableFillInSymbologyTab());
		markerFillCheckBoxItem.setDefaultValue(true);

		markerFillCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				Boolean newValue = (Boolean) event.getValue();

				if (newValue == null) {
					newValue = true;
				}
				if (newValue) {
					/* restore prev info */
					currentMark.setFill(prevFillInfo);
					markerFillColorPicker.show();
					markerFillOpacityItem.show();

				} else {

					markerFillColorPicker.hide();
					markerFillOpacityItem.hide();
					if (null != currentMark.getFill()) {
						prevFillInfo = currentMark.getFill();
					}
					currentMark.setFill(null); /* No filling at the moment */
				}
				fireSldContentChanged();
			}
		});

		/** Construct and setup "marker symbol fill color" form field **/

		markerFillColorPicker = new ColorPickerItem();

		markerFillColorPicker.setTitle(sldEditorMessages.fillColorInSymbologyTab());
		markerFillColorPicker.setDefaultValue(SldConstant.DEFAULT_FILL_FOR_MARKER);

		markerFillColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstant.DEFAULT_FILL_FOR_MARKER;
				}
				if (null == currentMark.getFill()) {
					currentMark.setFill(new FillInfo());
				}
				currentMark.getFill().setFillColor(newValue);

				fireSldContentChanged();
			}
		});

		/** Construct and setup "marker symbol fill opacity" form field **/

		markerFillOpacityItem = new SpinnerItem();
		markerFillOpacityItem.setTitle(sldEditorMessages.opacityTitleInSymbologyTab());
		markerFillOpacityItem.setTooltip(sldEditorMessages.opacityTooltipInSymbologyTab());
		markerFillOpacityItem.setDefaultValue(SldConstant.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_MARKER);
		markerFillOpacityItem.setMin(0);
		markerFillOpacityItem.setMax(100);
		markerFillOpacityItem.setStep(10);
		markerFillOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				// ((DynamicForm) event.getSource()).validate();

				float newValue = viewUtil.numericalToFloat(event.getValue(),
						SldConstant.DEFAULT_FILL_OPACITY_PERCENTAGE_FOR_MARKER);

				if (null == currentMark.getFill()) {
					currentMark.setFill(new FillInfo());
				}
				currentMark.getFill().setFillOpacity(newValue / 100f);
				// Debugging: updateStyleDesc();
				fireSldContentChanged();
			}
		});

		IntegerRangeValidator rangeValidator = new IntegerRangeValidator();
		rangeValidator.setMin(0);
		rangeValidator.setMax(100);

		markerFillOpacityItem.setValidators(rangeValidator);
		markerFillOpacityItem.setValidateOnChange(true);

		/*
		 * Border (stroke) fields for a marker symbol
		 */
		markerBorderCheckBoxItem = new CheckboxItem();
		markerBorderCheckBoxItem.setTitle(sldEditorMessages.enableBorderInSymbologyTab());
		markerBorderCheckBoxItem.setDefaultValue(false);

		markerBorderCheckBoxItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				Boolean newValue = (Boolean) event.getValue();

				if (newValue == null) {
					newValue = false;
				}
				if (newValue) {
					markerSymbolizerForm.showItem("borderColor");
					markerSymbolizerForm.showItem("borderOpacity");
					markerSymbolizerForm.showItem("borderWidth");

					currentMark.setStroke(new StrokeInfo()); /* default border */

				} else {
					markerSymbolizerForm.hideItem("borderColor");
					markerSymbolizerForm.hideItem("borderOpacity");
					markerSymbolizerForm.hideItem("borderWidth");
					currentMark.setStroke(null); /* No border */
				}
				fireSldContentChanged();
			}
		});

		markerStrokeColorPicker = new ColorPickerItem();
		markerStrokeColorPicker.setName("borderColor");
		markerStrokeColorPicker.setTitle(sldEditorMessages.borderColor());
		markerStrokeColorPicker.setDefaultValue(SldConstant.DEFAULT_FILL_FOR_LINE);

		markerStrokeColorPicker.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				String newValue = (String) event.getValue();
				if (null == newValue) {
					newValue = SldConstant.DEFAULT_FILL_FOR_LINE;
				}
				if (null == currentMark.getStroke()) {
					currentMark.setStroke(new StrokeInfo());
				}

				currentMark.getStroke().setStrokeColor(newValue);

				fireSldContentChanged();
			}
		});

		markerStrokeWidthItem = new SpinnerItem();
		markerStrokeWidthItem.setName("borderWidth");
		markerStrokeWidthItem.setTitle(sldEditorMessages.borderWidthTitle());
		markerStrokeWidthItem.setTooltip(sldEditorMessages.borderWidthTooltip());
		markerStrokeWidthItem.setDefaultValue(SldConstant.DEFAULT_STROKE_WIDTH);
		markerStrokeWidthItem.setMin(0);
		markerStrokeWidthItem.setMax(100);
		markerStrokeWidthItem.setStep(1);
		markerStrokeWidthItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				float newValue = viewUtil.numericalToFloat(event.getValue(), SldConstant.DEFAULT_STROKE_WIDTH);

				if (null == currentMark.getStroke()) {
					currentMark.setStroke(new StrokeInfo());
				}

				currentMark.getStroke().setStrokeWidth(newValue);

				fireSldContentChanged();

			}
		});
		/** Stroke opacity **/
		markerStrokeOpacityItem = new SpinnerItem();
		markerStrokeOpacityItem.setName("borderOpacity");
		markerStrokeOpacityItem.setTitle(sldEditorMessages.opacityTitleInSymbologyTab());
		markerStrokeOpacityItem.setTooltip(sldEditorMessages.opacityTooltipInSymbologyTab());
		markerStrokeOpacityItem.setDefaultValue(SldConstant.DEFAULT_STROKE_OPACITY_PERCENTAGE);
		markerStrokeOpacityItem.setMin(0);
		markerStrokeOpacityItem.setMax(100);
		markerStrokeOpacityItem.setStep(10.0f);
		markerStrokeOpacityItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {

				float newValue = viewUtil.numericalToFloat(event.getValue(),
						SldConstant.DEFAULT_STROKE_OPACITY_PERCENTAGE);

				if (null == currentMark.getStroke()) {
					currentMark.setStroke(new StrokeInfo());
				}

				currentMark.getStroke().setStrokeOpacity(newValue / 100f);
				fireSldContentChanged();

			}
		});

		IntegerRangeValidator rangeValidatorStrokeOpacity = new IntegerRangeValidator();
		rangeValidatorStrokeOpacity.setMin(0);
		rangeValidatorStrokeOpacity.setMax(100);

		markerStrokeOpacityItem.setValidators(rangeValidatorStrokeOpacity);
		markerStrokeOpacityItem.setValidateOnChange(true);

		markerSymbolizerForm.hide();
		markerSymbolizerForm.setItems(markerSymbolName, markerSizeItem, markerRotationItem, markerFillCheckBoxItem,
				markerFillColorPicker, markerFillOpacityItem, markerBorderCheckBoxItem, markerStrokeColorPicker,
				markerStrokeWidthItem, markerStrokeOpacityItem);

		markerSymbolizerForm.hideItem("borderColor");
		markerSymbolizerForm.hideItem("borderOpacity");
		markerSymbolizerForm.hideItem("borderWidth");

	}

	private void setupExternalGraphic() {
		externalGraphicForm = new DynamicForm();
		externalGraphicSizeItem = new SpinnerItem();
		externalGraphicSizeItem.setTitle(sldEditorMessages.sizeOfGraphicInSymbologyTab());
		externalGraphicSizeItem.setDefaultValue(12);
		externalGraphicSizeItem.setMin(0);
		externalGraphicSizeItem.setMax(200);
		externalGraphicSizeItem.setStep(1);

		externalGraphicSizeItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				String newValue = viewUtil.numericalToString(event.getValue(), null);

				if (newValue != null) {

					if (null == currentGraphicInfo.getSize()) {
						currentGraphicInfo.setSize(new SizeInfo());
					}
					currentGraphicInfo.getSize().setValue(newValue);
					fireSldContentChanged();
				}
			}
		});

		graphicFormatItem = new SelectItem();
		graphicFormatItem.setTitle(sldEditorMessages.formatTitleInPointInSymbologyTab());

		final LinkedHashMap<String, String> graficFormatList = new LinkedHashMap<String, String>();
		graficFormatList.put("image/png", "image/png");
		graficFormatList.put("image/gif", "image/gif");
		graphicFormatItem.setValueMap(graficFormatList);
		graphicFormatItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				ListGridRecord record = graphicFormatItem.getSelectedRecord();

				if (record != null) {
					GraphicInfo.ChoiceInfo choiceInfoGraphic = currentGraphicInfo.getChoiceList().get(0);
					String selected = graphicFormatItem.getValueAsString();
					choiceInfoGraphic.getExternalGraphic().getFormat().setFormat(selected);
					// clear the old onlineResource (also in the SLD domain object)
					urlItem.setValue("");
					choiceInfoGraphic.getExternalGraphic().getOnlineResource().getHref().setHref(null);
					fireSldContentChanged();
				}
			}
		});

		/** **/
		urlItem = new TextItem("Href", sldEditorMessages.hRefTitle());
		urlItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				GraphicInfo.ChoiceInfo choiceInfoGraphic = currentGraphicInfo.getChoiceList().get(0);
				OnlineResourceInfo onlineResourceInfo = choiceInfoGraphic.getExternalGraphic().getOnlineResource();
				if (null == onlineResourceInfo) {
					onlineResourceInfo = new OnlineResourceInfo();
					HrefInfo href = new HrefInfo();
					onlineResourceInfo.setHref(href);
					choiceInfoGraphic.getExternalGraphic().setOnlineResource(onlineResourceInfo);
				}
				onlineResourceInfo.getHref().setHref(event.getValue().toString());
				fireSldContentChanged();
			}
		});

		externalGraphicForm.setItems(graphicFormatItem, urlItem, externalGraphicSizeItem);
		externalGraphicForm.hide();
	}

	private void fireSldContentChanged() {
		SldContentChangedEvent.fire(this);
	}

	public Widget asWidget() {
		return symbolPane;
	}

	public HandlerRegistration addSldContentChangedHandler(SldContentChangedHandler handler) {
		return eventBus.addHandler(SldContentChangedEvent.getType(), handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public void hide() {
		symbolPane.hide();
	}

	public void show() {
		if (!symbolPane.isDrawn()) {
			System.out.println();
		}
		symbolPane.show();
	}

	public void clear() {
		externalGraphicForm.clearValues();
		markerSymbolizerForm.clearValues();
	}

}
