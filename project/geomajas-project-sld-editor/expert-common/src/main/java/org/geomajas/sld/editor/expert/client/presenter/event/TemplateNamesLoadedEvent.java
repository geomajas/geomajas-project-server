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
 * Event that indicates that the list of template names has changed.
 * 
 * @author Kristof Heirwegh
 */
public class TemplateNamesLoadedEvent extends GwtEvent<TemplateNamesLoadedEvent.TemplateNamesLoadedHandler> {

	public TemplateNamesLoadedEvent() {
	}

	public static void fire(HasHandlers source) {
		TemplateNamesLoadedEvent eventInstance = new TemplateNamesLoadedEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, TemplateNamesLoadedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface HasTemplateNamesLoadedHandlers extends HasHandlers {
		HandlerRegistration addTemplateNamesLoadedHandler(TemplateNamesLoadedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface TemplateNamesLoadedHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onTemplateNamesLoaded(TemplateNamesLoadedEvent event);
	}

	private static final Type<TemplateNamesLoadedHandler> TYPE = new Type<TemplateNamesLoadedHandler>();

	public static Type<TemplateNamesLoadedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<TemplateNamesLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TemplateNamesLoadedHandler handler) {
		handler.onTemplateNamesLoaded(this);
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
		return "TemplateNamesLoadedEvent[" + "]";
	}
}