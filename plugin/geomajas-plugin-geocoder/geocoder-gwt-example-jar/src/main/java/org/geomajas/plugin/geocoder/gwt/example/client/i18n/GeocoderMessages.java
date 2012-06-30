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

package org.geomajas.plugin.geocoder.gwt.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Messages for the geocoder example panels.
 *
 * @author Joachim Van der Auwera
 */
public interface GeocoderMessages extends Messages {

	/** @return title for the plugins tree node. */
	String treeGroupPlugins();

	/** @return title for the geocoder panel. */
	String geocoderTitle();

	/** @return description for the geocoder panel. */
	String geocoderDescription();

	/** @return label for button to hide the title on the geocoder widget. */
	String hideTitle();

	/** @return label for button to show the title on the geocoder widget. */
	String showTitle();

}
