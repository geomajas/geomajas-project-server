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

package org.geomajas.smartgwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.action.MenuAction;
import org.geomajas.smartgwt.client.action.menu.ToggleSnappingAction;
import org.geomajas.smartgwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.map.layer.Layer;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.smartgwt.client.spatial.geometry.operation.InsertCoordinateOperation;
import org.geomajas.smartgwt.client.util.DistanceFormat;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.smartgwt.client.widget.MapWidget.RenderStatus;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * <p>
 * Controller that measures distances on the map, by clicking points. The actual distances are displayed in a label at
 * the top left of the map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class MeasureDistanceController extends AbstractSnappingController {

	private static final ShapeStyle LINE_STYLE_1 = new ShapeStyle("#FFFFFF", 0, "#FF9900", 1, 2);

	private static final ShapeStyle LINE_STYLE_2 = new ShapeStyle("#FFFFFF", 0, "#FF5500", 1, 2);

	private GfxGeometry distanceLine;

	private GfxGeometry lineSegment;

	private DistanceLabel label;

	private GeometryFactory factory;

	private float tempLength;

	private Menu menu;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public MeasureDistanceController(MapWidget mapWidget) {
		super(mapWidget);
		distanceLine = new GfxGeometry("measureDistanceLine");
		distanceLine.setStyle(LINE_STYLE_1);
		lineSegment = new GfxGeometry("measureDistanceLineSegment");
		lineSegment.setStyle(LINE_STYLE_2);
	}

	// -------------------------------------------------------------------------
	// GraphicsController interface:
	// -------------------------------------------------------------------------

	/** Create the context menu for this controller. */
	public void onActivate() {
		menu = new Menu();
		menu.addItem(new CancelMeasuringAction(this));
		Layer selectedLayer = mapWidget.getMapModel().getSelectedLayer();
		if (selectedLayer instanceof VectorLayer) {
			menu.addItem(new ToggleSnappingAction((VectorLayer) selectedLayer, this));
		}
		mapWidget.setContextMenu(menu);
	}

	/** Clean everything up. */
	public void onDeactivate() {
		onDoubleClick(null);
		menu.destroy();
		menu = null;
		mapWidget.setContextMenu(null);
		mapWidget.unregisterWorldPaintable(distanceLine);
		mapWidget.unregisterWorldPaintable(lineSegment);
	}

	/** Set a new point on the distance-line. */
	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			Coordinate coordinate = getWorldPosition(event);
			if (distanceLine.getOriginalLocation() == null) {
				distanceLine.setGeometry(getFactory().createLineString(new Coordinate[] { coordinate }));
				mapWidget.registerWorldPaintable(distanceLine);
				mapWidget.registerWorldPaintable(lineSegment);
				label = new DistanceLabel();
				label.setDistance(0, 0);
				label.animateMove(mapWidget.getWidth() - 130, 10);
			} else {
				Geometry geometry = (Geometry) distanceLine.getOriginalLocation();
				InsertCoordinateOperation op = new InsertCoordinateOperation(geometry.getNumPoints(), coordinate);
				geometry = op.execute(geometry);
				distanceLine.setGeometry(geometry);
				tempLength = (float) geometry.getLength();
				label.setDistance(tempLength, 0);
			}
			mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);
		}
	}

	/** Update the drawing while moving the mouse. */
	public void onMouseMove(MouseMoveEvent event) {
		if (isMeasuring() && distanceLine.getOriginalLocation() != null) {
			Geometry geometry = (Geometry) distanceLine.getOriginalLocation();
			Coordinate coordinate1 = geometry.getCoordinates()[distanceLine.getGeometry().getNumPoints() - 1];
			Coordinate coordinate2 = getWorldPosition(event);
			lineSegment.setGeometry(getFactory().createLineString(new Coordinate[] { coordinate1, coordinate2 }));
			mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);
			label.setDistance(tempLength, (float) ((Geometry) lineSegment.getOriginalLocation()).getLength());
		}
	}

	/** Stop the measuring, and remove all graphics from the map. */
	public void onDoubleClick(DoubleClickEvent event) {
		tempLength = 0;
		mapWidget.unregisterWorldPaintable(distanceLine);
		mapWidget.unregisterWorldPaintable(lineSegment);
		distanceLine.setGeometry(null);
		lineSegment.setGeometry(null);
		if (label != null) {
			label.destroy();
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private boolean isMeasuring() {
		return distanceLine.getGeometry() != null;
	}

	/**
	 * The factory can only be used after the MapModel has initialized, that is why this getter exists...
	 *
	 * @return geometry factory
	 */
	private GeometryFactory getFactory() {
		if (factory == null) {
			factory = mapWidget.getMapModel().getGeometryFactory();
		}
		return factory;
	}

	// -------------------------------------------------------------------------
	// Private classes:
	// -------------------------------------------------------------------------

	/**
	 * The label that shows the distances.
	 * 
	 * @author Pieter De Graef
	 */
	private class DistanceLabel extends Label {

		public DistanceLabel() {
			setParentElement(mapWidget);
			setValign(VerticalAlignment.TOP);
			setShowEdges(true);
			setWidth(120);
			setPadding(3);
			setLeft(mapWidget.getWidth() - 130);
			setTop(-80);
			setBackgroundColor("#FFFFFF");
			setAnimateTime(500);
		}

		public void setDistance(float totalDistance, float radius) {
			String total = DistanceFormat.asMapLength(mapWidget, totalDistance);
			String r = DistanceFormat.asMapLength(mapWidget, radius);
			String dist = I18nProvider.getMenu().getMeasureDistanceString(total, r);
			setContents("<div><b>" + I18nProvider.getMenu().distance() + "</b>:</div><div style='margin-top:5px;'>"
					+ dist + "</div>");
		}
	}

	/**
	 * Menu item that stop the measuring
	 * 
	 * @author Pieter De Graef
	 */
	private class CancelMeasuringAction extends MenuAction {

		private final MeasureDistanceController controller;

		public CancelMeasuringAction(final MeasureDistanceController controller) {
			super(I18nProvider.getMenu().cancelMeasuring(), WidgetLayout.iconQuit);
			this.controller = controller;
			setEnableIfCondition(new MenuItemIfFunction() {

				public boolean execute(Canvas target, Menu menu, MenuItem item) {
					return controller.isMeasuring();
				}
			});
		}

		public void onClick(MenuItemClickEvent event) {
			controller.onDoubleClick(null);
		}
	}
}
