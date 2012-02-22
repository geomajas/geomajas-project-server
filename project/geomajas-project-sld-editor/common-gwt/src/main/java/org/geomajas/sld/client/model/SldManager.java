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
package org.geomajas.sld.client.model;

import java.util.List;

import org.geomajas.sld.client.model.event.SldAddedEvent.HasSldAddedHandlers;
import org.geomajas.sld.client.model.event.SldLoadedEvent.HasSldLoadedHandlers;
import org.geomajas.sld.client.model.event.SldSelectedEvent.HasSldSelectedHandlers;
import org.geomajas.sld.editor.client.GeometryType;

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
	 * @return the new SLD instance
	 */
	SldModel create(GeometryType type);

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
	void add(SldModel sld);

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
	 * 
	 * @param name name of the SLD
	 */
	void select(String name);

	void deselectAll();

	void saveAndDeselectAll();
}