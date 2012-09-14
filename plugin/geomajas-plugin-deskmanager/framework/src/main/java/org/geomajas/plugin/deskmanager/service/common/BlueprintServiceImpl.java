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

import java.util.Date;
import java.util.List;

import org.geomajas.global.ExceptionCode;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
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

	public Blueprint getBlueprintById(String uuid) throws GeomajasSecurityException {
		Blueprint bp = getBlueprintByIdInternal(uuid);
		if (bp != null) {
			if (((DeskmanagerSecurityContext) securityContext).readAllowed(bp)) {
				return bp;
			} else {
				throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Inlezen Blauwdruk",
						securityContext.getUserName());
			}
		} else {
			return null;
		}
	}

	public Blueprint getBlueprintByIdInternal(String uuid) {
		return (Blueprint) factory.getCurrentSession().get(Blueprint.class, uuid);
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

	public void deleteBlueprint(Blueprint bp) throws GeomajasSecurityException {
		if (((DeskmanagerSecurityContext) securityContext).deleteAllowed(bp)) {
			bp.setDeleted(true);
			factory.getCurrentSession().saveOrUpdate(bp);
		} else {
			throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Verwijderen Blauwdruk",
					securityContext.getUserName());
		}
	}

	public void saveOrUpdateBlueprint(Blueprint bp) throws GeomajasSecurityException {
		if (((DeskmanagerSecurityContext) securityContext).saveAllowed(bp)) {
			Date date = new Date();
			String fullName = ((DeskmanagerSecurityContext) securityContext).getFullName();
			if (bp.getCreationBy() == null) {
				bp.setCreationBy(fullName);
			}
			if (bp.getCreationDate() == null) {
				bp.setCreationDate(date);
			}
			bp.setLastEditDate(date);
			bp.setLastEditBy(fullName);

			factory.getCurrentSession().saveOrUpdate(bp);
		} else {
			throw new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, "Opslaan Blauwdruk",
					securityContext.getUserName());
		}
	}
}
