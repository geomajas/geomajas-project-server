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
package org.geomajas.widget.searchandfilter.search.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.geomajas.annotation.Api;

/**
 * Contains all necessary data to execute a search for features.
 *
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public interface Criterion extends Serializable {

	/**
	 * A limited validation just to make sure all necessary properties are
	 * filled in (not that for instance the layer exists and is readable).
	 *
	 * @return true when valid
	 */
	boolean isValid();

	/**
	 * Add all serverlayerIds this criterion operates on to the given Set.
	 *
	 * @param layerIds set of layer ids to add to
	 */
	void serverLayerIdVisitor(Set<String> layerIds);
	
	/**
	 * Retrieve all the criteria within this criterion (not recursive).
	 * <p>This will return an empty list for everything except AndCriterion and OrCriterion.
	 * <p>This is a convenience method so you do not need to check/cast every criterion (see Composite Pattern)
	 *  
	 * @return list of criteria 
	 */
	List<Criterion> getCriteria();

}
