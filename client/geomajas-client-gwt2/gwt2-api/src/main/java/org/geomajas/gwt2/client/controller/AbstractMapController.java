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

package org.geomajas.gwt2.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * <p>
 * Extension of the generic {@link AbstractController} that's specific for this client. It adds activation and
 * deactivation methods, and applies a {@link MapEventParser} at construction time.
 * </p>
 * <p>
 * It is recommended to use this class as a base to implement your own controllers.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@UserImplemented
@Api(allMethods = true)
public abstract class AbstractMapController extends AbstractController implements MapController {

	/**
	 * The map onto which this controller is be added. This field is null, until the controller is actually added to a
	 * map.
	 */
	protected MapPresenter mapPresenter;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/** Create a new controller. */
	protected AbstractMapController() {
		super(false);
	}

	/**
	 * Create a new controller.
	 * 
	 * @param dragging
	 *            Is the user currently busy dragging (mouse down) at the moment this controller is created? In most
	 *            cases this setting should be false.
	 */
	protected AbstractMapController(boolean dragging) {
		super(dragging);
	}

	/**
	 * Create a new controller.
	 * 
	 * @param eventParser
	 *            Provide your own event parser.
	 * @param dragging
	 *            Is the user currently busy dragging (mouse down) at the moment this controller is created? In most
	 *            cases this setting should be false.
	 */
	protected AbstractMapController(MapEventParser eventParser, boolean dragging) {
		super(eventParser, dragging);
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	@Override
	public void onActivate(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		eventParser = mapPresenter.getMapEventParser();
	}

	@Override
	public void onDeactivate(MapPresenter mapPresenter) {
	}
}