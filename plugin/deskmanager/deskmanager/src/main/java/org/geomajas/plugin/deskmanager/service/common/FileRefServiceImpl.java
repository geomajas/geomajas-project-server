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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.geomajas.plugin.deskmanager.domain.FileRef;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Repository
@Transactional
public class FileRefServiceImpl implements FileRefService, FileStore {

	private final Logger log = LoggerFactory.getLogger(FileRefServiceImpl.class);

	@Autowired
	private SessionFactory factory;

	// ------------------------------------------------------------------

	public FileRef getFileRefById(String id) throws HibernateException {
		return (FileRef) factory.getCurrentSession().get(FileRef.class, id);
	}

	public String saveOrUpdateFileRef(FileRef fileRef) throws HibernateException {
		factory.getCurrentSession().saveOrUpdate(fileRef);
		return fileRef.getId();
	}

	public void delete(String id) throws HibernateException {
		FileRef ref = getFileRefById(id);
		if (ref != null) {
			factory.getCurrentSession().delete(ref);
		}
	}

	// ----------------------------------------------------------
	// -- FileStore
	// ----------------------------------------------------------

	public String persist(InputStream is, String mimeType) throws Exception {
		FileRef bin = new FileRef();
		bin.setMimeType(mimeType);
		bin.setData(readData(is));
		return saveOrUpdateFileRef(bin);
	}

	public FileRef retrieve(String identifier) throws Exception {
		return getFileRefById(identifier);
	}

	public void remove(String identifier) throws Exception {
		delete(identifier);
	}

	// ----------------------------------------------------------

	private byte[] readData(InputStream is) throws Exception {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;

			while ((len = is.read(buffer)) >= 0) {
				baos.write(buffer, 0, len);
			}

			return baos.toByteArray();
		} catch (Exception e) {
			log.warn("Error reading from stream: " + e.getMessage());
			throw new Exception("Fout bij lezen data.");
		}
	}
}
