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

package org.geomajas.gwt2.widget.example.client.i18n;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Specific messages for the many samples.
 * 
 * @author Pieter De Graef
 */
public interface SampleMessages extends Messages {

	// ------------------------------------------------------------------------
	// Widget core examples: general UI examples
	// ------------------------------------------------------------------------

	String closeableDialogTitle();
	String closeableDialogDescrShort();
	String closeableDialogDescription();
	String closeableDialogButShow();

	String messageBoxTitle();
	String messageBoxDescrShort();
	String messageBoxDescription();
	String messageBoxInfoMessageBtn();
	String messageBoxWarnMessageBtn();
	String messageBoxErrorMessageBtn();
	String messageBoxHelpMessageBtn();
	String messageBoxYesNoBtn();
	String messageBoxYesNoCancelBtn();
	String messageBoxMessage();
	String messageBoxResponseYes();
	String messageBoxResponseNo();
	String messageBoxResponseCancel();
	SafeHtml messageBoxMessageLong();
	
	String featureSelectedTitle();
	String featureSelectedDescrShort();
	String featureSelectedDescription();

	String featureMouseOverTitle();
	String featureMouseOverDescrShort();
	String featureMouseOverDescription();

	// ------------------------------------------------------------------------
	// Messages for the MapLegendPanel samples:
	// ------------------------------------------------------------------------

	String mapLegendPanelWidget();

	String mapLegendAddRemovedAddLayers();

	String mapLegendAddRemovedRemoveLayers();

	String mapLegendAddRemoveTitle();

	String mapLegendAddRemoveDescrShort();

	String mapLegendAddRemoveDescription();

	String mapLegendOrderAvailableLayers();

	String mapLegendOrderTitle();

	String mapLegendOrderDescrShort();

	String mapLegendOrderDescription();

	// ------------------------------------------------------------------------
	// Messages for the MapLegendDropDown samples:
	// ------------------------------------------------------------------------

	String mapLegendDropDownWidget();

	String mapLegendDropDownTitle();

	String mapLegendDropDownDescrShort();

	String mapLegendDropDownDescription();
}