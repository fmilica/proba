insert into clinical_centre (id, name, version, is_active) values ('1', 'The Good Shepherd', '0', 'true');

/*Jedina klinika koju imamo za sada*/
insert into clinic (id, name, address, city, country, description, rating, clinical_center_id, version, is_active) 
values ('6', 'Saint Mary Clinic', 'Lipov Gaj 23', 'Novi Sad', 'Srbija', 'Mother and child clinic', '4.6', '1', '0', 'true');
/*Druga samo za probu*/
insert into clinic (id, name, address, city, country, description, rating, clinical_center_id, version, is_active) 
values ('17', 'Dr Gray Clinic', 'Aleksandar Veliki 11', 'Nis', 'Srbija', 'First cardio clinic', '4.9', '1', '0', 'true');
/*Treca samo za probu*/
insert into clinic (id, name, address, city, country, description, rating, clinical_center_id, version, is_active) 
values ('18', 'Shephered United Clinic', 'Puskinova 3', 'Beograd', 'Srbija', 'Our united front for health', '4.3', '1', '0', 'true');

/*Odinacije*/
/*Prva klinika*/
insert into ordination(id, ordination_type, name, ordination_number, clinic_id, version, is_active)
values('19', '0', 'The Great Room', '1', '6', '0', 'true');
insert into ordination(id, ordination_type, name, ordination_number, clinic_id, version, is_active)
values('20', '1', 'Regular Exam', '2', '6', '0', 'true');
insert into ordination(id, ordination_type, name, ordination_number, clinic_id, version, is_active)
values('37', '1', 'Regular Exam', '3', '6', '0', 'true');
/*Druga klinika*/
insert into ordination(id, ordination_type, name, ordination_number, clinic_id, version, is_active)
values('50', '1', 'The Gray Room', '1', '17', '0', 'true');
insert into ordination(id, ordination_type, name, ordination_number, clinic_id, version, is_active)
values('51', '1', 'The Good Exam', '2', '17', '0', 'true');

/*Admin klinickog centra*/
insert into clinical_centre_admin (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, clinical_centre_id, version, is_active, logged) 
values ('7', 'thegoodshepherdadm@gmail.com', 'admin1tgs', 'Ksenija', 'Prcic', 'female', '1990-06-15', 'Adresa', 'City', 'Country', '55', '22', '1', '0', 'true', 'false');

/*Admin klinike*/
insert into clinic_admin (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number,clinic_id, version, is_active, logged) 
values ('8', 'felisimadolanovski@gmail.com', 'felidola1', 'Felisima', 'Dolanovski', 'female', '1978-05-20', 'Adresa1', 'Grad1', 'Ukrajina', '55', '11', '17', '0', 'true', 'true');

insert into clinic_admin (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number,clinic_id, version, is_active, logged) 
values ('22', 'perapera2359@gmail.com', 'perapera2', 'Petar', 'Isailovic', 'male', '1989-07-20', 'Kralja Petra I 42', 'Nis', 'Srbija', '55', '98', '6', '0', 'true', 'false');

/*Doktori nase prve klinike*/
insert into doctor (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, rating, clinic_id, specialization, start_work, end_work, version, is_active, logged) 
values ('9', 'jovadoktor1@gmail.com', 'jovajova1', 'Jova', 'Jovic', 'male', '1965-03-01', 'Isidorina 3', 'Novi Sad', 'Srbija', '55', '33', '4.7', '6', '4', '6', '14', '0', 'true', 'true');
insert into doctor (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, rating, clinic_id, specialization, start_work, end_work, version, is_active, logged) 
values ('10', 'zovadoktorka@gmail.com', 'zovazova1', 'Zova', 'Zovic', 'female', '1980-07-09', 'Knez Mihailova 2', 'Beograd', 'Srbija', '55', '44', '4.3', '6', '5', '8', '16', '0', 'true', 'true');
insert into doctor (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, rating, clinic_id, specialization, start_work, end_work, version, is_active, logged) 
values ('52', 'kova', 'kova', 'Kova', 'Kovic', 'female', '1993-06-01', 'Milosa Obilica 56', 'Negotin', 'Srbija', '55', '99', '3.7', '6', '5', '13', '21', '0', 'true', 'true');
/*Doktori nase druge klinike*/
insert into doctor (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, rating, clinic_id, specialization, start_work, end_work, version, is_active, logged) 
values ('11', 'govadoktor@gmail.com', 'govagova1', 'Gova', 'Govic', 'male', '1972-10-19', 'Hajdova 5', 'Zrenjanin', 'Srbija', '55', '55', '4.8', '17', '5', '12', '20', '0', 'true', 'true');
insert into doctor (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, rating, clinic_id, specialization, start_work, end_work, version, is_active, logged) 
values ('48', 'mova', 'mova', 'Mova', 'Movic', 'female', '1978-7-18', 'Ivanjicka 17', 'Ivanjica', 'Srbija', '55', '88', '3.9', '17', '3', '08', '14', '0', 'true', 'false');

