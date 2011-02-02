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

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheKeyService;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Abstract base class for pipeline interceptors that implement a cache cycle.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T>
 */
public abstract class AbstractCachingInterceptor<T> implements PipelineInterceptor<T> {

	private final Logger log = LoggerFactory.getLogger(AbstractCachingInterceptor.class);

	private String id;

	private String fromStepId;

	private String toStepId;

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private CacheKeyService cacheKeyService;

	@Autowired
	private SecurityContext securityContext;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromStepId() {
		return fromStepId;
	}

	public void setFromStepId(String fromStepId) {
		this.fromStepId = fromStepId;
	}

	public String getToStepId() {
		return toStepId;
	}

	public void setToStepId(String toStepId) {
		this.toStepId = toStepId;
	}

	protected <CONTAINER extends CacheContainer> CONTAINER getContainer(String[] keys, CacheCategory category,
			PipelineContext pipelineContext) throws GeomajasException {
		return (CONTAINER) getContainer(null, keys, category, pipelineContext);
	}

	protected <CONTAINER extends CacheContainer> CONTAINER getContainer(String keyKey, String[] keys,
			CacheCategory category, PipelineContext pipelineContext) throws GeomajasException {
		VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		CONTAINER cc = null;
		String cacheKey = null;
		if (keyKey != null) {
			cacheKey = pipelineContext.getOptional(keyKey, String.class);
		}
		if (cacheKey != null) {
			cc = (CONTAINER) cacheManager.get(layer, category, cacheKey);
			if (securityContext == null) {
				// deserialize cached context
				securityContext = (SecurityContext) cc.getContext().get(CacheContext.SECURITY_CONTEXT_KEY);
			}
		} else {
			// context should have all keys !
			for (String key : keys) {
				if (pipelineContext.getOptional(key) == null) {
					return null;
				}
			}
			CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
			cacheKey = cacheKeyService.getCacheKey(layer, category, cacheContext);
			cc = (CONTAINER) cacheManager.get(layer, category, cacheKey);
			while (null != cc) {
				if (!cacheContext.equals(cc.getContext())) {
					cacheKey = cacheKeyService.makeUnique(cacheKey);
					cc = (CONTAINER) cacheManager.get(layer, category, cacheKey);
				} else {
					return cc;
				}
			}
		}

		return cc;
	}

	protected void putContainer(PipelineContext pipelineContext, CacheCategory category, String[] keys, String keyKey,
			CacheContainer cacheContainer, Envelope envelope) throws GeomajasException {
		try {
			// recorder.record(category, "Put item in cache");
			VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
			if (securityContext == null) {
				// deserialize cached context
				cacheContext.put(CacheContext.SECURITY_CONTEXT_KEY, securityContext);
			}
			cacheContainer.setContext(cacheContext);
			String cacheKey = cacheKeyService.getCacheKey(layer, category, cacheContext);
			Object cc = cacheManager.get(layer, category, cacheKey);
			while (null != cc) {
				cacheKey = cacheKeyService.makeUnique(cacheKey);
				cc = cacheManager.get(layer, category, cacheKey);
			}
			cacheManager.put(layer, category, cacheKey, cacheContainer, envelope);
			pipelineContext.put(keyKey, cacheKey);
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}

}
