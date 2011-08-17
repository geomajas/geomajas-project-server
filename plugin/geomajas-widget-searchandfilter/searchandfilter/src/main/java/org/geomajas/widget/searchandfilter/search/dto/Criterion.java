/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.search.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * Contains all necessary data to execute a search for features.
 *
 * @author Kristof Heirwegh
 */
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

}
