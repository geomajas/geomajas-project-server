/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.printing.component.impl;

import junit.framework.Assert;

import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.junit.Test;

public class BaseComponentImplTest {

	@Test
	public void testLayoutAbsolute() {
		AbsoluteComponent root = new AbsoluteComponent(100, 200, 0, 0);
		AbsoluteComponent level1 = new AbsoluteComponent(50, 60, 12, 13);
		AbsoluteComponent level2 = new AbsoluteComponent(80, 10, 7, 4);
		root.addComponent(level1);
		level1.addComponent(level2);
		root.calculateSize(null);
		root.layout(null);
		assertPosition(level1, 50, 60, 12, 13);
		assertPosition(level2, 80, 10, 7, 4);
	}

	@Test
	public void testLayoutRelative() {
		AbsoluteComponent root = new AbsoluteComponent(100, 200, 0, 0);
		BottomLeftComponent level1 = new BottomLeftComponent(50, 60, 12, 13);
		TopRightComponent level2 = new TopRightComponent(80, 10, 7, 4);
		root.addComponent(level1);
		root.addComponent(level2);
		root.calculateSize(null);
		root.layout(null);
		assertPosition(level1, 50, 60, 12, 13);
		assertPosition(level2, 80, 10, 13, 186);
	}

	@Test
	public void testLayoutJustified() {
		AbsoluteComponent root = new AbsoluteComponent(100, 200, 0, 0);
		FullWidthAbsoluteHeightComponent level1 = new FullWidthAbsoluteHeightComponent(60, 0, 0);
		FullHeightAbsoluteWidthComponent level2 = new FullHeightAbsoluteWidthComponent(80, 0, 0);
		root.addComponent(level1);
		root.addComponent(level2);
		root.calculateSize(null);
		root.layout(null);
		assertPosition(level1, 100, 60, 0, 0);
		assertPosition(level2, 80, 200, 0, 0);
	}

	private void assertPosition(PrintComponent component, float width, float height, float left, float bottom) {
		Assert.assertEquals(width, component.getBounds().getWidth());
		Assert.assertEquals(height, component.getBounds().getHeight());
		Assert.assertEquals(left, component.getBounds().getLeft());
		Assert.assertEquals(bottom, component.getBounds().getBottom());
	}

	// some components to play around with

	public class BaseComponent extends AbstractPrintComponent {

		public void accept(PrintComponentVisitor visitor) {
			// not needed
		}
	}

	public class BottomLeftComponent extends BaseComponent {

		public BottomLeftComponent(float width, float height, float marginX, float marginY) {
			getConstraint().setMarginX(marginX);
			getConstraint().setMarginY(marginY);
			getConstraint().setAlignmentX(LayoutConstraint.LEFT);
			getConstraint().setAlignmentY(LayoutConstraint.BOTTOM);
			getConstraint().setWidth(width);
			getConstraint().setHeight(height);
		}
	}

	class TopRightComponent extends BaseComponent {

		public TopRightComponent(float width, float height, float marginX, float marginY) {
			getConstraint().setMarginX(marginX);
			getConstraint().setMarginY(marginY);
			getConstraint().setAlignmentX(LayoutConstraint.RIGHT);
			getConstraint().setAlignmentY(LayoutConstraint.TOP);
			getConstraint().setWidth(width);
			getConstraint().setHeight(height);
		}
	}

	class AbsoluteComponent extends BaseComponent {

		public AbsoluteComponent(float width, float height, float marginX, float marginY) {
			getConstraint().setMarginX(marginX);
			getConstraint().setMarginY(marginY);
			getConstraint().setAlignmentX(LayoutConstraint.ABSOLUTE);
			getConstraint().setAlignmentY(LayoutConstraint.ABSOLUTE);
			getConstraint().setWidth(width);
			getConstraint().setHeight(height);
		}
	}

	class FullWidthAbsoluteHeightComponent extends BaseComponent {

		public FullWidthAbsoluteHeightComponent(float height, float marginX, float marginY) {
			getConstraint().setMarginX(marginX);
			getConstraint().setMarginY(marginY);
			getConstraint().setAlignmentX(LayoutConstraint.JUSTIFIED);
			getConstraint().setAlignmentY(LayoutConstraint.ABSOLUTE);
			getConstraint().setHeight(height);
		}
	}

	class FullHeightAbsoluteWidthComponent extends BaseComponent {

		public FullHeightAbsoluteWidthComponent(float width, float marginX, float marginY) {
			getConstraint().setMarginX(marginX);
			getConstraint().setMarginY(marginY);
			getConstraint().setAlignmentX(LayoutConstraint.ABSOLUTE);
			getConstraint().setAlignmentY(LayoutConstraint.JUSTIFIED);
			getConstraint().setWidth(width);
		}
	}

}
