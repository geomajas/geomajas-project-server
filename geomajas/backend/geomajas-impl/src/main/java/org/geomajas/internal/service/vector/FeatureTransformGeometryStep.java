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

package org.geomajas.internal.service.vector;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.pipeline.PipelineContext;
import org.geomajas.rendering.pipeline.PipelineStep;
import org.geomajas.security.SecurityContext;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Convert the geometry (if any) in the feature (in the context) using a transformation (also in the context).
 *
 * @author Joachim Van der Auwera
 */
public class FeatureTransformGeometryStep implements PipelineStep<Object, Object> {

	@Autowired
	private SecurityContext securityContext;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(Object request, PipelineContext context, Object response) throws GeomajasException {
		InternalFeature feature = context.get(SaveOrUpdateEachStep.FEATURE_KEY, InternalFeature.class);
		if (null != feature.getGeometry()) {
			try {
				MathTransform mapToLayer = context.get(SaveOrUpdateEachStep.CRS_TRANSFORM_KEY, MathTransform.class);
				feature.setGeometry(JTS.transform(feature.getGeometry(), mapToLayer));
			} catch (TransformException te) {
				throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
			}
		}
	}
}

