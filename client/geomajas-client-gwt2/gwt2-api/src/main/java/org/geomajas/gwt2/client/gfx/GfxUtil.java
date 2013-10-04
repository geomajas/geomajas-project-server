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

package org.geomajas.gwt2.client.gfx;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt2.client.controller.MapController;
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
	 * @param object object to stroke
	 * @param strokeColor the stroke color, use CSS2 color notation (#FFFFFF)
	 * @param strokeOpacity the stroke opacity, between 0.0 for fully transparent and 1.0 for fully opaque
	 * @param strokeWidth the stroke width in pixels
	 * @param dashArray whitespace separated list of dashes and gap lengths. If null, the property is omitted/removed.
	 */
	void applyStroke(VectorObject object, String strokeColor, double strokeOpacity, int strokeWidth, String dashArray);

	/**
	 * Apply the fill style to the given object. Currently supports {@link org.vaadin.gwtgraphics.client.Shape} and
	 *        {@link org.vaadin.gwtgraphics.client.Group} containing {@link org.vaadin.gwtgraphics.client.Shape}.
	 * 
	 * @param object object to fill
	 * @param fillColor the fill color, use CSS2 color notation (#FFFFFF)
	 * @param fillOpacity the fill opacity, between 0.0 for fully transparent and 1.0 for fully opaque
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