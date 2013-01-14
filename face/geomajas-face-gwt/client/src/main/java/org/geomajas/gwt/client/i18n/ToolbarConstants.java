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

package org.geomajas.gwt.client.i18n;

import com.google.gwt.i18n.client.Constants;

/**
 * <p>
 * Localization constants for all ToolbarActions and ToolbarSelectActions.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface ToolbarConstants extends Constants {

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String editingSelect();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String measureSelect();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String selectionSelect();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String zoomIn();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String zoomOut();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String pan();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String panToSelection();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String zoomToRectangle();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String zoomToSelection();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String zoomNext();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String zoomPrevious();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String scaleSelect();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String refreshConfiguration();

	/**
	 * @deprecated replaced by *Title & *Tooltip
	 */
	@Deprecated
	String featureInfo();


	String editingSelectTitle();

	String measureSelectTitle();

	String selectionSelectTitle();

	String zoomInTitle();

	String zoomOutTitle();

	String panTitle();

	String panToSelectionTitle();

	String zoomToRectangleTitle();

	String zoomToSelectionTitle();

	String zoomNextTitle();

	String zoomPreviousTitle();

	String scaleSelectTitle();

	String refreshConfigurationTitle();

	String featureInfoTitle();



	String editingSelectTooltip();

	String measureSelectTooltip();

	String selectionSelectTooltip();

	String zoomInTooltip();

	String zoomOutTooltip();

	String panTooltip();

	String panToSelectionTooltip();

	String zoomToRectangleTooltip();

	String zoomToSelectionTooltip();

	String zoomNextTooltip();

	String zoomPreviousTooltip();

	String scaleSelectTooltip();

	String refreshConfigurationTooltip();

	String featureInfoTooltip();

	String deselectAllTitle();

	String deselectAllTooltip();

}
