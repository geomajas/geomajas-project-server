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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

import org.geomajas.geometry.Crs;
import org.geomajas.global.CacheableObject;
import org.geomajas.service.pipeline.PipelineContext;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.twmacinta.util.MD5;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Implementation of {@link CacheKeyService}.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class CacheKeyServiceImpl implements CacheKeyService {

	private static final int BASE_KEY_LENGTH = 512;

	private static final int SERIALIZED_BUFFER_SIZE = 512;
	private static final int SAFE_ASCII_LOWER = 32;
	private static final int SAFE_ASCII_UPPER = 127;

	private static final char[] CHARACTERS = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	private static final String ENCODING = "UTF-8";

	private final Logger log = LoggerFactory.getLogger(CacheKeyServiceImpl.class);

	private final Random random = new Random();

	/** {@inheritDoc} */
	public String getCacheKey(CacheContext context) {
		try {
			MD5 md5 = new MD5();
			StringBuilder toHash = new StringBuilder(BASE_KEY_LENGTH);
			if (context instanceof CacheContextImpl) {
				CacheContextImpl cci = (CacheContextImpl) context;
				for (Map.Entry<String, Object> entry : cci.entries()) {
					md5.Update(entry.getKey(), ENCODING);
					md5.Update(":");
					if (log.isTraceEnabled()) {
						toHash.append(entry.getKey());
						toHash.append(":");
					}
					Object value = entry.getValue();
					if (null != value) {
						String cid = getCacheId(value);
						md5.Update(cid, ENCODING);
						if (log.isTraceEnabled()) {
							toHash.append(cid);
						}
					}
					md5.Update("-");
					if (log.isTraceEnabled()) {
						toHash.append("-");
					}
				}
			} else {
				String cid = getCacheId(context);
				md5.Update(cid, ENCODING);
				if (log.isTraceEnabled()) {
					toHash.append(cid);
				}
			}
			String key = md5.asHex();
			if (log.isTraceEnabled()) {
				log.trace("key for context {} which is a hash for {}", key, forceAscii(toHash));
			}
			if (log.isDebugEnabled()) {
				log.debug("key for context {}", key);
			}
			
			return key;
		} catch (UnsupportedEncodingException uee) {
			log.error("Impossible error, UTF-8 should be supported:" + uee.getMessage(), uee);
			return null;
		}
	}

	private String getCacheId(Object value) {
		if (value instanceof CacheableObject) {
			return ((CacheableObject) value).getCacheId();
		} else if (value instanceof Crs) {
			return ((Crs) value).getId();
		} else if (value instanceof CoordinateReferenceSystem) {
			return ((CoordinateReferenceSystem) value).toWKT();
		} else if (value instanceof Geometry) {
			return ((Geometry) value).toText();
		} else if (value instanceof Filter) {
			return value.toString();
		} else if (value instanceof Integer) {
			return Integer.toString((Integer) value);
		} else if (value instanceof String) {
			return value.toString();
		} else {
			try {
				log.debug("Serializing {} for unique id", value.getClass().getName());
				ByteArrayOutputStream baos = new ByteArrayOutputStream(SERIALIZED_BUFFER_SIZE);
				JBossObjectOutputStream serialize = new JBossObjectOutputStream(baos);
				serialize.writeObject(value);
				serialize.flush();
				serialize.close();
				return baos.toString("UTF-8");
			} catch (IOException ioe) {
				String fallback = value.toString();
				log.error("Could not serialize " + value + ", falling back to toString() which may cause problems.",
						ioe);
				return fallback;
			}
		}
	}

	/** {@inheritDoc} */
	public CacheContext getCacheContext(PipelineContext pipelineContext, String[] keys) {
		CacheContext res = new CacheContextImpl();
		for (String key : keys) {
			Object value = pipelineContext.getOptional(key);
			if (null != value) {
				res.put(key, value);
			} else {
				log.warn("No value for context key " + key);
			}
		}
		return res;
	}

	/** {@inheritDoc} */
	public String makeUnique(String duplicateKey) {
		log.debug("Need to make key {} unique.", duplicateKey);
		return duplicateKey + CHARACTERS[random.nextInt(CHARACTERS.length)];
	}

	/**
	 * Convert StringBuilder output to a string while escaping characters. This prevents outputting control characters
	 * and unreadable characters. It seems both cause problems for certain editors.
	 * <p/>
	 * The ASCII characters (excluding controls characters) are copied to the result as is, other characters are
	 * escaped using \\uxxxx notation. Note that string comparisons on this result may be inaccurate as both (Java
	 * strings) "\\u1234" and "\u1234" will produce the same converted string!
	 *
	 * @param source StringBuilder to convert
	 * @return String representation using only ASCI characters
	 */
	private String forceAscii(StringBuilder source) {
		int length = source.length();
		StringBuilder res = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			char c = source.charAt(i);
			if ((c >= SAFE_ASCII_LOWER && c <= SAFE_ASCII_UPPER) || Character.isSpaceChar(c)) {
				res.append(c);
			} else {
				res.append("\\u");
				String scode = Integer.toHexString(i);
				while (scode.length() < 4) {
					scode = "0" + scode;
				}
				res.append(scode);
			}
		}
		return res.toString();
	}

}
