/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.jsapi.example.client;

import org.geomajas.plugin.jsapi.example.client.example.Example;
import org.geomajas.plugin.jsapi.example.client.example.ExampleLayout;
import org.geomajas.plugin.jsapi.example.client.example.Examples;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout for the Geomajas JavaScript API showcase.
 * 
 * @author Pieter De Graef
 */
public class ShowcaseLayout extends Composite {

	/**
	 * UI binder interface for this showcase layout.
	 * 
	 * @author Pieter De Graef
	 */
	interface ShowcaseLayoutUiBinder extends UiBinder<Widget, ShowcaseLayout> {
	}

	private static final ShowcaseLayoutUiBinder UI_BINDER = GWT.create(ShowcaseLayoutUiBinder.class);

	@UiField
	protected ScrollPanel contentPanel;

	public ShowcaseLayout() {
		initWidget(UI_BINDER.createAndBindUi(this));

		FlowPanel flowPanel = new FlowPanel();
		for (Example example : Examples.EXAMPLES) {
			ExampleLayout exampleLayout = new ExampleLayout(example);
			flowPanel.add(exampleLayout);
		}
		contentPanel.add(flowPanel);
	}
}