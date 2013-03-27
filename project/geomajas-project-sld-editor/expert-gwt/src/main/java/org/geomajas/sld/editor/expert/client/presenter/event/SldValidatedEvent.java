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
 * Event that returns the result of a validationRequest
 * 
 * @author Kristof Heirwegh
 */
public class SldValidatedEvent extends GwtEvent<SldValidatedEvent.SldValidatedHandler> {

	private final boolean valid;
	
	public SldValidatedEvent(boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

	public static void fireValid(HasHandlers source) {
		SldValidatedEvent eventInstance = new SldValidatedEvent(true);
		source.fireEvent(eventInstance);
	}

	public static void fireInValid(HasHandlers source) {
		SldValidatedEvent eventInstance = new SldValidatedEvent(false);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldValidatedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface HasSldValidatedHandlers extends HasHandlers {
		HandlerRegistration addSldValidatedHandler(SldValidatedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface SldValidatedHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onSldValidated(SldValidatedEvent event);
	}

	private static final Type<SldValidatedHandler> TYPE = new Type<SldValidatedHandler>();

	public static Type<SldValidatedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldValidatedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldValidatedHandler handler) {
		handler.onSldValidated(this);
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
		return "SldValidatedEvent[" + "]";
	}
}