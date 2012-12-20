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
package org.geomajas.plugin.deskmanager.configuration.client;

import org.geomajas.configuration.client.ClientUserDataInfo;

/**
 * Configuration object that contains extra (deskmanager specific) information about layers.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * 
 */
public class DeskmanagerClientLayerInfo implements ClientUserDataInfo, ClientLayerInfo {

	private static final long serialVersionUID = 1L;

	private boolean publicLayer = true;

	private String name;

	private boolean active = true;

	private boolean systemLayer;


	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo#isPublicLayer()
	 */
	@Override
	public boolean isPublic() {
		return publicLayer;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo#setPublicLayer(boolean)
	 */
	@Override
	public void setPublic(boolean publicLayer) {
		this.publicLayer = publicLayer;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo#isActive()
	 */
	@Override
	public boolean isActive() {
		return active;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo#setActive(boolean)
	 */
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo#isSystemLayer()
	 */
	@Override
	public boolean isSystemLayer() {
		return systemLayer;
	}

	/* (non-Javadoc)
	 * @see org.geomajas.plugin.deskmanager.configuration.client.ClientLayerInfo#setSystemLayer(boolean)
	 */
	@Override
	public void setSystemLayer(boolean systemLayer) {
		this.systemLayer = systemLayer;
	}

	@Override
	public String toString() {
		return name;
	}

}
