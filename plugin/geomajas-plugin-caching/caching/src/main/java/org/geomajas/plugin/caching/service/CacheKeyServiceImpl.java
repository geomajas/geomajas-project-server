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

package org.geomajas.plugin.caching.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

import org.geomajas.global.CacheableObject;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.pipeline.PipelineContext;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.twmacinta.util.MD5;

/**
 * Implementation of {@link CacheKeyService}.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class CacheKeyServiceImpl implements CacheKeyService {

	private static final char[] CHARACTERS = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	private static final String ENCODING = "UTF-8";

	private final Logger log = LoggerFactory.getLogger(CacheKeyServiceImpl.class);

	private Random random = new Random();

	public String getCacheKey(CacheContext context) {
		try {
			MD5 md5 = new MD5();
			String toHash = "";
			if (context instanceof CacheContextImpl) {
				CacheContextImpl cci = (CacheContextImpl) context;
				for (Map.Entry<String, Object> entry : cci.entries()) {
					md5.Update(entry.getKey(), ENCODING);
					md5.Update(":");
					if (log.isDebugEnabled()) {
						toHash += entry.getKey() + ":";
					}
					Object value = entry.getValue();
					if (null != value) {
						String cid = getCacheId(value);
						md5.Update(cid, ENCODING);
						if (log.isDebugEnabled()) {
							toHash += cid;
						}
					}
					md5.Update("-");
					if (log.isDebugEnabled()) {
						toHash += "-";
					}
				}
			} else {
				String cid = getCacheId(context);
				md5.Update(cid, ENCODING);
				if (log.isDebugEnabled()) {
					toHash += cid;
				}
			}
			String key = md5.asHex();
			log.debug("key for context {} which is a hash for {}", key, toHash);
			return key;
		} catch (UnsupportedEncodingException uee) {
			log.error("Impossible error, UTF-8 should be supported:" + uee.getMessage(), uee);
			return null;
		}
	}

	private String getCacheId(Object value) {
		if (value instanceof CoordinateReferenceSystem) {
			return ((CoordinateReferenceSystem) value).getIdentifiers().toString();
		} else if (value instanceof CacheableObject) {
			return ((CacheableObject) value).getCacheId();
		} else {
			return value.toString();
		}
	}

	public CacheContext getCacheContext(PipelineContext pipelineContext, String[] keys) {
		CacheContext res = new CacheContextImpl();
		for (String key : keys) {
			try {
				res.put(key, pipelineContext.get(key));
			} catch (GeomajasException ge) {
				log.error(ge.getMessage(), ge);
			}
		}
		return res;
	}

	public String makeUnique(String duplicateKey) {
		log.debug("Need to make key {} unique.", duplicateKey);
		return duplicateKey + CHARACTERS[random.nextInt(36)];
	}
}
