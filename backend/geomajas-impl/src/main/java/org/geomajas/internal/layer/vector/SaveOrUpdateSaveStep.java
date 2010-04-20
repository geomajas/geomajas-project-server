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

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.GeomajasSecurityException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.opengis.filter.Filter;

import java.util.HashMap;
import java.util.Map;

/**
 * Save the data from the feature data object (read from the context) into the layer.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateSaveStep extends AbstractSaveOrUpdateStep {

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		InternalFeature newFeature = context.getOptional(PipelineCode.FEATURE_KEY, InternalFeature.class);
		Object feature = context.get(PipelineCode.FEATURE_DATA_OBJECT_KEY);
		String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		FeatureModel featureModel = layer.getFeatureModel();
		Boolean isCreateObject = context.getOptional(PipelineCode.IS_CREATE_KEY, Boolean.class);
		boolean isCreate  = false;
		if (null != isCreateObject && isCreateObject) {
			isCreate = true;
		}

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

		Filter securityFilter;
		if (isCreate) {
			securityFilter = getSecurityFilter(layer, securityContext.getCreateAuthorizedArea(layerId));
		} else {
			securityFilter = getSecurityFilter(layer, securityContext.getUpdateAuthorizedArea(layerId));
		}
		if (securityFilter.evaluate(feature)) {
			context.put(PipelineCode.FEATURE_DATA_OBJECT_KEY, layer.saveOrUpdate(feature));
		} else {
			if (isCreate) {
				throw new GeomajasSecurityException(ExceptionCode.FEATURE_CREATE_PROHIBITED,
						securityContext.getUserId());
			} else {
				throw new GeomajasSecurityException(ExceptionCode.FEATURE_UPDATE_PROHIBITED, newFeature.getId(),
						securityContext.getUserId());				
			}
		}
	}
}
