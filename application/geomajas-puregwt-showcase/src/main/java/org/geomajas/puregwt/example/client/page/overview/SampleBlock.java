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

package org.geomajas.puregwt.example.client.page.overview;

import org.geomajas.puregwt.example.client.sample.ShowcaseSampleDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout for a single category of samples. Displays the samples as anchors.
 * 
 * @author Pieter De Graef
 */
public class SampleBlock extends Composite {

	/**
	 * UI binder interface this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, SampleBlock> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	private final ShowcaseSampleDefinition factory;

	@UiField
	protected DivElement titleElement;

	@UiField
	protected DivElement descriptionElement;

	@UiField
	protected DivElement categoryElement;

	public SampleBlock(final ShowcaseSampleDefinition factory) {
		this.factory = factory;
		initWidget(UIBINDER.createAndBindUi(this));
		setSize("350px", "100px");

		titleElement.setInnerText(factory.getTitle());
		descriptionElement.setInnerHTML(factory.getShortDescription());
		categoryElement.setInnerText("Category: " + factory.getCategory());
	}

	public ShowcaseSampleDefinition getSamplePanelFactory() {
		return factory;
	}
}