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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GetGeodesksResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private List<GeodeskDto> loketten = new ArrayList<GeodeskDto>();

	public List<GeodeskDto> getGeodesks() {
		return loketten;
	}

	public void setGeodesks(List<GeodeskDto> loketten) {
		this.loketten = loketten;
	}

}
