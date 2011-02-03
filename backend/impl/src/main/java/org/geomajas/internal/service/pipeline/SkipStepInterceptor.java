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

package org.geomajas.internal.service.pipeline;

import org.geomajas.global.GeomajasException;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineInterceptor;

/**
 * Interceptor that unconditionally skips one or more steps.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T>
 */
public class SkipStepInterceptor<T> implements PipelineInterceptor<T> {

	private String id;

	private String toStepId;

	private String fromStepId;

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

	public ExecutionMode beforeSteps(PipelineContext context, T response) throws GeomajasException {
		return ExecutionMode.EXECUTE_NONE;
	}

	public void afterSteps(PipelineContext context, T response) throws GeomajasException {
	}

}
