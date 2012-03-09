/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Event fired when the viewing/editing/creation of 1 SLD has been closed by the user. 
 * It specifies the name of the SLD that has been viewed/edited/created {@link #fireSave(HasHandlers)}.
 * If the SLD has been edited/created, it has already been saved before this event is fired.
 * 
 * @author An Buyle
 * 
 */
public class SldEditSessionClosedEvent extends GwtEvent<SldEditSessionClosedEvent.SldEditSessionClosedHandler> {

	private String sldName;

	public SldEditSessionClosedEvent(String sldName) {
		this.sldName = sldName;
	}

	public String getSldName() {
		return this.sldName;
	}
	
	/**
	 * Fire a {@link SldEditSessionClosedEvent} to inform the observer(s)that viewing/editing/creation 
	 * of 1 SLD has been closed by the user.
	 * 
	 * @param source
	 * @param sldName
	 */
	public static void fire(HasHandlers source, String sldName) {
		SldEditSessionClosedEvent eventInstance = new SldEditSessionClosedEvent(sldName);
		source.fireEvent(eventInstance);
	}

	
	public static void fire(HasHandlers source, SldEditSessionClosedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author An Buyle
	 * 
	 */
	public interface HasSldEditSessionClosedHandlers extends HasHandlers {

		HandlerRegistration addSldEditSessionClosedHandler(SldEditSessionClosedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author An Buyle
	 * 
	 */
	public interface SldEditSessionClosedHandler extends EventHandler {

		/**
		 * Notifies that viewing/editing/creation of 1 SLD has been closed by the user.
		 * 
		 * @param event the event
		 */
		void onSldEditSessionClosed(SldEditSessionClosedEvent event);
	}

	private static final Type<SldEditSessionClosedHandler> TYPE = new Type<SldEditSessionClosedHandler>();

	public static Type<SldEditSessionClosedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldEditSessionClosedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldEditSessionClosedHandler handler) {
		handler.onSldEditSessionClosed(this);
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
		return "SldEditSessionClosedEvent[" + (null == sldName ? "{null}" : sldName) + "]";
	}

}