package org.geomajas.gwt.client.gfx.paintable;

import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.spatial.Bbox;
import org.junit.Assert;
import org.junit.Test;

public class CompositeTest {

	@Test
	public void testPaintableInterface() throws Exception {
		// visit should be called on all children

		Composite c = new Composite();
		MockPaintable r = new MockPaintable();
		c.addChild(r);

		PainterVisitor pv = new PainterVisitor(null);
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
