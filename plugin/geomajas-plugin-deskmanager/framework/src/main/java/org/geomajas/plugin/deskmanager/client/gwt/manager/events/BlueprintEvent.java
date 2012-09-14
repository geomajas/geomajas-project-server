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

import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;

/**
 * ...
 * 
 * @author Jan De Moerloose
 *
 */
public class BlueprintEvent {

	private final BlueprintDto blueprint;

	private final boolean deleted;

	private final boolean newInstance;

	public BlueprintEvent(BlueprintDto blueprint) {
		this.blueprint = blueprint;
		this.deleted = false;
		this.newInstance = false;
	}

	public BlueprintEvent(BlueprintDto blueprint, boolean deleted, boolean newInstance) {
		this.blueprint = blueprint;
		this.deleted = deleted;
		this.newInstance = newInstance;
	}

	public BlueprintDto getBlueprint() {
		return blueprint;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isNewInstance() {
		return newInstance;
	}
}
