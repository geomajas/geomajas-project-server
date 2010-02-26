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

package org.geomajas.internal.service.pipeline;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.pipeline.PipelineContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Context which is provided to a pipeline context to help execute.
 *
 * @author Joachim Van der Auwera
 */
public class PipelineContextImpl implements PipelineContext {

	private Map<String, Object> map = new HashMap<String, Object>();

	private boolean finished;

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

	public <TYPE> TYPE getOptional(String key, Class<TYPE> type) {
		Object obj = map.get(key);
		if (null != obj && type.isAssignableFrom(obj.getClass())) {
			return (TYPE) obj;
		}
		return null;
	}

	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
