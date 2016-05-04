CREATE TABLE `usuario` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`apellido` VARCHAR(255) NOT NULL,
	`cedula` VARCHAR(255) NOT NULL UNIQUE,
	`email` VARCHAR(255) NOT NULL,
	`direccion` VARCHAR(255) NOT NULL,
	`telefono` VARCHAR(255),
	`detalles_empleado_id` INT,
	`creacion` TIMESTAMP NOT NULL,
	`ultima_modificacion` TIMESTAMP NOT NULL,
	`foto` blob,
	`estado_civil_id` INT,
	`activo` BOOLEAN NOT NULL,
	`nacimiento` DATE,
	`sexo` varchar(1),
	PRIMARY KEY (`id`)
);

CREATE TABLE `empresa` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`siglas` VARCHAR(255) NOT NULL,
	`numeracion` INT,
	`telefono_1` VARCHAR(255),
	`telefono_2` VARCHAR(255),
	`fax` VARCHAR(255),
	`email` VARCHAR(255),
	`direccion` VARCHAR(255),
	`web` VARCHAR(255),
	`logo` blob,
	`creacion` TIMESTAMP NOT NULL,
	`ultima_modificacion` TIMESTAMP NOT NULL,
	`observacion` VARCHAR(255),
	`tipo` VARCHAR(255),
	`dia_corte_pago` INT(2) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `detalles_empleado` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`empresa_id` INT NOT NULL,
	`departamento_id` INT,
	`fecha_inicio` TIMESTAMP,
	`fecha_contrato` TIMESTAMP,
	`cargo_id` INT NOT NULL,
	`sueldo` DECIMAL(8,2) NOT NULL,
	`nro_cuenta` VARCHAR(100),
	`extra` TEXT(200),
	PRIMARY KEY (`id`)
);

