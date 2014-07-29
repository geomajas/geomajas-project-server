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
package org.geomajas.plugin.deskmanager.service.manager;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Service that handles a two step system for uploading-processing files.
 * First step: temporary saving files to file system.
 * Second step: reading, processing and deleting the files.
 * 
 * @author Jan Venstermans
 *
 */
public interface GenericFileService {

	/**
	 * Will save a file to the filesystem. Returns a string key that is a reference to the file.
	 *
	 * @param file
	 * @return id for referencing the file
	 */
	String saveFile(MultipartFile file);

	/**
	 * Retrieves a file from a given key.
	 *
	 * @param key
	 * @return file
	 */
	File getFile(String key);

	/**
	 * Deletes the file from a given key.
	 *
	 * @param key the key
	 */
	void deleteFile(String key);
}