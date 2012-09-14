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
package org.geomajas.plugin.deskmanager.domain.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Kristof Heirwegh
 */
public interface GeodeskInfo extends Serializable {

	String getName();

	Date getCreationDate();

	String getCreationBy();

	Date getLastEditDate();

	String getLastEditBy();

	boolean isActive();

	boolean isPublic();

	/**
	 * Enkel voor public loket.
	 * <p>
	 * Beperk (filter) data volgens grondgebied (via groep) van het loket.
	 * 
	 * @return
	 */
	boolean isLimitToCreatorTerritory();

	/**
	 * Enkel voor niet public loket.
	 * <p>
	 * Beperk (filter) data volgens grondgebied (via groep) van de gebruiker.
	 * 
	 * @return
	 */
	boolean isLimitToUserTerritory();
}
