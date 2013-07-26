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

package org.geomajas.gwt.example.base.client.page.overview;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.example.base.client.ExampleBase;
import org.geomajas.gwt.example.base.client.resource.ShowcaseResource;
import org.geomajas.gwt.example.base.client.sample.SamplePanelRegistry;
import org.geomajas.gwt.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.gwt.example.base.client.widget.ClickableImage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Samples overview page. Here you can search within all samples. You can also switch between block and list view.
 * 
 * @author Pieter De Graef
 */
public class SampleOverviewPage extends Composite {

	private static final String CATEGORY_ALL = "All";

	/**
	 * UI binder interface this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, SampleOverviewPage> {
	}

	private static final MyUiBinder UIBINDER = GWT.create(MyUiBinder.class);

	private HasSamples samplesView;

	private List<ShowcaseSampleDefinition> factories;

	@UiField
	protected ListBox categoryBox;

	@UiField
	protected TextBox searchBox;

	@UiField
	protected Anchor showAllAnchor;

	@UiField
	protected SpanElement countElement;

	@UiField
	protected ClickableImage listViewImg;

	@UiField
	protected ClickableImage blockViewImg;

	@UiField
	protected ResizeLayoutPanel sampleOverviewPanel;

	public SampleOverviewPage() {
		initWidget(UIBINDER.createAndBindUi(this));

		listViewImg.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				applyView(false);
			}
		});
		blockViewImg.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				applyView(true);
			}
		});
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		setSize("100%", "100%");

		if (sampleOverviewPanel.getWidget() == null) {
			factories = SamplePanelRegistry.getFactories();
			applyView(true);

			// Initialize the category ListBox:
			for (String category : SamplePanelRegistry.getCategories()) {
				categoryBox.addItem(category);
			}
			categoryBox.addChangeHandler(new ChangeHandler() {

				public void onChange(ChangeEvent event) {
					applyFilters();
				}
			});

			// Initialize searchBox:
			searchBox.addKeyUpHandler(new KeyUpHandler() {

				public void onKeyUp(KeyUpEvent event) {
					applyFilters();
				}
			});

			// Initialize the ShowAll anchor:
			showAllAnchor.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					searchBox.setValue("");
					applyFilters();
				}
			});

			applyFilters();
		}
	}

	private void applyView(boolean blockView) {
		sampleOverviewPanel.clear();
		if (blockView) {
			samplesView = new SampleBlockView(factories, this);
		} else {
			samplesView = new SampleListView(factories, this);
		}
		sampleOverviewPanel.add(samplesView);
	}

	@UiFactory
	protected ShowcaseResource getResourceBundle() {
		return ExampleBase.getShowcaseResource();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void applyCountTxt(int count) {
		countElement.setInnerText("(" + count + " / " + factories.size() + ")");
	}

	private void applyFilters() {
		List<ShowcaseSampleDefinition> filtered = new ArrayList<ShowcaseSampleDefinition>();
		for (ShowcaseSampleDefinition sampleFactory : factories) {
			boolean accepted = isInCategory(sampleFactory) && acceptSearchString(sampleFactory);
			if (accepted) {
				filtered.add(sampleFactory);
			}
		}
		applyCountTxt(filtered.size());
		samplesView.setData(filtered);
	}

	private boolean isInCategory(ShowcaseSampleDefinition sampleFactory) {
		int index = categoryBox.getSelectedIndex();
		String categoryFilter = categoryBox.getItemText(index);
		return CATEGORY_ALL.equalsIgnoreCase(categoryFilter) || categoryFilter.equals(sampleFactory.getCategory());
	}

	private boolean acceptSearchString(ShowcaseSampleDefinition sampleFactory) {
		String searchTxt = searchBox.getValue();
		if (searchTxt == null || searchTxt.length() == 0) {
			return true;
		}
		String temp = searchTxt.toLowerCase();
		if (sampleFactory.getTitle().toLowerCase().contains(temp)) {
			return true;
		}
		if (sampleFactory.getCategory().toLowerCase().contains(temp)) {
			return true;
		}
		if (sampleFactory.getDescription().toLowerCase().contains(temp)) {
			return true;
		}
		return false;
	}
}