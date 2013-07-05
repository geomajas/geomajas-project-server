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
package org.geomajas.puregwt.widget.client.featureselectbox.view;

import java.util.List;

import org.geomajas.puregwt.widget.client.featureselectbox.presenter.FeatureSelectBoxPresenter.Handler;
import org.geomajas.puregwt.widget.client.featureselectbox.resources.FeatureSelectBoxResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
public class FeatureSelectBox implements
		org.geomajas.puregwt.widget.client.featureselectbox.presenter.FeatureSelectBoxPresenter.View {

	private Handler handler;

	private PopupPanel thepanel;

	private int xPos;

	private int yPos;

	@UiField
	protected VerticalPanel contentPanel;

	private static final FeatureSelectBoxUiBinder UIBINDER = GWT.create(FeatureSelectBoxUiBinder.class);

	/**
	 * UI binder interface.
	 * 
	 * @author Dosi Bingov
	 * 
	 */
	interface FeatureSelectBoxUiBinder extends UiBinder<Widget, FeatureSelectBox> {
	}

	/**
	 * FeatureClickHandler that handles click on feature label.
	 * 
	 * @author Dosi Bingov
	 * 
	 */
	private class FeatureClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			Label label = (Label) event.getSource();
			handler.onFeatureSelected(label.getText());
		}
	}

	private FeatureClickHandler featureClickHandler = new FeatureClickHandler();

	public FeatureSelectBox() {
		thepanel = (PopupPanel) UIBINDER.createAndBindUi(this);
		thepanel.addStyleName(FeatureSelectBoxResource.INSTANCE.css().featureSelectBox());
		FeatureSelectBoxResource.INSTANCE.css().ensureInjected();

	}

	public void hide() {
		thepanel.hide();
	}

	@Override
	public Widget asWidget() {
		return thepanel;
	}

	public void clear() {
		contentPanel.clear();
	}

	@Override
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void setLabels(List<String> labels) {
		contentPanel.clear();

		for (String s : labels) {
			Label label = new Label();
			label.setStyleName(FeatureSelectBoxResource.INSTANCE.css().featureSelectBoxCell());
			label.setText(s);
			label.addClickHandler(featureClickHandler);
			contentPanel.add(label);
		}
	}

	@Override
	public void show(boolean animated) {
		// show widget only if there is content to show
		if (contentPanel.getWidgetCount() > 0) {
			thepanel.setPopupPosition(xPos, yPos);
			thepanel.setAnimationEnabled(animated);
			thepanel.show();
		}
	}

	@Override
	public void addLabel(String label) {
		Label l = new Label();
		l.setText(label);
		l.setStyleName(FeatureSelectBoxResource.INSTANCE.css().featureSelectBoxCell());
		l.addClickHandler(featureClickHandler);
		contentPanel.add(l);
	}

	@Override
	public void setShowPosition(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

	@Override
	public boolean isVisible() {
		return thepanel.isVisible();
	}
}
