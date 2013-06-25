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

package org.geomajas.puregwt.client.gfx;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.puregwt.client.controller.MapController;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Utility class concerning custom graphics rendering on the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface GfxUtil {

	/**
	 * Apply the stroke style to the given object. Currently supports {@link org.vaadin.gwtgraphics.client.Strokeable}
	 * and {@link org.vaadin.gwtgraphics.client.Group} containing {@link org.vaadin.gwtgraphics.client.Strokeable}.
	 * 
	 * @param object
	 * @param strokeColor
	 * @param strokeOpacity
	 * @param strokeWidth
	 * @param dashArray
	 */
	void applyStroke(VectorObject object, String strokeColor, double strokeOpacity, int strokeWidth, String dashArray);

	/**
	 * Apply the fill style to the given object. Currently supports {@link org.vaadin.gwtgraphics.client.Shape} and
	 *        {@link org.vaadin.gwtgraphics.client.Group} containing {@link org.vaadin.gwtgraphics.client.Shape}.
	 * 
	 * @param object
	 * @param fillColor
	 * @param fillOpacity
	 */
	void applyFill(VectorObject object, String fillColor, double fillOpacity);

	/**
	 * Apply the given controller onto the given object.
	 * 
	 * @param shape The object in need of a controller.
	 * @param mapController The controller to apply onto the object.
	 * @return The list of registrations that allows for removing the controller again.
	 */
	List<HandlerRegistration> applyController(VectorObject object, MapController mapController);

	/**
	 * Transform the given geometry into an object that can be drawn on the map.
	 * 
	 * @param geometry The geometry to transform.
	 * @return The equivalent path object.
	 */
	VectorObject toShape(Geometry geometry);
}