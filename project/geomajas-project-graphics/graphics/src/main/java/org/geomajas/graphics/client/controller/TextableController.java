/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.role.Textable;
import org.geomajas.graphics.client.operation.LabelOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Controller to change object label.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TextableController extends AbstractGraphicsController implements DoubleClickHandler {

	private boolean active;

	private HandlerRegistration registration;

	private TextPopup popup = new TextPopup();

	private EnterHandler handler = new EnterHandler();

	private List<HandlerRegistration> popupRegs = new ArrayList<HandlerRegistration>();

	private Textable object;

	public TextableController(GraphicsObject object, GraphicsService graphicsService) {
		super(graphicsService, object);
		this.object = object.getRole(Textable.TYPE);
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			registration = getObjectContainer().addDoubleClickHandler(this);
		} else {
			if (registration != null) {
				registration.removeHandler();
				registration = null;
			}
		}
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		popup.setText(object.getLabel());
		popup.clearAndShow(event.getClientX(), event.getClientY());
		popupRegs.add(popup.addCloseHandler(handler));
		popupRegs.add(popup.addDomHandler(handler, KeyDownEvent.getType()));
		DOM.setCapture(popup.getElement());
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	public void clearPopup() {
		DOM.releaseCapture(popup.getElement());
		popup.hide();
		for (HandlerRegistration reg : popupRegs) {
			reg.removeHandler();
		}
		popupRegs.clear();
	}

	/**
	 * 
	 */
	private static class TextPopup extends PopupPanel {

		private TextBox box;

		public TextPopup() {
			super(true);
			box = new TextBox();
			box.setVisibleLength(10);
			setWidget(box);
		}

		public void clearAndShow(final int clientX, final int clientY) {
			setPopupPositionAndShow(new PositionCallback() {

				@Override
				public void setPosition(int offsetWidth, int offsetHeight) {
					setPopupPosition(clientX, clientY - offsetHeight);
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {

						@Override
						public void execute() {
							box.setFocus(true);
						}
					});
				}
			});
		}

		public String getText() {
			return box.getText();
		}

		public void setText(String text) {
			box.setText(text);
		}

	}

	/**
	 * Handles ENTER key for login.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class EnterHandler implements KeyDownHandler, CloseHandler<PopupPanel> {

		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				execute(new LabelOperation(getObject(), null, object.getLabel(), popup.getText()));
				clearPopup();
			}
		}

		@Override
		public void onClose(CloseEvent<PopupPanel> event) {
			clearPopup();
		}

	}

	@Override
	public void setVisible(boolean visible) {
		getObject().asObject().setVisible(visible);
	}

}
