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
package org.geomajas.plugin.deskmanager.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.geomajas.annotation.Api;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * Reference of a file saved in the database.
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api(allMethods = true)
@Entity
@Table(name = "gdm_file")
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

	/**
	 * Get the id of this file ref.
	 * 
	 * @return the the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id of this file ref.
	 * 
	 * @param id the id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the mime type.
	 * 
	 * @return the mime type.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Set the mime type.
	 * 
	 * @param mimeType the mime type to set.
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Get the data.
	 * 
	 * @return the data.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Set the data.
	 * 
	 * @param data the data to set.
	 */
	public void setData(byte[] data) {
		this.data = data.clone();
	}

	/**
	 * Get the data as input stream.
	 * 
	 * @return the input stream.
	 */
	public InputStream getDataAsStream() {
		return new ByteArrayInputStream(getData());
	}
}
