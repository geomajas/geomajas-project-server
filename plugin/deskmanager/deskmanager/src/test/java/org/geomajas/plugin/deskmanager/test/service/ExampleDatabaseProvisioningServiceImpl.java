/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.test.service;

import java.util.Arrays;
import java.util.Date;
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
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.service.security.impl.UserServiceImpl;
import org.geomajas.service.DtoConverterService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver May
 * 
 *         Helper bean to build the initial configuration, as this application will run with an inmem db.
 * 
 */
@Component
public class ExampleDatabaseProvisioningServiceImpl implements DeskmanagerExampleDatabaseProvisioningService {

	public static final String GEODESK_TEST_DE = "TEST_DE";

	public static final String GEODESK_TEST_DE_PRIVATE = "TEST_DE_PRIVATE";

	public static final String GEODESK_TEST_NL = "TEST_NL";

	public static final String GEODESK_TEST_BE = "TEST_BE";

	private static final int SRID = 3857;

	private static final String EPSG_3857 = "EPSG:3857";

	public static final String CLIENTAPPLICATION_NAME;

	public static final String USER_NIKO_EMAIL = "niko.haak@gmail.com";
	public static final String USER_NIKO_PASSWORD = "kaah";

	public static final String USER_ADMIN_EMAIL = "admin@admin.com";
	public static final String USER_ADMIN_PASSWORD = "admin";

	private static final String BE =
			"POLYGON((408632.623169 6759429.725585,689708.137665 6701252.358792,631632.574341 " +
					"6365185.930147,279809.670021 6647604.904834,408632.623169 6759429.725585))";

	private static final String NL =
			"POLYGON((768676.624270 7072687.677447,688257.442080 6701252.358792,408189.680568 " +
					"6759570.062783,523869.228527 6999996.225551,768676.624270 7072687.677447))";

	private static final String DE =
			"POLYGON((768676.624270 7072687.677447,1628268.375928 7208921.727526,1439653.450853 " +
					"6018742.603211,634982.303291 6364083.475199,768676.624270 7072687.677447))";

	@Autowired
	private SessionFactory session;

	@Autowired
	private DtoConverterService dtoConverterService;

	private static ResourceBundle messages;

	static {
		initMessages();
		CLIENTAPPLICATION_NAME = getMessage("testUserApplicationName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.deskmanager.test.service.DeskmanagerExampleDatabaseProvisioningService#createData()
	 */
	@Override
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
		bluePrint.setTerritories(Arrays.asList(adminGroup, beGroup)/* , nlGroup, deGroup) */);
		bluePrint.setLastEditBy(getMessage("systemUsr"));
		bluePrint.setLastEditDate(new Date());
		bluePrint.setLimitToCreatorTerritory(false);
		bluePrint.setLimitToUserTerritory(false);
		bluePrint.setGeodesksActive(true);
		bluePrint.setName(CLIENTAPPLICATION_NAME);
		bluePrint.setPublic(true);
		bluePrint.setUserApplicationKey(CLIENTAPPLICATION_ID);

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
		geodesk.setGeodeskId(GEODESK_TEST_BE);

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
		geodesk2.setGeodeskId(GEODESK_TEST_NL);

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
		geodesk3.setGeodeskId(GEODESK_TEST_DE);

		session.getCurrentSession().saveOrUpdate(geodesk3);

		// Create an example non publigeodesk.
		Geodesk geodesk4 = new Geodesk();
		geodesk4.setActive(true);
		geodesk4.setBlueprint(bluePrint);
		geodesk4.setCreationBy(getMessage("systemUsr"));
		geodesk4.setCreationDate(new Date());
		geodesk4.setDeleted(false);
		geodesk4.setLastEditBy(getMessage("systemUsr"));
		geodesk4.setLastEditDate(new Date());
		geodesk4.setLimitToCreatorTerritory(true);
		geodesk4.setLimitToUserTerritory(false);
		geodesk4.setName(getMessage("testGermanGeodeskBasedOn") + " " + CLIENTAPPLICATION_NAME);
		geodesk4.setOwner(deGroup);
		geodesk4.setPublic(false);
		geodesk4.setGeodeskId(GEODESK_TEST_DE_PRIVATE);

		session.getCurrentSession().saveOrUpdate(geodesk4);

		// create user in group Netherlands, CONSULTING_USER
		User user = new User();
		user.setActive(true);
		user.setEmail(USER_NIKO_EMAIL);
		user.setName("niko");
		user.setSurname("haak");
		user.setPassword(UserServiceImpl.encodePassword(user.getEmail(), USER_NIKO_PASSWORD));
		session.getCurrentSession().save(user);
		GroupMember nlMember = user.join(nlGroup, Role.CONSULTING_USER);

		User adminUser = new User();
		adminUser.setActive(true);
		adminUser.setEmail(USER_ADMIN_EMAIL);
		adminUser.setName("admin");
		adminUser.setSurname("admin");
		adminUser.setPassword(UserServiceImpl.encodePassword(adminUser.getEmail(), USER_ADMIN_PASSWORD));
		adminUser.getGroups().add(new GroupMember(adminUser, beGroup, Role.ADMINISTRATOR));
		session.getCurrentSession().save(adminUser);
	}

	private static void initMessages() {
		try {
			messages = ResourceBundle.getBundle("org/geomajas/plugin/deskmanager/i18n/ServiceMessages");

		} catch (MissingResourceException e) {
		}

	}

	private static String getMessage(String key) {
		return messages.getString(key);
	}

}
