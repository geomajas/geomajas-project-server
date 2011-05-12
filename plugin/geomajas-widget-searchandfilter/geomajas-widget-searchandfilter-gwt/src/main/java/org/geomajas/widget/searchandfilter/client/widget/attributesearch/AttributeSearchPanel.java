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
package org.geomajas.widget.searchandfilter.client.widget.attributesearch;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureSearch.LogicalOperator;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchPanel;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;

import com.google.gwt.core.client.GWT;
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
 * Searchpanel using (non geometry) attributes.
 *
 * @see SearchWidgetRegistry.
 * @author Kristof Heirwegh
 */
public class AttributeSearchPanel extends SearchPanel {

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private FeatureSearch featureSearch;

	public AttributeSearchPanel(final MapWidget mapWidget) {
		super(mapWidget);
		featureSearch = new FeatureSearch(mapWidget.getMapModel(), true);
		featureSearch.setWidth100();
		featureSearch.setHeight100();
		setWidth(550);
		setHeight(350);
		addChild(featureSearch);
	}

	@Override
	public boolean validate() {
		Criterion cr = featureSearch.getSearchCriteria();
		return (cr != null && cr.isValid());
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		return featureSearch.getSearchCriteria();
	}
	
	@Override
	public VectorLayer getFeatureSearchVectorLayer() {
		return featureSearch.getLayer();
	}

	@Override
	public void reset() {
		featureSearch.empty();
	}

	@Override
	public void initialize(Criterion criterion) {
		featureSearch.setSearchCriteria(criterion);
	}

	public static boolean canHandle(Criterion criterion) {
		if (criterion == null) {
			return false;
		}
		List<Criterion> critters;
		if (criterion instanceof OrCriterion) {
			critters = ((OrCriterion) criterion).getCriteria();
		} else if (criterion instanceof AndCriterion) {
			critters = ((AndCriterion) criterion).getCriteria();
		} else {
			return false;
		}

		for (Criterion critter : critters) {
			if (!(critter instanceof AttributeCriterion)) {
				return false;
			}
		}
		return true;
	}

	// ----------------------------------------------------------

	/**
	 * Adapted from @see {@link FeatureSearch} by @author Pieter De Graef.
	 */
	public class FeatureSearch extends Canvas {

		// The vector layer to search in.
		private VectorLayer layer;
		// A form item that shows the user what layer he's searching in.
		private FormItem layerSelect;
		// Logical operator; match one or match all criteria?
		private RadioGroupItem logicalOperatorRadio;

		private VStack criterionStack;

		private VStack buttonStack;

		private MapModel mapModel;

		private List<AttributeCriterionPane> criterionPanes;

		private List<HLayout> buttonPanes;

		private List<HandlerRegistration> addHandlers;

		private List<HandlerRegistration> removeHandlers;

		private boolean manualLayerSelection;

		// -------------------------------------------------------------------------
		// Constructors:
		// -------------------------------------------------------------------------

		/**
		 * Create a search widget for searching in a specific map model. This
		 * widget will automatically react to the selection of layers within
		 * that map model, and redraw to compensate for the selected layer. In
		 * other words, searching always happens on the selected layer.
		 *
		 * @param mapModel
		 *            The MapModel containing the possible layer to search in.
		 * @param manualLayerSelection
		 *            If true, a select box will be shown so the user can select
		 *            what layer to search in. The possible list of layers
		 *            consists of all the vector layers that are present in the
		 *            given MapModel. If false, this widget will react to the
		 *            layer select events that come from the MapMdodel. In that
		 *            case searching happens in the selected layer (if it's a
		 *            vector layer).<br/>
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
		 * Add a new empty row in the search grid, at the given position. An
		 * single row can be used to create a single criterion.
		 *
		 * @param index
		 *            Row number in the search grid, that indicates where to
		 *            add/insert a new empty row.
		 */
		public void addEmptyRow(final int index) {
			addEmptyRow(index, null);
		}

