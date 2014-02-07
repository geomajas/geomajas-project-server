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
package org.geomajas.plugin.deskmanager.service.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kristof Heirwegh
 */
@Repository
@Transactional(rollbackFor = { Exception.class })
public class BlueprintServiceImpl implements BlueprintService {

	@Autowired
	private SessionFactory factory;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private LayerModelService layerModelService;

	@Autowired(required = false)
	private List<UserApplicationInfo> userApplications = new ArrayList<UserApplicationInfo>();

	public Blueprint getBlueprintById(String uuid) throws GeomajasSecurityException {
		Blueprint bp = getBlueprintByIdInternal(uuid);
		if (bp != null) {
			if (((DeskmanagerSecurityContext) securityContext).readAllowed(bp)) {
				return bp;
			} else {
				throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Read blueprint",
						securityContext.getUserName());
			}
		} else {
			return null;
		}
	}

	public Blueprint getBlueprintByIdInternal(String uuid) {
		Blueprint bluePrint = (Blueprint) factory.getCurrentSession().get(Blueprint.class, uuid);
		// updateBluePrintFromUserApplication(bluePrint);
		return bluePrint;
	}

	@SuppressWarnings("unchecked")
	public List<Blueprint> getBlueprints() throws GeomajasSecurityException {
		Criteria crit = factory.getCurrentSession().createCriteria(Blueprint.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		crit.add(Restrictions.eq("deleted", false));
		((DeskmanagerSecurityContext) securityContext).getFilterBlueprints();
		Criterion filter = ((DeskmanagerSecurityContext) securityContext).getFilterBlueprints();
		if (filter != null) {
			crit.createAlias("groups", "groups");
			crit.add(filter);
		}
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Blueprint> getBlueprintsInternal() {
		Criteria crit = factory.getCurrentSession().createCriteria(Blueprint.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		crit.add(Restrictions.eq("deleted", false));
		return crit.list();
	}

	public void deleteBlueprint(Blueprint bp) throws GeomajasSecurityException {
		if (((DeskmanagerSecurityContext) securityContext).deleteAllowed(bp)) {
			bp.setDeleted(true);
			factory.getCurrentSession().saveOrUpdate(bp);
		} else {
			throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Delete blueprint",
					securityContext.getUserName());
		}
	}

	public void saveOrUpdateBlueprint(Blueprint bp) throws GeomajasSecurityException {
		if (((DeskmanagerSecurityContext) securityContext).saveAllowed(bp)) {
			saveOrUpdateBlueprintInternal(bp);
		} else {
			throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Save blueprint",
					securityContext.getUserName());
		}
	}

	public void saveOrUpdateBlueprintInternal(Blueprint bp) {
		Date date = new Date();
		String fullName = ((DeskmanagerSecurityContext) securityContext).getFullName();
		if (bp.getCreationBy() == null) {
			bp.setCreationBy(fullName);
		}
		bp.setLastEditBy(fullName);
		if (bp.getCreationDate() == null) {
			bp.setCreationDate(date);
		}
		bp.setLastEditDate(date);
		updateBluePrintFromUserApplication(bp);
		factory.getCurrentSession().saveOrUpdate(bp);
	}

	/**
	 * Insert configuration from user application into blueprint, if blueprint does not exist.
	 */
	@Transactional(rollbackFor = { Exception.class })
	public void updateBluePrintFromUserApplication(Blueprint bluePrint) {
		if (bluePrint.getMainMapLayers() == null || bluePrint.getMainMapLayers().isEmpty()) {
			updateBluePrintMainMapFromUserApplication(bluePrint);
		}
		if (bluePrint.getOverviewMapLayers() == null || bluePrint.getOverviewMapLayers().isEmpty()) {
			updateBluePrintOverviewMapFromUserApplication(bluePrint);
		}
		factory.getCurrentSession().saveOrUpdate(bluePrint);
	}

	/**
	 * Updates blueprint that has no layers set so that the layers configured in the userApplication are inserted into
	 * the database.
	 * 
	 */
	@Transactional(rollbackFor = { Exception.class })
	private void updateBluePrintMainMapFromUserApplication(Blueprint bp) {
		UserApplicationInfo uai = getUserApplication(bp.getUserApplicationKey());
		if (uai != null) {
			ClientMapInfo mainMap = null;
			for (ClientMapInfo map : uai.getApplicationInfo().getMaps()) {
				if (GdmLayout.MAPMAIN_ID.equals(map.getId())) {
					mainMap = map;
				}
			}
			Map<String, LayerModel> layerModels = new HashMap<String, LayerModel>();
			for (LayerModel lm : layerModelService.getLayerModelsInternal()) {
				layerModels.put(lm.getClientLayerId(), lm);
			}

			for (ClientLayerInfo clientLayer : mainMap.getLayers()) {
				org.geomajas.plugin.deskmanager.domain.ClientLayer layer = 
					new org.geomajas.plugin.deskmanager.domain.ClientLayer();
				layer.setLayerModel(layerModels.get(clientLayer.getId()));
				bp.getMainMapLayers().add(layer);
			}
		}
	}

	/**
	 * Updates blueprint that has no layers set so that the layers configured in the userApplication are inserted into
	 * the database.
	 * 
	 */
	@Transactional(rollbackFor = { Exception.class })
	private void updateBluePrintOverviewMapFromUserApplication(Blueprint bp) {
		UserApplicationInfo uai = getUserApplication(bp.getUserApplicationKey());
		if (uai != null) {
			ClientMapInfo overviewMap = null;
			for (ClientMapInfo map : uai.getApplicationInfo().getMaps()) {
				if (GdmLayout.MAPOVERVIEW_ID.equals(map.getId())) {
					overviewMap = map;
				}
			}
			Map<String, LayerModel> models = new HashMap<String, LayerModel>();
			for (LayerModel model : layerModelService.getLayerModelsInternal()) {
				models.put(model.getClientLayerId(), model);
			}
			for (ClientLayerInfo clientLayer : overviewMap.getLayers()) {
				org.geomajas.plugin.deskmanager.domain.ClientLayer layer = 
					new org.geomajas.plugin.deskmanager.domain.ClientLayer();
				LayerModel model = models.get(clientLayer.getId());
				layer.setLayerModel(model);
				bp.getOverviewMapLayers().add(layer);
			}
		}
	}

	private UserApplicationInfo getUserApplication(String key) {
		if (key == null) {
			return null;
		}
		for (UserApplicationInfo uai : userApplications) {
			if (key.equals(uai.getKey())) {
				return uai;
			}
		}
		return null;
	}

}
