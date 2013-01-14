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

package org.geomajas.sld.editor.common.client.gin;

import org.geomajas.sld.editor.common.client.presenter.StyledLayerDescriptorListPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.inject.Provider;

/**
 * Base {@link Ginjector} for the SLD editor modules. Gives access to the common part and should be extended in the
 * specific modules for SmartGWT and PureGwt.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 * 
 */
@GinModules({ ClientModule.class })
public interface ClientGinjectorBase extends ClientNoSldListGinjectorBase {

	// Extra w.r.t. ClientNoSldListGinjectorBase
	Provider<StyledLayerDescriptorListPresenter> getStyledLayerDescriptorListPresenter();
		
}