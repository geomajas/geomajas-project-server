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
package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.puregwt.client.map.DefaultMapGadgetFactory;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.ui.Widget;

public class MockDefaultMapGadgetFactory implements DefaultMapGadgetFactory {


	public MapGadget createGadget(Type type, int top, int left, ClientMapInfo clientMapInfo) {
		return new TestGadget();
	}

	
	public class TestGadget implements MapGadget {

		public Widget asWidget() {
			return null;
		}

		public void beforeDraw(MapPresenter mapPresenter) {
		}

		public Alignment getHorizontalAlignment() {
			return Alignment.BEGIN;
		}

		public Alignment getVerticalAlignment() {
			return Alignment.BEGIN;
		}

		public int getHorizontalMargin() {
			return 0;
		}

		public int getVerticalMargin() {
			return 0;
		}

		public int getWidth() {
			return 0;
		}

		public int getHeight() {
			return 0;
		}

		public void setWidth(int width) {
		}

		public void setHeight(int height) {
		}

		public void setTop(int top) {
		}

		public void setLeft(int left) {
		}

		public void addResizeHandler(ResizeHandler resizeHandler) {
		}

	}

}
