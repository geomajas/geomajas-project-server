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
import java.util.List;

import org.geomajas.configuration.SortType;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.widget.Notify;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.Callback;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Wraps a FeatureListGrid in a Tab and adds some actions.
 * 
 * @author Kristof Heirwegh
 */
class FeatureListGridTab extends Tab implements SelectionChangedHandler {

	private static final String BTN_SHOW_DETAIL = "[ISOMORPHIC]/geomajas/widget/multifeaturelistgrid/info.gif";
	private static final String BTN_EXPORT = "[ISOMORPHIC]/geomajas/widget/multifeaturelistgrid/table_save.png";

	private FeatureListGrid featureListGrid;
	private Criterion criterion;
	private ToolStrip toolStrip;
	private ToolStripButton focusButton;
	private ToolStripButton showButton;
	private ToolStripButton exportButton;
	private List<ToolStripButton> extraButtons = new ArrayList<ToolStripButton>();

	private ExportToCsvHandler exportCsvHandler;
	private com.smartgwt.client.types.SortDirection sortDirGWT;
	private boolean sortFeatures;
	private String sortFieldName;
	private MapWidget mapWidget;
	private VectorLayer layer;
	private int numFeatures;

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);
	
	public FeatureListGridTab(final MapWidget mapWidget, final VectorLayer layer) {
		super(layer.getLabel());
		this.exportCsvHandler = new ExportFeatureListToCsvHandler(mapWidget.getMapModel(), layer);
		this.mapWidget = mapWidget;
		this.layer = layer;
		
		toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setHeight(24);

		focusButton = new ToolStripButton(messages.multiFeatureListGridButtonFocusSelection());
		focusButton.setIcon(WidgetLayout.iconZoomSelection);
		focusButton.setTooltip(messages.multiFeatureListGridButtonFocusSelectionTooltip());
		focusButton.setDisabled(true);
		focusButton.setShowDisabledIcon(false);
		focusButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				zoomToBounds();
			}
		});
		toolStrip.addButton(focusButton);

		showButton = new ToolStripButton(messages.multiFeatureListGridButtonShowDetail());
		showButton.setIcon(BTN_SHOW_DETAIL);
		showButton.setTooltip(messages.multiFeatureListGridButtonShowDetailTooltip());
		showButton.setDisabled(true);
		showButton.setShowDisabledIcon(false);
		showButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				showFeatureDetail();
			}
		});
		toolStrip.addButton(showButton);
		
		exportButton = new ToolStripButton(messages.multiFeatureListGridButtonExportToCSV());
		exportButton.setIcon(BTN_EXPORT);
		exportButton.setTooltip(messages.multiFeatureListGridButtonExportToCSVTooltip());
		exportButton.setShowDisabledIcon(false);
		if (exportCsvHandler == null) {
			exportButton.setVisible(false);
		}
		exportButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (exportCsvHandler != null) {
					exportButton.setDisabled(true);
					exportButton.setIcon(WidgetLayout.iconAjaxLoading);
					exportCsvHandler.execute(layer, new Callback() {

						public void execute() {
							exportButton.setDisabled(false);
							exportButton.setIcon(BTN_EXPORT);
						}
					});
				}
			}
		});
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
		
		String sortFieldName = layer.getLayerInfo().getFeatureInfo().getSortAttributeName(); 
					
		SortType sortType = layer.getLayerInfo().getFeatureInfo().getSortType();
		
		com.smartgwt.client.types.SortDirection sortDirGWT = null; 

		if (SortType.DESC.equals(sortType)) {
			sortDirGWT = com.smartgwt.client.types.SortDirection.DESCENDING;
		} else { /* also ascending if sortType == null */
			sortDirGWT = com.smartgwt.client.types.SortDirection.ASCENDING;					
		}
		this.sortDirGWT = sortDirGWT;
		if (null != sortFieldName) { /* if null and if sortFeatures==true, then sort on first column */
			featureListGrid.setSortField(sortFieldName);
			this.sortFieldName = sortFieldName;
		}
		featureListGrid.setSortDirection(sortDirGWT);
			
		VLayout pane = new VLayout();
		pane.setWidth100();
		pane.setHeight100();
		pane.setOverflow(Overflow.HIDDEN);
		pane.addMember(toolStrip);
		pane.addMember(featureListGrid);
		setPane(pane);
		setCanClose(true);
		
		//Display alert when feature list is not complete.
		this.addTabSelectedHandler(new TabSelectedHandler() {
			public void onTabSelected(TabSelectedEvent event) {
				if (numFeatures == SearchCommService.searchResultSize && numFeatures > 0) {
					Notify.info(messages.multiFeatureListGridMaxSizeExceededMessage());
				}
			}
		});
	}

	public boolean getSortFeatures() {
		return sortFeatures;
	}



	public void setSortFeatures(boolean sortFeatures) {
		this.sortFeatures = sortFeatures;
	}





	/**
	 * Add a button in the tool strip at the requested position.
	 *
	 * @param button button to add
	 * @param position position
	 */
	public void addButton(ToolStripButton button, int position) {
		toolStrip.addButton(button, position);
		extraButtons.add(button);
		button.setDisabled(true);
	}

	public void addFeatures(List<Feature> features) {
		numFeatures = features.size();
		for (Feature feature : features) {
			featureListGrid.addFeature(feature);
		}
		if (sortFeatures) {
			featureListGrid.sort(sortFieldName, sortDirGWT);
		}
		
		if (exportCsvHandler instanceof ExportFeatureListToCsvHandler) {
			((ExportFeatureListToCsvHandler) exportCsvHandler).setFeatures(features);
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
		for (ToolStripButton button : extraButtons) {
			button.setDisabled(count == 0);
		}
	}

	/**
	 * Get the selected records for the tab.
	 *
	 * @return selected records
	 */
	public ListGridRecord[] getSelection() {
		return featureListGrid.getSelection();
	}

	// ----------------------------------------------------------
	// -- Actions --
	// ----------------------------------------------------------

	private void zoomToBounds() {
		ListGridRecord[] selection = getSelection();
		int count = selection.length;
		if (count > 0) {
			List<Feature> features = new ArrayList<Feature>();
			for (ListGridRecord lgr : selection) {
				features.add(new Feature(lgr.getAttribute(FeatureListGrid.FIELD_NAME_FEATURE_ID), featureListGrid
						.getLayer()));
			}
			mapWidget.getMapModel().zoomToFeatures(features);
		}
	}

	private void showFeatureDetail() {
		ListGridRecord selected = featureListGrid.getSelectedRecord();
		if (selected != null) {
			String featureId = selected.getAttribute(FeatureListGrid.FIELD_NAME_FEATURE_ID);
			if (featureId != null && featureListGrid.getLayer() != null) {
				featureListGrid.getLayer().getFeatureStore()
						.getFeature(featureId, GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES, new LazyLoadCallback() {

							public void execute(List<Feature> response) {
								MultiFeatureListGrid.showFeatureDetailWindow(mapWidget, response.get(0));
							}
						});
			}
		}
	}

	/**
	 * @param criterion the criterion to set
	 */
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;

		if (exportCsvHandler instanceof ExportSearchToCsvHandler) {
			((ExportSearchToCsvHandler) exportCsvHandler).setCriterion(criterion);
		} else {
			this.exportCsvHandler = new ExportSearchToCsvHandler(mapWidget.getMapModel(), layer, criterion);
		}
	}

	/**
	 * @return the criterion
	 */
	public Criterion getCriterion() {
		return criterion;
	}
	
}