/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Put bounds in cache for later retrieval.
 *
 * @author Joachim Van der Auwera
 * @param <TYPE> type of response object for pipeline.
 */
public abstract class AbstractPutInCacheStep<TYPE> implements PipelineStep<TYPE> {

	private final Logger log = LoggerFactory.getLogger(AbstractPutInCacheStep.class);

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private TestRecorder recorder;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext pipelineContext, CacheCategory category, String keyKey, String contextKey,
			String useKey, CacheContainer cacheContainer, Envelope envelope) throws GeomajasException {
		try {
			recorder.record(category, "Put item in cache");
			VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);

			String cacheKey = pipelineContext.get(keyKey, String.class);
			CacheContext cacheContext = pipelineContext.get(contextKey, CacheContext.class);

			cacheContainer.setContext(cacheContext);
			cacheManager.put(layer, category, cacheKey, cacheContainer, envelope);
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
}
