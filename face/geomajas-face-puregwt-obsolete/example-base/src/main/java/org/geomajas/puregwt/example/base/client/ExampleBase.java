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

package org.geomajas.puregwt.example.base.client;

import org.geomajas.geometry.Bbox;
import org.geomajas.puregwt.example.base.client.page.sample.SamplePage;
import org.geomajas.puregwt.example.base.client.resource.ShowcaseResource;
import org.geomajas.puregwt.example.base.client.sample.ShowcaseSampleDefinition;
import org.geomajas.puregwt.example.base.client.widget.ShowcaseDialogBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.user.client.Window;

/**
 * Entry point and main class for PureGWT example application.
 * 
 * @author Pieter De Graef
 */
public class ExampleBase implements EntryPoint {

	// private static final GeomajasGinjector GEOMAJASINJECTOR = GWT.create(GeomajasGinjector.class);
	private static Ginjector ginjector;

	private static final ShowcaseResource RESOURCE = GWT.create(ShowcaseResource.class);

	private static final ShowcaseLayout LAYOUT = new ShowcaseLayout();

	public static final Bbox BBOX_ITALY = new Bbox(868324, 4500612, 1174072, 1174072);

	public static final Bbox BBOX_AFRICA = new Bbox(-2915614, -4324501, 9392582, 9392582);

	public void onModuleLoad() {
		// Prepare styling:
		RESOURCE.css().ensureInjected();
	}

	public static ShowcaseLayout getLayout() {
		return LAYOUT;
	}

	public static Ginjector getInjector() {
		return ginjector;
	}

	public static void setInjector(Ginjector injector) {
		ginjector = injector;
	}

	public static ShowcaseResource getShowcaseResource() {
		return RESOURCE;
	}

	public static void showSample(ShowcaseSampleDefinition sample) {
		ShowcaseDialogBox dialogBox = new ShowcaseDialogBox();
		dialogBox.setAnimationEnabled(true);
		dialogBox.setAutoHideEnabled(false);
		dialogBox.setModal(true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setText(sample.getTitle());
		SamplePage page = new SamplePage();
		int width = Window.getClientWidth() - 200;
		int height = Window.getClientHeight() - 160;
		page.setSize(width + "px", height + "px");
		page.setSamplePanelFactory(sample);
		dialogBox.setWidget(page);
		dialogBox.center();
		dialogBox.show();
	}
}