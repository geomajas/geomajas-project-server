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

package org.geomajas.puregwt.client.map.layer;

import java.util.List;

import org.geomajas.annotation.Api;

/**
 * The basic definition of a layer within the GWT client.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface Layer {

	/**
	 * Return this layer's client ID.
	 * 
	 * @return id of the client layer
	 */
	String getId();

	/**
	 * Return the layer's title. The difference between the ID and the title, is that the ID is used behind the screens,
	 * while the title is the visible name to the user.
	 * 
	 * @return
	 */
	String getTitle();

	/**
	 * Mark this layer as selected or not. Only one layer can be selected at any given time within a map.
	 * 
	 * @param selected
	 *            The boolean value that indicates whether or not to select this layer.
	 */
	void setSelected(boolean selected);

	/**
	 * Is this layer currently marked as selected or not?
	 * 
	 * @return Returns true or false.
	 */
	boolean isSelected();

	/**
	 * Mark this layer as visible or invisible. This may toggle the visibility flag, but does not guarantee that the
	 * layer be visible. This is because other factors might intrude upon the layer visibility, such as the minimum and
	 * maximum scales between which a layer can be visible.
	 * 
	 * @param markedAsVisible
	 *            Should the layer be marked as visible or invisible?
	 */
	void setMarkedAsVisible(boolean markedAsVisible);

	/**
	 * Has the layer been marked as visible?
	 * 
	 * @return True or false.
	 */
	boolean isMarkedAsVisible();

	/**
	 * Is the layer currently showing on the map or not? In other words, can we actually look at the layer on the map? A
	 * layer may be marked as visible but other factors (such as scale) may intrude upon the actual visibility of a
	 * layer.<br/>
	 * This value will return the final result of all these factors and clearly state whether or not the layer can be
	 * seen.
	 * 
	 * @return true or false.
	 */
	boolean isShowing();

	/** Refresh this layer on the map. This method will completely erase the current contents and redraw. */
	void refresh();
	
	/**
	 * Update the style of the layer. This will notify all stylechange listeners.
	 */
	void updateStyle();

	/**
	 * Get the full list of style presenters for this layer. Each style presenters represents a supported style for this
	 * layer.
	 * 
	 * @return The full (and ordered) list of style presenters for this layer.
	 */
	List<LayerStylePresenter> getStylePresenters();
}