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

package org.geomajas.plugin.deskmanager.security;

import org.geomajas.annotation.Api;
import org.geomajas.internal.security.DefaultSecurityContext;
import org.geomajas.plugin.deskmanager.domain.BaseGeodesk;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.role.authorization.DeskmanagerAuthorization;
import org.geomajas.plugin.deskmanager.security.role.authorization.DeskmanagerGeodeskAuthorization;
import org.geomajas.plugin.deskmanager.security.role.authorization.DeskmanagerManagementAuthorization;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * The security context is a thread scoped service which allows you to query the authorization details for the logged in
 * user.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 * 
 * @since 1.0.0
 */
@Api
public class DeskmanagerSecurityContext extends DefaultSecurityContext implements DeskmanagerManagementAuthorization,
		DeskmanagerGeodeskAuthorization {

	@Autowired
	private SessionFactory session;

	/**
	 * Get the role of the authenticated user.
	 * 
	 * @return the role.
	 */
	public Role getRole() {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerAuthorization) {
					return ((DeskmanagerAuthorization) authorization).getRole();
				}
			}
		}
		return null;
	}
	
	public String getGeodeskId() {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerAuthorization) {
					return ((DeskmanagerAuthorization) authorization).getGeodeskId();
				}
			}
		}
		return null;
	}

	/**
	 * Get a string representation of the full name of the authenticated user.
	 * 
	 * @return the full name.
	 */
	public String getFullName() {
		StringBuilder sb = new StringBuilder();
		sb.append(getProfile().getFirstName());
		sb.append(" ");
		sb.append(getProfile().getSurname());
		sb.append(" (");
		sb.append(getProfile().getTerritory().getName());
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Get the territory of the authenticated user.
	 * @return the territory.
	 */
	@Transactional
	public Territory getTerritory() {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerAuthorization) {
					return (Territory) session.getCurrentSession().merge(
							((DeskmanagerAuthorization) authorization).getGroup());
				}
			}
		}
		return null;
	}

	public Profile getProfile() {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerAuthorization) {
					return ((DeskmanagerAuthorization) authorization).getProfile();
				}
			}
		}
		return null;
	}

	// -- Layermodel --------------------------------------------------------

	public boolean saveAllowed(LayerModel lm) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).saveAllowed(lm);
				}
			}
		}
		return false;
	}

	public boolean deleteAllowed(LayerModel lm) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).deleteAllowed(lm);
				}
			}
		}
		return false;
	}

	public boolean readAllowed(LayerModel lm) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).readAllowed(lm);
				}
			}
		}
		return false;
	}

	public Criterion getFilterLayerModels() {
		Criterion filter = null;
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					Criterion part = ((DeskmanagerManagementAuthorization) authorization).getFilterLayerModels();
					if (null != part) {
						if (filter == null) {
							filter = part;
						} else {
							filter = Restrictions.and(filter, part);
						}
					}
				}
			}
		}
		return filter;
	}

	// -- Blueprint --------------------------------------------------------

	public boolean readAllowed(BaseGeodesk bp) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).readAllowed(bp);
				}
			}
		}
		return false;
	}

	public boolean saveAllowed(BaseGeodesk bp) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).saveAllowed(bp);
				}
			}
		}
		return false;
	}

	public boolean deleteAllowed(BaseGeodesk bp) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).deleteAllowed(bp);
				}
			}
		}
		return false;
	}

	public Criterion getFilterBlueprints() {
		Criterion filter = null;
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					Criterion part = ((DeskmanagerManagementAuthorization) authorization).getFilterBlueprints();
					if (null != part) {
						if (filter == null) {
							filter = part;
						} else {
							filter = Restrictions.and(filter, part);
						}
					}
				}
			}
		}
		return filter;
	}

	// -- Geodesk --------------------------------------------------------

	public boolean isGeodeskUseAllowed(String geodeskId) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerGeodeskAuthorization) {
					return ((DeskmanagerGeodeskAuthorization) authorization).isGeodeskUseAllowed(geodeskId);
				}
			}
		}
		return false;
	}

	public boolean readAllowed(Geodesk geodesk) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).readAllowed(geodesk);
				}
			}
		}
		return false;
	}

	public boolean saveAllowed(Geodesk l) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).saveAllowed(l);
				}
			}
		}
		return false;
	}

	public boolean deleteAllowed(Geodesk l) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization).deleteAllowed(l);
				}
			}
		}
		return false;
	}

	public Criterion getFilterLoketten() {
		Criterion filter = null;
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					Criterion part = ((DeskmanagerManagementAuthorization) authorization).getFilterLoketten();
					if (null != part) {
						if (filter == null) {
							filter = part;
						} else {
							filter = Restrictions.and(filter, part);
						}
					}
				}
			}
		}
		return filter;
	}

	// --- ShapeFileUpload ----------------------------------------------

	public boolean isShapeFileUploadAllowed(String clientLayerId) {
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof DeskmanagerManagementAuthorization) {
					return ((DeskmanagerManagementAuthorization) authorization)
							.isShapeFileUploadAllowed(clientLayerId);
				}
			}
		}
		return false;
	}
}
