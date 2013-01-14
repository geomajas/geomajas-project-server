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

package org.geomajas.widget.featureinfo.client.util;

import org.geomajas.annotation.Api;

/**
 * Class which helps to provide consistent sizes and names for layout purposes.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class FitLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Offset from current pointer position for displaying tooltip. */
	public static int tooltipOffsetX = 12;
	/** Offset from current pointer position for displaying tooltip. */
	public static int tooltipOffsetY = 12;

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private FitLayout() {
		// do not allow instantiation.
	}

}
