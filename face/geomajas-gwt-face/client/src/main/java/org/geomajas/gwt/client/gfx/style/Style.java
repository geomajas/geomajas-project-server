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

package org.geomajas.gwt.client.gfx.style;

/**
 * <p>
 * General interface for style objects.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface Style {

	/**
	 * Scale a style by a given value. This might be necessary when due to scale changes on the map, a style needs to
	 * look the same nonetheless. For example, when using the <code>ShapeStyle</code> for a shape, you may want the
	 * <code>stroke-width</code> to remain constant.
	 * 
	 * @param scale
	 *            The scale value. (will usually be the opposite of the map's scale value)
	 */
	void scale(double scale);
	
	Style clone();
}