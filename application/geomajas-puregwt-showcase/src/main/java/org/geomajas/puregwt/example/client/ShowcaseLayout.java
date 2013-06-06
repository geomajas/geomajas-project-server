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

package org.geomajas.puregwt.example.client;

import org.geomajas.puregwt.example.client.page.overview.SampleOverviewPage;
import org.geomajas.puregwt.example.client.resource.ShowcaseResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Top layout for the Geomajas PureGWT showcase.
 * 
 * @author Pieter De Graef
 */
public class ShowcaseLayout extends Composite {

	/**
	 * UI binder interface this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, ShowcaseLayout> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	@UiField
	protected SimplePanel contentPanel;

	public ShowcaseLayout() {
		initWidget(UIBINDER.createAndBindUi(this));
		contentPanel.add(new SampleOverviewPage());
	}
	
	public void setPage(IsWidget page) {
		contentPanel.clear();
		contentPanel.add(page);
	}

	@UiFactory
	public ShowcaseResource getResourceBundle() {
		return Showcase.RESOURCE;
	}
}