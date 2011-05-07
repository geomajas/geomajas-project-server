/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.layers.DefaultContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class ShowcaseLayout extends Composite {

	private static final TestLayoutUiBinder UIBINDER = GWT.create(TestLayoutUiBinder.class);

	/**
	 * ...
	 * 
	 * @author Pieter De Graef
	 */
	interface TestLayoutUiBinder extends UiBinder<Widget, ShowcaseLayout> {
	}

	@UiField
	protected ScrollPanel leftPanel;

	@UiField
	protected ScrollPanel contentPanel;

	public ShowcaseLayout() {
		initWidget(UIBINDER.createAndBindUi(this));
		Label blop = new Label("Dit is een label");
		leftPanel.add(blop);
		ContentPanel panel = new DefaultContentPanel();
		contentPanel.add(panel);
		panel.ensureWidget();
	}
}