		public void addEmptyRow(final int index, AttributeCriterion ac) {
			if (layer == null || !(layer instanceof VectorLayer)) {
				return;
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

			if (ac != null) {
				newRow.setSearchCriterion(ac);
			}
		}

		public void setSearchCriteria(Criterion criterion) {
			if (criterion != null) {
				List<Criterion> criteria;
				VectorLayer vl = null;
				if (criterion instanceof AndCriterion) {
					criteria = ((AndCriterion) criterion).getCriteria();
					setLogicalOperator(LogicalOperator.AND);
				} else if (criterion instanceof OrCriterion) {
					criteria = ((OrCriterion) criterion).getCriteria();
					setLogicalOperator(LogicalOperator.OR);
				} else {
					SC.warn(messages.attributeSearchWidgetNoValidCriterionUnsupportedType());
					return;
				}

				for (int i = 0; i < criteria.size(); i++) {
					AttributeCriterion ac = (AttributeCriterion) criteria.get(i);
					if (vl == null) {
						List<VectorLayer> layers = mapModel.getVectorLayersByServerId(ac.getServerLayerId());
						if (layers == null || layers.size() < 1) {
							SC.warn(messages.attributeSearchWidgetNoValidCriterionNoLayer());
							return;
						}
						vl = layers.get(0);
						setLayer(vl);
						empty(false);
					}
					addEmptyRow(i, ac);
				}
				if (criteria.size() > 1) {
					buttonPanes.get(0).getMember(1).setDisabled(false);
				}
			}
		}

		/**
		 * Get the full list of search criteria from the criterion grid.
		 *
		 * @return
		 */
		public Criterion getSearchCriteria() {
			if (layer != null) {
				List<Criterion> crits;
				Criterion critter;
				String value = (String) logicalOperatorRadio.getValue();
				if (value.equals(I18nProvider.getSearch().radioOperatorAnd())) {
					critter = new AndCriterion();
					crits = ((AndCriterion) critter).getCriteria();
				} else {
					critter = new OrCriterion();
					crits = ((OrCriterion) critter).getCriteria();
				}

				for (AttributeCriterionPane criterionPane : criterionPanes) {
					if (criterionPane.hasErrors()) {
						SC.warn(I18nProvider.getSearch().warningInvalidCriteria());
						return null;
					}
					AttributeCriterion ac = criterionPane.getSearchCriterion();
					if (ac != null) {
						crits.add(ac);
					}
				}
				if (crits == null || crits.size() == 0) {
					SC.warn(messages.attributeSearchWidgetNoValidCriterionNoCriteria());
					return null;
				}
				return critter;
			} else {
				SC.warn(messages.attributeSearchWidgetNoLayerSelected());
				return null;
			}
		}

		public void empty() {
			empty(true);
		}

		/**
		 * Empty the grid, thereby removing all rows. When that is done, a new
		 * empty row will be displayed.
		 */
		public void empty(boolean shouldAddEmptyRow) {
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
			if (shouldAddEmptyRow) {
				addEmptyRow(0);
			}
		}

		// -------------------------------------------------------------------------
		// Getters and setters:
		// -------------------------------------------------------------------------

		/**
		 * Set a new value for the logical operator. This operator determines
		 * whether all the criteria have to be met in the search, or just one of
		 * them.
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
		 * Return the current value for the logical operator.This operator
		 * determines whether all the criteria have to be met in the search, or
		 * just one of them.
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
		 * Return the layer onto which searching should happen. (the MapModel's
		 * selected layer)
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
			Object value = layerSelect.getValue();
			if (value == null || !value.equals(layer.getLabel())) {
				layerSelect.setValue(layer.getLabel());
			}
			empty();
		}

		// -------------------------------------------------------------------------
		// Private methods:
		// -------------------------------------------------------------------------

		private void updateLayerList() {
			List<String> layers = new ArrayList<String>();
			for (Layer<?> vLayer : mapModel.getLayers()) {
				if (vLayer instanceof VectorLayer) {
					layers.add(vLayer.getLabel());
				}
			}
			layerSelect.setValueMap(layers.toArray(new String[0]));
		}

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
				mapModel.addMapModelHandler(new MapModelHandler() {
					public void onMapModelChange(MapModelEvent event) {
						updateLayerList();
					}
				});
				updateLayerList();
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

			optionLayout.addMember(leftLayout);
			optionLayout.addMember(new LayoutSpacer());

			// Create a header for the criterionStack:
			HLayout headerLayout = new HLayout();
			headerLayout.setHeight(26);
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
			searchGrid.setBorder("1px solid lightgrey");

			layout.addMember(optionLayout);
			layout.addMember(searchGrid);
			addChild(layout);
		}

		private void updateLabelTitle(String title) {
			layerSelect.setValue("<b>" + title + "</b>");
		}
	}
}
