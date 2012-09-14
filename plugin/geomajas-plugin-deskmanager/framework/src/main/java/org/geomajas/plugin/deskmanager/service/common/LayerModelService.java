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
package org.geomajas.plugin.deskmanager.service.common;

import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.plugin.deskmanager.configuration.client.ExtraClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.security.GeomajasSecurityException;
import org.hibernate.HibernateException;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public interface LayerModelService {

	void saveOrUpdateLayerModel(LayerModel lm) throws GeomajasSecurityException;

	void deleteLayerModel(LayerModel lm) throws GeomajasSecurityException, HibernateException;

	LayerModel getLayerModelById(Long id) throws GeomajasSecurityException;

	LayerModel getLayerModelByClientLayerId(String id) throws GeomajasSecurityException;

	boolean isLayerModelInUse(String clientLayerId) throws GeomajasSecurityException;

	/**
	 * These are filtered by security, only systemadmin will see system layers.
	 */
	List<LayerModel> getLayerModels() throws GeomajasSecurityException;

	/**
	 * extract extra info from layer if available, otherwise returns defaults.
	 * 
	 * @param cli
	 * @return (never null)
	 */
	ExtraClientLayerInfo getExtraInfo(ClientLayerInfo cli);

	/**
	 * No security, internal use only!
	 */
	List<LayerModel> getLayerModelsInternal();

	/**
	 * No security, internal use only!
	 */
	void saveOrUpdateLayerModelInternal(LayerModel lm);

	/**
	 * No security, internal use only!
	 */
	List<LayerModel> getDynamicLayerModelsInternal();
	
	/**
	 * No security, internal use only!
	 */
	LayerModel getLayerModelByClientLayerIdInternal(String id);
}
