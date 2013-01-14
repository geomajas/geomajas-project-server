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

package org.geomajas.plugin.editing.client.snap;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;
import org.geomajas.plugin.editing.client.snap.event.CoordinateSnapEvent;
import org.geomajas.plugin.editing.client.snap.event.CoordinateSnapHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * The central snapping service. Make sure to add at least one snapping rule before using it. Without snapping rules,
 * snapping will never occur. It is also possible to add a handler to listen to snapping events.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class SnapService {

	private List<SnappingRule> rules;

	private EventBus eventBus;

	private boolean hasSnapped;

	private double currentDistance;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new service for snapping. This service can for example assist the editing process to provide a snapping
	 * functionality.
	 */
	public SnapService() {
		rules = new ArrayList<SnappingRule>();
		eventBus = new SimpleEventBus();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/**
	 * Add a handler to be notified of snapping events. Those events are fired every time the <code>snap</code> method
	 * is called. The event itself will know whether or not snapping actually occurred.
	 * 
	 * @param handler
	 *            The handler to be notified of snapping events.
	 * @return Returns the registration which can remove the handler again.
	 */
	public HandlerRegistration addCoordinateSnapHandler(CoordinateSnapHandler handler) {
		return eventBus.addHandler(CoordinateSnapHandler.TYPE, handler);
	}

	/**
	 * Using the current list of snapping rules, try to snap the given coordinate. Fires a {@link CoordinateSnapEvent}
	 * in any case (whether snapping occurred or not).
	 * 
	 * @param coordinate
	 *            The original location.
	 * @return The returned (perhaps snapped) location. If no snapping occurred, the original location is returned.
	 */
	public Coordinate snap(Coordinate coordinate) {
		Coordinate result = coordinate;
		currentDistance = Double.MAX_VALUE;
		hasSnapped = false;

		for (SnappingRule rule : rules) {
			if (rule.isHighPriority() || !hasSnapped) {
				double distance = Math.min(currentDistance, rule.getDistance());
				result = rule.getAlgorithm().snap(coordinate, distance);
				if (rule.getAlgorithm().hasSnapped()) {
					currentDistance = rule.getAlgorithm().getCalculatedDistance();
					hasSnapped = true;
				}
			}
		}
		eventBus.fireEvent(new CoordinateSnapEvent(coordinate, result));
		return result;
	}

	/**
	 * After the <code>snap</code> method has been called, this method can return the distance that was covered in the
	 * snap event.
	 */
	public double getCalculatedDistance() {
		return currentDistance;
	}

	/**
	 * Check to see whether or not snapping actually occurred. Call this after the <code>snap</code> method has been
	 * called.
	 * 
	 * @return Has snapping actually occurred?
	 */
	public boolean hasSnapped() {
		return hasSnapped;
	}

	/**
	 * Update the playing field for snapping, by providing a bounding box wherein the source providers should present
	 * their geometries.
	 * 
	 * @param mapBounds
	 *            The bounding box wherein we expect snapping to occur. Is usually the current view on the map.
	 */
	public void update(Bbox mapBounds) {
		for (final SnappingRule condition : rules) {
			condition.getSourceProvider().update(mapBounds);
			condition.getSourceProvider().getSnappingSources(new GeometryArrayFunction() {

				public void execute(Geometry[] geometries) {
					condition.getAlgorithm().setGeometries(geometries);
				}
			});
		}
	}

	/** Remove all snapping rules from this service. Without any snapping rules, snapping can not occur. */
	public void clearSnappingRules() {
		rules.clear();
	}

	/**
	 * Add a new snapping rules to the list. Each new rule provides information on how snapping should occur.
	 * 
	 * @param algorithm
	 *            The snapping algorithm to be used. For example, snap to end-points only, or also to edges, or...
	 * @param sourceProvider
	 *            The provider of target geometries where to snap. For example, snap to features of a layer.
	 * @param distance
	 *            The maximum distance to bridge during snapping.
	 * @param highPriority
	 *            High priority means that this rule will always be executed. Low priority means that if a previous
	 *            snapping algorithm has found a snapping candidate, this algorithm will not be executed anymore.
	 */
	public void addSnappingRule(SnapAlgorithm algorithm, SnapSourceProvider sourceProvider, double distance,
			boolean highPriority) {
		rules.add(new SnappingRule(algorithm, sourceProvider, distance, highPriority));
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Internal representation of a snapping rule.
	 * 
	 * @author Pieter De Graef
	 */
	private final class SnappingRule {

		private final SnapAlgorithm algorithm;

		private final SnapSourceProvider sourceProvider;

		private final double distance;

		private final boolean highPriority;

		private SnappingRule(SnapAlgorithm algorithm, SnapSourceProvider sourceProvider, double distance,
				boolean highPriority) {
			this.algorithm = algorithm;
			this.sourceProvider = sourceProvider;
			this.distance = distance;
			this.highPriority = highPriority;
		}

		public SnapAlgorithm getAlgorithm() {
			return algorithm;
		}

		public SnapSourceProvider getSourceProvider() {
			return sourceProvider;
		}

		public double getDistance() {
			return distance;
		}

		public boolean isHighPriority() {
			return highPriority;
		}
	}
}