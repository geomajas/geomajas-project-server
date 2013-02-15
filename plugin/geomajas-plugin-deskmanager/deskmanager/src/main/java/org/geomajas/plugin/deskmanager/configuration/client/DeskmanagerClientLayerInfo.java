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
package org.geomajas.plugin.deskmanager.configuration.client;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientUserDataInfo;

/**
 * Configuration object that contains extra (deskmanager specific) information about layers.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public class DeskmanagerClientLayerInfo implements ClientUserDataInfo {

	private static final long serialVersionUID = 1L;

	private boolean publicLayer = true;

	private String name;

	private boolean active = true;

	private boolean systemLayer;


	/**
	 * Whether a layer is public.
	 * 
	 * @return true if the layer is public.
	 */
	public boolean isPublic() {
		return publicLayer;
	}

	/**
	 * Set whether a layer is public or not.
	 * 
	 * @param publicLayer
	 */
	public void setPublic(boolean publicLayer) {
		this.publicLayer = publicLayer;
	}

	/**
	 * Get the name of a layer. This name is used in the management interface.
	 * 
	 * @return the name of the layer
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the layer. This name is used in the management interface.
	 * 
	 * @param name
	 *            the name of the layer
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Whether the layer is active.
	 * 
	 * @return true if the layer is active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set whether the layer is active or not.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Whether the layer is a system layer. A system layer is a layer that is configured using the spring xml
	 * configuration, not added dynamically.
	 * 
	 * @return true if the layer is a system layer.
	 */
	public boolean isSystemLayer() {
		return systemLayer;
	}

	/**
	 * Set whether layer is a system layer. A system layer is a layer that is configured using the spring xml
	 * configuration, not added dynamically.
	 * 
	 * @param systemLayer
	 */
	public void setSystemLayer(boolean systemLayer) {
		this.systemLayer = systemLayer;
	}

	@Override
	public String toString() {
		return name;
	}

}
