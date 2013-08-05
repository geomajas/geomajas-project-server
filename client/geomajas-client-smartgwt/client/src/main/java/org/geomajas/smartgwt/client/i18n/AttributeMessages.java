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
 * Localization constants concerning attributes (FeatureAttributeEditor, FeatureAttributeWindow, ...).
 * </p>
 *
 * @author Pieter De Graef
 */
public interface AttributeMessages extends Messages {

	String getAttributeWindowTitle(String featureLabel);

	String btnSaveTitle();

	String btnSaveTooltip();

	String btnResetTitle();

	String btnResetTooltip();

	String btnZoomFeature();

	String btnZoomTooltip();

	String btnEditTitle();

	String btnEditTooltip();

	String btnCancelTitle();

	String btnCancelTooltip();
	
	String btnApplyTitle();

	String btnApplyTooltip();
	
	String btnDeleteTitle();

	String btnDeleteTooltip();
	
	String btnNewTitle();

	String btnNewTooltip();

	String one2ManyMoreTitle();
	
	String one2ManyMoreTooltip();
}
