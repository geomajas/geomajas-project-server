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

import java.util.List;

import org.geomajas.gwt.client.map.workflow.activity.Activity;

import com.smartgwt.client.util.SC;

/**
 * <p>
 * Base definition of a processor handling a list of activities as part of the workflow. Different implementations of
 * the processor handle the activities in different ways. For example, the {@link SequenceProcessor} will handle the
 * list of activities sequentially, while a different processor might give you a choice.
 * </p>
 *
 * @author Pieter De Graef
 */
public abstract class WorkflowProcessor {

	/**
	 * The list of activities this processor must try to get through.
	 */
	private List<Activity> activities;

	/**
	 * The default error handler for workflow activities.
	 */
	private WorkflowErrorHandler defaultErrorHandler;

	/**
	 * Protected default constructor that creates a default error handler. This default error handler simply throws up a
	 * warning, visible to the user.
	 */
	protected WorkflowProcessor() {
		defaultErrorHandler = new WorkflowErrorHandler() {

			public void handleError(WorkflowContext context, Throwable throwable) {
				SC.warn(throwable.getMessage());
			}
		};
	}

	/**
	 * Ensures that each activity configured in this process is supported. This method should be called by implemented
	 * subclasses for each activity that is part of the workflow process (the list of activities).
	 *
	 * @param activity
	 * @return
	 */
	public abstract boolean supports(Activity activity);

	/**
	 * This method kicks off the processing of workflow activities.
	 */
	public abstract void doActivities();

	/**
	 * This method kicks off the processing of workflow activities.
	 *
	 * @param seedData
	 *            A data object to be passed along to the {@link WorkflowContext} before starting the activities.
	 */
	public abstract void doActivities(Object seedData);

	/**
	 * Set the collection of Activities to be executed by the Workflow Process.
	 *
	 * @param activities
	 *            ordered collection (List) of activities to be executed by the processor
	 */
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	/**
	 * Set a default error handler, in case an activity throws an exception without it having it's own error handler. In
	 * that case, this default error handler handles the exception.
	 *
	 * @param defaultErrorHandler
	 */
	public void setDefaultErrorHandler(WorkflowErrorHandler defaultErrorHandler) {
		this.defaultErrorHandler = defaultErrorHandler;
	}

	/**
	 * Return the list of activities.
	 */
	public List<Activity> getActivities() {
		return activities;
	}

	/**
	 * Return the default error handler.
	 */
	public WorkflowErrorHandler getDefaultErrorHandler() {
		return defaultErrorHandler;
	}
}
