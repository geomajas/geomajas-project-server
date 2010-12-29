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

package org.geomajas.geometry;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.global.Api;
import org.opengis.referencing.operation.MathTransform;

/**
 * Extension of a {@link MathTransform} which allows getting the source and target CRSs and a
 * {@link com.vividsolutions.jts.geom.Geometry} with the transformable area.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public interface CrsTransform extends MathTransform {

	/**
	 * Get id or code for this CrsTransform. For example "EPSG:4326->EPSG:900913".
	 *
	 * @return crs id
	 */
	String getId();

	/**
	 * Source {@link Crs} for transformation.
	 *
	 * @return source crs
	 */
	Crs getSource();

	/**
	 * Target {@link Crs} for transformation.
	 *
	 * @return target crs
	 */
	Crs getTarget();

	/**
	 * Geometry of the area which can be transformed using this transformation.
	 *
	 * @return transformable area (in source crs)
	 */
	Geometry getTransformableGeometry();

	/**
	 * Envelope of the area which can be transformed using this transformation.
	 *
	 * @return transformable area (in source crs)
	 */
	Envelope getTransformableEnvelope();

	/**
	 * Bbox of the area which can be transformed using this transformation.
	 *
	 * @return transformable area (in source crs)
	 */
	Bbox getTransformableBbox();

}
