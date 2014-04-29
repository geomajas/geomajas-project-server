/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.security.role.authorization;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.plugin.deskmanager.domain.BaseGeodesk;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.hibernate.criterion.Criterion;

/**
 * Authorization for management interface of the deskmanager plugin. This interface handles the access rights to layer
 * models, blueprints and geodesks in the management interface.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * 
 * @since 1.15.0
 */
@Api(allMethods = true)
@UserImplemented
public interface DeskmanagerManagementAuthorization {

	/**
	 * Whether the user can save a layermodel.
	 * 
	 * @param layerModel
	 *            the layermodel.
	 * @return true if the user may save the layermodel.
	 */
	boolean saveAllowed(LayerModel layerModel);

	/**
	 * Wheter the user can delete a layermodel.
	 * 
	 * @param layerModel
	 *            the layermodel.
	 * @return true if the user may delete the layermodel.
	 */
	boolean deleteAllowed(LayerModel layerModel);

	/**
	 * Wheter the user can read a layermodel.
	 * 
	 * @param layerModel
	 *            the layermodel.
	 * @return true if the user may read the layermodel.
	 */
	boolean readAllowed(LayerModel lm);

	/**
	 * The criterion to filter the layermodels that a user has access to.
	 * 
	 * @return the filter.
	 */
	Criterion getFilterLayerModels();

	/**
	 * Whether a user can read a blueprint.
	 * 
	 * @param blueprint
	 *            the blueprint.
	 * @return true if the user may read the blueprint.
	 */
	boolean readAllowed(BaseGeodesk blueprint);

	/**
	 * Whether a user can save a blueprint.
	 * 
	 * @param blueprint
	 *            the blueprint.
	 * @return true if the user may save the blueprint.
	 */
	boolean saveAllowed(BaseGeodesk blueprint);

	/**
	 * Whether a user can delete a blueprint.
	 * 
	 * @param blueprint
	 *            the blueprint.
	 * @return true if the user may delete the blueprint.
	 */
	boolean deleteAllowed(BaseGeodesk blueprint);

	/**
	 * The criterion to filter the blueprints that a user has access to.
	 * 
	 * @return the filter.
	 */
	Criterion getFilterBlueprints();

	/**
	 * Whether a user can read a geodesk.
	 * 
	 * @param geodesk
	 *            the geodesk.
	 * @return true if the user may read the geodesk.
	 */
	boolean readAllowed(Geodesk geodesk);

	/**
	 * Whether a user can save a geodesk.
	 * 
	 * @param geodesk
	 *            the geodesk.
	 * @return true if the user may save the geodesk.
	 */
	boolean saveAllowed(Geodesk geodesk);

	/**
	 * Whether a user can delete a geodesk.
	 * 
	 * @param geodesk
	 *            the geodesk.
	 * @return true if the user may delete the geodesk.
	 */
	boolean deleteAllowed(Geodesk geodesk);

	/**
	 * The criterion to filter the geodesks that a user has access to.
	 * 
	 * @return the filter.
	 */
	Criterion getFilterGeodesks();

	/**
	 * Whether a user can upload a new shapefile for the given layer.
	 * 
	 * @param clientLayerId
	 *            can be null, denoting a new file/table to be created.
	 * @return true is the user may upload a shapefile.
	 */
	boolean isShapeFileUploadAllowed(String clientLayerId);

}
