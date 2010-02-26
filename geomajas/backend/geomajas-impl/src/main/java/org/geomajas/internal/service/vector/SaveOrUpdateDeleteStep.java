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
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handle possible delete of an individual feature in saveOrUpdate.
 * <p/>
 * If the feature is deleted, the pipeline is finished.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateDeleteStep implements PipelineStep {

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
		InternalFeature newFeature = context.getOptional(PipelineCode.FEATURE_KEY, InternalFeature.class);
		if (null == newFeature) {
			// delete ?
			InternalFeature oldFeature = context.getOptional(PipelineCode.OLD_FEATURE_KEY, InternalFeature.class);
			if (null != oldFeature) {
				String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
				if (securityContext.isFeatureDeleteAuthorized(layerId, oldFeature)) {
					VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
					layer.delete(oldFeature.getLocalId());
				} else {
					throw new GeomajasSecurityException(ExceptionCode.FEATURE_DELETE_PROHIBITED,
							oldFeature.getId(), securityContext.getUserId());
				}
			}
			context.setFinished(true); // stop pipeline execution
		}
	}
}
