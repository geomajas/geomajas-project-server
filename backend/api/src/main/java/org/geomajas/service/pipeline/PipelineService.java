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
 * Service which is allows "executing" a pipeline.
 *
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
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
