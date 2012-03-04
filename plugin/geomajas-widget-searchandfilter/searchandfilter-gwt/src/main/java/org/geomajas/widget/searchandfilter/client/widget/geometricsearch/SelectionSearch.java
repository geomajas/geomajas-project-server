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
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.widget.Notify;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Geometric search method based on a predefined geometry.
 *
 * @author Kristof Heirwegh
 * @author Bruce Palmkoeck
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class SelectionSearch extends AbstractGeometricSearchMethod implements FeatureSelectionHandler {

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private VLayout rootLayout;
	private VLayout mainLayout;
	private DynamicForm frmBuffer;
	private SpinnerItem spiBuffer;
	private Geometry geometry;
	private List<IButton> selectButtons = new ArrayList<IButton>();
	private Label noSelection;

	public String getTitle() {
		return messages.geometricSearchWidgetSelectionSearchTitle();
	}

	public void reset() {
		geometry = null;
		frmBuffer.reset();
	}

	/** {@inheritDoc} */
	public void initialize(MapWidget mapWidget, GeometryUpdateHandler handler) {
		super.initialize(mapWidget, handler);
		mapWidget.getMapModel().addFeatureSelectionHandler(this);
		updateState();
	}
		
	public Canvas getSearchCanvas() {
		if (rootLayout == null) {
			rootLayout = new VLayout(15);
			rootLayout.setWidth100();
			rootLayout.setHeight100();
			rootLayout.setPadding(5);

			Canvas selectionSearch = new Canvas();
			selectionSearch.setWidth100();
			selectionSearch.setHeight100();

			mainLayout = new VLayout(20);
			mainLayout.setWidth100();
			mainLayout.setHeight100();
	
			Label titleBar = new Label(messages.geometricSearchWidgetSelectionSearchTitle());
			titleBar.setBackgroundColor("#E0E9FF");
			titleBar.setWidth100();
			titleBar.setHeight(20);
			titleBar.setPadding(5);
	
			HStack actionStack = new HStack(WidgetLayout.marginSmall);
			HStack selectStack = new HStack(WidgetLayout.marginSmall);
	
			IButton btnZoom = new IButton(messages.geometricSearchWidgetSelectionSearchZoomToSelection());
			btnZoom.setIcon(WidgetLayout.iconZoomSelection);
			btnZoom.setAutoFit(true);
			btnZoom.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					onZoomClick();
				}
			});
			actionStack.addMember(btnZoom);
	
			IButton btnAdd = new IButton(messages.geometricSearchWidgetSelectionSearchAddSelection());
			btnAdd.setIcon(WidgetLayout.iconSelectedAdd);
			btnAdd.setAutoFit(true);
			btnAdd.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					onAddClick();
				}
			});
			selectStack.addMember(btnAdd);
			for (IButton button : selectButtons) {
				selectStack.addMember(button);
			}
	
			frmBuffer = new DynamicForm();
			frmBuffer.setWidth100();
			spiBuffer = new SpinnerItem();
			spiBuffer.setTitle(messages.geometricSearchWidgetBufferLabel());
			spiBuffer.setDefaultValue(5);
			spiBuffer.setMin(0);
			spiBuffer.setWidth(60);
			frmBuffer.setFields(spiBuffer);
	
			// ----------------------------------------------------------
	
			mainLayout.addMember(actionStack);
			mainLayout.addMember(frmBuffer);
			mainLayout.addMember(selectStack);
			mainLayout.setVisible(false);
			
			// ---------------------------------------------------------
			
			noSelection = new Label(messages.geometricSearchWidgetSelectionSearchNothingSelected());
			noSelection.setWidth100();
			noSelection.setAlign(Alignment.CENTER);
			noSelection.setPadding(5);
			noSelection.setTooltip(messages.geometricSearchWidgetSelectionSearchNothingSelectedTooltip());

			selectionSearch.addChild(mainLayout);
			selectionSearch.addChild(noSelection);
			
			rootLayout.addMember(titleBar);
			rootLayout.addMember(selectionSearch);
		}
		return rootLayout;
	}

	/**
	 * Add select button.
	 *
	 * @param button button to add
	 */
	@Api
	public void addSelectButton(IButton button) {
		selectButtons.add(button);
	}

	private void onAddClick() {
		List<Geometry> geometries = new ArrayList<Geometry>();
		for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
			if (layer.isShowing()) {
				VectorLayerStore store = layer.getFeatureStore();
				for (String featureId : layer.getSelectedFeatures()) {
					Feature f = store.getPartialFeature(featureId);
					if (f.isSelected()) {
						geometries.add(f.getGeometry());
					}
				}
			}
		}
		setGeometry(geometries);
	}

	/**
	 * Set the current current geometry using the list of geometries which are merged and buffered if requested.
	 *
	 * @param geometries geometries to combine
	 */
	@Api
	public void setGeometry(List<Geometry> geometries) {
		if (geometries.size() == 0) {
			Notify.info(messages.geometricSearchWidgetSelectionSearchNothingSelected());
		} else {
			Integer buffer = (Integer) spiBuffer.getValue();
			if (buffer != 0) {
				SearchCommService.mergeAndBufferGeometries(geometries, buffer, new DataCallback<Geometry[]>() {
					public void execute(Geometry[] result) {
						updateGeometry(geometry, result[1]);
						geometry = result[1];
					}
				});
			} else {
				SearchCommService.mergeGeometries(geometries, new DataCallback<Geometry>() {
					public void execute(Geometry result) {
						updateGeometry(geometry, result);
						geometry = result;
					}
				});
			}
		}
	}

	private void onZoomClick() {
		Bbox bounds = null;
		for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
			if (layer.isShowing()) {
				VectorLayerStore store = layer.getFeatureStore();
				for (String featureId : layer.getSelectedFeatures()) {
					Feature f = store.getPartialFeature(featureId);
					if (bounds == null) {
						bounds = f.getGeometry().getBounds();
					} else {
						bounds = bounds.union(f.getGeometry().getBounds());
					}
				}
			}
		}
		if (bounds == null) {
			Notify.info(messages.geometricSearchWidgetSelectionSearchNothingSelected());
		} else {
			mapWidget.getMapModel().getMapView().applyBounds(bounds, ZoomOption.LEVEL_FIT);
		}
	}

	// --- FeatureSelectionHandler ---------------------------------------

	public void onFeatureSelected(FeatureSelectedEvent event) {
		updateState();
	}

	public void onFeatureDeselected(FeatureDeselectedEvent event) {
		updateState();
	}
	
	private void updateState() {
		boolean hasSelection = (mapWidget.getMapModel().getNrSelectedFeatures() > 0);
		mainLayout.setVisible(hasSelection);
		noSelection.setVisible(!hasSelection);
	}
}
