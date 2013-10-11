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
 * General definition of an exception thrown during the execution of an activity. If the activity encounters an error,
 * it will throw a WorkflowException.
 *
 * @author Pieter De Graef
 */
public class WorkflowException extends Exception {

	private static final long serialVersionUID = 151L;

	public WorkflowException() {
		super();
	}

	public WorkflowException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public WorkflowException(String arg0) {
		super(arg0);
	}

	public WorkflowException(Throwable arg0) {
		super(arg0);
	}
}
