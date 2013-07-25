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
package org.geomajas.puregwt.widget.client.dialog;

import org.geomajas.annotation.Api;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Client resource bundle interface for pure GWT Messagebox widgets.
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface MessageBoxResource extends ClientBundle {

	/**
	 * Instance for use outside UIBinder.
	 */
	MessageBoxResource INSTANCE = GWT.create(MessageBoxResource.class);

	/**
	 * Get the css resource.
	 * @return the css resource
	 */
	@Source("messageBox.css")
	MessageBoxCssResource css();

	/**
	 * Image sprite.
	 * @return
	 */
	@Source("images/dialog-error.png")
	@ImageOptions(repeatStyle = RepeatStyle.None, width = 48, height = 48)
	ImageResource messageBoxErrorIcon();

	/**
	 * Image sprite.
	 * @return
	 */
	@Source("images/dialog-help.png")
	@ImageOptions(repeatStyle = RepeatStyle.None, width = 48, height = 48)
	ImageResource messageBoxHelpIcon();

	/**
	 * Image sprite.
	 * @return
	 */
	@Source("images/dialog-information.png")
	@ImageOptions(repeatStyle = RepeatStyle.None, width = 48, height = 48)
	ImageResource messageBoxInfoIcon();

	/**
	 * Image sprite.
	 * @return
	 */
	@Source("images/dialog-warning.png")
	@ImageOptions(repeatStyle = RepeatStyle.None, width = 48, height = 48)
	ImageResource messageBoxWarnIcon();
}
