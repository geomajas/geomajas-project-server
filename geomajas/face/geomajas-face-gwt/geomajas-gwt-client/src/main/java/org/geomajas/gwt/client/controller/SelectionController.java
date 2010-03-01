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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.gwt.client.action.menu.DeselectAllAction;
import org.geomajas.gwt.client.action.menu.ToggleSelectionAction;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.widgets.menu.Menu;

/**
 * Allow selection of features, either by clicking on individual features, or by dragging a rectangle and selecting all
 * features which (for a certain percentage) fall into the indicated area.
 * 
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
public class SelectionController extends RectangleController {

	private boolean shiftOrCtrl;

	private Menu menu;

	private int clickTimeout; // timeout in milliseconds for handling as click versus dragging

	private float coverageRatio; // coverage percentage which is used to determine a feature as selected

	private boolean priorityToSelectedLayer; // give priority to the selected layer on when toggling

	private int pixelTolerance;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a new selection controller, that controls selection on the given map through user interaction.
	 * 
	 * @param mapWidget
	 *            The map onto whom you want to control selection.
	 * @param clickTimeout
	 *            Timeout in milliseconds for handling as click versus dragging.
	 * @param coverageRatio
	 *            Coverage percentage which is used to determine a feature as selected. This is only used when dragging
	 *            a rectangle to select in. Must be a floating value between 0 and 1.
	 * @param priorityToSelectedLayer
	 *            Activate or disable priority to the selected layer. This works only if there is a selected layer, and
	 *            that selected layer is a {@link VectorLayer}. In all other cases, the selection toggle will occur on
	 *            the first object that is encountered. In other words it will depend on the layer drawing order,
	 *            starting at the top.
	 * @param pixelTolerance
	 *            Number of pixels that describes the tolerance allowed when trying to select features.
	 */
	public SelectionController(MapWidget mapWidget, int clickTimeout, float coverageRatio,
			boolean priorityToSelectedLayer, int pixelTolerance) {
		super(mapWidget);

		this.clickTimeout = clickTimeout;
		this.coverageRatio = coverageRatio;
		this.priorityToSelectedLayer = priorityToSelectedLayer;
		this.pixelTolerance = pixelTolerance;
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	@Override
	public void onActivate() {
		menu = new Menu();
		menu.addItem(new ToggleSelectionAction(mapWidget, pixelTolerance));
		menu.addItem(new DeselectAllAction(mapWidget.getMapModel()));
		mapWidget.setContextMenu(menu);
	}

	@Override
	public void onDeactivate() {
		onDoubleClick(null);
		menu.destroy();
		menu = null;
		mapWidget.setContextMenu(null);
		if (dragging) {
			dragging = false;
			mapWidget.render(rectangle, "delete");
		}
	}

	/**
	 * First mouse button: publish a toggle event on the selection topic for the referenced MapWidget. Second mouse
	 * button opens a menu.
	 * 
	 * @param event
	 *            event
	 */
	@Override
	public void onMouseUp(MouseUpEvent event) {
		// handle click if any?
		if (dragging) {
			// shift or CTRL is used when depressed either at beginning or end:
			shiftOrCtrl = (event.isShiftKeyDown() || event.isControlKeyDown());

			if (timestamp + clickTimeout > new Date().getTime()) {
				stopDragging();
				// click behavior instead of drag
				ToggleSelectionAction action = new ToggleSelectionAction(mapWidget, priorityToSelectedLayer,
						pixelTolerance);
				action.toggle(getScreenPosition(event), !shiftOrCtrl);
				return;
			}
		}
		// normal "rectangle" handling
		super.onMouseUp(event);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Coverage percentage which is used to determine a feature as selected. This is only used when dragging a rectangle
	 * to select in. Must be a floating value between 0 and 1.
	 */
	public float getCoverageRatio() {
		return coverageRatio;
	}

	/**
	 * Set a new coverage percentage which is used to determine a feature as selected. This is only used when dragging a
	 * rectangle to select in. Must be a floating value between 0 and 1.
	 */
	public void setCoverageRatio(float coverageRatio) {
		this.coverageRatio = coverageRatio;
	}

	/**
	 * Will priority be given to the selected layer? This works only if there is a selected layer, and that selected
	 * layer is a {@link VectorLayer}. In all other cases, the selection toggle will occur on the first object that is
	 * encountered. In other words it will depend on the layer drawing order, starting at the top.
	 */
	public boolean isPriorityToSelectedLayer() {
		return priorityToSelectedLayer;
	}

	/**
	 * Activate or disable priority to the selected layer. This works only if there is a selected layer, and that
	 * selected layer is a {@link VectorLayer}. In all other cases, the selection toggle will occur on the first object
	 * that is encountered. In other words it will depend on the layer drawing order, starting at the top.
	 */
	public void setPriorityToSelectedLayer(boolean priorityToSelectedLayer) {
		this.priorityToSelectedLayer = priorityToSelectedLayer;
	}

	/**
	 * Number of pixels that describes the tolerance allowed when trying to select features.
	 */
	public int getPixelTolerance() {
		return pixelTolerance;
	}

	/**
	 * Number of pixels that describes the tolerance allowed when trying to select features.
	 * 
	 * @param pixelTolerance
	 *            The new value.
	 */
	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}

	// -------------------------------------------------------------------------
	// Private and protected methods:
	// -------------------------------------------------------------------------

	@Override
	protected void selectRectangle(Bbox selectedArea) {
		Layer<?> layer = mapWidget.getMapModel().getSelectedLayer();
		if (layer != null && layer instanceof VectorLayer) {
			GwtCommand commandRequest = new GwtCommand("command.feature.SearchByLocation");
			SearchByLocationRequest request = new SearchByLocationRequest();
			request.setLayerIds(new String[] { layer.getId() });
			Polygon polygon = mapWidget.getMapModel().getGeometryFactory().createPolygon(selectedArea);
			request.setLocation(GeometryConverter.toDto(polygon));
			request.setCrs(mapWidget.getMapModel().getCrs());
			request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
			request.setRatio(coverageRatio);
			request.setSearchType(SearchByLocationRequest.SEARCH_FIRST_LAYER);
			commandRequest.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

				public void execute(CommandResponse commandResponse) {
					if (commandResponse instanceof SearchByLocationResponse) {
						SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
						Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = response.getFeatureMap();
						for (String layerId : featureMap.keySet()) {
							selectFeatures(featureMap.get(layerId));
						}
					}
				}
			});
		}
	}

	private void selectFeatures(List<org.geomajas.layer.feature.Feature> orgFeatures) {
		MapModel mapModel = mapWidget.getMapModel();
		Layer<?> layer = mapWidget.getMapModel().getSelectedLayer();
		if (layer != null && layer instanceof VectorLayer) {
			VectorLayer vectorLayer = (VectorLayer) layer;
			if (!shiftOrCtrl) {
				mapModel.clearSelectedFeatures();
			}
			for (org.geomajas.layer.feature.Feature orgFeature : orgFeatures) {
				Feature feature = new Feature(orgFeature, vectorLayer);
				mapModel.selectFeature(feature);
			}
		}
	}
}
