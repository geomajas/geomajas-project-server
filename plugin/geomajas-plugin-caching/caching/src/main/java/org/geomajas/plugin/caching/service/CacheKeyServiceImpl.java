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

package org.geomajas.plugin.caching.service;

import com.twmacinta.util.MD5;
import org.geomajas.global.CacheableObject;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.service.pipeline.PipelineContext;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

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

	public String getCacheKey(Layer layer, CacheCategory category, CacheContext context) {
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
			md5.Update("$");
			md5.Update(layer.getId());
			md5.Update("-");
			md5.Update(category.getName());
			if (log.isDebugEnabled()) {
				toHash += "$" + layer.getId() + "-" + category.getName();
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
		return duplicateKey + CHARACTERS[random.nextInt(36)];
	}
}
