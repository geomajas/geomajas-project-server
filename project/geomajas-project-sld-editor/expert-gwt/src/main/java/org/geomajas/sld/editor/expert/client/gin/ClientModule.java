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
package org.geomajas.sld.editor.expert.client.gin;

import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.common.client.view.ViewUtil;
import org.geomajas.sld.editor.expert.client.SldEditorPlaceManager;
import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;
import org.geomajas.sld.editor.expert.client.model.SldManager;
import org.geomajas.sld.editor.expert.client.model.SldManagerImpl;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;
import org.geomajas.sld.editor.expert.client.presenter.SmartGwtRootPresenter;
import org.geomajas.sld.editor.expert.client.view.SldEditorView;
import org.geomajas.sld.editor.expert.client.view.ViewUtilImpl;

import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalyticsImpl;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

/**
 * @author Kristof Heirwegh
 */
public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
		bind(RootPresenter.class).asEagerSingleton();
		bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class).in(Singleton.class);
		bind(PlaceManager.class).to(SldEditorPlaceManager.class).in(Singleton.class); // rebind if needed
		bind(SmartGwtRootPresenter.class).asEagerSingleton();

		bind(SldManager.class).to(SldManagerImpl.class).in(Singleton.class);
		bind(ViewUtil.class).to(ViewUtilImpl.class).in(Singleton.class);

		// i18n
		bind(SldEditorMessages.class).in(Singleton.class);
		bind(SldEditorExpertMessages.class).in(Singleton.class);
		
		bindPresenter(SldEditorExpertPresenter.class,
				SldEditorExpertPresenter.MyView.class, SldEditorView.class,
				SldEditorExpertPresenter.MyProxy.class);
	}
}