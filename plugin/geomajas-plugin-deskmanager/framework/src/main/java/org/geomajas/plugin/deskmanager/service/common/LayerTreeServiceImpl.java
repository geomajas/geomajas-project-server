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

import org.geomajas.plugin.deskmanager.domain.LayerTree;
import org.geomajas.plugin.deskmanager.domain.LayerTreeNode;
import org.geomajas.plugin.deskmanager.domain.LayerTree.LayerTreeNodeVisitor;
import org.geomajas.security.GeomajasSecurityException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service is used indirectly to save/update Blueprints / Geodesks, security is handled there.
 * 
 * @author Kristof Heirwegh
 */
@Service("LayerTreeService")
@Repository
@Transactional(rollbackFor = { Exception.class })
public class LayerTreeServiceImpl implements LayerTreeService {

	@Autowired
	private SessionFactory factory;

	public void saveOrUpdateLayerTree(LayerTree lt) throws GeomajasSecurityException {
		lt.visitNodes(new LayerTreeNodeVisitor() {

			public void visit(LayerTreeNode node) {
				if (node.getView() != null) {
					factory.getCurrentSession().saveOrUpdate(node.getView());
				}
				if (node.getId() == null) {
					factory.getCurrentSession().saveOrUpdate(node);
				}
			}
		});
		factory.getCurrentSession().saveOrUpdate(lt);
	}

	public void deleteLayerTree(LayerTree lt) throws GeomajasSecurityException {
		if (lt.getRootNode() != null) {
			factory.getCurrentSession().delete(lt.getRootNode());
		}
		lt.setRootNode(null);
		factory.getCurrentSession().delete(lt);
	}

	public void deleteLayerTreeNode(LayerTreeNode ltn) throws GeomajasSecurityException {
		if (ltn != null) {
			factory.getCurrentSession().delete(ltn);
		}
	}

}
