package org.geomajas.plugin.rasterizing.cachetest;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.TileMetadataImpl;
import org.geomajas.internal.service.pipeline.PipelineContextImpl;
import org.geomajas.layer.Layer;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.caching.infinispan.cache.InfinispanCacheFactory;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContextImpl;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.plugin.caching.service.CacheServiceInfo;
import org.geomajas.plugin.caching.service.CachingSupportService;
import org.geomajas.plugin.caching.service.CachingSupportServiceSecurityContextAdder;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.infinispan.manager.DefaultCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml" })
public class CacheTest {

//	@Autowired
//	private CacheManagerService cacheManagerService;
//
//	@Autowired
//	private CacheServiceInfo cacheServiceInfo;
//
//	@Autowired
//	private CachingSupportService cachingSupportService;
//
//	@Autowired
//	private CachingSupportServiceSecurityContextAdder securityContextAdder;
//
//	private static final String[] KEYS = { PipelineCode.LAYER_ID_KEY, PipelineCode.TILE_METADATA_KEY };
//
//	private Layer[] layers = new Layer[10];
//
//	private Random random = new Random();
//
//	private DefaultCacheManager manager;
//
//	private ExecutorService executor;
//
//	@Before
//	public void setUp() {
//		for (int i = 0; i < 10; i++) {
//			layers[i] = new DummyLayer(i);
//		}
//		InfinispanCacheFactory f = (InfinispanCacheFactory) cacheServiceInfo.getCacheFactory();
////		manager = f.getCacheManager();
//	}
//	
//	@Test
//	public void testWrite() {
//		for (int i = 0; i < 10000; i++) {
//			manager.getCache("$raster$defaultInfinispanCacheConfig").put(UUID.randomUUID().toString(), createImage());
//		}
//	}
//
//	@Test
//	public void testStartFillStopCycle() throws InterruptedException {
//
//		executor = Executors.newFixedThreadPool(1);
//		for (int i = 0; i < 500000; i++) {
//			executor.execute(new Putter(random.nextInt(5000)));
//		}
//		executor.awaitTermination(1000, TimeUnit.SECONDS);
//		System.out.println("Done");
//	}
//
//	private byte[] createImage() {
//		byte[] bb = new byte[500 * 500];
//		for (int i = 0; i < 500 * 500; i++) {
//			bb[i] = (byte) random.nextInt();
//		}
//		return bb;
//	}
//
//	class DummyLayer implements Layer {
//
//		private int id;
//
//		public DummyLayer(int id) {
//			super();
//			this.id = id;
//		}
//
//		@Override
//		public LayerInfo getLayerInfo() {
//			return null;
//		}
//
//		@Override
//		public CoordinateReferenceSystem getCrs() {
//			return null;
//		}
//
//		@Override
//		public String getId() {
//			return id + "";
//		}
//	}
//
//	class Putter implements Runnable {
//
//		int i;
//
//		private int layerId;
//
//		public Putter(int i) {
//			this.i = i;
//			layerId = random.nextInt(10);
//		}
//
//		@Override
//		public void run() {
//			System.out.println(Thread.currentThread().getName()+"put "+layerId+"/img" + i);
//			RasterizingContainer rc = new RasterizingContainer();
//			rc.setImage(createImage());
//			rc.setContext(new CacheContextImpl());
//			PipelineContext pipelineContext = new PipelineContextImpl();
//			pipelineContext.put(PipelineCode.LAYER_ID_KEY, layerId + "");
//			TileMetadataImpl metadata = new TileMetadataImpl();
//			metadata.setCode(new TileCode(0, 1, 2));
//			metadata.setPanOrigin(new Coordinate());
//			pipelineContext.put(PipelineCode.TILE_METADATA_KEY, new GetVectorTileRequest(metadata));
//			cachingSupportService.putContainer(pipelineContext, securityContextAdder, CacheCategory.RASTER, KEYS,
//					RasterizingPipelineCode.IMAGE_ID_KEY, RasterizingPipelineCode.IMAGE_ID_CONTEXT, rc, new Envelope(0,
//							1, 0, 1));
//
//			try {
//				new Getter(layerId, (String) pipelineContext.get(RasterizingPipelineCode.IMAGE_ID_KEY)).run();;
//			} catch (GeomajasException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
//	class Getter implements Runnable {
//
//		String key;
//
//		private int layerId;
//
//		public Getter(int layerId, String key) {
//			this.key = key;
//			this.layerId = layerId;
//		}
//
//		@Override
//		public void run() {
//			Object o = cacheManagerService.get(layers[layerId], CacheCategory.RASTER, key);
//		}
//
//	}
}
