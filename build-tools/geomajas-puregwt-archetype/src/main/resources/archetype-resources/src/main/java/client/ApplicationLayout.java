#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.client;

import org.geomajas.puregwt.client.GeomajasGinjector;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Application layout.
 * 
 * @author Pieter De Graef
 */
public class ApplicationLayout extends Composite {

	/**
	 * UI binder interface for the application layout.
	 * 
	 * @author Pieter De Graef
	 */
	interface ApplicationLayoutUiBinder extends UiBinder<Widget, ApplicationLayout> {
	}

	private static final ApplicationLayoutUiBinder UI_BINDER = GWT.create(ApplicationLayoutUiBinder.class);

	private static final GeomajasGinjector INJECTOR = GWT.create(GeomajasGinjector.class);

	private final MapPresenter mapPresenter;

	@UiField
	protected SimplePanel contentPanel;

	public ApplicationLayout() {
		initWidget(UI_BINDER.createAndBindUi(this));
		mapPresenter = INJECTOR.getMapPresenter();
		mapPresenter.initialize("app", "mapOsm");
		mapPresenter.getMapRenderer().setAnimationMillis(300);
		ResizableMapLayout mapLayout = new ResizableMapLayout(mapPresenter);
		contentPanel.setWidget(mapLayout);
	}
}