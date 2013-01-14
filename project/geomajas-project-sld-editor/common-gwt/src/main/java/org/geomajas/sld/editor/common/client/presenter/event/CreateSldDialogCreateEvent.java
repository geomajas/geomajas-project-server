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
package org.geomajas.sld.editor.common.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Event that indicates that the user has pushed the create button in the SLD creation dialog.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CreateSldDialogCreateEvent extends GwtEvent<CreateSldDialogCreateEvent.CreateSldDialogCreateHandler> {

	public CreateSldDialogCreateEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		CreateSldDialogCreateEvent eventInstance = new CreateSldDialogCreateEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, CreateSldDialogCreateEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasCreateSldDialogCreateHandlers extends HasHandlers {

		HandlerRegistration addCreateSldDialogCreateHandler(CreateSldDialogCreateHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface CreateSldDialogCreateHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onCreateSldPopupCreate(CreateSldDialogCreateEvent event);
	}

	private static final Type<CreateSldDialogCreateHandler> TYPE = new Type<CreateSldDialogCreateHandler>();

	public static Type<CreateSldDialogCreateHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<CreateSldDialogCreateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CreateSldDialogCreateHandler handler) {
		handler.onCreateSldPopupCreate(this);
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
		return "CreateSldDialogCreateEvent[" + "]";
	}
}
