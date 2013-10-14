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

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.GText;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.GraphicsService;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Controller that creates a {@link GText}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CreateTextController extends CreateController<GText> implements MouseUpHandler {

	private boolean active;

	private HandlerRegistration registration;
	
	private TextPopup popup = new TextPopup();
	
	protected EnterHandler handler = new EnterHandler();

	private List<HandlerRegistration> popupRegs = new ArrayList<HandlerRegistration>();
	
	private Coordinate position;


	public CreateTextController(GraphicsService graphicsService) {
		super(graphicsService);
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			registration = getObjectContainer().addMouseUpHandler(this);
		} else {
			if (registration != null) {
				registration.removeHandler();
				registration = null;
			}
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {	
		position = getUserCoordinate(event);
		popup.clearAndShow(event.getClientX(), event.getClientY());
		popupRegs.add(popup.addCloseHandler(handler));
		popupRegs.add(popup.addDomHandler(handler, KeyDownEvent.getType()));
		DOM.setCapture(popup.getElement());
	}
	
	public void clearPopup() {
		DOM.releaseCapture(popup.getElement());
		popup.hide();
		for (HandlerRegistration reg : popupRegs) {
			reg.removeHandler();
		}
		popupRegs.clear();
	}

	protected GText createText(String text) {
		return CreateTextController.createTextDefault(text, position);
	}
	
	public static GText createTextDefault(String text, Coordinate position) {
		GText result = new GText(0, 0, text);
		result.setPosition(position);
		result.setFontColor("black");
		return result;
	}

	protected void addObject(GText result) {
		if (result == null) {
			execute(null);
			return;
		}
		execute(new AddOperation(result));
	}

	protected Coordinate getClickPosition() {
		return position;
	}
	
	protected String getPopupText() {
		return popup.getText();
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
			setText("");
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
	 * Handles ENTER key event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	protected class EnterHandler implements KeyDownHandler, CloseHandler<PopupPanel> {

		@Override
		public void onKeyDown(KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				String text = popup.getText();
				if (text != null && !text.isEmpty()) {
					addObject(createText(text));
				}
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
		// TODO Auto-generated method stub
		
	}

}
