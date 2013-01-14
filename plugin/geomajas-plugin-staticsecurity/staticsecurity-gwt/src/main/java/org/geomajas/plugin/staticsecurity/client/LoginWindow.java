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

package org.geomajas.plugin.staticsecurity.client;

import com.smartgwt.client.types.KeyNames;
import org.geomajas.annotation.Api;
import org.geomajas.plugin.staticsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LoginHandler;
import org.geomajas.plugin.staticsecurity.client.event.LoginSuccessEvent;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemKeyPressEvent;
import com.smartgwt.client.widgets.form.events.ItemKeyPressHandler;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Window used for logging in. Display a simple form for logging in, and 2 buttons. One buttons effectively logs you in,
 * while the other allows for resetting the data filled in the form. When logging in not all attempts are successful.
 * Sometimes people forget their password. To connect to the correct events, add a {@link LoginHandler} by giving it to
 * this widget's constructor. It will add the handler to the <code>Authentication</code> final class.
 * </p>
 * <p>
 * This window by default displays a Geomajas logo. You can change this logo and you can add a general slogan to your
 * wishes. The logo (together with it's width) and slogan can be given through getters and setters or by calling the
 * correct constructor. Note that you have to set these before you call the draw. Also note that when setting a new
 * logo, you have to set it's width. This width may not be larger than 480 pixels.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.7.1
 * @deprecated use {@link TokenRequestWindow}
 */
@Api
@Deprecated
public class LoginWindow extends Window implements LoginHandler {

	private static final String FIELD_USER_NAME = "userName";

	private static final String FIELD_PASSWORD = "password";

	private int logoWidth = 300;

	private String logo = "[ISOMORPHIC]/geomajas/geomajas_logo.png";

	private String background = "[ISOMORPHIC]/geomajas/staticsecurity/login_background_grey.jpg";

	private DynamicForm loginForm;

	private String slogan;

	private Label errorLabel;

	private StaticSecurityMessages i18n;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create the default login window. Shows the login window with a default layout.
	 *
	 * @since 1.7.1
	 */
	@Api
	public LoginWindow() {
		super();
		Authentication.getInstance().addLoginHandler(this);
		buildWidget();
	}

	/**
	 * Create the default login window with an extra <code>LoginHandler</code> added to catch login events. Shows the
	 * login window with a default layout.
	 * 
	 * @param loginHandler
	 *            A <code>LoginHandler</code> that should be immediately added to catch login events.
	 * @since 1.7.1
	 */
	@Api
	public LoginWindow(LoginHandler loginHandler) {
		super();
		Authentication.getInstance().addLoginHandler(this);
		Authentication.getInstance().addLoginHandler(loginHandler);
		buildWidget();
	}

	/**
	 * Create a login window with a custom layout, and add a custom <code>LoginHandler</code> to catch login events.
	 * 
	 * @param loginHandler
	 *            A <code>LoginHandler</code> that should be immediately added to catch login events.
	 * @param slogan
	 *            Add a general slogan to this window.
	 * @param logo
	 *            Change the default logo into this one.
	 * @param logoWidth
	 *            When setting a new logo, it is imperative to also give it's width in pixels.
	 * @since 1.7.1
	 */
	@Api
	public LoginWindow(LoginHandler loginHandler, String slogan, String logo, int logoWidth) {
		super();
		this.slogan = slogan;
		this.logo = logo;
		this.logoWidth = logoWidth;
		Authentication.getInstance().addLoginHandler(this);
		Authentication.getInstance().addLoginHandler(loginHandler);
		buildWidget();
	}

	// -------------------------------------------------------------------------
	// LoginHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * Called when a login attempt fails. This will display a login failure message.
	 */
	public void onLoginFailure(LoginFailureEvent event) {
		reportError(i18n.loginFailure());
	}

	/**
	 * Called when a login attempt is successful. This method will destroy this login window.
	 */
	public void onLoginSuccess(LoginSuccessEvent event) {
		destroy();
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

	/**
	 * @return Get the general slogan.
	 */
	public String getSlogan() {
		return slogan;
	}

	/**
	 * Set a new slogan for this window. If null, no slogan will be displayed (that is also the default value).
	 * 
	 * @param slogan
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	/**
	 * @return Get the URL to the background image to be used in this window.
	 */
	public String getBackground() {
		return background;
	}

	/**
	 * Set the URL to the background image to be used in this window. Make sure it has a width and height that fits this
	 * window (default is 500x300). If this value is null, no background will be shown.
	 * 
	 * @param background
	 *            The new URL to the background image.
	 */
	public void setBackground(String background) {
		this.background = background;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected void reportError(String error) {
		errorLabel.setContents("<span style='color:#FFAA00;'>" + error + "</span>");
	}

	private void buildWidget() {
		i18n = GWT.create(StaticSecurityMessages.class);

		setHeaderIcon("[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png", 16, 16);
		setTitle(i18n.loginWindowTitle());
		setWidth(500);
		setHeight(300);
		setShowShadow(true);
		setShadowDepth(10);

		VLayout layout = new VLayout();
		if (background != null) {
			layout.setBackgroundImage(background);
		}
		layout.setLayoutAlign(Alignment.CENTER);
		layout.setMembersMargin(10);
		layout.setPadding(10);

		Img logoImg = new Img(logo);
		logoImg.setWidth(logoWidth);
		logoImg.setLayoutAlign(Alignment.CENTER);
		logoImg.setLayoutAlign(VerticalAlignment.CENTER);
		layout.addMember(logoImg);

		if (slogan != null && slogan.length() > 0) {
			Label titleLabel = new Label(slogan);
			titleLabel.setWidth(logoWidth);
			titleLabel.setHeight(24);
			titleLabel.setLayoutAlign(Alignment.CENTER);
			titleLabel.setAlign(Alignment.CENTER);
			layout.addMember(titleLabel);
		}

		// User name:
		TextItem userNameItem = new TextItem(FIELD_USER_NAME);
		userNameItem.setWidth(logoWidth - 60);
		userNameItem.setTitle(i18n.loginUserName());
		userNameItem.setSelectOnFocus(true);
		userNameItem.setWrapTitle(false);

		// Password:
		PasswordItem passwordItem = new PasswordItem(FIELD_PASSWORD);
		passwordItem.setWidth(logoWidth - 60);
		passwordItem.setTitle(i18n.loginPassword());
		passwordItem.setWrapTitle(false);

		// Login form:
		loginForm = new DynamicForm();
		loginForm.setAutoFocus(true);
		loginForm.setNumCols(2);
		loginForm.setWidth(logoWidth);
		loginForm.setLayoutAlign(Alignment.CENTER);
		loginForm.setFields(userNameItem, passwordItem);
		loginForm.setCanFocus(true);
		loginForm.addItemKeyPressHandler(new ItemKeyPressHandler() {

			public void onItemKeyPress(ItemKeyPressEvent event) {
				if (KeyNames.ENTER.equals(event.getKeyName())) {
					login();
				}
			}
		});
		layout.addMember(loginForm);

		// Login button:
		IButton loginButton = new IButton(i18n.loginBtnLogin());
		loginButton.setWidth(80);
		loginButton.setIcon("[ISOMORPHIC]/geomajas/staticsecurity/key_go.png");
		loginButton.addClickHandler(new LoginClickHandler());
		loginButton.setLayoutAlign(Alignment.RIGHT);

		// Reset button:
		IButton resetButton = new IButton(i18n.loginBtnReset());
		resetButton.setWidth(80);
		resetButton.setIcon("[ISOMORPHIC]/geomajas/silk/undo.png");
		resetButton.addClickHandler(new ResetClickHandler());
		resetButton.setLayoutAlign(Alignment.LEFT);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setHeight(30);
		buttonLayout.setWidth(logoWidth);
		buttonLayout.setMembersMargin(10);
		buttonLayout.setLayoutAlign(Alignment.CENTER);

		VLayout loginBtnLayout = new VLayout();
		loginBtnLayout.setWidth("50%");
		loginBtnLayout.addMember(loginButton);
		buttonLayout.addMember(loginBtnLayout);

		VLayout resetBtnLayout = new VLayout();
		resetBtnLayout.setWidth("50%");
		resetBtnLayout.addMember(resetButton);
		buttonLayout.addMember(resetBtnLayout);

		layout.addMember(buttonLayout);
		layout.addMember(new LayoutSpacer());

		// Error label:
		errorLabel = new Label();
		errorLabel.setWidth100();
		errorLabel.setHeight(14);
		layout.addMember(errorLabel);

		addItem(layout);
	}

	private void login() {
		String userId = loginForm.getValueAsString(FIELD_USER_NAME);
		String password = loginForm.getValueAsString(FIELD_PASSWORD);
		if (userId == null || userId.length() == 0) {
			reportError(i18n.loginNoUserName());
			return;
		}
		if (password == null || password.length() == 0) {
			reportError(i18n.loginNoPassword());
			return;
		}
		Authentication.getInstance().login(userId, password, null);
	}

	/**
	 * ClickHandler for the login button.
	 * 
	 * @author Pieter De Graef
	 */
	private class LoginClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			login();
		}
	}

	/**
	 * ClickHandler for the reset button.
	 * 
	 * @author Pieter De Graef
	 */
	private class ResetClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			loginForm.setValue(FIELD_USER_NAME, "");
			loginForm.setValue(FIELD_PASSWORD, "");
		}
	}
}
