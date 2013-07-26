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

import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;
import org.geomajas.sld.editor.expert.client.model.SldManager;
import org.geomajas.sld.editor.expert.client.model.SldManagerImpl;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorPlaceManager;

import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
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
		bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class).in(Singleton.class);
		
// You will need to add this if you are using MVP
//		bind(RootPresenter.class).asEagerSingleton();

		/**
		 * this makes the sldeditor the default place to open.
		 * <p>Rebind this if you want something else.
		 */
		bind(PlaceManager.class).to(SldEditorPlaceManager.class).in(Singleton.class);

		/**
		 * Contains service calls to parse / validate / load / save SLD data
		 */
		bind(SldManager.class).to(SldManagerImpl.class).in(Singleton.class);

		/**
		 * i18n
		 */
		bind(SldEditorExpertMessages.class).in(Singleton.class);
	}
}