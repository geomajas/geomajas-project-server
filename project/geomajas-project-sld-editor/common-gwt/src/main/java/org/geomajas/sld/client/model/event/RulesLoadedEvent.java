/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.client.model.event;

import java.util.List;

import org.geomajas.sld.FeatureTypeStyleInfo;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class RulesLoadedEvent extends GwtEvent<RulesLoadedEvent.RulesLoadedHandler> {

	private List<FeatureTypeStyleInfo> featureTypeStyleList;

	public RulesLoadedEvent(List<FeatureTypeStyleInfo>  featureTypeStyleList) {
		this.featureTypeStyleList = featureTypeStyleList;
	}

	public static void fire(HasHandlers source, List<FeatureTypeStyleInfo> featureTypeStyleList) {
		RulesLoadedEvent eventInstance = new RulesLoadedEvent(featureTypeStyleList);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, RulesLoadedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}
	
	public List<FeatureTypeStyleInfo> getFeatureTypeStyleList() {
		return featureTypeStyleList;
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasRulesLoadedHandlers extends HasHandlers {

		HandlerRegistration addRulesLoadedHandler(RulesLoadedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface RulesLoadedHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onRulesLoaded(RulesLoadedEvent event);
	}

	private static final Type<RulesLoadedHandler> TYPE = new Type<RulesLoadedHandler>();

	public static Type<RulesLoadedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<RulesLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RulesLoadedHandler handler) {
		handler.onRulesLoaded(this);
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
		return "RulesLoadedEvent[" + "]";
	}
}