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

package org.geomajas.rendering.pipeline;

import org.geomajas.global.GeomajasException;

import java.util.Map;

/**
 * Definition of one execution step in a pipeline.
 *
 * @param <REQUEST> type of request object for the pipeline
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 */
public interface PipelineStep<REQUEST, RESPONSE> {

	/**
	 * Get the id for the step. This is used for possible skipping and looping in the pipeline.
	 *
	 * @return pipeline step id
	 */
	String getId();

	/**
	 * Execute this step in the pipeline.
	 * <p/>
	 * This is expected to modify and transform both the parameters and response objects.
	 * <p/>
	 * The result indicates how the pipeline should continue. Following values are possible :
	 * <ul>
	 * <li>null : Continue to the next step.</li>
	 * <li>PipelineService.STOP : Stop the rendering pipeline, return the result object.</li>
	 * <li>"step-id" : Continue the pipeline at given step. This will skip steps in the pipeline if the requested step
	 * is not the next one. When no step with given id exists in the pipeline after the current step, an exception is
	 * thrown.</li>
	 * <li>"-" : Restart the pipeline. This could in some cases be used to handle certain exceptions by changing one of
	 * the parameters. It is however not recommended to use this.</li>
	 * <li>"-step-id" : Go back the to a previous step in the pipeline (effectively creating a loop). This will go back
	 * in the pipeline to the step with given id. When no step with given id exists in the pipeline before the current
	 * step, an exception is thrown. To prevent infinite loops, there is a limit to the number of steps which can be
	 * performed in a pipeline.</li>
	 * <li></li>
	 * </ul>
	 *
	 * @param request request object, contains parameters for the pipeline service
	 * @param parameters map of objects which are used as shared memory between the pipeline steps
	 * @param response response object for the pipeline service
	 * @return instructions for progressing the pipeline
	 * @throws GeomajasException any exception which may have been throws during the execution
	 */
	String execute(REQUEST request, Map<String, Object> parameters, RESPONSE response) throws GeomajasException;
}
