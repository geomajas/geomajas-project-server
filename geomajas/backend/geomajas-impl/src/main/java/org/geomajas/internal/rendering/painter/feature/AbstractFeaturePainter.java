/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.internal.rendering.painter.feature;

import org.geomajas.rendering.painter.feature.FeaturePainter;

/**
 * <p>
 * Abstract implementation of the <code>FeaturePainter</code> interface. It implements common functionality for all
 * feature painter: the option settings. In order to decently support lazy loading, all feature painter should support
 * these options! Supported options are:
 * <ul>
 * <li><b>renderingAttributes</b>: will attributes be added to the <code>JtsFeature</code> objects or not?</li>
 * <li><b>renderingGeometry</b>: will geometries be added to the <code>JtsFeature</code> objects or not?</li>
 * <li><b>renderingStyle</b>: will style definitions be added to the <code>JtsFeature</code> objects or not?</li>
 * <li><b>renderingLabel</b>: will the label string be added to the <code>JtsFeature</code> objects or not?</li>
 * </ul>
 * </p>
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractFeaturePainter implements FeaturePainter {

	private boolean renderingAttributes;

	private boolean renderingGeometry;

	private boolean renderingStyle;

	private boolean renderingLabels;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor. The default settings allow geometries, labels and style definitions to be added to the
	 * <code>RenderedFeature</code> objects, but not attributes.
	 */
	public AbstractFeaturePainter() {
		this(true, true, true, true);
	}

	/**
	 * Constructor that determines what should be painted and what not.
	 * 
	 * @param renderingAttributes
	 *            Do we allow attributes to be added to the <code>JtsFeature</code> objects or not?
	 * @param renderingGeometry
	 *            Do we allow geometries to be added to the <code>JtsFeature</code> objects or not?
	 * @param renderingStyle
	 *            Do we allow style definitions to be added to the <code>JtsFeature</code> objects or not?
	 * @param renderingLabels
	 *            Do we allow the label string to be added to the <code>JtsFeature</code> objects or not?
	 */
	public AbstractFeaturePainter(boolean renderingAttributes, boolean renderingGeometry, boolean renderingStyle,
			boolean renderingLabels) {
		this.renderingAttributes = renderingAttributes;
		this.renderingGeometry = renderingGeometry;
		this.renderingStyle = renderingStyle;
		this.renderingLabels = renderingLabels;
	}

	// -------------------------------------------------------------------------
	// FeaturePainter implementation (part of it):
	// -------------------------------------------------------------------------

	/** Will attributes be added to the <code>JtsFeature</code> objects or not? */
	public boolean isRenderingAttributes() {
		return renderingAttributes;
	}

	/**
	 * Determine whether attributes will be added to the <code>JtsFeature</code> objects or not.
	 * 
	 * @param renderingAttributes
	 *            The new value.
	 */
	public void setRenderingAttributes(boolean renderingAttributes) {
		this.renderingAttributes = renderingAttributes;
	}

	/** Will geometries be added to the <code>JtsFeature</code> objects or not? */
	public boolean isRenderingGeometry() {
		return renderingGeometry;
	}

	/**
	 * Determine whether geometries will be added to the <code>JtsFeature</code> objects or not.
	 * 
	 * @param renderingGeometry
	 *            The new value.
	 */
	public void setRenderingGeometry(boolean renderingGeometry) {
		this.renderingGeometry = renderingGeometry;
	}

	/** Will style definitions be added to the <code>JtsFeature</code> objects or not? */
	public boolean isRenderingStyle() {
		return renderingStyle;
	}

	/**
	 * Determine whether style definitions will be added to the <code>JtsFeature</code> objects or not.
	 * 
	 * @param renderingStyle
	 *            The new value.
	 */
	public void setRenderingStyle(boolean renderingStyle) {
		this.renderingStyle = renderingStyle;
	}

	/** Will the label string be added to the <code>JtsFeature</code> objects or not? */
	public boolean isRenderingLabels() {
		return renderingLabels;
	}

	/**
	 * Determine whether the label string will be added to the <code>JtsFeature</code> objects or not.
	 * 
	 * @param renderingLabels
	 *            The new value.
	 */
	public void setRenderingLabels(boolean renderingLabels) {
		this.renderingLabels = renderingLabels;
	}
}