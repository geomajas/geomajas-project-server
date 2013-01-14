/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.service;

import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.GeometryCriterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for DtoSearchConverterServiceImpl.
 * 
 * @author Kristof Heirwegh
 *
 */
public class DtoSearchConverterServiceImplTest {

	private static final String LA = "Laag a";
	private static final String LB = "Laag b";
	private static final String LC = "Laag c";
	private static final String ATTNAME = "blah";
	private static final String OPER = "=";
	private static final String VAL = "bliep";
	
	private final AttributeCriterion aCA = new AttributeCriterion(LA, ATTNAME, OPER, VAL);
	private final AttributeCriterion aCB = new AttributeCriterion(LB, ATTNAME, OPER, VAL);
	private final AttributeCriterion aCC = new AttributeCriterion(LC, ATTNAME, OPER, VAL);
	
	@Test
	public void testPruningBasic() throws Exception {
		DtoSearchConverterServiceImpl dscsi = new DtoSearchConverterServiceImpl();
		AndCriterion critter = new AndCriterion();
		
		// -- single attr, should be no change
		critter.getCriteria().add(aCA);
		dscsi.prune(critter);
		Assert.assertTrue("1", critter.getCriteria().size() == 1);
		
		// -- two attrs on same layer, should be no change	
		critter.getCriteria().add(aCA);
		dscsi.prune(critter);
		Assert.assertTrue("2", critter.getCriteria().size() == 2);

		// -- extra attr on different layer, all should be removed (nothing in common)	
		critter.getCriteria().add(aCB);
		dscsi.prune(critter);
		Assert.assertTrue("3", critter.getCriteria().size() == 0);
	}

	@Test
	public void testPruningCombined() throws Exception {
		DtoSearchConverterServiceImpl dscsi = new DtoSearchConverterServiceImpl();
		AndCriterion critter = new AndCriterion();
		GeometryCriterion gc = new GeometryCriterion();
		critter.getCriteria().add(gc);
		
		// -- single geo, should be no change
		gc.getServerLayerIds().add(LA);
		dscsi.prune(critter);
		Assert.assertTrue("1", critter.getCriteria().size() == 1);
		
		// -- add attr on same layer, should be no change
		critter.getCriteria().add(aCA);
		dscsi.prune(critter);
		Assert.assertTrue("2a", critter.getCriteria().size() == 2);
		Assert.assertTrue("2b", gc.getServerLayerIds().size() == 1);
		
		// -- two layers in gc, uncommon one should be removed	
		gc.getServerLayerIds().add(LB);
		dscsi.prune(critter);
		Assert.assertTrue("3a", critter.getCriteria().size() == 2);
		Assert.assertTrue("3b", gc.getServerLayerIds().size() == 1);
		Assert.assertEquals("3c", gc.getServerLayerIds().get(0), LA);

		// -- extra attr on different layer, all should be removed (nothing in common)	
		gc.getServerLayerIds().add(LB);
		critter.getCriteria().add(aCC);
		dscsi.prune(critter);
		Assert.assertTrue("4", critter.getCriteria().size() == 0);
	}

	@Test
	public void testPruningGroups() throws Exception {
		DtoSearchConverterServiceImpl dscsi = new DtoSearchConverterServiceImpl();
		AndCriterion critter = new AndCriterion();
		AndCriterion subAnd = new AndCriterion();
		OrCriterion subOr = new OrCriterion();
		GeometryCriterion gc = new GeometryCriterion();
		critter.getCriteria().add(subAnd);
		subAnd.getCriteria().add(gc);
		subOr.getCriteria().add(aCA);
		gc.getServerLayerIds().add(LA);
		
		// -- single SubAnd
		dscsi.prune(critter);
		Assert.assertTrue("1a", critter.getCriteria().size() == 1);
		Assert.assertTrue("1b", subAnd.getCriteria().size() == 1);

		// -- SubAnd & subOr
		critter.getCriteria().add(subOr);
		dscsi.prune(critter);
		Assert.assertTrue("2a", critter.getCriteria().size() == 2);
		Assert.assertTrue("2b", subAnd.getCriteria().size() == 1);
		Assert.assertTrue("2c", subOr.getCriteria().size() == 1);

		// -- SubAnd & subOr Multi
		gc.getServerLayerIds().add(LB);
		subOr.getCriteria().add(aCB);
		dscsi.prune(critter);
		Assert.assertTrue("3a", critter.getCriteria().size() == 2);
		Assert.assertTrue("3b", subAnd.getCriteria().size() == 1);
		Assert.assertTrue("3c", gc.getServerLayerIds().size() == 2);
		Assert.assertTrue("3d", subOr.getCriteria().size() == 2);

		// -- SubAnd & subOr Multi -- diff in Geo
		gc.getServerLayerIds().add(LC);
		dscsi.prune(critter);
		Assert.assertTrue("4a", critter.getCriteria().size() == 2);
		Assert.assertTrue("4b", subAnd.getCriteria().size() == 1);
		Assert.assertTrue("4c", gc.getServerLayerIds().size() == 2);
		Assert.assertTrue("4d", subOr.getCriteria().size() == 2);

		// -- SubAnd & subOr Multi -- diff in subOr
		subOr.getCriteria().add(aCC);
		dscsi.prune(critter);
		Assert.assertTrue("5a", critter.getCriteria().size() == 2);
		Assert.assertTrue("5b", subAnd.getCriteria().size() == 1);
		Assert.assertTrue("5c", gc.getServerLayerIds().size() == 2);
		Assert.assertTrue("5d", subOr.getCriteria().size() == 2);

		// -- SubAnd & subOr Multi -- diff in subAnd
		gc.getServerLayerIds().add(LC); // must be same as is And
		GeometryCriterion gc2 = new GeometryCriterion();
		gc2.getServerLayerIds().add(LA);
		gc2.getServerLayerIds().add(LB);
		gc2.getServerLayerIds().add(LC);
		subAnd.getCriteria().add(gc2);
		dscsi.prune(critter);
		Assert.assertTrue("6a", critter.getCriteria().size() == 2);
		Assert.assertTrue("6b", subAnd.getCriteria().size() == 2);
		Assert.assertTrue("6c", gc.getServerLayerIds().size() == 2);
		Assert.assertTrue("6d", gc2.getServerLayerIds().size() == 2);
		Assert.assertTrue("6e", subOr.getCriteria().size() == 2);
	}

	@Test
	public void testPruningMultiNest() throws Exception {
		DtoSearchConverterServiceImpl dscsi = new DtoSearchConverterServiceImpl();
		AndCriterion root = new AndCriterion();
		OrCriterion subOr = new OrCriterion();
		AndCriterion subsubAnd1 = new AndCriterion();
		AndCriterion subsubAnd2 = new AndCriterion();
		
		root.getCriteria().add(subOr);
		subOr.getCriteria().add(subsubAnd1);
		subOr.getCriteria().add(subsubAnd2);
		subsubAnd1.getCriteria().add(aCA);
		subsubAnd2.getCriteria().add(aCB);
		
		// -- single SubAnd
		dscsi.prune(root);
		Assert.assertTrue("a", root.getCriteria().size() == 1);
		Assert.assertTrue("b", subOr.getCriteria().size() == 2);
		Assert.assertTrue("c", subsubAnd1.getCriteria().size() == 1);
		Assert.assertTrue("d", subsubAnd2.getCriteria().size() == 1);
	}
}
