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
package org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetFactory;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchHandler;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;


/**
 * A collection of FeatureListGrids.
 * 
 * @author Kristof Heirwegh
 * @author Joachim Van der Auwera
 * @author An Buyle
 * @author Oliver May
 * 
 * @since 1.0.0
 */
@Api
public class MultiFeatureListGrid extends Canvas implements SearchHandler {

	protected final MapWidget map;

	protected TabSet tabset;

	protected Label empty;

	protected boolean clearTabsetOnSearch;

	protected boolean showDetailsOnSingleResult;
	
	protected boolean showCsvExportAction = true;
	
	private boolean sortFeatures; 
	
	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private List<ExtraButton> extraButtons = new ArrayList<ExtraButton>();

	public MultiFeatureListGrid(MapWidget map) {
		super();
		this.map = map;

		tabset = new TabSet();
		tabset.setWidth100();
		tabset.setHeight100();
		tabset.setOverflow(Overflow.HIDDEN);
		tabset.addCloseClickHandler(new CloseClickHandler() {

			public void onCloseClick(TabCloseClickEvent event) {
				setEmpty((tabset.getTabs().length == 1));
			}
		});
		this.addChild(tabset);

		empty = new Label(messages.multiFeatureListGridNoData());
		empty.setWidth100();
		empty.setAlign(Alignment.CENTER);
		empty.setPadding(15);
		this.addChild(empty);
	}

	public void initialize() {
	}

	public MapWidget getMap() {
		return map;
	}

	public boolean isClearTabsetOnSearch() {
		return clearTabsetOnSearch;
	}

	public void setClearTabsetOnSearch(boolean clearTabsetOnSearch) {
		this.clearTabsetOnSearch = clearTabsetOnSearch;
	}

	public boolean isShowDetailsOnSingleResult() {
		return showDetailsOnSingleResult;
	}

	public void setShowDetailsOnSingleResult(boolean showDetailsOnSingleResult) {
		this.showDetailsOnSingleResult = showDetailsOnSingleResult;
	}

	public boolean isShowCsvExportAction() {
		return showCsvExportAction;
	}
	
	public void setShowCsvExportAction(boolean showCsvExportAction) {
		this.showCsvExportAction = showCsvExportAction;
	}

	public void setSortFeatures(boolean sortFeatures) {
		this.sortFeatures = sortFeatures;
	}

	public boolean doSortFeatures() {
		return sortFeatures;
	}

	/**
	 * Remove all data from the widget.
	 */
	public void removeAll() {
		for (Tab tab : tabset.getTabs()) {
			tabset.removeTab(tab);
		}
		setEmpty();
	}

	/**
	 * Remove just the given layer (if it exists).
	 * 
	 * @param layer layer to remove
	 */
	public void remove(VectorLayer layer) {
		removeTab(layer);
	}

	/**
	 * Add features for a specific layer in the widget.
	 * 
	 * @param layer
	 * @param features
	 * @deprecated Use {@link #addFeatures(Map)}
	 */
	@Deprecated
	public void addFeatures(VectorLayer layer, List<Feature> features) {
		Map<VectorLayer, List<Feature>> featureMap = new HashMap<VectorLayer, List<Feature>>();
		featureMap.put(layer, features);
		addFeatures(featureMap, null);
	}

	/**
	 * Add features for a specific layer in the widget.
	 *
	 * @param layer layer to add features for
	 * @param features features to ass
	 * @param csvExportData
	 *            will be used by CSV Export to retrieve features.
	 * @deprecated Use {@link #addFeatures(Map, Criterion)}           
	 */
	@Deprecated
	public void addFeatures(VectorLayer layer, List<Feature> features, Object csvExportData) {
		Map<VectorLayer, List<Feature>> featureMap = new HashMap<VectorLayer, List<Feature>>();
		featureMap.put(layer, features);

		if (csvExportData instanceof Criterion) {
			addFeatures(featureMap, (Criterion) csvExportData);
		} else {
			addFeatures(featureMap, null);
		}
		
	}
	
	/**
	 * Add features in the widget for several layers.
	 *
	 * @param featureMap map of features per layer
	 */
	public void addFeatures(Map<VectorLayer, List<Feature>> result) {
		addFeatures(result, null);
	}

