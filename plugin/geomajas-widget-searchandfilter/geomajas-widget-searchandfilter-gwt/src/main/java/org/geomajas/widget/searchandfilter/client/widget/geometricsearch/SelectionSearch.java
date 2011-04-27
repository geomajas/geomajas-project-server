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
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 * @author Bruce Palmkoeck
 */
public class SelectionSearch extends AbstractGeometricSearchMethod {

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private static final String BTN_ADD_IMG = "[ISOMORPHIC]/geomajas/osgeo/selected-add.png";
	private static final String BTN_FOCUS_IMG =	"[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";

	private DynamicForm frmBuffer;
	private SpinnerItem spiBuffer;
	private Geometry geometry;

	public String getTitle() {
		return messages.geometricSearchWidgetSelectionSearchTitle();
	}

	public void reset() {
		geometry = null;
		frmBuffer.reset();
	}

	public Canvas getSearchCanvas() {
		VLayout mainLayout = new VLayout(20);
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setPadding(5);

		Label titleBar = new Label(messages.geometricSearchWidgetSelectionSearchTitle());
		titleBar.setBackgroundColor("#E0E9FF");
		titleBar.setWidth100();
		titleBar.setHeight(20);
		titleBar.setPadding(5);

		IButton btnZoom = new IButton(messages.geometricSearchWidgetSelectionSearchZoomToSelection());
		btnZoom.setIcon(BTN_FOCUS_IMG);
		btnZoom.setAutoFit(true);
		btnZoom.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onZoomClick();
			}
		});

		IButton btnAdd = new IButton(messages.geometricSearchWidgetSelectionSearchAddSelection());
		btnAdd.setIcon(BTN_ADD_IMG);
		btnAdd.setAutoFit(true);
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onAddClick();
			}
		});

		frmBuffer = new DynamicForm();
		frmBuffer.setWidth100();
		spiBuffer = new SpinnerItem();
		spiBuffer.setTitle(messages.geometricSearchWidgetBufferLabel());
		spiBuffer.setDefaultValue(5);
		spiBuffer.setMin(0);
		spiBuffer.setWidth(60);
		frmBuffer.setFields(spiBuffer);

		// ----------------------------------------------------------

		mainLayout.addMember(titleBar);
		mainLayout.addMember(btnZoom);
		mainLayout.addMember(frmBuffer);
		mainLayout.addMember(btnAdd);

		return mainLayout;
	}

	private void onAddClick() {
		List<Geometry> geoms = new ArrayList<Geometry>();
		for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
			if (layer.isShowing()) {
				VectorLayerStore store = layer.getFeatureStore();
				for (String featureId : layer.getSelectedFeatures()) {
					Feature f = store.getPartialFeature(featureId);
					if (f.isSelected()) {
						geoms.add(f.getGeometry());
					}
				}
			}
		}
		if (geoms.size() == 0) {
			SC.say(messages.geometricSearchWidgetSelectionSearchNothingSelected());
		} else {
			Integer buffer = (Integer) spiBuffer.getValue();
			if (buffer != 0) {
				SearchCommService.mergeAndBufferGeometries(geoms, buffer, new DataCallback<Geometry[]>() {
					public void execute(Geometry[] result) {
						updateGeometry(geometry, result[1]);
						geometry = result[1];
					}
				});
			} else {
				SearchCommService.mergeGeometries(geoms, new DataCallback<Geometry>() {
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
			SC.say(messages.geometricSearchWidgetSelectionSearchNothingSelected());
		} else {
			mapWidget.getMapModel().getMapView().applyBounds(bounds, ZoomOption.LEVEL_FIT);
		}
	}
}
