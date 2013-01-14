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

package org.geomajas.gwt.client.widget;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.event.DispatchStoppedEvent;
import org.geomajas.gwt.client.command.event.DispatchStoppedHandler;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt.client.util.WidgetLayout;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
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
 * A {@link View} strategy can be passed to the constructor to determine the appearance of the loading screen. Two
 * strategies have been implemented:
 * <ul>
 * <li>{@link DefaultView} is the default strategy. This strategy displays a Geomajas logo, a title, and a progress bar
 * that shows the loading progress. Both the logo width/height and the title can be changed to your wishes. The title is
 * given to the constructor and the logo (together with it's width) can be given through getters and setters. Note that
 * you have to set these before you call the draw. Also note that when setting a new logo, you have to set it's width
 * and height. This width may not be larger than 480 pixels for the default.
 * <li>{@link LogoOnlyView} is a strategy that just shows the logo. The logo and its width/height can be changed to your
 * wishes, either through setters or through the {@link WidgetLayout} properties.
 * </ul>
 * </p>
 * <p>
 * 
 * </p>
 * <p>
 * Use: add this loading screen to the main page by calling it's {#link #draw()} method. Do this AFTER you have drawn
 * your application layout, so the loading screen is placed on top of it.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class LoadingScreen extends VLayout {

	private HandlerRegistration onLoadRegistration;

	private HandlerRegistration onDispatchStopRegistration;
	
	private View view;

	private int progressPercentage;

	private boolean fadingDone;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create a loading screen given the main map and a title to be displayed. This screen uses the
	 * {@link DefaultView} strategy.
	 * 
	 * @param mapWidget The main map
	 * @param applicationTitle The application's title. This will be displayed right under the logo image.
	 */
	public LoadingScreen(MapWidget mapWidget, String applicationTitle) {
		this(mapWidget, new DefaultView(applicationTitle));
	}

	/**
	 * Create a loading screen given the main map and a {@link View} strategy to be used.
	 * 
	 * @param mapWidget
	 *            The main map
	 * @param view
	 *            The view object to use.
	 */
	public LoadingScreen(MapWidget mapWidget, View view) {
		this.view = view;
		setCursor(Cursor.WAIT);
		addMember(view.getWidget());
		setBackgroundColor(WidgetLayout.loadingScreenBackgroundColor);
		setHeight100();
		setWidth100();
		setAlign(VerticalAlignment.CENTER);
		setAlign(Alignment.CENTER);
		// make sure we always fade out after 20s !
		final MapWidget m = mapWidget;
		Timer t = new Timer() {

			@Override
			public void run() {
				fadeOut(m);
			}

		};
		t.schedule(20000);
		registerMap(mapWidget);

	}
	
	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Set a new image width for the logo. This should be done when a new logo image has been set.
	 * 
	 * @param logoWidth
	 *            The width of the logo image to be used.
	 */
	public void setLogoWidth(String logoWidth) {
		view.setLogoWidth(logoWidth);
	}

	/**
	 * Set a new image height for the logo. This should be done when a new logo image has been set.
	 * 
	 * @param logoHeight
	 *            The height of the logo image to be used.
	 */
	public void setLogoHeight(String logoHeight) {
		view.setLogoHeight(logoHeight);
	}


	/**
	 * Set a new URL to a new logo image to be used in this loading screen. After using this method, also set the
	 * image's width.
	 * 
	 * @param logo logo
	 */
	public void setLogo(String logo) {
		view.setLogo(logo);
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void registerMap(final MapWidget mapWidget) {
		if (mapWidget != null) {
			onLoadRegistration = mapWidget.getMapModel().addMapModelHandler(new MapModelHandler() {

				public void onMapModelChange(MapModelEvent event) {
					onLoadRegistration.removeHandler();
					view.onLoadStart();
					if (GwtCommandDispatcher.getInstance().isBusy()) {
						onDispatchStopRegistration = GwtCommandDispatcher.getInstance().addDispatchStoppedHandler(
								new DispatchStoppedHandler() {

									public void onDispatchStopped(DispatchStoppedEvent event) {
										fadeOut(mapWidget);
									}
								});
					} else {
						fadeOut(mapWidget);
					}
				}
			});
		}
	}

	protected void onDraw() {
		super.onDraw();
		Timer timer = new Timer() {

			public void run() {
				progressPercentage += 10;
				view.onLoadProgress(progressPercentage);
				if (progressPercentage < 100) {
					schedule(50);
				}
			}
		};
		timer.schedule(50);
	}

	private void fadeOut(final MapWidget mapWidget) {
		if (!fadingDone) {
			// progressBar.setPercentDone(100);
			view.onLoadStop();
			if (onDispatchStopRegistration != null) {
				onDispatchStopRegistration.removeHandler();
			}
			setCursor(Cursor.DEFAULT);

			setAnimateTime(1000);

			if (!Dom.isIE()) {
				// TODO Why should IE have a different approach??
				mapWidget.setResizedHandlerDisabled(true);
			}
			animateFade(0, new AnimationCallback() {

				public void execute(boolean earlyFinish) {
					mapWidget.setResizedHandlerDisabled(false);
					LoadingScreen.this.destroy();
				}
			});
			fadingDone = true;
		}
	}
	
	/**
	 * View strategy to be implemented by UI part of the loading screen. As a minimum, a {@link View} should implement
	 * the {@link #getWidget()} method. The returned widget will be shown in the center of the screen.
	 * 
	 * @author Jan De Moerloose
	 * @since 1.10.0
	 * 
	 */
	@Api(allMethods = true)
	public interface View {

		/**
		 * Returns this view's widget.
		 * 
		 * @return the widget
		 */
		Widget getWidget();

		/**
		 * Called when loading of the map is started.
		 */
		void onLoadStart();

		/**
		 * Called at regular time intervals to indicate progress (fake percentage).
		 * 
		 * @param progressPercentage percentage of progress passed (0-100)
		 */
		void onLoadProgress(int progressPercentage);

		/**
		 * Called when loading of the map is done or more than 20 seconds have passed.
		 */
		void onLoadStop();

		/**
		 * Set a new image width for the logo. This should be done when a new logo image has been set.
		 * 
		 * @param logoWidth The width of the logo image to be used.
		 */
		void setLogoWidth(String logoWidth);

		/**
		 * Set a new image height for the logo. This should be done when a new logo image has been set.
		 * 
		 * @param logoHeight
		 *            The height of the logo image to be used.
		 */
		void setLogoHeight(String logoHeight);
		
		/**
		 * Set a new URL to a new logo image to be used in this loading screen. After using this method, also set the
		 * image's width.
		 * 
		 * @param logo logo
		 */
		void setLogo(String logo);

	}
	
	/**
	 * Default implementation of UI part. This implementation displays a Geomajas logo, a title, and a progress bar that
	 * shows the loading progress. Both the logo width/height and the title can be changed to your wishes. The title is
	 * given to the constructor and the logo (together with it's width) can be given through getters and setters. Note
	 * that you have to set these before you call the draw. Also note that when setting a new logo, you have to set it's
	 * width and height. This width may not be larger than 480 pixels for the default.
	 * 
	 * @author Jan De Moerloose
	 * @since 1.10.0
	 * 
	 */
	@Api(allMethods = true)
	public static class DefaultView extends HLayout implements View {

		private String logoWidth = WidgetLayout.loadingScreenLogoWidth;

		private String logoHeight = WidgetLayout.loadingScreenLogoHeight;

		private String logo = WidgetLayout.loadingScreenLogo;

		private Label label;

		private Progressbar progressBar;

		/**
		 * Construct a default UI with the specified title.
		 * 
		 * @param applicationTitle title to display
		 */
		public DefaultView(String applicationTitle) {
			VLayout banner = new VLayout();
			banner.setLayoutAlign(Alignment.CENTER);
			banner.setLayoutAlign(VerticalAlignment.CENTER);

			LayoutSpacer spacerTop = new LayoutSpacer();
			spacerTop.setHeight(WidgetLayout.loadingScreenTopSpacerHeight);
			banner.addMember(spacerTop);

			Img logoImg = new Img(logo);
			logoImg.setWidth(logoWidth);
			logoImg.setHeight(logoHeight);
			logoImg.setLayoutAlign(Alignment.CENTER);
			logoImg.setLayoutAlign(VerticalAlignment.CENTER);
			banner.addMember(logoImg);

			Label titleLabel = new Label(applicationTitle);
			titleLabel.setWidth(logoWidth);
			titleLabel.setHeight(WidgetLayout.loadingScreenTitleHeight);
			titleLabel.setLayoutAlign(Alignment.CENTER);
			titleLabel.setAlign(Alignment.CENTER);
			banner.addMember(titleLabel);

			LayoutSpacer spacer = new LayoutSpacer();
			banner.addMember(spacer);

			VLayout progressLayout = new VLayout();
			progressLayout.setBackgroundColor(WidgetLayout.loadingScreenProgressBackgroundColor);
			progressLayout.setOpacity(WidgetLayout.loadingScreenProgressOpacity);
			progressLayout.setHeight(WidgetLayout.loadingScreenProgressHeight);
			progressLayout.setPadding(WidgetLayout.loadingScreenProgressPadding);

			label = new Label(I18nProvider.getGlobal().loadScreenDownLoadText());
			label.setLayoutAlign(Alignment.CENTER);
			label.setWidth100();
			label.setHeight(WidgetLayout.loadingScreenProgressLabelHeight);
			label.setStyleName("loadingScreenLabel");
			label.setOpacity(100);
			progressLayout.addMember(label);

			progressBar = new Progressbar();
			progressBar.setHeight(WidgetLayout.loadingScreenProgressBarHeight);
			progressBar.setWidth100();
			progressBar.setVertical(false);
			progressBar.setLayoutAlign(Alignment.CENTER);
			progressBar.setLayoutAlign(VerticalAlignment.CENTER);
			progressBar.setOpacity(100);
			progressLayout.addMember(progressBar);
			banner.addMember(progressLayout);

			setBackgroundColor(WidgetLayout.loadingScreenBackgroundColor);
			setShowEdges(true);
			setShowShadow(true);
			setShadowDepth(WidgetLayout.loadingScreenShadowDepth);
			setLayoutAlign(Alignment.CENTER);
			setLayoutAlign(VerticalAlignment.CENTER);
			setWidth(WidgetLayout.loadingScreenWidth);
			setHeight(WidgetLayout.loadingScreenHeight);
			if (!(WidgetLayout.loadingScreenBackgroundImage == null)) {
				setBackgroundImage(WidgetLayout.loadingScreenBackgroundImage);
			}
			setEdgeOpacity(WidgetLayout.loadingScreenEdgeOpacity);
			setAlign(Alignment.CENTER);
			addMember(banner);
		}

		/**
		 * Set a new image width for the logo. This should be done when a new logo image has been set.
		 * 
		 * @param logoWidth The width of the logo image to be used.
		 */
		public void setLogoWidth(String logoWidth) {
			this.logoWidth = logoWidth;
		}

		/**
		 * Set a new image height for the logo. This should be done when a new logo image has been set.
		 * 
		 * @param logoHeight
		 *            The height of the logo image to be used.
		 */
		public void setLogoHeight(String logoHeight) {
			this.logoHeight = logoHeight;
		}

			/**
		 * Set a new URL to a new logo image to be used in this loading screen. After using this method, also set the
		 * image's width.
		 * 
		 * @param logo logo
		 */
		public void setLogo(String logo) {
			this.logo = logo;
		}

		/**
		 * Returns this view's widget.
		 * 
		 * @return the widget
		 */
		public Widget getWidget() {
			return this;
		}

		/**
		 * Called when loading of the map is started.
		 */
		public void onLoadStart() {
			label.setContents(I18nProvider.getGlobal().loadScreenLoadText());
		}

		/**
		 * Called at regular time intervals to indicate progress (fake percentage).
		 * 
		 * @param progressPercentage percentage of progress passed (0-100)
		 */
		public void onLoadProgress(int progressPercentage) {
			progressBar.setPercentDone(progressPercentage);
		}

		/**
		 * Called when loading of the map is done or more than 20 seconds have passed.
		 */
		public void onLoadStop() {
			label.setContents(I18nProvider.getGlobal().loadScreenReadyText());
		}
	}
	
	/**
	 * {@link View} strategy that just shows the logo. The logo and its width/height can be changed to your wishes,
	 * either through setters or through the {@link WidgetLayout} properties.
	 * 
	 * @author Jan De Moerloose
	 * @since 1.10.0
	 * 
	 */
	@Api(allMethods = true)
	public static class LogoOnlyView extends HLayout implements View {

		private String logoWidth = WidgetLayout.loadingScreenLogoWidth;

		private String logoHeight = WidgetLayout.loadingScreenLogoHeight;

		private String logo = WidgetLayout.loadingScreenLogo;
		
		public LogoOnlyView() {
			VLayout banner = new VLayout();
			banner.setLayoutAlign(Alignment.CENTER);
			banner.setLayoutAlign(VerticalAlignment.CENTER);

			Img logoImg = new Img(logo);
			logoImg.setWidth(logoWidth);
			logoImg.setHeight(logoHeight);
			logoImg.setLayoutAlign(Alignment.CENTER);
			logoImg.setLayoutAlign(VerticalAlignment.CENTER);
			banner.addMember(logoImg);

			setShowShadow(true);
			setShadowDepth(WidgetLayout.loadingScreenShadowDepth);
			setLayoutAlign(Alignment.CENTER);
			setLayoutAlign(VerticalAlignment.CENTER);
			setWidth(logoWidth);
			setHeight(logoHeight);
			setAlign(Alignment.CENTER);
			addMember(banner);
		}
		/**
		 * Set a new image width for the logo. This should be done when a new logo image has been set.
		 * 
		 * @param logoWidth The width of the logo image to be used.
		 */
		public void setLogoWidth(String logoWidth) {
			this.logoWidth = logoWidth;
		}

		/**
		 * Set a new image height for the logo. This should be done when a new logo image has been set.
		 * 
		 * @param logoHeight
		 *            The height of the logo image to be used.
		 */
		public void setLogoHeight(String logoHeight) {
			this.logoHeight = logoHeight;
		}

			/**
		 * Set a new URL to a new logo image to be used in this loading screen. After using this method, also set the
		 * image's width.
		 * 
		 * @param logo logo
		 */
		public void setLogo(String logo) {
			this.logo = logo;
		}
		/**
		 * Returns this view's widget.
		 * 
		 * @return the widget
		 */
		public Widget getWidget() {
			return this;
		}

		/**
		 * Called when loading of the map is started.
		 */
		public void onLoadStart() {
		}

		/**
		 * Called at regular time intervals to indicate progress (fake percentage).
		 * 
		 * @param progressPercentage percentage of progress passed (0-100)
		 */
		public void onLoadProgress(int progressPercentage) {
		}

		/**
		 * Called when loading of the map is done or more than 20 seconds have passed.
		 */
		public void onLoadStop() {
		}

	}
}
