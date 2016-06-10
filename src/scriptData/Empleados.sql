INSERT INTO control.empresa
(`id`,`nombre`,`siglas`,`numeracion`,`telefono_1`,`telefono_2`,`fax`,`email`,`direccion`,`web`,`logo`,`creacion`,`ultima_modificacion`,`observacion`,`tipo`,`dia_corte_pago`,`activo`)
VALUES
(1,'Sepro Alarma C.A.','SA-CA.','R-555666777','0241-7896532','0241-7845531','0241-7896532','sepro.alarmas@gmail.com','Valencia - Carabobo','seporalarmas.com','1971-01-01 00:00:01', '1971-01-01 00:00:01', null,'Seguridad',true,'24','1');




INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',1,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Juan Antonio','Perez Rodriguez','10587123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',1,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Raul','Morillo Aponte','10584123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',1,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Jose Antonio','Ramoz Iponte','10582123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',1,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Samuel Miguel','Carrillo Tonson','10598123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',1,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Tomas Jesus','Ochoa Sandoval','10377123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',1,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Isbel Sofia','Alcantara de Reyez','10593123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','F');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',2,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Savier Jose','Maldonado Aristurieta','10723123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',2,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Pedro Ramon','Ramirez Iponte','197563123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',2,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Jesus Domingo','Herrera Rodriguez','18523123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',2,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Domingo Antonio','Herrera Rodriguez','16761123','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,2,'16-05-05 00:00:00','2015-06-01 00:00:00',2,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Gabriel Alberto','Martinez Manaure','12729223','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,4,'16-05-05 00:00:00','2015-06-01 00:00:00',5,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Jordan Jesus','Fabuzze Gutierrez','16723146','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,4,'16-05-05 00:00:00','2015-06-01 00:00:00',5,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Diana Yuan','Sandoval de Min','15723119','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','F');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,4,'16-05-05 00:00:00','2015-06-01 00:00:00',5,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Constansa de la Concepcion','Alvarado Pimentelo','18723182','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','F');

INSERT INTO control.detalles_empleado
(`empresa_id`,`departamento_id`,`fecha_inicio`,`fecha_contrato`,`cargo_id`,`sueldo`,`quincena`,`acumula_decimos`,`nro_cuenta`,`extra`)
VALUES
(1,4,'16-05-05 00:00:00','2015-06-01 00:00:00',5,0.00,0.00, 1, '100000001', '');
INSERT INTO control.usuario
(`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
('Deocles Patricio','Moncler Zacato','11723118','juan@gmail.com','Valencia - Carabobo','04169876432',last_insert_id(), '1971-01-01 00:00:01', '1971-01-01 00:00:01', null,1,true,'1971-01-01 00:00:01','M');




INSERT INTO control.cliente
(`id`,`nombre`,`detalles`,`ruc`,`direccion`,`telefono`,`activo`,`creacion`,`ultima_modificacion`)
VALUES
(1, 'PC Actual', 'Servicios de electronica', 'NIT-454564787', 'Naguanagua - Carabobo', '0245-5896542', 1, '2016-06-10 01:48:08', '2016-06-10 01:48:08');

INSERT INTO control.cliente
(`id`,`nombre`,`detalles`,`ruc`,`direccion`,`telefono`,`activo`,`creacion`,`ultima_modificacion`)
VALUES
(2, 'Empresas Sindoni', 'Servicios de alimentacion', 'NIT-454554787', 'Maracay - Aragua', '0244-5896542', 1, '2016-06-10 01:48:08', '2016-06-10 01:48:08');

INSERT INTO control.cliente
(`id`,`nombre`,`detalles`,`ruc`,`direccion`,`telefono`,`activo`,`creacion`,`ultima_modificacion`)
VALUES
(3, 'CC Sambil Valencia', 'Centro Comercial', 'NIT-124564787', 'Manongo - Carabobo', '0241-5896642', 1, '2016-06-10 01:48:08', '2016-06-10 01:48:08');