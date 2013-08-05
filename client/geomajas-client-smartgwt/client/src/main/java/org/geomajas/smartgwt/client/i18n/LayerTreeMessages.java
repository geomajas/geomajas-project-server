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

package org.geomajas.smartgwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization constants for the LayerTree.
 * </p>
 *
 * @author Frank Wynants
 */
public interface LayerTreeMessages extends Messages {

	String activeLayer(String layerName);

	String none();

	String visibleAction();

	String inVisibleAction();

	String noLayerSelected();

	String labelAction();

	String labelHideAction();

	String layerTreeHelp();

	String layerRefresh();
}
