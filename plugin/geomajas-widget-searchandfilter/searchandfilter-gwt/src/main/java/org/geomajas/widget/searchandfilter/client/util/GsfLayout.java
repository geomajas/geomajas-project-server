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

package org.geomajas.widget.searchandfilter.client.util;

import org.geomajas.annotation.Api;
import org.geomajas.widget.searchandfilter.client.widget.search.DockableWindowSearchWidget.SearchWindowPositionType;

import com.smartgwt.client.widgets.Canvas;

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
	/** The width of the combined search panel. */
	public static String combinedSearchPanelWidth = "400";

	/** SearchWindow Layout options. Can also be null, which means no positioning. */
	public static SearchWindowPositionType searchWindowPositionType = SearchWindowPositionType.CENTERED;
	/** SearchWindow Layout options - depends on type. */
	public static int searchWindowPosTop;
	/** SearchWindow Layout options - depends on type. */
	public static int searchWindowPosLeft;
	/** SearchWindow Layout options - depends on type.
	 * <p>Possible values: BR, BL, TR, TL, R, L, B, T, C where B=Bottom, T=Top, L=Left, R=right and C=center. */
	public static String searchWindowPosSnapTo;
	/** Only needed for SNAPTO. */
	public static Canvas searchWindowParentElement;
	
	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private GsfLayout() {
		// do not allow instantiation.
	}
	
}
