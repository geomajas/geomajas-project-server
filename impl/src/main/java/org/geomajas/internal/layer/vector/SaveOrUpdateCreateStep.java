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
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Create the feature it did not exist (and assure "featureDataObject" is available).
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateCreateStep extends AbstractSaveOrUpdateStep {

	public void execute(PipelineContext context, Object response) throws GeomajasException {
		InternalFeature oldFeature = context.getOptional(PipelineCode.OLD_FEATURE_KEY, InternalFeature.class);
		InternalFeature newFeature = context.get(PipelineCode.FEATURE_KEY, InternalFeature.class);
		if (null == oldFeature) {
			// create new feature
			String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			FeatureModel featureModel = layer.getFeatureModel();
			if (securityContext.isFeatureCreateAuthorized(layerId, newFeature)) {
				Object feature;
				if (newFeature.getId() == null) {
					feature = featureModel.newInstance();
				} else {
					feature = featureModel.newInstance(newFeature.getId());
				}
				context.put(PipelineCode.FEATURE_DATA_OBJECT_KEY, feature);
				context.put(PipelineCode.IS_CREATE_KEY, true);
			} else {
				throw new GeomajasSecurityException(ExceptionCode.FEATURE_CREATE_PROHIBITED, securityContext
						.getUserId());
			}
		}
	}
}

