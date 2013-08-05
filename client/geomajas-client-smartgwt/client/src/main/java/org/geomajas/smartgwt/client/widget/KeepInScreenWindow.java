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

package org.geomajas.smartgwt.client.widget;

import com.smartgwt.client.widgets.Window;
import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.util.WidgetLayout;

/**
 * Variant of {@link Window} which automatically limits itself to stay within screen bounds based on {@link
 * WidgetLayout#featureAttributeWindowKeepInScreen}.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api
public class KeepInScreenWindow extends Window {

	@Override
	public void onDraw() {
		// try to force to be inside the screen
		if (WidgetLayout.featureAttributeWindowKeepInScreen) {
			WidgetLayout.keepWindowInScreen(this);
		}
		super.onDraw();
	}

}
