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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import java.util.ArrayList;
import java.util.List;


/**
 * WoaHandler that already implements the changedhandler methods.
 * 
 * @author Oliver May
 */
public abstract class AbstractWoaHandler implements WoaEventHandler {
	private List<WoaEventHandler.WoaChangedHandler> changedHandlers = 
		new ArrayList<WoaEventHandler.WoaChangedHandler>(); 
	
	public void registerChangedHandler(WoaEventHandler.WoaChangedHandler handler) {
		changedHandlers.add(handler);
	}
	
	public void fireChangedHandler() {
		for (WoaEventHandler.WoaChangedHandler handler : changedHandlers) {
			handler.onChange();
		}
	}
}
