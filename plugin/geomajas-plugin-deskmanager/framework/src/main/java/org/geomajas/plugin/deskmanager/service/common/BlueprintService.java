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
package org.geomajas.plugin.deskmanager.service.common;

import java.util.List;

import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.security.GeomajasSecurityException;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public interface BlueprintService {

	Blueprint getBlueprintById(String uuid) throws GeomajasSecurityException;

	/**
	 * No security check, only for internal use!
	 */
	Blueprint getBlueprintByIdInternal(String uuid);

	List<Blueprint> getBlueprints() throws GeomajasSecurityException;

	List<Blueprint> getBlueprintsInternal();

	/**
	 * Blueprints are never really deleted, but marked as deleted.
	 */
	void deleteBlueprint(Blueprint bp) throws GeomajasSecurityException;

	void saveOrUpdateBlueprint(Blueprint bp) throws GeomajasSecurityException;

	/**
	 * 
	 */
	void updateBluePrintFromUserApplication(Blueprint bp);
}
