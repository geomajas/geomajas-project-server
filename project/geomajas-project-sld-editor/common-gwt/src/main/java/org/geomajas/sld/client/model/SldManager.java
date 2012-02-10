/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.client.model;

import java.util.List;

import com.google.gwt.event.shared.HasHandlers;

/**
 * The main model class of the SLD editor.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface SldManager extends HasHandlers {

	/**
	 * Fetch all the SLD's from the server (asynchronously). Instead of passing a callback here, clients should use the
	 * model events to get registered of changes to the current list of SLD's.
	 */
	void fetchAll();

	/**
	 * Returns the current list of names of all SLD's.
	 * @return the list of names
	 */
	List<String> getCurrentNames();
}