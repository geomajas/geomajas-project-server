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

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.TemplateLabeled;
import org.geomajas.graphics.client.operation.LabelOperation;
import org.geomajas.graphics.client.service.GraphicsService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mogaleaf.client.common.widgets.ColorHandler;
import com.mogaleaf.client.common.widgets.SimpleColorPicker;

/**
 * {@link Editor} for the {@link Labeled} role.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LabelEditor implements Editor {

	private static final Binder UIBINDER = GWT.create(Binder.class);

	/**
	 * UI binder.
	 * 
	 */
	interface Binder extends UiBinder<HTMLPanel, LabelEditor> {

	}

	protected GraphicsService service;

	private HTMLPanel widget;

	@UiField
	protected TextBox labelBox;
	
	@UiField
	protected Button fillColorButton;

	@UiField
	protected TextBox fillColorBox;
	
	@UiField
	protected TextBox fontSize;
	
	@UiField
	protected TextBox fontFamily;

	protected GraphicsObject object;
	
	private String iconUrl;
	
	private SimpleColorPicker colorPicker;

	public LabelEditor() {
		widget = UIBINDER.createAndBindUi(this);
		widget.setStyleName("popupWindow", true);
	}

	public void setService(GraphicsService service) {
		this.service = service;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public boolean supports(GraphicsObject object) {
		return object.hasRole(Labeled.TYPE) && !(object.getRole(Labeled.TYPE) instanceof TemplateLabeled);
	}

	@Override
	public void setObject(GraphicsObject object) {
		this.object = object;
		Labeled label = object.getRole(Labeled.TYPE);
		labelBox.setText(label.getLabel());
		fillColorBox.setText(label.getFontColor());
		fontSize.setText(label.getFontSize()  + "");
		fontFamily.setText(label.getFontFamily());
	}

	public void onOk() {
		String beforeLabel = object.getRole(Labeled.TYPE).getLabel();
		String beforeColor = object.getRole(Labeled.TYPE).getFontColor();
		int beforeSize = object.getRole(Labeled.TYPE).getFontSize();
		String beforeFont = object.getRole(Labeled.TYPE).getFontFamily();
		service.execute(new LabelOperation(object, null, beforeLabel, beforeColor, beforeSize,
				beforeFont, labelBox.getText(), fillColorBox.getText(), Integer.parseInt(fontSize.getText()),
				fontFamily.getText()));
	}

	@Override
	public String getLabel() {
		return "Edit text";
	}

	@Override
	public boolean validate() {
		if (labelBox.getText().isEmpty()) {
			return false;
		}
		return true;
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

}
