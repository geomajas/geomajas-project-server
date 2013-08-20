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
package org.geomajas.gwt.widget.client.mouseover.component;

import java.util.List;

import org.geomajas.gwt.widget.client.mouseover.resources.ToolTipBoxResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tool tip box that is shown when the MouseOverListener is added to the map.
 * 
 * @author Dosi Bingov
 * 
 */
public class ToolTipBox implements IsWidget {

	private PopupPanel thepanel;

	private String title;

	@UiField
	protected VerticalPanel contentPanel;

	private static final ToolTipBoxUiBinder UIBINDER = GWT.create(ToolTipBoxUiBinder.class);

	/**
	 * UI binder interface.
	 * 
	 * @author Dosi Bingov
	 * 
	 */
	interface ToolTipBoxUiBinder extends UiBinder<Widget, ToolTipBox> {
	}

	public ToolTipBox() {
		thepanel = (PopupPanel) UIBINDER.createAndBindUi(this);

		ToolTipBoxResource.INSTANCE.css().ensureInjected();
		thepanel.addStyleName(ToolTipBoxResource.INSTANCE.css().toolTipBox());
	}

	public void show(int left, int top, boolean animated) {
		thepanel.setPopupPosition(left, top);
		thepanel.setAnimationEnabled(animated);
		thepanel.show();
	}

	public PopupPanel getBox() {
		return this.thepanel;
	}

	public void hide() {
		thepanel.hide();
	}

	@Override
	public Widget asWidget() {
		return thepanel;
	}

	public void addContentAndShow(List<String> content, int left, int top, boolean animated) {

		for (String s : content) {
			Label label = new Label(s);
			label.addStyleName(ToolTipBoxResource.INSTANCE.css().toolTipBoxCell());
			contentPanel.add(label);
		}

		thepanel.setAnimationEnabled(animated);
		thepanel.setPopupPosition(left, top);
		thepanel.show();
	}

	public void clearContent() {
		contentPanel.clear();
	}

	public void setContentTitle(String contentTitle) {

		if (contentPanel.getWidgetCount() > 0 && title.equals(((Label) contentPanel.getWidget(0)).getText())) {
			((Label) contentPanel.getWidget(0)).setText(contentTitle);
		} else {
			Label title = new Label(contentTitle);
			title.addStyleName(ToolTipBoxResource.INSTANCE.css().toolTipBoxCellTitle());
			contentPanel.insert(title, 0);
			contentPanel.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_CENTER);
		}

		title = contentTitle;
	}

	public String getContentTitle() {
		return title;
	}

	public void addContentLine(String s) {
		Label label = new Label(s);
		label.addStyleName(ToolTipBoxResource.INSTANCE.css().toolTipBoxCell());
		contentPanel.add(label);
	}
}
