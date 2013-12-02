/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.common;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristof Heirwegh
 */
@Component
public class FileServiceImpl implements FileService {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

	private static final long serialVersionUID = 1L;

	@Autowired
	private FileStore store;

	public String persist(InputStream is, String mimeType) throws Exception {
		return store.persist(is, mimeType);
	}

	public FileRef retrieve(String identifier) throws Exception {
		return store.retrieve(identifier);
	}

	public void remove(String identifier) throws Exception {
		store.remove(identifier);
	}
}
