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

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.anchor.Anchored;
import org.geomajas.graphics.client.object.anchor.ResizableAnchorer;
import org.geomajas.graphics.client.operation.AnchorOperation;
import org.geomajas.graphics.client.operation.AnchorStyleOperation;
import org.geomajas.graphics.client.service.GraphicsService;
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
 * {@link Editor} for the {@link ResizableAnchorer} role.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AnchorStyleEditor implements Editor {

	private static final Binder UIBINDER = GWT.create(Binder.class);

	/**
	 * UI binder.
	 * 
	 */
	interface Binder extends UiBinder<HTMLPanel, AnchorStyleEditor> {

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
	protected TextBox pointPositionX;
	
	@UiField
	protected TextBox pointPositionY;
	
	@UiField
	protected Label pointLabel;

	@UiField
	protected Button pointColorButton;

	@UiField
	protected TextBox pointColorBox;

	@UiField
	protected TransparencySliderBar pointOpacitySlider;

	private GraphicsObject object;

	private String iconUrl;

	private SimpleColorPicker colorPicker;
	
	public void setService(GraphicsService service) {
		this.service = service;
	}

	public AnchorStyleEditor() {
		widget = UIBINDER.createAndBindUi(this);
		widget.setStyleName("anchorPointPopup");
		widget.setStyleName("popupWindow", true);
		pointPositionX.setStyleName("anchorPopupPositionTextBox");
		pointPositionY.setStyleName("anchorPopupPositionTextBox");
//		strokeWidthBox.setStyleName("textBoxCell",true);
//		pointColorBox.setStyleName("textBoxCell",true);
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
	 int left = widget.getAbsoluteLeft() + widget.getOffsetWidth();
	 int top = widget.getAbsoluteTop();
	
	 colorPicker.setPopupPosition(left, top);
	 colorPicker.show();
	 }

	@UiHandler("pointColorButton")
	public void showpointColorChoice(ClickEvent e) {
		colorPicker = new SimpleColorPicker();
		colorPicker.addListner(new ColorHandler() {

			@Override
			public void newColorSelected(String color) {
				pointColorBox.setText(color);
			}
		});
		int left = widget.getAbsoluteLeft() + widget.getOffsetWidth();
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
		return object.hasRole(ResizableAnchorer.TYPE) ;
	}

	@Override
	public void setObject(GraphicsObject object) {
		this.object = object;
		//line
		strokeWidthBox.setText(object.getRole(ResizableAnchorer.TYPE).getAnchorLineWidth() + "");
		strokeColorBox.setText(object.getRole(ResizableAnchorer.TYPE).getAnchorLineColor());
		strokeOpacitySlider.setCurrentValue(
				1 - object.getRole(ResizableAnchorer.TYPE).getAnchorLineOpacity());
		strokeLabel.setText("Anchor Line Parameters");

		//point style
		pointColorBox.setText(object.getRole(ResizableAnchorer.TYPE).getAnchorPointColor());
		pointOpacitySlider.setCurrentValue(
				1 - object.getRole(ResizableAnchorer.TYPE).getAnchorPointOpacity());
		pointLabel.setText("Anchor Point Parameters");
	
		//point coordinates
		pointPositionX.setText(object.getRole(Anchored.TYPE).getAnchorPosition().getX() + "");
		pointPositionY.setText(object.getRole(Anchored.TYPE).getAnchorPosition().getY() + "");
		
	}

	@Override
	public void onOk() {
		int beforeStrokeWidth = object.getRole(ResizableAnchorer.TYPE).getAnchorLineWidth();
		String beforeStrokeColor = object.getRole(ResizableAnchorer.TYPE).getAnchorLineColor();
		double beforeStrokeOpacity = object.getRole(ResizableAnchorer.TYPE).getAnchorLineOpacity();
		String beforePointColor = object.getRole(ResizableAnchorer.TYPE).getAnchorPointColor();
		double beforePointOpacity = object.getRole(ResizableAnchorer.TYPE).getAnchorPointOpacity();
		service.execute(new AnchorStyleOperation(object, beforeStrokeWidth,
				beforeStrokeColor, beforeStrokeOpacity, beforePointColor, beforePointOpacity, Integer
						.parseInt(strokeWidthBox.getText()), strokeColorBox.getText(), 1 - strokeOpacitySlider
						.getCurrentValue(), pointColorBox.getText(), 1 - pointOpacitySlider
						.getCurrentValue()));

		//location
		Coordinate beforePosition  = object.getRole(Anchored.TYPE).getAnchorPosition();
		service.execute(new AnchorOperation(object, beforePosition, new Coordinate(Double
				.parseDouble(pointPositionX.getText()), Double.parseDouble(pointPositionY.getText()))));

	}

	@Override
	public String getLabel() {
		return "Edit Anchor Style";
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
}
