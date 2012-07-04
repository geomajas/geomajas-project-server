CREATE TABLE POINT
(
        ID integer identity primary key,
        NAME varchar(50)
);
CALL AddGeometryColumn(null, 'POINT', 'GEOM', 4326, 'POINT', 2);
CALL CreateSpatialIndex(null, 'POINT', 'GEOM', '4326');

insert into POINT (NAME, GEOM) values ('point1', ST_GeomFromText('POINT (0 0)', 4326));
insert into POINT (NAME, GEOM) values ('point2', ST_GeomFromText('POINT (1 0)', 4326));
insert into POINT (NAME, GEOM) values ('point3', ST_GeomFromText('POINT (0 1)', 4326));
insert into POINT (NAME, GEOM) values ('point4', ST_GeomFromText('POINT (1 1)', 4326));
