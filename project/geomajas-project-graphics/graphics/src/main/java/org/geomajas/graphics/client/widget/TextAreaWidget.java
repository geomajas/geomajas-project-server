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
package org.geomajas.graphics.client.widget;

import org.geomajas.geometry.Bbox;
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.object.role.Textable;
import org.geomajas.graphics.client.util.GraphicsUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Simple implementation of an HTM text area.
 * 
 * @author Jan De Moerloose
 */
public class TextAreaWidget implements IsWidget, Fillable, Strokable, Textable {

	/**
	 * UI binder interface this widget.
	 * 
	 */
	interface MyUiBinder extends UiBinder<HTMLPanel, TextAreaWidget> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	protected HTMLPanel widget;

	private String fillColor = "#FFFFFF";

	private double fillOpacity = 1.0;

	private String strokeColor = "#0000FF";

	private int strokeWidth = 8;

	private double strokeOpacity = 0.7;

	private Bbox screenBounds = new Bbox(0, 0, 100, 100);

	private String fontFamily = "";

	private int fontSize = 14;

	private String fontColor = "#000000";

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------
	/**
	 * Create a new instance.
	 */
	public TextAreaWidget() {
		widget = UIBINDER.createAndBindUi(this);
	}

	public void setScreenBounds(Bbox screenBounds) {
		this.screenBounds = GraphicsUtil.clone(screenBounds);
		updateStyle();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
		updateStyle();
	}

	@Override
	public void setFillOpacity(double fillOpacity) {
		this.fillOpacity = fillOpacity;
		updateStyle();
	}

	@Override
	public String getFillColor() {
		return fillColor;
	}

	@Override
	public double getFillOpacity() {
		return fillOpacity;
	}

	public void updateStyle() {
		widget.getElement().getStyle().setWidth((int) screenBounds.getWidth() - 4 * strokeWidth, Unit.PX);
		widget.getElement().getStyle().setLeft(screenBounds.getX() + strokeWidth, Unit.PX);
		widget.getElement().getStyle().setTop(screenBounds.getY() + strokeWidth, Unit.PX);

		widget.getElement()
				.getStyle()
				.setProperty("boxShadow",
						"0px 0px 0px " + strokeWidth + "px " + getRgbaColor(strokeColor, strokeOpacity));
		widget.getElement().getStyle().setPadding(strokeWidth, Unit.PX);
		widget.getElement().getStyle().setBackgroundColor(getRgbaColor(fillColor, fillOpacity));
		widget.getElement().getStyle().setFontSize(fontSize, Unit.PX);
		widget.getElement().getStyle().setProperty("fontFamily", fontFamily);
		widget.getElement().getStyle().setColor(fontColor);

	}

	private String getRgbaColor(String hexColor, double opacity) {
		if (hexColor.startsWith("#")) {
			Integer intval = Integer.decode("0x" + hexColor.substring(1));
			int color = intval.intValue();
			int b = (color) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int r = (color >> 16) & 0xFF;
			return "rgba(" + r + "," + g + "," + b + "," + opacity + ")";
		}
		return hexColor;
	}

	@Override
	public String getStrokeColor() {
		return strokeColor;
	}

	@Override
	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
		updateStyle();
	}

	@Override
	public int getStrokeWidth() {
		return strokeWidth;
	}

	@Override
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
		updateStyle();
	}

	@Override
	public double getStrokeOpacity() {
		return strokeOpacity;
	}

	@Override
	public void setStrokeOpacity(double strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
		updateStyle();
	}

	@Override
	public void setLabel(String content) {
		widget.getElement().setInnerText(content);
		updateStyle();
	}

	@Override
	public String getLabel() {
		return widget.getElement().getInnerText();
	}

	@Override
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
		updateStyle();
	}

	@Override
	public String getFontColor() {
		return fontColor;
	}

	@Override
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		updateStyle();
	}

	@Override
	public int getFontSize() {
		return fontSize;
	}

	@Override
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
		updateStyle();
	}

	@Override
	public String getFontFamily() {
		return fontFamily;
	}

}