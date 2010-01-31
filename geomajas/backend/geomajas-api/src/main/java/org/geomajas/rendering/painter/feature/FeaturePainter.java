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

package org.geomajas.rendering.painter.feature;

import java.util.List;

import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.LayerPaintContext;

/**
 * <p>
 * Interface for painter that builds JTS features. Implementations of this interface will be used in the
 * <code>VectorLayer</code>'s paint function. Basically they transform the object from the <code>LayerModel</code> into
 * <code>JtsFeature</code> objects. Different implementations of this interface will transform to different
 * implementations of a <code>JtsFeature</code>.
 * </p>
 * <p>
 * It also adds the possibility to determine what parts of the features should be painted: attributes, geometries,
 * styles. Leaving out some (or all) of these parts can increase performance. Supported options are:
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
public interface FeaturePainter {

	/**
	 * Paint an individual feature. In other words transform the generic feature object into a <code>JtsFeature</code>.
	 * 
	 * @param paintContext
	 *            The provided painting context. It helps to determine what style a feature should receive.
	 * @param feature
	 *            A feature object that comes directly from the <code>LayerModel</code>. Some LayerModels cannot handle
	 *            these features to be altered directly (think Hibernate), so it is important that implementations of
	 *            this interface never transform the feature's geometries.
	 * @throws RenderException
	 */
	void paint(LayerPaintContext paintContext, Object feature) throws RenderException;

	/**
	 * Returns the full list of <code>JtsFeature</code> objects.
	 */
	List<InternalFeature> getFeatures();

	/** Will attributes be added to the <code>JtsFeature</code> objects or not? */
	boolean isRenderingAttributes();

	/** Will geometries be added to the <code>JtsFeature</code> objects or not? */
	boolean isRenderingGeometry();

	/** Will style definitions be added to the <code>JtsFeature</code> objects or not? */
	boolean isRenderingStyle();

	/** Will the label string be added to the <code>JtsFeature</code> objects or not? */
	boolean isRenderingLabels();

	/**
	 * Determine whether attributes will be added to the <code>JtsFeature</code> objects or not.
	 * 
	 * @param renderingAttributes
	 *            The new value.
	 */
	void setRenderingAttributes(boolean renderingAttributes);

	/**
	 * Determine whether geometries will be added to the <code>JtsFeature</code> objects or not.
	 * 
	 * @param renderingGeometry
	 *            The new value.
	 */
	void setRenderingGeometry(boolean renderingGeometry);

	/**
	 * Determine whether style definitions will be added to the <code>JtsFeature</code> objects or not.
	 * 
	 * @param renderingStyle
	 *            The new value.
	 */
	void setRenderingStyle(boolean renderingStyle);

	/**
	 * Determine whether the label string will be added to the <code>JtsFeature</code> objects or not.
	 * 
	 * @param renderingLabels
	 *            The new value.
	 */
	void setRenderingLabels(boolean renderingLabels);
}
