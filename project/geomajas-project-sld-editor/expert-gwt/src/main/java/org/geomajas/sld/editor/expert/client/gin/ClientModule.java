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

import org.geomajas.sld.editor.expert.client.SldEditorPlaceManager;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;
import org.geomajas.sld.editor.expert.client.presenter.SmartGwtRootPresenter;
import org.geomajas.sld.editor.expert.client.view.SldEditorView;

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

		bindPresenter(SldEditorExpertPresenter.class,
				SldEditorExpertPresenter.MyView.class, SldEditorView.class,
				SldEditorExpertPresenter.MyProxy.class);

		// bindPresenter(StyledLayerDescriptorListPresenter.class,
		// StyledLayerDescriptorListPresenter.MyView.class,
		// StyledLayerDescriptorListView.class,
		// StyledLayerDescriptorListPresenter.MyProxy.class);
		//
		// bindSingletonPresenterWidget(CreateSldDialogPresenterWidget.class,
		// CreateSldDialogPresenterWidget.MyView.class,
		// CreateSldDialogView.class); // Used by
		// StyledLayerDescriptorListPresenter

	}
}