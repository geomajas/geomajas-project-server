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
import org.geomajas.service.pipeline.PipelineInfo;
import org.geomajas.service.pipeline.PipelineService;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Service which is allows "executing" a pipeline.
 *
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 */
@Component
public class PipelineServiceImpl<RESPONSE> implements PipelineService<RESPONSE> {

	@Autowired
	private ApplicationContext applicationContext;

	/** @inheritDoc */
	public void execute(String key, String layerId, PipelineContext context, RESPONSE response)
			throws GeomajasException {
		execute(getPipeline(key, layerId), context, response);
	}

	/** @inheritDoc */
	public void execute(PipelineInfo<RESPONSE> pipeline, PipelineContext startContext, RESPONSE response)
			throws GeomajasException {
		PipelineContext context = startContext;
		if (null == context) {
			context = createContext();
		}
		for (PipelineStep<RESPONSE> step : pipeline.getPipeline()) {
			if (context.isFinished()) {
				break;
			}
			step.execute(context, response);
		}
	}

	/** @inheritDoc */
	public PipelineInfo<RESPONSE> getPipeline(String pipelineName, String layerId) throws GeomajasException {
		PipelineInfo<RESPONSE> layerPipeline = null;
		PipelineInfo<RESPONSE> defaultPipeline = null;
		Collection<PipelineInfo> pipelines = applicationContext.getBeansOfType(PipelineInfo.class).values();
		for (PipelineInfo<RESPONSE> pipeline : pipelines) {
			if (pipeline.getPipelineName().equals(pipelineName)) {
				String lid = pipeline.getLayerId();
				if (null == lid) {
					defaultPipeline = pipeline;
				} else if (lid.equals(layerId)) {
					layerPipeline = pipeline;
				}
			}
		}
		if (null == layerPipeline) {
			layerPipeline = defaultPipeline;
		}
		if (null == layerPipeline) {
			throw new GeomajasException(ExceptionCode.PIPELINE_UNKNOWN, pipelineName, layerId);
		}
		while (null != layerPipeline.getDelegatePipeline()) {
			layerPipeline = layerPipeline.getDelegatePipeline();
		}
		return layerPipeline;
	}

	/** @inheritDoc */
	public PipelineContext createContext() {
		return new PipelineContextImpl();
	}
}
