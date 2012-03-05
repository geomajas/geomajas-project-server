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
package org.geomajas.widget.utility.gwt.client.util;

import org.geomajas.annotation.Api;

import com.smartgwt.client.types.Overflow;


/**
 * Class which helps to provide consistent sizes and names for layout purposes, see
 * {@link org.geomajas.gwt.client.util.WidgetLayout}.
 * <p/>
 * Implemented as static class to allow overwriting values at application start, thus allowing skinning.
 *
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public final class GuwLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF
	/** Member margin in the {@link org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton}. */
	public static int ribbonButtonInnerMargin = 2;
	/** Default icon size for the big buttons in a ribbon. */
	public static int ribbonColumnButtonIconSize = 24;
	/** Default icon size for the small vertical action lists in a ribbon. */
	public static int ribbonColumnListIconSize = 16;

	/** Member margin between ribbon groups in the ribbon bar. */
	public static int ribbonBarInternalMargin = 2;
	/** Member margin between components in a ribbon group. */
	public static int ribbonGroupInternalMargin = 10;
	
	/** Overflow of the ribbon bar */
	public static Overflow ribbonBarOverflow = Overflow.AUTO;
	/** Overflow of the ribbon group */
	public static Overflow ribbonGroupOverflow = Overflow.AUTO;

	// CHECKSTYLE VISIBILITY MODIFIER: ON
	/**
	 * Drop-down specific constants.
	 * 
	 * @author Emiel Ackermann
	 */
	public static final class DropDown {
		
		private DropDown() { }
		
		// CHECKSTYLE VISIBILITY MODIFIER: OFF
		/**
		 * Button layout, which is the same as a RibbonButton 
		 * in a ToolbarActionList; icon (16px) on the left and title on the right.
		 */
		public static final String ICON_AND_TITLE = "iconAndTitle";
		/** Default icon size for the icon without description. */
		public static int ribbonBarDropDownButtonIconSize = 16;

		/**
		 * Button layout consisting of an icon (24px) on the 
		 * left and the title and description on the right, the title on top of the description.
		 */
		public static final String ICON_TITLE_AND_DESCRIPTION = "iconTitleAndDescription";
		/** Default icon size for the icon with description. */
		public static int ribbonBarDropDownButtonDescriptionIconSize = 24;
		
		/** Wether to show the drop down image on the buttons. */
		public static boolean showDropDownImage;
		
		// CHECKSTYLE VISIBILITY MODIFIER: ON
	}
	

	private GuwLayout() {
		// do not allow instantiation.
	}

}
