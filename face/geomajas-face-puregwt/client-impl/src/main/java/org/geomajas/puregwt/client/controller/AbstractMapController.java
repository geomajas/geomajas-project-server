/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.puregwt.client.map.MapPresenter;

/**
 * <p>
 * Extension of the generic {@link AbstractController} that's specific for the PureGWT face. It adds activation and
 * deactivation methods, and applies a {@link MapEventParser} at construction time.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@UserImplemented
@Api(allMethods = true)
public abstract class AbstractMapController extends AbstractController implements MapController {

	protected MapPresenter mapPresenter;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	protected AbstractMapController(boolean dragging) {
		super(dragging);
	}

	protected AbstractMapController(MapEventParser eventParser, boolean dragging) {
		super(eventParser, dragging);
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void onActivate(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		eventParser = mapPresenter.getMapEventParser();
	}

	/** {@inheritDoc} */
	public void onDeactivate(MapPresenter mapPresenter) {
	}
}