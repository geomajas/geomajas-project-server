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

package org.geomajas.service.pipeline;

import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;

/**
 * A pipeline interceptor allows to perform some action before and after a consecutive series of pipeline steps. It also
 * allows to optionally skip those steps and/or the after action by letting the before action returning different
 * execution modes.
 *
 * Do not implement this class, extend {@link AbstractPipelineInterceptor} instead.
 * 
 * @param <RESPONSE>
 *            type of response object for the pipeline
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
public interface PipelineInterceptor<RESPONSE> {

	/**
	 * Execution mode. This allows to execute both steps and after action, nothing, just the steps or just the after
	 * action.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	enum ExecutionMode {
		EXECUTE_ALL, EXECUTE_NONE, EXECUTE_STEPS, EXECUTE_AFTER
	}

	/**
	 * Get the id for the interceptor. This can be used to identify the interceptor step in the pipeline.
	 *
	 * @return interceptor id
	 */
	String getId();

	/**
	 * The id of the first step to intercept.
	 * 
	 * @return pipeline step id
	 */
	String getFromStepId();

	/**
	 * The id of the last step to intercept.
	 * 
	 * @return pipeline step id
	 */
	String getToStepId();

	/**
	 * Perform some action before the steps are executed.
	 * 
	 * @param context
	 *            pipeline context
	 * @param response
	 *            pipeline response
	 * @return the execution mode
	 * @throws GeomajasException any exception which may have been thrown during the execution
	 */
	ExecutionMode beforeSteps(PipelineContext context, RESPONSE response) throws GeomajasException;

	/**
	 * Perform some action before the steps are executed.
	 * 
	 * @param context
	 *            pipeline context
	 * @param response
	 *            pipeline response
	 * @throws GeomajasException any exception which may have been thrown during the execution
	 */
	void afterSteps(PipelineContext context, RESPONSE response) throws GeomajasException;

}
