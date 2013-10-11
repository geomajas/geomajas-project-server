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

package org.geomajas.gwt.client.map.layer;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerStyleChangedHandler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * The most basic interface for a layer on the client. Since we will want to
 * paint layers on the map, it implements the {@link PaintableGroup} interface.
 * </p>
 * 
 * @param <T>
 *            layer info type, {@link ClientLayerInfo}
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api
public interface Layer<T extends ClientLayerInfo> extends PaintableGroup {

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	/**
	 * Return this layer's client ID.
	 * 
	 * @return id of the client layer
	 */
	String getId();

	/**
	 * Return this layer's server ID.
	 * 
	 * @return id of the server layer
	 */
	String getServerLayerId();

	// -------------------------------------------------------------------------
	// Layer status functions:
	// -------------------------------------------------------------------------

	/**
	 * Return the layer's label. The difference between the ID and the label, is
	 * that the ID is used behind the screens, while the label is the visible
	 * name to the user.
	 * 
	 * @return label
	 */
	String getLabel();

	/**
	 * Forces this layer to update its showing status.
	 */
	void updateShowing();

	/**
	 * Forces this layer to update its style and notifies all {@link LayerStyleChangedHandler}s.
	 */
	void updateStyle();
	
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
	 * @param selected selected status
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
	 * @deprecated Use {#isLabelsShowing()} instead
	 * 
	 * @return True if labels are showing, false otherwise ; note that if the
	 *         layer is not visible the labels are automatically invisible
	 */
	@Deprecated
	boolean isLabeled();

	/**
	 * Returns true if labels are currently showing (eg. isShowing == true && isLabelsVisible == true).
	 *
	 * @return true when labels are enabled and the layer is showing
	 * @since 1.10.0
	 */
	@Api
	boolean isLabelsShowing();

	/**
	 * Get the showing status of this layer.
	 * 
	 * @return The showing status of this layer
	 */
	boolean isShowing();

	/**
	 * Updates the visible status for this layer, this doesn't automicly means
	 * that the showing status is updated as well.
	 * 
	 * @param visible
	 *            The wished visible status
	 */
	void setVisible(boolean visible);

	/**
	 * Add handlers for {@link LayerChangedHandler}s. These events occur when
	 * the layer changes it's visible or labeled values.
	 * 
	 * @param handler handler to register
	 * @return Returns the handler registration object.
	 */
	HandlerRegistration addLayerChangedHandler(LayerChangedHandler handler);

	/**
	 * Add handlers for {@link LayerStyleChangedHandler}s. These events occur
	 * when the layer style changes.
	 * 
	 * @param handler handler to register
	 * @return Returns the handler registration object.
	 * @since 1.8.0
	 */
	HandlerRegistration addLayerStyleChangedHandler(LayerStyleChangedHandler handler);
}