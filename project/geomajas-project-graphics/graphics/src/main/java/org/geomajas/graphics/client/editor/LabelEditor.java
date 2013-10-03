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

import org.geomajas.graphics.client.object.ExternalLabel;
import org.geomajas.graphics.client.object.GText;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.Textable;
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
		return object.hasRole(Labeled.TYPE) || object.hasRole(Textable.TYPE);
	}

	@Override
	public void setObject(GraphicsObject object) {
		this.object = object;
		Textable textable = getTextable();
		if (textable != null) {
			labelBox.setText(textable.getLabel());
			fillColorBox.setText(textable.getFontColor());
			fontSize.setText(textable.getFontSize()  + "");
			fontFamily.setText(textable.getFontFamily());
		}
	}

	public void onOk() {
		Textable textable = getTextable();
		if (textable != null) {
			String beforeLabel = textable.getLabel();
			String beforeColor = textable.getFontColor();
			int beforeSize = textable.getFontSize();
			String beforeFont = textable.getFontFamily();
			service.execute(new LabelOperation(object, null, beforeLabel, beforeColor, beforeSize,
					beforeFont, labelBox.getText(), fillColorBox.getText(), Integer.parseInt(fontSize.getText()),
					fontFamily.getText()));
		}
	}
	
	private Textable getTextable() {
		Textable textable = null;
		if (object.hasRole(Labeled.TYPE)) {
			textable = object.getRole(Labeled.TYPE).getTextable();
		} else if (object.hasRole(Textable.TYPE)) {
			textable = object.getRole(Textable.TYPE);
		}
		return textable;
	}

	@Override
	public String getLabel() {
		return "Edit text";
	}

	@Override
	public boolean validate() {
		// only if renderable is labeled, there should always be text
		// This is the case for GText
		// TODO make more generic
		if (object instanceof GText && !(object instanceof ExternalLabel)) {
			return !(((GText) object).getRole(Textable.TYPE).getLabel().isEmpty());
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
