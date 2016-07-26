/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.vector;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.opengis.filter.Filter;

/**
 * Handle possible delete of an individual feature in saveOrUpdate.
 * <p/>
 * If the feature is deleted, the pipeline is finished.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateDeleteStep extends AbstractSaveOrUpdateStep {

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		InternalFeature newFeature = context.getOptional(PipelineCode.FEATURE_KEY, InternalFeature.class);
		if (null == newFeature) {
			// delete ?
			InternalFeature oldFeature = context.getOptional(PipelineCode.OLD_FEATURE_KEY, InternalFeature.class);
			if (null != oldFeature) {
				String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
				if (securityContext.isFeatureDeleteAuthorized(layerId, oldFeature)) {
					VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
					Object featureObj = layer.read(oldFeature.getId());
					if (null != featureObj) {
						Filter securityFilter = getSecurityFilter(layer,
								securityContext.getDeleteAuthorizedArea(layerId));
						if (securityFilter.evaluate(featureObj)) {
							layer.delete(oldFeature.getId());
						} else {
							throw new GeomajasSecurityException(ExceptionCode.FEATURE_DELETE_PROHIBITED,
									oldFeature.getId(), securityContext.getUserId());
						}
					}
				} else {
					throw new GeomajasSecurityException(ExceptionCode.FEATURE_DELETE_PROHIBITED,
							oldFeature.getId(), securityContext.getUserId());
				}
			}
			context.setFinished(true); // stop pipeline execution
		}
	}
}
