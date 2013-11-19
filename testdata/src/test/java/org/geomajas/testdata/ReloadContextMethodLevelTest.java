package org.geomajas.testdata;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ ReloadContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/org/geomajas/testdata/reloadContext.xml" })
public class ReloadContextMethodLevelTest {

	@Autowired
	private ReloadBean testBean;
	
	@Test
	public void test1() {
		testBean.setDirty(true);
	}

	@ReloadContext
	@Test
	public void test2() {
		Assert.assertFalse(testBean.isDirty());
	}
	
	

}
