/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.action.toolbar;

import org.geomajas.annotation.Api;

/**
 * Constants with the ids for the standard toolbar widgets.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
public interface ToolId {

	/** Tool id for a group of buttons sharing a title and a layout (used for instance in a drop-down panel). */
	String BUTTON_GROUP = "ButtonGroup";
	
	/** Tool id for a drop-down button. */
	String DROP_DOWN_BUTTON = "DropDownButton";
	
	/** Tool id for toolbar separator. */
	String TOOL_SEPARATOR = "ToolbarSeparator";

	/** Tool id for the edit mode toolbar button. */
	String TOOL_EDIT = "EditMode";

	/** Tool id for the feature info mode toolbar button. */
	String TOOL_FEATURE_INFO = "FeatureInfoMode";

	/** Tool id for the measure distance toolbar button. */
	String TOOL_MEASURE_DISTANCE_MODE = "MeasureDistanceMode";

	/** Tool id for the pan toolbar button. */
	String TOOL_PAN_MODE = "PanMode";

	/** Tool id for the pan to selection toolbar button. */
	String TOOL_PAN_TO_SELECTION = "PanToSelection";

	/** Tool id for the selection toolbar button. */
	String TOOL_SELECTION_MODE = "SelectionMode";

	/**
	 * Tool id for the deselect all toolbar button.
	 * 
	 * @since 1.11.0
	 */
	String TOOL_DESELECT_ALL = "DeselectAll";

	/** Tool id for the zoom in toolbar button. */
	String TOOL_ZOOM_IN = "ZoomIn";

	/** Tool id for the zoom out toolbar button. */
	String TOOL_ZOOM_OUT = "ZoomOut";

	/** Tool id for the zoom in mode toolbar button. */
	String TOOL_ZOOM_IN_MODE = "ZoomInMode";

	/** Tool id for the zoom out mode toolbar button. */
	String TOOL_ZOOM_OUT_MODE = "ZoomOutMode";

	/** Tool id for the next zoom level toolbar button. */
	String TOOL_ZOOM_NEXT = "ZoomNext";

	/** Tool id for the previous zoom level toolbar button. */
	String TOOL_ZOOM_PREVIOUS = "ZoomPrevious";

	/** Tool id for the zoom to rectangle action toolbar button. */
	String TOOL_ZOOM_TO_RECTANGLE = "ZoomToRectangle";
	
	/** Tool id for the zoom to rectangle mode toolbar button. */
	String TOOL_ZOOM_TO_RECTANGLE_MODE = "ZoomToRectangleMode";

	/** Tool id for the zoom to selection toolbar button. */
	String TOOL_ZOOM_TO_SELECTION = "ZoomToSelection";

	/** Tool id for the scale selection toolbar widget. */
	String TOOL_SCALE_SELECT = "ScaleSelect";

	/** Tool id for the layer tree xxx toolbar button. */
	String TOOL_LAYER_VISIBLE = "LayerVisibleTool";

	/** Tool id for the layer tree xxx toolbar button. */
	String TOOL_LAYER_SNAPPING = "LayerSnappingTool";

	/** Tool id for the layer tree xxx toolbar button. */
	String TOOL_LAYER_LABELLED = "LayerLabeledTool";

	/** Tool id for the layer tree xxx toolbar button. */
	String TOOL_LAYER_REFRESH = "LayerRefreshAction";

	/** Tool id for a button consisting of a icon and a title. */
	String BUTTON_ICON_TITLE = "ButtonIconTitle";
	
	/** Tool id for a button consisting of a icon, a title and a description. */
	String BUTTON_ICON_TITLE_DESCRIPTION = "ButtonIconTitleDescription";

}
