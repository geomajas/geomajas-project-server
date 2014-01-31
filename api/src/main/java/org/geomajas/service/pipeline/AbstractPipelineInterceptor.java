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

package org.geomajas.service.pipeline;

import javax.annotation.PostConstruct;

import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;

/**
 * Interceptor that unconditionally skips one or more steps.
 * 
 * @param <T>
 *
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api
public class AbstractPipelineInterceptor<T> implements PipelineInterceptor<T> {

	private String id;

	private String toStepId;

	private String fromStepId;

	private String stepId;

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Set the id for this interceptor.
	 *
	 * @param id id
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getFromStepId() {
		return fromStepId;
	}

	/**
	 * Set id of first step to intercept.
	 *
	 * @param fromStepId from step id
	 * @since 1.9.0
	 */
	@Api
	public void setFromStepId(String fromStepId) {
		this.fromStepId = fromStepId;
	}

	@Override
	public String getToStepId() {
		return toStepId;
	}

	/**
	 * Set id of last step to intercept.
	 *
	 * @param toStepId to step id
	 * @since 1.9.0
	 */
	@Api
	public void setToStepId(String toStepId) {
		this.toStepId = toStepId;
	}
	
	/**
	 * Shortcut notation for setting the same from and to step id.
	 *
	 * @param stepId step id
	 * @since 1.9.0
	 */
	@Api
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	@Override
	public ExecutionMode beforeSteps(PipelineContext context, T response) throws GeomajasException {
		return ExecutionMode.EXECUTE_ALL;
	}

	@Override
	public void afterSteps(PipelineContext context, T response) throws GeomajasException {
	}

	/**
	 * Finish the configuration.
	 */
	@PostConstruct
	protected void postConstruct() {
		if (stepId != null) {
			setFromStepId(stepId);
			setToStepId(stepId);
		} 
	}

}
