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
package org.geomajas.plugin.deskmanager.service.manager;

import org.geomajas.configuration.NamedStyleInfo;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;

/**
 * @author Kristof Heirwegh
 */
@Component
public class CloneServiceImpl implements CloneService {

	private static final XStream STREAMER = new XStream();

	public NamedStyleInfo clone(NamedStyleInfo nsi) {
		if (nsi == null) {
			return null;
		}
		return (NamedStyleInfo) STREAMER.fromXML(STREAMER.toXML(nsi));
	}
}
