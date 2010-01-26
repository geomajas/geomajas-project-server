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

import java.util.List;

/**
 * Configuration info for a pipeline service.
 *
 * @param <REQUEST> type of request object for the pipeline
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 */
public class PipelineInfo<REQUEST, RESPONSE> {

	private List<PipelineStep<REQUEST, RESPONSE>> pipeline;

	/**
	 * Get the list of steps which form the pipeline.
	 *
	 * @return list of pipeline steps
	 */
	public List<PipelineStep<REQUEST, RESPONSE>> getPipeline() {
		return pipeline;
	}

	/**
	 * Set the list of steps which form the pipeline.
	 *
	 * @param pipeline list of pipeline steps
	 */
	public void setPipeline(List<PipelineStep<REQUEST, RESPONSE>> pipeline) {
		this.pipeline = pipeline;
	}
}
