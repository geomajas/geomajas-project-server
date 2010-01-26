package org.geomajas.internal.filter;

import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.springframework.util.Assert;

public class CqlTest {

	@Test
	public void toFilter() {
		Filter f;
		try {
			f = CQL.toFilter("a.b < 5");
		} catch (CQLException e) {
			f = null;
		}
		Assert.notNull(f, "Filter should not be null");

//		try {
//	        f = CQL.toFilter("bookstore[1].book < 5");
//        } catch (CQLException e) {
//        	f = null;
//        }
//		Assert.notNull(f, "Filter should not be null");
	}
}
