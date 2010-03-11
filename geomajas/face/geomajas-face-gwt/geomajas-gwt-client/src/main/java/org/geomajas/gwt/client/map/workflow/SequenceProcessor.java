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

/**
 * <p>
 * WorkflowProcessor implementation that executes a list of activities sequentially.
 * </p>
 *
 * @author Pieter De Graef
 */
public class SequenceProcessor extends WorkflowProcessor {

	/**
	 * The workflow context object.
	 */
	private WorkflowContext context;

	/**
	 * Default constructor, calling it's super.
	 */
	public SequenceProcessor() {
		super();
	}

	/**
	 * Constructor that immediately takes the workflow context.
	 *
	 * @param context
	 */
	public SequenceProcessor(WorkflowContext context) {
		super();
		this.context = context;
	}

	/**
	 * Is a certain activity supported or not? Just checks if the activity is not null.
	 */
	public boolean supports(Activity activity) {
		return activity != null;
	}

	/**
	 * Start the process.
	 */
	public void doActivities() {
		doActivities(null);
	}

	/**
	 * This method kicks off the processing of workflow activities. It goes over them one by one and executes them.
	 *
	 * @param seedData
	 *            A data object to be passed along to the {@link WorkflowContext} before starting the activities.
	 */
	public void doActivities(Object seedData) {
		// retrieve injected by Spring
		List<Activity> activities = getActivities();

		if (seedData != null) {
			context.setSeedData(seedData);
		}

		for (Activity activity : activities) {
			try {
				context = activity.execute(context);
			} catch (Throwable th) {
				WorkflowErrorHandler errorHandler = activity.getErrorHandler();
				if (errorHandler == null) {
					getDefaultErrorHandler().handleError(context, th);
					break;
				} else {
					errorHandler.handleError(context, th);
				}
			}

			// ensure its ok to continue the process
			if (processShouldStop(context, activity)) {
				break;
			}
		}
	}

	/**
	 * Determine if the process should stop
	 *
	 * @param workflowContext
	 *            the current process context
	 * @param activity
	 *            the current activity in the iteration
	 */
	private boolean processShouldStop(WorkflowContext workflowContext, Activity activity) {
		if (workflowContext != null && workflowContext.stopProcess()) {
			return true;
		}
		return false;
	}

	public WorkflowContext getContext() {
		return context;
	}

	public void setContext(WorkflowContext context) {
		this.context = context;
	}
}
