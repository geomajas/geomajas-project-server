/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component;

import org.geomajas.plugin.printing.component.dto.ViewPortComponentInfo;

/**
 * Component representing a view port (small embedded map).
 * 
 * @author Jan De Moerloose
 *
 */
public interface ViewPortComponent extends MapComponent<ViewPortComponentInfo> {

	float getZoomScale();

	float getUserX();

	float getUserY();

}