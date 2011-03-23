/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.puregwt.client.map.event;

import org.geomajas.global.Api;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Geomajas event bus.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface EventBus {

	<H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler);

	<H extends EventHandler> HandlerRegistration addHandlerToSource(Type<H> type, Object source, H handler);

	void fireEvent(GwtEvent<?> event);

	void fireEventFromSource(GwtEvent<?> event, Object source);
}