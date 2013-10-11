/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.client;

import java.util.List;

import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeEvent;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeHandler;
import org.geomajas.plugin.geocoder.client.event.SelectLocationEvent;
import org.geomajas.plugin.geocoder.client.event.SelectLocationHandler;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringRequest;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

/**
 * Widget for starting a geocoder location search.
 *
 * @author Pieter De Graef
 */
public class GeocoderPresenter implements SelectLocationHandler, SelectAlternativeHandler {

	private MapPresenter mapPresenter;

	private GeocoderTextBox geocoderTextBox;

	private String servicePattern = ".*";

	private HandlerManager handlerManager;
	
	private GeocoderMessages messages = GWT.create(GeocoderMessages.class);


	/**
	 * Create geocoder widget which allows searching a location from a string.
	 *
	 * @param mapPresenter map to apply search results
	 * @param geocoderTextBox geocoder widget
	 */
	GeocoderPresenter(MapPresenter mapPresenter, GeocoderTextBox geocoderTextBox) {
		this.mapPresenter = mapPresenter;
		this.geocoderTextBox = geocoderTextBox;

		handlerManager = new HandlerManager(this);
		setSelectAlternativeHandler(this);
		setSelectLocationHandler(this);
	}

	/**
	 * Clear the current location.
	 */
	public void clearLocation() {
		geocoderTextBox.setValue("");
	}

	/**
	 * Go to the location which matches the given string.
	 *
	 * @param location location
	 */
	public void goToLocation(final String location) {
		GwtCommand command = new GwtCommand(GetLocationForStringRequest.COMMAND);
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs(mapPresenter.getViewPort().getCrs());
		request.setLocation(location);
		request.setServicePattern(servicePattern);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetLocationForStringResponse>() {
			public void execute(GetLocationForStringResponse response) {
				goToLocation(response, location);
			}
		});
	}


	/**
	 * Go to the location which matches the given string.
	 *
	 * @param response get location command response
	 * @param location location to go to
	 */
	public void goToLocation(final GetLocationForStringResponse response, final String location) {
		if (response.isLocationFound()) {
			handlerManager.fireEvent(new SelectLocationEvent(mapPresenter, response));
		} else {
			List<GetLocationForStringAlternative> alternatives = response.getAlternatives();
			if (null != alternatives && alternatives.size() > 0) {
				handlerManager.fireEvent(new SelectAlternativeEvent(mapPresenter, alternatives));
			} else {
				// TODO This should throw an event...
				Window.alert(messages.locationNotFound(location));
			}
		}
	}

	/**
	 * Get the regular expression which is used to select which geocoder services to use.
	 *
	 * @return geocoder selection regular expression
	 */
	public String getServicePattern() {
		return servicePattern;
	}

	/**
	 * Set the regular expression which is used to select which geocoder services to use.
	 *
	 * @param servicePattern geocoder selection regular expression
	 */
	public void setServicePattern(String servicePattern) {
		this.servicePattern = servicePattern;
	}

	/**
	 * Set the select alternative handler.
	 * <p/>
	 * There can only be one handler, the default displays the alternatives in a window on the map widget.
	 *
	 * @param handler select alternative handler
	 * @return handler registration.
	 */
	public HandlerRegistration setSelectAlternativeHandler(SelectAlternativeHandler handler) {
		if (handlerManager.getHandlerCount(SelectAlternativeHandler.TYPE) > 0) {
			SelectAlternativeHandler previous = handlerManager.getHandler(SelectAlternativeHandler.TYPE, 0);
			handlerManager.removeHandler(SelectAlternativeHandler.TYPE, previous);
		}
		return handlerManager.addHandler(SelectAlternativeHandler.TYPE, handler);
	}

	/**
	 * Set the select location handler.
	 * <p/>
	 * There can only be one handler, the default zooms the map widget to the selected location.
	 *
	 * @param handler select location handler
	 * @return handler registration.
	 */
	public HandlerRegistration setSelectLocationHandler(SelectLocationHandler handler) {
		if (handlerManager.getHandlerCount(SelectLocationHandler.TYPE) > 0) {
			SelectLocationHandler previous = handlerManager.getHandler(SelectLocationHandler.TYPE, 0);
			handlerManager.removeHandler(SelectLocationHandler.TYPE, previous);
		}
		return handlerManager.addHandler(SelectLocationHandler.TYPE, handler);
	}

	@Override
	public void onSelectAlternative(SelectAlternativeEvent event) {
		// TODO implement me...
	}

	@Override
	public void onSelectLocation(SelectLocationEvent event) {
		mapPresenter.getViewPort().applyBounds(event.getBbox());
		geocoderTextBox.setValue(event.getCanonicalLocation());
	}

	/**
	 * Fire an event.
	 *
	 * @param event event to fire
	 */
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
}