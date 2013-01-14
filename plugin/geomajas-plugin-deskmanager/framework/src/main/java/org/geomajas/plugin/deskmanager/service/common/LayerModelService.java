/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.common;

import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.security.GeomajasSecurityException;
import org.hibernate.HibernateException;

/**
 * Service that allows access to layermodels. The layermodels are mostly used in the management interface.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * 
 */
public interface LayerModelService {

	/**
	 * Save or update a layermodel. Security is applied, if the current user has no access to the layermodel, an
	 * exception is thrown.
	 * 
	 * @param lm
	 *            the layermodel to save
	 * @throws GeomajasSecurityException
	 */
	void saveOrUpdateLayerModel(LayerModel lm) throws GeomajasSecurityException;

	/**
	 * Delete a layermodel. Security is applied, if the current user has no access to the layermodel, an exception is
	 * thrown.
	 * 
	 * @param lm
	 * @throws GeomajasSecurityException
	 * @throws HibernateException
	 */
	void deleteLayerModel(LayerModel lm) throws GeomajasSecurityException, HibernateException;

	/**
	 * Retrieve a layermodel for a given layer model id. Security is applied, if the current user has no access to the
	 * layermodel, a security exception is thrown.
	 * 
	 * @param id
	 *            the id of the layermodel to fetch
	 * @return the layermodel
	 * @throws GeomajasSecurityException
	 */
	LayerModel getLayerModelById(String id) throws GeomajasSecurityException;

	/**
	 * Retrieve a layermodel for a given client layer id. Security is applied, if the current user has no access to the
	 * layermodel, a security exception is thrown.
	 * 
	 * @param id
	 *            the client layer id of the layermodel to fetch
	 * @return the layermodel
	 * @throws GeomajasSecurityException
	 */
	LayerModel getLayerModelByClientLayerId(String id) throws GeomajasSecurityException;

	/**
	 * Checks if the layermodel is in use by any Blueprint and/or Geodesk. If the current user has nog access to the
	 * layermode, a security exception is thrown.
	 * 
	 * @param clientLayerId
	 *            the client layer id of the layermodel.
	 * @return true if ths layermodel is in use.
	 * @throws GeomajasSecurityException
	 */
	boolean isLayerModelInUse(String clientLayerId) throws GeomajasSecurityException;

	/**
	 * Retrieve a list of layermodels where the current user has access to, these are filtered by security, only
	 * systemadmin will see system layers.
	 * 
	 * @return the list of layermodels.
	 * @throws GeomajasSecurityException
	 */
	List<LayerModel> getLayerModels() throws GeomajasSecurityException;

	/**
	 * Extract extra info from layer if available, otherwise returns defaults. The extra info contains deskmanager
	 * specific infomation such as active, public, etc.
	 * 
	 * @param the
	 *            client layer info
	 * @return the extra info.
	 */
	org.geomajas.plugin.deskmanager.configuration.client.DeskmanagerClientLayerInfoI getExtraInfo(ClientLayerInfo cli);

	/**
	 * Retrieve a list of all layer models. <br />
	 * <b>Security is not applied on this method!</b>
	 * 
	 * @return the list of layer models.
	 */
	List<LayerModel> getLayerModelsInternal();

	/**
	 * Save or update a layer model. <br />
	 * <b>Security is not applied on this method!</b>
	 * 
	 * @param lm
	 *            the layer model to save.
	 */
	void saveOrUpdateLayerModelInternal(LayerModel lm);

	/**
	 * Retrieve a list of all layer models that where dynamically added. <br />
	 * <b>Security is not applied on this method!</b>
	 * 
	 * @return the list of layer models.
	 */
	List<LayerModel> getDynamicLayerModelsInternal();

	/**
	 * Retrieve a layermodel from a given client layer id. <br />
	 * <b>Security is not applied on this method!</b>
	 * 
	 * @param id
	 * @return
	 */
	LayerModel getLayerModelByClientLayerIdInternal(String id);
}
