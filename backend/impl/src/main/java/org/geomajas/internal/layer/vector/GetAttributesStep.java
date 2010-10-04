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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerAssociationSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.pipeline.GetAttributesContainer;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;

/**
 * Step for the getAttributes pipeline in {@link org.geomajas.internal.layer.VectorLayerServiceImpl}.
 *
 * @author Joachim Van der Auwera
 */
public class GetAttributesStep implements PipelineStep<GetAttributesContainer> {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetAttributesContainer response) throws GeomajasException {
		List<Attribute<?>> attributes = response.getAttributes();
		if (null == attributes) {
			attributes = new ArrayList<Attribute<?>>();
			response.setAttributes(attributes);
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			Filter filter = context.get(PipelineCode.FILTER_KEY, Filter.class);
			String attributeName = context.get(PipelineCode.ATTRIBUTE_NAME_KEY, String.class);
			if (layer instanceof VectorLayerAssociationSupport) {
				List<Attribute<?>> list = ((VectorLayerAssociationSupport) layer).getAttributes(attributeName, filter);
				attributes.addAll(list);
			}
		}
	}
}
