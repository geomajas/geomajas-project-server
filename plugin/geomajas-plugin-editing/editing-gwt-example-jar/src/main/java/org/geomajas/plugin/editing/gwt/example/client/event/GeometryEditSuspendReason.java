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

package org.geomajas.plugin.editing.gwt.example.client.event;

/**
 * A descriptor for the reason why the normal editing process has gone into suspension.
 * 
 * @author Pieter De Graef
 */
public enum GeometryEditSuspendReason {

	/** Default reason. */
	UNKNOWN,

	/**
	 * Suspension is caused because we're waiting for a remove-ring event. Only when the user has clicked on an inner
	 * ring, will editing continue.
	 */
	REMOVE_RING
}