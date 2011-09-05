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

package org.geomajas.widget.searchandfilter.client.util;

import org.geomajas.annotation.Api;

/**
 * Class which helps to provide consistent sizes and names for layout purposes, see
 * {@link org.geomajas.gwt.client.util.WidgetLayout}.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public final class GsfLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Combined search panel width. */
	public static String geometricSearchPanelTabWidth = "400";
	/** Combined search panel height. */
	public static String geometricSearchPanelTabHeight = "250";

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private GsfLayout() {
		// do not allow instantiation.
	}

}
