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
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class ExtraClientLayerInfo implements ClientUserDataInfo {

	private static final long serialVersionUID = 1L;

	private boolean publicLayer = true;

	private String label;

	private boolean active = true;

	/**
	 * These layers are not shown & cannot be used in Beheer.
	 */
	private boolean systemLayer;

	/**
	 * These layers are shown but cannot be edited in Beheer.
	 */
	private boolean readOnly;

	private boolean showInLegend = true;

	// ----------------------------------------------------------

	public boolean isPublicLayer() {
		return publicLayer;
	}

	public void setPublicLayer(boolean publicLayer) {
		this.publicLayer = publicLayer;
	}

	public String getName() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSystemLayer() {
		return systemLayer;
	}

	public void setSystemLayer(boolean systemLayer) {
		this.systemLayer = systemLayer;
	}

	@Override
	public String toString() {
		return label;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isShowInLegend() {
		return showInLegend;
	}

	public void setShowInLegend(boolean showInLegend) {
		this.showInLegend = showInLegend;
	}
}
