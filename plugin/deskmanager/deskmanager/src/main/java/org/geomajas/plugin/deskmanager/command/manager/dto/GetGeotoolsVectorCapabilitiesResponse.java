/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.manager.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;

/**
 * @author Kristof Heirwegh
 */
public class GetGeotoolsVectorCapabilitiesResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<VectorCapabilitiesInfo> layers = new ArrayList<VectorCapabilitiesInfo>();

	public List<VectorCapabilitiesInfo> getVectorCapabilities() {
		return layers;
	}

	public void setVectorCapabilities(List<VectorCapabilitiesInfo> layers) {
		this.layers = layers;
	}
}
