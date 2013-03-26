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
package org.geomajas.sld.editor.expert.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geomajas.sld.editor.common.client.model.BasicErrorHandler;
import org.geomajas.sld.editor.common.client.model.event.SldAddedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldAddedEvent.SldAddedHandler;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent.SldChangedHandler;
import org.geomajas.sld.editor.common.client.model.event.SldLoadedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldLoadedEvent.SldLoadedHandler;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.editor.common.client.presenter.event.SldEditSessionClosedEvent;
import org.geomajas.sld.editor.common.client.view.ViewUtil;
import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent.TemplateSelectHandler;

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

	private List<SldInfo> templateNames = new ArrayList<SldInfo>();
	private RawSld template;
	
	private final EventBus eventBus;
	private ViewUtil viewUtil;

	private final Logger logger = Logger.getLogger(SldManagerImpl.class.getName());

	private SldModel currentSld;

	@Inject
	public SldManagerImpl(EventBus eventBus, final ViewUtil viewUtil) {
		this.eventBus = eventBus;
		this.viewUtil = viewUtil;
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + "d/sld");
		// this.eventBus.addHandler(SldContentChangedEvent.getType(), new ContentChangedHandler());
		this.eventBus.addHandler(TemplateSelectEvent.getType(), new TemplateSelectedHandler());
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public void fetchTemplateNames() {
		templateNames.clear();
		service.findTemplates(new AsyncCallback<List<SldInfo>>() {
			public void onSuccess(List<SldInfo> result) {
				for (SldInfo template : result) {
					templateNames.add(template);
				}
				TemplateNamesLoadedEvent.fire(SldManagerImpl.this);
			}
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "fetchTemplateNames failed", caught);
			}
		});
	}

	public List<SldInfo> getTemplateNames() {
		return templateNames;
	}

	public void fetchTemplate(String name) {
		template = null;
		service.findTemplateByName(name, new AsyncCallback<RawSld>() {
			public void onSuccess(RawSld result) {
				template = result;
				TemplateLoadedEvent.fire(SldManagerImpl.this);
			}
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "findTemplateByName failed", caught);
			}
		});
	}

	public RawSld getTemplate() {
		return template;
	}
	
	// ---------------------------------------------------------------

