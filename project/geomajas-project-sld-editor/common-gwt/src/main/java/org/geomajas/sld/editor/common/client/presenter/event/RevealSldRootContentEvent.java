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
import com.gwtplatform.mvp.client.Presenter;

/**
 * 
 * @author An Buyle
 * 
 */
public class RevealSldRootContentEvent extends 
		GwtEvent<RevealSldRootContentEvent.RevealSldRootContentHandler> {
	
	private static final Type<RevealSldRootContentHandler> TYPE = new Type<RevealSldRootContentHandler>();

	private final Presenter<?, ?> content;
	  
	public RevealSldRootContentEvent(final Presenter<?, ?> content) {
		 this.content = content;
	}
	
	
	public static void fire(final HasHandlers source, final Presenter<?, ?> content) {
		RevealSldRootContentEvent eventInstance = new RevealSldRootContentEvent(content);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, RevealSldRootContentEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	public Presenter<?, ?> getContent() {
		 return content;
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author An Buyle
	 * 
	 */
	public interface HasRevealSldRootContentHandlers extends HasHandlers {

		HandlerRegistration addRevealSldRootContentHandler(RevealSldRootContentHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author An Buyle
	 * 
	 */
	public interface RevealSldRootContentHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onRevealSldRootContent(RevealSldRootContentEvent event);
	}


	public static Type<RevealSldRootContentHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<RevealSldRootContentHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RevealSldRootContentHandler handler) {
		handler.onRevealSldRootContent(this);
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
		return "revealSldRootContentEvent[" + "]";
	}
}