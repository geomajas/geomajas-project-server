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
package org.geomajas.widget.searchandfilter.service;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * FileDownloadService.
 *
 * @author Kristof Heirwegh
 */
@Service
public class FileDownloadServiceImpl implements FileDownloadService {

	private final Logger log = LoggerFactory.getLogger(FileDownloadServiceImpl.class);

	private final Object lock = new Object();

	private Map<String, Item> fileMap = new ConcurrentHashMap<String, Item>();

	public boolean hasFile(String id) {
		return fileMap.containsKey(id);
	}

	public Item getFile(String id) {
		return fileMap.get(id);
	}

	public Item removeFile(String id) {
		synchronized (lock) {
			Item it = fileMap.remove(id);
			if (it != null) {
				File f = it.getFile();
				if (f != null) {
					try {
						f.delete();
						log.debug("Deleted a temporary download file: " + f.getName());
					} catch (Exception e) {
						log.warn("Failed to delete temporary download file: " + e.getMessage());
					}
				}
			} else {
				log.warn("No such item: " + id);
			}
			return it;
		}
	}

	public void addFile(String id, File file, String description) {
		fileMap.put(id, new Item(file, description));
	}
}
