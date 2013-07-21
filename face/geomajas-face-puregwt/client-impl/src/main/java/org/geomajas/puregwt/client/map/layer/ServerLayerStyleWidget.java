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

import org.geomajas.sld.RuleInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * Widget that displays a single style for a {@link ServerLayer}. For a {@link VectorServerLayer} that widget will
 * represent a single SLD rule, for a {@link RasterServerLayer} this widget is used as the entire legend widget.
 * </p>
 * <p>
 * As this widget is used only by both the {@link VectorServerLayer} and the {@link RasterServerLayer}, the constructor
 * has protected visibility.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class ServerLayerStyleWidget implements IsWidget {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface ContentWidgetViewUiBinder extends UiBinder<Widget, ServerLayerStyleWidget> {
	}

	private static final ContentWidgetViewUiBinder UI_BINDER = GWT.create(ContentWidgetViewUiBinder.class);

	private final String title;

	private final String url;

	private Widget widget;

	@UiField
	protected Image icon;

	@UiField
	protected Label label;

	private RuleInfo rule;

	protected ServerLayerStyleWidget(String url, String title, RuleInfo rule) {
		this.title = title;
		this.url = url;
		this.rule = rule;
	}

	public Widget asWidget() {
		if (widget == null) {
			widget = UI_BINDER.createAndBindUi(this);
			icon.setUrl(url);
			label.setText(title);
		}
		return widget;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public RuleInfo getRule() {
		return rule;
	}
}