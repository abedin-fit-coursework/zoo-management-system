-- 0) Baza
CREATE DATABASE zooloski_vrt CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE zooloski_vrt;

-- 1) TIP_KAVEZA
CREATE TABLE tip_kaveza (
  id INT AUTO_INCREMENT NOT NULL,
  naziv VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (naziv)
);

-- 2) KAVEZ
CREATE TABLE kavez (
  id INT AUTO_INCREMENT NOT NULL,
  lokacija VARCHAR(100) NOT NULL,
  tip_kaveza_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (tip_kaveza_id) REFERENCES tip_kaveza(id),
  UNIQUE(lokacija)
);

-- 3) SERTIFIKAT
CREATE TABLE sertifikat (
  id INT AUTO_INCREMENT NOT NULL,
  naziv VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE(naziv)
);

-- 4) ZAPOSLENI
CREATE TABLE zaposleni (
  id INT AUTO_INCREMENT NOT NULL,
  jmbg VARCHAR(13) NOT NULL,
  ime VARCHAR(50) NOT NULL,
  prezime VARCHAR(50) NOT NULL,
  datum_zaposlenja DATE NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (jmbg)
);

-- 5) STARATELJ
CREATE TABLE staratelj (
  zaposleni_id INT NOT NULL,
  specijalizacija VARCHAR(100) NOT NULL,
  PRIMARY KEY (zaposleni_id),
  FOREIGN KEY (zaposleni_id) REFERENCES zaposleni(id)
);

-- 6) VETERINAR
CREATE TABLE veterinar (
  zaposleni_id INT NOT NULL,
  PRIMARY KEY (zaposleni_id),
  FOREIGN KEY (zaposleni_id) REFERENCES zaposleni(id)
);

-- 7) KATEGORIJA
CREATE TABLE kategorija (
  id INT AUTO_INCREMENT NOT NULL,
  naziv VARCHAR(100) NOT NULL,
  nadkategorija_id INT,
  PRIMARY KEY (id),
  FOREIGN KEY (nadkategorija_id) REFERENCES kategorija(id),
  UNIQUE(naziv)
);

-- 8) STATUS_ZIVOTINJE
CREATE TABLE status_zivotinje (
  id INT AUTO_INCREMENT NOT NULL,
  naziv VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (naziv)
);

-- 9) ZIVOTINJA
CREATE TABLE zivotinja (
  id INT AUTO_INCREMENT NOT NULL,
  latinski_naziv VARCHAR(100) NOT NULL,
  vrsta VARCHAR(100) NOT NULL,
  datum_dolaska DATE NOT NULL,
  status_id INT NOT NULL,
  kavez_id INT NOT NULL,
  kategorija_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (status_id) REFERENCES status_zivotinje(id),
  FOREIGN KEY (kavez_id) REFERENCES kavez(id),
  FOREIGN KEY (kategorija_id) REFERENCES kategorija(id)
);

-- 10) TURA
CREATE TABLE tura (
  id INT AUTO_INCREMENT NOT NULL,
  datum_ture DATE NOT NULL,
  staratelj_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (staratelj_id) REFERENCES staratelj(zaposleni_id)
);

-- 11) ZIVOTINJA_TURA
CREATE TABLE zivotinja_tura (
  id INT AUTO_INCREMENT NOT NULL,
  zivotinja_id INT NOT NULL,
  tura_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (zivotinja_id, tura_id),
  FOREIGN KEY (zivotinja_id) REFERENCES zivotinja(id),
  FOREIGN KEY (tura_id) REFERENCES tura(id)
);

-- 12) ZIVOTINJA_STARATELJ
CREATE TABLE zivotinja_staratelj (
  id INT AUTO_INCREMENT NOT NULL,
  zivotinja_id INT NOT NULL,
  staratelj_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (zivotinja_id, staratelj_id),
  FOREIGN KEY (zivotinja_id) REFERENCES zivotinja(id),
  FOREIGN KEY (staratelj_id) REFERENCES staratelj(zaposleni_id)
);

-- 13) PREGLED
CREATE TABLE pregled (
  id INT AUTO_INCREMENT NOT NULL,
  datum_pregleda DATE NOT NULL,
  rezultat VARCHAR(500) NOT NULL,
  preporuke VARCHAR(500) NOT NULL,
  zivotinja_id INT NOT NULL,
  veterinar_id INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (zivotinja_id) REFERENCES zivotinja(id),
  FOREIGN KEY (veterinar_id) REFERENCES veterinar(zaposleni_id)
);

