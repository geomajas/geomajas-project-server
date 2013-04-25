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

package org.geomajas.puregwt.example.client;

import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.user.client.ui.LazyPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * General definition of a content panel for a single example in the showcase.
 * 
 * @author Pieter De Graef
 */
public abstract class ContentPanel extends LazyPanel implements RequiresResize {

	private ContentPanelView view;

	public abstract String getTitle();

	public abstract String getDescription();

	public abstract Widget getContentWidget();
	
	protected MapPresenter mapPresenter;
	
	protected ContentPanel(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	protected Widget createWidget() {
		view = new ContentPanelView();
		view.setName(getTitle());
		view.setDescription(getDescription());
		view.setContentWidget(getContentWidget());
		return view;
	}

	@Override
	public void onResize() {
		Widget widget = getWidget();
		if (widget instanceof RequiresResize) {
			((RequiresResize) widget).onResize();
		}
	}

	public MapPresenter getMapPresenter() {
		return mapPresenter;
	}
}