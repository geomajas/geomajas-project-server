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

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Default implementation of {@link GenericFileService}.
 * 
 * @author Jan Venstermans
 */
@Component
public class GenericFileServiceImpl implements GenericFileService {

	private final Logger log = LoggerFactory.getLogger(GenericFileServiceImpl.class);

	private static final long serialVersionUID = 1L;

	private Map<String, String> fileLocations = new HashMap<String, String>();

	@Override
	public String saveFile(MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			try {
				File dirFile = Files.createTempDir();
				String tmpName = dirFile.getPath() + File.separator + file.getOriginalFilename();
				File f = new File(tmpName);
				f.createNewFile();
				file.transferTo(f);
				String uuid = UUID.randomUUID().toString();
				fileLocations.put(uuid, f.getAbsolutePath());
				return uuid;
			} catch (Exception e) {
				log.warn("Exception while processing file", e);
			}
		}
		return null;
	}

	@Override
	public File getFile(String key) {
		if (key != null && fileLocations.containsKey(key)) {
			return new File(fileLocations.get(key));
		}
		return null;
	}

	@Override
	public void deleteFile(String key) {
		File file = getFile(key);
		if (file != null) {
			//Delete file and parent directory
			file.getParentFile().delete();
		}
		fileLocations.remove(key);
	}
}
