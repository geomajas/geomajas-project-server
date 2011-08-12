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
package org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.CommandRequest;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetFactory;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.Callback;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchHandler;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * A collection of FeatureListGrids.
 * 
 * @author Kristof Heirwegh
 */
public class MultiFeatureListGrid extends Canvas implements SearchHandler {

	protected final MapWidget map;

	protected TabSet tabset;

	protected Label empty;

	protected boolean clearTabsetOnSearch;

	protected boolean showDetailsOnSingleResult;

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

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

	/**
	 * Remove all data from the widget.
	 */
	public void removeAll() {
		for (Tab tab : tabset.getTabs()) {
			tabset.removeTab(tab);
		}
		setEmpty(true);
	}

	/**
	 * Remove just the given layer (if it exists).
	 * 
	 * @param layer layer to remove
	 */
	public void remove(VectorLayer layer) {
		removeTab(layer);
	}

	public void addFeatures(VectorLayer layer, List<Feature> features) {
		addFeatures(layer, features, null, showDetailsOnSingleResult);
	}

	/**
	 * Add features for a specific layer in the widget.
	 *
	 * @param layer layer to add features for
	 * @param features features to ass
	 * @param csvExportData
	 *            will be used by CSV Export to retrieve features.
	 */
	public void addFeatures(VectorLayer layer, List<Feature> features, Object csvExportData) {
		addFeatures(layer, features, csvExportData, showDetailsOnSingleResult);
	}

	public void addFeatures(Map<VectorLayer, List<Feature>> result) {
		addFeatures(result, null);
	}

	/**
	 * Add features in the widget for several layers.
	 *
	 * @param featureMap map of features per layer
	 * @param csvExportData
	 *            will be used by CSV Export to retrieve features.
	 */
	public void addFeatures(Map<VectorLayer, List<Feature>> featureMap, Object csvExportData) {
		for (Entry<VectorLayer, List<Feature>> entry : featureMap.entrySet()) {
			addFeatures(entry.getKey(), entry.getValue(), csvExportData, false);
		}
		if (showDetailsOnSingleResult && featureMap.size() == 1) {
			List<Feature> features = featureMap.values().iterator().next();
			if (features.size() == 1) {
				showFeatureDetailWindow(features.get(0));
			}
		}
	}

