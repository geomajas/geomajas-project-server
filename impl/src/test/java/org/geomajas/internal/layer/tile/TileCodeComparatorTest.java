/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.layer.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.geomajas.layer.tile.TileCode;
import org.junit.Assert;
import org.junit.Test;


/**
 * Test the tilecode comparator.
 * 
 * @author Oliver May
 *
 */
public class TileCodeComparatorTest {

	Random rg = new Random(342);
	
	@Test
	public void testTileCodeComparator7on5() {
		List<TileCode> list = createTileCodes(0, 0, 7, 5); 
		TileCodeComparator tcc = new TileCodeComparator(3, 2);
		
		Collections.sort(list, tcc);
		
		Assert.assertArrayEquals(new TileCode[] {
				new TileCode(0, 3, 2), 
				
				new TileCode(0, 2, 3),  // maybe not 2,3 but 3,3
				new TileCode(0, 3, 3), 
				new TileCode(0, 4, 3), 
				new TileCode(0, 4, 2), 
				new TileCode(0, 4, 1), 
				new TileCode(0, 3, 1), 
				new TileCode(0, 2, 1), 
				new TileCode(0, 2, 2), 

				new TileCode(0, 1, 4), // maybe not 1,4 but 2,4
				new TileCode(0, 2, 4), 
				new TileCode(0, 3, 4), 
				new TileCode(0, 4, 4), 
				new TileCode(0, 5, 4), 
				new TileCode(0, 5, 3), 
				new TileCode(0, 5, 2), 
				new TileCode(0, 5, 1), 
				new TileCode(0, 5, 0), 
				new TileCode(0, 4, 0), 
				new TileCode(0, 3, 0), 
				new TileCode(0, 2, 0), 
				new TileCode(0, 1, 0), 
				new TileCode(0, 1, 1), 
				new TileCode(0, 1, 2), 
				new TileCode(0, 1, 3), 

				new TileCode(0, 6, 4), 
				new TileCode(0, 6, 3), 
				new TileCode(0, 6, 2), 
				new TileCode(0, 6, 1), 
				new TileCode(0, 6, 0), 
				new TileCode(0, 0, 0), 
				new TileCode(0, 0, 1), 
				new TileCode(0, 0, 2), 
				new TileCode(0, 0, 3), 
				new TileCode(0, 0, 4) 
				}, list.toArray(new TileCode[0]));
	}
	
	@Test
	public void testTileCodeComparator3on3() {
		List<TileCode> list = createTileCodes(0, 0, 3, 3); 
		TileCodeComparator tcc = new TileCodeComparator(1, 1);
		
		Collections.sort(list, tcc);
		
		Assert.assertArrayEquals(new TileCode[] {
				new TileCode(0, 1, 1), 
				
				new TileCode(0, 0, 2),  // maybe not 1,2 but 0,2
				new TileCode(0, 1, 2), 
				new TileCode(0, 2, 2), 
				new TileCode(0, 2, 1), 
				new TileCode(0, 2, 0), 
				new TileCode(0, 1, 0), 
				new TileCode(0, 0, 0), 
				new TileCode(0, 0, 1), 

				}, list.toArray(new TileCode[0]));
	}
	
	@Test
	public void testShuffeledTileCodeComparator7on5() {
		List<TileCode> list = createShuffeledTileCodes(0, 0, 7, 5); 
		TileCodeComparator tcc = new TileCodeComparator(3, 2);
		
		Collections.sort(list, tcc);
		
		Assert.assertArrayEquals(new TileCode[] {
				new TileCode(0, 3, 2), 
				
				new TileCode(0, 2, 3),  // maybe not 2,3 but 3,3
				new TileCode(0, 3, 3), 
				new TileCode(0, 4, 3), 
				new TileCode(0, 4, 2), 
				new TileCode(0, 4, 1), 
				new TileCode(0, 3, 1), 
				new TileCode(0, 2, 1), 
				new TileCode(0, 2, 2), 

				new TileCode(0, 1, 4), // maybe not 1,4 but 2,4
				new TileCode(0, 2, 4), 
				new TileCode(0, 3, 4), 
				new TileCode(0, 4, 4), 
				new TileCode(0, 5, 4), 
				new TileCode(0, 5, 3), 
				new TileCode(0, 5, 2), 
				new TileCode(0, 5, 1), 
				new TileCode(0, 5, 0), 
				new TileCode(0, 4, 0), 
				new TileCode(0, 3, 0), 
				new TileCode(0, 2, 0), 
				new TileCode(0, 1, 0), 
				new TileCode(0, 1, 1), 
				new TileCode(0, 1, 2), 
				new TileCode(0, 1, 3), 

				new TileCode(0, 6, 4), 
				new TileCode(0, 6, 3), 
				new TileCode(0, 6, 2), 
				new TileCode(0, 6, 1), 
				new TileCode(0, 6, 0), 
				new TileCode(0, 0, 0), 
				new TileCode(0, 0, 1), 
				new TileCode(0, 0, 2), 
				new TileCode(0, 0, 3), 
				new TileCode(0, 0, 4) 
				}, list.toArray(new TileCode[0]));
	}
	
	@Test
	public void testShuffeledTileCodeComparator3on3() {
		List<TileCode> list = createShuffeledTileCodes(0, 0, 3, 3); 
		TileCodeComparator tcc = new TileCodeComparator(1, 1);
		
		Collections.sort(list, tcc);
		
		Assert.assertArrayEquals(new TileCode[] {
				new TileCode(0, 1, 1), 
				
				new TileCode(0, 0, 2),  // maybe not 1,2 but 0,2
				new TileCode(0, 1, 2), 
				new TileCode(0, 2, 2), 
				new TileCode(0, 2, 1), 
				new TileCode(0, 2, 0), 
				new TileCode(0, 1, 0), 
				new TileCode(0, 0, 0), 
				new TileCode(0, 0, 1), 

				}, list.toArray(new TileCode[0]));
	}
	
	@Test
	public void testTileCodeComparatorSymmetry() {
		List<TileCode> list = createTileCodes(0, 0, 3, 3); 
		TileCodeComparator comp = new TileCodeComparator(1, 1);
		for (TileCode code1 : list) {
			for (TileCode code2 : list) {
				int left = comp.compare(code1, code2);
				int right = - comp.compare(code2, code1);
				if (left != right) {
					System.out.println(code1 + " " + code2);
				}
				Assert.assertEquals(left, right);
			}
		}
	}
	
	/**
	 * Create a shuffeled tile codes collection with the given dimensions.
	 * 
	 * @param offsetX the x offset 
	 * @param offsetY the y offset
	 * @param width the with
	 * @param height thw height
	 * @return the shuffeled collection.
	 */
	private List<TileCode> createShuffeledTileCodes(int offsetX, int offsetY, int width, int height) {
		List<TileCode> codes = createTileCodes(offsetX, offsetY, width, height);
		Collections.shuffle(codes, rg);
		return codes;
	}
	
	/**
	 * Create a tile codes collection with the given dimensions.
	 * 
	 * @param offsetX the x offset 
	 * @param offsetY the y offset
	 * @param width the with
	 * @param height thw height
	 * @return the collection.
	 */
	private List<TileCode> createTileCodes(int offsetX, int offsetY, int width, int height) {
		List<TileCode> codes = new ArrayList<TileCode>(width * height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				codes.add(new TileCode(0, i + offsetX, j + offsetY));
			}
		}
		return codes;
	}
}
