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
 * Event that indicates that the template has loaded.
 * 
 * @author Kristof Heirwegh
 */
public class TemplateLoadedEvent extends GwtEvent<TemplateLoadedEvent.TemplateLoadedHandler> {

	public TemplateLoadedEvent() {
	}

	public static void fire(HasHandlers source) {
		TemplateLoadedEvent eventInstance = new TemplateLoadedEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, TemplateLoadedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface HasTemplateLoadedHandlers extends HasHandlers {
		HandlerRegistration addTemplateLoadedHandler(TemplateLoadedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface TemplateLoadedHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onTemplateLoaded(TemplateLoadedEvent event);
	}

	private static final Type<TemplateLoadedHandler> TYPE = new Type<TemplateLoadedHandler>();

	public static Type<TemplateLoadedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<TemplateLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TemplateLoadedHandler handler) {
		handler.onTemplateLoaded(this);
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
		return "TemplateLoadedEvent[" + "]";
	}
}