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
import org.geomajas.sld.editor.common.client.model.SldManager;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;

import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

/**
 * @author Kristof Heirwegh
 * GinModules({ ClientModule.class })
 */
public interface ClientGinjector extends Ginjector {
	
	EventBus getEventBus();
	PlaceManager getPlaceManager();
	SldManager getSldManager();
	SldEditorMessages getSldEditorMessages();
	
	// -- own presenters
	Provider<SldEditorExpertPresenter> getSldEditorExpertPresenter();
	
}