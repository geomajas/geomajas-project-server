package org.geomajas.layer.geotools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/geotools/dataStoreFactory.xml" })
public class DataStoreFactoryTest {

	@Autowired
	@Qualifier("typedDataStore")
	DataStore typed;

	@Autowired
	@Qualifier("nonTypedDataStore")
	DataStore nonTyped;

	@Autowired
	@Qualifier("ngDataStore")
	DataStore ngDataStore;

	@Test
	public void testTyped() {
		Assert.assertTrue(typed instanceof JDBCDataStore);
		FIDMapperFactory factory = ((JDBCDataStore) typed).getFIDMapperFactory();
		Assert.assertTrue(factory instanceof DefaultFIDMapperFactory);
		Assert.assertTrue(((DefaultFIDMapperFactory) factory).isReturningTypedFIDMapper());

	}

	@Test
	public void testNonTyped() {
		Assert.assertTrue(nonTyped instanceof JDBCDataStore);
		FIDMapperFactory factory = ((JDBCDataStore) nonTyped).getFIDMapperFactory();
		Assert.assertTrue(factory instanceof DefaultFIDMapperFactory);
		Assert.assertFalse(((DefaultFIDMapperFactory) factory).isReturningTypedFIDMapper());
	}

	@Test
	public void testNextGen() {
		// ignores useTypedFids, should log warning
		Assert.assertFalse(ngDataStore instanceof JDBCDataStore);
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
