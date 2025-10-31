DROP TABLE IF EXISTS "public"."clientes";
-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS clientes_id_seq;

-- Table Definition
CREATE TABLE "public"."clientes" (
    "id" int4 NOT NULL DEFAULT nextval('clientes_id_seq'::regclass),
    "nombrecompleto" varchar(50),
    "dni" varchar(10) NOT NULL,
    "direccion" varchar(30),
    "celular" bpchar(10),
    PRIMARY KEY ("id")
);


-- Indices
CREATE UNIQUE INDEX clientes_dni_key ON public.clientes USING btree (dni);

DROP TABLE IF EXISTS "public"."ordenes_trabajo";
-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS ordenes_trabajo_id_seq;

-- Table Definition
CREATE TABLE "public"."ordenes_trabajo" (
    "id" int4 NOT NULL DEFAULT nextval('ordenes_trabajo_id_seq'::regclass),
    "cliente" int4,
    "documento_id" int4,
    "codigo" varchar(15) NOT NULL,
    "fecha" timestamp DEFAULT CURRENT_TIMESTAMP,
    "descripcion" varchar(50),
    "modelo" varchar(10),
    "marca" varchar(10),
    "rajaduras" bool,
    "manchas" bool,
    "golpes" bool,
    "estado" bool DEFAULT true,
    CONSTRAINT "fk_cliente" FOREIGN KEY ("cliente") REFERENCES "public"."clientes"("id") ON DELETE CASCADE,
    CONSTRAINT "fk_documento" FOREIGN KEY ("documento_id") REFERENCES "public"."documentos"("id") ON DELETE SET NULL,
    PRIMARY KEY ("id")
);


-- Indices
CREATE UNIQUE INDEX ordenes_trabajo_codigo_key ON public.ordenes_trabajo USING btree (codigo);

DROP TABLE IF EXISTS "public"."documentos";
-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS documentos_id_seq;

