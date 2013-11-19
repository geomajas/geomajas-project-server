package org.geomajas.testdata;

import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class TestPathBinaryStreamAssertTest extends TestPathBinaryStreamAssert {

	public TestPathBinaryStreamAssertTest() {
		super("org/geomajas/testdata");
	}

	@Test
	public void testAssertEqual() throws Exception {
		assertEqual("test.txt", false);
	}

	@Test
	public void testAssertEqualAssertionError() {
		try {
			assertEqual("test2.txt", false);
			Assert.fail("expected assertion error for non-equal streams");
		} catch (AssertionError e) {
		} catch (Exception e) {
			Assert.fail("expected assertion error for non-equal streams");
		}
	}

	@Test
	public void testAssertEqualFailure() {
		try {
			assertEqual("test3.txt", false);
			Assert.fail("expected failure for non-existing file");
		} catch (AssertionError e) {
			Assert.fail("expected failure for non-existing file");
		} catch (Exception e) {
		}
	}

	@Override
	public void generateActual(OutputStream out) throws Exception {
		out.write("test".getBytes("UTF8"));
	}
}
