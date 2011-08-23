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
	/** Download icon. */
	public static String iconDownload = "[ISOMORPHIC]/geomajas/osgeo/save.png";
	/** Undo icon. */
	public static String iconUndo = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	/** Quit icon. */
	public static String iconQuit = "[ISOMORPHIC]/geomajas/osgeo/quit.png";
	/** Info icon. */
	public static String iconInfo = "[ISOMORPHIC]/geomajas/osgeo/info.png";
	/** Undo icon. */
	public static String iconUndo = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	/** Table icon. */
	public static String iconTable = "[ISOMORPHIC]/geomajas/osgeo/table.png";
	/** Zoom to selection icon. */
	public static String iconZoomSelect = "[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";
	/** Show labels icon. */
	public static String iconLabelsShow = "[ISOMORPHIC]/geomajas/widget/layertree/labels-show.png";
	/** Hide labels icon. */
	public static String iconLabelsHide = "[ISOMORPHIC]/geomajas/widget/layertree/labels-hide.png";
	/** Disabled labels icon. */
	public static String iconLabelsDisabled = "[ISOMORPHIC]/geomajas/widget/layertree/labels-disabled.png";
	/** Show layer icon. */
	public static String iconLayerShow = "[ISOMORPHIC]/geomajas/widget/layertree/layer-show.png";
	/** Show layer labelled icon. */
	public static String iconLayerShowLabeled = "[ISOMORPHIC]/geomajas/widget/layertree/layer-show-labeled.png";
	/** Hide layer icon. */
	public static String iconLayerHide = "[ISOMORPHIC]/geomajas/widget/layertree/layer-hide.png";
	/** Disabled layer icon. */
	public static String iconLayerDisabled = "[ISOMORPHIC]/geomajas/widget/layertree/layer-disabled.png";
	/** Hide layer icon. */
	public static String iconRefresh = "[ISOMORPHIC]/geomajas/widget/layertree/refresh-hide.png";
	/** Disabled layer icon. */
	public static String iconRefreshDisabled = "[ISOMORPHIC]/geomajas/widget/layertree/refresh-disabled.png";
	/** Tips icon. */
	public static String iconTips = "[ISOMORPHIC]/geomajas/osgeo/tips.png";
	/** Help contents icon. */
	public static String iconHelpContents = "[ISOMORPHIC]/geomajas/osgeo/help-contents.png";
	/** Error icon. */
	public static String iconError = "[ISOMORPHIC]/geomajas/widget/error.png";
	/** icon. */
	public static String icon = ;

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