/*Tri tipa pregleda*/
insert into appointment_type (id, name, duration, price, /*doctor_id,*/ clinic_id, version, is_active) values ('12', 'Gynecological', '1', '30', /*'9',*/ '6', '0', 'true');
insert into appointment_type (id, name, duration, price, /*doctor_id,*/ clinic_id, version, is_active) values ('13', 'Dermatological', '2', '40', /*'10',*/ '6', '0', 'true');
insert into appointment_type (id, name, duration, price, /*doctor_id,*/ clinic_id, version, is_active) values ('14', 'Dentist', '3', '60', /*'11',*/ '6', '0', 'true');
/*Dva tipa pregleda ce imati i klinika2*/
insert into appointment_type (id, name, duration, price, clinic_id, version, is_active) values ('19', 'Dentist', '4', '90', '17', '0', 'true');
insert into appointment_type (id, name, duration, price, clinic_id, version, is_active) values ('49', 'Orthopeadic', '2', '150', '17', '0', 'true');

/*Tabela doktori i tipovi pregleda*/
insert into appointment_type_doctor (appointment_type_id, doctor_id) values ('12', '9');
/*insert into appointment_type_doctor (appointment_type_id, doctor_id, version) values ('13', '10');klinika ima tip ali nema doktora*/
insert into appointment_type_doctor (appointment_type_id, doctor_id) values ('12', '10'); /*'14', '10'*/
insert into appointment_type_doctor (appointment_type_id, doctor_id) values ('14', '52');
/*Druga klinika*/
insert into appointment_type_doctor (appointment_type_id, doctor_id) values ('19', '11');
insert into appointment_type_doctor (appointment_type_id, doctor_id) values ('49', '48');

/* Medicinske sestre */
insert into nurse (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, version, is_active, clinic_id, logged) 
values ('15', 'zikambrat@gmail.com', 'zikazika1', 'Zika', 'Zikic', 'male', '1990-06-15', 'Adresa', 'City', 'Country', '44', '66', '0', 'true', '6', 'false');

/* Pacijenti i njihovi zdravstveni kartoni*/
insert into patient (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, clinical_centre_id, version, is_active) 
values ('16', 'srdjanmilic12@gmail.com', 'srdjabroj1', 'Srdjan', 'Milic', 'male', '1993-06-01', 'Adresa', 'City', 'Country', '55', '77', '1', '0', 'true');
/*Potvrdjen zahtev za registraciju*/
insert into registration_request(id, registered_user_id, approved, description, version, is_active) 
values ('56', '16', 'true', '', '0', 'true');
insert into medical_records(id, height, weight, blood_pressure, blood_type, allergies, patient_id, version, is_active)
values ('30' ,'180', '60', '120/60', 'A','No allergies', '16', '0', 'true');
update patient
set medical_record_id = '30', registration_request_id = '56'
where id = '16';
insert into patient (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, clinical_centre_id, version, is_active) 
values ('29', 'milkapacijent@gmail.com', 'milka12!', 'Milka', 'Jagodic', 'female', '1993-06-01', 'Adresa', 'City', 'Country', '55', '111', '1', '0', 'true');
/*Potvrdjen zahtev za registraciju*/
insert into registration_request(id, registered_user_id, approved, description, version, is_active) 
values ('55', '29', 'true', '', '0', 'true');
insert into medical_records(id, height, weight, blood_pressure, blood_type, allergies, patient_id, version, is_active)
values ('43' ,'163', '67', '130/80', 'AB-','Polen', '29', '0', 'true');
update patient
set medical_record_id = '43', registration_request_id = '55'
where id = '29';
/*Nova registracija pacijenta
insert into patient (id, email, password, name, surname, gender, date_of_birth, address, city, country, phone_number, security_number, clinical_centre_id, version) 
values ('30', 'dolanovkatarina@gmail.com', 'katarina1!', 'Katarina', 'Dolanov', 'female', '1993-06-01', 'Adresa', 'City', 'Country', '5500', '7700', '1', '0');
insert into registration_request(id, registered_user_id, approved, description, version) 
values ('31', '30', 'false', '', '0');*/

