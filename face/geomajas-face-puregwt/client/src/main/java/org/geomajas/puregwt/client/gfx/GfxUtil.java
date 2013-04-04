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
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.puregwt.client.controller.MapController;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Path;

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
	 * Apply the main elements of the given style onto the shape.
	 * 
	 * @param shape
	 *            The shape in need of styling.
	 * @param style
	 *            The style to apply. Only the following elements are applied: fill color, fill opacity, stroke color,
	 *            stroke opacity and stroke width.
	 */
	void applyStyle(Shape shape, FeatureStyleInfo style);

	/**
	 * Apply the given controller onto the given shape.
	 * 
	 * @param shape
	 *            The shape in need of a controller.
	 * @param mapController
	 *            The controller to apply onto the shape.
	 * @return The list of registrations that allows for removing the controller again.
	 */
	List<HandlerRegistration> applyController(Shape shape, MapController mapController);

	/**
	 * Transform the given geometry into a path object that can be drawn on the map.
	 * 
	 * @param geometry
	 *            The geometry to transform.
	 * @return The quivalent path object.
	 */
	Path toPath(Geometry geometry);
}