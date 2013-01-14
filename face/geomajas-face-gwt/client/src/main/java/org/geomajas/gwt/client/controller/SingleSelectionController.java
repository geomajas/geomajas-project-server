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

package org.geomajas.gwt.client.controller;

import org.geomajas.gwt.client.action.menu.ToggleSelectionAction;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Controller that allows the selection of only one single feature. Whenever the user clicks on the map, if there is an
 * underlying feature, it will be selected and all other features will be deselected.
 * 
 * @author Pieter De Graef
 */
public class SingleSelectionController extends AbstractGraphicsController {

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
	 * @param priorityToSelectedLayer
	 *            Activate or disable priority to the selected layer. This works only if there is a selected layer, and
	 *            that selected layer is a {@link org.geomajas.gwt.client.map.layer.VectorLayer}. In all other cases,
	 *            the selection toggle will occur on the first object that is encountered. In other words it will depend
	 *            on the layer drawing order, starting at the top.
	 * @param pixelTolerance
	 *            Number of pixels that describes the tolerance allowed when trying to select features.
	 */
	public SingleSelectionController(MapWidget mapWidget, boolean priorityToSelectedLayer, int pixelTolerance) {
		super(mapWidget);

		this.priorityToSelectedLayer = priorityToSelectedLayer;
		this.pixelTolerance = pixelTolerance;
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	/**
	 * Toggle selection at the event location.
	 * 
	 * @param event
	 *            event
	 */
	public void onUp(HumanInputEvent<?> event) {
		if (!isRightMouseButton(event)) {
			ToggleSelectionAction action = new ToggleSelectionAction(mapWidget, pixelTolerance);
			action.setPriorityToSelectedLayer(priorityToSelectedLayer);
			action.toggleSingle(getLocation(event, RenderSpace.SCREEN));
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

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
}