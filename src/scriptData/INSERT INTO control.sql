INSERT INTO control.departamento
(`id`,`nombre`,`activo`)
VALUES
(1, 'ADMINISTRATIVO', true);
INSERT INTO control.departamento
(`id`,`nombre`,`activo`)
VALUES
(2, 'OPERATIVO', true);
INSERT INTO control.departamento
(`id`,`nombre`,`activo`)
VALUES
(3, 'CONTABILIDAD', true);
INSERT INTO control.departamento
(`id`,`nombre`,`activo`)
VALUES
(4, 'SISTEMAS', true);
INSERT INTO control.departamento
(`id`,`nombre`,`activo`)
VALUES
(5, 'RECURSOS HUMANOS', true);



INSERT INTO control.cargo
(`id`,`nombre`,`activo`)
VALUES
(1, 'SUPERVISOR SF', true);
INSERT INTO control.cargo
(`id`,`nombre`,`activo`)
VALUES
(2, 'GUARDIA', true);
INSERT INTO control.cargo
(`id`,`nombre`,`activo`)
VALUES
(3, 'PROTECTORES', true);
INSERT INTO control.cargo
(`id`,`nombre`,`activo`)
VALUES
(4, 'BUNKER', true);
INSERT INTO control.cargo
(`id`,`nombre`,`activo`)
VALUES
(5, 'ANALISTA', true);



INSERT INTO control.estado_civil
(`id`,`nombre`,`activo`)
VALUES
(1, 'SOLTERO(A)', true);
INSERT INTO control.estado_civil
(`id`,`nombre`,`activo`)
VALUES
(2, 'CASADO(A)', true);
INSERT INTO control.estado_civil
(`id`,`nombre`,`activo`)
VALUES
(3, 'DIVORSIADO(A)', true);
INSERT INTO control.estado_civil
(`id`,`nombre`,`activo`)
VALUES
(4, 'VIUDO(A)', true);


INSERT INTO control.usuario
(`id`,`nombre`,`apellido`,`cedula`,`email`,`direccion`,`telefono`,`detalles_empleado_id`,`creacion`,`ultima_modificacion`,`foto`,`estado_civil_id`,`activo`,`nacimiento`,`sexo`)
VALUES
(1,'admi','admi','admi','admi','admi','admi',null, '1970-01-01 00:00:01', '1970-01-01 00:00:01',null,1,true,'1970-01-01 00:00:01','M');


INSERT INTO control.identidad
(`id`,`nombre_usuario`,`contrasena`,`activo`,`usuario_id`)
VALUES
(1, 'admi','60eb0f73e33ce3ffd4e51d974447db53', true, 1);

INSERT INTO control.roles
(`id`,`nombre`,`permiso`,`activo`,`usuario_id`)
VALUES
(1, 'total','', true, 1);


INSERT INTO control.constante
(`id`,`nombre`,`valor`,`activo`)
VALUES
(1, 'decimo_cuarto', '30.5', 1);




INSERT INTO control.accion_tipo
(`id`,`nombre`,`activo`)
VALUES
(1, 'INGRESO', true);

INSERT INTO control.accion_tipo
(`id`,`nombre`,`activo`)
VALUES
(2, 'AGREGO', true);

INSERT INTO control.accion_tipo
(`id`,`nombre`,`activo`)
VALUES
(3, 'EDITO', true);

INSERT INTO control.accion_tipo
(`id`,`nombre`,`activo`)
VALUES
(4, 'ELIMINO', true);