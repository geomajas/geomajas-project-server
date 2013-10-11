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

package org.geomajas.gwt.client.gfx.paintable;

import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.ImageContext;
import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.event.GraphicsReadyHandler;
import org.junit.Assert;
import org.junit.Test;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class CompositeTest {

	@Test
	public void testPaintableInterface() throws Exception {
		// visit should be called on all children

		Composite c = new Composite();
		MockPaintable r = new MockPaintable();
		c.addChild(r);

		PainterVisitor pv = new PainterVisitor(new MapContext(){

			public MenuContext getMenuContext() {
				return null;
			}

			public ImageContext getRasterContext() {
				return null;
			}

			public GraphicsContext getVectorContext() {
				return null;
			}

			public boolean isReady() {
				return true;
			}

			public HandlerRegistration addGraphicsReadyHandler(GraphicsReadyHandler handler) {
				return null;
			}

			public void fireEvent(GwtEvent<?> event) {
			}});
		pv.registerPainter(new MockPainter());
		c.accept(pv, null, null, false);
		Assert.assertTrue("should have accepted visitor", r.visitorAccepted);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCircularRef() throws Exception {
		// make sure we can't get into neverending loops
		Composite c = new Composite();
		Composite sub = new Composite();
		sub.addChild(c);
		c.addChild(sub);
	}

	// ----------------------------------------------------------

	class MockPaintable implements Paintable {

		public String id;
		public boolean visitorAccepted = false;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
			visitorAccepted = true;
		}
	}

	class MockPainter implements Painter {

		public void deleteShape(Paintable paintable, Object group, MapContext context) {
		}

		public String getPaintableClassName() {
			return MockPaintable.class.getName();
		}

		public void paint(Paintable paintable, Object group, MapContext context) {
			((MockPaintable) paintable).visitorAccepted = true;
		}
	}
}
