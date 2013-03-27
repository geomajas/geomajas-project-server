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

package org.geomajas.widget.advancedviews.client;

import com.google.gwt.i18n.client.Messages;


/**
 * Messages for the Advanced Views widgets.
 *
 * @author Kristof Heirwegh
 */
public interface AdvancedViewsMessages extends Messages {

	String expandingThemeWidgetNoThemeAvailableTitle();
	String expandingThemeWidgetTooltip();
	String expandingThemeWidgetTitle();
	
	// Configuration of Themes
	String themeConfigThemeImage();
	String themeConfigThemeTurnsOtherLayersOff();
	String themeConfigThemeGridNameField();
	String themeConfigThemeConfigGroup();
	String themeConfigBreadcrumbThemeConfig();
	String themeConfigBreadcrumbViewConfig();
	String themeConfigBreadcrumbRangeConfig();
	String themeConfigBreadcrumbLayerConfig();
	String themeConfigViewRemove();
	String themeConfigViewRemoveConfirm();
	String themeConfigViewAdd();
	String themeConfigViewDefaultNewTitle();
	String themeConfigViewName();
	String themeConfigViewDescription();
	String themeConfigViewConfigGroup();
	String themeConfigViewGridMinScaleField();
	String themeConfigViewGridMaxScaleField();
	String themeConfigRangeRemove(); 
	String themeConfigRangeRemoveConfirm();
	String themeConfigRangeTitle();
	String themeConfigRangeMinScale();
	String themeConfigRangeMaxScale();
	String themeConfigLayerGridNameField();
	String themeConfigLayerGridVisibleField();
	String themeConfigLayerGridOpacityField();
	String themeConfigLayerRemove();
	String themeConfigLayerAdd();
	String themeConfigLayerRemoveConfirm();
	String themeConfigLayerTitle();
	String themeConfigLayerVisibility();
	String themeConfigLayerVisibilityOn();
	String themeConfigLayerVisibilityOff();
	String themeConfigLayerOpacity();
	String themeConfigRangeAddInline();
	String themeConfigRangeAdd();
	String themeConfigLayerSelect();
	String themeConfigInvalidRangeWarning();
	
	String detailTabThemes();
}
