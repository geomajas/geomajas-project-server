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

package org.geomajas.plugin.jsapi.example.client.example;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout for an example within the showcase.
 * 
 * @author Pieter De Graef
 */
public class ExampleLayout extends Widget {

	/**
	 * UI binder interface for this showcase layout.
	 * 
	 * @author Pieter De Graef
	 */
	interface ExampleLayoutUiBinder extends UiBinder<DivElement, ExampleLayout> {
	}

	private static final ExampleLayoutUiBinder UI_BINDER = GWT.create(ExampleLayoutUiBinder.class);

	@UiField
	protected DivElement titleDiv;

	@UiField
	protected DivElement explanationDiv;

	public ExampleLayout(final Example example) {
		setElement(UI_BINDER.createAndBindUi(this));
		setTitle(example.getTitle());
		setExplanation(example.getExplanation());

		addDomHandler(new MouseOverHandler() {

			public void onMouseOver(MouseOverEvent event) {
				setStyleName("outer-over");
			}
		}, MouseOverEvent.getType());

		addDomHandler(new MouseOutHandler() {

			public void onMouseOut(MouseOutEvent event) {
				setStyleName("outer");
			}
		}, MouseOutEvent.getType());

		addDomHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
				String path = Window.Location.getPath();
				int pos = path.lastIndexOf("/");
				if (pos > 0) {
					path = path.substring(0, pos + 1);
				} else {
					path = "";
				}
				urlBuilder.setPath(path + example.getLink());
				Window.open(urlBuilder.buildString(), "_blank", "");
			}
		}, MouseUpEvent.getType());
	}

	public void setTitle(String title) {
		titleDiv.setInnerText(title);
	}

	public void setExplanation(String explanation) {
		explanationDiv.setInnerText(explanation);
	}
}