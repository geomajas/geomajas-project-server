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

package org.geomajas.puregwt.example.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Global resource bundle for this showcase application.
 * 
 * @author Pieter De Graef
 */
public interface ShowcaseResource extends ClientBundle {

	@Source("showcase.css")
	ShowcaseCssResource css();

	@Source("image/geomajas_icon_small.png")
	ImageResource geomajasIconSmall();

	@Source("image/geomajas_logo.png")
	ImageResource geomajasLogo();

	@Source("image/geomajas_logo_bg.png")
	ImageResource geomajasLogoBg();

	@Source("image/geosparc_banner.png")
	ImageResource geosparcBanner();

	@Source("image/background.jpg")
	ImageResource background();

	@Source("image/header.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	ImageResource header();

	@Source("image/sub_header.png")
	ImageResource subHeader();

	@Source("image/sample_bg.png")
	ImageResource sampleBackground();

	// Icons:

	@Source("image/icon/close.png")
	ImageResource iconClose();

	@Source("image/icon/close_hover.png")
	ImageResource iconCloseHover();

	@Source("image/icon/arrow_down.png")
	ImageResource iconArrowDown();

	@Source("image/icon/arrow_down_hover.png")
	ImageResource iconArrowDownHover();

	@Source("image/icon/arrow_up.png")
	ImageResource iconArrowUp();

	@Source("image/icon/arrow_up_hover.png")
	ImageResource iconArrowUpHover();

	@Source("image/icon/arrow_left.png")
	ImageResource iconArrowLeft();

	@Source("image/icon/arrow_left_hover.png")
	ImageResource iconArrowLeftHover();

	@Source("image/icon/list_view.png")
	ImageResource listView();

	@Source("image/icon/list_view_hover.png")
	ImageResource listViewHover();

	@Source("image/icon/block_view.png")
	ImageResource blockView();

	@Source("image/icon/block_view_hover.png")
	ImageResource blockViewHover();

	// Panel images:

	@Source("image/panel/panel_top_left.png")
	@ImageOptions(height = 6, width = 6)
	ImageResource panelTopLeft();

	@Source("image/panel/panel_top_right.png")
	@ImageOptions(height = 6, width = 6)
	ImageResource panelTopRight();

	@Source("image/panel/panel_bottom_left.png")
	@ImageOptions(height = 6, width = 6)
	ImageResource panelBottomLeft();

	@Source("image/panel/panel_bottom_right.png")
	@ImageOptions(height = 6, width = 6)
	ImageResource panelBottomRight();

	@Source("image/panel/panel_top.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	ImageResource panelTop();

	@Source("image/panel/panel_right.png")
	@ImageOptions(repeatStyle = RepeatStyle.Vertical)
	ImageResource panelRight();

	@Source("image/panel/panel_bottom.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	ImageResource panelBottom();

	@Source("image/panel/panel_left.png")
	@ImageOptions(repeatStyle = RepeatStyle.Vertical)
	ImageResource panelLeft();

	@Source("image/panel/panel_middle.png")
	@ImageOptions(repeatStyle = RepeatStyle.Both)
	ImageResource panelMiddle();
}