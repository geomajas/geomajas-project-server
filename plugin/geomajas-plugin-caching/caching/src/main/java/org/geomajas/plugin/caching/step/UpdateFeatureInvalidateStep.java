/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step to invalidate caches for features which need to be updated during SaveOrUpdate.
 *
 * @author Joachim Van der Auwera
 */
public class UpdateFeatureInvalidateStep implements PipelineStep {

	private final Logger log = LoggerFactory.getLogger(UpdateFeatureInvalidateStep.class);

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private TestRecorder recorder;

	private String id;

	/** {@inheritDoc} */
	public String getId() {
		return id;
	}

	/**
	 * Set the step id.
	 *
	 * @param id step id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	public void execute(PipelineContext context, Object result) throws GeomajasException {
		try {
			log.debug("UpdateFeatureInvalidateStep start");
			VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);

			// invalidate the area of the old feature
			InternalFeature oldFeature = context.getOptional(PipelineCode.OLD_FEATURE_KEY, InternalFeature.class);
			if (null != oldFeature) {
				// get original geometry from storage to assure not changed by transformation and available
				Object feature = layer.read(oldFeature.getId());
				context.put(PipelineCode.FEATURE_DATA_OBJECT_KEY, feature); // put in context to prevent getting twice
				FeatureModel featureModel = layer.getFeatureModel();
				Geometry oldGeometry = featureModel.getGeometry(feature);
				if (null != oldGeometry) {
					// invalidate
					recorder.record("layer", "Invalidate geometry for old version of feature");
					Envelope oldEnvelope = oldGeometry.getEnvelopeInternal();
					log.debug("invalidate old feature area {}", oldEnvelope);
					cacheManager.invalidate(layer, oldEnvelope);
				}
			}

			// invalidate area for new feature
			InternalFeature feature = context.get(PipelineCode.FEATURE_KEY, InternalFeature.class);
			Geometry geometry = feature.getGeometry();
			if (null != geometry) {
				recorder.record("layer", "Invalidate geometry for new feature");
				Envelope envelope = geometry.getEnvelopeInternal();
				log.debug("invalidate new feature area {}", envelope);
				cacheManager.invalidate(layer, envelope);
			}
		} catch (Throwable t) { //NOPMD
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
}
