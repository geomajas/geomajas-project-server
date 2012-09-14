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

import org.geomajas.plugin.deskmanager.domain.FileRef;
import org.hibernate.HibernateException;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public interface FileRefService {

	FileRef getFileRefById(String id) throws HibernateException;

	String saveOrUpdateFileRef(FileRef fileRef) throws HibernateException;

	void delete(String id) throws HibernateException;
}
