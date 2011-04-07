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

import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.event.SearchEvent;
import org.geomajas.gwt.client.widget.event.SearchHandler;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetFactory;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
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
public class MultiFeatureListGrid extends Canvas implements SearchHandler, MultiLayerSearchHandler {

	protected final MapWidget map;

	protected TabSet tabset;

	protected Label empty;

	protected boolean clearTabsetOnSearch;

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	public MultiFeatureListGrid(MapWidget map) {
		super();
		this.map = map;

		tabset = new TabSet();
		tabset.setWidth100();
		tabset.setHeight100();
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

	/**
	 * Remove all.
	 */
	public void removeAll() {
		for (Tab tab : tabset.getTabs()) {
			tabset.removeTab(tab);
		}
		setEmpty(true);
	}

	/**
	 * Remove just the given layer (if exists).
	 *
	 * @param layer
	 */
	public void remove(VectorLayer layer) {
		removeTab(layer);
	}

	public void addFeatures(VectorLayer layer, List<Feature> features) {
		FeatureListGridTab t = getTab(layer);
		t.empty();
		t.addFeatures(features);
		tabset.selectTab(t);
	}

	public void addFeatures(Map<VectorLayer, List<Feature>> result) {
		for (Entry<VectorLayer, List<Feature>> entry : result.entrySet()) {
			addFeatures(entry.getKey(), entry.getValue());
		}
	}

	// ----------------------------------------------------------
	// -- SearchHandler, MultiLayerSearchHandler --
	// ----------------------------------------------------------

	public void onSearchDone(SearchEvent event) {
		if (clearTabsetOnSearch) {
			removeAll();
		}
		addFeatures(event.getLayer(), event.getFeatures());
	}

	public void onSearchDone(MultiLayerSearchEvent event) {
		if (clearTabsetOnSearch) {
			removeAll();
		}
		addFeatures(event.getResult());
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
		String id = tabset.getID() + "_" + layer.getId();
		Tab t = tabset.getTab(id);
		if (t == null) {
			t = new FeatureListGridTab(map, layer);
			t.setID(id);
			tabset.addTab(t);
			setEmpty((tabset.getTabs().length == 0));
		}
		return (FeatureListGridTab) t;
	}

	/**
	 * Wraps a FeatureListGrid in a Tab and adds some actions.
	 *
	 * @author Kristof Heirwegh
	 */
	private class FeatureListGridTab extends Tab implements SelectionChangedHandler {
		private static final String BUTTON_ICON_FOCUSSELECTION =
			"[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";
		private static final String BUTTON_ICON_SHOWDETAIL =
			"[ISOMORPHIC]/geomajas/widget/multifeaturelistgrid/info.gif";
		private static final String BUTTON_ICON_EXPORT =
			"[ISOMORPHIC]/geomajas/widget/multifeaturelistgrid/table_save.png";

		private FeatureListGrid featureListGrid;
		private ToolStripButton focusButton;
		private ToolStripButton showButton;
		private ToolStripButton exportButton;

		public FeatureListGridTab(final MapWidget mapWidget, final VectorLayer layer) {
			super(layer.getLabel());
			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setWidth100();
			focusButton = new ToolStripButton(messages.multiFeatureListGridButtonFocusSelection());
			showButton = new ToolStripButton(messages.multiFeatureListGridButtonShowDetail());
			exportButton = new ToolStripButton(messages.multiFeatureListGridButtonExportToCSV());
			focusButton.setIcon(BUTTON_ICON_FOCUSSELECTION);
			showButton.setIcon(BUTTON_ICON_SHOWDETAIL);
			exportButton.setIcon(BUTTON_ICON_EXPORT);
			focusButton.setTooltip(messages.multiFeatureListGridButtonFocusSelectionTooltip());
			showButton.setTooltip(messages.multiFeatureListGridButtonShowDetailTooltip());
			exportButton.setTooltip(messages.multiFeatureListGridButtonExportToCSVTooltip());
			focusButton.setDisabled(true);
			showButton.setDisabled(true);
			showButton.setShowDisabledIcon(false);
			focusButton.setShowDisabledIcon(false);
			focusButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					zoomToBounds();
				}
			});
			showButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					showFeatureDetail(mapWidget);
				}
			});
			exportButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					exportToCSV();
				}
			});
			toolStrip.addButton(focusButton);
			toolStrip.addButton(showButton);
			toolStrip.addButton(exportButton);

			featureListGrid = new FeatureListGrid(mapWidget.getMapModel());
			featureListGrid.setLayer(layer);
			featureListGrid.addSelectionChangedHandler(this);

			VLayout pane = new VLayout();
			pane.setWidth100();
			pane.setHeight100();
			pane.addMember(toolStrip);
			pane.addMember(featureListGrid);
			setPane(pane);
			setCanClose(true);
		}

		public void addFeatures(List<Feature> features) {
			for (Feature feature : features) {
				featureListGrid.addFeature(feature);
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
		 * Statefull callback that zooms to bounds when all features have been retrieved
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

		private void showFeatureDetail(final MapWidget mapWidget) {
			ListGridRecord selected = featureListGrid.getSelectedRecord();
			if (selected != null) {
				String featureId = selected.getAttribute("featureId");
				if (featureId != null && featureListGrid.getLayer() != null) {
					featureListGrid.getLayer().getFeatureStore()
							.getFeature(featureId, GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES, new LazyLoadCallback() {
								public void execute(List<Feature> response) {
									Window window = FeatureDetailWidgetFactory.createFeatureDetailWindow(
											response.get(0), false);
									window.setPageTop(mapWidget.getAbsoluteTop() + 10);
									window.setPageLeft(mapWidget.getAbsoluteLeft() + 10);
									window.draw();
								}
							});
				}
			}
		}

		private void exportToCSV() {
			// TODO
		}
	}
}
