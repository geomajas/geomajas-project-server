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

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.service.VectorLayerServiceImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.rendering.pipeline.PipelineContext;
import org.geomajas.rendering.pipeline.PipelineStep;
import org.opengis.filter.Filter;

import java.util.Iterator;
import java.util.List;

/**
 * Step for the getObjects pipeline in {@link org.geomajas.internal.service.VectorLayerServiceImpl}.
 *
 * @author Joachim Van der Auwera
 */
public class GetObjectsStep implements PipelineStep<GetObjectsRequest, List<Object>> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(GetObjectsRequest request, PipelineContext context, List<Object> response)
			throws GeomajasException {
		VectorLayer layer = request.getLayer();
		Filter filter = context.get(VectorLayerServiceImpl.FILTER_KEY, Filter.class);
		String attributeName = request.getAttributeName();
		if (layer instanceof VectorLayerAssociationSupport) {
			Iterator<?> it = ((VectorLayerAssociationSupport) layer).getObjects(attributeName, filter);
			while (it.hasNext()) {
				response.add(it.next());
			}
		}
	}
}
