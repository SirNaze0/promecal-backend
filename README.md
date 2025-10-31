# Promecal

Proyecto Spring Boot para la gestión de procesos en San Marcos.

## Descripción

**Promecal** es una API backend desarrollada con Spring Boot para la gestión de clientes, usuarios, órdenes de trabajo, informes diagnósticos y proformas de servicio en San Marcos. El sistema permite registrar, consultar, actualizar y eliminar información relevante para la operación y administración de servicios técnicos.

## Estructura

- **src/main/java/com/sanmarcos/promecal/**  
  Código fuente principal:

  - `PromecalApplication.java`: Clase principal de arranque.
  - `config/`: Configuración de la aplicación.
  - `controller/`: Controladores REST.
  - `exception/`: Manejo de excepciones.
  - `model/`: Entidades y modelos de datos.
  - `repository/`: Repositorios JPA.
  - `security/`: Seguridad y autenticación.
  - `service/`: Lógica de negocio.

- **src/main/resources/**  
  Recursos:

  - `application.yml`: Configuración de la aplicación.
  - `data.sql`: Datos iniciales.

- **src/test/java/com/sanmarcos/promecal/**  
  Pruebas unitarias y de integración.

## Endpoints principales

### Autenticación

- `POST /auth/login` — Iniciar sesión.
- `POST /auth/register` — Registrar usuario.

### Usuarios

- `GET /api/usuarios` — Listar usuarios.
- `POST /api/usuarios` — Crear usuario.
- `GET /api/usuarios/{id}` — Obtener usuario por ID.
- `PUT /api/usuarios/{id}` — Actualizar usuario.
- `DELETE /api/usuarios/{id}` — Eliminar usuario.

### Clientes

- `GET /api/clientes` — Listar clientes.
- `POST /api/clientes` — Crear cliente.
- `GET /api/clientes/{id}` — Obtener cliente por ID.
- `PUT /api/clientes/{id}` — Actualizar cliente.
- `DELETE /api/clientes/{id}` — Eliminar cliente.

### Proforma de Servicio

- `GET /api/proformaservicio` — Listar proformas.
- `POST /api/proformaservicio` — Crear proforma.
- `GET /api/proformaservicio/{id}` — Obtener proforma por ID.
- `PUT /api/proformaservicio/{id}` — Actualizar proforma.
- `DELETE /api/proformaservicio/{id}` — Eliminar proforma.
- `GET /api/proformaservicio/cliente/{dni}` — Proformas por cliente.
- `POST /api/proformaservicio/{id}/pago` — Registrar pago de proforma.

### Órdenes de Trabajo

- `GET /api/ordentrabajo` — Listar órdenes.
- `POST /api/ordentrabajo` — Crear orden.
- `GET /api/ordentrabajo/codigos` — Obtener códigos de orden.
- `DELETE /api/ordentrabajo/{id}` — Eliminar orden.
- `GET /api/ordentrabajo/{id}` — Obtener orden por ID.
- `GET /api/ordentrabajo/{id}/historial` — Historial de la orden.
- `PUT /api/ordentrabajo/{id}` — Actualizar orden.

### Informe Diagnóstico

- `POST /api/informediagnostico` — Crear informe diagnóstico.

## Requisitos

- Java 17+
- Maven

## Ejecución

```sh
./mvnw spring-boot:run
```

## Docker

Construye y ejecuta el contenedor:

```sh
docker build -t promecal .
docker run -p 8080:8080 promecal
```

## Licencia

MIT
