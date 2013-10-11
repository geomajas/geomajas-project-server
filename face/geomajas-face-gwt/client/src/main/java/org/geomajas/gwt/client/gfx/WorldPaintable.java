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

package org.geomajas.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;

/**
 * <p>
 * A {@link Paintable} object that has the ability to also be drawn in world space (next to screen space). To
 * effectively draw your objects in world space, and have them move about automatically when navigating, you must not
 * only render them, but also add them to the <code>MapWidget</code>. This <code>MapWidget</code> will apply the correct
 * transformation matrix on the parent group of all world space objects, so they are drawn at the correct location.
 * </p>
 * <p>
 * To redraw the WorldPaintable objects, use the following piece of code:<br>
 * <code>mapWidget.render(mapWidget.getMapModel(), RenderGroup.PAN, RenderStatus.UPDATE);</code>
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api
public interface WorldPaintable extends Paintable {

	/**
	 * Get the object that represents the original location (usually a coordinate, bbox or geometry).
	 */
	Object getOriginalLocation();

	/**
	 * Perform a transformation from world space to pan space, so the location object can than be shown on the map,
	 * using the pan group.
	 * 
	 * @param transformer
	 *            The map's transformer.
	 */
	void transform(WorldViewTransformer transformer);

	/**
	 * Returns a preferably unique ID that identifies the object even after it is painted. This can later be used to
	 * update or delete it from the {@link GraphicsContext}.
	 *
	 * @return object id
	 */
	String getId();
}
