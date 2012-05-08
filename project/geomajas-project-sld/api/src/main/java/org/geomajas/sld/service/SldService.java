/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.service;

import java.util.List;

import org.geomajas.sld.StyledLayerDescriptorInfo;

/**
 * Service to lookup and persist SLD (Styled Layer Descriptor) styles. Implementations can be backed by e.g. the file
 * system or a database.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface SldService {

	/**
	 * Return all styles.
	 * 
	 * @return list of all styles
	 * @throws SldException oops
	 */
	List<StyledLayerDescriptorInfo> findAll() throws SldException;

	/**
	 * Find a style by name.
	 * 
	 * @param name name of the style
	 * @return the style or null if the style does not exist
	 * @throws SldException oops
	 */
	StyledLayerDescriptorInfo findByName(String name) throws SldException;

	/**
	 * Create or update a style.
	 * 
	 * @param sld the style
	 * @return the style after update
	 * @throws SldException oops
	 */
	StyledLayerDescriptorInfo saveOrUpdate(StyledLayerDescriptorInfo sld) throws SldException;

	/**
	 * Create a style.
	 * 
	 * @param sld the style
	 * @return the new style
	 * @throws SldException oops (e.g. if SLD with the same name already exists)
	 */
	StyledLayerDescriptorInfo create(StyledLayerDescriptorInfo sld) throws SldException;

	/**
	 * Remove a style.
	 * 
	 * @param name the name of the style
	 * @return true if removed, false if there was no such style
	 * @throws SldException oops
	 */
	boolean remove(String name) throws SldException;

	/**
	 * Validates (part of) a style.
	 * 
	 * @param obj any of the Info classes for which can be validated as types
	 * @throws SldException invalid or unrecognized object
	 */
	void validate(Object obj) throws SldException;

}
