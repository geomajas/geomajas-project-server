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
 * Event fired when the user selects a template.
 * 
 * @author Kristof Heirwegh
 */
public class TemplateSelectEvent extends GwtEvent<TemplateSelectEvent.TemplateSelectHandler> {

	private String templateName;

	public TemplateSelectEvent(String sldInfo) {
		this.templateName = sldInfo;
	}

	public String getTemplateName() {
		return templateName;
	}

	public static void fire(HasHandlers source, String templateName) {
		TemplateSelectEvent eventInstance = new TemplateSelectEvent(templateName);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, TemplateSelectEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Kristof Heirwegh
	 * 
	 */
	public interface HasTemplateSelectHandlers extends HasHandlers {
		HandlerRegistration addTemplateSelectHandler(TemplateSelectHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Kristof Heirwegh
	 * 
	 */
	public interface TemplateSelectHandler extends EventHandler {
		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onTemplateSelect(TemplateSelectEvent event);
	}

	private static final Type<TemplateSelectHandler> TYPE = new Type<TemplateSelectHandler>();

	public static Type<TemplateSelectHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<TemplateSelectHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TemplateSelectHandler handler) {
		handler.onTemplateSelect(this);
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
		return "TemplateSelectEvent[" + "]";
	}
}