CREATE TABLE `departamento` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `cargo` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `registro_acciones` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`usuario_id` INT NOT NULL,
	`accion_tipo_id` INT NOT NULL,
	`detalles` VARCHAR(255),
	`fecha` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `accion_tipo` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `estado_civil` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(20) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `cliente` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`detalles` VARCHAR(255) NOT NULL,
	`ruc` INT(13) NOT NULL,
	`direccion` TEXT(100) NOT NULL,
	`telefono` TEXT(100) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	`creacion` TIMESTAMP,
	`ultima_modificacion` TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE `identidad` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre_usuario` VARCHAR(255) NOT NULL,
	`contrasena` VARCHAR(255) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	`usuario_id` INT,
	PRIMARY KEY (`id`)
);

CREATE TABLE `pago` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`inicio` TIMESTAMP NOT NULL,
	`finalizo` TIMESTAMP NOT NULL,
	`detalles` VARCHAR(255),
	`fecha` TIMESTAMP NOT NULL,
	`sueldo` DECIMAL(8,2) NOT NULL,
	`dias` INT NOT NULL,
	`horas_normales` DECIMAL(8,2) NOT NULL,
	`horas_suplementarias` DECIMAL(8,2) NOT NULL,
	`horas_sobre_tiempo` DECIMAL(8,2) NOT NULL,
	`total_horas_extras` DECIMAL(8,2) NOT NULL,
	`salario` DECIMAL(8,2) NOT NULL,
	`monto_horas_suplementarias` DECIMAL(8,2) NOT NULL,
	`monto_horas_sobre_tiempo` DECIMAL(8,2) NOT NULL,
	`bono` DECIMAL(8,2) NOT NULL,
	`transporte` DECIMAL(8,2) NOT NULL,
	`total_bonos` DECIMAL(8,2) NOT NULL,
	`vacaciones` DECIMAL(8,2) NOT NULL,
	`subtotal` DECIMAL(8,2) NOT NULL,
	`decimo_tercero` DECIMAL(8,2) NOT NULL,
	`decimo_cuarto` DECIMAL(8,2) NOT NULL,
	`jubilacion_patronal` DECIMAL(8,2) NOT NULL,
	`aporte_patronal` DECIMAL(8,2) NOT NULL,
	`seguros` DECIMAL(8,2) NOT NULL,
	`uniformes` DECIMAL(8,2) NOT NULL,
	`total_ingreso` DECIMAL(8,2) NOT NULL,
	`empleado` VARCHAR(255) NOT NULL,
	`cedula` VARCHAR(255) NOT NULL,
	`empresa` VARCHAR(255) NOT NULL,
	`cliente_nombre` VARCHAR(255),
	`cliente_id` INT,
	`usuario_id` INT,
	PRIMARY KEY (`id`)
);

CREATE TABLE `roles` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`permiso` TEXT(20) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	`usuario_id` INT NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `control_empleado` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`usuario_id` INT NOT NULL,
	`cliente_id` INT,
	`fecha` DATE NOT NULL,
	`horas_suplementarias` INT,
	`horas_extras` INT,
	`libre` BOOLEAN,
	`vacaciones` BOOLEAN,
	PRIMARY KEY (`id`)
);

CREATE TABLE `bono` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`usuario_id` INT NOT NULL,
	`concepto` TEXT(20) NOT NULL,
	`detalles` TEXT(100) NOT NULL,
	`monto` DECIMAL NOT NULL,
	`fecha` TIMESTAMP NOT NULL,
	`activo` BOOLEAN NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `constante` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL,
	`valor` VARCHAR(255) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `actuariales` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`usuario_id` INT NOT NULL,
	`primario` DECIMAL(8,2) NOT NULL,
	`secundario` DECIMAL(8,2) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	`fecha` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `seguro` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`empresa_id` INT NOT NULL,
	`nombre` VARCHAR(255) NOT NULL,
	`valor` DECIMAL(8,2) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	`fecha` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `uniforme` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`empresa_id` INT NOT NULL,
	`nombre` VARCHAR(255) NOT NULL,
	`valor` DECIMAL(8,2) NOT NULL,
	`activo` BOOLEAN NOT NULL,
	`fecha` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE `usuario` ADD CONSTRAINT `usuario_fk0` FOREIGN KEY (`detalles_empleado_id`) REFERENCES `detalles_empleado`(`id`);

ALTER TABLE `usuario` ADD CONSTRAINT `usuario_fk1` FOREIGN KEY (`estado_civil_id`) REFERENCES `estado_civil`(`id`);

ALTER TABLE `detalles_empleado` ADD CONSTRAINT `detalles_empleado_fk0` FOREIGN KEY (`empresa_id`) REFERENCES `empresa`(`id`);

ALTER TABLE `detalles_empleado` ADD CONSTRAINT `detalles_empleado_fk1` FOREIGN KEY (`departamento_id`) REFERENCES `departamento`(`id`);

ALTER TABLE `detalles_empleado` ADD CONSTRAINT `detalles_empleado_fk2` FOREIGN KEY (`cargo_id`) REFERENCES `cargo`(`id`);

ALTER TABLE `registro_acciones` ADD CONSTRAINT `registro_acciones_fk0` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`);

ALTER TABLE `registro_acciones` ADD CONSTRAINT `registro_acciones_fk1` FOREIGN KEY (`accion_tipo_id`) REFERENCES `accion_tipo`(`id`);

ALTER TABLE `identidad` ADD CONSTRAINT `identidad_fk0` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`);

ALTER TABLE `pago` ADD CONSTRAINT `pago_fk0` FOREIGN KEY (`cliente_id`) REFERENCES `cliente`(`id`);

ALTER TABLE `pago` ADD CONSTRAINT `pago_fk1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`);

ALTER TABLE `roles` ADD CONSTRAINT `roles_fk0` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`);

ALTER TABLE `control_empleado` ADD CONSTRAINT `control_empleado_fk0` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`);

ALTER TABLE `control_empleado` ADD CONSTRAINT `control_empleado_fk1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente`(`id`);

ALTER TABLE `bono` ADD CONSTRAINT `bono_fk0` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`);

ALTER TABLE `actuariales` ADD CONSTRAINT `actuariales_fk0` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`);

ALTER TABLE `seguro` ADD CONSTRAINT `seguro_fk0` FOREIGN KEY (`empresa_id`) REFERENCES `empresa`(`id`);

ALTER TABLE `uniforme` ADD CONSTRAINT `uniforme_fk0` FOREIGN KEY (`empresa_id`) REFERENCES `empresa`(`id`);
