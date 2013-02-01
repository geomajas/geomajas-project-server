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
	/** Icon for the favourites action. */
	public static String iconSearchFavourites = "[ISOMORPHIC]/geomajas/searchIcons/favourite.png";
	/** Icon for the spatial search action. */
	public static String iconSpatialSearch = "[ISOMORPHIC]/geomajas/searchIcons/geographic.png";
	/** Icon for the free search action. */
	public static String iconSearchFree = "[ISOMORPHIC]/geomajas/searchIcons/free.png";
	/** Icon for the combined search action. */
	public static String iconSearchCombined = "[ISOMORPHIC]/geomajas/searchIcons/combined.png";
	/** Average width in pixels text characters, used to calculate button size. 
	 * (buttonOffset + buttonFontWidth * title.length()) */
	public static int buttonFontWidth = 7;
	/** Offset size of a button without the text, used to calculate button size. */
	public static int buttonOffset = 28;
	
	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private GsfLayout() {
		// do not allow instantiation.
	}
	
}
