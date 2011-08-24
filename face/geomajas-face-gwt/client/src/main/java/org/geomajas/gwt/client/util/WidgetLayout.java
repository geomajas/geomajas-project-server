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
	/** Save icon (disk). */
	public static String iconSave = "[ISOMORPHIC]/geomajas/osgeo/save1.png";
	/** Alternate save icon. */
	public static String iconSaveAlt = "[ISOMORPHIC]/geomajas/osgeo/save.png";
	/** Open icon (folder). */
	public static String iconOpen = "[ISOMORPHIC]/geomajas/osgeo/open1.png";
	/** Alternate open icon. */
	public static String iconOpenAlt = "[ISOMORPHIC]/geomajas/osgeo/open.png";
	/** Quit icon. */
	public static String iconQuit = "[ISOMORPHIC]/geomajas/osgeo/quit.png";
	/** Info icon. */
	public static String iconInfo = "[ISOMORPHIC]/geomajas/osgeo/info.png";
	/** Undo icon. */
	public static String iconUndo = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	/** Table icon. */
	public static String iconTable = "[ISOMORPHIC]/geomajas/osgeo/table.png";
	/** Pan icon. */
	public static String iconPan = "[ISOMORPHIC]/geomajas/osgeo/pan.png";
	/** Pan to selection icon. */
	public static String iconPanToSelection = "[ISOMORPHIC]/geomajas/osgeo/pan_to_selection.png";
	/** Measure length icon. */
	public static String iconMeasureLength = "[ISOMORPHIC]/geomajas/osgeo/length-measure.png";
	/** Zoom to selection icon. */
	public static String iconZoomSelect = "[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";
	/** Create vector icon. */
	public static String iconVectorCreate = "[ISOMORPHIC]/geomajas/osgeo/vector-create.png";
	/** Remove vector icon. */
	public static String iconVectorRemove = "[ISOMORPHIC]/geomajas/osgeo/vector-remove.png";
	/** Edit vector icon. */
	public static String iconVectorEdit = "[ISOMORPHIC]/geomajas/osgeo/vector-edit.png";
	/** Selected delete icon. */
	public static String iconSelectedDelete = "[ISOMORPHIC]/geomajas/osgeo/selected-delete.png";
	/** Create vertex icon. */
	public static String iconVertexCreate = "[ISOMORPHIC]/geomajas/osgeo/vertex-create.png";
	/** Delete vertex icon. */
	public static String iconVertexDelete = "[ISOMORPHIC]/geomajas/osgeo/vertex-delete.png";
	/** Add ring icon. */
	public static String iconRingAdd = "[ISOMORPHIC]/geomajas/osgeo/ring-add.png";
	/** Delete ring icon. */
	public static String iconRingDelete = "[ISOMORPHIC]/geomajas/osgeo/ring-delete.png";
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
	/** Layer invisible icon. */
	public static String iconLayerInvisible = "[ISOMORPHIC]/geomajas/widget/layertree/layer-invisible.png";
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
	/** Geomajas icon. */
	public static String iconGeomajas = "[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png";
	/** Remove icon. */
	public static String iconRemove = "[ISOMORPHIC]/geomajas/silk/cancel.png";
	/** Add icon. */
	public static String iconAdd = "[ISOMORPHIC]/geomajas/silk/add.png";
	/** Find icon. */
	public static String iconFind = "[ISOMORPHIC]/geomajas/silk/find.png";


	/** Loading screen logo. */
	public static String loadingScreenLogo = "[ISOMORPHIC]/geomajas/geomajas_logo.png";
	/** Loading screen logo width. */
	public static int loadingScreenLogoWidth = 300;
	/** Loading screen inner width. */
	public static int loadingScreenWidth = 500;
	/** Loading screen inner height. */
	public static int loadingScreenHeight = 300;
	/** Loading screen shadow depth. */
	public static int loadingScreenShadowDepth = 10;
	/** Loading screen background image. */
	public static String loadingScreenBackgroundImage = "[ISOMORPHIC]/geomajas/widget/background.png";
	/** Loading screen background colour. */
	public static String loadingScreenBackgroundColor = "#FFFFFF";
	/** Loading screen top spacer height. */
	public static int loadingScreenTopSpacerHeight = 40;
	/** Loading screen title height. */
	public static int loadingScreenTitleHeight = 24;
	/** Loading screen edge opacity. */
	public static int loadingScreenEdgeOpacity = 70;
	/** Loading screen progress height. */
	public static int loadingScreenProgressHeight = 80;
	/** Loading screen progress label height. */
	public static int loadingScreenProgressLabelHeight = 15;
	/** Loading screen progress bar height. */
	public static int loadingScreenProgressBarHeight = 30;
	/** Loading screen progress opacity. */
	public static int loadingScreenProgressOpacity = 30;
	/** Loading screen progress padding. */
	public static int loadingScreenProgressPadding = 15;
	/** Loading screen progress background colour. */
	public static String loadingScreenProgressBackgroundColor = "#000000";


	/** Geomajas logo. */
	public static String aboutGeomajasLogo = "[ISOMORPHIC]/geomajas/geomajas_logo.png";
	/** Geomajas logo width. */
	public static int aboutGeomajasWidth = 400;
	/** Geomajas logo height. */
	public static int aboutGeomajasHeight = 300;

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
