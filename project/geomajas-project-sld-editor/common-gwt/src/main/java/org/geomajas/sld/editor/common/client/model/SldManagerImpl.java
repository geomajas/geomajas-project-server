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
package org.geomajas.sld.editor.common.client.model;

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
import org.geomajas.sld.editor.common.client.GeometryType;
import org.geomajas.sld.editor.common.client.SldUtils;
import org.geomajas.sld.editor.common.client.model.event.SldAddedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldAddedEvent.SldAddedHandler;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent.HasSldChangedHandlers;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent.SldChangedHandler;
import org.geomajas.sld.editor.common.client.model.event.SldLoadedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldLoadedEvent.SldLoadedHandler;
import org.geomajas.sld.editor.common.client.model.event.SldRemovedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent.SldContentChangedHandler;
import org.geomajas.sld.editor.common.client.presenter.event.SldEditSessionClosedEvent;
import org.geomajas.sld.editor.common.client.view.ViewUtil;

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
public class SldManagerImpl implements SldManager, HasSldChangedHandlers {

	private final SldGwtServiceAsync service = GWT.create(SldGwtService.class);

	private List<SldModel> currentList = new ArrayList<SldModel>();

	private final EventBus eventBus;
	private ViewUtil viewUtil;

	private final Logger logger = Logger.getLogger(SldManagerImpl.class.getName());

	private SldModel currentSld;

	private final SldModelFactory modelFactory;