	/**
	 * Add features in the widget for several layers.
	 *
	 * @param featureMap map of features per layer
	 * @param criterion the original request for this search
	 */
	public void addFeatures(Map<VectorLayer, List<Feature>> featureMap, Criterion criterion) {
		if (showDetailsOnSingleResult && featureMap.size() == 1) {
			// sorting is never needed if only 1 entry
			List<Feature> features = featureMap.values().iterator().next();
			if (features.size() == 1) {
				showFeatureDetailWindow(map, features.get(0));
			}
		}

		//Add feature tabs in map order
		for (VectorLayer layer : map.getMapModel().getVectorLayers()) {
			if (featureMap.containsKey(layer)) {
				addFeatures(layer, featureMap.get(layer), criterion);
			}
		}
		tabset.selectTab(0);
	}

	private void addFeatures(VectorLayer layer, List<Feature> features, Criterion criterion) {
		FeatureListGridTab tab = getOrCreateTab(layer);
		tab.empty();
		tab.setSortFeatures(sortFeatures);
		tab.setCriterion(criterion);
		tab.addFeatures(features);
		tabset.addTab(tab, 0);
		setEmpty();
	}

	/**
	 * Add a button in the tool strip at the requested position.
	 *
	 * @param layerId layer which needs the extra button
	 * @param button button to add
	 * @param position position
	 */
	@Api
	public void addButton(String layerId, ToolStripButton button, int position) {
		extraButtons.add(new ExtraButton(layerId, button, position));
	}

	// ----------------------------------------------------------
	// -- SearchHandler --
	// ----------------------------------------------------------

	public void onSearchStart(SearchEvent event) {
	} // not used

	public void onSearchEnd(SearchEvent event) {
	} // not used

	public void onSearchDone(SearchEvent event) {
		if (clearTabsetOnSearch) {
			removeAll();
		}
		addFeatures(event.getResult(), event.getCriterion());
	}

	// ----------------------------------------------------------

	private void setEmpty(boolean state) {
		empty.setVisible(state);
	}

	private void setEmpty() {
		setEmpty((tabset.getTabs().length == 0));
	}

	private void removeTab(VectorLayer layer) {
		String id = tabset.getID() + "_" + layer.getId();
		if (tabset.getTab(id) != null) {
			tabset.removeTab(id);
			setEmpty();
		}
	}

	private FeatureListGridTab getOrCreateTab(VectorLayer layer) {
		String layerId = layer.getId();
		String id = tabset.getID() + "_" + layerId;
		FeatureListGridTab t = (FeatureListGridTab) tabset.getTab(id);
		if (t == null) {
			t = new FeatureListGridTab(map, layer, isShowCsvExportAction());
			t.setID(id);
			for (ExtraButton button : extraButtons) {
				if (layerId.equals(button.getLayerId())) {
					t.addButton(button.getButton(), button.getPosition());
				}
			}
		}
		return t;
	}

	/**
	 * Get the selected records for the tab.
	 *
	 * @param layerId layer to get selected items for
	 * @return selected records
	 */
	@Api
	public ListGridRecord[] getSelection(String layerId) {
		String id = tabset.getID() + "_" + layerId;
		FeatureListGridTab tab = (FeatureListGridTab) tabset.getTab(id);
		if (tab != null) {
			return tab.getSelection();
		}
		return null;
	}

	/**
	 * Container for keeping a button until attached to a tab.
	 *
	 * @author Joachim Van der Auwera
	 */
	private static class ExtraButton {
		private String layerId;
		private ToolStripButton button;
		private int position;

		public ExtraButton(String layerId, ToolStripButton button, int position) {
			this.layerId = layerId;
			this.button = button;
			this.position = position;
		}

		public String getLayerId() {
			return layerId;
		}

		public ToolStripButton getButton() {
			return button;
		}

		public int getPosition() {
			return position;
		}
	}
	
	//FIXME: move to a service?
	public static void showFeatureDetailWindow(final MapWidget mapWidget, final Feature feature) {
		Window window = FeatureDetailWidgetFactory.createFeatureDetailWindow(feature, false);
		window.setPageTop(mapWidget.getAbsoluteTop() + 10);
		window.setPageLeft(mapWidget.getAbsoluteLeft() + 10);
		window.draw();
	}
	
}
