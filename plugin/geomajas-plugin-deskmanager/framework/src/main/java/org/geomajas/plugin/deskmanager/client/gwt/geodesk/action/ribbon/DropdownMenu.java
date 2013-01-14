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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.action.ribbon;

import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Page containing all available searchmethods.
 * 
 * @author Kristof Heirwegh
 */
public class DropdownMenu extends VLayout {

	private static final String BACKGROUND_IMG = "[ISOMORPHIC]/geomajas/widget/themewidget/background.png";

	public DropdownMenu(int membersMargin) {
		super(5);
		setPadding(5);
		setBackgroundImage(BACKGROUND_IMG);
		setShowShadow(true);
		setOverflow(Overflow.HIDDEN);
		setKeepInParentRect(true);
		addMouseOutHandler(new MouseOutHandler() {

			public void onMouseOut(MouseOutEvent event) {
				Rectangle rect = DropdownMenu.this.getRect();
				int x = event.getX();
				int y = event.getY();
				if (x < rect.getLeft() || x > rect.getWidth() + rect.getLeft() || y < rect.getTop()
						|| y > rect.getTop() + rect.getHeight()) {
					DropdownMenu.this.animateHide(AnimationEffect.SLIDE);
				}
			}
		});
	}

	// ----------------------------------------------------------
	// -- builder methods
	// ----------------------------------------------------------

	public Canvas createCategory(String title) {
		Label cat = new Label(title);
		cat.setStyleName("searchPageCategory");
		cat.setWidth100();
		cat.setHeight(20);
		return cat;
	}

	// ----------------------------------------------------------

	/**
	 * TODO.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public static class MenuItem extends HLayout {

		private static final String DESCRIPTION_HOVER_STYLENAME = "themeWidgetDescriptionHover";

		private static final String DESCRIPTION_STYLENAME = "themeWidgetDescription";

		private static final int IMAGE_SIZE = 48;

		public MenuItem(String imageSrc, String title, String description, ClickHandler handler) {
			super(5);
			setPadding(5);
			setStyleName(DESCRIPTION_STYLENAME);
			setHeight(IMAGE_SIZE + 10);
			addClickHandler(handler);
			setCursor(Cursor.HAND);

			// -- image
			Img img = new Img(imageSrc, IMAGE_SIZE, IMAGE_SIZE);
			img.setCursor(Cursor.HAND);
			img.setImageHeight(IMAGE_SIZE);
			img.setImageWidth(IMAGE_SIZE);
			addMember(img);

			// -- description
			Label desc = new Label("<b>" + title + "</b><br />" + description);
			desc.setCursor(Cursor.HAND);
			desc.setBackgroundColor("transparent");
			desc.setWidth100();
			desc.setHeight100();
			desc.setBackgroundColor("transparent");
			desc.setValign(VerticalAlignment.CENTER);
			addMember(desc);

			// -- events
			MouseOverHandler movh = new MouseOverHandler() {

				public void onMouseOver(MouseOverEvent event) {
					setStyleName(DESCRIPTION_HOVER_STYLENAME);
				}
			};
			MouseOutHandler mouh = new MouseOutHandler() {

				public void onMouseOut(MouseOutEvent event) {
					setStyleName(DESCRIPTION_STYLENAME);
				}
			};

			addMouseOverHandler(movh);
			img.addMouseOverHandler(movh);
			desc.addMouseOverHandler(movh);

			addMouseOutHandler(mouh);
			img.addMouseOutHandler(mouh);
			desc.addMouseOutHandler(mouh);
		}
	}
}
