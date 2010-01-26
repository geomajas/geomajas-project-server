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

package org.geomajas.gwt.client.map.workflow.activity;

import org.geomajas.gwt.client.map.workflow.WorkflowContext;
import org.geomajas.gwt.client.map.workflow.WorkflowErrorHandler;
import org.geomajas.gwt.client.map.workflow.WorkflowException;

/**
 * <p>
 * Definition of a single activity/step in a workflow process.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface Activity {

	/**
	 * Get the error handler that is specifically tuned for the activity.
	 */
	WorkflowErrorHandler getErrorHandler();

	/**
	 * This method is called by the encompassing {@link org.geomajas.gwt.client.map.workflow.WorkflowProcessor} to
	 * execute the activity.
	 *
	 * @param context
	 *            The context object used in the workflow.
	 * @return Returns a (the same) context object. It's contents may have changed during this activity.
	 * @throws WorkflowException
	 *             In case something goes wrong, throw an exception.
	 */
	WorkflowContext execute(WorkflowContext context) throws WorkflowException;
}
