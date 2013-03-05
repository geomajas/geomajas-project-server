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
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;

/**
 * Request object for {@link org.geomajas.plugin.deskmanager.command.manager.SaveLayerModelCommand}.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class SaveLayerModelRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.deskmanager.manager.SaveLayerModel";

	/**
	 * Bitmask to save settings. 
	 */
	public static final int SAVE_SETTINGS = 1;

	/**
	 * Bitmask to save all clientwidgetinfo's.
	 */
	public static final int SAVE_CLIENTWIDGETINFO = 32;


	private LayerModelDto layerModel;

	private int saveBitmask;
	
	public LayerModelDto getLayerModel() {
		return layerModel;
	}

	public void setLayerModel(LayerModelDto layerModel) {
		this.layerModel = layerModel;
	}

	/**
	 * Set the bitmask of what to save.
	 * 
	 * @param saveBitmask
	 */
	public void setSaveBitmask(int saveBitmask) {
		this.saveBitmask = saveBitmask;
	}
	
	/**
	 * Get the bitmask of what needs to be saved. 
	 */ 
	public int getSaveBitmask() {
		return saveBitmask;
	}

}
