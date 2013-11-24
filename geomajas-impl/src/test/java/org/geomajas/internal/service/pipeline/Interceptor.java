package org.geomajas.internal.service.pipeline;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineInterceptor;

/**
 * Interceptor fro testing.
 * 
 * @author Jan De Moerloose
 * 
 * @param <RESPONSE>
 */
public class Interceptor<RESPONSE> implements PipelineInterceptor<StringAttribute> {

	private String fromStepId;

	private String toStepId;

	private String id;

	private ExecutionMode mode = ExecutionMode.EXECUTE_ALL;

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
	
	public ExecutionMode getMode() {
		return mode;
	}
	
	public void setMode(ExecutionMode mode) {
		this.mode = mode;
	}

	public ExecutionMode beforeSteps(PipelineContext context,
			StringAttribute response) throws GeomajasException {
		response.setValue(response.getValue() + "before"+ id);
		return mode;
	}

	public void afterSteps(PipelineContext context, StringAttribute response) throws GeomajasException {
		response.setValue(response.getValue() + "after"+ id);
	}

}
