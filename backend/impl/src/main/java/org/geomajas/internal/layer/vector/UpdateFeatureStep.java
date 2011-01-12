/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.vector;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Update an existing {@link InternalFeature} from the underlying feature data object.
 *
 * @author Joachim Van der Auwera
 */
public class UpdateFeatureStep implements PipelineStep {

	@Autowired
	private SecurityContext securityContext;

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
		String layerId = layer.getId();
		FeatureModel featureModel = layer.getFeatureModel();

//		// Not needed for existing features, but no problem to re-set feature id
//		String id = featureModel.getId(feature);
//		newFeature.setId(id);

		filterAttributes(layerId, newFeature, featureModel.getAttributes(feature));

		newFeature.setEditable(securityContext.isFeatureUpdateAuthorized(layerId, newFeature));
		newFeature.setDeletable(securityContext.isFeatureDeleteAuthorized(layerId, newFeature));

	}

	private Map<String, Attribute> filterAttributes(String layerId, InternalFeature feature,
			Map<String, Attribute> featureAttributes) {
		feature.setAttributes(featureAttributes); // to allow isAttributeReadable to see full object
		Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
		for (String key : featureAttributes.keySet()) {
			if (securityContext.isAttributeReadable(layerId, feature, key)) {
				Attribute attribute = featureAttributes.get(key);
				attribute.setEditable(securityContext.isAttributeWritable(layerId, feature, key));
				filteredAttributes.put(key, featureAttributes.get(key));
			}
		}
		feature.setAttributes(filteredAttributes);
		return filteredAttributes;
	}

}

