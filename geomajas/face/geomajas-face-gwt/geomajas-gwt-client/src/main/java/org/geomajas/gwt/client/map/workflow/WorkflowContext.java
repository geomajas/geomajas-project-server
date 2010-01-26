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

package org.geomajas.gwt.client.map.workflow;

/**
 * <p>
 * Very elementary interface for a general context regarding workflow. It has one method to regulate the stopping of the
 * workflow process, and one to pass along some seed data before starting.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface WorkflowContext {

	/**
	 * Informs the {@link WorkflowProcessor} to stop the processing of activities. It is the {@link WorkflowProcessor}s
	 * responsibility to ask for this, and execute no more activities when "true" is returned.
	 */
	boolean stopProcess();

	/**
	 * Provide some seed information to the context. This is usually provided at the time of workflow kick off.
	 *
	 * @param seedData
	 */
	void setSeedData(Object seedData);
}
