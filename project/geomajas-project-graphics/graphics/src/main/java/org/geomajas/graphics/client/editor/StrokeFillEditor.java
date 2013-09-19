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
package org.geomajas.graphics.client.editor;

import org.geomajas.graphics.client.object.GPath;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.operation.FillOperation;
import org.geomajas.graphics.client.operation.StrokeOperation;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.util.Interruptible;
import org.geomajas.graphics.client.util.textbox.TextBoxEditorDecorator;
import org.geomajas.graphics.client.widget.TransparencySliderBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mogaleaf.client.common.widgets.ColorHandler;
import com.mogaleaf.client.common.widgets.SimpleColorPicker;

/**
 * {@link Editor} for the {@link Strokable} and {@link Fillable} roles. Supports both roles in a single panel.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StrokeFillEditor implements Editor, Interruptible {

	private static final Binder UIBINDER = GWT.create(Binder.class);

	/**
	 * UI binder.
	 * 
	 */
	interface Binder extends UiBinder<HTMLPanel, StrokeFillEditor> {

	}

	private GraphicsService service;

	private HTMLPanel widget;

	@UiField
	protected Label strokeLabel;

	@UiField
	protected TextBox strokeWidthBox;

	@UiField
	protected TextBoxEditorDecorator<String> strokeWidthBoxDecorator;

	@UiField
	protected Button strokeColorButton;

	@UiField
	protected TextBox strokeColorBox;

	@UiField
	protected TransparencySliderBar strokeOpacitySlider;

	@UiField
	protected Label fillLabel;

	@UiField
	protected Button fillColorButton;

	@UiField
	protected TextBox fillColorBox;

	@UiField
	protected TransparencySliderBar fillOpacitySlider;

	private GraphicsObject object;

	private String noFillStyle = "noFillParameters";

	private String noStrokeStyle = "noStrokeParameters";

	private String iconUrl;

	private SimpleColorPicker colorPicker;
	
	private boolean inProgress;
	
	public void setService(GraphicsService service) {
		this.service = service;
	}

	public StrokeFillEditor() {
		widget = UIBINDER.createAndBindUi(this);
		widget.setStyleName("strokeFillPopup");
		widget.setStyleName("popupWindow", true);
	}

	 @UiHandler("strokeColorButton")
	 public void showStrokeColorChoice(ClickEvent e) {
	 colorPicker = new SimpleColorPicker();
	 colorPicker.addListner(new ColorHandler() {
		 @Override
		 public void newColorSelected(String color) {
		 strokeColorBox.setText(color);
		 }
	 });
	 int left = widget.getAbsoluteLeft() + widget.getOffsetWidth() + 10;
	 int top = widget.getAbsoluteTop();
	
	 colorPicker.setPopupPosition(left, top);
	 colorPicker.show();
	 }

	 @UiHandler("fillColorButton")
	 public void showFillColorChoice(ClickEvent e) {
		colorPicker = new SimpleColorPicker();
		colorPicker.addListner(new ColorHandler() {

			@Override
			public void newColorSelected(String color) {
				fillColorBox.setText(color);
			}
		});
		int left = widget.getAbsoluteLeft() + widget.getOffsetWidth() + 10;
		int top = widget.getAbsoluteTop() + widget.getOffsetHeight() / 2;
		colorPicker.setPopupPosition(left, top);
		colorPicker.show();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public boolean supports(GraphicsObject object) {
		return object.hasRole(Fillable.TYPE) || object.hasRole(Strokable.TYPE) ;
	}

	@Override
	public void setObject(GraphicsObject object) {
		this.object = object;
		if (object.hasRole(Strokable.TYPE)) {
			strokeWidthBox.setText(object.getRole(Strokable.TYPE).getStrokeWidth() + "");
			strokeColorBox.setText(object.getRole(Strokable.TYPE).getStrokeColor());
			strokeOpacitySlider.setCurrentValue(
					1 - object.getRole(Strokable.TYPE).getStrokeOpacity());
			if (object instanceof GPath) {
				strokeLabel.setText("Line Parameters");
			} else {
				strokeLabel.setText("Border Parameters");
			}
			widget.removeStyleDependentName(noStrokeStyle);
		} else {
			widget.addStyleDependentName(noStrokeStyle);
		}
		if (object.hasRole(Fillable.TYPE)) {
			fillColorBox.setText(object.getRole(Fillable.TYPE).getFillColor());
			fillOpacitySlider.setCurrentValue(
					1 - object.getRole(Fillable.TYPE).getFillOpacity());
			fillLabel.setText("Area Parameters");
			widget.removeStyleDependentName(noFillStyle);
		} else {
			widget.addStyleDependentName(noFillStyle);
		}
	}

	@Override
	public void onOk() {
		if (object.hasRole(Strokable.TYPE)) {
			int beforeStrokeWidth = object.getRole(Strokable.TYPE).getStrokeWidth();
			String beforeStrokeColor = object.getRole(Strokable.TYPE).getStrokeColor();
			double beforeStrokeOpacity = object.getRole(Strokable.TYPE).getStrokeOpacity();
			service.execute(new StrokeOperation( object, beforeStrokeColor,
					beforeStrokeOpacity, beforeStrokeWidth, strokeColorBox.getText(), 1 - strokeOpacitySlider
							.getCurrentValue(), Integer.parseInt(strokeWidthBox.getText())));
		}

		if (object.hasRole(Fillable.TYPE)) {
			String beforeFillColor = object.getRole(Fillable.TYPE).getFillColor();
			double beforeFillOpacity = object.getRole(Fillable.TYPE).getFillOpacity();
			service.execute(new FillOperation(object, beforeFillColor, beforeFillOpacity,
					fillColorBox.getText(), 1 - fillOpacitySlider.getCurrentValue()));
		}
		inProgress = false;
	}

	@Override
	public String getLabel() {
		return "Edit style";
	}

	@Override
	public boolean validate() {
		try {
			Integer.parseInt(strokeWidthBox.getText());
			return true;
		} catch (Exception e) {
			strokeWidthBoxDecorator.showError("Stroke width must be an integer.");
			return false;
		}
	}

	@Override
	public void undo() {
		service.undo();
	}

	@Override
	public void setIconUrl(String url) {
		this.iconUrl = url;
	}

	@Override
	public String getIconUrl() {
		return iconUrl;
	}

	@Override
	public void cancel() {
		inProgress = false;
	}

	@Override
	public void stop() {
		inProgress = false;
	}

	@Override
	public void save() {
		onOk();	
	}

	@Override
	public void pause() {
		// not used yet
	}

	@Override
	public void resume() {
		// not used yet
	}

	@Override
	public boolean isInterrupted() {
		// not used yet
		return false;
	}

	@Override
	public boolean isInProgress() {
		return inProgress;
	}

	@Override
	public void start() {
		inProgress = true;
	}
}
