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
package org.geomajas.example.gwt.client.samples.i18n;

/**
 * Interface to represent the constants contained in resource bundle:
 * 'src/main/resources/org/geomajas/example/gwt/client/samples/i18n/Samples.properties'
 * .
 * 
 * @author Jan De Moerloose
 */
public interface Samples extends com.google.gwt.i18n.client.ConstantsWithLookup {

	/**
	 * Translated "The type of road. Will be used as label.".
	 * 
	 * @return translated "The type of road. Will be used as label."
	 */
	@DefaultStringValue("The type of road. Will be used as label.")
	@Key("road.label.Tooltip")
	String roadLabelTooltip();
}
