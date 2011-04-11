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

import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;

import com.google.gwt.core.client.GWT;
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
public class SelectionSearch implements GeometricSearchMethod {

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private static final String BTN_ADD_IMG = "[ISOMORPHIC]/geomajas/osgeo/selected-add.png";
	private static final String BTN_FOCUS_IMG =	"[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";

	private MapWidget mapWidget;
	private DynamicForm frmBuffer;

	public SelectionSearch() {
		super();
	}

	public void initialize(MapWidget map) {
		this.mapWidget = map;
	}

	public String getTitle() {
		return messages.geometricSearchWidgetSelectionSearchTitle();
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
				for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
					// TODO
				}
			}
		});

		IButton btnAdd = new IButton(messages.geometricSearchWidgetSelectionSearchAddSelection());
		btnAdd.setIcon(BTN_ADD_IMG);
		btnAdd.setAutoFit(true);
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});

		frmBuffer = new DynamicForm();
		frmBuffer.setWidth100();
		SpinnerItem spiBuffer = new SpinnerItem();
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

	public Geometry getGeometry() {
		// TODO Auto-generated method stub
		return null;
	}

	public void reset() {
		frmBuffer.reset();
	}
}
