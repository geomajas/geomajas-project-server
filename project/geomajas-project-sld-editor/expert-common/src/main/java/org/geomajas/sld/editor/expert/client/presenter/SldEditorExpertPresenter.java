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
package org.geomajas.sld.editor.expert.client.presenter;

import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;
import org.geomajas.sld.editor.expert.client.model.SldManager;
import org.geomajas.sld.editor.expert.client.model.SldModel;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelEvent.HasSldCancelHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelEvent.SldCancelHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent.HasSldCancelledHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent.SldCancelledHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCloseEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCloseEvent.HasSldCloseHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCloseEvent.SldCloseHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldLoadedEvent.HasSldLoadedHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.SldSaveEvent.HasSldSaveHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.SldSaveEvent.SldSaveHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidateEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidateEvent.HasSldValidateHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidateEvent.SldValidateHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidatedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidatedEvent.HasSldValidatedHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidatedEvent.SldValidatedHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent.HasTemplateLoadedHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent.TemplateLoadedHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent.HasTemplateNamesLoadedHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent.TemplateNamesLoadedHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent.HasTemplateSelectHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent.TemplateSelectHandler;
import org.geomajas.sld.editor.expert.client.view.ViewUtil;
import org.geomajas.sld.editor.expert.client.view.ViewUtil.YesNoCallback;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

