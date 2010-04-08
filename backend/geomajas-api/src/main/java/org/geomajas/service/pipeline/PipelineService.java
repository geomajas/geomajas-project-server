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
import org.geomajas.global.GeomajasException;

/**
 * Service which is allows "executing" a pipeline.
 *
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public interface PipelineService<RESPONSE> {

	/**
	 * Execute the pipeline, starting with a known context.
	 *
	 * @param pipeline pipeline steps / configuration
	 * @param context known context (can be null if there are no parameters for the pipeline)
	 * @param response response object which is filled by the pipeline
	 * @throws GeomajasException any exception which may have been throws during the execution
	 */
	void execute(PipelineInfo<RESPONSE> pipeline, PipelineContext context, RESPONSE response)
			throws GeomajasException;

	/**
	 * Execute the pipeline, starting with a known context.
	 *
	 * @param key key for the pipeline
	 * @param layerId layer if
	 * @param context known context (can be null if there are no parameters for the pipeline)
	 * @param response response object which is filled by the pipeline
	 * @throws GeomajasException any exception which may have been throws during the execution
	 */
	void execute(String key, String layerId, PipelineContext context, RESPONSE response)
			throws GeomajasException;

	/**
	 * Get a pipeline configuration based on the key and optional layer id.
	 * <p/>
	 * This will attempt to get the layer specific pipeline and if that does not exist, it will use the general
	 * pipeline. The layer specific pipeline is assumed to have bean name <code>key + "." + layerId</code>.
	 *
	 * @param key key for the pipeline
	 * @param layerId layer if
	 * @return pipeline configuration
	 * @throws GeomajasException when pipeline info not found
	 */
	PipelineInfo<RESPONSE> getPipeline(String key, String layerId) throws GeomajasException;

	/**
	 * Get a new pipeline context.
	 *
	 * @return new pipeline context
	 */
	PipelineContext createContext();
}
