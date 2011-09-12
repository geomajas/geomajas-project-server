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
package org.geomajas.widget.utility.smartgwt.client.util;

import org.geomajas.annotation.Api;


/**
 * Class which helps to provide consistent sizes and names for layout purposes, see
 * {@link org.geomajas.gwt.client.util.WidgetLayout}.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 *
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public class GuwLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Member margin between ribbon groups in the ribbon bar. */
	public static int ribbonBarInternalMargin = 2;
	/** Member margin between components in a ribbon group. */
	public static int ribbonGroupInternalMargin = 10;

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private GuwLayout() {
		// do not allow instantiation.
	}

}
