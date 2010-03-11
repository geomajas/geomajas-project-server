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

package org.geomajas.gwt.client.map.layer;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * The most basic interface for a layer on the client. Since we will want to paint layers on the map, it implements the
 * <code>Paintable</code> interface.
 * </p>
 *
 * @param <T> layer info type, {@link ClientLayerInfo}
 *
 * @author Pieter De Graef
 */
public interface Layer<T extends ClientLayerInfo> extends PaintableGroup {

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	/**
	 * Return this layer's client ID.
	 *
	 * @return
	 */
	String getId();

	// -------------------------------------------------------------------------
	// Layer status functions:
	// -------------------------------------------------------------------------

	/**
	 * Return the layer's label. The difference between the ID and the label, is that the ID is used behind the screens,
	 * while the label is the visible name to the user.
	 *
	 * @return
	 */
	String getLabel();

	/**
	 * Forces this layer to update its showing status.
	 */
	void updateShowing();

	/**
	 * Is this layer currently selected or not?
	 *
	 * @return true if selected, false otherwise
	 */
	boolean isSelected();

	/**
	 * Return the map model for this layer.
	 *
	 * @return the map model
	 */
	MapModel getMapModel();

	/**
	 * Return the info for this layer.
	 *
	 * @return the info
	 */
	T getLayerInfo();

	/**
	 * Set the selected status of the layer.
	 *
	 * @param selected
	 */
	void setSelected(boolean selected);

	/**
	 * Set the labeled status for this layer.
	 *
	 * @param labeled
	 *            true if labels should be shown
	 */
	void setLabeled(boolean labeled);

	/**
	 * Get the labeled status for this layer.
	 *
	 * @return True if labels are visible, false otherwise ; note that if the layer is visible the labels are automicly
	 *         invisible
	 */
	boolean isLabeled();

	/**
	 * Get the showing status of this layer.
	 *
	 * @return The showing status of this layer
	 */
	boolean isShowing();

	/**
	 * Updates the visible status for this layer, this doesn't automicly means that the showing status is updated as
	 * well.
	 *
	 * @param visible
	 *            The wished visible status
	 */
	void setVisible(boolean visible);

	/**
	 * Add handlers for {@link LayerChangedEvent}s. These events occur when the layer changes it's visible or labeled
	 * values.
	 *
	 * @param handler
	 * @return
	 */
	HandlerRegistration addLayerChangedHandler(LayerChangedHandler handler);
}