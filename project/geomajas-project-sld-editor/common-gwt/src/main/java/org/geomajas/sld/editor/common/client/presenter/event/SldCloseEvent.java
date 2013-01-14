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
 * Event fired when the user wants to close the current SLD. It has an additional option to save the current
 * SLD before closing {@link #fireSave(HasHandlers)}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldCloseEvent extends GwtEvent<SldCloseEvent.SldCloseHandler> {

	private boolean save;

	public SldCloseEvent(boolean save) {
		this.save = save;
	}

	public boolean isSave() {
		return save;
	}
	
	/**
	 * Fires a {@link SldCloseEvent} that will save the current SLD and close it. If not successful, the current SLD
	 * remains open.
	 * 
	 * @param source
	 * @param name
	 */
	public static void fireSave(HasHandlers source) {
		SldCloseEvent eventInstance = new SldCloseEvent(true);
		source.fireEvent(eventInstance);
	}

	/**
	 * Fires a {@link SldCloseEvent} that will close the current SLD regardless of changes.
	 * 
	 * @param source
	 * @param name
	 */
	public static void fire(HasHandlers source) {
		SldCloseEvent eventInstance = new SldCloseEvent(false);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldCloseEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldCloseHandlers extends HasHandlers {

		HandlerRegistration addSldCloseHandler(SldCloseHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldCloseHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onSldClose(SldCloseEvent event);
	}

	private static final Type<SldCloseHandler> TYPE = new Type<SldCloseHandler>();

	public static Type<SldCloseHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldCloseHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldCloseHandler handler) {
		handler.onSldClose(this);
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
		return "SldCloseEvent[" + "]";
	}

}