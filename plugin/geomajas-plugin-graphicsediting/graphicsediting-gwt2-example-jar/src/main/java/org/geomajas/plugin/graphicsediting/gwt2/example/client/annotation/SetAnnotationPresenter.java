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
package org.geomajas.plugin.graphicsediting.gwt2.example.client.annotation;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * SetAnnotationPresenter.
 * 
 * @author Jan De Moerloose
 */
public interface SetAnnotationPresenter {
	/**
	 * Action.
	 * 
	 * @author Jan De Moerloose
	 */
	enum Action {
		CREATE_TEXT,
		CREATE_RECTANGLE,
		CREATE_POLYGON,
		CREATE_LINE,
		MAP, 
		CREATE_ICON,
		CREATE_ANCHORED_ICON,
		CREATE_ANCHORED_TEXT
	}

	/**
	 * View part.
	 * 
	 */
	interface View extends IsWidget {

		void setHandler(Handler handler);

		void setActive(Action action, boolean active);

	}

	/**
	 * View callback.
	 * 
	 */
	interface Handler {

		void onAction(Action action);
		
		void onDuplicateWhenDragging(boolean duplicate);

	}
	
	View getView();
}