-- Table Definition
CREATE TABLE "public"."documentos" (
    "id" int4 NOT NULL DEFAULT nextval('documentos_id_seq'::regclass),
    "nombre" varchar(255),
    "ruta_archivo" varchar(255),
    "fecha_subida" timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "public"."informes_diagnostico";
-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS informes_diagnostico_id_seq;

-- Table Definition
CREATE TABLE "public"."informes_diagnostico" (
    "id" int4 NOT NULL DEFAULT nextval('informes_diagnostico_id_seq'::regclass),
    "codigo_orden_trabajo" varchar(15) NOT NULL,
    "fecha" timestamp,
    "estadoactual" varchar,
    "problemasencontrados" varchar,
    "diagnosticotecnico" varchar,
    "recomendaciones" varchar,
    "factibilidadreparacion" varchar,
    "observacionesadicionales" varchar,
    "numeroserie" varchar,
    "equipoirreparable" bool,
    CONSTRAINT "fk_ordentrabajo_informe" FOREIGN KEY ("codigo_orden_trabajo") REFERENCES "public"."ordenes_trabajo"("codigo") ON DELETE CASCADE,
    PRIMARY KEY ("id")
);


-- Indices
CREATE UNIQUE INDEX informes_diagnostico_codigo_orden_trabajo_key ON public.informes_diagnostico USING btree (codigo_orden_trabajo);

DROP TABLE IF EXISTS "public"."orden_trabajo_historial";
-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS orden_trabajo_historial_id_seq;

-- Table Definition
CREATE TABLE "public"."orden_trabajo_historial" (
    "id" int4 NOT NULL DEFAULT nextval('orden_trabajo_historial_id_seq'::regclass),
    "orden_trabajo_id" int4 NOT NULL,
    "fecha_modificacion" timestamp,
    "campo_modificado" varchar(255),
    "valor_anterior" varchar(255),
    "valor_nuevo" varchar(255),
    CONSTRAINT "fk_ordentrabajo_historial" FOREIGN KEY ("orden_trabajo_id") REFERENCES "public"."ordenes_trabajo"("id") ON DELETE CASCADE,
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "public"."usuarios";
-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS usuarios_id_seq;

-- Table Definition
CREATE TABLE "public"."usuarios" (
    "id" int4 NOT NULL DEFAULT nextval('usuarios_id_seq'::regclass),
    "nombrecompleto" varchar(50) NOT NULL,
    "nombreusuario" varchar(20) NOT NULL,
    "correoelectronico" varchar(30) NOT NULL,
    "contrasena" varchar(100) NOT NULL,
    "rol" varchar(30) NOT NULL,
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "public"."proformas_servicio";
-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS proformas_servicio_id_seq;

-- Table Definition
CREATE TABLE "public"."proformas_servicio" (
    "id" int4 NOT NULL DEFAULT nextval('proformas_servicio_id_seq'::regclass),
    "codigo_orden_trabajo" varchar(15),
    "detalleservicio" varchar(30),
    "precioservicio" numeric(10,2),
    "tiempoestimadoentrega" varchar(10),
    "condicionescontratacion" varchar(50),
    "estadopago" varchar(10),
    "fecha" timestamp DEFAULT CURRENT_TIMESTAMP,
    "boletaurl" varchar(100),
    CONSTRAINT "fk_ordentrabajo_proforma" FOREIGN KEY ("codigo_orden_trabajo") REFERENCES "public"."ordenes_trabajo"("codigo") ON DELETE CASCADE,
    PRIMARY KEY ("id")
);


-- Indices
CREATE UNIQUE INDEX proformas_servicio_codigo_orden_trabajo_key ON public.proformas_servicio USING btree (codigo_orden_trabajo);

INSERT INTO "public"."clientes" ("id", "nombrecompleto", "dni", "direccion", "celular") VALUES
(17, 'Rosa Sanchez', '12345678', 'Av. José Larco 1234', '935544572 '),
(20, 'María Quispe', '23456789', 'Calle Puno 567', '958714549 '),
(21, 'Juan Alcantara', '72453945', 'Jirón de la Unión 890', '932132554 '),
(23, 'Lujan Carrion', '76543219', 'Av. La Marina 2468', '961247853 '),
(24, 'Ricardo Arjona', '98765748', 'Av. Puno 567', '923434532 '),
(25, 'Henry Campos', '48574832', 'Av. Carrasco 88', '932424324 '),
(26, 'Sosa Lupuche', '57482373', 'Jirón de la Unión 866', '923432443 '),
(29, 'juan perez', '987654321', 'asdsad', '123456787 ');
INSERT INTO "public"."ordenes_trabajo" ("id", "cliente", "documento_id", "codigo", "fecha", "descripcion", "modelo", "marca", "rajaduras", "manchas", "golpes", "estado") VALUES
(47, 17, 51, 'ORD03893', '2024-11-13 12:00:00', 'Descripción de la orden', 'Modelo 1', 'Marca Z', 'f', 'f', 'f', 't'),
(48, 17, 52, 'ORD97730', '2024-12-10 21:05:00', 'Descripción de la orden', 'Modelo 1', 'Marca A', 'f', 't', 'f', 't'),
(49, 17, 53, 'ORD91050', '2024-12-10 21:05:00', 'Descripción de la orden', 'Modelo 1', 'Marca A', 'f', 't', 'f', 't'),
(50, 17, 54, 'ORD41826', '2025-03-04 11:46:00', 'aaa', 'modelo A', 'Marca A', 'f', 'f', 'f', 't'),
(51, 17, 55, 'ORD75088', '2025-03-04 11:46:00', 'aaa', 'modelo A', 'Marca A', 'f', 'f', 'f', 't');
INSERT INTO "public"."documentos" ("id", "nombre", "ruta_archivo", "fecha_subida") VALUES
(1, 'documento_remision_10033857141121768977.pdf', 'https://drive.google.com/uc?export=view&id=1vbUI_OU82Cy4OPtXZ7lEw-i11MevYEen', '2024-11-14 18:06:45.563151'),
(2, 'documento_remision_16431895427555659628.pdf', 'https://drive.google.com/uc?export=view&id=1y4DjSKmAn9QE7VJTKvKa-7Lh7ExozeZn', '2024-11-14 15:32:11.896663'),
(3, 'documento_remision_3587507041886198311.pdf', 'https://drive.google.com/uc?export=view&id=1oiLZud33QThoP7gPL8IwCPr9IIxR3fJL', '2024-11-14 16:51:42.134508'),
(4, 'documento_remision_9738894777931475376.pdf', 'https://drive.google.com/uc?export=view&id=15-p-fTio2wmqH_jozWi0i_82xotPbMlr', '2024-11-14 18:07:24.433547'),
(5, 'documento_remision_15680242105097231197.pdf', 'https://drive.google.com/uc?export=view&id=1KMhGn063kNYG19PLKuPzXo09ZoIO0lNJ', '2024-11-15 23:32:19.626951'),
(6, 'documento_remision_11694752626377598412.pdf', 'https://drive.google.com/uc?export=view&id=1CpWpJpfrFhg6WN7AIMx7h_jY4qrzN3eO', '2024-11-15 23:35:19.558473'),
(7, 'documento_remision_6060080670255743212.pdf', 'https://drive.google.com/uc?export=view&id=1IwkLOtrImiY-HXI4xCIQq222x4YIk7er', '2024-11-16 00:26:04.827922'),
(8, 'documento_remision_12562287806405084267.pdf', 'https://drive.google.com/uc?export=view&id=1xvLqzNtIFNM9xEvjfrnM7qQGEKLNq51Y', '2024-11-16 19:55:54.526239'),
(9, 'documento_remision_14099328387159879636.pdf', 'https://drive.google.com/uc?export=view&id=1aSG94o7GcPbxffuf-lL7wFlmYeAB_TbK', '2024-11-16 19:59:29.406512'),
(10, 'documento_remision_13341705676816158769.pdf', 'https://drive.google.com/uc?export=view&id=1DA9n0NkdRGKc4bqoHZ2r7dbDXp6zUiHu', '2024-11-16 20:05:16.851621'),
(11, 'documento_remision_7244960043359919857.pdf', NULL, '2024-11-19 12:36:49.359382'),
(12, 'documento_remision_4014136749263547583.pdf', NULL, '2024-11-19 13:42:55.577899'),
(13, 'documento_remision_4076252918600066592.pdf', NULL, '2024-11-19 13:57:12.423972'),
(14, 'documento_remision_1060392132649734656.pdf', NULL, '2024-11-19 14:04:53.085618'),
(15, 'documento_remision_17948689330079866026.pdf', 'https://drive.google.com/uc?export=view&id=1PHSxdAHcXpCHY_RYRjhMCpOPCQ4w0c0U', '2024-11-19 14:10:48.244768'),
(16, 'documento_remision_5156405616609450974.pdf', 'https://drive.google.com/uc?export=view&id=1uKETYTTBJ5iqrvAcCaXx1-FIUbHDKv8A', '2024-11-19 14:12:58.102494'),
(17, 'documento_remision_13401504289906933481.pdf', NULL, '2024-12-08 23:13:44.446694'),
(18, 'documento_remision_2384749672739419854.pdf', NULL, '2024-12-08 23:14:21.063939'),
(19, 'documento_remision_10731232615887023617.pdf', NULL, '2024-12-08 23:16:05.934944'),
(20, 'documento_remision_539639880123388119.pdf', NULL, '2024-12-08 23:20:54.904814'),
(21, 'documento_remision_8257061660685773349.pdf', NULL, '2024-12-08 23:21:50.035948'),
(22, 'documento_remision_17207617249705941844.pdf', NULL, '2024-12-08 23:27:25.27469'),
(23, 'documento_remision_17855945168965731219.pdf', NULL, '2024-12-08 23:27:30.733972'),
(24, 'documento_remision_17468781448168349682.pdf', NULL, '2024-12-08 23:27:33.265167'),
(25, 'documento_remision_5238722160463817768.pdf', NULL, '2024-12-08 23:27:35.818349'),
(26, 'documento_remision_16002540808382465082.pdf', NULL, '2024-12-08 23:27:37.575943'),
(27, 'documento_remision_8199418896662286129.pdf', NULL, '2024-12-08 23:27:52.70623'),
(28, 'documento_remision_8559571317403624202.pdf', NULL, '2024-12-08 23:28:46.497875'),
(29, 'documento_remision_4742701644027554237.pdf', NULL, '2024-12-08 23:34:14.756796'),
(30, 'documento_remision_6504989060069033629.pdf', NULL, '2024-12-08 23:37:19.469339'),
(31, 'documento_remision_12452166738125133617.pdf', NULL, '2024-12-08 23:38:23.940746'),
(32, 'documento_remision_13506213902785539048.pdf', NULL, '2024-12-08 23:38:58.592099'),
(33, 'documento_remision_14286760397693446285.pdf', NULL, '2024-12-08 23:41:59.887859'),
(34, 'documento_remision_18128377815775120940.pdf', NULL, '2024-12-08 23:43:34.90877'),
(35, 'documento_remision_5628683523357113573.pdf', NULL, '2024-12-08 23:46:18.51566'),
(36, 'documento_remision_1581774581624058304.pdf', NULL, '2024-12-08 23:49:40.128938'),
(37, 'documento_remision_4226314370741972807.pdf', NULL, '2024-12-08 23:51:27.478298'),
(38, 'documento_remision_4827151316037885266.pdf', NULL, '2024-12-08 23:52:02.24902'),
(39, 'documento_remision_8073243995712128898.pdf', NULL, '2024-12-08 23:54:41.522652'),
(40, 'documento_remision_5505323442330614433.pdf', NULL, '2024-12-08 23:59:41.787516'),
(41, 'documento_remision_4226617755880095478.pdf', NULL, '2024-12-09 00:04:23.497107'),
(42, 'documento_remision_12861853336438060125.pdf', NULL, '2024-12-09 00:04:51.126895'),
(43, 'documento_remision_950935177958289534.pdf', NULL, '2024-12-09 00:06:00.749229'),
(44, 'documento_remision_1728673696556608636.pdf', NULL, '2024-12-09 00:15:34.576151'),
(45, 'documento_remision_4723881019557798197.pdf', NULL, '2024-12-09 00:16:01.86646'),
(46, 'documento_remision_13375502559474112773.pdf', NULL, '2024-12-09 13:51:14.647923'),
(47, 'documento_remision_5608989417896518151.pdf', NULL, '2024-12-09 21:57:02.449351'),
(48, 'documento_remision_17199703949531576770.pdf', NULL, '2024-12-10 18:45:35.940782'),
(49, 'documento_remision_17443695189358323452.pdf', NULL, '2024-12-10 18:51:39.737011'),
(50, 'documento_remision_2979360836514420962.pdf', NULL, '2024-12-10 13:52:16.946233'),
(51, 'documento_remision_3477675865017379115.pdf', 'https://drive.google.com/uc?export=view&id=1Db7wPXJFoXA9ygtn1e7BwQsn0lBREoV6', '2024-12-10 15:20:49.43364'),
(52, 'documento_remision_6963654168903911576.pdf', 'https://drive.google.com/uc?export=view&id=1ZAfmy8A84jy5ZPsgjslhNoqkK617jS2x', '2024-12-10 18:06:26.419321'),
(53, 'documento_remision_18063310802009158014.pdf', 'https://drive.google.com/uc?export=view&id=1dIDbmR5oOxbdk5uAeL46iPpOQKBvmG4d', '2024-12-10 18:06:30.150269'),
(54, 'documento_remision_17312647066042411253.pdf', 'https://drive.google.com/uc?export=view&id=1GQOzcY9S7B_1gD3PtMAYv4PhrP4qCh7i', '2025-03-04 11:46:56.640559'),
(55, 'documento_remision_15788251099115138473.pdf', 'https://drive.google.com/uc?export=view&id=1UvJ2soo9knod5JGt1MW23RmvfoSOAgnR', '2025-03-04 11:47:00.034927');
INSERT INTO "public"."informes_diagnostico" ("id", "codigo_orden_trabajo", "fecha", "estadoactual", "problemasencontrados", "diagnosticotecnico", "recomendaciones", "factibilidadreparacion", "observacionesadicionales", "numeroserie", "equipoirreparable") VALUES
(28, 'ORD03893', '2024-12-10 19:32:51.468', 'gkgj', 'Soy cabro', 'kgjg', 'kjg', 'Media', NULL, '86', 'f'),
(29, 'ORD91050', '2025-03-04 15:44:35.577', 'asdasd', 'asdas', 'sadasdsad', 'sdfs', 'Media', NULL, '123123', 'f'),
(30, 'ORD97730', '2025-03-04 16:18:35.31', 'asdasd', 'asdasd', 'sdasd', 'asdsad', 'Media', 'https://drive.google.com/uc?export=view&id=10OFWawWU29E6tS84dovvxxy6XarIBcVz', '1231234234', 'f');
INSERT INTO "public"."orden_trabajo_historial" ("id", "orden_trabajo_id", "fecha_modificacion", "campo_modificado", "valor_anterior", "valor_nuevo") VALUES
(51, 47, '2024-12-10 15:20:44.333954', 'documento', 'https://drive.google.com/file/d/1RDSf7DoKPHK2hHXGVqLr1orHZyMSgGLF/view', 'https://drive.google.com/uc?export=view&id=18ccuKgldEMLHSJbyUWVAPMiBuW-W9fTD'),
(52, 47, '2024-12-10 15:20:49.231755', 'documento', 'https://drive.google.com/uc?export=view&id=18ccuKgldEMLHSJbyUWVAPMiBuW-W9fTD', 'https://drive.google.com/uc?export=view&id=1Db7wPXJFoXA9ygtn1e7BwQsn0lBREoV6'),
(53, 47, '2025-02-25 13:52:12.751272', 'manchas', 'false', 'true'),
(54, 47, '2025-02-25 13:52:13.175271', 'rajaduras', 'true', 'false'),
(55, 47, '2025-02-25 13:52:14.17619', 'rajaduras', 'true', 'false'),
(56, 47, '2025-02-25 13:52:21.914979', 'rajaduras', 'false', 'true'),
(57, 47, '2025-02-25 15:05:48.708959', 'manchas', 'true', 'false'),
(58, 47, '2025-02-25 15:05:48.93048', 'golpes', 'true', 'false'),
(59, 47, '2025-02-25 15:05:49.254726', 'rajaduras', 'true', 'false'),
(60, 47, '2025-02-25 15:05:49.580626', 'marca', 'Marca A', 'Marca B'),
(61, 47, '2025-02-25 15:06:05.449807', 'manchas', 'false', 'true'),
(62, 47, '2025-02-25 15:06:05.672639', 'golpes', 'false', 'true'),
(63, 47, '2025-02-25 15:06:06.126859', 'rajaduras', 'false', 'true'),
(64, 47, '2025-02-25 15:06:24.684683', 'marca', 'Marca B', 'Marca C'),
(65, 47, '2025-02-25 15:39:48.557226', 'manchas', 'true', 'false'),
(66, 47, '2025-02-25 15:39:48.829788', 'golpes', 'true', 'false'),
(67, 47, '2025-02-25 15:39:49.179399', 'rajaduras', 'true', 'false'),
(68, 47, '2025-02-25 15:39:49.401619', 'golpes', 'true', 'false'),
(69, 47, '2025-02-25 15:39:49.518058', 'marca', 'Marca C', 'Marca Z'),
(70, 47, '2025-02-25 15:39:49.635084', 'rajaduras', 'true', 'false'),
(71, 47, '2025-02-25 15:39:50.087235', 'marca', 'Marca C', 'Marca Z'),
(72, 47, '2025-02-25 15:39:50.381748', 'rajaduras', 'true', 'false'),
(73, 47, '2025-02-25 15:39:50.637905', 'marca', 'Marca C', 'Marca Z');
INSERT INTO "public"."usuarios" ("id", "nombrecompleto", "nombreusuario", "correoelectronico", "contrasena", "rol") VALUES
(19, 'Gerardo Herrera', 'tecnico', 'Gerardo@promecal.com', '$2a$10$8gesD94k4G3jS0l3psvGvuyaE8hf88uapq0pq9mQWEbhsuWvsV8AO', 'ROLE_ASISTENTE_TECNICO'),
(20, 'Carlos Sosa', 'ventas', 'Carlos@promecal.com', '$2a$10$VQXQPfxsdq5Tfy3kD585PO/YYEx4N9/PpBv0CH34Y/Trx2wYFd3Qe', 'ROLE_EJECUTIVO_DE_VENTAS'),
(21, 'Marlo Mejía', 'recepcion', 'Marlo@promecal.com', '$2a$10$H1D97eeaPfkDzv./jXazHeiN96y.UENjrJW1khx3JCf9Kt60pJ7fW', 'ROLE_ASISTENTE_DE_RECEPCION'),
(27, 'Jefferson Asencios', 'admin', 'jefferson@unmsm.edu.pe', '$2a$10$4k23wlYZWC6SD1joOFJX4.voWTfbm1DmVV1QUZnVFJ65VmJ/JnLki', 'ROLE_ADMINISTRADOR');
INSERT INTO "public"."proformas_servicio" ("id", "codigo_orden_trabajo", "detalleservicio", "precioservicio", "tiempoestimadoentrega", "condicionescontratacion", "estadopago", "fecha", "boletaurl") VALUES
(45, 'ORD03893', 'ADADA', 12331.00, '123', 'ADSADA', 'PAGADO', '2025-03-04 03:03:03', 'https://drive.google.com/uc?export=view&id=1FStPm6jUNkCPtb5uA7OFLdJ8OHtO6YKy');
