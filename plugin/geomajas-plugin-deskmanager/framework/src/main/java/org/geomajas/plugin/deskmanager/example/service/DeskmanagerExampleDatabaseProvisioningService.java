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
package org.geomajas.plugin.deskmanager.example.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.geomajas.service.DtoConverterService;
import org.geomajas.widget.layer.configuration.client.ClientLayerTreeInfo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver May
 * 
 *         Helper bean to build the initial configuration, as this application will run with an inmem db.
 * 
 */
public class DeskmanagerExampleDatabaseProvisioningService {

	private static final int SRID = 3857;

	private static final String EPSG_3857 = "EPSG:3857";
	
	public static final String CLIENTAPPLICATION_NAME;
	
	public static final String CLIENTAPPLICATION_ID = "test_id";

	private static final String BE = 
		"POLYGON ((553702.181679138331674 6705753.988936071284115,624165.707162118284032 " +
		"6627894.0473966691643,685356.050909590441734 6586647.407568283379078,672711.849014592706226 " +
		"6468481.737381294369698,643695.764229209162295 6461933.756740380078554,631632.574341233470477 " +
		"6365185.930146945640445,534246.90833572705742 6443743.093275262974203,477117.896043154876679 " +
		"6430271.275361705571413,399434.864928592578508 6512171.203913758508861,347678.775552286882885 " +
		"6582534.101577165536582,295934.191364223952405 6585436.862936250865459,279809.670021348341834 " +
		"6647604.904833872802556,369020.899769917246886 6682687.801654456183314,450517.900791840977035 " +
		"6668705.532083887606859,553702.181679138331674 6705753.988936071284115))";

	private static final String NL = 
		"POLYGON ((666654.367397028952837 6773340.011113058775663,685356.050909590441734 " +
		"6586647.407568283379078,624165.707162118284032 6627894.0473966691643,553702.181679138331674 " +
		"6705753.988936071284115,450517.900791840977035 6668705.532083887606859,369020.899769917246886 " +
		"6682683.196772356517613,426385.768421765416861 6731803.707678327336907,523869.228527462400962 " +
		"6999996.225550541654229,676174.910680112196133 7077972.473691512830555,768676.624270307715051 " +
		"7072687.677446531131864,789483.757233986048959 7009686.601410366594791,761744.748345170053653 " +
		"6841536.245151089504361,733528.27414368360769 6773414.51483643706888,666654.367397028952837 " +
		"6773340.011113058775663))";

	private static final String DE = 
		"POLYGON ((1104501.56431816262193 7358587.663925886154175,1106468.951510177226737 " +
		"7283972.818126597441733,1218960.929697778541595 7239322.101687751710415,1217775.895307265920565 " +
		"7171802.88156802020967,1330963.93738442985341 7207456.467835446819663,1393546.408911052392796 " +
		"7259746.98327483702451,1519229.085435860557482 7184470.158761968836188,1571796.290584317408502 " +
		"7124274.151799720712006,1597803.768640374997631 7029035.474973003380001,1566768.523315788479522 " +
		"6979532.74533221591264,1607186.249664355767891 6913904.603682250715792,1634729.670352784916759 " +
		"6816405.468909976072609,1626054.758406648878008 6754182.679174122400582,1671684.335035849595442 " +
		"6640184.871854516677558,1622004.932140008779243 6621707.542855709791183,1592649.444301020354033 " +
		"6642063.306020502001047,1564732.1049942325335 6608376.464689117856324,1484794.05698119988665 " +
		"6574240.938390942290425,1443461.668166166637093 6530535.269748809747398,1362562.936933637363836 " +
		"6492529.258993349969387,1382052.725841819308698 6440929.823120635002851,1393834.038617494283244 " +
		"6368261.958010921254754,1450640.905638839816675 6327125.96815799921751,1513493.749089506454766 " +
		"6254045.682978676632047,1474243.799349079141393 6176362.826202777214348,1434251.764966038987041 " +
		"6155093.912443067878485,1450031.130661185598001 6046772.041934676468372,1439653.450852940557525 " +
		"6018742.603210766799748,1404936.545285959495232 6052523.838739362545311,1351569.72955360263586 " +
		"6057599.877313791774213,1271982.58978242892772 6027989.047416327521205,1173808.818381265271455 " +
		"6035019.893455975688994,1157954.668962456053123 5991588.288338806480169,1101625.267253778176382 " +
		"6037296.556466386653483,1068024.364947788184509 6028202.019372164271772,948732.820500010508113 " +
		"6078756.569848541170359,925877.764026502496563 6042807.37038373015821,831195.817261510528624 " +
		"6043963.722670667804778,845324.18844169692602 6162437.253602568991482,901607.569397347280756 " +
		"6277879.423304007388651,741190.729523182846606 6309199.231290172785521,688658.039939473965205 " +
		"6353928.60610321816057,694939.87272807653062 6429360.233285292983055,672711.849014592706226 " +
		"6468481.737381294369698,685356.050909590441734 6586647.407568283379078,666654.367397028952837 " +
		"6773340.011113058775663,733528.27414368360769 6773414.51483643706888,761744.748345170053653 " +
		"6841536.245151089504361,789483.757233986048959 7009686.601410366594791,768676.624270307715051 " +
		"7072687.677446531131864,790415.677482847124338 7112402.499255958013237,883458.134921155753545 " +
		"7122629.920164710842073,904104.195249212207273 7081228.267645135521889,979693.28210091451183 " +
		"7174093.826896948739886,954243.805675342562608 7245445.959449938498437,949135.502089021145366 " +
		"7354638.76780375931412,1033272.94381603365764 7329109.484978463500738,1104501.56431816262193 " +
		"7358587.663925886154175))";

