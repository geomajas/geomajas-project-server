/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.SelectionType;
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
public class FreeDrawingSearch extends AbstractGeometricSearchMethod implements GeometryDrawHandler {

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	private DynamicForm frmBuffer;
	private SpinnerItem spiBuffer;
	private IButton btnUndo;
	private IButton btnRedo;
	
	private IButton btnPoint;
	private IButton btnLine;
	private IButton btnPolygon;
	
	private Geometry mergedGeom;
	private final ArrayList<Geometry> geometries = new ArrayList<Geometry>();
	private final ArrayList<Geometry> redoGeoms = new ArrayList<Geometry>();

	private GraphicsController originalController;
	private ParentDrawController drawController;

	public void initialize(MapWidget map, GeometryUpdateHandler handler) {
		super.initialize(map, handler);
		geometries.clear();
		drawController = new ParentDrawController(mapWidget, this);
		drawController.setEditMode(EditMode.INSERT_MODE);
		drawController.setMaxBoundsDisplayed(false);
	}

	public String getTitle() {
		return MESSAGES.geometricSearchWidgetFreeDrawingSearchTitle();
	}

	public void reset() {
		geometries.clear();
		frmBuffer.reset();
		btnUndo.setDisabled(true);
		btnRedo.setDisabled(true);
		removeFreeDrawingController();
	}

	public Canvas getSearchCanvas() {
		final VLayout mainLayout = new VLayout(20);
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setPadding(5);

		Label titleBar = new Label(MESSAGES.geometricSearchWidgetFreeDrawingSearchTitle());
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

		btnPoint = new IButton(MESSAGES.geometricSearchWidgetFreeDrawingPoint());
		btnPoint.setIcon(WidgetLayout.iconPointCreate);
		btnPoint.setActionType(SelectionType.RADIO);
		btnPoint.setRadioGroup("drawType");
		btnPoint.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDrawPoint();
			}
		});

		btnLine = new IButton(MESSAGES.geometricSearchWidgetFreeDrawingLine());
		btnLine.setIcon(WidgetLayout.iconLineCreate);
		btnLine.setActionType(SelectionType.RADIO);
		btnLine.setRadioGroup("drawType");
		btnLine.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDrawLine();
			}
		});

		btnPolygon = new IButton(MESSAGES.geometricSearchWidgetFreeDrawingPolygon());
		btnPolygon.setIcon(WidgetLayout.iconPolygonCreate);
		btnPolygon.setActionType(SelectionType.RADIO);
		btnPolygon.setRadioGroup("drawType");
		btnPolygon.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDrawPolygon();
			}
		});

		btnUndo = new IButton(MESSAGES.geometricSearchWidgetFreeDrawingUndo());
		btnUndo.setIcon(WidgetLayout.iconUndo);
		btnUndo.setDisabled(true);
		btnUndo.setShowDisabledIcon(false);
		btnUndo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onUndo();
			}
		});

		btnRedo = new IButton(MESSAGES.geometricSearchWidgetFreeDrawingRedo());
		btnRedo.setIcon(WidgetLayout.iconRedo);
		btnRedo.setDisabled(true);
		btnRedo.setShowDisabledIcon(false);
		btnRedo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onRedo();
			}
		});

		frmBuffer = new DynamicForm();
		frmBuffer.setWidth100();
		spiBuffer = new SpinnerItem();
		spiBuffer.setTitle(MESSAGES.geometricSearchWidgetBufferLabel());
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

		actionsButtonBar.addMember(btnUndo);
		actionsButtonBar.addMember(btnRedo);

		mainLayout.addMember(titleBar);
		mainLayout.addMember(geomsButtonBar);
		mainLayout.addMember(actionsButtonBar);
		mainLayout.addMember(frmBuffer);

		return mainLayout;
	}

	// ----------------------------------------------------------

	private void resetButtonState() {
		btnLine.deselect();
		btnPoint.deselect();
		btnPolygon.deselect();
	}
	
	private void onDrawPoint() {
		if (drawController.getController() != null) {
			btnPoint.select();
		}
		startDrawing(new PointDrawController(mapWidget, drawController, this));
	}

	private void onDrawLine() {
		if (drawController.getController() != null) {
			btnLine.select();
		}
		startDrawing(new LineStringDrawController(mapWidget, drawController, this));
	}

	private void onDrawPolygon() {
		if (drawController.getController() != null) {
			btnPolygon.select();
		}
		startDrawing(new PolygonDrawController(mapWidget, drawController, this));
	}

	private void onUndo() {
		if (geometries.size() > 0) {
			Geometry geom = geometries.remove(geometries.size() - 1);
			redoGeoms.add(geom);
			btnRedo.setDisabled(false);
			updateView();
		}
		if (geometries.size() == 0) {
			btnUndo.setDisabled(true);
		}
	}

	private void onRedo() {
		if (redoGeoms.size() > 0) {
			Geometry geom = redoGeoms.remove(redoGeoms.size() - 1);
			geometries.add(geom);
			if (geometries.size() == 1) {
				btnUndo.setDisabled(false);
			}
			updateView();
		}
		if (redoGeoms.size() == 0) {
			btnRedo.setDisabled(true);
		}
	}

	private void startDrawing(AbstractFreeDrawingController controller) {
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
		resetButtonState();
	}

	private void addNewGeometry(Geometry geom) {
		if (drawController.getController() != null) {
			if (!geom.isEmpty()
					&& geom.isValid()
					&& !geometries.contains(geom)) {

				geometries.add(geom);
				if (geometries.size() == 1) {
					btnUndo.setDisabled(false);
				}
				updateView();
				removeFreeDrawingController();
			} else {
				SC.say(MESSAGES.geometricSearchWidgetFreeDrawingInvalidGeometry());
				removeFreeDrawingController();
			}
		} else {
			SC.say(MESSAGES.geometricSearchWidgetFreeDrawingNothingDrawn());
		}
	}

	private void updateView() {
		if (geometries.size() > 0) {
			Integer buffer = (Integer) spiBuffer.getValue();
			if (buffer != 0) {
				SearchCommService.mergeAndBufferGeometries(geometries, buffer, new DataCallback<Geometry[]>() {
					public void execute(Geometry[] result) {
						updateGeometry(mergedGeom, result[1]);
						mergedGeom = result[1];
					}
				});
			} else {
				SearchCommService.mergeGeometries(geometries, new DataCallback<Geometry>() {
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

	public void onDraw(Geometry geometry) {
		addNewGeometry(geometry);
	}
}
