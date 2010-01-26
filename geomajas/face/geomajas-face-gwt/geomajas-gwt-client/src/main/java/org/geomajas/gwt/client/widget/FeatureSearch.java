/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.extension.command.dto.SearchFeatureRequest;
import org.geomajas.extension.command.dto.SearchFeatureResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.attribute.AttributeCriterionPane;
import org.geomajas.gwt.client.widget.event.SearchEvent;
import org.geomajas.gwt.client.widget.event.SearchHandler;
import org.geomajas.layer.feature.SearchCriterion;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.BlurbItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * <p>
 * Widget that supports searching for features through their alpha numerical attributes. Requires a value for
 * "manualLayerSelection" at construction time. If true, a select box will be shown so the user can select what layer to
 * search in. The possible list of layers consists of all the vector layers that are present in the given MapModel. If
 * false, this widget will react to the layer select events that come from the MapMdodel. In that case searching happens
 * in the selected layer (if it's a vector layer).
 * </p>
 * <p>
 * When a search has been executed, and a result is returned from the server, a {@link SearchEvent} will be triggered.
 * So in order to do something with the result, add a {@link SearchHandler} to this widget. Hint: There is a
 * <code>DefaultSearchHandler</code> that displays the resulting set of features in a {@link FeatureListGrid} widget.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FeatureSearch extends Canvas {

	/**
	 * The logical operator. This operator determines whether all the criteria have to be met in the search (AND), or
	 * just one of them (OR).
	 * 
	 * @author Pieter De Graef
	 */
	public static enum LogicalOperator {
		AND, OR
	}

	private VectorLayer layer; // The vector layer to search in.

	private FormItem layerSelect; // A form item that shows the user what layer he's searching in.

	private RadioGroupItem logicalOperatorRadio; // Logical operator; match one or match all criteria?

	private IButton searchButton; // The actual button that executes the search.

	private IButton resetButton; // A button that resets the search layout.

	private VStack criterionStack;

	private VStack buttonStack;

	private MapModel mapModel;

	private List<AttributeCriterionPane> criterionPanes;

	private List<HLayout> buttonPanes;

	private List<HandlerRegistration> addHandlers;

	private List<HandlerRegistration> removeHandlers;

	/** The maximum number of allowed results. No more features will be retrieved then this number. */
	private int maximumResultSize = 100;

	private boolean manualLayerSelection;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a search widget for searching in a specific map model. This widget will automatically react to the
	 * selection of layers within that map model, and redraw to compensate for the selected layer. In other words,
	 * searching always happens on the selected layer.
	 * 
	 * @param mapModel
	 *            The MapModel containing the possible layer to search in.
	 * @param manualLayerSelection
	 *            If true, a select box will be shown so the user can select what layer to search in. The possible list
	 *            of layers consists of all the vector layers that are present in the given MapModel. If false, this
	 *            widget will react to the layer select events that come from the MapMdodel. In that case searching
	 *            happens in the selected layer (if it's a vector layer).<br/>
	 *            This value cannot be altered anymore.
	 */
	public FeatureSearch(MapModel mapModel, boolean manualLayerSelection) {
		super();
		this.mapModel = mapModel;
		this.manualLayerSelection = manualLayerSelection;
		criterionPanes = new ArrayList<AttributeCriterionPane>();
		buttonPanes = new ArrayList<HLayout>();
		addHandlers = new ArrayList<HandlerRegistration>();
		removeHandlers = new ArrayList<HandlerRegistration>();
		buildUI();
		setLogicalOperator(LogicalOperator.AND);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Add a handler for the {@link SearchEvent}. A search event, is the event that is triggered when the result of a
	 * search request successfully returns from the server. The list of features from that result will be present in
	 * that search event object.
	 * </p>
	 * <p>
	 * Hint: There is a <code>DefaultSearchHandler</code> that displays the resulting set of features in a
	 * {@link FeatureListGrid} widget.
	 * </p>
	 */
	public HandlerRegistration addSearchHandler(SearchHandler handler) {
		return doAddHandler(handler, SearchEvent.TYPE);
	}

	/**
	 * Add a new empty row in the search grid, at the given position. An single row can be used to create a single
	 * criterion.
	 * 
	 * @param index
	 *            Row number in the search grid, that indicates where to add/insert a new empty row.
	 */
	public void addEmptyRow(final int index) {
		if (layer == null || !(layer instanceof VectorLayer)) {
			return;
		}
		searchButton.setDisabled(false);
		if (index > 0) {
			resetButton.setDisabled(false);
		}

		// Empty row:
		AttributeCriterionPane newRow = new AttributeCriterionPane(layer);
		newRow.setHeight(32);
		newRow.setStyleName("searchRow");

		HLayout btnLayout = new HLayout();
		btnLayout.setHeight(32);
		btnLayout.setMembersMargin(5);
		btnLayout.setAlign(Alignment.CENTER);
		btnLayout.setPadding(4);
		btnLayout.setStyleName("searchRow");

		IButton btnAddRow = new IButton();
		btnAddRow.setWidth(26);
		btnAddRow.setIcon("[ISOMORPHIC]/geomajas/silk/add.png");
		HandlerRegistration addReg = btnAddRow.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				IButton btnAddRow2 = (IButton) event.getSource();
				for (int i = 0; i < buttonPanes.size(); i++) {
					IButton button = (IButton) buttonPanes.get(i).getMember(0);
					if (btnAddRow2.equals(button)) {
						addEmptyRow(i + 1);
						break;
					}
				}
				buttonPanes.get(0).getMember(1).setDisabled(false);
			}
		});

		IButton btnRemoveRow = new IButton();
		btnRemoveRow.setWidth(26);
		btnRemoveRow.setIcon("[ISOMORPHIC]/geomajas/silk/remove.png");
		HandlerRegistration removeReg = btnRemoveRow.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				IButton sourceBtn = (IButton) event.getSource();
				for (int i = 0; i < buttonPanes.size(); i++) {
					IButton button = (IButton) buttonPanes.get(i).getMember(1);
					if (sourceBtn.equals(button)) {
						criterionStack.removeMember(criterionPanes.remove(i));

						// Remove row 0 from add button/registration:
						buttonStack.removeMember(buttonPanes.remove(i));
						addHandlers.remove(i).removeHandler();
						removeHandlers.remove(i).removeHandler();
						break;
					}
				}
				if (buttonPanes.size() == 1) {
					buttonPanes.get(0).getMember(1).setDisabled(true);
					resetButton.setDisabled(true);
				}
			}
		});
		if (index == 0) {
			btnRemoveRow.setDisabled(true);
		}

		btnLayout.addMember(btnAddRow);
		btnLayout.addMember(btnRemoveRow);

		// Add to the stacks:
		criterionStack.addMember(newRow, index + 1);
		buttonStack.addMember(btnLayout, index + 1);

		// Add to the lists:
		criterionPanes.add(index, newRow);
		buttonPanes.add(index, btnLayout);
		addHandlers.add(index, addReg);
		removeHandlers.add(index, removeReg);
	}

	/**
	 * Get the full list of search criteria from the criterion grid.
	 * 
	 * @return
	 */
	public List<SearchCriterion> getSearchCriteria() {
		List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();
		for (AttributeCriterionPane criterionPane : criterionPanes) {
			SearchCriterion criterion = criterionPane.getSearchCriterion();
			if (criterion != null) {
				criteria.add(criterion);
			}
		}
		return criteria;
	}

	/**
	 * Empty the grid, thereby removing all rows. When that is done, a new empty row will be displayed.
	 */
	public void empty() {
		searchButton.setDisabled(true);
		resetButton.setDisabled(true);
		for (AttributeCriterionPane criterionPane : criterionPanes) {
			criterionStack.removeMember(criterionPane);
		}
		criterionPanes.clear();
		for (HLayout criterionPane : buttonPanes) {
			buttonStack.removeMember(criterionPane);
		}
		buttonPanes.clear();
		for (HandlerRegistration handlerRegistration : addHandlers) {
			handlerRegistration.removeHandler();
		}
		addHandlers.clear();
		for (HandlerRegistration handlerRegistration : removeHandlers) {
			handlerRegistration.removeHandler();
		}
		removeHandlers.clear();
		addEmptyRow(0);
	}

	/**
	 * Execute the actual search. All features that are returned in the result will be added to the layer's feature
	 * store. Then a {@link SearchEvent} is fired.
	 */
	public void search() {
		if (layer != null) {
			// First we try to get the list of criteria:
			List<SearchCriterion> criteria = getSearchCriteria();
			if (criteria != null && !criteria.isEmpty()) {
				SearchFeatureRequest request = new SearchFeatureRequest();
				String value = (String) logicalOperatorRadio.getValue();
				if (value.equals(I18nProvider.getSearch().radioOperatorAnd())) {
					request.setBooleanOperator("AND");
				} else {
					request.setBooleanOperator("OR");
				}
				request.setCriteria(criteria.toArray(new SearchCriterion[0]));
				request.setCrs(mapModel.getCrs());
				request.setLayerId(layer.getId());
				request.setMax(maximumResultSize);
				request.setFilter(layer.getFilter());

				GwtCommand command = new GwtCommand("command.feature.Search");
				command.setCommandRequest(request);
				GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

					public void execute(CommandResponse response) {
						if (response instanceof SearchFeatureResponse) {
							SearchFeatureResponse resp = (SearchFeatureResponse) response;
							List<Feature> features = new ArrayList<Feature>();
							for (org.geomajas.layer.feature.Feature dtoFeature : resp.getFeatures()) {
								Feature feature = new Feature(dtoFeature, layer);
								layer.getFeatureStore().addFeature(feature);
								features.add(feature);
							}
							SearchEvent event = new SearchEvent(layer, features);
							FeatureSearch.this.fireEvent(event);
						}
					}
				});
			} else {
				SC.warn(I18nProvider.getSearch().warningNoCriteria());
			}
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Set a new value for the logical operator. This operator determines whether all the criteria have to be met in the
	 * search, or just one of them.
	 */
	public void setLogicalOperator(LogicalOperator operator) {
		switch (operator) {
			case AND:
				logicalOperatorRadio.setValue(I18nProvider.getSearch().radioOperatorAnd());
				break;
			case OR:
				logicalOperatorRadio.setValue(I18nProvider.getSearch().radioOperatorOr());
		}
	}

	/**
	 * Return the current value for the logical operator.This operator determines whether all the criteria have to be
	 * met in the search, or just one of them.
	 * 
	 * @return
	 */
	public LogicalOperator getLogicalOperator() {
		String value = (String) logicalOperatorRadio.getValue();
		if (value.equals(I18nProvider.getSearch().radioOperatorAnd())) {
			return LogicalOperator.AND;
		}
		return LogicalOperator.OR;
	}

	/**
	 * Return the layer onto which searching should happen. (the MapModel's selected layer)
	 * 
	 * @return
	 */
	public VectorLayer getLayer() {
		return layer;
	}

	/**
	 * Set a new layer onto which searching should happen.
	 * 
	 * @param layer
	 */
	public void setLayer(VectorLayer layer) {
		this.layer = layer;
		empty();
	}

	/**
	 * Get the maximum number of allowed results. No more features will be retrieved then this number.
	 */
	public int getMaximumResultSize() {
		return maximumResultSize;
	}

	/**
	 * Set a new maximum number of allowed results. No more features will be retrieved then this number.
	 * 
	 * @param maximumResultSize
	 *            The new value.
	 */
	public void setMaximumResultSize(int maximumResultSize) {
		this.maximumResultSize = maximumResultSize;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void buildUI() {
		// Create the layout:
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		logicalOperatorRadio = new RadioGroupItem("logicalOperator", "");
		logicalOperatorRadio.setValueMap(I18nProvider.getSearch().radioOperatorOr(), I18nProvider.getSearch()
				.radioOperatorAnd());
		logicalOperatorRadio.setVertical(false);
		logicalOperatorRadio.setRequired(true);
		logicalOperatorRadio.setAlign(Alignment.LEFT);
		logicalOperatorRadio.setWidth(250);

		HLayout optionLayout = new HLayout();
		optionLayout.setHeight(50);
		optionLayout.setWidth100();

		VLayout leftLayout = new VLayout();
		leftLayout.setAlign(Alignment.LEFT);

		HLayout layerLayout = new HLayout();
		DynamicForm layerForm = new DynamicForm();
		layerForm.setHeight(30);
		if (manualLayerSelection) {
			layerSelect = new SelectItem();
			layerSelect.setTitle(I18nProvider.getSearch().labelLayerSelected());
			layerSelect.setWidth(250);
			layerSelect.setHint(I18nProvider.getSearch().labelNoLayerSelected());
			((SelectItem) layerSelect).setShowHintInField(true);
			layerSelect.addChangedHandler(new ChangedHandler() {

				public void onChanged(ChangedEvent event) {
					String layerLabel = (String) event.getValue();
					for (Layer<?> vLayer : mapModel.getLayers()) {
						if (vLayer.getLabel().equals(layerLabel)) {
							setLayer((VectorLayer) vLayer);
						}
					}
				}
			});
			mapModel.addMapModelHandler(new MapModelHandler() {

				public void onMapModelChange(MapModelEvent event) {
					List<String> layers = new ArrayList<String>();
					for (Layer<?> vLayer : mapModel.getLayers()) {
						if (vLayer instanceof VectorLayer) {
							layers.add(vLayer.getLabel());
						}
					}
					layerSelect.setValueMap(layers.toArray(new String[0]));
				}
			});
		} else {
			mapModel.addLayerSelectionHandler(new LayerSelectionHandler() {

				public void onDeselectLayer(LayerDeselectedEvent event) {
					empty();
					updateLabelTitle(I18nProvider.getSearch().labelNoLayerSelected());
				}

				public void onSelectLayer(LayerSelectedEvent event) {
					if (event.getLayer() instanceof VectorLayer) {
						setLayer((VectorLayer) event.getLayer());
						if (event.getLayer() != null) {
							updateLabelTitle(event.getLayer().getLabel());
						}
					}
				}
			});
			layerSelect = new BlurbItem();
			layerSelect.setShowTitle(true);
			layerSelect.setTitle(I18nProvider.getSearch().labelLayerSelected());
			layerSelect.setWidth(250);
			layerSelect.setValue("<b>" + I18nProvider.getSearch().labelNoLayerSelected() + "</b>");
		}
		layerForm.setFields(layerSelect);
		layerLayout.addMember(layerForm);

		leftLayout.addMember(layerLayout);
		DynamicForm logicalForm = new DynamicForm();
		logicalForm.setFields(logicalOperatorRadio);
		leftLayout.addMember(logicalForm);

		VLayout rightLayout = new VLayout();
		rightLayout.setAlign(Alignment.RIGHT);
		rightLayout.setMembersMargin(5);
		rightLayout.setWidth(100);
		searchButton = new IButton(I18nProvider.getSearch().btnSearch());
		searchButton.setIcon("[ISOMORPHIC]/geomajas/silk/find.png");
		searchButton.setWidth(100);
		searchButton.setDisabled(true);
		searchButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				search();
			}
		});
		resetButton = new IButton(I18nProvider.getSearch().btnReset());
		resetButton.setIcon("[ISOMORPHIC]/geomajas/silk/undo.png");
		resetButton.setWidth(100);
		resetButton.setDisabled(true);
		resetButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				empty();
			}
		});
		rightLayout.addMember(searchButton);
		rightLayout.addMember(resetButton);

		optionLayout.addMember(leftLayout);
		optionLayout.addMember(rightLayout);

		// Create a header for the criterionStack:
		HLayout headerLayout = new HLayout();
		headerLayout.setHeight(26);
		headerLayout.setWidth100();
		headerLayout.setStyleName("headerBar");
		HTMLPane attrHeader = new HTMLPane();
		attrHeader.setStyleName("searchHeader");
		attrHeader.setContents("Attribute");
		attrHeader.setWidth(140);
		HTMLPane operatorHeader = new HTMLPane();
		operatorHeader.setContents("Operator");
		operatorHeader.setWidth(140);
		operatorHeader.setStyleName("searchHeader");
		HTMLPane valueHeader = new HTMLPane();
		valueHeader.setContents("Value");
		valueHeader.setStyleName("searchHeader");
		valueHeader.setWidth100();

		criterionStack = new VStack();
		criterionStack.setAlign(VerticalAlignment.TOP);
		headerLayout.addMember(attrHeader);
		headerLayout.addMember(operatorHeader);
		headerLayout.addMember(valueHeader);
		criterionStack.addMember(headerLayout);

		buttonStack = new VStack();
		buttonStack.setWidth(70);
		buttonStack.setAlign(VerticalAlignment.TOP);
		HTMLPane btnHeader = new HTMLPane();
		btnHeader.setStyleName("headerBar");
		btnHeader.setWidth(70);
		btnHeader.setHeight(26);
		buttonStack.addMember(btnHeader);

		HLayout searchGrid = new HLayout();
		searchGrid.addMember(criterionStack);
		searchGrid.addMember(buttonStack);
		searchGrid.setWidth100();
		searchGrid.setHeight100();
		searchGrid.setBorder("1px solid lightgrey");

		layout.addMember(optionLayout);
		layout.addMember(searchGrid);
		addChild(layout);
	}

	private void updateLabelTitle(String title) {
		layerSelect.setValue("<b>" + title + "</b>");
	}
}
