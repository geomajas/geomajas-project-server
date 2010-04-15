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

package org.geomajas.gwt.client.gfx;

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
 */
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
	
	String getId();
}
