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

package org.geomajas.smartgwt.client.util;

import org.geomajas.annotation.Api;

import com.smartgwt.client.widgets.Window;

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

	/** Small spacer size. */
	public static int spacerSmall = 10;
	/** Large spacer size. */
	public static int spacerLarge = 20;

	/** Default offset for displaying pop-up windows. Also used (doubled) to limit maximum size of pop-ups. */
	public static int windowOffset = 20;

	/** Opacity for modal mask. */
	public static int modalMaskOpacity = 50;

	/** Add icon. */
	public static String iconAdd = "[ISOMORPHIC]/geomajas/silk/add.png";
	/** Add image icon. */
	public static String iconAddImage = "[ISOMORPHIC]/geomajas/silk/image-add.png";
	/** AJAX loading icon. */
	public static String iconAjaxLoading = "[ISOMORPHIC]/geomajas/ajax-loader.gif";
	/** Display attributes icon. */
	public static String iconAttributesDisplay = "[ISOMORPHIC]/geomajas/osgeo/attributes-display.png";
	/** Show attributes icon. */
	public static String iconAttributesShow = "[ISOMORPHIC]/geomajas/osgeo/attributes-show.png";
	/** Bookmark icon. */
	public static String iconBookmark = "[ISOMORPHIC]/geomajas/osgeo/bookmark_new.png";
	/** Cancel icon. */
	public static String iconCancel = "[ISOMORPHIC]/geomajas/silk/cancel.png";
	/** Copy icon. */
	public static String iconCopy = "[ISOMORPHIC]/geomajas/osgeo/edit-copy.png";
	/** Create icon. */
	public static String iconCreate = "[ISOMORPHIC]/geomajas/osgeo/create.png";
	/** Cut icon. */
	public static String iconCut = "[ISOMORPHIC]/geomajas/osgeo/edit-cut.png";
	/** Edit icon. */
	public static String iconEdit = "[ISOMORPHIC]/geomajas/osgeo/edit.png";
	/** Error icon. */
	public static String iconError = "[ISOMORPHIC]/geomajas/widget/error.png";
	/** Export image icon. */
	public static String iconExportImage = "[ISOMORPHIC]/geomajas/osgeo/image-export.png";
	/** Export layer icon. */
	public static String iconExportLayer = "[ISOMORPHIC]/geomajas/osgeo/layer-export.png";
	/** Export map icon. */
	public static String iconExportMap = "[ISOMORPHIC]/geomajas/osgeo/layer-map.png";
	/** Export PDF icon. */
	public static String iconExportPdf = "[ISOMORPHIC]/geomajas/osgeo/pdf-export.png";
	/** Export SVG icon. */
	public static String iconExportSvg = "[ISOMORPHIC]/geomajas/osgeo/svg-export.png";
	/** Find icon. */
	public static String iconFind = "[ISOMORPHIC]/geomajas/silk/find.png";
	/** Font icon. */
	public static String iconFont = "[ISOMORPHIC]/geomajas/silk/font.png";
	/** Geomajas icon. */
	public static String iconGeomajas = "[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png";
	/** Help contents icon. */
	public static String iconHelpContents = "[ISOMORPHIC]/geomajas/osgeo/help-contents.png";
	/** Hide icon. */
	public static String iconHide = "[ISOMORPHIC]/geomajas/osgeo/hide.png";
	/** Info icon. */
	public static String iconInfo = "[ISOMORPHIC]/geomajas/osgeo/info.png";
	/** Disabled labels icon. */
	public static String iconLabelsDisabled = "[ISOMORPHIC]/geomajas/widget/layertree/labels-disabled.png";
	/** Hide labels icon. */
	public static String iconLabelsHide = "[ISOMORPHIC]/geomajas/widget/layertree/labels-hide.png";
	/** Show labels icon. */
	public static String iconLabelsShow = "[ISOMORPHIC]/geomajas/widget/layertree/labels-show.png";
	/** Disabled layer icon. */
	public static String iconLayerDisabled = "[ISOMORPHIC]/geomajas/widget/layertree/layer-disabled.png";
	/** Hide layer icon. */
	public static String iconLayerHide = "[ISOMORPHIC]/geomajas/widget/layertree/layer-hide.png";
	/** Layer invisible icon. */
	public static String iconLayerInvisible = "[ISOMORPHIC]/geomajas/widget/layertree/layer-invisible.png";
	/** Show layer icon. */
	public static String iconLayerShow = "[ISOMORPHIC]/geomajas/widget/layertree/layer-show.png";
	/** Show layer labelled icon. */
	public static String iconLayerShowLabeled = "[ISOMORPHIC]/geomajas/widget/layertree/layer-show-labeled.png";
	/** Line icon. */
	public static String iconLine = "[ISOMORPHIC]/geomajas/osgeo/line.png";
	/** Create line icon. */
	public static String iconLineCreate = "[ISOMORPHIC]/geomajas/osgeo/line-create.png";
	/** Delete line icon. */
	public static String iconLineDelete = "[ISOMORPHIC]/geomajas/osgeo/line-delete.png";
	/** Edit line icon. */
	public static String iconLineEdit = "[ISOMORPHIC]/geomajas/osgeo/line-edit.png";
	/** Move line icon. */
	public static String iconLineMove = "[ISOMORPHIC]/geomajas/osgeo/line-move.png";
	/** Line polygon edit icon. */
	public static String iconLinePolygonEdit = "[ISOMORPHIC]/geomajas/osgeo/line-polygon-edit.png";
	/** Split line icon. */
	public static String iconLineSplit = "[ISOMORPHIC]/geomajas/osgeo/line-split.png";
	/** Measure area icon. */
	public static String iconMeasureArea = "[ISOMORPHIC]/geomajas/osgeo/area-measure.png";
	/** Measure length icon. */
	public static String iconMeasureLength = "[ISOMORPHIC]/geomajas/osgeo/length-measure.png";
	/** Mouse info icon. */
	public static String iconMouseInfo = "[ISOMORPHIC]/geomajas/osgeo/mouse_info_tool.png";
	/** Mouse tooltip icon. */
	public static String iconMouseTooltip = "[ISOMORPHIC]/geomajas/osgeo/mouse-tooltip.png";
	/** Open icon (folder). */
	public static String iconOpen = "[ISOMORPHIC]/geomajas/osgeo/open1.png";
	/** Alternate open icon. */
	public static String iconOpenAlt = "[ISOMORPHIC]/geomajas/osgeo/open.png";
	/** Pan icon. */
	public static String iconPan = "[ISOMORPHIC]/geomajas/osgeo/pan.png";
	/** Pan info icon. */
	public static String iconPanInfo = "[ISOMORPHIC]/geomajas/osgeo/pan-info.png";
	/** Pan to selection icon. */
	public static String iconPanToSelection = "[ISOMORPHIC]/geomajas/osgeo/pan_to_selection.png";
	/** Clear picker icon. */
	public static String iconPickerClear = "[SKIN]/pickers/clear_picker.png";
	/** Search picker icon. */
	public static String iconPickerSearch = "[SKIN]/pickers/search_picker.png";
	/** Point icon. */
	public static String iconPoint = "[ISOMORPHIC]/geomajas/osgeo/point.png";
	/** Create point icon. */
	public static String iconPointCreate = "[ISOMORPHIC]/geomajas/osgeo/point-create.png";
	/** Pointer icon. */
	public static String iconPointer = "[ISOMORPHIC]/geomajas/osgeo/pointer.png";
	/** Plygon icon. */
	public static String iconPolygon = "[ISOMORPHIC]/geomajas/osgeo/polygon.png";
	/** Create polygon icon. */
	public static String iconPolygonCreate = "[ISOMORPHIC]/geomajas/osgeo/polygon-create.png";
	/** Print icon. */
	public static String iconPrint = "[ISOMORPHIC]/geomajas/osgeo/print.png";
	/** Quit icon. */
	public static String iconQuit = "[ISOMORPHIC]/geomajas/osgeo/quit.png";
	/** Redo icon. */
	public static String iconRedo = "[ISOMORPHIC]/geomajas/osgeo/redo.png";
	/** Hide layer icon. */
	public static String iconRefresh = "[ISOMORPHIC]/geomajas/widget/layertree/refresh.png";
	/** Disabled layer icon. */
	public static String iconRefreshDisabled = "[ISOMORPHIC]/geomajas/widget/layertree/refresh-disabled.png";
	/** Rasterize icon. */
	public static String iconRasterize = "[ISOMORPHIC]/geomajas/osgeo/rasterize.png";
	/** Redraw icon. */
	public static String iconRedraw = "[ISOMORPHIC]/geomajas/osgeo/redraw.png";
	/** Region icon. */
	public static String iconRegion = "[ISOMORPHIC]/geomajas/osgeo/region.png";
	/** Reload icon. */
	public static String iconReload = "[ISOMORPHIC]/geomajas/osgeo/reload.png";
	/** Remove icon. */
	public static String iconRemove = "[ISOMORPHIC]/geomajas/silk/cancel.png";
	/** Reset icon. */
	public static String iconReset = "[ISOMORPHIC]/geomajas/silk/arrow_refresh.png";
	/** Ring icon. */
	public static String iconRing = "[ISOMORPHIC]/geomajas/osgeo/ring.png";
	/** Add ring icon. */
	public static String iconRingAdd = "[ISOMORPHIC]/geomajas/osgeo/ring-add.png";
	/** Delete ring icon. */
	public static String iconRingDelete = "[ISOMORPHIC]/geomajas/osgeo/ring-delete.png";
	/** Save icon (disk). */
	public static String iconSave = "[ISOMORPHIC]/geomajas/osgeo/save1.png";
	/** Alternate save icon. */
	public static String iconSaveAlt = "[ISOMORPHIC]/geomajas/osgeo/save.png";
	/** Save as icon (disk). */
	public static String iconSaveAs = "[ISOMORPHIC]/geomajas/osgeo/save-as1.png";
	/** Alternate save as icon. */
	public static String iconSaveAsAlt = "[ISOMORPHIC]/geomajas/osgeo/save-as.png";
	/** Selected icon. */
	public static String iconSelect = "[ISOMORPHIC]/geomajas/osgeo/select.png";
	/** Add selected icon. */
	public static String iconSelectedAdd = "[ISOMORPHIC]/geomajas/osgeo/selected-add.png";
	/** Delete selected icon. */
	public static String iconSelectedDelete = "[ISOMORPHIC]/geomajas/osgeo/selected-delete.png";
	/** Show icon. */
	public static String iconShow = "[ISOMORPHIC]/geomajas/osgeo/show.png";
	/** Table icon. */
	public static String iconTable = "[ISOMORPHIC]/geomajas/osgeo/table.png";
	/** Tips icon. */
	public static String iconTips = "[ISOMORPHIC]/geomajas/osgeo/tips.png";
	/** Tools icon. */
	public static String iconTools = "[ISOMORPHIC]/geomajas/osgeo/tools.png";
	/** Undo icon. */
	public static String iconUndo = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	/** Edit vector icon. */
	public static String iconVectorEdit = "[ISOMORPHIC]/geomajas/osgeo/vector-edit.png";
	/** Create vector icon. */
	public static String iconVectorCreate = "[ISOMORPHIC]/geomajas/osgeo/vector-create.png";
	/** Remove vector icon. */
	public static String iconVectorRemove = "[ISOMORPHIC]/geomajas/osgeo/vector-remove.png";
	/** Create vertex icon. */
	public static String iconVertexCreate = "[ISOMORPHIC]/geomajas/osgeo/vertex-create.png";
	/** Delete vertex icon. */
	public static String iconVertexDelete = "[ISOMORPHIC]/geomajas/osgeo/vertex-delete.png";
	/** Zoom to extent icon. */
	public static String iconZoomExtent = "[ISOMORPHIC]/geomajas/osgeo/zoom-extent.png";
	/** Zoom in icon. */
	public static String iconZoomIn = "[ISOMORPHIC]/geomajas/osgeo/zoom-in.png";
	/** Zoom to last icon. */
	public static String iconZoomLast = "[ISOMORPHIC]/geomajas/osgeo/zoom-last.png";
	/** Zoom to layer icon. */
	public static String iconZoomLayer = "[ISOMORPHIC]/geomajas/osgeo/zoom-layer.png";
	/** Zoom more icon. */
	public static String iconZoomMore = "[ISOMORPHIC]/geomajas/osgeo/zoom-more.png";
	/** Zoom to next icon. */
	public static String iconZoomNext = "[ISOMORPHIC]/geomajas/osgeo/zoom-next.png";
	/** Zoom ou icon. */
	public static String iconZoomOut = "[ISOMORPHIC]/geomajas/osgeo/zoom-out.png";
	/** Zoom refresh icon. */
	public static String iconZoomRefresh = "[ISOMORPHIC]/geomajas/osgeo/zoom-refresh.png";
	/** Zoom to selection icon. */
	public static String iconZoomSelection = "[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";

	/** Loading screen logo. */
	public static String loadingScreenLogo = "[ISOMORPHIC]/geomajas/geomajas_logo.png";
	/** Loading screen logo width. */
	public static String loadingScreenLogoWidth = "300";
	/** Loading screen logo height. */
	public static String loadingScreenLogoHeight = "80";
	/** Loading screen inner width. */
	public static String loadingScreenWidth = "500";
	/** Loading screen inner height. */
	public static String loadingScreenHeight = "300";
	/** Loading screen shadow depth. */
	public static int loadingScreenShadowDepth = 10;
	/** Loading screen background image. */
	public static String loadingScreenBackgroundImage = "[ISOMORPHIC]/geomajas/widget/background.png";
	/** Loading screen background colour. */
	public static String loadingScreenBackgroundColor = "#FFFFFF";
	/** Loading screen top spacer height. */
	public static String loadingScreenTopSpacerHeight = "40";
	/** Loading screen title height. */
	public static String loadingScreenTitleHeight = "24";
	/** Loading screen edge opacity. */
	public static int loadingScreenEdgeOpacity = 70;
	/** Loading screen progress height. */
	public static String loadingScreenProgressHeight = "80";
	/** Loading screen progress label height. */
	public static String loadingScreenProgressLabelHeight = "15";
	/** Loading screen progress bar height. */
	public static String loadingScreenProgressBarHeight = "30";
	/** Loading screen progress opacity. */
	public static int loadingScreenProgressOpacity = 30;
	/** Loading screen progress padding. */
	public static int loadingScreenProgressPadding = 15;
	/** Loading screen progress background colour. */
	public static String loadingScreenProgressBackgroundColor = "#000000";

	/** Geomajas logo. */
	public static String aboutGeomajasLogo = "[ISOMORPHIC]/geomajas/geomajas_logo.png";
	/** Geomajas logo width. */
	public static String aboutGeomajasWidth = "400";
	/** Geomajas logo height. */
	public static String aboutGeomajasHeight = "300";

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

	/** Fixed width for {@link org.geomajas.smartgwt.client.widget.FeatureAttributeWindow} or null for auto. */
	public static String featureAttributeWindowWidth;
	/** Fixed height for {@link org.geomajas.smartgwt.client.widget.FeatureAttributeWindow} or null for auto. */
	public static String featureAttributeWindowHeight;
	/** Width for the layout in the feature attribute window. */
	public static String featureAttributeWindowLayoutWidth = "450";
	/** Should feature attribute window try to stay within the browser window? */
	public static boolean featureAttributeWindowKeepInScreen = true;

	/** Width for the exception window. */
	public static String exceptionWindowWidth = "450";
	/** Height for the exception window in normal mode. */
	public static String exceptionWindowHeightNormal = "132";
	/** Height for the exception window when showing details. */
	public static String exceptionWindowHeightDetails = "350";
	/** View/hide details button width. */
	public static String exceptionWindowButtonWidth = "100";
	/** Error icon size. */
	public static int exceptionWindowIconSize = 64;
	/** Style for main message in exception window. */
	public static String exceptionWindowMessageStyle = "font-size:12px; font-weight:bold;";
	/** Style for detail header in exception window. */
	public static String exceptionWindowDetailHeaderStyle = "font-size:12px; font-weight:bold;";
	/** Style for normal detail stack trace line in exception window. */
	public static String exceptionWindowDetailTraceNormalStyle = "font-size:12px; padding-left:10px;";
	/** Style for likely less important (framework) detail stack trace line in exception window. */
	public static String exceptionWindowDetailTraceLessStyle = "font-size:9px; padding-left:10px;";
	/** Border style for details in exception window. */
	public static String exceptionWindowDetailBorderStyle = "1px solid #A0A0A0;";
	/** Should the window be confined to stay within the parent rectangle? */
	public static boolean exceptionWindowKeepInScreen = true;

	/** Padding for the buttons in the toolbar. */
	public static int toolbarPadding = 2;
	/** Button size for the small buttons in the toolbar. */
	public static int toolbarSmallButtonSize = 24;
	/** Button size for the large buttons in the toolbar. */
	public static int toolbarLargeButtonSize = 32;
	/** Height of the strip which is part of the toolbar. */
	public static int toolbarStripHeight = 8;

	/** Padding for the buttons in the layer tree. */
	public static int layerTreePadding = 2;
	/** Button size for the small buttons in the layer tree. */
	public static int layerTreeButtonSize = 24;
	/** Height of the strip which is part of the layer tree. */
	public static int layerTreeStripHeight = 8;
	/** Background in the  the layer tree. */
	public static String layerTreeBackground = "#cccccc";

	/** Position of the left of the croc eye. 
	 * @since 1.12.0
	 */
	public static int crocEyePositionLeft = 75;

	/** Position of the top of the croc eye.
	 * @since 1.12.0
	 */
	public static int crocEyePositionTop = 110;

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
		if (null != window.getHeightAsString()) {
			int screenHeight = com.google.gwt.user.client.Window.getClientHeight();
			int windowHeight = window.getViewportHeight();
			window.setMaxHeight(screenHeight - WidgetLayout.windowOffset * 2);
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
		if (null != window.getWidthAsString()) {
			int screenWidth = com.google.gwt.user.client.Window.getClientWidth();
			int windowWidth = window.getViewportWidth();
			window.setMaxWidth(screenWidth - WidgetLayout.windowOffset * 2);
			if (windowWidth + window.getAbsoluteLeft() > screenWidth) {
				int left = screenWidth - windowWidth;
				if (left >= 0) {
					window.setPageLeft(left);
				} else {
					window.setWidth(screenWidth - WidgetLayout.windowOffset);
					window.setPageLeft(WidgetLayout.windowOffset);
				}
			}
		}
	}
}
