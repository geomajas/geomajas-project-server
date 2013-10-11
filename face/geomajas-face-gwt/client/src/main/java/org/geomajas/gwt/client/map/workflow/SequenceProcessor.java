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
