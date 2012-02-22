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

import org.geomajas.sld.editor.common.client.model.RuleModel;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Provides call-back to be called when an attribute (or a group of attributes) of the SLD has been changed, usually as
 * a result of user editing of a form item in one of the SLD editor widgets.
 * 
 * @author An Buyle
 * 
 */
public class RuleChangedEvent extends GwtEvent<RuleChangedEvent.RuleChangedHandler> {

	private RuleModel ruleModel;

	public RuleChangedEvent(RuleModel ruleModel) {
		this.ruleModel = ruleModel;
	}

	public RuleModel getRuleModel() {
		return ruleModel;
	}

	/**
	 * @param source
	 * @param isComplete if true, the attribute/attributes that have changed do NOT result in an incomplete SLD (example
	 *        of isComplete false is when only the attribute of a rule filter has been specified and not yet the
	 *        operation)
	 * 
	 */
	public static void fire(HasHandlers source, RuleModel ruleModel) {
		RuleChangedEvent eventInstance = new RuleChangedEvent(ruleModel);
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasRuleChangedHandlers extends HasHandlers {

		HandlerRegistration addRuleChangedHandler(RuleChangedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface RuleChangedHandler extends EventHandler {

		/**
		 * Notifies content changed.
		 * 
		 * @param event the event
		 */
		void onChanged(RuleChangedEvent event);

	}

	/**
	 * Empty implementation of all handler methods.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public static class RuleChangedAdapter implements RuleChangedHandler {

		public void onChanged(RuleChangedEvent event) {
		}

	}

	private static final Type<RuleChangedHandler> TYPE = new Type<RuleChangedHandler>();

	public static Type<RuleChangedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<RuleChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RuleChangedHandler handler) {
		handler.onChanged(this);
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
		return "RuleChangedEvent[" + "]";
	}

}