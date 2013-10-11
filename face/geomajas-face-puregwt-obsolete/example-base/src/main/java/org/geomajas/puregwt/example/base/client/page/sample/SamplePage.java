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

package org.geomajas.puregwt.example.base.client.page.sample;

import org.geomajas.puregwt.example.base.client.ExampleBase;
import org.geomajas.puregwt.example.base.client.resource.ShowcaseResource;
import org.geomajas.puregwt.example.base.client.sample.ShowcaseSampleDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Page layout for samples.
 * 
 * @author Pieter De Graef
 */
public class SamplePage extends Composite {

	/**
	 * UI binder interface this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, SamplePage> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	private ShowcaseSampleDefinition samplePanelFactory;

	@UiField
	protected DivElement descriptionElement;

	@UiField
	protected SimplePanel contentPanel;

	public SamplePage() {
		initWidget(UIBINDER.createAndBindUi(this));
	}

	public ShowcaseSampleDefinition getSamplePanelFactory() {
		return samplePanelFactory;
	}

	public void setSamplePanelFactory(ShowcaseSampleDefinition samplePanelFactory) {
		this.samplePanelFactory = samplePanelFactory;
		refreshGui();
	}

	private void refreshGui() {
		descriptionElement.setInnerHTML(samplePanelFactory.getDescription());
		contentPanel.clear();
		contentPanel.add(samplePanelFactory.create());
	}

	@UiFactory
	protected ShowcaseResource getResourceBundle() {
		return ExampleBase.getShowcaseResource();
	}
}