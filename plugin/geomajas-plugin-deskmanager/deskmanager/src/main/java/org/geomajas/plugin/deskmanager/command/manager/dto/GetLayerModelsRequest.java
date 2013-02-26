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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandRequest;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GetLayerModelsRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.manager.GetLayerModels";
	
	private String locale; 

	public String toString() {
		return COMMAND;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getLocale() {
		return locale;
	}

}
