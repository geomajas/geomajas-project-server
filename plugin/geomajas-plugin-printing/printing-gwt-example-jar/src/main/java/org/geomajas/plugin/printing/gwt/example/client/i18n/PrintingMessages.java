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

package org.geomajas.plugin.printing.gwt.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Messages for the printing example panels.
 *
 * @author Jan De Moerloose
 */
public interface PrintingMessages extends Messages {

	/** @return title for the plugins tree node. */
	String treeGroupPlugins();

	/** @return title for the print panel. */
	String printTitle();

	/** @return description for the print panel. */
	String printDescription();

}
