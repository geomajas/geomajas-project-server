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
import org.geomajas.rendering.pipeline.PipelineContext;
import org.geomajas.rendering.pipeline.PipelineStep;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Save the data from the feature data object (read from the context) into the layer.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateSaveStep implements PipelineStep<SaveOrUpdateOneContainer, SaveOrUpdateOneContainer> {

	@Autowired
	private SecurityContext securityContext;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(SaveOrUpdateOneContainer request, PipelineContext context,
			SaveOrUpdateOneContainer response) throws GeomajasException {
		InternalFeature newFeature = request.getNewFeature();
		Object feature = context.get(SaveOrUpdateEachStep.FEATURE_DATA_OBJECT_KEY);
		String layerId = request.getSaveOrUpdateContainer().getLayerId();
		VectorLayer layer = request.getSaveOrUpdateContainer().getLayer();
		FeatureModel featureModel = layer.getFeatureModel();

		// Assure only writable attributes are set
		Map<String, Attribute> requestAttributes = newFeature.getAttributes();
		Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
		if (null != requestAttributes) {
			for (String key : requestAttributes.keySet()) {
				if (securityContext.isAttributeWritable(layerId, newFeature, key)) {
					filteredAttributes.put(key, requestAttributes.get(key));
				}
			}
		}
		featureModel.setAttributes(feature, filteredAttributes);

		if (newFeature.getGeometry() != null) {
			featureModel.setGeometry(feature, newFeature.getGeometry());
		}
		context.put(SaveOrUpdateEachStep.FEATURE_DATA_OBJECT_KEY, layer.saveOrUpdate(feature));
	}
}
