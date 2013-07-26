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

package org.geomajas.gwt.client.map.render;

import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt.client.map.MapConfiguration;

import com.google.gwt.animation.client.Animation;

/**
 * Extension of the GWT animation definition for navigation around the map. It has support for both zooming and panning.
 * 
 * @author Pieter De Graef
 */
public class MapNavigationAnimation extends Animation {

	private final MapConfiguration configuration;

	private AbstractNavigationFunction function;

	private boolean running;

	protected List<LayerRenderer> mapScalesRenderers;

	private double currentScale;

	private double currentX;

	private double currentY;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	/** Initialize the animation. */
	public MapNavigationAnimation(MapConfiguration configuration, AbstractNavigationFunction function) {
		super();
		this.configuration = configuration;
		this.function = function;
	}

	// ------------------------------------------------------------------------
	// public methods:
	// ------------------------------------------------------------------------

	/**
	 * Start the animation right now, using the given parameters. Only the botton X layers will be animated, where X
	 * equals the value set through <code>setNrAnimatedLayers</code>.
	 * 
	 * @param layerPresenters
	 *            A collection of {@link MapScalesRenderer}s that should be animated. This class will call the
	 *            navigation methods onto these presenters directly.
	 * @param sourceScale
	 *            The source zooming factor. This is a delta value. Value=1 will keep the layer presenters at their
	 *            current scale level.
	 * @param targetScale
	 *            The target zooming factor. This is a delta value. Value=1 will keep the layer presenters at their
	 *            current scale level.
	 * @param sourcePosition
	 *            The source translation factor.
	 * @param targetPosition
	 *            The target translation factor.
	 * @param millis
	 *            The time in milliseconds this animation should run.
	 */
	public void start(List<LayerRenderer> layerPresenters, double sourceScale, double targetScale,
			Coordinate sourcePosition, Coordinate targetPosition, int millis) {
		this.mapScalesRenderers = layerPresenters;

		function.setBeginLocation(sourcePosition.getX(), sourcePosition.getY(), sourceScale);
		function.setEndLocation(targetPosition.getX(), targetPosition.getY(), targetScale);

		running = true;
		run(millis);
	}

	/**
	 * Extend the animation to a new location and scale. This is executed only if the animation is actually running.
	 * 
	 * @param targetScale
	 *            The new target scale.
	 * @param targetPosition
	 *            The new target location.
	 * @param millis
	 *            The time in milliseconds starting from now (time is not added).
	 */
	public void extend(double targetScale, Coordinate targetPosition, int millis) {
		if (running) {
			start(mapScalesRenderers, currentScale, targetScale, new Coordinate(currentX, currentY), targetPosition,
					millis);
		}
	}

	/**
	 * Is this animation currently running?
	 * 
	 * @return yes or no, true or false...
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Get the navigation function that's currently being used.
	 * 
	 * @return The navigation function that's currently being used.
	 */
	public AbstractNavigationFunction getFunction() {
		return function;
	}

	/**
	 * Set the navigation function be be used.
	 * 
	 * @param function
	 *            Apply a new navigation function.
	 */
	public void setFunction(AbstractNavigationFunction function) {
		this.function = function;
	}

	/**
	 * Get the current list of map scale renderers.
	 * 
	 * @return The current list of map scale renderers.
	 */
	public List<LayerRenderer> getMapScaleRenderers() {
		return mapScalesRenderers;
	}

	// ------------------------------------------------------------------------
	// Overridden methods:
	// ------------------------------------------------------------------------

	/**
	 * Method that keeps tabs on the animation progress, and automatically transforms all {@link MapScalesRenderer}s
	 * accordingly.
	 * 
	 * @param progress
	 *            The progress within the animation. Is a value between 0 and 1, where 1 means that the animation come
	 *            to it's end.
	 */
	protected void onUpdate(double progress) {
		running = true;
		double[] location = function.getLocation(progress);
		currentX = location[0];
		currentY = location[1];
		currentScale = location[2];
		if (Double.isNaN(currentScale) || Double.isInfinite(currentScale)) {
			currentScale = 1;
		}

		for (int i = 0; i < mapScalesRenderers.size(); i++) {
			LayerRenderer presenter = mapScalesRenderers.get(i);
			LayerScaleRenderer scalePresenter = presenter.getVisibleScale();
			if (scalePresenter != null) {
				if (configuration.isAnimated(presenter.getLayer()) && Dom.isTransformationSupported()) {
					scalePresenter.getHtmlContainer().applyScale(currentScale, 0, 0);
					scalePresenter.getHtmlContainer().setLeft((int) Math.round(currentX));
					scalePresenter.getHtmlContainer().setTop((int) Math.round(currentY));
				} else {
					scalePresenter.getHtmlContainer().setVisible(false);
				}
			}
		}
	}

	/** Called when the current animation has been canceled. */
	protected void onCancel() {
		running = false;
	}

	/**
	 * Called when the animation has been completed successfully. Finishes the navigation process on all
	 * {@link MapScalesRenderer}s.
	 */
	protected void onComplete() {
		onUpdate(1); // Needed when millis = 0 (no animation). This fakes a zoom.
		running = false;
	}
}