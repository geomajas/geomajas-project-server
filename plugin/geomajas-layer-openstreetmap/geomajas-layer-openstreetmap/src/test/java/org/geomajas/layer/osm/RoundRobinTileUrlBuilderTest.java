package org.geomajas.layer.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.Assert;

import org.junit.Test;

public class RoundRobinTileUrlBuilderTest {

	@Test
	public void testBuildUrl() {
		RoundRobinTileUrlBuilder builder = new RoundRobinTileUrlBuilder();
		Assert.assertEquals("http://a.tile.openstreetmap.org/1/2/3.png", builder.buildUrl(1, 2, 3));
		Assert.assertEquals("http://b.tile.openstreetmap.org/4/5/6.png", builder.buildUrl(4, 5, 6));
		Assert.assertEquals("http://c.tile.openstreetmap.org/7/8/9.png", builder.buildUrl(7, 8, 9));
	}

	@Test
	public void testMultiThreading() throws InterruptedException, ExecutionException {
		// tests correct round robin iteration in each individual thread
		RoundRobinTileUrlBuilder builder = new RoundRobinTileUrlBuilder();
		ArrayList<String> urls = new ArrayList<String>();
		urls.add("a");
		urls.add("b");
		urls.add("c");
		builder.setBaseUrls(urls);
		ExecutorService service = Executors.newFixedThreadPool(10);
		List<Callable<List<String>>> tasks = new ArrayList<Callable<List<String>>>();
		for (int i = 0; i < 10; i++) {
			tasks.add(new TestTask(builder));
		}
		// we expect the same result as single-threaded for each thread
		List<String> expected = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			expected.add(builder.buildUrl(1, 2, 3));
		}
		
		List<Future<List<String>>> futures = service.invokeAll(tasks);
		for (Future<List<String>> future : futures) {
			Assert.assertEquals(expected, future.get());
		
		}
	}

	public class TestTask implements Callable<List<String>> {

		private TileUrlBuilder builder;

		public TestTask(TileUrlBuilder builder) {
			this.builder = builder;
		}

		public List<String> call() throws Exception {
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < 10; i++) {
				result.add(builder.buildUrl(1, 2, 3));
				Thread.sleep(50);
			}
			return result;
		}

	}
}
