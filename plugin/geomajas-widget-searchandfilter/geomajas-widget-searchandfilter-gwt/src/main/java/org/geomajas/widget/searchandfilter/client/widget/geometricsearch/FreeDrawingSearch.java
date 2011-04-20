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

import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.CommService;
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
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 * @author Bruce Palmkoeck
 */
public class FreeDrawingSearch extends AbstractGeometricSearchMethod {

	private SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private static final String BTN_POINT_CREATE_IMG = "[ISOMORPHIC]/geomajas/osgeo/point-create.png";
	private static final String BTN_LINE_CREATE_IMG = "[ISOMORPHIC]/geomajas/osgeo/line-create.png";
	private static final String BTN_POLYGON_CREATE_IMG = "[ISOMORPHIC]/geomajas/osgeo/polygon-create.png";
	private static final String BTN_ADD_IMG = "[ISOMORPHIC]/geomajas/osgeo/selected-add.png";
	private static final String BTN_CANCEL_IMG = "[ISOMORPHIC]/geomajas/osgeo/undo.png";

	private DynamicForm frmBuffer;
	private SpinnerItem spiBuffer;
	private IButton btnRemoveLast;
	
	private Geometry mergedGeom;
	private final ArrayList<Geometry> geometries = new ArrayList<Geometry>();

	private GraphicsController originalController;
	private ParentDrawController drawController;

	public void initialize(MapWidget map, GeometryUpdateHandler handler) {
		super.initialize(map, handler);
		geometries.clear();
		drawController = new ParentDrawController(mapWidget);
		drawController.setEditMode(EditMode.INSERT_MODE);
		drawController.setMaxBoundsDisplayed(false);
	}

	public String getTitle() {
		return messages.geometricSearchWidgetFreeDrawingSearchTitle();
	}

	public void reset() {
		geometries.clear();
		frmBuffer.reset();
		btnRemoveLast.setDisabled(true);
		removeFreeDrawingController();
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
		HLayout actionsButtonBar = new HLayout();
		actionsButtonBar.setWidth100();
		actionsButtonBar.setAutoHeight();
		actionsButtonBar.setMembersMargin(10);

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
		btnLine.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDrawLine();
			}
		});

		IButton btnPolygon = new IButton(messages.geometricSearchWidgetFreeDrawingPolygon());
		btnPolygon.setIcon(BTN_POLYGON_CREATE_IMG);
		btnPolygon.setAutoFit(true);
		btnPolygon.addClickHandler(new ClickHandler() {
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

		btnRemoveLast = new IButton(messages.geometricSearchWidgetFreeDrawingRemoveLast());
		btnRemoveLast.setIcon(BTN_CANCEL_IMG);
		btnRemoveLast.setAutoFit(true);
		btnRemoveLast.setDisabled(true);
		btnRemoveLast.setShowDisabledIcon(false);
		btnRemoveLast.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onRemoveLast();
			}
		});

		frmBuffer = new DynamicForm();
		frmBuffer.setWidth100();
		spiBuffer = new SpinnerItem();
		spiBuffer.setTitle(messages.geometricSearchWidgetBufferLabel());
		spiBuffer.setDefaultValue(5);
		spiBuffer.setMin(0);
		spiBuffer.setWidth(60);
		spiBuffer.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				updateView();
			}
		});
		frmBuffer.setFields(spiBuffer);

		// ----------------------------------------------------------

		geomsButtonBar.addMember(btnPoint);
		geomsButtonBar.addMember(btnLine);
		geomsButtonBar.addMember(btnPolygon);
		
		actionsButtonBar.addMember(btnAdd);
		actionsButtonBar.addMember(btnRemoveLast);

		mainLayout.addMember(titleBar);
		mainLayout.addMember(geomsButtonBar);
		mainLayout.addMember(frmBuffer);
		mainLayout.addMember(actionsButtonBar);

		return mainLayout;
	}

	// ----------------------------------------------------------

	private void onDrawPoint() {
		if (drawController.getController() != null) {
			addNewGeometry();
		}
		startDrawing(new PointDrawController(mapWidget, drawController));
	}

	private void onDrawLine() {
		if (drawController.getController() != null) {
			addNewGeometry();
		}
		startDrawing(new LineStringDrawController(mapWidget, drawController));
	}

	private void onDrawPolygon() {
		if (drawController.getController() != null) {
			addNewGeometry();
		}
		startDrawing(new PolygonDrawController(mapWidget, drawController));
	}

	private void onAdd() {
		addNewGeometry();
	}
	
	private void onRemoveLast() {
		if (geometries.size() > 0) {
			geometries.remove(geometries.size() - 1);
			updateView();
		}
		if (geometries.size() == 0) {
			btnRemoveLast.setDisabled(true);
		}
	}

	private void startDrawing(FreeDrawingController controller) {
		originalController = mapWidget.getController();
		drawController.setController(controller);
		mapWidget.setController(drawController);
		mapWidget.getMapModel().getFeatureEditor().startEditing(null, null);
	}

	private void removeFreeDrawingController() {
		if (drawController != null) {
			drawController.cleanup();
			mapWidget.setController(originalController);
		}
	}

	private void addNewGeometry() {
		if (drawController.getController() != null) {
			Geometry geom = drawController.getController().getGeometry();
			if (!geom.isEmpty()
					&& geom.isValid()
					&& !geometries.contains(geom)) {
	
				geometries.add(geom);
				if (geometries.size() == 1) {
					btnRemoveLast.setDisabled(false);
				}
				updateView();
				removeFreeDrawingController();
			} else {
				removeFreeDrawingController();
				SC.say(messages.geometricSearchWidgetFreeDrawingInvalidGeometry());
			}
		} else {
			SC.say(messages.geometricSearchWidgetFreeDrawingNothingDrawn());
		}
	}
	
	private void updateView() {
		if (geometries.size() > 0) {
			Integer buffer = (Integer) spiBuffer.getValue();
			if (buffer != 0) {
				CommService.mergeAndBufferGeometries(geometries, buffer, new DataCallback<Geometry[]>() {
					public void execute(Geometry[] result) {
						updateGeometry(mergedGeom, result[1]);
						mergedGeom = result[1];
					}
				});
			} else {
				CommService.mergeGeometries(geometries, new DataCallback<Geometry>() {
					public void execute(Geometry result) {
						updateGeometry(mergedGeom, result);
						mergedGeom = result;
					}
				});
			}
		} else {
			updateGeometry(mergedGeom, null);
			mergedGeom = null;
		}
	}
}