/**
 * Presenter for Expert SldEditorWindow.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorExpertPresenter extends
		Presenter<SldEditorExpertPresenter.MyView, SldEditorExpertPresenter.MyProxy> {

	public static final String NAMETOKEN = "sld-editor-expert";

	private static final SldEditorExpertMessages EXP_MSG = GWT.create(SldEditorExpertMessages.class);

	private final SldManager manager;
	private final ViewUtil viewUtil;

	/**
	 * {@link SldEditorExpertPresenter}'s view.
	 */
	public interface MyView extends PopupView, HasTemplateSelectHandlers, HasTemplateNamesLoadedHandlers,
			HasTemplateLoadedHandlers, HasSldLoadedHandlers, HasSldCloseHandlers, HasSldSaveHandlers,
			HasSldValidateHandlers, HasSldValidatedHandlers, HasSldCancelHandlers, HasSldCancelledHandlers {

		void setTemplates(SldModel model);

		void selectTemplateCancelled();

		void clearData();

		void modelToView(SldModel model, boolean keepDirty);

		void viewToModel(SldModel model);

		void hide();
	}

	/**
	 * {@link SldEditorExpertPresenter}'s proxy.
	 */
	@ProxyStandard
	@NameToken(NAMETOKEN)
	public interface MyProxy extends ProxyPlace<SldEditorExpertPresenter> {
	}

	@Inject
	public SldEditorExpertPresenter(EventBus eventBus, MyView view, MyProxy proxy, final SldManager manager,
			final ViewUtil viewUtil) {
		super(eventBus, view, proxy);
		this.manager = manager;
		this.viewUtil = viewUtil;
	}

	/**
	 * Add your own custom SaveEventListener.
	 * <p>
	 * Save does not automatically close the editing window. <br/ >
	 * Call presenter.closeEditor() to close the editor afterwards.
	 * 
	 * @param handler
	 */
	public HandlerRegistration addSldSaveHandler(SldSaveHandler handler) {
		return getView().addSldSaveHandler(handler);
	}

	/**
	 * Add your own custom CancelledEventListener.
	 * 
	 * @param handler
	 */
	public HandlerRegistration addSldCancelledHandler(SldCancelledHandler handler) {
		return getView().addSldCancelledHandler(handler);
	}

	/**
	 * Send a close editor event.
	 * <p>
	 * (please note that dirty is not checked, eg. widget is unconditionally closed. Use cancel to check first)
	 */
	public void closeEditor() {
		SldCloseEvent.fire(this);
	}

	/**
	 * Conveniencemethod to load data into the editor.
	 * 
	 * @param rawXml
	 */
	public void loadSld(final String rawXml, final String name, final String title) {
		SldModel m = manager.getModel();
		m.clear();
		m.setTemplate(null);
		m.setDirty(false);
		m.setValid(false); // might be true, not checking...
		m.getRawSld().setName(name);
		m.getRawSld().setTitle(title);
		m.getRawSld().setXml(rawXml);
		SldLoadedEvent.fire(SldEditorExpertPresenter.this);
	}

	/**
	 * Conveniencemethod to load data into the editor.
	 * <p>
	 * Note that this is an async call, it will take some time before the data appears in the editor.
	 * 
	 * @param sldi
	 */
	public void loadSld(final StyledLayerDescriptorInfo sldi) {
		manager.convertToRawSld(sldi, new AsyncCallback<RawSld>() {
			public void onSuccess(RawSld result) {
				loadSld(result.getXml(), result.getName(), result.getTitle());
			}

			public void onFailure(Throwable caught) {
				loadSld(EXP_MSG.failedToParseSldObject(), sldi.getName(), sldi.getTitle());
			}
		});
	}

	/**
	 * The editors model, get the xml data here.
	 * 
	 * @return
	 */
	public SldModel getModel() {
		return manager.getModel();
	}

	// ---------------------------------------------------------------

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
		// RevealRootPopupContentEvent.fire(this, this); // can't (easily) use SmartGWT.
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		manager.fetchTemplateNames();
	}

	@ProxyEvent
	protected void onSldLoaded(SldLoadedEvent event) {
		getView().modelToView(manager.getModel(), event.isKeepDirty());
	}

	@Override
	protected void onBind() {
		super.onBind();

		registerHandler(getView().addSldValidateHandler(new SldValidateHandler() {
			public void onSldValidate(SldValidateEvent event) {
				getView().viewToModel(manager.getModel());
				manager.validateCurrent(event.isSaveAfterValidate());
			}
		}));

		registerHandler(getView().addSldValidatedHandler(new SldValidatedHandler() {
			public void onSldValidated(SldValidatedEvent event) {
				if (event.isValid()) {
					viewUtil.showMessage(EXP_MSG.validationSucceeded());
				} else {
					viewUtil.showWarning(EXP_MSG.validationFailed());
				}
			}
		}));

		registerHandler(getView().addSldCancelHandler(new SldCancelHandler() {
			public void onSldCancel(SldCancelEvent event) {
				getView().viewToModel(manager.getModel());
				if (manager.getModel().isDirty()) {
					viewUtil.showYesNoMessage(EXP_MSG.confirmLoseDirtyData(), new YesNoCallback() {
						public void onYes() {
							manager.getModel().clear();
							getView().clearData();
							SldCancelledEvent.fire(SldEditorExpertPresenter.this);
						}

						public void onNo() {
						}

						public void onCancel() {
						}
					});
				} else {
					manager.getModel().clear();
					getView().clearData();
					SldCancelledEvent.fire(SldEditorExpertPresenter.this);
				}
			}
		}));

		registerHandler(getView().addSldCloseHandler(new SldCloseHandler() {
			public void onSldClose(SldCloseEvent event) {
				manager.getModel().clear();
				getView().clearData();
				getView().hide();
				// hide by setting another presenter (or null) into the slot
				RevealRootContentEvent.fire(SldEditorExpertPresenter.this, null);
			}
		}));

		registerHandler(getView().addTemplateLoadedHandler(new TemplateLoadedHandler() {
			public void onTemplateLoaded(TemplateLoadedEvent event) {
				// loading a template replaces the data, so throwing a new dataloaded event
				manager.getModel().setRawSld(manager.getModel().getTemplate());
				SldLoadedEvent.fire(SldEditorExpertPresenter.this, new SldLoadedEvent(true));
			}
		}));

		registerHandler(getView().addTemplateNamesLoadedHandler(new TemplateNamesLoadedHandler() {
			public void onTemplateNamesLoaded(TemplateNamesLoadedEvent event) {
				getView().setTemplates(manager.getModel());
			}
		}));

		registerHandler(getView().addTemplateSelectHandler(new TemplateSelectHandler() {
			public void onTemplateSelect(final TemplateSelectEvent event) {
				String content = (manager.getModel().getRawSld() != null ? manager.getModel().getRawSld().getXml()
						: null);
				if (manager.getModel().isDirty() || (content != null && !"".equals(content))) {
					viewUtil.showYesNoMessage(EXP_MSG.confirmLoseDirtyData(), new YesNoCallback() {
						public void onYes() {
							manager.fetchTemplate(event.getTemplateName());
						}

						public void onNo() {
							revert();
						}

						public void onCancel() {
							revert();
						}

						private void revert() {
							getView().selectTemplateCancelled();
						}
					});
				} else {
					manager.fetchTemplate(event.getTemplateName());
				}
			}
		}));
	}
}
