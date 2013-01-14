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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.util;

/**
 * @author Oliver May
 * 
 */
public final class CodeServer {
	
	private CodeServer() { }

	public static String getCodeServer() {
		String codeServer = com.google.gwt.user.client.Window.Location.getParameter("gwt.codesvr");
		if (codeServer != null) {
			codeServer = "?gwt.codesvr=" + codeServer;
		} else {
			codeServer = "";
		}
		return codeServer;
	}
}
