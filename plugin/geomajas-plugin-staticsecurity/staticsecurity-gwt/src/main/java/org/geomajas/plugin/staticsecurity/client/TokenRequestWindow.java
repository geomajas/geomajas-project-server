/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.client;

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
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.staticsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LoginHandler;
import org.geomajas.plugin.staticsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.staticsecurity.client.util.SsecAccess;
import org.geomajas.plugin.staticsecurity.client.util.SsecLayout;

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
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api
public class TokenRequestWindow extends Window implements LoginHandler {

	private static final StaticSecurityMessages MESSAGES = GWT.create(StaticSecurityMessages.class);

	private static final String FIELD_USER_NAME = "userName";
	private static final String FIELD_PASSWORD = "password";

	private TokenChangedHandler finishLoginHandler;
	private DynamicForm loginForm;
	private String slogan;
	private Label errorLabel;
	private int loginAttempt;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create the default login window. Shows the login window with a default layout.
	 */
	@Api
	public TokenRequestWindow() {
		super();
		errorLabel = new Label();
		loginForm = new DynamicForm();
	}

	/**
	 * Create the default login window with an extra <code>LoginHandler</code> added to catch login events. Shows the
	 * login window with a default layout.
	 *
	 * @param tokenChangedHandler callback for when the token was modified
	 */
	@Api
	public TokenRequestWindow(TokenChangedHandler tokenChangedHandler) {
		this();
		finishLoginHandler = tokenChangedHandler;
	}

	/**
	 * Called when a login attempt fails. This will display a login failure message.
	 */
	public void onLoginFailure(LoginFailureEvent event) {
		String msg;
		if (1 == ++loginAttempt) {
			msg = MESSAGES.tokenRequestRetry();
		} else {
			msg = MESSAGES.tokenRequestRetryAgain(loginAttempt);
		}
		reportError(msg);
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
	 * @return Get the general slogan.
	 */
	public String getSlogan() {
		return slogan;
	}

	/**
	 * Set a new slogan for this window. If null, no slogan will be displayed (that is also the default value).
	 * 
	 * @param slogan slogan
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	@Override
	public void draw() {
		buildWidget();
		// try to force to be inside the screen
		if (SsecLayout.loginWindowKeepInScreen) {
			WidgetLayout.keepWindowInScreen(this);
		}
		super.draw();
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected void reportError(String error) {
		errorLabel.setContents("<span style='color:#FFAA00;'>" + error + "</span>");
	}

	private void buildWidget() {
		setHeaderIcon(WidgetLayout.iconGeomajas, 16, 16);
		setTitle(MESSAGES.tokenRequestWindowTitle());
		setWidth(SsecLayout.loginWindowWidth);
		setHeight(SsecLayout.loginWindowHeight);
		setIsModal(true);
		setShowModalMask(true);
		setModalMaskOpacity(WidgetLayout.modalMaskOpacity);
		centerInPage();
		setAutoSize(true);
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);

		VLayout layout = new VLayout();
		if (null != SsecLayout.loginWindowBackground) {
			layout.setBackgroundImage(SsecLayout.loginWindowBackground);
		}
		layout.setLayoutAlign(Alignment.CENTER);
		layout.setMembersMargin(WidgetLayout.marginLarge);
		layout.setPadding(WidgetLayout.marginLarge);

		Img logoImg = new Img(SsecLayout.loginWindowLogo);
		logoImg.setWidth(SsecLayout.loginWindowLogoWidth);
		logoImg.setHeight(SsecLayout.loginWindowLogoHeight);
		logoImg.setLayoutAlign(Alignment.CENTER);
		logoImg.setLayoutAlign(VerticalAlignment.CENTER);
		layout.addMember(logoImg);

		if (slogan != null && slogan.length() > 0) {
			Label titleLabel = new Label(slogan);
			titleLabel.setWidth(SsecLayout.loginWindowLogoWidth);
			titleLabel.setHeight(SsecLayout.loginWindowSloganHeight);
			titleLabel.setLayoutAlign(Alignment.CENTER);
			titleLabel.setAlign(Alignment.CENTER);
			layout.addMember(titleLabel);
		}

		// User name:
		TextItem userNameItem = new TextItem(FIELD_USER_NAME);
		userNameItem.setWidth(SsecLayout.loginWindowFieldWidth);
		userNameItem.setTitle(MESSAGES.tokenRequestUserId());
		userNameItem.setSelectOnFocus(true);
		userNameItem.setWrapTitle(false);

		// Password:
		PasswordItem passwordItem = new PasswordItem(FIELD_PASSWORD);
		passwordItem.setWidth(SsecLayout.loginWindowFieldWidth);
		passwordItem.setTitle(MESSAGES.tokenRequestPassword());
		passwordItem.setWrapTitle(false);

		// Login form:
		loginForm.setAutoFocus(true);
		loginForm.setNumCols(2);
		loginForm.setWidth(SsecLayout.loginWindowLogoWidth);
		loginForm.setLayoutAlign(Alignment.CENTER);
		loginForm.setFields(userNameItem, passwordItem);
		loginForm.setCanFocus(true);
		loginForm.addItemKeyPressHandler(new ItemKeyPressHandler() {

			public void onItemKeyPress(ItemKeyPressEvent event) {
				if ("Enter".equals(event.getKeyName())) {
					login();
				}
			}
		});
		layout.addMember(loginForm);

		// Login button:
		IButton loginButton = new IButton(MESSAGES.tokenRequestButtonLogin());
		loginButton.setWidth(SsecLayout.loginWindowButtonWidth);
		loginButton.setIcon(SsecLayout.iconLogin);
		loginButton.addClickHandler(new TokenRequestLoginClickHandler());
		loginButton.setLayoutAlign(Alignment.RIGHT);

		// Reset button:
		IButton resetButton = new IButton(MESSAGES.tokenRequestButtonReset());
		resetButton.setWidth(SsecLayout.loginWindowButtonWidth);
		resetButton.setIcon(WidgetLayout.iconUndo);
		resetButton.addClickHandler(new TokenRequestResetClickHandler());
		resetButton.setLayoutAlign(Alignment.LEFT);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setHeight(30);
		buttonLayout.setWidth(SsecLayout.loginWindowLogoWidth);
		buttonLayout.setMembersMargin(WidgetLayout.marginSmall);
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
		errorLabel.setWidth100();
		errorLabel.setHeight(SsecLayout.loginWindowErrorHeight);
		layout.addMember(errorLabel);

		addItem(layout);
	}

	private void login() {
		String userId = loginForm.getValueAsString(FIELD_USER_NAME);
		String password = loginForm.getValueAsString(FIELD_PASSWORD);
		if (userId == null || userId.length() == 0) {
			reportError(MESSAGES.tokenRequestNoUserName());
			return;
		}
		if (password == null || password.length() == 0) {
			reportError(MESSAGES.tokenRequestNoPassword());
			return;
		}
		SsecAccess.login(userId, password, null);
	}

	/**
	 * ClickHandler for the login button.
	 * 
	 * @author Pieter De Graef
	 */
	private class TokenRequestLoginClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			login();
		}
	}

	/**
	 * ClickHandler for the reset button.
	 * 
	 * @author Pieter De Graef
	 */
	private class TokenRequestResetClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			loginForm.setValue(FIELD_USER_NAME, "");
			loginForm.setValue(FIELD_PASSWORD, "");
		}
	}
}