	@Inject
	public SldManagerImpl(EventBus eventBus, final ViewUtil viewUtil, SldModelFactory modelFactory) {
		this.eventBus = eventBus;
		this.viewUtil = viewUtil;
		this.modelFactory = modelFactory;
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + "d/sld");
		this.eventBus.addHandler(SldContentChangedEvent.getType(), new ContentChangedHandler());
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public void fetchAll() {
		currentList.clear();
		service.findAll(new AsyncCallback<List<String>>() {

			public void onSuccess(List<String> result) {
				for (String name : result) {
					// create an empty one for now
					StyledLayerDescriptorInfo descriptorInfo = new StyledLayerDescriptorInfo();
					descriptorInfo.setName(name);
					// create an empty model
					currentList.add(modelFactory.create(descriptorInfo));
				}
				SldLoadedEvent.fire(SldManagerImpl.this);
			}

			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "listAll failed", caught);
			}
		});

	}

	public void saveCurrent() {
		saveCurrent(null);
		
	}
	
	private void saveCurrent(final BasicErrorHandler errorHandler) {
		if (currentSld != null) {
			if (currentSld.isComplete()) {
				currentSld.synchronize();
				service.saveOrUpdate(currentSld.getSld(), new AsyncCallback<StyledLayerDescriptorInfo>() {

					public void onSuccess(StyledLayerDescriptorInfo sld) {
						if (null != sld) {
							currentSld.refresh(modelFactory.create(sld));
							logger.info("SldManager: SLD was successfully saved");
							SldChangedEvent.fire(SldManagerImpl.this);
						}
					}

					public void onFailure(Throwable caught) {
						if (null != errorHandler) {
							errorHandler.onFailure(caught);
						}

					}
				});
			}
		}
	}
	

	public List<String> getCurrentNames() {
		List<String> names = new ArrayList<String>();
		for (SldModel model : currentList) {
			names.add(model.getName());
		}
		return names;
	}

	public void add(SldModel sld, final BasicErrorHandler errorHandler) {
		sld.synchronize();
		service.create(sld.getSld(), new AsyncCallback<StyledLayerDescriptorInfo>() {

			/** call-back for handling saveOrUpdate() success return **/

			public void onSuccess(StyledLayerDescriptorInfo sld) {
				SldModel sldModel = null;
				if (null != sld) {
					sldModel = modelFactory.create(sld);
					currentList.add(sldModel);
					logger.info("SldManager: new SLD was successfully created. Execute selectSld()");
					// selectSld(sld.getName(), false);
				}
				SldAddedEvent.fire(SldManagerImpl.this, null == sldModel ? null : sldModel.getName());
			}

			public void onFailure(Throwable caught) {
				if (null != errorHandler) {
					errorHandler.onFailure(caught);
				} else {
					viewUtil.showWarning("De SLD met standaard inhoud kon niet gecre&euml;erd worden. (Interne fout: "
							 + caught.getMessage() + ")");
				}
			}
		});
	}

	public SldModel create(GeometryType geomType, String sldName) {
		StyledLayerDescriptorInfo sld = new StyledLayerDescriptorInfo();
		sld.setName(sldName);
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
		namedlayerChoicelist.get(0).getUserStyle().setTitle(sldName);
		return modelFactory.create(sld);
	}

	public HandlerRegistration addSldLoadedHandler(SldLoadedHandler handler) {
		return eventBus.addHandler(SldLoadedEvent.getType(), handler);
	}

	public HandlerRegistration addSldAddedHandler(SldAddedHandler handler) {
		return eventBus.addHandler(SldAddedEvent.getType(), handler);
	}

	public HandlerRegistration addSldSelectedHandler(SldSelectedHandler handler) {
		return eventBus.addHandler(SldSelectedEvent.getType(), handler);
	}

	public boolean select(final String name) {
		return select(name, new BasicErrorHandler() {
			public void onFailure(Throwable caught) {
				SldEditSessionClosedEvent.fire(SldManagerImpl.this, name);
			}
		});
	}
	

	/* (non-Javadoc)
	 * @see org.geomajas.sld.editor.common.client.model.SldManager#select(java.lang.String, 
	 * 	org.geomajas.sld.editor.common.client.model.BasicErrorHandler)
	 * 
	 */
	public boolean select(final String name, final BasicErrorHandler errorHandler) {

		final SldModel selectedSld = findByName(name);
		if (selectedSld != null) {
			service.findByName(selectedSld.getName(), new AsyncCallback<StyledLayerDescriptorInfo>() {

				public void onSuccess(StyledLayerDescriptorInfo result) {
					currentSld = selectedSld;
					currentSld.refresh(modelFactory.create(result));
					SldSelectedEvent.fire(SldManagerImpl.this, currentSld);
				}

				public void onFailure(Throwable caught) {
					if (null != errorHandler) {
						errorHandler.onFailure(caught);
					}
					SldEditSessionClosedEvent.fire(SldManagerImpl.this, name);

				}

			});
			return true;
		} else {
			return false;
		}
	}

	public SldModel getCurrentSld() {
		return currentSld;
	}

	private SldModel findByName(String name) {
		for (SldModel sld : currentList) {
			if (sld.getName().equalsIgnoreCase(name)) {
				return sld;
			}
		}
		return null;
	}

	public void removeCurrent() {
		if (currentSld != null) {
			service.remove(currentSld.getName(), new AsyncCallback<Boolean>() {

				/** call-back for handling saveOrUpdate() success return **/

				public void onSuccess(Boolean sld) {
					if (sld) {
						currentList.remove(currentSld);
						logger.info("SldManager: new SLD was successfully created. Execute selectSld()");
					}
					SldRemovedEvent.fire(SldManagerImpl.this);
				}

				public void onFailure(Throwable caught) {
					// SC.warn("De SLD met standaard inhoud kon niet gecre&euml;erd worden. (Interne fout: "
					// + caught.getMessage() + ")");
					//
					// winModal.destroy();

				}
			});
		}
	}

	public void refreshCurrent() {
		select(currentSld.getName());
	}

	public HandlerRegistration addSldChangedHandler(SldChangedHandler handler) {
		return eventBus.addHandler(SldChangedEvent.getType(), handler);
	}

	/**
	 * {@link SldContentChangedHandler} that sets the model to dirty and propagates the dirty state to
	 * {@link SldChangedEvent} listeners.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class ContentChangedHandler implements SldContentChangedHandler {

		public void onChanged(SldContentChangedEvent event) {
			if (null != getCurrentSld()) {
				getCurrentSld().setDirty(true);
				SldChangedEvent.fire(SldManagerImpl.this);
			}
		}

	}

	public void deselectAll() {
		String sldName = null == currentSld ? null : currentSld.getName();
		currentSld = null;
		SldSelectedEvent.fire(SldManagerImpl.this, null);
		SldEditSessionClosedEvent.fire(SldManagerImpl.this, sldName);
	}

	public void saveAndDeselectAll() {
		new OneTimeDeselector(); // Will fire SldEditSessionClosedEvent when current SLD has been successfully 
								 // saved.
		saveCurrent(new BasicErrorHandler() {
				public void onFailure(Throwable caught) {
					SldEditSessionClosedEvent.fire(SldManagerImpl.this, (String) null);
				}
			});
	}

	public void saveAndSelect(String name) {
		new OneTimeSelector(name);
		saveCurrent(null);
	}
	
	
	/**
	 * One-time {@link SldChangedHandler} that deselects the current SLD after it has been saved.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class OneTimeDeselector implements SldChangedHandler {

		private HandlerRegistration registration;

		public OneTimeDeselector() {
			registration = addSldChangedHandler(this);
		}

		public void onChanged(SldChangedEvent event) {
			deselectAll();
			if (registration != null) {
				registration.removeHandler();
			}
		}
	}

	/**
	 * One-time {@link SldChangedHandler} that selects a new SLD after the current has been saved.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class OneTimeSelector implements SldChangedHandler {

		private HandlerRegistration registration;
		
		private String name;

		public OneTimeSelector(String name) {
			this.name = name;
			registration = addSldChangedHandler(this);
		}

		public void onChanged(SldChangedEvent event) {
			select(name);
			if (registration != null) {
				registration.removeHandler();
			}
		}
	}
	

}
