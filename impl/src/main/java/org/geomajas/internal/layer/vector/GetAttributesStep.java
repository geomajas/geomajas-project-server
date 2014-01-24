/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
