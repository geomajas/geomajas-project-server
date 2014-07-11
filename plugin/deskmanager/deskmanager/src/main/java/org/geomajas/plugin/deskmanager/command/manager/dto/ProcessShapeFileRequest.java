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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandRequest;

/**
 * Request object for ProcessShapeFileCommand.
 *
 * @author Oliver May
 */
public class ProcessShapeFileRequest implements CommandRequest {
	public static final String COMMAND = "command.deskmanager.manager.ProcessShapeFile";

	private String fileId;

	private String layerName;

	private String srid;

	/**
	 * Get the id of the file to process.
	 * @return the file id
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * Set the id of the file to process.
	 *
	 * @param fileId
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * Get the name of the (existing) layer to upload to. Null if it's a new layer.
	 *
	 * @return the layer name
	 */
	public String getLayerName() {
		return layerName;
	}

	/**
	 * Set the name of the (existing) layer to upload to. Leave empty if the upload is for a
	 * new layer.
	 * @param layerName the layer name.
	 */
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

}
