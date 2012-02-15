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
package org.geomajas.sld.client.presenter.event;

import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.client.model.SldGeneralInfo;
import org.geomajas.sld.filter.FilterTypeInfo;

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
public class SldContentChangedEvent extends GwtEvent<SldContentChangedEvent.SldContentChangedHandler> {

	private boolean isComplete;

	private SldGeneralInfo sldGeneralInfo;

	private FilterTypeInfo filterTypeInfo;
	
	private SymbolizerTypeInfo symbolizerInfo;
	
	private GraphicInfo graphicInfo;

	public SldContentChangedEvent(boolean isComplete, SldGeneralInfo sldGeneralInfo) {
		this.isComplete = isComplete;
		this.sldGeneralInfo = sldGeneralInfo;
	}

	public SldContentChangedEvent(boolean isComplete, FilterTypeInfo filterTypeInfo) {
		this.isComplete = isComplete;
		this.filterTypeInfo = filterTypeInfo;
	}

	public SldContentChangedEvent(boolean isComplete, SymbolizerTypeInfo symbolizerInfo) {
		this.isComplete = isComplete;
		this.symbolizerInfo = symbolizerInfo;
	}

	public SldContentChangedEvent(boolean isComplete, GraphicInfo graphicInfo) {
		this.isComplete = isComplete;
		this.graphicInfo = graphicInfo;
	}

	public boolean isContentComplete() {
		return this.isComplete;
	}

	/**
	 * @param source
	 * @param isComplete if true, the attribute/attributes that have changed do NOT result in an incomplete SLD (example
	 *        of isComplete false is when only the attribute of a rule filter has been specified and not yet the
	 *        operation)
	 * 
	 */
	public static void fire(HasHandlers source, boolean isComplete, SldGeneralInfo sldGeneralInfo) {
		SldContentChangedEvent eventInstance = new SldContentChangedEvent(isComplete, sldGeneralInfo);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, boolean isComplete, FilterTypeInfo filterInfo) {
		SldContentChangedEvent eventInstance = new SldContentChangedEvent(isComplete, filterInfo);
		source.fireEvent(eventInstance);
	}
	
	public static void fire(HasHandlers source, boolean isComplete, SymbolizerTypeInfo symbolizerInfo) {
		SldContentChangedEvent eventInstance = new SldContentChangedEvent(isComplete, symbolizerInfo);
		source.fireEvent(eventInstance);
	}
	
	public static void fire(HasHandlers source, boolean isComplete, GraphicInfo graphicInfo) {
		SldContentChangedEvent eventInstance = new SldContentChangedEvent(isComplete, graphicInfo);
		source.fireEvent(eventInstance);
	}
	

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldContentChangedHandlers extends HasHandlers {

		HandlerRegistration addSldContentChangedHandler(SldContentChangedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldContentChangedHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onSldGeneralInfoChanged(SldContentChangedEvent event);
		
		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onFilterInfoChanged(SldContentChangedEvent event);
		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onSymbolizerInfoChanged(SldContentChangedEvent event);
		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onGraphicInfoChanged(SldContentChangedEvent event);
	}

	/**
	 * Empty implementation of all handler methods.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public static abstract class SldContentChangedAdapter implements SldContentChangedHandler {

		public void onSldGeneralInfoChanged(SldContentChangedEvent event) {
		}
		
		public void onFilterInfoChanged(SldContentChangedEvent event) {
		}

		public void onSymbolizerInfoChanged(SldContentChangedEvent event) {
		}
		
		public void onGraphicInfoChanged(SldContentChangedEvent event) {
		}
	}

	private static final Type<SldContentChangedHandler> TYPE = new Type<SldContentChangedHandler>();

	public static Type<SldContentChangedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldContentChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldContentChangedHandler handler) {
		if (getSldGeneralInfo() != null) {
			handler.onSldGeneralInfoChanged(this);
		}
		if (getFilterTypeInfo() != null) {
			handler.onFilterInfoChanged(this);
		}
		if (getSymbolizerInfo() != null) {
			handler.onSymbolizerInfoChanged(this);
		}
		if (getGraphicInfo() != null) {
			handler.onGraphicInfoChanged(this);
		}
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
		return "SldContentChangedEvent[" + "]";
	}

	public SldGeneralInfo getSldGeneralInfo() {
		return sldGeneralInfo;
	}
	
	public FilterTypeInfo getFilterTypeInfo() {
		return filterTypeInfo;
	}
	
	public SymbolizerTypeInfo getSymbolizerInfo() {
		return symbolizerInfo;
	}
	
	public GraphicInfo getGraphicInfo() {
		return graphicInfo;
	}



}