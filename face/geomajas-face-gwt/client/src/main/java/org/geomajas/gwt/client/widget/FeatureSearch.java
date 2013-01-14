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

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
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
import com.smartgwt.client.widgets.layout.LayoutSpacer;
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

	public static final String STYLE_SEARCH_ROW = "searchRow";
	public static final String STYLE_SEARCH_HEADER = "searchHeader";
	public static final String STYLE_HEADER_BAR = "headerBar";

	/**
	 * The logical operator. This operator determines whether all the criteria have to be met in the search (AND), or
	 * just one of them (OR).
	 * 
	 * @author Pieter De Graef
	 */
	public static enum LogicalOperator {
		/** AND operator. */
		AND,
		/** OR operator. */
		OR
	}

	private VectorLayer layer; // The vector layer to search in.

	private FormItem layerSelect; // A form item that shows the user what layer he's searching in.

	private RadioGroupItem logicalOperatorRadio; // Logical operator; match one or match all criteria?

	private IButton searchButton; // The actual button that executes the search.

	private IButton resetButton; // A button that resets the search layout.

	private VStack criterionStack;

	private VStack buttonStack;

	private final MapModel mapModel;

	private final List<AttributeCriterionPane> criterionPanes;

	private final List<HLayout> buttonPanes;

	private final List<HandlerRegistration> addHandlers;

	private final List<HandlerRegistration> removeHandlers;

	/** The maximum number of allowed results. No more features will be retrieved then this number. */
	private int maximumResultSize = 100;

	private final boolean manualLayerSelection;

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
	 *
	 * @param handler search handler
	 * @return handler registration
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
		if (layer == null) {
			return;
		}
		searchButton.setDisabled(false);
		if (index > 0) {
			resetButton.setDisabled(false);
		}

		// Empty row:
		AttributeCriterionPane newRow = new AttributeCriterionPane(layer);
		newRow.setHeight(32);
		newRow.setStyleName(STYLE_SEARCH_ROW);

		HLayout btnLayout = new HLayout();
		btnLayout.setHeight(32);
		btnLayout.setMembersMargin(WidgetLayout.marginSmall);
		btnLayout.setAlign(Alignment.CENTER);
		btnLayout.setPadding(4);
		btnLayout.setStyleName(STYLE_SEARCH_ROW);

		IButton btnAddRow = new IButton();
		btnAddRow.setWidth(26);
		btnAddRow.setIcon(WidgetLayout.iconAdd);
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
		btnRemoveRow.setIcon(WidgetLayout.iconRemove);
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
	 * @return list of criteria
	 */
	public List<SearchCriterion> getSearchCriteria() {
		List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();
		for (AttributeCriterionPane criterionPane : criterionPanes) {
			if (criterionPane.hasErrors()) {
				SC.warn(I18nProvider.getSearch().warningInvalidCriteria());
				return null;
			}
			SearchCriterion criterion = criterionPane.getSearchCriterion();
			if (criterion != null) {
				criteria.add(criterion);
			}
		}
		if (criteria.size() == 0) {
			SC.warn(I18nProvider.getSearch().warningNoCriteria());
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
				request.setCriteria(criteria.toArray(new SearchCriterion[criteria.size()]));
				request.setCrs(mapModel.getCrs());
				request.setLayerId(layer.getServerLayerId());
				request.setMax(maximumResultSize);
				request.setFilter(layer.getFilter());
				request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());

				GwtCommand command = new GwtCommand(SearchFeatureRequest.COMMAND);
				command.setCommandRequest(request);
				GwtCommandDispatcher.getInstance().execute(command,
						new AbstractCommandCallback<SearchFeatureResponse>() {

					public void execute(SearchFeatureResponse response) {
						List<Feature> features = new ArrayList<Feature>();
						for (org.geomajas.layer.feature.Feature dtoFeature : response.getFeatures()) {
							Feature feature = new Feature(dtoFeature, layer);
							layer.getFeatureStore().addFeature(feature);
							features.add(feature);
						}
						SearchEvent event = new SearchEvent(layer, features);
						FeatureSearch.this.fireEvent(event);
					}
				});
			}
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Set a new value for the logical operator. This operator determines whether all the criteria have to be met in the
	 * search, or just one of them.
	 *
	 * @param operator logical operator
	 */
	public void setLogicalOperator(LogicalOperator operator) {
		switch (operator) {
			case AND:
				logicalOperatorRadio.setValue(I18nProvider.getSearch().radioOperatorAnd());
				break;
			case OR:
				logicalOperatorRadio.setValue(I18nProvider.getSearch().radioOperatorOr());
				break;
			default:
				throw new IllegalStateException("Unknown operator " + operator);
		}
	}

	/**
	 * Return the current value for the logical operator.This operator determines whether all the criteria have to be
	 * met in the search, or just one of them.
	 * 
	 * @return current logical operator
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
	 * @return layer to search on
	 */
	public VectorLayer getLayer() {
		return layer;
	}

	/**
	 * Set a new layer onto which searching should happen.
	 * 
	 * @param layer layer to search on
	 */
	public void setLayer(VectorLayer layer) {
		this.layer = layer;
		Object value = layerSelect.getValue();
		if (value == null || !value.equals(layer.getLabel())) {
			layerSelect.setValue(layer.getLabel());
		}
		empty();
	}

	/**
	 * Get the maximum number of allowed results. No more features will be retrieved then this number.
	 *
	 * @return maximum results
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

		logicalOperatorRadio = new RadioGroupItem("logicalOperator");
		logicalOperatorRadio.setValueMap(I18nProvider.getSearch().radioOperatorOr(), I18nProvider.getSearch()
				.radioOperatorAnd());
		logicalOperatorRadio.setVertical(false);
		logicalOperatorRadio.setRequired(true);
		logicalOperatorRadio.setAlign(Alignment.LEFT);
		logicalOperatorRadio.setWidth(250);
		logicalOperatorRadio.setShowTitle(false);

		HLayout optionLayout = new HLayout();
		optionLayout.setHeight(50);
		optionLayout.setWidth100();

		VLayout leftLayout = new VLayout();
		leftLayout.setAlign(Alignment.LEFT);

		HLayout layerLayout = new HLayout();
		layerLayout.setWidth(420);
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
			mapModel.addMapModelChangedHandler(new MapModelChangedHandler() {

				public void onMapModelChanged(MapModelChangedEvent event) {
					fillLayerSelect();
				}
			});
			// needed if map is already loaded !
			fillLayerSelect();
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
		logicalForm.setAutoWidth();
		logicalForm.setLayoutAlign(Alignment.CENTER);
		logicalForm.setFields(logicalOperatorRadio);
		leftLayout.setWidth(420);
		leftLayout.addMember(logicalForm);

		VLayout rightLayout = new VLayout();
		rightLayout.setLayoutAlign(VerticalAlignment.TOP);
		rightLayout.setMargin(5);
		rightLayout.setMembersMargin(5);
		rightLayout.setWidth(100);
		searchButton = new IButton(I18nProvider.getSearch().btnSearch());
		searchButton.setIcon(WidgetLayout.iconFind);
		searchButton.setWidth(100);
		searchButton.setDisabled(true);
		searchButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				search();
			}
		});
		resetButton = new IButton(I18nProvider.getSearch().btnReset());
		resetButton.setIcon(WidgetLayout.iconUndo);
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
		optionLayout.addMember(new LayoutSpacer());
		optionLayout.addMember(rightLayout);

		// Create a header for the criterionStack:
		HLayout headerLayout = new HLayout();
		headerLayout.setHeight(26);
		headerLayout.setStyleName(STYLE_HEADER_BAR);
		HTMLPane attrHeader = new HTMLPane();
		attrHeader.setStyleName(STYLE_SEARCH_HEADER);
		attrHeader.setContents("Attribute");
		attrHeader.setWidth(140);
		HTMLPane operatorHeader = new HTMLPane();
		operatorHeader.setContents("Operator");
		operatorHeader.setWidth(140);
		operatorHeader.setStyleName(STYLE_SEARCH_HEADER);
		HTMLPane valueHeader = new HTMLPane();
		valueHeader.setContents("Value");
		valueHeader.setStyleName(STYLE_SEARCH_HEADER);

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
		btnHeader.setStyleName(STYLE_HEADER_BAR);
		btnHeader.setWidth(70);
		btnHeader.setHeight(26);
		buttonStack.addMember(btnHeader);

		HLayout searchGrid = new HLayout();
		searchGrid.addMember(criterionStack);
		searchGrid.addMember(buttonStack);
		searchGrid.setBorder("1px solid lightgrey");

		layout.addMember(optionLayout);
		layout.addMember(searchGrid);
		addChild(layout);
	}

	private void updateLabelTitle(String title) {
		layerSelect.setValue("<b>" + title + "</b>");
	}

	private void fillLayerSelect() {
		List<String> layers = new ArrayList<String>();
		for (Layer<?> vLayer : mapModel.getLayers()) {
			if (vLayer instanceof VectorLayer) {
				layers.add(vLayer.getLabel());
			}
		}
		layerSelect.setValueMap(layers.toArray(new String[layers.size()]));
	}
}