/*Jedan pregled potvrdjen i zavrsen*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, version, ordination_id, is_active)
values ('21', 'true', '2020-05-13', '16', '20', 'true', '16', '6', '9', '19', '0', '20', 'true');

/*TREBAO MI JE SAMO APPOINTMENT KOJI JE POTVRDJEN A NIJE ZAVRSEN*/
/*pregled kod jove koji je sada, pa moze da pristupi medical record*/
/*JOS JEDAN MILKIN PREGLED KOJI JE TRENUTNO*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, ordination_id, version, is_active)
values ('44', 'false', '2020-05-26', '16', '17', 'true', '29', '6', '9', '12', '19', '0', 'true');
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('54', '44', '2020-05-15', 'true', '6', '0', 'true');
update appointment
set appointment_request_id = '54'
where id = '44';

/*NE preklapa se NI SA JEDNIM drugim vremenom u toj ordinaciji*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, version, is_active)
values ('36', 'false', '2020-05-21', '06', '07', 'false', '29', '6', '9', '12', '0', 'true');
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('38', '36', '2020-05-20', 'false', '6', '0', 'true');
update appointment
set appointment_request_id = '38'
where id = '36';
/*NE preklapa se sa jednim drugim vremenom u toj ordinaciji*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, version, is_active)
values ('41', 'false', '2020-05-21', '08', '09', 'false', '29', '6', '9', '12', '0', 'true');
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('42', '41', '2020-05-20', 'false', '6', '0', 'true');
update appointment
set appointment_request_id = '42'
where id = '41';
/*Preklapa se sa drugim vremenom u toj ordinaciji*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, version, is_active)
values ('39', 'false', '2020-05-21', '09', '10', 'false', '29', '6', '9', '12', '0', 'true');
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('40', '39', '2020-05-20', 'false', '6', '0', 'true');
update appointment
set appointment_request_id = '40'
where id = '39';

/*Jedan pregled NEpotvrdjen*/
/*ZAHTEVI DRUGE KLINIKE!!!*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, version, is_active)
values ('23', 'false', '2020-05-15', '16', '20', 'false', '16', '17', '11', '19', '0', 'true');
/*Jedan zahtev za nepotvrdjen pregled*/
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('24', '23', '2020-05-15', 'false', '17', '0', 'true');
update appointment
set appointment_request_id = '24'
where id = '23';

/*Predefinisani pregledi*/
/*Predefinisani druga klinika*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, clinic_id, doctor_id, app_type, ordination_id, version, is_active)
values ('45', 'false', '2020-05-21', '16', '20', 'false', '17', '11', '19', '50', '0', 'true');
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, clinic_id, doctor_id, app_type, ordination_id, version, is_active)
values ('46', 'false', '2020-05-21', '12', '16', 'false', '17', '11', '19', '51', '0', 'true');
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, clinic_id, doctor_id, app_type, ordination_id, version, is_active)
values ('47', 'false', '2020-05-21', '08', '10', 'false', '17', '48', '49', '50', '0', 'true');

/*Zahtevi u prvoj klinici*/
/*Milkini pregledi - OBA ODOBRENA!*/
/*Jedan prosao*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, ordination_id, version, is_active)
values ('57', 'true', '2020-05-13', '09', '10', 'true', '29', '6', '9', '12', '20', '0', 'true');
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('58', '57', '2020-05-12', 'true', '6', '0', 'true');
update appointment
set appointment_request_id = '58'
where id = '57';
/*Jedan potvrdjen - preklapanje sa njim*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, ordination_id, version, is_active)
values ('25', 'false', '2020-05-21', '09', '10', 'true', '29', '6', '9', '12', '20', '0', 'true');
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('26', '25', '2020-05-15', 'true', '6', '0', 'true');
update appointment
set appointment_request_id = '26'
where id = '25';
/*Jedan nepotvrdjen - preklapanje sa njim*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, ordination_id, version, is_active)
values ('27', 'false', '2020-05-21', '08', '11', 'false', '29', '6', '10', '14', '37', '0', 'true');
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('28', '27', '2020-05-20', 'true', '6', '0', 'true');
update appointment
set appointment_request_id = '28'
where id = '27';

/*Zahtev za operaciju*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, ordination_type, version, is_active)
values ('74', 'false', '2020-05-25', '13', '15', 'false', '29', '6', '9', '12', '0', '0', 'true');
insert into appointment_request (id, appointment_id, request_date, approved, clinic_id, version, is_active)
values ('75', '74', '2020-05-15', 'false', '6', '0', 'true');
update appointment
set appointment_request_id = '75'
where id = '74';

/*Zahtev za operaciju*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, patient_id, clinic_id, doctor_id, app_type, ordination_type, version, ordination_id, is_active)
values ('62', 'false', '2020-06-25', '13', '15', 'true', '29', '6', '9', '12', '0', '0', '19', 'true');

/*Sifarnik*/
insert into diagnose_perscription (id, version, is_active)
values ('33', '0', 'true');
/*Dijagnoza*/
insert into diagnosis (id, name, sifarnik_id, version, is_active)
values ('31', 'Vitiligo', '33', '0', 'true');
insert into diagnosis (id, name, sifarnik_id, version, is_active)
values ('53', 'Healthy', '33', '0', 'true');
/*Lek*/
insert into prescription (id, medicine, version, is_active)
values ('32', 'Krema za vitiligo', '0', 'true');
insert into prescription (id, medicine, version, is_active)
values ('35', 'Tablete za vitiligo', '0', 'true');
/*Povezivanje dijagnoze i lekova za nju*/
insert into perscription_diagnosis (diagnosis_id, perscription_id)
values ('31', '32');
insert into perscription_diagnosis (diagnosis_id, perscription_id)
values ('31', '35');

