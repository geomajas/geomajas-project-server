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

import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo.ChoiceInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.client.model.event.SldAddedEvent;
import org.geomajas.sld.client.model.event.SldAddedEvent.SldAddedHandler;
import org.geomajas.sld.client.model.event.SldLoadedEvent;
import org.geomajas.sld.client.model.event.SldLoadedEvent.SldLoadedHandler;
import org.geomajas.sld.editor.client.GeometryType;
import org.geomajas.sld.editor.client.SldUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

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
				SldLoadedEvent.fire(SldManagerImpl.this);
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
	
	public void add(StyledLayerDescriptorInfo sld) {
		service.create(sld, new AsyncCallback<StyledLayerDescriptorInfo>() {

			/** call-back for handling saveOrUpdate() success return **/

			public void onSuccess(StyledLayerDescriptorInfo sld) {
				if (null != sld) {
					currentList.add(sld);
					logger.info("SldManager: new SLD was successfully created. Execute selectSld()");
					//selectSld(sld.getName(), false);
				}
				SldAddedEvent.fire(SldManagerImpl.this);
			}

			public void onFailure(Throwable caught) {
//				SC.warn("De SLD met standaard inhoud kon niet gecre&euml;erd worden. (Interne fout: "
//						+ caught.getMessage() + ")");
//
//				winModal.destroy();

			}
		});
	}

	public StyledLayerDescriptorInfo create(GeometryType geomType) {
		StyledLayerDescriptorInfo sld = new StyledLayerDescriptorInfo();
		sld.setName("NewSLD");
		sld.setVersion("1.0.0");

		List<ChoiceInfo> choiceList = new ArrayList<ChoiceInfo>();
		sld.setChoiceList(choiceList);
		choiceList.add(new ChoiceInfo());

		NamedLayerInfo namedLayerInfo = new NamedLayerInfo();
		List<NamedLayerInfo.ChoiceInfo> namedlayerChoicelist = new ArrayList<NamedLayerInfo.ChoiceInfo>();
		namedLayerInfo.setChoiceList(namedlayerChoicelist);
		namedlayerChoicelist.add(new NamedLayerInfo.ChoiceInfo());
		namedlayerChoicelist.get(0).setUserStyle(new UserStyleInfo());
		choiceList.get(0).setNamedLayer(namedLayerInfo);

		FeatureTypeStyleInfo featureTypeStyle = new FeatureTypeStyleInfo();
		List<RuleInfo> ruleList = new ArrayList<RuleInfo>();
		RuleInfo defaultRule = SldUtils.createDefaultRule(geomType);
		ruleList.add(defaultRule);
		featureTypeStyle.setRuleList(ruleList);

		List<FeatureTypeStyleInfo> featureTypeStyleList = new ArrayList<FeatureTypeStyleInfo>();
		featureTypeStyleList.add(featureTypeStyle);

		namedlayerChoicelist.get(0).getUserStyle().setFeatureTypeStyleList(featureTypeStyleList);
		return sld;
	}

	public HandlerRegistration addSldLoadedHandler(SldLoadedHandler handler) {
		return eventBus.addHandler(SldLoadedEvent.getType(), handler);
	}

	public HandlerRegistration addSldAddedHandler(SldAddedHandler handler) {
		return eventBus.addHandler(SldAddedEvent.getType(), handler);
	}

}
