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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Simple implementation of the {@link TitledPanel} interface:<br/>
 * The heading is displayed using an H2 element, and the content is displayed within a {@link ScrollPanel}.
 * 
 * @author Jan De Moerloose
 */
public class TextAreaWidget implements IsWidget {

	/**
	 * UI binder interface this widget.
	 * 
	 */
	interface MyUiBinder extends UiBinder<VerticalPanel, TextAreaWidget> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	@UiField
	protected HeadingElement titleElement;

	@UiField
	protected ScrollPanel scrollPanel;

	@UiField
	protected FlowPanel contentPanel;
	
	protected VerticalPanel widget;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------
	/**
	 * Create a new instance.
	 */
	public TextAreaWidget() {
		widget = UIBINDER.createAndBindUi(this);
	}

	// ------------------------------------------------------------------------
	// TitledPanel implementation:
	// ------------------------------------------------------------------------

	public void setHeading(String heading) {
		titleElement.setInnerText(heading);
	}

	public void setScreenBounds(Bbox screenBounds) {
		widget.setPixelSize((int) screenBounds.getWidth() - 30, (int) screenBounds.getHeight() - 30);
		widget.getElement().getStyle().setLeft(screenBounds.getX(), Unit.PX);
		widget.getElement().getStyle().setTop(screenBounds.getY(), Unit.PX);
		contentPanel.setPixelSize((int) screenBounds.getWidth() - 30, (int) screenBounds.getHeight() - 30);
		
	}

	public void setText(String content) {
		contentPanel.getElement().setInnerText(content);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}