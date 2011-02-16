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
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.service.pipeline.AbstractPipelineInterceptor;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Pipeline interceptor for testing execution modes.
 *
 * @author Joachim Van der Auwera
 */
public class ForTestInterceptor extends AbstractPipelineInterceptor<StringAttribute> {
	private ExecutionMode executionMode = ExecutionMode.EXECUTE_ALL;

	public void setExecutionMode(ExecutionMode executionMode) {
		this.executionMode = executionMode;
	}

	@Override
	public ExecutionMode beforeSteps(PipelineContext context, StringAttribute response)
			throws GeomajasException {
		response.setValue(response.getValue() + "_before_");
		return executionMode;
	}

	@Override
	public void afterSteps(PipelineContext context, StringAttribute response) throws GeomajasException {
		response.setValue(response.getValue() + "_after_");
	}
}
