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
package org.geomajas.sld.editor.common.client.model.event;


import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Event that indicates that an SLD has been selected.
 * 
 * @author An Buyle
 * 
 */
public class SldSelectEvent extends GwtEvent<SldSelectEvent.SldSelectHandler> {

	private String sldName;

	

	public SldSelectEvent(String sldName) {
		this.sldName = sldName;
	}

	public String getSldName() {
		return sldName;
	}

	
	public static void fire(HasHandlers source, String sldName) {
		SldSelectEvent eventInstance = new SldSelectEvent(sldName);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldSelectEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldSelectHandlers extends HasHandlers {

		HandlerRegistration addSldSelectHandler(SldSelectHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldSelectHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onSldSelect(SldSelectEvent event);
	}

	private static final Type<SldSelectHandler> TYPE = new Type<SldSelectHandler>();

	public static Type<SldSelectHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldSelectHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldSelectHandler handler) {
		handler.onSldSelect(this);
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
		return "SldSelectEvent[" + "]";
	}
}