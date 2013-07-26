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
package org.geomajas.sld.editor.expert.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Event that indicates that the Sld must be Validated.
 * <p>Throws Validated or SaveEvent (if saveAfterValidate is selected and validation == ok).
 * 
 * @author Kristof Heirwegh
 */
public class SldValidateEvent extends GwtEvent<SldValidateEvent.SldValidateHandler> {

	private final boolean saveAfterValidate;
	
	public SldValidateEvent(boolean saveAfterValidate) {
		this.saveAfterValidate = saveAfterValidate;
	}

	public boolean isSaveAfterValidate() {
		return saveAfterValidate;
	}

	public static void fire(HasHandlers source) {
		SldValidateEvent eventInstance = new SldValidateEvent(false);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldValidateEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface HasSldValidateHandlers extends HasHandlers {
		HandlerRegistration addSldValidateHandler(SldValidateHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface SldValidateHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onSldValidate(SldValidateEvent event);
	}

	private static final Type<SldValidateHandler> TYPE = new Type<SldValidateHandler>();

	public static Type<SldValidateHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldValidateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldValidateHandler handler) {
		handler.onSldValidate(this);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "SldValidateEvent[" + "]";
	}
}