-- 14) VETERINAR_SERTIFIKAT
CREATE TABLE veterinar_sertifikat (
  id INT AUTO_INCREMENT NOT NULL,
  veterinar_id INT NOT NULL,
  sertifikat_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (veterinar_id, sertifikat_id),
  FOREIGN KEY (veterinar_id) REFERENCES veterinar(zaposleni_id),
  FOREIGN KEY (sertifikat_id) REFERENCES sertifikat(id)
);


-- tip_kaveza (zatvoreni i otvoreni)
INSERT INTO tip_kaveza (naziv) VALUES
('zatvoreni'),
('otvoreni');

-- kavez (5)
INSERT INTO kavez (lokacija, tip_kaveza_id) VALUES
('Sektor A, red 1',1),
('Sektor B, red 2',2),
('Sektor C, red 1',1),
('Sektor A, red 3',2),
('Sektor D, red 1',1);

-- sertifikat (5)
INSERT INTO sertifikat (naziv) VALUES
('Sertifikat za egzotične životinje'),
('Sertifikat za velike sisare'),
('Sertifikat za ptice'),
('Sertifikat za reptile'),
('Sertifikat za morske životinje');

-- zaposleni (10 - 5 staratelja + 5 veterinara)
INSERT INTO zaposleni (jmbg, ime, prezime, datum_zaposlenja) VALUES
('1234567890123','Marko','Petrović','2020-03-15'),
('2345678901234','Ana','Jovanović','2019-06-20'),
('3456789012345','Stefan','Nikolić','2021-01-10'),
('4567890123456','Jelena','Đorđević','2018-09-05'),
('5678901234567','Ivan','Marković','2022-02-28'),
('6789012345678','Milica','Stojanović','2017-11-12'),
('7890123456789','Nikola','Ilić','2020-07-18'),
('8901234567890','Jovana','Pavlović','2019-04-22'),
('9012345678901','Dušan','Radović','2021-08-30'),
('0123456789012','Sara','Milošević','2018-12-01');

-- staratelj (5)
INSERT INTO staratelj (zaposleni_id, specijalizacija) VALUES
(1,'Ishrana'),
(2,'Reprodukcija'),
(3,'Zdravstvena njega'),
(4,'Higijena'),
(5,'Adaptacija');

-- veterinar (5)
INSERT INTO veterinar (zaposleni_id) VALUES
(6),
(7),
(8),
(9),
(10);

-- kategorija (5 - sa hijerarhijom)
INSERT INTO kategorija (naziv, nadkategorija_id) VALUES
('Sisari',NULL),
('Mesožderi',1),
('Ptice',NULL),
('Reptili',NULL),
('Primati',1),
('Grabljivice',3);

-- status_zivotinje (aktivna i u karantinu)
INSERT INTO status_zivotinje (naziv) VALUES
('aktivna'),
('u karantinu');

-- zivotinja (5)
INSERT INTO zivotinja (latinski_naziv, vrsta, datum_dolaska, status_id, kavez_id, kategorija_id) VALUES
('Panthera leo','Lav','2023-05-10',1,1,2),
('Pan troglodytes','Šimpanza','2022-08-15',2,2,5),
('Aquila chrysaetos','Zlatni orao','2024-01-20',1,3,6),
('Python regius','Kraljević piton','2023-11-05',1,4,4),
('Panthera tigris','Tigar','2024-02-01',2,5,2);

-- tura (5)
INSERT INTO tura (datum_ture, staratelj_id) VALUES
('2026-06-01',1),
('2026-07-05',2),
('2026-08-10',3),
('2026-06-15',4),
('2026-07-20',5);

-- zivotinja_tura (5)
INSERT INTO zivotinja_tura (zivotinja_id, tura_id) VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,5);

-- zivotinja_staratelj (5)
INSERT INTO zivotinja_staratelj (zivotinja_id, staratelj_id) VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,5);

-- pregled (5)
INSERT INTO pregled (datum_pregleda, rezultat, preporuke, zivotinja_id, veterinar_id) VALUES
('2026-01-15','Zdravstveno stanje odlično','Nastaviti sa trenutnom ishranom',1,6),
('2026-01-20','Blaga upala zuba','Preporučuje se dentalna intervencija',2,7),
('2026-01-25','Sve u redu','Redovni pregledi',3,8),
('2026-01-28','Otežano disanje','Praćenje respiratornog sistema',4,9),
('2026-02-01','Povreda šape','Terapija antibioticima, mirovanje 2 nedelje',5,10);

-- veterinar_sertifikat (5)
INSERT INTO veterinar_sertifikat (veterinar_id, sertifikat_id) VALUES
(6,1),
(7,2),
(8,3),
(9,4),
(10,5);