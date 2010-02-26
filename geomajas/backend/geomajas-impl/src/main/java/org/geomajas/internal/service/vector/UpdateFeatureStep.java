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
		String layerId = layer.getLayerInfo().getId();
		FeatureModel featureModel = layer.getFeatureModel();

		// Not needed for existing features, but no problem to re-set feature id
		String id = featureModel.getId(feature);
		newFeature.setId(layerId + "." + id);

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

