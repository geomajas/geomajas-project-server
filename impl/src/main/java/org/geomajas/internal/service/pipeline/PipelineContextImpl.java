/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service.pipeline;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Context which is provided to a pipeline context to help execute.
 *
 * @author Joachim Van der Auwera
 */
public class PipelineContextImpl implements PipelineContext {

	private Map<String, Object> map = new HashMap<String, Object>();

	private boolean finished;

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}
	
	public Object get(String key) throws GeomajasException {
		Object res = getOptional(key);
		if (null == res) {
			throw new GeomajasException(ExceptionCode.PIPELINE_CONTEXT_MISSING, key);
		}
		return res;
	}

	public Object getOptional(String key) {
		return map.get(key);
	}

	public <TYPE> TYPE get(String key, Class<TYPE> type) throws GeomajasException {
		TYPE res = getOptional(key, type);
		if (null == res) {
			throw new GeomajasException(ExceptionCode.PIPELINE_CONTEXT_MISSING, key);
		}
		return res;
	}

	public <TYPE> TYPE getOptional(String key, Class<TYPE> type, TYPE defaultValue) {
		Object obj = map.get(key);
		if (null != obj && type.isAssignableFrom(obj.getClass())) {
			return (TYPE) obj;
		}
		return defaultValue;
	}

	public <TYPE> TYPE getOptional(String key, Class<TYPE> type) {
		return getOptional(key, type, null);
	}

	public Object put(String key, Object value) {
		if (null != key) {
			return map.put(key, value);
		}
		return null;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
