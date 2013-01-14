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
