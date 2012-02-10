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

package org.geomajas.sld.client.gin;

import org.geomajas.sld.client.presenter.MainPagePresenter;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorListPresenter;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorPresenter;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

/**
 * Base {@link Ginjector} for the SLD editor modules. Gives access to the common part and should be extended in the
 * specific modules for SmartGWT and PureGwt.
 * 
 * @author Jan De Moerloose
 * 
 */
@GinModules({ ClientModule.class })
public interface ClientGinjectorBase extends Ginjector {

	Provider<MainPagePresenter> getMainPagePresenter();

	AsyncProvider<StyledLayerDescriptorListPresenter> getStyledLayerDescriptorListPresenter();

	AsyncProvider<StyledLayerDescriptorPresenter> getStyledLayerDescriptorPresenter();

	EventBus getEventBus();

	PlaceManager getPlaceManager();
}