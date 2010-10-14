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
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Prepare saving the feature in case of update (assure "featureDataObject" is available).
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateUpdateStep extends AbstractSaveOrUpdateStep {

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		InternalFeature oldFeature = context.getOptional(PipelineCode.OLD_FEATURE_KEY, InternalFeature.class);
		InternalFeature newFeature = context.get(PipelineCode.FEATURE_KEY, InternalFeature.class);
		if (null != oldFeature) {
			String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			if (securityContext.isFeatureUpdateAuthorized(layerId, oldFeature, newFeature)) {
				if (null == context.getOptional(PipelineCode.FEATURE_DATA_OBJECT_KEY)) {
					context.put(PipelineCode.FEATURE_DATA_OBJECT_KEY, layer.read(newFeature.getId()));
				}
			} else {
				throw new GeomajasSecurityException(ExceptionCode.FEATURE_UPDATE_PROHIBITED,
						oldFeature.getId(), securityContext.getUserId());
			}
		}
	}
}

