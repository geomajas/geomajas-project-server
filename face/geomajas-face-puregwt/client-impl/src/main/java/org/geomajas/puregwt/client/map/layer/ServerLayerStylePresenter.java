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

package org.geomajas.puregwt.client.map.layer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Style presenter for serverside layers.
 * 
 * @author Pieter De Graef
 */
public class ServerLayerStylePresenter implements LayerStylePresenter {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface ContentWidgetViewUiBinder extends UiBinder<Widget, ServerLayerStylePresenter> {
	}

	private static final ContentWidgetViewUiBinder UI_BINDER = GWT.create(ContentWidgetViewUiBinder.class);

	private final int index;

	private final String title;

	private final String url;

	private Widget widget;

	@UiField
	protected Image icon;

	@UiField
	protected Label label;

	public ServerLayerStylePresenter(int index, String url, String title) {
		this.index = index;
		this.title = title;
		this.url = url;
	}

	public Widget asWidget() {
		if (widget == null) {
			widget = UI_BINDER.createAndBindUi(this);
			icon.setUrl(url);
			label.setText(title);
		}
		return widget;
	}

	public int getIndex() {
		return index;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}
}