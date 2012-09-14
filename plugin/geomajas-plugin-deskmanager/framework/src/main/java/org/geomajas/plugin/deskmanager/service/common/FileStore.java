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

import java.io.InputStream;

/**
 * Don't be misled by the name, this store stores files, they are not necessarily stored in the filesystem.
 * 
 * @author Kristof Heirwegh
 * 
 */
public interface FileStore {

	/**
	 * @param is the binary data
	 * @param feature feature this
	 * @return an identifier that can be used to retrieve this binary (for example a uuid)
	 */
	String persist(InputStream is, String mimeType) throws Exception;

	/**
	 * @param identifier usually uuid identifying the required binary
	 * @return the binary data
	 */
	FileRef retrieve(String identifier) throws Exception;

	void remove(String identifier) throws Exception;
}
