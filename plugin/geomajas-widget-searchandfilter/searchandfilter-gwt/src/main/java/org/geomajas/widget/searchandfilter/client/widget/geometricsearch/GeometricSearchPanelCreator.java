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
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Creator (or factory) pattern needed because GWT cannot instantiate classes starting from name (as String).
 * @author Kristof Heirwegh
 */
public interface GeometricSearchPanelCreator {
	GeometricSearchPanel createInstance(MapWidget mapWidget);
}
