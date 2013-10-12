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
package org.geomajas.gwt2.widget.client.featureselectbox.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Feature select box interface.
 * 
 * @author Dosi Bingov
 * 
 */
public interface FeatureSelectBoxPresenter {

	/**
	 * View of FeatureSelectBox.
	 * 
	 * @author Dosi Bingov
	 * 
	 */
	interface View extends IsWidget {

		void setHandler(Handler handler);

		/**
		 * clears old labels and set new labels to the content panel of the widget.
		 * 
		 * @param labels
		 */
		void setLabels(List<String> labels);

		void addLabel(String label);

		void show(boolean animated);

		void setShowPosition(int xPos, int yPos);

		void hide();

		void clear();

		boolean isVisible();
	}

	/**
	 * Handler for FeatureSelectBox.
	 * 
	 * @author Dosi Bingov
	 * 
	 */
	interface Handler {

		void onFeatureSelected(String label);
	}

	void setView(View view);

	View getView();
}
