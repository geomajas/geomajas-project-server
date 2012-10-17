/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.widgets.layout.VLayout;


/**
 * @author Oliver May
 *
 */
public abstract class AbstractConfigurationLayout extends VLayout implements WoaEventHandler {

	private List<ChangedHandler> changedHandlers = new ArrayList<AbstractConfigurationLayout.ChangedHandler>(); 
	
	public void registerChangedHandler(ChangedHandler handler) {
		changedHandlers.add(handler);
	}
	
	protected void fireChangedHandler() {
		for (ChangedHandler handler : changedHandlers) {
			handler.onChange();
		}
	}
	
	/**
	 * Listens to change events that may trigger changes on the buttonbar layout.
	 * 
	 * @author Oliver May
	 * 
	 */
	public interface ChangedHandler {

		/**
		 * Called when something had changed.
		 */
		void onChange();
	}

}
