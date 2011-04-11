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
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 * @author Bruce Palmkoeck
 */
public class FreeDrawingSearch implements GeometricSearchMethod {

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private static final String BTN_POINT_CREATE_IMG = "[ISOMORPHIC]/geomajas/osgeo/point-create.png";
	private static final String BTN_LINE_CREATE_IMG = "[ISOMORPHIC]/geomajas/osgeo/line-create.png";
	private static final String BTN_POLYGON_CREATE_IMG = "[ISOMORPHIC]/geomajas/osgeo/polygon-create.png";
	private static final String BTN_ADD_IMG = "[ISOMORPHIC]/geomajas/osgeo/selected-add.png";

	private MapWidget mapWidget;
	private DynamicForm frmBuffer;
	private Geometry geometry;

	public FreeDrawingSearch() {
		super();
	}

	public void initialize(MapWidget map) {
		this.mapWidget = map;
	}

	public String getTitle() {
		return messages.geometricSearchWidgetFreeDrawingSearchTitle();
	}

	public Geometry getGeometry() {
		return geometry;
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

		Label titleBar = new Label(messages.geometricSearchWidgetFreeDrawingSearchTitle());
		titleBar.setBackgroundColor("#E0E9FF");
		titleBar.setWidth100();
		titleBar.setHeight(20);
		titleBar.setPadding(5);

		HLayout geomsButtonBar = new HLayout();
		geomsButtonBar.setWidth100();
		geomsButtonBar.setAutoHeight();
		geomsButtonBar.setMembersMargin(10);

		IButton btnPoint = new IButton(messages.geometricSearchWidgetFreeDrawingPoint());
		btnPoint.setIcon(BTN_POINT_CREATE_IMG);
		btnPoint.setAutoFit(true);
		btnPoint.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDrawPoint();
			}
		});

		IButton btnLine = new IButton(messages.geometricSearchWidgetFreeDrawingLine());
		btnLine.setIcon(BTN_LINE_CREATE_IMG);
		btnLine.setAutoFit(true);
		btnPoint.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDrawLine();
			}
		});

		IButton btnPolygon = new IButton(messages.geometricSearchWidgetFreeDrawingPolygon());
		btnPolygon.setIcon(BTN_POLYGON_CREATE_IMG);
		btnPolygon.setAutoFit(true);
		btnPoint.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDrawPolygon();
			}
		});

		IButton btnAdd = new IButton(messages.geometricSearchWidgetFreeDrawingAdd());
		btnAdd.setIcon(BTN_ADD_IMG);
		btnAdd.setAutoFit(true);
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onAdd();
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

		geomsButtonBar.addMember(btnPoint);
		geomsButtonBar.addMember(btnLine);
		geomsButtonBar.addMember(btnPolygon);

		mainLayout.addMember(titleBar);
		mainLayout.addMember(geomsButtonBar);
		mainLayout.addMember(frmBuffer);
		mainLayout.addMember(btnAdd);

		return mainLayout;
	}

	// ----------------------------------------------------------

	private void onDrawPoint() {
		// TODO
	}

	private void onDrawLine() {
		// TODO
	}

	private void onDrawPolygon() {
		// TODO
	}

	private void onAdd() {
		// TODO
	}

}
