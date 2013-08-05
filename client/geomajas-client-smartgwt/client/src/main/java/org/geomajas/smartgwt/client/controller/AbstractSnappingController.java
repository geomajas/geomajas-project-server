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

package org.geomajas.smartgwt.client.controller;

import java.util.List;

import org.geomajas.configuration.SnappingRuleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.snapping.Snapper;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseEvent;

/**
 * <p>
 * Basic controller to start building upon when you need snapping. Using snapping goes through the "getScreenPosition"
 * and "getWorldPosition" methods. These will return snapped points instead of the actual points , but only when
 * snapping is activated. Snapping can be activated and deactivated at any time. Activating snapping, requires you to
 * pass along a list of snapping rules to follow, and a snapping mode. (SnapMode.MODE_ALL_GEOMTRIES_EQUAL or
 * SnapMode.MODE_PRIORITY_TO_INTERSECTING_GEOMETRIES)
 * </p>
 *
 * @author Pieter De Graef
 */
public abstract class AbstractSnappingController extends AbstractGraphicsController {

	/**
	 * The internal snapper that is used for the snapping functionality.
	 */
	private Snapper snapper;

	/**
	 * Boolean that indicates whether or not snapping is currently active.
	 */
	private boolean snappingActive;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Just like the <code>AbstractSnappingController</code>, this class expects a map widget at construction.
	 */
	protected AbstractSnappingController(MapWidget mapWidget) {
		super(mapWidget);
	}

	// -------------------------------------------------------------------------
	// Protected methods:
	// -------------------------------------------------------------------------

	/**
	 * Activate snapping on this controller. When snapping is activated, the methods "getScreenPosition" and
	 * "getWorldPosition" (to ask the position of an event), will return snapped positions.
	 *
	 * @param rules
	 *            A list of <code>SnappingRuleInfo</code>s. These determine to what layers to snap, using what distances
	 *            and what algorithms.
	 * @param mode
	 *            The snapper mode. Either the snapper considers all geometries equal, or it tries to snap to
	 *            intersecting geometries before snapping to other geometries. Basically:
	 *            <ul>
	 *            <li>SnapMode.MODE_ALL_GEOMTRIES_EQUAL</li>
	 *            <li>SnapMode.MODE_PRIORITY_TO_INTERSECTING_GEOMETRIES</li>
	 *            </ul>
	 */
	public void activateSnapping(List<SnappingRuleInfo> rules, Snapper.SnapMode mode) {
		if (rules != null) {
			snapper = new Snapper(mapWidget.getMapModel(), rules, mode);
			snappingActive = true;
		}
	}

	/**
	 * Deactivate snapping for the time being. Snapping can be activated again at any time.
	 */
	public void deactivateSnapping() {
		snappingActive = false;
	}

	/**
	 * Return the screen position of the mouse event, unless snapping is activated. If that is the case, a snapped
	 * point will be returned (still in screen space).
	 */
	protected Coordinate getScreenPosition(MouseEvent<?> event) {
		if (snappingActive) {
			return getTransformer().worldToView(getWorldPosition(event));
		}
		return super.getScreenPosition(event);
	}

	/**
	 * Return the screen position of the mouse event, unless snapping is activated. If that is the case, a snapped
	 * point will be returned (still in pan space).
	 */
	protected Coordinate getPanPosition(MouseEvent<?> event) {
		if (snappingActive) {
			return getTransformer().worldToPan(getWorldPosition(event));
		}
		return super.getPanPosition(event);
	}

	/**
	 * Return the world position of the mouse event, unless snapping is activated. If that is the case, a snapped point
	 * will be returned (still in world space).
	 */
	protected Coordinate getWorldPosition(MouseEvent<?> event) {
		Coordinate world = super.getWorldPosition(event);
		if (snappingActive) {
			return snapper.snap(world);
		}
		return world;
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	/**
	 * Return the used snapper. (can be null)
	 *
	 * @return {@link Snapper} object
	 */
	public Snapper getSnapper() {
		return snapper;
	}

	/**
	 * See whether or not snapping is currently activated.
	 *
	 * @return true when snapping is active
	 */
	public boolean isSnappingActive() {
		return snappingActive;
	}
}