/*Medical report (izvestaj doktora)*/
insert into medical_report (id, description, diagnosis_id, appointment_id, version, verified, is_active)
values ('34', 'Pacijent pokazuje pocetni stadijum Vitiliga, pecati se nalaze na sakama i ledjima.', '31', '21', '0', 'false', 'true');
/*Lekovi prepisani ovim izvestajem gore*/
insert into medical_report_perscription(medical_report_id, prescription_id)
values ('34', '32');
insert into medical_report_perscription(medical_report_id, prescription_id)
values ('34', '35');
/*Dodavanje izvestaja u appointment*/
update appointment
set medical_report_id = '34'
where id = '21';

insert into medical_report (id, description, diagnosis_id, appointment_id, version, verified, nurse_id, is_active)
values ('59', 'Pacijent pokazuje pocetni stadijum Vitiliga, pecati se nalaze na sakama i ledjima.', '31', '57', '0', 'true', '15', 'true');
/*Lekovi prepisani ovim izvestajem gore*/
insert into medical_report_perscription(medical_report_id, prescription_id)
values ('59', '32');
insert into medical_report_perscription(medical_report_id, prescription_id)
values ('59', '35');
/*Dodavanje izvestaja u appointment*/
update appointment
set medical_report_id = '59'
where id = '57';

/*Zahtevi za bolovanje/odmor*/
insert into leave(id, version, start_date, end_date, leave_type, medical_personnel_id, is_active)
values ('60', '0', '2020-05-27', '2020-06-03', '0', '15', 'true');
insert into leave_request(id, version, leave_id, approved, description, is_active)
values ('61', '0', '60', 'false', '', 'true');
insert into leave(id, version, start_date, end_date, leave_type, medical_personnel_id, is_active)
values ('63', '0', '2020-06-06', '2020-06-13', '1', '15', 'true');
insert into leave_request(id, version, leave_id, approved, description, is_active)
values ('64', '0', '63', 'true', '', 'true');
insert into leave(id, version, start_date, end_date, leave_type, medical_personnel_id, is_active)
values ('65', '0', '2020-05-27', '2020-06-03', '0', '9', 'true');
insert into leave_request(id, version, leave_id, approved, description, is_active)
values ('66', '0', '65', 'true', '', 'true');
insert into leave(id, version, start_date, end_date, leave_type, medical_personnel_id, is_active)
values ('67', '0', '2020-05-02', '2020-05-07', '1', '9', 'true');
insert into leave_request(id, version, leave_id, approved, description, is_active)
values ('68', '0', '67', 'true', '', 'true');
insert into leave(id, version, start_date, end_date, leave_type, medical_personnel_id, is_active)
values ('69', '0', '2020-05-27', '2020-06-03', '0', '10', 'true');
insert into leave_request(id, version, leave_id, approved, description, is_active)
values ('70', '0', '69', 'false', '', 'true');
insert into leave(id, version, start_date, end_date, leave_type, medical_personnel_id, is_active)
values ('72', '0', '2020-06-13', '2020-06-30', '0', '9', 'true');
insert into leave_request(id, version, leave_id, approved, description, is_active)
values ('73', '0', '72', 'false', '', 'true');

/*Mother and child klinika jedan pregled koji je potvrdjen ali nema jos pacijenta da bi predefined appointments prikazivao nesto*/
insert into appointment (id, app_finished, app_date, app_start_time, app_end_time, confirmed, clinic_id, doctor_id, app_type, ordination_id, version, is_active)
values ('71', 'false', '2020-06-07', '16', '17', 'true', '6', '9', '12', '19', '0', 'true');