//	/**
//	 * {@link SldContentChangedHandler} that sets the model to dirty and propagates the dirty state to
//	 * {@link SldChangedEvent} listeners.
//	 * 
//	 * @author Jan De Moerloose
//	 */
//	public class ContentChangedHandler implements SldContentChangedHandler {
//		public void onChanged(SldContentChangedEvent event) {
//			if (null != getCurrentSld()) {
//				getCurrentSld().setDirty(true);
//				SldChangedEvent.fire(SldManagerImpl.this);
//			}
//		}
//	}
	
	public class TemplateSelectedHandler implements TemplateSelectHandler {
		public void onTemplateSelect(TemplateSelectEvent event) {
			fetchTemplate(event.getTemplateName());
		}
	}
	
	// ---------------------------------------------------------------
	
	public void saveCurrent() {
		saveCurrent(null);
	}
	
	
	private void saveCurrent(final BasicErrorHandler errorHandler) {
//		if (currentSld != null) {
//			if (currentSld.isComplete()) {
//				currentSld.synchronize();
//				service.saveOrUpdate(currentSld.getSld(), new AsyncCallback<StyledLayerDescriptorInfo>() {
//
//					public void onSuccess(StyledLayerDescriptorInfo sld) {
//						if (null != sld) {
//							currentSld.refresh(modelFactory.create(sld));
//							logger.info("SldManager: SLD was successfully saved");
//							SldChangedEvent.fire(SldManagerImpl.this);
//						}
//					}
//
//					public void onFailure(Throwable caught) {
//						if (null != errorHandler) {
//							errorHandler.onFailure(caught);
//						}
//
//					}
//				});
//			}
//		}
	}
	

	public void add(SldModel sld, final BasicErrorHandler errorHandler) {
//		sld.synchronize();
//		service.create(sld.getSld(), new AsyncCallback<StyledLayerDescriptorInfo>() {
//
//			/** call-back for handling saveOrUpdate() success return **/
//
//			public void onSuccess(StyledLayerDescriptorInfo sld) {
//				SldModel sldModel = null;
//				if (null != sld) {
//					sldModel = modelFactory.create(sld);
//					currentList.add(sldModel);
//					logger.info("SldManager: new SLD was successfully created. Execute selectSld()");
//					// selectSld(sld.getName(), false);
//				}
//				SldAddedEvent.fire(SldManagerImpl.this, null == sldModel ? null : sldModel.getName());
//			}
//
//			public void onFailure(Throwable caught) {
//				if (null != errorHandler) {
//					errorHandler.onFailure(caught);
//				} else {
//					viewUtil.showWarning("De SLD met standaard inhoud kon niet gecre&euml;erd worden. (Interne fout: "
//							 + caught.getMessage() + ")");
//				}
//			}
//		});
	}

	public SldModel create(String sldName) {
		SldModel m = new SldModelImpl();
		m.setRawSld(new RawSld());
		m.getRawSld().setName(sldName);
		m.getRawSld().setTitle(sldName);
		m.getRawSld().setVersion("1.0.0");
		return m;
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
		return false;
//		final SldModel selectedSld = findByName(name);
//		if (selectedSld != null) {
//			service.findByName(selectedSld.getName(), new AsyncCallback<StyledLayerDescriptorInfo>() {
//
//				public void onSuccess(StyledLayerDescriptorInfo result) {
//					currentSld = selectedSld;
//					currentSld.refresh(modelFactory.create(result));
//					SldSelectedEvent.fire(SldManagerImpl.this, currentSld);
//				}
//
//				public void onFailure(Throwable caught) {
//					if (null != errorHandler) {
//						errorHandler.onFailure(caught);
//					}
//					SldEditSessionClosedEvent.fire(SldManagerImpl.this, name);
//
//				}
//
//			});
//			return true;
//		} else {
//			return false;
//		}
	}

	public SldModel getCurrentSld() {
		return currentSld;
	}

	public void removeCurrent() {
//		if (currentSld != null) {
//			service.remove(currentSld.getName(), new AsyncCallback<Boolean>() {
//
//				/** call-back for handling saveOrUpdate() success return **/
//
//				public void onSuccess(Boolean sld) {
//					if (sld) {
//						currentList.remove(currentSld);
//						logger.info("SldManager: new SLD was successfully created. Execute selectSld()");
//					}
//					SldRemovedEvent.fire(SldManagerImpl.this);
//				}
//
//				public void onFailure(Throwable caught) {
//					// SC.warn("De SLD met standaard inhoud kon niet gecre&euml;erd worden. (Interne fout: "
//					// + caught.getMessage() + ")");
//					//
//					// winModal.destroy();
//
//				}
//			});
//		}
	}

	public void refreshCurrent() {
		select(currentSld.getName());
	}

	public HandlerRegistration addSldChangedHandler(SldChangedHandler handler) {
		return eventBus.addHandler(SldChangedEvent.getType(), handler);
	}


//	/**
//	 * One-time {@link SldChangedHandler} that deselects the current SLD after it has been saved.
//	 * 
//	 * @author Jan De Moerloose
//	 * 
//	 */
//	class OneTimeDeselector implements SldChangedHandler {
//
//		private HandlerRegistration registration;
//
//		public OneTimeDeselector() {
//			registration = addSldChangedHandler(this);
//		}
//
//		public void onChanged(SldChangedEvent event) {
//			deselectAll();
//			if (registration != null) {
//				registration.removeHandler();
//			}
//		}
//	}
//
//	/**
//	 * One-time {@link SldChangedHandler} that selects a new SLD after the current has been saved.
//	 * 
//	 * @author Jan De Moerloose
//	 * 
//	 */
//	class OneTimeSelector implements SldChangedHandler {
//
//		private HandlerRegistration registration;
//		
//		private String name;
//
//		public OneTimeSelector(String name) {
//			this.name = name;
//			registration = addSldChangedHandler(this);
//		}
//
//		public void onChanged(SldChangedEvent event) {
//			select(name);
//			if (registration != null) {
//				registration.removeHandler();
//			}
//		}
//	}
	

}