	private void addFeatures(VectorLayer layer, List<Feature> features, Object csvExportData, boolean showSingleResult)
	{
		FeatureListGridTab t;
		if (csvExportData instanceof Criterion) {
			t = getTab(layer, (Criterion) csvExportData);
		} else if (csvExportData instanceof CommandRequest) {
			t = getTab(layer, (CommandRequest) csvExportData);
		} else {
			if (csvExportData != null) {
				SC.logWarn("Unsupported csvExportData class: " + csvExportData.getClass().getName());
			}
			t = getTab(layer);
		}

		t.empty();
		t.addFeatures(features);
		tabset.selectTab(t);
		if (showSingleResult && features.size() == 1) {
			showFeatureDetailWindow(features.get(0));
		}
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

	private void removeTab(VectorLayer layer) {
		String id = tabset.getID() + "_" + layer.getId();
		if (tabset.getTab(id) != null) {
			tabset.removeTab(id);
			setEmpty((tabset.getTabs().length == 0));
		}
	}

	private FeatureListGridTab getTab(VectorLayer layer) {
		return getTab(layer, new ExportFeatureListToCsvHandler(map.getMapModel(), layer));
	}

	private FeatureListGridTab getTab(VectorLayer layer, CommandRequest searchRequest) {
		return getTab(layer, new ExportSearchToCsvHandler(map.getMapModel(), layer, searchRequest));
	}

	private FeatureListGridTab getTab(VectorLayer layer, Criterion criterion) {
		return getTab(layer, new ExportSearchToCsvHandler(map.getMapModel(), layer, criterion));
	}

	private FeatureListGridTab getTab(VectorLayer layer, ExportToCsvHandler handler) {
		String id = tabset.getID() + "_" + layer.getId();
		FeatureListGridTab t = (FeatureListGridTab) tabset.getTab(id);
		if (t == null) {
			t = new FeatureListGridTab(map, layer, handler);
			t.setID(id);
			tabset.addTab(t);
		} else {
			// Do not forget to update
			t.setExportToCsvHandler(handler);
		}
		setEmpty((tabset.getTabs().length == 0));
		return t;
	}

	private void showFeatureDetailWindow(final Feature feature) {
		Window window = FeatureDetailWidgetFactory.createFeatureDetailWindow(feature, false);
		window.setPageTop(map.getAbsoluteTop() + 10);
		window.setPageLeft(map.getAbsoluteLeft() + 10);
		window.draw();
	}

	/**
	 * Wraps a FeatureListGrid in a Tab and adds some actions.
	 * 
	 * @author Kristof Heirwegh
	 */
	private class FeatureListGridTab extends Tab implements SelectionChangedHandler {

		private static final String BTN_FOCUSSELECTION = "[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";
		private static final String BTN_SHOWDETAIL = "[ISOMORPHIC]/geomajas/widget/multifeaturelistgrid/info.gif";
		private static final String BTN_EXPORT = "[ISOMORPHIC]/geomajas/widget/multifeaturelistgrid/table_save.png";
		private static final String PROCESSING = "[ISOMORPHIC]/geomajas/ajax-loader.gif";

		private FeatureListGrid featureListGrid;
		private ToolStripButton focusButton;
		private ToolStripButton showButton;
		private ToolStripButton exportButton;

		private ExportToCsvHandler handler;

		public void setExportToCsvHandler(ExportToCsvHandler handler) {
			this.handler = handler;
		}

		public FeatureListGridTab(final MapWidget mapWidget, final VectorLayer layer, final ExportToCsvHandler handler)
		{
			super(layer.getLabel());
			this.handler = handler;
			setOverflow(Overflow.HIDDEN);
			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			toolStrip.setHeight(24);
			focusButton = new ToolStripButton(messages.multiFeatureListGridButtonFocusSelection());
			showButton = new ToolStripButton(messages.multiFeatureListGridButtonShowDetail());
			exportButton = new ToolStripButton(messages.multiFeatureListGridButtonExportToCSV());
			focusButton.setIcon(BTN_FOCUSSELECTION);
			showButton.setIcon(BTN_SHOWDETAIL);
			exportButton.setIcon(BTN_EXPORT);
			focusButton.setTooltip(messages.multiFeatureListGridButtonFocusSelectionTooltip());
			showButton.setTooltip(messages.multiFeatureListGridButtonShowDetailTooltip());
			exportButton.setTooltip(messages.multiFeatureListGridButtonExportToCSVTooltip());
			focusButton.setDisabled(true);
			showButton.setDisabled(true);
			showButton.setShowDisabledIcon(false);
			focusButton.setShowDisabledIcon(false);
			exportButton.setShowDisabledIcon(false);
			if (handler == null) {
				exportButton.setVisible(false);
			}
			focusButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					zoomToBounds();
				}
			});
			showButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					showFeatureDetail();
				}
			});
			exportButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					if (handler != null) {
						exportButton.setDisabled(true);
						exportButton.setIcon(PROCESSING);
						handler.execute(layer, new Callback() {

							public void execute() {
								exportButton.setDisabled(false);
								exportButton.setIcon(BTN_EXPORT);
							}
						});
					}
				}
			});
			toolStrip.addButton(focusButton);
			toolStrip.addButton(showButton);
			toolStrip.addButton(exportButton);
			featureListGrid = new FeatureListGrid(mapWidget.getMapModel(), new DoubleClickHandler() {

				public void onDoubleClick(DoubleClickEvent event) {
					showFeatureDetail();
				}
			});
			featureListGrid.setLayer(layer);
			featureListGrid.addSelectionChangedHandler(this);
			featureListGrid.setAutoFitFieldWidths(true);
			featureListGrid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
			featureListGrid.setOverflow(Overflow.AUTO);
			featureListGrid.setWidth100();
			featureListGrid.setHeight100();

			VLayout pane = new VLayout();
			pane.setWidth100();
			pane.setHeight100();
			pane.setOverflow(Overflow.HIDDEN);
			pane.addMember(toolStrip);
			pane.addMember(featureListGrid);
			setPane(pane);
			setCanClose(true);
		}

		public void addFeatures(List<Feature> features) {
			for (Feature feature : features) {
				featureListGrid.addFeature(feature);
			}
			if (handler instanceof ExportFeatureListToCsvHandler) {
				((ExportFeatureListToCsvHandler) handler).setFeatures(features);
			}
		}

		public void empty() {
			featureListGrid.empty();
		}

		// ----------------------------------------------------------
		// -- Events --
		// ----------------------------------------------------------

		public void onSelectionChanged(SelectionEvent event) {
			int count = event.getSelection().length;
			if (count == 0) {
				focusButton.setDisabled(true);
				showButton.setDisabled(true);
			} else if (count == 1) {
				focusButton.setDisabled(false);
				showButton.setDisabled(false);
			} else {
				focusButton.setDisabled(false);
				showButton.setDisabled(true);
			}
		}

		// ----------------------------------------------------------
		// -- Actions --
		// ----------------------------------------------------------

		private void zoomToBounds() {
			int count = featureListGrid.getSelection().length;
			if (count > 0) {
				LazyLoadCallback llc = new ZoomToBoundsFeatureLazyLoadCallback(count);
				for (ListGridRecord lgr : featureListGrid.getSelection()) {
					featureListGrid.getLayer().getFeatureStore()
							.getFeature(lgr.getAttribute("featureId"), GeomajasConstant.FEATURE_INCLUDE_GEOMETRY, llc);
				}
			}
		}

		/**
		 * Stateful callback that zooms to bounds when all features have been retrieved.
		 * 
		 * @author Kristof Heirwegh
		 */
		private class ZoomToBoundsFeatureLazyLoadCallback implements LazyLoadCallback {

			private int featureCount;
			private Bbox bounds;

			public ZoomToBoundsFeatureLazyLoadCallback(int featureCount) {
				this.featureCount = featureCount;
			}

			public void execute(List<Feature> response) {
				if (response != null && response.size() > 0) {
					if (bounds == null) {
						bounds = (Bbox) response.get(0).getGeometry().getBounds().clone();
					} else {
						bounds = bounds.union(response.get(0).getGeometry().getBounds());
					}
				}
				featureCount--;
				if (featureCount == 0) {
					if (bounds != null) {
						map.getMapModel().getMapView().applyBounds(bounds, ZoomOption.LEVEL_FIT);
					}
				}
			}
		}

		private void showFeatureDetail() {
			ListGridRecord selected = featureListGrid.getSelectedRecord();
			if (selected != null) {
				String featureId = selected.getAttribute("featureId");
				if (featureId != null && featureListGrid.getLayer() != null) {
					featureListGrid.getLayer().getFeatureStore()
							.getFeature(featureId, GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES, new LazyLoadCallback() {

								public void execute(List<Feature> response) {
									showFeatureDetailWindow(response.get(0));
								}
							});
				}
			}
		}
	}
}
