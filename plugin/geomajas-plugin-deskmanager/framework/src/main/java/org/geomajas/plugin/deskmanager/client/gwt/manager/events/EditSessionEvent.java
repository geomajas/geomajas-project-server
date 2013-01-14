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
package org.geomajas.plugin.deskmanager.client.gwt.manager.events;

import com.smartgwt.client.widgets.Canvas;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class EditSessionEvent {

	private final Canvas requestee;

	private final boolean start;

	/**
	 * @param start
	 * @param requestee the object that is handling the editevent and should not be disabled
	 */
	public EditSessionEvent(boolean start, Canvas requestee) {
		this.requestee = requestee;
		this.start = start;
	}

	public boolean isSessionStart() {
		return start;
	}

	public boolean isSessionEnd() {
		return !start;
	}

	public Canvas getRequestee() {
		return requestee;
	}

	public boolean isParentOfRequestee(Canvas parent) {
		// -- > This doesn't work in al cases, so not reliable
		// !tab.getPane().contains(ese.getRequestee())

		Canvas c = requestee;
		while (c != null) {
			if (c.equals(parent)) {
				return true;
			} else {
				c = c.getParentElement();
			}
		}
		return false;
	}
}
