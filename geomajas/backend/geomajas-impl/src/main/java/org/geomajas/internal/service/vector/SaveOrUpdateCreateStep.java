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

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.GeomajasSecurityException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.pipeline.PipelineContext;
import org.geomajas.rendering.pipeline.PipelineStep;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Create the feature it did not exist (and assure "featureDataObject" is available).
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateCreateStep implements PipelineStep<SaveOrUpdateOneContainer, SaveOrUpdateOneContainer> {

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
		InternalFeature oldFeature = request.getOldFeature();
		InternalFeature newFeature = request.getNewFeature();
		if (null == oldFeature) {
			// create new feature
			String layerId = request.getSaveOrUpdateContainer().getLayerId();
			VectorLayer layer = request.getSaveOrUpdateContainer().getLayer();
			FeatureModel featureModel = layer.getFeatureModel();
			if (securityContext.isFeatureCreateAuthorized(layerId, oldFeature)) {
				if (newFeature.getLocalId() == null) {
					context.put(SaveOrUpdateEachStep.FEATURE_DATA_OBJECT_KEY, featureModel.newInstance());
				} else {
					context.put(SaveOrUpdateEachStep.FEATURE_DATA_OBJECT_KEY,
							featureModel.newInstance(newFeature.getLocalId()));
				}
			} else {
				throw new GeomajasSecurityException(ExceptionCode.FEATURE_CREATE_PROHIBITED, securityContext
						.getUserId());
			}
		}
	}
}

