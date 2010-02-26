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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Configuration info for a pipeline service.
 *
 * @param <RESPONSE> type of response object for the pipeline
 *
 * @author Joachim Van der Auwera
 */
public class PipelineInfo<RESPONSE> {

	@NotNull
	private String id;

	private List<PipelineStep<RESPONSE>> pipeline;

	/**
	 * Get the pipeline id. This is the ide which is used to retrieve a specific pipeline.
	 *
	 * @return pipeline id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the pipeline id.
	 *
	 * @param id pipeline id
	 */
	public void setId(String id) {
		this.id = id;
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
