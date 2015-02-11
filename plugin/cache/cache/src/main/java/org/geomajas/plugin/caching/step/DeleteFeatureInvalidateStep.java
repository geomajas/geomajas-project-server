/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step to invalidate caches for features which need to be deleted during SaveOrUpdate.
 *
 * @author Joachim Van der Auwera
 */
public class DeleteFeatureInvalidateStep implements PipelineStep {

	private final Logger log = LoggerFactory.getLogger(DeleteFeatureInvalidateStep.class);

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private TestRecorder recorder;

	private String id;

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Set the step id.
	 *
	 * @param id id
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void execute(PipelineContext context, Object result) throws GeomajasException {
		try {
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
							// @todo no security checks before invalidating, the delete may still fail at this moment,
							// in which case the invalidation should not have been done. Still better to invalidate too
							// much then too little.
							//Filter securityFilter = getSecurityFilter(layer,
							// 	securityContext.getDeleteAuthorizedArea(layerId));
							//if (securityFilter.evaluate(featureObj)) {
							//	layer.delete(oldFeature.getId());
							//} else {
							//	throw new GeomajasSecurityException(ExceptionCode.FEATURE_DELETE_PROHIBITED,
							//			oldFeature.getId(), securityContext.getUserId());
							//}
							Geometry geometry = layer.getFeatureModel().getGeometry(featureObj);
							if (null != geometry) {
								recorder.record("layer", "Invalidate geometry for deleted feature");
								cacheManager.invalidate(layer, geometry.getEnvelopeInternal());
							}
						}
					} else {
						throw new GeomajasSecurityException(ExceptionCode.FEATURE_DELETE_PROHIBITED,
								oldFeature.getId(), securityContext.getUserId());
					}
				}
			}
		} catch (Throwable t) { //NOPMD
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
}
