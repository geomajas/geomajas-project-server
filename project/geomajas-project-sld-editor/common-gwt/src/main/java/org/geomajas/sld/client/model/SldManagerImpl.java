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
package org.geomajas.sld.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geomajas.sld.StyledLayerDescriptorInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Default implementation of {@link SldManager}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldManagerImpl implements SldManager {

	private final SldGwtServiceAsync service = GWT.create(SldGwtService.class);

	private List<StyledLayerDescriptorInfo> currentList = new ArrayList<StyledLayerDescriptorInfo>();

	private final EventBus eventBus;

	private final Logger logger = Logger.getLogger(SldManagerImpl.class.getName());

	@Inject
	public SldManagerImpl(EventBus eventBus) {
		this.eventBus = eventBus;
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + "d/sld");
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public void fetchAll() {
		currentList.clear();
		service.findAll(new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> result) {
				for (String name : result) {
					StyledLayerDescriptorInfo descriptorInfo = new StyledLayerDescriptorInfo();
					descriptorInfo.setName(name);
					currentList.add(descriptorInfo);
				}
				SldListChangedEvent.fire(SldManagerImpl.this);
			}

			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "listAll failed", caught);
			}
		});

	}

	public List<String> getCurrentNames() {
		List<String> names = new ArrayList<String>();
		for (StyledLayerDescriptorInfo info : currentList) {
			names.add(info.getName());
		}
		return names;
	}

}
