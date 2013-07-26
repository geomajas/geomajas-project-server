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

package org.geomajas.gwt.client.map.render;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.event.ScaleLevelRenderedHandler;
import org.geomajas.gwt.client.gfx.HtmlContainer;
import org.geomajas.gwt.client.map.layer.Layer;

import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Scale based renderer for the map. It provides the rendering through specific renderers for each required scale.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface LayerRenderer {

	/**
	 * Get the target layer for this renderer.
	 * 
	 * @return The layer for this renderer.
	 */
	Layer getLayer();

	/**
	 * Add a handler for {@link org.geomajas.gwt.client.map.render.event.ScaleLevelRenderedEvent}s. At some point
	 * after a call to the {@link #ensureScale(double, Bbox)} method, that particular scale level will be rendered (i.e.
	 * all tiles have arrived). These handlers will be notified of that event.
	 * 
	 * @param handler
	 *            The event handler.
	 * @return A registration to the handler.
	 */
	HandlerRegistration addScaleLevelRenderedHandler(ScaleLevelRenderedHandler handler);

	/**
	 * Get the HTML container wherein all scales should be rendered.
	 * 
	 * @return The HTML container.
	 */
	HtmlContainer getHtmlContainer();

	/**
	 * Ensure that the requested scale becomes available and fully rendered as soon as possible.
	 * 
	 * @param scale
	 *            The requested scale.
	 * @param bounds
	 *            Bounding box to focus on when trying to build the scale rendering.
	 */
	void ensureScale(double scale, Bbox bounds);

	/**
	 * Bring the requested scale to the front. The scale level may still be invisible though, so make sure to also set
	 * the visibility.
	 * 
	 * @param scale
	 *            The requested scale.
	 */
	void bringScaleToFront(double scale);

	/**
	 * Determine the visibility of a certain scale level.
	 * 
	 * @param scale
	 *            The scale level to make visible. Make sure to first call {@link #ensureScale(double, Bbox)}. Otherwise
	 *            this scale level might not be available to display or hide.
	 * @param visible
	 *            Show or hide the scale level?
	 */
	void setScaleVisibility(double scale, boolean visible);

	/**
	 * Apply a translation onto a certain scale level.
	 * 
	 * @param scale
	 *            The scale level to translate.
	 * @param translation
	 *            The translation vector.
	 */
	void applyScaleTranslation(double scale, Coordinate translation);

	/**
	 * Cancel rendering. When ensuring a certain scale level, often the rendering needs to be fetched from the server.
	 * This cancel might stop that request. In any case, this method prevents the workings of the
	 * {@link #ensureScale(double, Bbox)} method.
	 */
	void cancel();

	/**
	 * Get the currently visible scale. This will return the scale that was lastly made visible (more than one scale
	 * might be visible at any time).
	 * 
	 * @return The currently visible scale.
	 */
	LayerScaleRenderer getVisibleScale();

	/**
	 * Get the renderer for a specific scale level.
	 * 
	 * @param scale
	 *            The scale to get a renderer for.
	 * @return Returns the renderer, or null.
	 */
	LayerScaleRenderer getScale(double scale);

	/** Clear all contents within this object. */
	void clear();
}