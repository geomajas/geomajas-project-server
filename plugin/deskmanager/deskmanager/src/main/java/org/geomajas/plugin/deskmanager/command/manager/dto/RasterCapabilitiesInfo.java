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

import org.geomajas.geometry.Bbox;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Jan De Moerloose
 * 
 */
public class RasterCapabilitiesInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String crs;

	private String description;

	private String previewUrl;

	private String baseUrl;

	private Bbox extent;

	private List<String> getFeatureInfoFormats;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Bbox getExtent() {
		return extent;
	}

	public void setExtent(Bbox extent) {
		this.extent = extent;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Get a list of feature info formats supported by the server.
	 *
	 * @return the list of feature info formats.
	 */
	public List<String> getGetFeatureInfoFormats() {
		return getFeatureInfoFormats;
	}

	/**
	 * Set a list of feature info formats.
	 *
	 * @param getFeatureInfoFormats the supported formats.
	 */
	public void setGetFeatureInfoFormats(List<String> getFeatureInfoFormats) {
		this.getFeatureInfoFormats = getFeatureInfoFormats;
	}
}
