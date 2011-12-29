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

package org.geomajas.puregwt.client.map.render;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;

/**
 * Representation of a single scale level for a certain layer. Implementations should focus on getting the required
 * tiles from the server and displaying them. No general transformation should be applied, as that is the task for the
 * {@link MapScalesRenderer}.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface TiledScaleRenderer {

	/**
	 * Get the scale represented by this scale presenter.
	 * 
	 * @return The scale represented by this object.
	 */
	double getScale();

	/**
	 * Return the container wherein all tiles are rendered.
	 * 
	 * @return The container wherein all tiles are rendered.
	 */
	HtmlContainer getHtmlContainer();

	/**
	 * Render the required tiles for the given bounding box. This bounding box is assumed to be in the scale represented
	 * by this object.
	 * 
	 * @param bounds
	 *            The bounding box to display.
	 */
	void render(final Bbox bounds);

	/** Cancel rendering. If there are any requests underway to the server, these can all be canceled. */
	void cancel();

	/**
	 * Method called when the request(s) for tiles comes back from the server. We now know which tiles to render or
	 * where to get the images. The tiles themselves are not rendered yet.
	 * 
	 * @param container
	 *            The container wherein to render the tiles.
	 * @param scale
	 *            The scale of this presenter.
	 */
	void onTilesReceived(HtmlContainer container, double scale);

	/**
	 * Method called when all tiles are fully rendered. At this point the required bounding box as displayed.
	 * 
	 * @param container
	 *            The container wherein to render the tiles.
	 * @param scale
	 *            The scale of this presenter.
	 */
	void onTilesRendered(HtmlContainer container, double scale);
}