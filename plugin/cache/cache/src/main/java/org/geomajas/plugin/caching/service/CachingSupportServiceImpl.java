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

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.caching.step.CacheContainer;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link CachingSupportService} implementation.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
@Component
public class CachingSupportServiceImpl implements CachingSupportService {

	private final Logger log = LoggerFactory.getLogger(CachingSupportServiceImpl.class);

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private CacheKeyService cacheKeyService;

	@Override
	public <CONTAINER extends CacheContainer> CONTAINER getContainer(String keyKey, String contextKey,
			String[] keys, CacheCategory category, PipelineContext pipelineContext,
			CachingSupportServiceContextAdder contextAdder, Class<CONTAINER> containerClass) {
		CONTAINER cc = null;
		try {
			VectorLayer layer = pipelineContext.getOptional(PipelineCode.LAYER_KEY, VectorLayer.class);
			String cacheKey = null;
			if (keyKey != null) {
				cacheKey = pipelineContext.getOptional(keyKey, String.class);
			}
			if (cacheKey != null) {
				cc = cacheManager.get(layer, category, cacheKey, containerClass);
			}
			if (null == cc) {
				CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
				if (null != contextAdder) {
					contextAdder.addMoreContext(cacheContext); // add more data...
				}

				cacheKey = cacheKeyService.getCacheKey(cacheContext);
				cc = cacheManager.get(layer, category, cacheKey, containerClass);
				while (null != cc) {
					if (!cacheContext.equals(cc.getContext())) {
						cacheKey = cacheKeyService.makeUnique(cacheKey);
						cc = cacheManager.get(layer, category, cacheKey, containerClass);
					} else {
						pipelineContext.put(keyKey, cacheKey);
						pipelineContext.put(contextKey, cacheContext);
						return cc;
					}
				}
			}
		} catch (Throwable t) { //NOPMD
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
		return cc;
	}

	@Override
	public void putContainer(PipelineContext pipelineContext, CachingSupportServiceContextAdder contextAdder,
			CacheCategory category, String[] keys, String keyKey, String contextKey, CacheContainer cacheContainer,
			Envelope envelope) {
		try {
			VectorLayer layer = pipelineContext.getOptional(PipelineCode.LAYER_KEY, VectorLayer.class);
			CacheContext cacheContext = pipelineContext.getOptional(contextKey, CacheContext.class);
			if (null == cacheContext) {
				cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
				if (null != contextAdder) {
					contextAdder.addMoreContext(cacheContext); // add more data...
				}
			}

			cacheContainer.setContext(cacheContext);

			String cacheKey = pipelineContext.getOptional(keyKey, String.class);
			if (null == cacheKey) {
				cacheKey = cacheKeyService.getCacheKey(cacheContext);
			}
			CacheContainer cc = cacheManager.get(layer, category, cacheKey, CacheContainer.class);
			while (null != cc && !cacheContext.equals(cc.getContext())) {
				cacheKey = cacheKeyService.makeUnique(cacheKey);
				cc = cacheManager.get(layer, category, cacheKey, CacheContainer.class);
			}
			if (keyKey != null) {
				pipelineContext.put(keyKey, cacheKey);
			}
			cacheManager.put(layer, category, cacheKey, cacheContainer, envelope);
			pipelineContext.put(keyKey, cacheKey);
		} catch (Throwable t) { //NOPMD
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}

}
