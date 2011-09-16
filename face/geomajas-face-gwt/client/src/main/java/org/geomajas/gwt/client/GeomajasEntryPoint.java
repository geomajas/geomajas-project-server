/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.gwt.GwtCommandCallback;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

/**
 * Entry point for the Geomajas GWT face.
 *
 * @author Joachim Van der Auwera
 */
public class GeomajasEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		GwtCommandDispatcher dispatcher =  GwtCommandDispatcher.getInstance();
		GwtCommandCallback callback = new GwtCommandCallback();
		dispatcher.setCommandExceptionCallback(callback);
		dispatcher.setCommunicationExceptionCallback(callback);
	}
}
