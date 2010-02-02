package org.geomajas.layermodel.shapeinmem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class ShapeInMemFilterTest extends AbstractFilterTest {

	@Test
	public void betweenFilterOnInteger() {
		Filter intFilter = filterCreator.createBetweenFilter(PARAM_INT_ATTR, "2", "8");
		try {
			Iterator<?> it = layerModel.getElements(intFilter);
			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(2, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void betweenFilterOnFloat() {
		Filter filter = filterCreator.createBetweenFilter(PARAM_FLOAT_ATTR, "2", "8");
		try {
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(2, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void betweenFilterOnDouble() {
		Filter filter = filterCreator.createBetweenFilter(PARAM_DOUBLE_ATTR, "2", "8");
		try {
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(2, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnInteger() {
		Filter lt = filterCreator.createCompareFilter(PARAM_INT_ATTR, "<", "15");
		try {
			Iterator<?> it = layerModel.getElements(lt);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
		Filter ne = filterCreator.createCompareFilter(PARAM_INT_ATTR, "<>", "10");
		try {
			Iterator<?> it = layerModel.getElements(ne);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(7, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnFloat() {
		Filter lt = filterCreator.createCompareFilter(PARAM_FLOAT_ATTR, "<", "15");
		try {
			Iterator<?> it = layerModel.getElements(lt);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
		Filter ne = filterCreator.createCompareFilter(PARAM_FLOAT_ATTR, "<>", "10");
		try {
			Iterator<?> it = layerModel.getElements(ne);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(7, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnDouble() {
		Filter lt = filterCreator.createCompareFilter(PARAM_DOUBLE_ATTR, "<", "15");
		try {
			Iterator<?> it = layerModel.getElements(lt);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
		Filter ne = filterCreator.createCompareFilter(PARAM_DOUBLE_ATTR, "<>", "10");
		try {
			Iterator<?> it = layerModel.getElements(ne);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(7, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnDate() {
		DateFormat format = new SimpleDateFormat("dd/mm/yyyy");
		Date d1;
		try {
			d1 = format.parse("12/12/2005");
		} catch (ParseException e1) {
			d1 = new Date();
		}

		Filter filter = filterCreator.createCompareFilter(PARAM_DATE_ATTR, "<", d1);
		try {
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnBoolean() {
		Filter eq = filterCreator.createCompareFilter(PARAM_BOOLEAN_ATTR, "==", "true");
		try {
			Iterator<?> it = layerModel.getElements(eq);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}

		Filter ne = filterCreator.createCompareFilter(PARAM_BOOLEAN_ATTR, "<>", "true");
		try {
			Iterator<?> it = layerModel.getElements(ne);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void compareFilterOnString() {
		Filter eq = filterCreator.createCompareFilter(PARAM_TEXT_ATTR, "==", "inside");
		try {
			Iterator<?> it = layerModel.getElements(eq);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(1, t);
		} catch (Exception e) {
		}

		Filter ne = filterCreator.createCompareFilter(PARAM_TEXT_ATTR, "<>", "inside");
		try {
			Iterator<?> it = layerModel.getElements(ne);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(7, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void likeFilter() {
		Filter filter = filterCreator.createLikeFilter(PARAM_TEXT_ATTR, "*sid*");
		try {
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(2, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void logicFilter() {
		Filter filter1 = filterCreator.createCompareFilter(PARAM_INT_ATTR, "<", "15");
		Filter filter2 = filterCreator.createLikeFilter(PARAM_TEXT_ATTR, "over*");
		Filter filter = filterCreator.createLogicFilter(filter1, "and", filter2);

		try {
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(1, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void fidFilter() {
		Filter filter = filterCreator.createFidFilter(new String[] {"1", "2"});
		try {
			Iterator<?> it = layerModel.getElements(filter);
			SimpleFeature f = (SimpleFeature) it.next();
			f = (SimpleFeature) it.next();
			Assert.assertEquals("inside", f.getAttribute(PARAM_TEXT_ATTR));
		} catch (Exception e) {
		}
	}

	@Test
	public void containsFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layerModel.read("1");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterCreator.createContainsFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(3, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void withinFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layerModel.read("7");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterCreator.createWithinFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(4, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void intersectsFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layerModel.read("7");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterCreator.createIntersectsFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(6, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void touchesFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layerModel.read("7");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterCreator.createTouchesFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(1, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void bboxFilter() {
		try {
			Envelope bbox = new Envelope(-0.4d, -0.3d, -0.2d, 0.1d);
			Filter filter = filterCreator.createBboxFilter("EPSG:4326", bbox, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(3, t);
		} catch (Exception e) {
		}
	}

	@Test
	public void overlapsFilter() {
		try {
			SimpleFeature feature = (SimpleFeature) layerModel.read("2");
			Geometry geom = (Geometry) feature.getDefaultGeometry();
			Filter filter = filterCreator.createOverlapsFilter(geom, PARAM_GEOMETRY_ATTR);
			Iterator<?> it = layerModel.getElements(filter);

			int t = 0;
			while (it.hasNext()) {
				it.next();
				t++;
			}
			Assert.assertEquals(1, t);
		} catch (Exception e) {
		}
	}
}