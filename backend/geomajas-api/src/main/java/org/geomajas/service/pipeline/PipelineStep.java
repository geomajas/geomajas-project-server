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
import org.geomajas.global.UserImplemented;

/**
 * Definition of one execution step in a pipeline.
 *
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
@UserImplemented
public interface PipelineStep<RESPONSE> {

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
	 *
	 * @param context contains a map of objects which are used as shared memory between the pipeline steps
	 * @param response response object for the pipeline service
	 * @throws GeomajasException any exception which may have been throws during the execution
	 */
	void execute(PipelineContext context, RESPONSE response) throws GeomajasException;
}
