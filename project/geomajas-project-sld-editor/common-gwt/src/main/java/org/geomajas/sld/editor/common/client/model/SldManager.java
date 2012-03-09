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
package org.geomajas.sld.editor.common.client.model;

import java.util.List;

import org.geomajas.sld.editor.common.client.GeometryType;
import org.geomajas.sld.editor.common.client.model.event.SldAddedEvent.HasSldAddedHandlers;
import org.geomajas.sld.editor.common.client.model.event.SldLoadedEvent.HasSldLoadedHandlers;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent.HasSldSelectedHandlers;

/**
 * The main model class of the SLD editor.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface SldManager extends HasSldLoadedHandlers, HasSldAddedHandlers, HasSldSelectedHandlers {

	/**
	 * Fetch all the SLD's from the server (asynchronously). Instead of passing a callback here, clients should use the
	 * model events to get registered of changes to the current list of SLD's.
	 */
	void fetchAll();

	/**
	 * Returns the current list of names of all SLD's.
	 * 
	 * @return the list of names
	 */
	List<String> getCurrentNames();

	/**
	 * Creates an instance of the specified type.
	 * 
	 * @param type the geometry type
	 * @param string  name of the SLD
	 * @return the new SLD instance
	 */
	SldModel create(GeometryType type, String string);

	/**
	 * Get the currently selected SLD.
	 * 
	 * @return the new SLD instance
	 */
	SldModel getCurrentSld();

	/**
	 * Adds a new SLD to the server list.
	 * 
	 * @param sld the sld to add
	 */
	void add(SldModel sld,  BasicErrorHandler errorHandler);

	/**
	 * Remove the currently selected SLD from the server list.
	 * 
	 */
	void removeCurrent();

	/**
	 * Save the currently selected SLD.
	 * 
	 */
	void saveCurrent();

	/**
	 * Save the currently selected SLD.
	 * 
	 */
	void refreshCurrent();

	/**
	 * Select an SLD from the list.
	 * Note: Caller is responsible of state control. E.g. do not call this function when the current 
	 * 
	 * @param name name of the SLD
	 * @return true if the SLD is contained in the SLD list on the client, else false 
	 */
	boolean select(String name, final BasicErrorHandler errorHandler);
	boolean select(String name);
	
	void deselectAll();

	void saveAndDeselectAll();

	void saveAndSelect(String name);
}