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

package org.geomajas.layer.google.gwt.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Messages for the google example panels.
 *
 * @author Jan De Moerloose
 */
public interface GoogleMessages extends Messages {

	/** @return title for the layers tree node. */
	String treeGroupLayers();

	/** @return title for the google panel. */
	String googleTitle();

	/** @return description for the google panel. */
	String googleDescription();

}
