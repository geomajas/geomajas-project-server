/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A view of a {@link ContentWidget}.
 * 
 * @author Pieter De Graef
 */
public class ContentPanelView extends Composite {

	/**
	 * UI binder for content widgets.
	 * 
	 * @author Pieter De Graef
	 */
	interface ContentWidgetViewUiBinder extends UiBinder<Widget, ContentPanelView> {
	}

	private static final ContentWidgetViewUiBinder UI_BINDER = GWT.create(ContentWidgetViewUiBinder.class);

	@UiField
	protected VerticalPanel layout;

	@UiField
	protected Element titleElement;

	@UiField
	protected Element descriptionElement;

	@UiField
	protected SimplePanel contentWidget;

	public ContentPanelView() {
		initWidget(UI_BINDER.createAndBindUi(this));
		setSize("100%", "100%");
		getElement().getStyle().setPadding(10, Unit.PX);
		layout.setCellHeight(contentWidget, "100%");

		contentWidget.setSize("100%", "100%");
		contentWidget.getElement().setId("contentWidgetWur");
		contentWidget.getElement().getFirstChildElement().getStyle().setWidth(100, Unit.PCT);
		contentWidget.getElement().getFirstChildElement().getStyle().setHeight(100, Unit.PCT);
	}

	public void setDescription(String html) {
		descriptionElement.setInnerHTML(html);
	}

	public void setContentWidget(Widget widget) {
		contentWidget.setWidget(widget);
	}

	public void setName(String text) {
		titleElement.setInnerText(text);
	}
}