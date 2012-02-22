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
 * Event fired when a rule has been selected.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RuleSelectedEvent extends GwtEvent<RuleSelectedEvent.RuleSelectedHandler> {

	private RuleModel ruleModel;

	private boolean clearAll;

	public RuleSelectedEvent() {
		this.clearAll = true;
	}

	public RuleSelectedEvent(RuleModel ruleModel) {
		this.ruleModel = ruleModel;
	}

	public boolean isClearAll() {
		return clearAll;
	}

	public static void fireSelected(HasHandlers source, RuleModel ruleModel) {
		RuleSelectedEvent eventInstance = new RuleSelectedEvent(ruleModel);
		source.fireEvent(eventInstance);
	}

	public static void fireClearAll(HasHandlers source) {
		RuleSelectedEvent eventInstance = new RuleSelectedEvent();
		source.fireEvent(eventInstance);
	}

	public RuleModel getRuleModel() {
		return ruleModel;
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasRuleSelectedHandlers extends HasHandlers {

		HandlerRegistration addRuleSelectedHandler(RuleSelectedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface RuleSelectedHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onRuleSelected(RuleSelectedEvent event);
	}

	private static final Type<RuleSelectedHandler> TYPE = new Type<RuleSelectedHandler>();

	public static Type<RuleSelectedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<RuleSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RuleSelectedHandler handler) {
		handler.onRuleSelected(this);
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
		return "RuleSelectedEvent[" + "]";
	}
}