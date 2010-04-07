/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.action.menu.ToggleSnappingAction;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.operation.InsertCoordinateOperation;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

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

	private GfxGeometry distanceLine;

	private GfxGeometry lineSegment;

	private DistanceLabel label;

	private ShapeStyle lineStyle1 = new ShapeStyle("#FFFFFF", 0, "#FF9900", 1, 2);

	private ShapeStyle lineStyle2 = new ShapeStyle("#FFFFFF", 0, "#FF5500", 1, 2);

	private GeometryFactory factory;

	private float tempLength;

	private Menu menu;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public MeasureDistanceController(MapWidget mapWidget) {
		super(mapWidget);
		distanceLine = new GfxGeometry("measureDistanceLine");
		distanceLine.setStyle(lineStyle1);
		lineSegment = new GfxGeometry("measureDistanceLineSegment");
		lineSegment.setStyle(lineStyle2);
		factory = mapWidget.getMapModel().getGeometryFactory();
	}

	// -------------------------------------------------------------------------
	// GraphicsController interface:
	// -------------------------------------------------------------------------

	/** Create the context menu for this controller. */
	public void onActivate() {
		menu = new Menu();
		menu.addItem(new CancelMeasuringAction(this));
		menu.addItem(new ToggleSnappingAction(mapWidget.getMapModel(), this));
		mapWidget.setContextMenu(menu);
	}

	/** Clean everything up. */
	public void onDeactivate() {
		onDoubleClick(null);
		menu.destroy();
		menu = null;
		mapWidget.setContextMenu(null);
		mapWidget.getWorldSpacePaintables().remove(distanceLine);
		mapWidget.getWorldSpacePaintables().remove(lineSegment);
	}

	/** Set a new point on the distance-line. */
	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			Coordinate coordinate = getWorldPosition(event);
			if (distanceLine.getOriginalLocation() == null) {
				distanceLine.setGeometry(factory.createLineString(new Coordinate[] { coordinate }));
				mapWidget.getWorldSpacePaintables().add(distanceLine);
				mapWidget.getWorldSpacePaintables().add(lineSegment);
				label = new DistanceLabel();
				label.setDistance(0, 0);
				label.animateMove(mapWidget.getWidth() - 130, 10);
			} else {
				Geometry geometry = (Geometry) distanceLine.getOriginalLocation();
				InsertCoordinateOperation op = new InsertCoordinateOperation(geometry.getNumPoints(), coordinate);
				distanceLine.setGeometry(op.execute(geometry));
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
			lineSegment.setGeometry(factory.createLineString(new Coordinate[] { coordinate1, coordinate2 }));
			mapWidget.render(mapWidget.getMapModel(), RenderGroup.VECTOR, RenderStatus.UPDATE);
			label.setDistance(tempLength, (float) lineSegment.getGeometry().getLength());
		}
	}

	/** Stop the measuring, and remove all graphics from the map. */
	public void onDoubleClick(DoubleClickEvent event) {
		mapWidget.render(distanceLine, RenderGroup.WORLD, RenderStatus.DELETE);
		mapWidget.render(lineSegment, RenderGroup.WORLD, RenderStatus.DELETE);
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
			String dist = I18nProvider.getMenu().getMeasureDistanceString(totalDistance, radius);
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
			super(I18nProvider.getMenu().cancelMeasuring(), "[ISOMORPHIC]/geomajas/quit.png");
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
