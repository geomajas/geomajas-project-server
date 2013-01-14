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
package org.geomajas.plugin.deskmanager.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Entity
@Table(name = "files")
public class FileRef implements org.geomajas.plugin.deskmanager.service.common.FileRef {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@Column(name = "mimetype", nullable = false)
	private String mimeType;

	@Lob
	// == bytea
	@Type(type = "org.hibernate.type.PrimitiveByteArrayBlobType")
	@Column(name = "data", nullable = false)
	private byte[] data;

	public FileRef() {
	}

	public FileRef(String id) {
		this.id = id;
	}

	// ----------------------------------------------------------

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data.clone();
	}

	public InputStream getDataAsStream() {
		return new ByteArrayInputStream(getData());
	}

}
