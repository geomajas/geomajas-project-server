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

package org.geomajas.widget.utility.gwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Messages for the utility widgets.
 *
 * @author Joachim Van der Auwera
 */
public interface GuwMessages extends Messages {

	/** @return Title for the next button in the wizard. */
	String wizardNext();

	/** @return Title for the previous button in the wizard. */
	String wizardPrevious();

	/** @return Title for the cancel button in the wizard. */
	String wizardCancel();

	/** @return Title for the finish button in the wizard. */
	String wizardFinish();

	/** @return Message which is displayed when saving a wizard page fails. */
	String wizardSavePageFailed();
	
	String refreshLayersTitle();
	
	String refreshLayersTooltip();
}
