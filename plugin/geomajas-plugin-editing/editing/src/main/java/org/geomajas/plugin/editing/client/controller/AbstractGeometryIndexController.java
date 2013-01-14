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

package org.geomajas.plugin.editing.client.controller;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.snap.SnapService;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Abstract base controller for specific controllers used in the editing process.
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractGeometryIndexController extends AbstractController {

	protected final GeometryEditService service;

	protected final SnapService snappingService;

	protected boolean snappingEnabled;

	protected Bbox maxBounds;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance, setting all the necessary services.
	 * 
	 * @param service
	 *            The geometry edit service this controller uses.
	 * @param snappingService
	 *            The snapping service used for snapping locations.
	 * @param eventParser
	 *            The map event parser used for converting events positions into real world locations.
	 */
	public AbstractGeometryIndexController(GeometryEditService service, SnapService snappingService,
			MapEventParser eventParser) {
		super(eventParser, service.getEditingState() == GeometryEditState.DRAGGING);
		this.service = service;
		this.snappingService = snappingService;
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Get the real world location of the event, while making sure it is within the maximum bounds. If no maximum bounds
	 * have been set, the original event location in world space is returned.
	 * 
	 * @param event
	 *            The event to extract the location from.
	 * @return The location within maximum bounds.
	 */
	public Coordinate getLocationWithinMaxBounds(HumanInputEvent<?> event) {
		Coordinate location = getLocation(event, RenderSpace.WORLD);
		location = getLocationWithinMaxBounds(location);
		return location;
	}

	/**
	 * Get the snapped real world location of the event, while making sure it is within the maximum bounds. If no
	 * maximum bounds have been set, the snapped location of the event in world space is returned.
	 * 
	 * @param event
	 *            The event to extract the location from.
	 * @return The location within maximum bounds.
	 */
	public Coordinate getSnappedLocationWithinMaxBounds(HumanInputEvent<?> event) {
		Coordinate location = getLocation(event, RenderSpace.WORLD);
		location = getLocationWithinMaxBounds(location);
		if (snappingEnabled) {
			location = snappingService.snap(location);
		}
		return location;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Should this controller make use of snapping or not?
	 * 
	 * @return true or false.
	 */
	public boolean isSnappingEnabled() {
		return snappingEnabled;
	}

	/**
	 * Determine whether or not this controller should make use of snapping.
	 * 
	 * @param snappingEnabled
	 *            True or false.
	 */
	public void setSnappingEnabled(boolean snappingEnabled) {
		this.snappingEnabled = snappingEnabled;
	}

	/**
	 * Get an optional maximum bounds wherein all positions should stay.
	 * 
	 * @return An optional maximum bounds.
	 */
	public Bbox getMaxBounds() {
		return maxBounds;
	}

	/**
	 * Apply an optional maximum bounds wherein all positions should stay.
	 * 
	 * @param maxBounds
	 *            The maximum bounds, or null if no restrictions should be placed on locations during editing (null is
	 *            the default value).
	 */
	public void setMaxBounds(Bbox maxBounds) {
		this.maxBounds = maxBounds;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Get a location, derived from the original, that is sure to be within the maximum bounds. If no maximum bounds
	 * have been set, a clone of the original is returned.
	 * 
	 * @param original
	 *            The original location.
	 * @return The derived location within the maximum bounds.
	 */
	private Coordinate getLocationWithinMaxBounds(Coordinate original) {
		double x = original.getX();
		double y = original.getY();
		if (maxBounds != null) {
			if (original.getX() < maxBounds.getX()) {
				x = maxBounds.getX();
			} else if (original.getX() > maxBounds.getMaxX()) {
				x = maxBounds.getMaxX();
			}
			if (original.getY() < maxBounds.getY()) {
				y = maxBounds.getY();
			} else if (original.getY() > maxBounds.getMaxY()) {
				y = maxBounds.getMaxY();
			}
		}
		return new Coordinate(x, y);
	}
}