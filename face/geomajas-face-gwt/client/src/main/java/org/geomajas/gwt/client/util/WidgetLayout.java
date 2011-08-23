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

package org.geomajas.gwt.client.util;

import com.smartgwt.client.widgets.Window;
import org.geomajas.annotation.Api;

/**
 * Class which helps to provide consistent sizes and names for layout purposes.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
public final class WidgetLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Small margin width. */
	public static int marginSmall = 5;
	/** Large margin width. */
	public static int marginLarge = 10;

	/** Default offset for displaying pop-up windows. Also used (doubled) to limit maximum size of pop-ups. */
	public static int windowOffset = 20;

	/** Edit icon. */
	public static String iconEdit = "[ISOMORPHIC]/geomajas/osgeo/edit.png";
	/** Save icon. */
	public static String iconSave = "[ISOMORPHIC]/geomajas/osgeo/save1.png";
	/** Undo icon. */
	public static String iconUndo = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	/** Quit icon. */
	public static String iconQuit = "[ISOMORPHIC]/geomajas/osgeo/quit.png";
	/** Zoom to selection icon. */
	public static String iconZoomSelect = "[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";

	/** Background colour for the legend widget. */
	public static String legendBackgroundColor = "#FFFFFF";
	/** Row height for raster legend info. */
	public static int legendRasterRowHeight = 20;
	/** Row height for vector legend info. */
	public static int legendVectorRowHeight = 21;
	/** Icon to use for raster legend image, relative to isomorphic dir. */
	public static String legendRasterIcon = "geomajas/osgeo/layer-raster.png";
	/** Icon width for raster legend image. */
	public static int legendRasterIconWidth = 16;
	/** Icon height for raster legend image. */
	public static int legendRasterIconHeight = 16;
	/** Indent for legend labels (relative to the margin used). */
	public static int legendLabelIndent = 20;

	/** Fixed width for {@link org.geomajas.gwt.client.widget.FeatureAttributeWindow} or 0 for auto. */
	public static int featureAttributeWindowWidth;
	/** Fixed height for {@link org.geomajas.gwt.client.widget.FeatureAttributeWindow} or 0 for auto. */
	public static int featureAttributeWindowHeight;
	/** Width for the layout in the feature attribute window. */
	public static int featureAttributeWindowLayoutWidth = 450;
	/** Should feature attribute window try to stay within the browser window? */
	public static boolean featureAttributeWindowKeepInScreen = true;

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private WidgetLayout() {
		// do not allow instantiation.
	}

	/**
	 * Try to force a window to stay within the screen bounds.
	 *
	 * @param window window to affect
	 */
	public static void keepWindowInScreen(Window window) {
		window.setKeepInParentRect(true);
		int screenHeight = com.google.gwt.user.client.Window.getClientHeight();
		int windowHeight = window.getHeight();
		if (windowHeight + window.getAbsoluteTop() > screenHeight) {
			int top = screenHeight - windowHeight;
			if (top >= 0) {
				window.setPageTop(top);
			} else {
				window.setHeight(screenHeight - WidgetLayout.windowOffset);
				window.setPageTop(WidgetLayout.windowOffset);
			}
		}
	}

}
