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

import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;

/**
 * Readonly & no security, you need to be able to read them before your profile is set (they are in a profile...).
 * <p>
 * (Strictly speaking only getByCode is needed before security so rest could be made to pass by security.).
 * 
 * @author Kristof Heirwegh
 */
public interface TerritoryService {

	List<Territory> getTerritories();

	Territory getById(long id);

	Territory getByCode(String code);

	void saveOrUpdateTerritory(Territory group);

	void saveOrUpdateCategory(TerritoryCategory category);

}
