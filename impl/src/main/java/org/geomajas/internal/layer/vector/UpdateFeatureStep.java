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

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.AttributeService;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Update an existing {@link InternalFeature} from the underlying feature data object.
 *
 * @author Joachim Van der Auwera
 */
public class UpdateFeatureStep implements PipelineStep {

	@Autowired
	private AttributeService attributeService;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		Object feature = context.get(PipelineCode.FEATURE_DATA_OBJECT_KEY);
		InternalFeature newFeature = context.get(PipelineCode.FEATURE_KEY, InternalFeature.class);
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);

		// Not needed for existing features, but no problem to re-set feature id
		// essential for auto-generated ids (like for Hibernate layer)
		String id = layer.getFeatureModel().getId(feature);
		newFeature.setId(id);

		newFeature = attributeService.getAttributes(layer, newFeature, feature);
		if (null == newFeature) {
			context.put(PipelineCode.FEATURE_KEY, null);
		}
	}

}

