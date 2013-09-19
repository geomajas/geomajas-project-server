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
package org.geomajas.plugin.graphicsediting.example.client.annotation;

import org.geomajas.plugin.graphicsediting.example.client.annotation.SetAnnotationPresenter.Action;
import org.geomajas.plugin.graphicsediting.example.client.annotation.SetAnnotationPresenter.Handler;
import org.geomajas.gwt.client.widget.AbstractMapWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * AnnotationToolBar.
 * 
 * @author Jan De Moerloose
 */
public class AnnotationToolBar extends AbstractMapWidget implements SetAnnotationPresenter.View, MouseDownHandler {

	private static final Binder UIBINDER = GWT.create(Binder.class);

	/**
	 * {@link UiBinder} for this class.
	 * 
	 * @author Jan De Moerloose
	 */
	interface Binder extends UiBinder<FlowPanel, AnnotationToolBar> {
	}

	private FlowPanel flowLayout;

	private Handler handler;
	
	@UiField
	protected CheckBox duplicateCheckbox;

	@UiField
	protected ToggleButton createLineButton;

	@UiField
	protected ToggleButton createTextButton;

	@UiField
	protected ToggleButton createAnchoredTextButton;
	
	@UiField
	protected ToggleButton createPolygonButton;

	@UiField
	protected ToggleButton createRectangleButton;

	@UiField
	protected ToggleButton mapButton;
	
	@UiField
	protected ToggleButton createIconButton;
	
	@UiField
	protected ToggleButton createAnchoredIconButton;
	

	public AnnotationToolBar() {
		super(null);
		flowLayout = UIBINDER.createAndBindUi(this);
		flowLayout.addDomHandler(this, MouseDownEvent.getType());
		asWidget().getElement().getStyle().setPosition(Position.ABSOLUTE);
		asWidget().getElement().getStyle().setTop(20, Unit.PX);
		asWidget().getElement().getStyle().setLeft(100, Unit.PX);
	}
	
	@UiHandler("duplicateCheckbox")
	void handleDuplicateCheck(ClickEvent e) {
		handler.onDuplicateWhenDragging(duplicateCheckbox.getValue());
	}

	@UiHandler("createLineButton")
	void handleCreateLine(ClickEvent e) {
		handler.onAction(Action.CREATE_LINE);
		e.stopPropagation();
	}
	
	@UiHandler("createPolygonButton")
	void handleCreatePolygon(ClickEvent e) {
		handler.onAction(Action.CREATE_POLYGON);
		e.stopPropagation();
	}
	
	@UiHandler("createRectangleButton")
	void handleCreateRectangle(ClickEvent e) {
		handler.onAction(Action.CREATE_RECTANGLE);
		e.stopPropagation();
	}
	
	@UiHandler("createTextButton")
	void handleCreateText(ClickEvent e) {
		handler.onAction(Action.CREATE_TEXT);
		e.stopPropagation();
	}
	
	@UiHandler("mapButton")
	void handleMap(ClickEvent e) {
		handler.onAction(Action.MAP);
		e.stopPropagation();
	}
	
	@UiHandler("createIconButton")
	void handleIcon(ClickEvent e) {
		handler.onAction(Action.CREATE_ICON);
		e.stopPropagation();
	}
	
	@UiHandler("createAnchoredIconButton")
	void handleAnchoredIcon(ClickEvent e) {
		handler.onAction(Action.CREATE_ANCHORED_ICON);
		e.stopPropagation();
	}
	
	@UiHandler("createAnchoredTextButton")
	void handleAnchoredText(ClickEvent e) {
		handler.onAction(Action.CREATE_ANCHORED_TEXT);
		e.stopPropagation();
	}
	
	@Override
	public void setActive(Action action, boolean active) {
		switch(action) {
			case CREATE_LINE:
				createLineButton.setDown(active);
				break;
			case CREATE_POLYGON:
				createPolygonButton.setDown(active);
				break;
			case CREATE_RECTANGLE:
				createRectangleButton.setDown(active);
				break;
			case CREATE_TEXT:
				createTextButton.setDown(active);
				break;
			case CREATE_ANCHORED_TEXT:
				createAnchoredTextButton.setDown(active);
				break;
			case MAP:
				mapButton.setDown(active);
				break;
			case CREATE_ICON:
				createIconButton.setDown(active);
				break;
			case CREATE_ANCHORED_ICON:
				createAnchoredIconButton.setDown(active);
				break;
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		event.stopPropagation();
	}

	@Override
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public Widget asWidget() {
		return flowLayout;
	}

}
