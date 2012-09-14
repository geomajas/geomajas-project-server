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
import org.geomajas.global.ExceptionCode;
import org.geomajas.plugin.deskmanager.configuration.client.ExtraClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
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
public class LayerModelServiceImpl implements LayerModelService {

	private static final ExtraClientLayerInfo DEFAULT_ECLI = new ExtraClientLayerInfo();

	@Autowired
	private SessionFactory factory;

	@Autowired
	private SecurityContext securityContext;

	public void saveOrUpdateLayerModel(LayerModel lm) throws GeomajasSecurityException {
		if (((DeskmanagerSecurityContext) securityContext).saveAllowed(lm)) {
			factory.getCurrentSession().saveOrUpdate(lm);
		} else {
			throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Opslaan LaagModel",
					securityContext.getUserName());
		}
	}

	public void deleteLayerModel(LayerModel lm) throws GeomajasSecurityException, HibernateException {
		if (((DeskmanagerSecurityContext) securityContext).deleteAllowed(lm)) {
			if (!isLayerModelInUse(lm.getClientLayerId())) {
				lm.setDeleted(true);
				factory.getCurrentSession().update(lm);
			} else {
				throw new HibernateException("Layermodel is still in use.");
			}
		} else {
			throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Verwijderen LaagModel",
					securityContext.getUserName());
		}
	}

	public LayerModel getLayerModelById(Long id) throws GeomajasSecurityException {
		LayerModel l = (LayerModel) factory.getCurrentSession().get(LayerModel.class, id);
		if (l != null) {
			if (((DeskmanagerSecurityContext) securityContext).readAllowed(l)) {
				return l;
			} else {
				throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Inlezen LaagModel",
						securityContext.getUserName());
			}
		} else {
			return null;
		}
	}

	public LayerModel getLayerModelByClientLayerId(String id) throws GeomajasSecurityException {
		Query q = factory.getCurrentSession().createQuery("FROM LayerModel l WHERE l.clientLayerId = :id");
		q.setParameter("id", id);
		q.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		LayerModel l = (LayerModel) q.uniqueResult();

		if (l != null) {
			if (((DeskmanagerSecurityContext) securityContext).readAllowed(l)) {
				return l;
			} else {
				throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Inlezen LaagModel",
						securityContext.getUserName());
			}
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<LayerModel> getLayerModels() throws GeomajasSecurityException {
		Criteria crit = factory.getCurrentSession().createCriteria(LayerModel.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		crit.add(Restrictions.eq("deleted", false));
		Criterion filter = ((DeskmanagerSecurityContext) securityContext).getFilterLayerModels();
		if (filter != null) {
			crit.add(filter);
		}
		crit.addOrder(Order.asc("name"));
		return crit.list();
	}

	public boolean isLayerModelInUse(String clientLayerId) throws GeomajasSecurityException {
		Query q = factory.getCurrentSession().createQuery(
				"SELECT count(*) FROM LayerTreeNode l WHERE l.clientLayerId = :id");
		q.setParameter("id", clientLayerId);
		Long res = (Long) q.uniqueResult();
		return (res > 0);
	}

	/** {@inheritDoc} */
	public ExtraClientLayerInfo getExtraInfo(ClientLayerInfo cli) {
		if (cli.getUserData() != null && cli.getUserData() instanceof ExtraClientLayerInfo) {
			return (ExtraClientLayerInfo) cli.getUserData();
		} else {
			return DEFAULT_ECLI;
		}
	}

	// -------------------------------------------------

	@SuppressWarnings("unchecked")
	public List<LayerModel> getLayerModelsInternal() {
		Criteria crit = factory.getCurrentSession().createCriteria(LayerModel.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		crit.add(Restrictions.eq("deleted", false));
		return crit.list();
	}

	public void saveOrUpdateLayerModelInternal(LayerModel lm) {
		factory.getCurrentSession().saveOrUpdate(lm);
	}

	@SuppressWarnings("unchecked")
	public List<LayerModel> getDynamicLayerModelsInternal() {
		Criteria crit = factory.getCurrentSession().createCriteria(LayerModel.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		crit.add(Restrictions.eq("deleted", false));
		crit.add(Restrictions.eq("readOnly", false));
		return crit.list();
	}
	
	public LayerModel getLayerModelByClientLayerIdInternal(String id) {
		Query q = factory.getCurrentSession().createQuery("FROM LayerModel l WHERE l.clientLayerId = :id");
		q.setParameter("id", id);
		q.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		LayerModel l = (LayerModel) q.uniqueResult();
		return l;
	}
}
