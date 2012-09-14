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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import org.geomajas.command.CommandResponse;

/**
 * @author Kristof Heirwegh
 */
public class GetGeodeskUrlBaseResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private String loketUrlBaseVO;

	private String loketUrlBaseLO;

	private String loketUrlBasePublic;

	public String getLoketUrlBaseVO() {
		return loketUrlBaseVO;
	}

	public void setLoketUrlBaseVO(String loketUrlBaseVO) {
		this.loketUrlBaseVO = loketUrlBaseVO;
	}

	public String getLoketUrlBaseLO() {
		return loketUrlBaseLO;
	}

	public void setLoketUrlBaseLO(String loketUrlBaseLO) {
		this.loketUrlBaseLO = loketUrlBaseLO;
	}

	public String getLoketUrlBasePublic() {
		return loketUrlBasePublic;
	}

	public void setLoketUrlBasePublic(String loketUrlBasePublic) {
		this.loketUrlBasePublic = loketUrlBasePublic;
	}

}
