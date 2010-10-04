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

package org.geomajas.internal.layer.vector;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetBoundsContainer;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.geotools.geometry.jts.JTS;
import org.opengis.filter.Filter;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Step for getBounds in {@link org.geomajas.layer.VectorLayerService}.
 *
 * @author Joachim Van der Auwera
 */
public class GetBoundsStep implements PipelineStep<GetBoundsContainer> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetBoundsContainer response)
			throws GeomajasException {
		if (null == response.getEnvelope()) {
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			MathTransform crsTransform = context.get(PipelineCode.CRS_TRANSFORM_KEY, MathTransform.class);
			Filter filter = context.get(PipelineCode.FILTER_KEY, Filter.class);
			Envelope bounds = layer.getBounds(filter);
			try {
				bounds = JTS.transform(bounds, crsTransform);
			} catch (TransformException te) {
				throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
			}
			response.setEnvelope(bounds);
		}
	}
}
