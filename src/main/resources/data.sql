DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;


CREATE TABLE IF NOT EXISTS roles (
    id LONG AUTO_INCREMENT NOT NULL,
    name varchar(255) NOT NULL UNIQUE,
    description varchar(255)
);

CREATE TABLE IF NOT EXISTS  users (
    id LONG AUTO_INCREMENT NOT NULL,
    login varchar(255) NOT NULL UNIQUE,
    encryptedPassword varchar(255) NOT NULL,
    enabled BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS  users_roles (
    user_id LONG NOT NULL,
    role_id LONG NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id)
        REFERENCES users (id),
    FOREIGN KEY (role_id)
        REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS  persons (
    id LONG AUTO_INCREMENT NOT NULL,
    name varchar(255) NOT NULL,
    email varchar(255) NOT NULL UNIQUE,
    birthday DATE,
    phone varchar(100) NOT NULL,
    city varchar(255) NOT NULL
);


INSERT INTO roles (name,description)
VALUES ('ROLE_ADMIN','Admin role'),
       ('ROLE_USER','User role');

INSERT INTO users (login,encryptedPassword,enabled)
VALUES ('admin','$2a$10$0rmATXhl7C.n7NoJgUXPXugxY0BVqH/vH5GNoxJtyvTULBOT6Jt4K',true),
       ('user','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test1','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test2','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test3','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test4','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test5','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test6','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test7','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test8','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test9','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test10','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test11','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test12','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test13','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test14','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true),
       ('test15','$2a$10$vIKz1UhkYH.HtFq4RjbL/Of6nQPwJVYki2/C52prGvAW8Ner2HzCS',true);

INSERT INTO users_roles (user_id,role_id)
VALUES (1,1),
       (1,2),
       (2,2);

INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Trevor Gay','ornare.placerat.orci@Pellentesque.ca','1991-11-09','8(910)9543715','Schifferstadt'),('Stacy Berry','ultricies.adipiscing@cursus.edu','2009-07-08','8(951)3789612','Drogenbos'),('Brett Holman','et@gravida.edu','1961-04-24','8(938)5478102','Stirling'),('Chandler Mendoza','dignissim@eueuismod.com','2008-06-12','8(953)0657366','Hofheim am Taunus'),('Gabriel Edwards','Curabitur.egestas@ametrisusDonec.ca','1974-07-16','8(975)4140759','Pietracatella'),('Malik Robertson','tellus@orci.org','1971-05-15','8(986)8379390','Shimla'),('Gannon Floyd','non.lorem@liberomauris.ca','1993-04-01','8(948)3274937','Warburg'),('Xavier Mcfadden','hendrerit.Donec@elitpharetraut.ca','1969-11-30','8(938)9377568','Ottignies'),('Palmer Miles','non.enim.commodo@molestiedapibusligula.edu','2004-10-31','8(929)9943561','Sint-Pieters-Kapelle'),('Isaiah Sloan','scelerisque.scelerisque@Aenean.com','1953-05-21','8(983)4965789','Trowbridge');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Christopher Kline','orci.tincidunt@convallisestvitae.edu','2005-03-14','8(926)4072613','Algarrobo'),('Scarlet Jones','ultrices.sit@nonsollicitudina.org','1964-10-18','8(909)6247770','Monte Patria'),('Mercedes Battle','interdum@nascetur.edu','1973-06-28','8(963)9143295','Scunthorpe'),('Barclay Riddle','justo.nec@non.ca','1994-12-02','8(982)4698661','Lahore'),('Kai Aguilar','egestas@Nuncmauriselit.net','1993-11-22','8(908)7027243','Zonhoven'),('Brian Watkins','non.dapibus.rutrum@Inornare.edu','1973-12-12','8(966)9577913','Bernburg'),('Aretha Stevenson','sollicitudin.a@bibendum.ca','2010-02-26','8(999)6292082','Oviedo'),('Keith Witt','eu.tellus.Phasellus@ipsumDonec.net','1970-12-11','8(996)0940508','Cajazeiras'),('Abbot Buckner','egestas@velturpisAliquam.com','1963-08-17','8(946)8701485','Overland Park'),('Jael Mccullough','eleifend@liberoInteger.org','1972-03-07','8(964)1806502','Casperia');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Ezekiel Johnson','Aliquam.adipiscing@sitamet.org','1986-01-28','8(997)1636570','Bossut-Gottechain'),('Mikayla Marsh','dolor@purusaccumsaninterdum.com','1967-06-12','8(938)5980752','Padre las Casas'),('Wang Robbins','Aenean.eget@tincidunt.edu','1962-02-08','8(909)1360162','Hay River'),('Sharon Gillespie','feugiat.Lorem@purussapiengravida.edu','1963-08-23','8(920)9990038','Watford'),('Lawrence Anderson','laoreet.ipsum@seddictum.net','1992-08-18','8(918)8054544','Ballarat'),('Inez Olson','dignissim@metuseu.org','1960-06-15','8(966)3329840','Richmond'),('Garrison Bradshaw','luctus.ipsum.leo@Nullam.net','1952-03-13','8(940)5392705','Saint-Mard'),('Yoshi Lester','elit@Integeraliquamadipiscing.net','2001-11-22','8(932)5055560','Banjarbaru'),('Remedios Thompson','nascetur.ridiculus@leoelementum.co.uk','1962-02-08','8(932)3006998','Scunthorpe'),('Lee Ward','ornare.sagittis@CuraeDonec.ca','1988-10-04','8(924)4274138','Terragnolo');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Plato Delaney','ac.mi@Vestibulumuteros.ca','1987-06-15','8(993)5088859','Davenport'),('Ryan Rocha','elit@Donectempor.org','2007-10-06','8(961)4208270','Jecheon'),('Carl Rollins','augue.ac.ipsum@QuisquevariusNam.edu','1984-06-20','8(975)7812433','Laino Castello'),('Gisela Bernard','tempus.mauris@Integersem.ca','1993-05-22','8(961)6151283','Groot-Bijgaarden'),('Britanni Gamble','cursus.non.egestas@auctornonfeugiat.net','1989-02-26','8(966)0316178','Rostov'),('Blair Dillon','magna.Ut@maurisanunc.edu','2002-07-04','8(969)4250104','Wimmertingen'),('Knox Acevedo','non@tellusPhasellus.edu','1965-07-11','8(962)4429796','Stalhille'),('Honorato Evans','eget.laoreet.posuere@interdumenimnon.net','1995-06-08','8(980)2468223','Temuka'),('Arsenio Dickson','nunc.sit@sit.org','2004-02-01','8(926)0120706','Ketchikan'),('Jillian Richardson','sem.vitae@egetnisi.edu','1987-12-10','8(924)1782815','Genzano di Lucania');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Keefe Silva','magna@dolor.net','1958-06-18','8(925)4375641','Sart-Dames-Avelines'),('Arthur Rogers','augue.eu@aauctor.org','1958-02-07','8(984)2120586','Chicago'),('Aurelia Pickett','dui.in.sodales@lacusvestibulum.ca','2005-08-28','8(944)9211335','Mondolfo'),('Richard Mann','eleifend.nec.malesuada@lectus.org','2001-06-10','8(906)7541625','Lanklaar'),('Brynne Pruitt','lacus.pede.sagittis@magnaUttincidunt.org','1962-06-23','8(974)0164880','Lauro de Freitas'),('Jerry Burris','Mauris@sapien.org','1996-08-25','8(945)5416942','Tokoroa'),('Bell Tran','pretium@ipsumdolorsit.com','1978-07-04','8(982)4774729','Duisburg'),('Rogan Hahn','Cum@quisturpis.co.uk','2009-05-15','8(973)8927432','Bellevue'),('Baker Small','arcu.Nunc@Suspendissesagittis.ca','1966-06-21','8(994)5964631','Anchorage'),('Zeus Rodriquez','id.sapien@nonummyipsumnon.org','2005-05-13','8(959)8655412','Ilbono');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Courtney Contreras','penatibus.et@gravida.org','1973-06-01','8(922)8596960','Ranst'),('Georgia Evans','a.enim@ipsum.com','2002-06-03','8(925)8663906','Strombeek-Bever'),('Jason Moss','dictum.augue.malesuada@Cras.ca','1995-05-02','8(900)5012703','Hindupur'),('Jordan Bernard','urna.justo.faucibus@pedeNuncsed.com','1953-02-03','8(967)9322706','Lakeshore'),('Troy Bond','Donec.felis@non.ca','1971-01-22','8(930)7268925','Fahler'),('Brady Frederick','lectus.convallis.est@augueeutellus.ca','1964-05-25','8(980)5280459','Sogliano Cavour'),('Ramona Valdez','odio.sagittis@necleoMorbi.co.uk','1954-08-11','8(934)9208642','Heerenveen'),('Brielle Mooney','erat.in.consectetuer@Donectemporest.edu','1955-01-31','8(934)2055040','Canning'),('Troy Estes','Curabitur.dictum@sodalesnisi.com','1965-06-25','8(998)2910424','Mandasor'),('Charles Gutierrez','ut@nec.edu','2009-05-01','8(985)5638628','Bakal');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Mallory Frederick','mus.Donec.dignissim@elit.ca','2004-02-01','8(989)2241409','Penticton'),('Nicholas Figueroa','malesuada.vel@sedturpisnec.com','1990-01-18','8(930)0179469','Lethbridge'),('Nichole Mccullough','dolor@facilisisSuspendisse.com','1979-06-29','8(996)0230403','Ruvo del Monte'),('Yvonne Mcclain','mauris.Integer@Crasdolordolor.org','1987-07-20','8(971)6706733','Chastre-Villeroux-Blanmont'),('Erica White','at.risus.Nunc@ligula.ca','1962-07-26','8(965)6119021','College'),('Colby Goodman','Nulla@ornareplaceratorci.com','2002-02-06','8(955)4616773','Hofstade'),('Quintessa Howe','blandit@neque.ca','1955-03-22','8(940)0627191','Neustadt am Rübenberge'),('Lars Pate','Nunc.sollicitudin.commodo@Proin.ca','2005-01-31','8(970)7296578','Bicester'),('Tad Holcomb','pede.Cras@felis.edu','1950-04-17','8(908)1812303','Chiquinquirá'),('Amity Holcomb','malesuada@dolor.ca','1994-09-04','8(928)1163376','Rueglio');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Ivan Johnston','ut.ipsum@vestibulum.co.uk','1979-02-12','8(942)4411019','Santipur'),('Cole Castro','dolor.Donec.fringilla@faucibus.edu','2005-07-22','8(922)3123984','Serik'),('Ariana Romero','orci@enim.ca','1961-04-29','8(927)9190974','Belfast'),('Caryn Mckinney','mi@Aliquam.com','1973-09-17','8(909)5342182','Morro Reatino'),('Kuame Beach','eu.metus@maurisaliquam.com','1957-01-03','8(901)1432324','Mount Pearl'),('Pearl Chen','ultrices.sit@liberoProin.edu','1984-08-19','8(974)2004285','Anzi'),('Kathleen Terry','molestie@ettristique.co.uk','1995-06-22','8(957)9243736','Marzabotto'),('Neville Dorsey','dolor@neque.org','1953-10-28','8(988)6209074','Kortessem'),('Kirk Barnes','a@leoelementumsem.co.uk','1963-08-15','8(954)4506087','Drayton Valley'),('Baxter Lynn','in.faucibus@auctorveliteget.edu','2008-10-26','8(945)9351067','Akhtubinsk');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Zane Freeman','nascetur@non.edu','1965-12-17','8(956)1171718','Abingdon'),('Bradley Stafford','at.velit@viverraDonec.edu','1965-11-03','8(948)6535532','Forres'),('Amena Madden','neque@penatibuset.com','1960-04-22','8(995)1700722','Depok'),('Chaim Britt','ipsum.cursus@Aeneanegetmetus.edu','1968-04-18','8(960)4922050','Muzaffargarh'),('Leslie Lowery','imperdiet@in.com','1970-05-04','8(977)7221547','Calle Blancos'),('Kirsten Blackburn','molestie@quispedePraesent.edu','1957-05-07','8(973)7119331','Aalen'),('Nehru Ballard','netus.et@posuereatvelit.com','1959-01-14','8(964)8649431','Rexton'),('Kermit Rivas','ultrices.sit@tempor.ca','2000-07-07','8(909)1793494','Vancouver'),('Gwendolyn Stanley','at@idante.net','1993-11-16','8(964)1977924','Kota'),('Roary Mills','placerat.augue.Sed@elit.net','1981-11-11','8(918)4291001','Castor');
INSERT INTO persons (name,email,birthday,phone,city) VALUES ('Adrian Watson','In.at.pede@laciniavitae.org','1968-12-06','8(973)3929286','Rothes'),('Damon Torres','Aliquam.tincidunt@orciconsectetuereuismod.co.uk','2001-12-05','8(925)5619066','Khyber Agency'),('Brianna Andrews','vitae.sodales@Donec.co.uk','1964-06-28','8(944)8757192','Podolsk'),('Prescott Burch','vitae.semper.egestas@rutrum.co.uk','2000-09-23','8(966)7013439','Rves'),('Devin Coffey','sed@Duisa.ca','1957-06-01','8(993)6137631','Henderson'),('Justin Farrell','mauris.sapien@Cumsociisnatoque.co.uk','1967-10-05','8(941)1140294','Chagai'),('Dean Hammond','Curabitur.vel.lectus@Suspendisse.com','1979-06-07','8(971)7557510','Şanlıurfa'),('Lacota Velasquez','quis.pede@enim.edu','2009-12-24','8(987)0809650','Kawerau'),('Carol Ayers','velit@euerat.co.uk','1954-06-26','8(986)3221752','Zhukovka'),('Brandon Williams','Mauris.vestibulum@blanditviverra.co.uk','1952-12-14','8(906)7044566','Dollard-des-Ormeaux');
