# Arquitectura de TaskFlow

Bienvenido: guía rápida para desarrolladores nuevos sobre estructura, flujo de datos, reglas de negocio, seguridad y tests.

## Capas y paquetes

- `controller` — Entrada HTTP, validación de contratos (DTOs) y respuesta. Ejemplo: `src/main/java/com/taskflow/controller/TaskController.java` (`TaskController`).
- `service` — Casos de uso y orquestación. Ejemplo: `src/main/java/com/taskflow/service/TaskService.java` (`TaskService`).
- `repository` — Acceso a datos con Spring Data JPA. Ejemplo: `src/main/java/com/taskflow/repository/TaskRepository.java` (`TaskRepository`).
- `model` — Entidades del dominio y sus reglas. Ejemplo: `src/main/java/com/taskflow/model/Task.java` (`Task`).
- `dto` — Contratos HTTP (normalmente `record`) con Bean Validation: `src/main/java/com/taskflow/dto`.
- `mapper` — Mapeo manual DTO ↔ Entidad. Ejemplo: `src/main/java/com/taskflow/mapper/TaskMapper.java` (`TaskMapper`).
- `advice` — Manejadores globales de excepciones. Ejemplo: `src/main/java/com/taskflow/advice/GlobalExceptionHandler.java` (`GlobalExceptionHandler`).
- `config` / `security` — Configuración y filtros de seguridad. Ej.: `src/main/java/com/taskflow/config/SecurityConfig.java` (`SecurityConfig`) y `src/main/java/com/taskflow/security/JwtAuthenticationFilter.java` (`JwtAuthenticationFilter`).
- `config` (seed) — Poblado de datos en H2: `src/main/java/com/taskflow/config/DataSeeder.java` (`DataSeeder`).

## Recorrido completo de POST /projects/{projectId}/tasks

1. La petición llega a `POST /projects/{projectId}/tasks` en `src/main/java/com/taskflow/controller/TaskController.java` (`TaskController.createTask`). El cuerpo se valida con `@Valid` sobre el DTO (en `src/main/java/com/taskflow/dto`).
2. `TaskController.createTask` comprueba la existencia del proyecto llamando `projectService.buscarPorId(projectId)` y lanza `ProjectNotFoundException` (404) si no existe.
3. `TaskController.createTask` invoca `taskService.crear(request, projectId)`.
4. Dentro de `TaskService.crear` el mapeo se realiza con `TaskMapper.aEntidadNueva(request, projectId)` y la entidad resultante se persiste con `TaskRepository.save(...)`.
5. `TaskController.createTask` responde `201 Created` con cabecera `Location: /tasks/{id}` y el cuerpo construido mediante `TaskMapper.aResponse(creada)`.

Observaciones: validación de formato (Bean Validation) produce 400; errores de dominio (factory o setStatus) producen excepciones que `GlobalExceptionHandler` traduce a 400 o 422 según corresponda; la existencia de recursos referenciados (project, user) se valida en controlador o servicio según el caso.

## Dónde viven las reglas de negocio

- Las reglas e invariantes principales residen en las entidades del dominio, principalmente en `src/main/java/com/taskflow/model/Task.java` (`Task`). Ejemplos de reglas: creación con `Task.crear(...)`, transition rules en `Task.setStatus(...)`, `Task.estaVencida()`.
- `TaskService` orquesta y puede traducir/exponer errores del dominio (p. ej. captar una excepción checked y lanzar `TaskStateException` para que el advice emita 422). No dupliques lógica de reglas en servicios o controladores.
- `TaskMapper` decide cuándo usar la factory (`aEntidadNueva`) vs. el constructor de rehidratación (`aReemplazo`) para respetar las reglas temporales.

## Seguridad con JWT

- La configuración principal está en `src/main/java/com/taskflow/config/SecurityConfig.java` (`SecurityConfig`).
- El filtro `src/main/java/com/taskflow/security/JwtAuthenticationFilter.java` (`JwtAuthenticationFilter`) extrae el token del encabezado, lo valida y carga el `Authentication` en el `SecurityContext`.
- Endpoints públicos: `/auth/**`, `/info`, Swagger UI y consola H2; todo lo demás requiere token JWT.
- Autorización: reglas finas usan `@PreAuthorize` y el componente `src/main/java/com/taskflow/security/ProjectSecurity.java` (`ProjectSecurity`) para comprobar que quien borra o modifica un proyecto sea el propietario o tenga `ROLE_ADMIN`.
- JWT es sin estado: el servidor no guarda sesiones; confía en la firma y reclamaciones del token.

## Organización de tests

- Unit tests: JUnit 5 + Mockito sin arrancar Spring. Ubicación: `src/test/java/com/taskflow/...` con sufijo `*Test`. Prueban lógica de dominio y servicios aislados.
- Slice tests: `@WebMvcTest` para controladores y `@DataJpaTest` para repositorios. Prueban contratos de capa y mapeos.
- Integration tests: `@SpringBootTest` con perfil `test` y Testcontainers; terminan en `*IT.java` y requieren `-Ddocker.tests=true` para ejecutarse. No se activan en la ejecución por defecto de `mvn test`.

## Consejos prácticos

- Nunca exponer entidades en controladores: usar `TaskMapper` y `record` DTOs.
- POST crea → responde `201 Created` con cabecera `Location` hacia `GET /tasks/{id}`.
- Para levantar datos de ejemplo usar el perfil H2: ejecutar con `-Dspring-boot.run.profiles=h2` (puebla `DataSeeder`).
- Para manejar errores, revisar `src/main/java/com/taskflow/advice/GlobalExceptionHandler.java` (`GlobalExceptionHandler`) y las excepciones custom (`TaskValidationException`, `TaskStateException`, `ProjectNotFoundException`, etc.).

Archivos esenciales para leer primero:
- `src/main/java/com/taskflow/controller/TaskController.java` (`TaskController`)
- `src/main/java/com/taskflow/service/TaskService.java` (`TaskService`)
- `src/main/java/com/taskflow/mapper/TaskMapper.java` (`TaskMapper`)
- `src/main/java/com/taskflow/model/Task.java` (`Task`)
- `src/main/java/com/taskflow/repository/TaskRepository.java` (`TaskRepository`)
- `src/main/java/com/taskflow/advice/GlobalExceptionHandler.java` (`GlobalExceptionHandler`)
- `src/main/java/com/taskflow/config/SecurityConfig.java` (`SecurityConfig`)
- `src/main/java/com/taskflow/security/JwtAuthenticationFilter.java` (`JwtAuthenticationFilter`)
- `src/main/java/com/taskflow/config/DataSeeder.java` (`DataSeeder`)

Si quieres, genero un diagrama de secuencia simplificado del flujo POST o un listado de las excepciones manejadas por `GlobalExceptionHandler`.Las fechas límite se validan en la fábrica de dominio `Task.crear(...)` (`src/main/java/com/taskflow/model/Task.java`).
