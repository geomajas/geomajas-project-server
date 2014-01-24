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
package org.geomajas.plugin.deskmanager.service.common;

import java.util.List;

import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.security.GeomajasSecurityException;

/**
 *  
 * @author Jan De Moerloose
 *
 */
public interface GeodeskService {

	Geodesk getGeodeskById(String uuid) throws GeomajasSecurityException;

	/**
	 * This method will check all security, so not for internal use.
	 */
	Geodesk getGeodeskByPublicId(String id) throws GeomajasSecurityException;

	/**
	 * This method won't check security, so for internal use.
	 */
	Geodesk getGeodeskByPublicIdInternal(String id);

	String getGeodeskNameByPublicId(String id) throws GeomajasSecurityException;

	List<Geodesk> getGeodesks() throws GeomajasSecurityException;

	void deleteGeodesk(Geodesk gd) throws GeomajasSecurityException;

	void saveOrUpdateGeodesk(Geodesk gd) throws GeomajasSecurityException;

	/**
	 * No security check here.
	 * 
	 * @param publicId
	 * @return
	 */
	boolean geodeskExists(String publicId);

	boolean isGeodeskUseAllowed(String id, Role role, Territory group);

	boolean isGeodeskReadAllowed(Geodesk gd, Role role, Territory group);

	boolean isGeodeskSaveAllowed(Geodesk gd, Role role, Territory group);

	boolean isGeodeskDeleteAllowed(Geodesk gd, Role role, Territory group);

	/**
	 * This is slightly different than LoketExists, where the former will not check inactive & deleted loketten, this
	 * method will check all (as key needs to be UNIQUE).
	 * 
	 * @param publicId
	 * @return
	 */
	boolean geodeskIdExists(String publicId);
}
