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

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

/**
 * Presenter for SldEditorWindow.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorExpertPresenter extends
		Presenter<SldEditorExpertPresenter.MyView, SldEditorExpertPresenter.MyProxy> {

	public static final String NAMETOKEN = "sld-editor-expert";
	
	/**
	 * {@link SldEditorExpertPresenter}'s view.
	 */
	public interface MyView extends View { }

	/**
	 * {@link SldEditorExpertPresenter}'s proxy.
	 */
	@ProxyStandard
	@NameToken(NAMETOKEN)
	public interface MyProxy extends ProxyPlace<SldEditorExpertPresenter> {	}

	@Inject
	public SldEditorExpertPresenter(EventBus eventBus, MyView view,	MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}
}