	@Autowired
	private SessionFactory session;

	@Autowired
	private DtoConverterService dtoConverterService;

	private static ResourceBundle messages;
	
	static {
		initMessages();
		CLIENTAPPLICATION_NAME = getMessage("testUserApplicationName");
	}
	
	@Transactional
	public void createData() throws WktException, GeomajasException {

		// Create category
		TerritoryCategory cat = new TerritoryCategory();
		cat.setCategoryType(getMessage("testTerritoryCategoryType"));
		cat.setDescription(getMessage("testTerritoryCategoryDescription"));
		cat.setId("ALL");

		session.getCurrentSession().saveOrUpdate(cat);

		// Create group Admin
		Territory adminGroup = new Territory();
		adminGroup.setCode("ADMIN");
		adminGroup.setName(getMessage("adminGroupName"));
		adminGroup.setCrs(EPSG_3857);
		adminGroup.setCategory(cat);

		Geometry allGeom = GeometryService.toPolygon(Bbox.ALL);
		allGeom.setSrid(SRID);
		adminGroup.setGeometry(dtoConverterService.toInternal(allGeom));
		// adminGroup.setTerritory(dtoConverterService.toInternal(geom));

		session.getCurrentSession().saveOrUpdate(adminGroup);

		// Create group Belgium.
		Territory beGroup = new Territory();
		beGroup.setCode("BE");
		beGroup.setName(getMessage("exampleDatabaseProvisioningServiceBelgium"));
		beGroup.setCategory(cat);
		Geometry geom = WktService.toGeometry(BE);
		geom.setSrid(SRID);
		beGroup.setCrs(EPSG_3857);
		beGroup.setGeometry(dtoConverterService.toInternal(geom));
		session.getCurrentSession().saveOrUpdate(beGroup);

		// Create group Netherlands.
		Territory nlGroup = new Territory();
		nlGroup.setCode("NL");
		nlGroup.setName(getMessage("exampleDatabaseProvisioningServiceNetherlands"));
		nlGroup.setCategory(cat);
		nlGroup.setCrs(EPSG_3857);
		Geometry geomNl = WktService.toGeometry(NL);
		geomNl.setSrid(SRID);
		nlGroup.setGeometry(dtoConverterService.toInternal(geomNl));
		session.getCurrentSession().saveOrUpdate(nlGroup);

		// Create group Germany.
		Territory deGroup = new Territory();
		deGroup.setCode("DE");
		deGroup.setName(getMessage("exampleDatabaseProvisioningServiceGermany"));
		deGroup.setCategory(cat);
		deGroup.setCrs(EPSG_3857);
		Geometry geomDe = WktService.toGeometry(DE);
		geomDe.setSrid(SRID);
		deGroup.setGeometry(dtoConverterService.toInternal(geomDe));
		session.getCurrentSession().saveOrUpdate(deGroup);

		// Create an example blueprint.
		Blueprint bluePrint = new Blueprint();
		bluePrint.setActive(true);
		bluePrint.setCreationBy(getMessage("systemUsr"));
		bluePrint.setCreationDate(new Date());
		bluePrint.setGroups(Arrays.asList(adminGroup, beGroup)/*, nlGroup, deGroup)*/);
		bluePrint.setLastEditBy(getMessage("systemUsr"));
		bluePrint.setLastEditDate(new Date());
		bluePrint.setLimitToCreatorTerritory(false);
		bluePrint.setLimitToUserTerritory(false);
		bluePrint.setGeodesksActive(true);
		bluePrint.setName(CLIENTAPPLICATION_NAME);
		bluePrint.setPublic(true);
		bluePrint.setUserApplicationKey(CLIENTAPPLICATION_ID);
		// TEST empty layertree
		bluePrint.getMainMapClientWidgetInfos().put(ClientLayerTreeInfo.IDENTIFIER + "TEST", new ClientLayerTreeInfo());
		

		session.getCurrentSession().saveOrUpdate(bluePrint);

		// Create an example geodesk.
		Geodesk geodesk = new Geodesk();
		geodesk.setActive(true);
		geodesk.setBlueprint(bluePrint);
		geodesk.setCreationBy(getMessage("systemUsr"));
		geodesk.setCreationDate(new Date());
		geodesk.setDeleted(false);
		geodesk.setLastEditBy(getMessage("systemUsr"));
		geodesk.setLastEditDate(new Date());
		geodesk.setLimitToCreatorTerritory(true);
		geodesk.setLimitToUserTerritory(false);
		geodesk.setName(getMessage("testBelgianGeodeskBasedOn") + " " + CLIENTAPPLICATION_NAME);
		geodesk.setOwner(beGroup);
		geodesk.setPublic(true);
		geodesk.setGeodeskId("TEST_BE");

		session.getCurrentSession().saveOrUpdate(geodesk);

		// Create an example geodesk.
		Geodesk geodesk2 = new Geodesk();
		geodesk2.setActive(true);
		geodesk2.setBlueprint(bluePrint);
		geodesk2.setCreationBy(getMessage("systemUsr"));
		geodesk2.setCreationDate(new Date());
		geodesk2.setDeleted(false);
		geodesk2.setLastEditBy(getMessage("systemUsr"));
		geodesk2.setLastEditDate(new Date());
		geodesk2.setLimitToCreatorTerritory(true);
		geodesk2.setLimitToUserTerritory(false);
		geodesk2.setName(getMessage("testDutchGeodeskBasedOn") + " " + CLIENTAPPLICATION_NAME);
		geodesk2.setOwner(nlGroup);
		geodesk2.setPublic(true);
		geodesk2.setGeodeskId("TEST_NL");

		session.getCurrentSession().saveOrUpdate(geodesk2);

		// Create an example geodesk.
		Geodesk geodesk3 = new Geodesk();
		geodesk3.setActive(true);
		geodesk3.setBlueprint(bluePrint);
		geodesk3.setCreationBy(getMessage("systemUsr"));
		geodesk3.setCreationDate(new Date());
		geodesk3.setDeleted(false);
		geodesk3.setLastEditBy(getMessage("systemUsr"));
		geodesk3.setLastEditDate(new Date());
		geodesk3.setLimitToCreatorTerritory(true);
		geodesk3.setLimitToUserTerritory(false);
		geodesk3.setName(getMessage("testGermanGeodeskBasedOn") + " " + CLIENTAPPLICATION_NAME);
		geodesk3.setOwner(deGroup);
		geodesk3.setPublic(true);
		geodesk3.setGeodeskId("TEST_DE");

		session.getCurrentSession().saveOrUpdate(geodesk3);

	}
	
	private static void initMessages() {
		try {
			messages =
					ResourceBundle.getBundle("org/geomajas/plugin/deskmanager/i18n/ServiceMessages",
							Locale.forLanguageTag("nl")); 
	
		} catch (MissingResourceException e ) {
		}
		
	}
	
	private static String getMessage(String key) {
		return messages.getString(key);
	}
	

}
