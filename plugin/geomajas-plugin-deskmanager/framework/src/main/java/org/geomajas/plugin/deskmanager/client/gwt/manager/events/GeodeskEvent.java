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
package org.geomajas.plugin.deskmanager.client.gwt.manager.events;

import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class GeodeskEvent {

	private final GeodeskDto loket;

	private final boolean deleted;

	private final boolean newInstance;

	public GeodeskEvent(GeodeskDto loket) {
		this.loket = loket;
		this.deleted = false;
		this.newInstance = false;
	}

	public GeodeskEvent(GeodeskDto loket, boolean deleted, boolean newInstance) {
		this.loket = loket;
		this.deleted = deleted;
		this.newInstance = newInstance;
	}

	public GeodeskDto getGeodesk() {
		return loket;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isNewInstance() {
		return newInstance;
	}
}
