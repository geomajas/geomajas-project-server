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

package org.geomajas.service.pipeline;

import org.geomajas.global.Api;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Configuration info for a pipeline service.
 *
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class PipelineInfo<RESPONSE> {

	@NotNull
	private String pipelineName;

	private String layerId;

	private PipelineInfo delegatePipeline;

	private List<PipelineStep<RESPONSE>> pipeline;
	
	/**
	 * Get the name of the pipeline for which this is an implementation.
	 *
	 * @return pipeline name
	 */
	public String getPipelineName() {
		return pipelineName;
	}

	/**
	 * Set the name of the pipeline for which steps are defined.
	 *
	 * @param pipelineName pipeline name
	 */
	public void setPipelineName(String pipelineName) {
		this.pipelineName = pipelineName;
	}

	/**
	 * Set the layer for which this definition should apply.
	 * <p/>
	 * When this is not set (or null), it indicates a candidate default implementation.
	 *
	 * @return layer for which this pipeline applies
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Get the layer id for which this pipeline applies.
	 * <p/>
	 * When this is not set (or null), it indicates a candidate default implementation.
	 *
	 * @param layerId layer for which this pipeline applies
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	/**
	 * Get the delegate pipeline.
	 * <p/>
	 * When this is set, the pipeline for the delegate should be used instead of the pipeline defined in this bean.
	 *
	 * @return delegate pipeline definition
	 */
	public PipelineInfo getDelegatePipeline() {
		return delegatePipeline;
	}

	/**
	 * Define a delegate pipeline.
	 * <p/>
	 * When this is set, the pipeline for the delegate should be used instead of the pipeline defined in this bean.
	 *
	 * @param delegatePipeline delegate pipeline definition
	 */
	public void setDelegatePipeline(PipelineInfo delegatePipeline) {
		this.delegatePipeline = delegatePipeline;
	}

	/**
	 * Get the list of steps which form the pipeline.
	 *
	 * @return list of pipeline steps
	 */
	public List<PipelineStep<RESPONSE>> getPipeline() {
		return pipeline;
	}

	/**
	 * Set the list of steps which form the pipeline.
	 *
	 * @param pipeline list of pipeline steps
	 */
	public void setPipeline(List<PipelineStep<RESPONSE>> pipeline) {
		this.pipeline = pipeline;
	}
}
