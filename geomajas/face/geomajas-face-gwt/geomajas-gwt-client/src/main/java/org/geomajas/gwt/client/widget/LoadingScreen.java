/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.widget;

import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.event.DispatchStoppedEvent;
import org.geomajas.gwt.client.command.event.DispatchStoppedHandler;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.util.DOM;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * A widget that covers the entire browser window, displaying application loading progress, and that fades out after the
 * application has been loaded. Your basic loading screen.
 * </p>
 * <p>
 * This screen by default displays a Geomajas logo, a title, and a progress bar that shows the loading progress. Both
 * the logo and the title can be changed to your wishes. The title is given to the constructor and the logo (together
 * with it's width) can be given through getters and setters. Note that you have to set these before you call the draw.
 * Also note that when setting a new logo, you have to set it's width. This width may not be larger than 480 pixels.
 * </p>
 * <p>
 * Use: add this loading screen to the main page by simply calling it's <code>draw</code> method. Do this AFTER you have
 * drawn your application layout, so the loading screen is placed on top of it.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LoadingScreen extends VLayout {

	private HandlerRegistration onLoadRegistration;

	private HandlerRegistration onDispatchStoppenRegistration;

	private int logoWidth = 300;

	private String logo = "[ISOMORPHIC]/geomajas/temp/geomajas_logo.png";

	private Label label;

	private Progressbar progressBar;

	private int progressPercentage;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create a loading screen given the main map's map model, and a title to be displayed.
	 * 
	 * @param mapModel
	 *            The main map's model.
	 * @param applicationTitle
	 *            The application's title. This will be displayed right under the logo image.
	 */
	public LoadingScreen(MapWidget mapWidget, String applicationTitle) {
		super();
		registerMap(mapWidget);
		setCursor(Cursor.WAIT);

		VLayout banner = new VLayout();
		banner.setLayoutAlign(Alignment.CENTER);
		banner.setLayoutAlign(VerticalAlignment.CENTER);

		LayoutSpacer spacerTop = new LayoutSpacer();
		spacerTop.setHeight(40);
		banner.addMember(spacerTop);

		Img logoImg = new Img(logo);
		logoImg.setWidth(logoWidth);
		logoImg.setLayoutAlign(Alignment.CENTER);
		logoImg.setLayoutAlign(VerticalAlignment.CENTER);
		banner.addMember(logoImg);

		Label titleLabel = new Label(applicationTitle);
		titleLabel.setWidth(logoWidth);
		titleLabel.setHeight(24);
		titleLabel.setLayoutAlign(Alignment.CENTER);
		titleLabel.setAlign(Alignment.CENTER);
		banner.addMember(titleLabel);

		LayoutSpacer spacer = new LayoutSpacer();
		banner.addMember(spacer);

		VLayout progressLayout = new VLayout();
		progressLayout.setBackgroundColor("#000000");
		progressLayout.setOpacity(30);
		progressLayout.setHeight(80);
		progressLayout.setPadding(15);

		label = new Label(I18nProvider.getGlobal().loadScreenDownLoadText());
		label.setLayoutAlign(Alignment.CENTER);
		label.setWidth100();
		label.setHeight(15);
		label.setStyleName("loadingScreenLabel");
		label.setOpacity(100);
		progressLayout.addMember(label);

		progressBar = new Progressbar();
		progressBar.setHeight(30);
		progressBar.setWidth100();
		progressBar.setVertical(false);
		progressBar.setLayoutAlign(Alignment.CENTER);
		progressBar.setLayoutAlign(VerticalAlignment.CENTER);
		progressBar.setOpacity(100);
		progressLayout.addMember(progressBar);
		banner.addMember(progressLayout);

		HLayout inner = new HLayout();
		inner.setBackgroundColor("#FFFFFF");
		inner.setShowEdges(true);
		inner.setShowShadow(true);
		inner.setShadowDepth(10);
		inner.setLayoutAlign(Alignment.CENTER);
		inner.setLayoutAlign(VerticalAlignment.CENTER);
		inner.setWidth(500);
		inner.setHeight(300);
		inner.setBackgroundImage("[ISOMORPHIC]/geomajas/temp/background.png");
		inner.setEdgeOpacity(70);
		inner.setAlign(Alignment.CENTER);
		inner.addMember(banner);

		setBackgroundColor("#FFFFFF");
		setHeight100();
		setWidth100();
		setAlign(VerticalAlignment.CENTER);
		setAlign(Alignment.CENTER);
		addMember(inner);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Get the current logo image width.
	 */
	public int getLogoWidth() {
		return logoWidth;
	}

	/**
	 * Set a new image width for the logo. This should be done when a new logo image has been set.
	 * 
	 * @param logoWidth
	 *            The width of the logo image to be used.
	 */
	public void setLogoWidth(int logoWidth) {
		this.logoWidth = logoWidth;
	}

	/**
	 * Get the URL of the logo to be used in this loading screen.
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * Set a new URL to a new logo image to be used in this loading screen. After using this method, also set the
	 * image's width.
	 * 
	 * @param logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void registerMap(final MapWidget mapWidget) {
		if (mapWidget != null) {
			onLoadRegistration = mapWidget.getMapModel().addMapModelHandler(new MapModelHandler() {

				public void onMapModelChange(MapModelEvent event) {
					onLoadRegistration.removeHandler();
					label.setContents(I18nProvider.getGlobal().loadScreenLoadText());
					onDispatchStoppenRegistration = GwtCommandDispatcher.getInstance().addDispatchStoppedHandler(
							new DispatchStoppedHandler() {

								public void onDispatchStopped(DispatchStoppedEvent event) {
									// progressBar.setPercentDone(100);
									label.setContents(I18nProvider.getGlobal().loadScreenReadyText());
									onDispatchStoppenRegistration.removeHandler();
									setCursor(Cursor.DEFAULT);

									GWT.log("Loading screen starts to fade out....", null);
									setAnimateTime(1000);

									if (!DOM.isIE()) {
										// TODO Why should IE have a different approach??
										mapWidget.setResizedHandlerDisabled(true);
									}
									animateFade(0, new AnimationCallback() {

										public void execute(boolean earlyFinish) {
											GWT.log("Loading screen destroy", null);
											mapWidget.setResizedHandlerDisabled(false);
											LoadingScreen.this.destroy();
										}
									});
								}
							});
				}
			});
		}
	}

	protected void onDraw() {
		super.onDraw();
		Timer timer = new Timer() {

			public void run() {
				progressPercentage += 10;
				progressBar.setPercentDone(progressPercentage);
				if (progressPercentage < 100) {
					schedule(50);
				}
			}
		};
		timer.schedule(50);
	}
}
