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

/**
 * <p>
 * Interface for an error handler, handling exceptions that were thrown during the activities' execution phase.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface WorkflowErrorHandler {

	/**
	 * Executed when an activity throws an exception during execution. The WorkflowProcessor must make such this method
	 * is executed.
	 *
	 * @param context
	 *            The currently used workflow context.
	 * @param throwable
	 *            The error that was thrown by one of the activities.
	 */
	void handleError(WorkflowContext context, Throwable throwable);
}
