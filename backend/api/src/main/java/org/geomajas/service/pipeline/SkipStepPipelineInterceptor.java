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

package org.geomajas.service.pipeline;


import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;

/**
 * Interceptor that unconditionally skips the intercepted steps.
 *
 * @param <T>
 *
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api
public class SkipStepPipelineInterceptor<T> extends AbstractPipelineInterceptor<T> {

	/** {@inheritDoc} */
	@Override
	public ExecutionMode beforeSteps(PipelineContext context, T response) throws GeomajasException {
		return ExecutionMode.EXECUTE_NONE;
	}

}
