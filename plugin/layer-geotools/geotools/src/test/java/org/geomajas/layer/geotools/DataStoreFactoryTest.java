package org.geomajas.layer.geotools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.geomajas.layer.geotools.DummyJdbcFactory.DummyJdbcDataStore;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO This testclass has very little use left, might want to remove it.
 * <li> typed/untyped is no longer supported.
 * <li> can't extend jdbcdatastore anymore, testing for dummystore is not very useful.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/geotools/dataStoreFactory.xml" })
public class DataStoreFactoryTest {

	@Autowired
	@Qualifier("ngDataStore")
	DataStore ngDataStore;

	@Test
	public void testNextGen() {
		// ignores useTypedFids, should log warning
		Assert.assertFalse(ngDataStore instanceof DummyJdbcDataStore);
	}

	public static void main(String[] args) throws IOException {
	    /**
	     * Optional - enable/disable the use of memory-mapped io
	     */
	    Param test = new Param("test",
	            Boolean.class, "enable/disable the use of memory-mapped io", false);
	    Map<String,Object> map = new HashMap<String,Object>();
	    map.put("test", test);
	    Boolean b = (Boolean)test.lookUp(map);
	    System.out.println(b);

	}
}
