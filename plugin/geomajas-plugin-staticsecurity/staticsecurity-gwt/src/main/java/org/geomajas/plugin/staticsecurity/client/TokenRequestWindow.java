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

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.KeyNames;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.HTMLFlow;
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
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.gwt.client.util.HtmlBuilder;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.staticsecurity.client.util.SsecAccess;
import org.geomajas.plugin.staticsecurity.client.util.SsecLayout;

/**
 * <p>
 * Window used for logging in. Display a simple form for logging in, and 2 buttons. One buttons effectively logs you in,
 * while the other allows you to reset the form. When logging in not all attempts are successful. You can pass a
 * {@link TokenChangedHandler} which is called when the login was successful (specific to this window).
 * </p>
 * <p>
 * Note that the login window cannot be cancelled. You have to pass valid credentials for the window to disappear.
 * </p>
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api
public class TokenRequestWindow extends Window implements TokenChangedHandler {

	public static final String STYLE_NAME_WINDOW = "tokenRequestWindow";
	public static final String STYLE_NAME_ERROR = "tokenRequestError";

	private static final StaticSecurityMessages MESSAGES = GWT.create(StaticSecurityMessages.class);
	private static final String FIELD_USER_NAME = "userName";
	private static final String FIELD_PASSWORD = "password";

	private TokenChangedHandler finishLoginHandler;
	private DynamicForm loginForm;
	private String slogan;
	private HTMLFlow errorWidget;
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
		errorWidget = new HTMLFlow();
		errorWidget.setStyleName(STYLE_NAME_ERROR);
		loginForm = new DynamicForm();

		setHeaderIcon(WidgetLayout.iconGeomajas, 16, 16);
		setTitle(MESSAGES.tokenRequestWindowTitle());
		setIsModal(true);
		setShowModalMask(true);
		setModalMaskOpacity(WidgetLayout.modalMaskOpacity);
		setAutoCenter(true);
		setAutoSize(true);
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setStyleName(STYLE_NAME_WINDOW);
	}

	/**
	 * Create the default login window with an extra {@link TokenChangedHandler} added to catch login events. Shows the
	 * login window with a default layout.
	 *
	 * @param tokenChangedHandler callback for when the token was modified
	 */
	@Api
	public TokenRequestWindow(TokenChangedHandler tokenChangedHandler) {
		this();
		finishLoginHandler = tokenChangedHandler;
	}

	/** {@inheritDoc} */
	public void onTokenChanged(TokenChangedEvent event) {
		String token = event.getToken();
		if (null != token && token.length() > 0) {
			if (finishLoginHandler != null) {
				finishLoginHandler.onTokenChanged(event);
			}
			destroy();
		} else {
			String msg;
			if (1 == ++loginAttempt) {
				msg = MESSAGES.tokenRequestRetry();
			} else {
				msg = MESSAGES.tokenRequestRetryAgain(loginAttempt);
			}
			reportError(msg);
		}
	}

	/**
	 * Set a new slogan for this window. If null, no slogan will be displayed (that is also the default value).
	 * 
	 * @param slogan slogan
	 */
	@Api
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	@Override
	public void onDraw() {
		buildWidget();
		// try to force to be inside the screen
		if (SsecLayout.tokenRequestWindowKeepInScreen) {
			WidgetLayout.keepWindowInScreen(this);
		}
		super.onDraw();
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected void reportError(String error) {
		errorWidget.setContents(HtmlBuilder.divStyle(SsecLayout.tokenRequestWindowErrorStyle, error));
	}

	private void buildWidget() {

		VLayout layout = new VLayout();
		layout.setWidth(SsecLayout.tokenRequestWindowWidth);
		layout.setHeight(SsecLayout.tokenRequestWindowHeight);
		if (null != SsecLayout.tokenRequestWindowBackground) {
			layout.setBackgroundImage(SsecLayout.tokenRequestWindowBackground);
		}
		layout.setLayoutAlign(Alignment.CENTER);
		layout.setMembersMargin(WidgetLayout.marginLarge);
		layout.setPadding(WidgetLayout.marginLarge);

		Img logoImg = new Img(SsecLayout.tokenRequestWindowLogo);
		logoImg.setWidth(SsecLayout.tokenRequestWindowLogoWidth);
		logoImg.setHeight(SsecLayout.tokenRequestWindowLogoHeight);
		logoImg.setLayoutAlign(Alignment.CENTER);
		logoImg.setLayoutAlign(VerticalAlignment.CENTER);
		layout.addMember(logoImg);

		if (slogan != null && slogan.length() > 0) {
			Label titleLabel = new Label(slogan);
			titleLabel.setWidth(SsecLayout.tokenRequestWindowLogoWidth);
			titleLabel.setHeight(SsecLayout.tokenRequestWindowSloganHeight);
			titleLabel.setLayoutAlign(Alignment.CENTER);
			titleLabel.setAlign(Alignment.CENTER);
			layout.addMember(titleLabel);
		}

		// User name:
		TextItem userNameItem = new TextItem(FIELD_USER_NAME);
		userNameItem.setWidth(SsecLayout.tokenRequestWindowFieldWidth);
		userNameItem.setTitle(MESSAGES.tokenRequestUserId());
		userNameItem.setSelectOnFocus(true);
		userNameItem.setWrapTitle(false);

		// Password:
		PasswordItem passwordItem = new PasswordItem(FIELD_PASSWORD);
		passwordItem.setWidth(SsecLayout.tokenRequestWindowFieldWidth);
		passwordItem.setTitle(MESSAGES.tokenRequestPassword());
		passwordItem.setWrapTitle(false);

		// Login form:
		loginForm.setAutoFocus(true);
		loginForm.setNumCols(2);
		loginForm.setWidth(SsecLayout.tokenRequestWindowLogoWidth);
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
		IButton loginButton = new IButton(MESSAGES.tokenRequestButtonLogin());
		loginButton.setWidth(SsecLayout.tokenRequestWindowButtonWidth);
		loginButton.setIcon(SsecLayout.iconLogin);
		loginButton.addClickHandler(new TokenRequestLoginClickHandler());
		loginButton.setLayoutAlign(Alignment.RIGHT);

		// Reset button:
		IButton resetButton = new IButton(MESSAGES.tokenRequestButtonReset());
		resetButton.setWidth(SsecLayout.tokenRequestWindowButtonWidth);
		resetButton.setIcon(WidgetLayout.iconUndo);
		resetButton.addClickHandler(new TokenRequestResetClickHandler());
		resetButton.setLayoutAlign(Alignment.LEFT);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setHeight(30);
		buttonLayout.setWidth(SsecLayout.tokenRequestWindowLogoWidth);
		buttonLayout.setMembersMargin(WidgetLayout.marginSmall);
		buttonLayout.setLayoutAlign(Alignment.CENTER);

		VLayout loginBtnLayout = new VLayout();
		loginBtnLayout.setWidth(SsecLayout.tokenRequestWindowButtonLayoutWidth);
		loginBtnLayout.addMember(loginButton);
		buttonLayout.addMember(loginBtnLayout);

		VLayout resetBtnLayout = new VLayout();
		resetBtnLayout.setWidth(SsecLayout.tokenRequestWindowButtonLayoutWidth);
		resetBtnLayout.addMember(resetButton);
		buttonLayout.addMember(resetBtnLayout);

		layout.addMember(buttonLayout);
		layout.addMember(new LayoutSpacer());

		// Error label:
		errorWidget.setWidth(SsecLayout.tokenRequestWindowLogoWidth);
		errorWidget.setHeight(SsecLayout.tokenRequestWindowErrorHeight);
		errorWidget.setLayoutAlign(Alignment.CENTER);
		errorWidget.setAlign(Alignment.CENTER);
		layout.addMember(errorWidget);

		addItem(layout);
	}

	private void login() {
		String userId = loginForm.getValueAsString(FIELD_USER_NAME);
		String password = loginForm.getValueAsString(FIELD_PASSWORD);
		if (userId == null || userId.length() == 0) {
			reportError(MESSAGES.tokenRequestNoUserName());
		} else if (password == null || password.length() == 0) {
			reportError(MESSAGES.tokenRequestNoPassword());
		} else {
			reportError(MESSAGES.tokenLoggingIn());
			SsecAccess.login(userId, password, this);
		}
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
			reportError("");
		}
	}
}
