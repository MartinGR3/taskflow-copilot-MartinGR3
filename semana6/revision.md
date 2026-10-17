# Copilot CLI Session

> [!NOTE]
> - **Session ID:** `72711cbc-3304-4b66-9c07-02f9133830c2`  
> - **Started:** 17/10/2026, 12:52:57 p.m.  
> - **Duration:** 46s  
> - **Exported:** 17/10/2026, 12:53:43 p.m.  

---

<sub>2s</sub>

### User

Revisa semana6/proyecto-final.diff contra la especificación specs/search.md.

---

<sub>7s</sub>

### Copilot

Reading the provided diff and the search spec in parallel to compare changes against the specification. Running two file reads: the diff and specs/search.md.

---

<sub>7s</sub>

### `view` — Failed

**view the file at C:\Users\marti\taskflow-copilot-MartinGR3\week6\proyecto-final.diff.**

C:\Users\marti\taskflow-copilot-MartinGR3\week6\proyecto-final.diff

```
Path does not exist
```


---

<sub>7s</sub>

### `view`

**view the file at C:\Users\marti\taskflow-copilot-MartinGR3\specs\search.md.**

C:\Users\marti\taskflow-copilot-MartinGR3\specs\search.md

<details>
<summary>69 lines</summary>

````
# Especificación — `GET /tasks/search?q=`

## Qué

Un endpoint que busca tareas por su título, en todos los proyectos, para encontrar una tarea sin
recorrer la lista completa. «api» encuentra «Documentar la API con Swagger».

```
GET /tasks/search?q=api
Authorization: ******
```

## Reglas

1. Coincide toda tarea cuyo `title` **contiene** `q`, sin distinguir mayúsculas de minúsculas. La
   consulta ya existe en el repositorio: `TaskRepository.findByTitleContainingIgnoreCase(String)`.
   Úsala; no escribas SQL, `@Query` ni un filtro sobre `findAll()`.
2. Antes de buscar, `q` se recorta con `strip()`: `"  api "` busca `"api"`.
3. `q` es obligatorio. Si falta, o si después de recortarlo queda vacío, responde **`400`** con el
   `ErrorResponse` uniforme de siempre y el mensaje `El parámetro 'q' es obligatorio.`. Para eso el
   servicio lanza `TaskValidationException`, que el `GlobalExceptionHandler` ya convierte en 400.
4. Orden: alfabético por título, con el comparador que ya existe, `TaskOrders.POR_TITULO`.
5. Respuesta: `200` con una lista de `TaskResponse` (el mismo DTO que `GET /tasks`, con el mapper que
   ya existe, `TaskMapper.aResponse`). Si nada coincide, `200` con `[]`.
6. Seguridad: igual que el resto de `/tasks` (sin token → `401`). No cambies `SecurityConfig`.

## Dónde

- **Sin DTO ni mapper nuevos**: la respuesta es una lista del `TaskResponse` que ya existe.
- Método nuevo en `TaskService`:
  `public List<Task> buscarPorTitulo(String q) throws TaskValidationException`.
- Endpoint nuevo en `TaskController`: `@GetMapping("/tasks/search")`, con
  `@RequestParam(name = "q", required = false) String q`. El `required = false` es a propósito: así
  el «falta `q`» llega al servicio y sale con el mismo 400 uniforme que el «`q` vacío».
- Hoy `GET /tasks/search` responde `400` con «El parámetro 'id' tiene un valor ilegible»: lo atrapa
  `GET /tasks/{id}`. Con el endpoint nuevo, Spring prefiere la ruta literal `/tasks/search` sin
  importar en qué orden declares los métodos. No escribas un comentario que diga lo contrario.

> **Por qué no se reutiliza `ReportService.buscarPorTitulo`**, que hace casi lo mismo: habría que
> inyectar `ReportService` en `TaskController`, y el slice `TaskControllerTest` (que no puedes tocar)
> dejaría de arrancar. Medido: sus 10 tests fallan con `No qualifying bean of type
> 'com.taskflow.service.ReportService'`. Es la regla «no agregues dependencias nuevas al constructor
> de un controller que ya existe» de la skill `crear-endpoint-taskflow`.

## Tests que deben existir al terminar

Los dos en **clases nuevas** (las que ya existen no se tocan):

- **Unit** `src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java`, con
  `@Mock TaskRepository` e `@InjectMocks TaskService`:
  - el repositorio devuelve dos tareas en orden **no** alfabético y el servicio las regresa ordenadas
    por título (el test falla si quitas el orden);
  - `"  api "` llama al repositorio con `"api"` (`verify`);
  - `null` y `"   "` lanzan `TaskValidationException` y el repositorio **nunca** se llama.
- **Slice** `src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java`, con
  `@WebMvcTest(TaskController.class)`, `@AutoConfigureMockMvc(addFilters = false)` y `@MockitoBean`
  de `TaskService`, `ProjectService` (el constructor de `TaskController` recibe los dos) y
  `JwtAuthenticationFilter`:
  - `GET /tasks/search?q=api` → `200` y el JSON trae los ids y títulos que devolvió el servicio, en
    ese orden;
  - `GET /tasks/search` sin `q`, con el servicio lanzando `TaskValidationException` → `400` y
    `$.message` = `El parámetro 'q' es obligatorio.`.

## Resultado esperado con la semilla

Con la app arrancada con el perfil `h2` y el token de `ana`:

| Petición | Resultado |
|---|---|
| `GET /tasks/search?q=api` | `200`, ids `[9, 5]`: «Documentar la API con Swagger» y «Optimizar consultas de la API», en ese orden |
| `GET /tasks/search?q=API` | lo mismo: `[9, 5]` |
| `GET /tasks/search?q=zzz` | `200` con `[]` |
| `GET /tasks/search?q=%20%20` | `400` con `message` = `El parámetro 'q' es obligatorio.` |
| `GET /tasks/search` | `400` con el mismo mensaje |
| `GET /tasks/search?q=api` sin token | `401` |

## Restricciones para el agente

- No modifiques, borres ni desactives ningún test existente.
- No toques archivos fuera de `TaskService.java`, `TaskController.java` y las dos clases de test
  nuevas.
- Al terminar, `mvn -q test` tiene que pasar completo.

El pull request del proyecto final lleva además `semana6/`, `.github/skills/verificar-taskflow/casos-search.ps1`
y una línea nueva en `verificar.ps1`. Esos archivos los agregas tú en el proceso (PF-2 a PF-7 de la guía),
no el agente: no cuentan contra esta restricción.
````

</details>


---

<sub>10s</sub>

### Copilot

Abrir el diff solicitado para revisar los cambios contra la especificación. Leer semana6/proyecto-final.diff para compararlo con specs/search.md.

---

<sub>10s</sub>

### `view`

**view the file at C:\Users\marti\taskflow-copilot-MartinGR3\semana6\proyecto-final.diff.**

C:\Users\marti\taskflow-copilot-MartinGR3\semana6\proyecto-final.diff

<details>
<summary>226 lines</summary>

```diff
diff --git a/src/main/java/com/taskflow/controller/ProjectController.java b/src/main/java/com/taskflow/controller/ProjectController.java
index 26cdc94..3285ebc 100644
--- a/src/main/java/com/taskflow/controller/ProjectController.java
+++ b/src/main/java/com/taskflow/controller/ProjectController.java
@@ -136,7 +136,7 @@ public class ProjectController {
      */
     @Operation(summary = "Borra un proyecto y sus tareas",
             description = "Solo ADMIN o el owner. Un USER no-owner recibe 403.")
-    @PreAuthorize("hasRole('ADMIN') or @projectSecurity.esOwner(#id, authentication.name)")
+    @PreAuthorize("hasRole('ADMIN') or @projectSecurity.esOwner(#a0, authentication.name)")
     @DeleteMapping("/projects/{id}")
     public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
         projectService.eliminar(id);
diff --git a/src/main/java/com/taskflow/controller/TaskController.java b/src/main/java/com/taskflow/controller/TaskController.java
index 2b5389e..fe7b851 100644
--- a/src/main/java/com/taskflow/controller/TaskController.java
+++ b/src/main/java/com/taskflow/controller/TaskController.java
@@ -72,6 +72,20 @@ public class TaskController {
         return tareas.stream().map(TaskMapper::aResponse).toList();
     }
 
+    /**
+     * GET /tasks/search?q= — busca tareas cuyo título contiene q (case-insensitive). q se recibe
+     * con required = false para que la validación la haga el servicio (lanza
+     * TaskValidationException -> advice -> 400 con el ErrorResponse uniforme).
+     */
+    @Operation(summary = "Busca tareas por título",
+            description = "Busca tareas cuyo título contiene el parámetro q (case-insensitive).")
+    @GetMapping("/tasks/search")
+    public List<TaskResponse> searchTasks(@RequestParam(name = "q", required = false) String q)
+            throws TaskValidationException {
+        List<Task> tareas = taskService.buscarPorTitulo(q);
+        return tareas.stream().map(TaskMapper::aResponse).toList();
+    }
+
     /** GET /tasks/overdue — lista tareas vencidas de todos los proyectos. */
     @Operation(summary = "Lista tareas vencidas",
             description = "Lista tareas cuya Task.estaVencida() es true, ordenadas por dueDate asc.")
diff --git a/src/main/java/com/taskflow/security/ProjectSecurity.java b/src/main/java/com/taskflow/security/ProjectSecurity.java
index d04fe4c..2a0828d 100644
--- a/src/main/java/com/taskflow/security/ProjectSecurity.java
+++ b/src/main/java/com/taskflow/security/ProjectSecurity.java
@@ -35,13 +35,23 @@ public class ProjectSecurity {
      * borrar algo que no existe"). El "no encontrado" lo decide la capa de negocio, no la de seguridad.
      */
     public boolean esOwner(Long projectId, String username) {
-        return projectRepository.findById(projectId)
+        if (projectId == null) {
+            // Si el id es null, no llamamos al repositorio para evitar excepciones en la evaluación de SpEL.
+            // Devolver true deja hablar al servicio (404) como en el caso de proyecto inexistente.
+            System.out.println("ProjectSecurity.esOwner -> projectId is null, returning true");
+            return true;
+        }
+        boolean result = projectRepository.findById(projectId)
                 .map(proyecto -> {
                     Long userId = userRepository.findByUsername(username)
                             .map(User::getId)
                             .orElse(null);
-                    return proyecto.getOwnerId().equals(userId);
+                    boolean r = proyecto.getOwnerId().equals(userId);
+                    System.out.println("ProjectSecurity.esOwner -> projectId=" + projectId + ", username=" + username + ", ownerId=" + proyecto.getOwnerId() + ", userId=" + userId + ", result=" + r);
+                    return r;
                 })
                 .orElse(true);   // proyecto inexistente -> deja hablar al 404 del servicio
+        System.out.println("ProjectSecurity.esOwner -> final result for projectId=" + projectId + ": " + result);
+        return result;
     }
 }
diff --git a/src/main/java/com/taskflow/service/TaskService.java b/src/main/java/com/taskflow/service/TaskService.java
index e0183c8..b8e440d 100644
--- a/src/main/java/com/taskflow/service/TaskService.java
+++ b/src/main/java/com/taskflow/service/TaskService.java
@@ -144,4 +144,19 @@ public class TaskService {
                 .sorted(TaskOrders.POR_FECHA)
                 .toList();
     }
+
+    /**
+     * Busca tareas cuyo título contiene el fragmento q (case-insensitive). Antes de buscar
+     * se recorta q con strip(); si q es null o queda vacío, lanza TaskValidationException con el
+     * mensaje esperado por la especificación.
+     */
+    public List<Task> buscarPorTitulo(String q) throws TaskValidationException {
+        if (q == null || q.strip().isEmpty()) {
+            throw new TaskValidationException("El parámetro 'q' es obligatorio.");
+        }
+        String trimmed = q.strip();
+        return repository.findByTitleContainingIgnoreCase(trimmed).stream()
+                .sorted(TaskOrders.POR_TITULO)
+                .toList();
+    }
 }
diff --git a/src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java b/src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java
new file mode 100644
index 0000000..c8472a2
--- /dev/null
+++ b/src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java
@@ -0,0 +1,70 @@
+package com.taskflow.slice;
+
+import com.taskflow.controller.TaskController;
+import com.taskflow.exception.TaskValidationException;
+import com.taskflow.model.Priority;
+import com.taskflow.model.Task;
+import com.taskflow.model.TaskStatus;
+import com.taskflow.security.JwtAuthenticationFilter;
+import com.taskflow.service.ProjectService;
+import com.taskflow.service.TaskService;
+import org.junit.jupiter.api.Test;
+import org.springframework.beans.factory.annotation.Autowired;
+import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
+import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
+import org.springframework.http.MediaType;
+import org.springframework.test.context.bean.override.mockito.MockitoBean;
+import org.springframework.test.web.servlet.MockMvc;
+
+import java.time.LocalDate;
+import java.util.List;
+
+import static org.hamcrest.Matchers.nullValue;
+import static org.mockito.Mockito.when;
+import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
+import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
+import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
+
+@WebMvcTest(TaskController.class)
+@AutoConfigureMockMvc(addFilters = false)
+class BusquedaTareasControllerTest {
+
+    @Autowired
+    private MockMvc mockMvc;
+
+    @MockitoBean
+    private TaskService taskService;
+
+    @MockitoBean
+    private ProjectService projectService;
+
+    @MockitoBean
+    private JwtAuthenticationFilter jwtAuthenticationFilter;
+
+    @Test
+    void get_search_con_q_devuelve200YLista() throws Exception {
+        LocalDate hoy = LocalDate.now();
+        try {
+            Task a = new Task(9L, "Documentar la API con Swagger", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, hoy);
+            Task b = new Task(5L, "Optimizar consultas de la API", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, hoy);
+            when(taskService.buscarPorTitulo("api")).thenReturn(List.of(a, b));
+
+            mockMvc.perform(get("/tasks/search?q=api").contentType(MediaType.APPLICATION_JSON))
+                    .andExpect(status().isOk())
+                    .andExpect(jsonPath("$.length()").value(2))
+                    .andExpect(jsonPath("$[0].id").value(9))
+                    .andExpect(jsonPath("$[0].title").value("Documentar la API con Swagger"));
+        } catch (TaskValidationException e) {
+            throw new RuntimeException(e);
+        }
+    }
+
+    @Test
+    void get_search_sin_q_devuelve400ConMensaje() throws Exception {
+        when(taskService.buscarPorTitulo(null)).thenThrow(new TaskValidationException("El parámetro 'q' es obligatorio."));
+
+        mockMvc.perform(get("/tasks/search").contentType(MediaType.APPLICATION_JSON))
+                .andExpect(status().isBadRequest())
+                .andExpect(jsonPath("$.message").value("El parámetro 'q' es obligatorio."));
+    }
+}
diff --git a/src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java b/src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java
new file mode 100644
index 0000000..0fcdacc
--- /dev/null
+++ b/src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java
@@ -0,0 +1,53 @@
+package com.taskflow.unit;
+
+import com.taskflow.exception.TaskValidationException;
+import com.taskflow.model.Priority;
+import com.taskflow.model.Task;
+import com.taskflow.model.TaskStatus;
+import com.taskflow.repository.TaskRepository;
+import com.taskflow.service.TaskOrders;
+import com.taskflow.service.TaskService;
+import org.junit.jupiter.api.Test;
+import org.junit.jupiter.api.extension.ExtendWith;
+import org.mockito.InjectMocks;
+import org.mockito.Mock;
+import org.mockito.junit.jupiter.MockitoExtension;
+
+import java.time.LocalDate;
+import java.util.List;
+
+import static org.assertj.core.api.Assertions.assertThat;
+import static org.junit.jupiter.api.Assertions.assertThrows;
+import static org.mockito.Mockito.verify;
+import static org.mockito.Mockito.when;
+
+@ExtendWith(MockitoExtension.class)
+class BusquedaTareasServiceTest {
+
+    @Mock
+    private TaskRepository repo;
+
+    @InjectMocks
+    private TaskService service;
+
+    @Test
+    void buscarPorTitulo_convierteYOrdena_porTitulo() throws Exception {
+        Task t1 = new Task(5L, "Optimizar consultas de la API", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, LocalDate.now());
+        Task t2 = new Task(9L, "Documentar la API con Swagger", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, LocalDate.now());
+        // repo devuelve en orden no alfabético
+        when(repo.findByTitleContainingIgnoreCase("api")).thenReturn(List.of(t1, t2));
+
+        List<Task> resultado = service.buscarPorTitulo("  api ");
+
+        // deben venir ordenadas por título (Documentar... antes de Optimizar...)
+        assertThat(resultado).containsExactly(t2, t1);
+        verify(repo).findByTitleContainingIgnoreCase("api");
+    }
+
+    @Test
+    void buscarPorTitulo_null_o_vacio_lanzaValidation() {
+        assertThrows(TaskValidationException.class, () -> service.buscarPorTitulo(null));
+        assertThrows(TaskValidationException.class, () -> service.buscarPorTitulo("   "));
+        // repository nunca se llama en estos casos
+    }
+}
```

</details>


---

<sub>15s</sub>

### Copilot

Reading the current versions of the affected production files for context: ProjectController, TaskController, ProjectSecurity, TaskService, and TaskRepository.

---

<sub>15s</sub>

### `view`

**view the file at C:\Users\marti\taskflow-copilot-MartinGR3\src\main\java\com\taskflow\controller\ProjectController.java.**

C:\Users\marti\taskflow-copilot-MartinGR3\src\main\java\com\taskflow\controller\ProjectController.java

<details>
<summary>132 lines</summary>

```
package com.taskflow.controller;

import com.taskflow.dto.ProjectRequest;
import com.taskflow.dto.ProjectResponse;
import com.taskflow.dto.TaskResponse;
import com.taskflow.exception.ProjectNotFoundException;
import com.taskflow.mapper.ProjectMapper;
import com.taskflow.mapper.TaskMapper;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.taskflow.dto.ProjectSummaryResponse;

import java.net.URI;
import java.util.List;

/**
 * ProjectController — la puerta HTTP de los proyectos: CRUD completo, en DTOs
 * (ProjectRequest / ProjectResponse); las tareas de un proyecto salen como TaskResponse.
 *
 * Repetición deliberada del patrón de TaskController: mismos gestos (201 + Location, @Valid, 404 vía
 * orElseThrow -> advice). Con el lado Project queda completa la tabla de endpoints de la API (menos /auth/*).
 */
@RestController
@Tag(name = "Projects", description = "CRUD de proyectos y sus tareas. Borrar un proyecto arrastra sus tareas (cascada).")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /** GET /projects — todos los proyectos como ProjectResponse. */
    @Operation(summary = "Lista todos los proyectos",
            description = "Devuelve todos los proyectos como ProjectResponse (lista vacía si no hay ninguno).")
    @GetMapping("/projects")
    public List<ProjectResponse> getProjects() {
        return projectService.listar().stream()
                .map(ProjectMapper::aResponse)
                .toList();
    }

    /** GET /projects/{id} — 200 con ProjectResponse; 404 uniforme si no existe. */
    @Operation(summary = "Obtiene un proyecto por id",
            description = "200 con el ProjectResponse; 404 uniforme si el id no existe.")
    @GetMapping("/projects/{id}")
    public ProjectResponse getProject(@PathVariable("id") Long id) {
        Project proyecto = projectService.buscarPorId(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectMapper.aResponse(proyecto);
    }

    /**
     * GET /projects/{id}/tasks — las tareas de un proyecto como TaskResponse. Distinción clave:
     * proyecto inexistente -> 404 (orElseThrow); proyecto sin tareas -> 200 con []. El filtro
     * ?status= es opcional (mismo enum que /tasks). Delega en ProjectService.tareasDe.
     */
    @Operation(summary = "Lista las tareas de un proyecto",
            description = "Tareas del proyecto como TaskResponse; 404 si el proyecto no existe, 200 con [] si no tiene tareas. Filtro opcional ?status=.")
    @GetMapping("/projects/{id}/tasks")
    public List<TaskResponse> getTareasDeProyecto(
            @PathVariable("id") Long id,
            @RequestParam(name = "status", required = false) TaskStatus status) {   // status: opcional
        projectService.buscarPorId(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        List<Task> tareas = projectService.tareasDe(id);
        if (status != null) {                          // filtro opcional por estado
            tareas = tareas.stream().filter(t -> t.getStatus() == status).toList();
        }
        return tareas.stream().map(TaskMapper::aResponse).toList();
    }

    @Operation(summary = "Resumen de un proyecto",
            description = "Devuelve el conteo de tareas por estado y cuántas están vencidas.")
    @GetMapping("/projects/{id}/summary")
    public ProjectSummaryResponse getProjectSummary(@PathVariable("id") Long id) {
        Project proyecto = projectService.buscarPorId(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return projectService.resumenDe(proyecto);
    }

    /**
     * POST /projects — 201 + Location a /projects/{id}. @Valid dispara Bean Validation (400 si falla).
     * El owner sale del JWT — el Authentication (inyectado por Spring Security) trae el username
     * del token; el service lo resuelve a ownerId. El dueño es QUIEN crea, no una constante.
     */
    @Operation(summary = "Crea un proyecto",
            description = "El owner se toma del usuario autenticado (el username del JWT).")
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request,
                                                         Authentication authentication) {
        Project creado = projectService.crear(request, authentication.getName());
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/projects/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(location).body(ProjectMapper.aResponse(creado));
    }

    /** PUT /projects/{id} — reemplazo COMPLETO. 200 con ProjectResponse; 404 si no existe. */
    @Operation(summary = "Reemplaza un proyecto",
            description = "Conserva id, owner y createdAt; name y description vienen del cuerpo.")
    @PutMapping("/projects/{id}")
    public ProjectResponse updateProject(@PathVariable("id") Long id,
                                         @Valid @RequestBody ProjectRequest request) {
        Project actualizado = projectService.reemplazar(id, request);
        return ProjectMapper.aResponse(actualizado);
    }

    /**
     * DELETE /projects/{id} — 204 No Content; 404 si no existe. Borra en cascada las tareas del
     * proyecto (regla "no Task sin Project").
     *
     * La regla de negocio, aplicada: @PreAuthorize se evalúa ANTES del método: pasa si el
     * usuario es ADMIN o es el owner del proyecto (bean @projectSecurity, data-driven). Un USER que no
     * es owner -> 403 (la API SÍ sabe quién es, por eso 403 y no 401). Proyecto inexistente ->
     * esOwner devuelve true a propósito para que hable el 404 del servicio.
     */
    @Operation(summary = "Borra un proyecto y sus tareas",
            description = "Solo ADMIN o el owner. Un USER no-owner recibe 403.")
    @PreAuthorize("hasRole('ADMIN') or @projectSecurity.esOwner(#a0, authentication.name)")
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        projectService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
```

</details>


---

<sub>18s</sub>

### `view`

**view the file at C:\Users\marti\taskflow-copilot-MartinGR3\src\main\java\com\taskflow\controller\TaskController.java.**

C:\Users\marti\taskflow-copilot-MartinGR3\src\main\java\com\taskflow\controller\TaskController.java

<details>
<summary>155 lines</summary>

```
package com.taskflow.controller;

import com.taskflow.dto.TaskRequest;
import com.taskflow.dto.TaskResponse;
import com.taskflow.dto.TaskStatusUpdateRequest;
import com.taskflow.exception.ProjectNotFoundException;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.mapper.TaskMapper;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * TaskController — la PUERTA HTTP de las tareas: CRUD completo, y la entidad Task NO sale por la
 * puerta: toda firma habla en DTOs (TaskRequest / TaskResponse).
 *
 * Contrato de responsabilidades: el controller traduce HTTP <-> dominio y delega. Además de
 * TaskService, inyecta ProjectService para el cableo Task↔Project (validar que el
 * proyecto exista al crear una tarea bajo su path).
 *
 * La checked TaskValidationException se declara con throws y SUBE al GlobalExceptionHandler (que la
 * mapea a 400). No se atrapa aquí porque tiene un handler global esperándola: una excepción se
 * maneja en la capa que PUEDE hacer algo con ella, y ésa es el advice.
 */
@RestController
@Tag(name = "Tasks", description = "CRUD de tareas: crear bajo un proyecto, leer, reemplazar, cambiar estado y borrar.")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    /** GET /tasks — todas o filtradas por ?status= / ?priority=. Devuelve TaskResponse. */
    @Operation(summary = "Lista tareas",
            description = "Todas las tareas, o filtradas por ?status= / ?priority=. Devuelve TaskResponse.")
    @GetMapping("/tasks")
    public List<TaskResponse> getTasks(
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) Priority priority) {
        List<Task> tareas;
        if (status != null) {
            tareas = taskService.porEstado(status);
        } else if (priority != null) {                 // mismo patrón que ?status=
            tareas = taskService.porPrioridad(priority);
        } else {
            tareas = taskService.listar();
        }
        return tareas.stream().map(TaskMapper::aResponse).toList();
    }

    /**
     * GET /tasks/search?q= — busca tareas cuyo título contiene q (case-insensitive). q se recibe
     * con required = false para que la validación la haga el servicio (lanza
     * TaskValidationException -> advice -> 400 con el ErrorResponse uniforme).
     */
    @Operation(summary = "Busca tareas por título",
            description = "Busca tareas cuyo título contiene el parámetro q (case-insensitive).")
    @GetMapping("/tasks/search")
    public List<TaskResponse> searchTasks(@RequestParam(name = "q", required = false) String q)
            throws TaskValidationException {
        List<Task> tareas = taskService.buscarPorTitulo(q);
        return tareas.stream().map(TaskMapper::aResponse).toList();
    }

    /** GET /tasks/overdue — lista tareas vencidas de todos los proyectos. */
    @Operation(summary = "Lista tareas vencidas",
            description = "Lista tareas cuya Task.estaVencida() es true, ordenadas por dueDate asc.")
    @GetMapping("/tasks/overdue")
    public List<TaskResponse> getOverdueTasks() {
        List<Task> tareas = taskService.vencidas();
        return tareas.stream().map(TaskMapper::aResponse).toList();
    }

    @Operation(summary = "Lista tareas sin responsable",
            description = "Lista tareas sin responsable de todos los proyectos, ordenadas por dueDate asc (nulls al final).")
    @GetMapping("/tasks/unassigned")
    public List<TaskResponse> getUnassignedTasks() {
        List<Task> tareas = taskService.sinResponsable();
        return tareas.stream().map(TaskMapper::aResponse).toList();
    }

    /** GET /tasks/{id} — 200 con TaskResponse, o 404 uniforme (orElseThrow -> advice). */
    @Operation(summary = "Obtiene una tarea por id",
            description = "200 con el TaskResponse; 404 uniforme si el id no existe.")
    @GetMapping("/tasks/{id}")
    public TaskResponse getTaskPorId(@PathVariable("id") Long id) {
        Task task = taskService.buscarPorId(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.aResponse(task);
    }

    /**
     * POST /projects/{projectId}/tasks — crea una tarea bajo un proyecto. 201 + header Location
     * apuntando a donde el recurso SE LEE (/tasks/{id}), no a la URL de creación. @Valid dispara Bean
     * Validation (400 si falla). Cableo: si el proyecto no existe -> 404 (ProjectNotFoundException).
     */
    @Operation(summary = "Crea una tarea dentro de un proyecto",
            description = "El proyecto debe existir (404 si no). La tarea nace en estado TODO.")
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(@PathVariable("projectId") Long projectId,
                                                   @Valid @RequestBody TaskRequest request)
            throws TaskValidationException {
        projectService.buscarPorId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        Task creada = taskService.crear(request, projectId);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/tasks/{id}")
                .buildAndExpand(creada.getId())
                .toUri();
        return ResponseEntity.created(location).body(TaskMapper.aResponse(creada));
    }

    /** PUT /tasks/{id} — reemplazo COMPLETO. 200 con TaskResponse; 404 si no existe. */
    @Operation(summary = "Reemplaza una tarea por completo",
            description = "Semántica PUT: conserva id, status y projectId; el resto viene del cuerpo.")
    @PutMapping("/tasks/{id}")
    public TaskResponse updateTask(@PathVariable("id") Long id,
                                   @Valid @RequestBody TaskRequest request)
            throws TaskValidationException {
        Task actualizada = taskService.reemplazar(id, request);
        return TaskMapper.aResponse(actualizada);
    }

    /** DELETE /tasks/{id} — 204 No Content; 404 uniforme si no existe (advice). */
    @Operation(summary = "Borra una tarea",
            description = "204 No Content; 404 uniforme si la tarea no existe.")
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /tasks/{id}/status — cambia SOLO el estado. 200 con TaskResponse; 404 si no existe; y
     * 422 si la regla de estado lo prohíbe (DONE sin assignee), vía TaskStateException.
     */
    @Operation(summary = "Cambia el estado de una tarea",
            description = "TODO/IN_PROGRESS/DONE. Pasar a DONE sin responsable responde 422.")
    @PatchMapping("/tasks/{id}/status")
    public TaskResponse patchStatus(@PathVariable("id") Long id,
                                    @Valid @RequestBody TaskStatusUpdateRequest request) {
        Task actualizada = taskService.cambiarStatus(id, request.status());
        return TaskMapper.aResponse(actualizada);
    }
}
```

</details>


---

<sub>21s</sub>

### `view`

**view the file at C:\Users\marti\taskflow-copilot-MartinGR3\src\main\java\com\taskflow\security\ProjectSecurity.java.**

C:\Users\marti\taskflow-copilot-MartinGR3\src\main\java\com\taskflow\security\ProjectSecurity.java

<details>
<summary>52 lines</summary>

```
package com.taskflow.security;

import com.taskflow.model.User;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * ProjectSecurity — el bean de autorización DATA-DRIVEN. Un matcher de URL no puede saber quién
 * es el owner del proyecto 7 (eso es un DATO en la BD, no una ruta), así que la regla de negocio
 * "solo ADMIN u owner borra el proyecto" se expresa con @PreAuthorize invocando este bean:
 *
 *   @PreAuthorize("hasRole('ADMIN') or @projectSecurity.esOwner(#id, authentication.name)")
 *
 * El nombre del bean ('projectSecurity') es el de la clase con inicial minúscula: por eso el SpEL lo
 * referencia como @projectSecurity.
 */
@Component
public class ProjectSecurity {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectSecurity(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    /**
     * ¿El usuario 'username' es el owner del proyecto 'projectId'? Compara el ownerId del proyecto
     * contra el id del usuario.
     *
     * Proyecto INEXISTENTE -> devuelve true A PROPÓSITO: así el @PreAuthorize deja pasar y es el 404
     * del servicio (ProjectService.eliminar) el que habla — evitamos un 403 mentiroso ("no puedes
     * borrar algo que no existe"). El "no encontrado" lo decide la capa de negocio, no la de seguridad.
     */
    public boolean esOwner(Long projectId, String username) {
        if (projectId == null) {
            // Si el id es null, no llamamos al repositorio para evitar excepciones en la evaluación de SpEL.
            // Devolver true deja hablar al servicio (404) como en el caso de proyecto inexistente.
            System.out.println("ProjectSecurity.esOwner -> projectId is null, returning true");
            return true;
        }
        boolean result = projectRepository.findById(projectId)
                .map(proyecto -> {
                    Long userId = userRepository.findByUsername(username)
                            .map(User::getId)
                            .orElse(null);
                    boolean r = proyecto.getOwnerId().equals(userId);
                    System.out.println("ProjectSecurity.esOwner -> projectId=" + projectId + ", username=" + username + ", ownerId=" + proyecto.getOwnerId() + ", userId=" + userId + ", result=" + r);
                    return r;
                })
                .orElse(true);   // proyecto inexistente -> deja hablar al 404 del servicio
        System.out.println("ProjectSecurity.esOwner -> final result for projectId=" + projectId + ": " + result);
        return result;
    }
}
```

</details>


---

<sub>22s</sub>

### `view`

**view the file at C:\Users\marti\taskflow-copilot-MartinGR3\src\main\java\com\taskflow\service\TaskService.java.**

C:\Users\marti\taskflow-copilot-MartinGR3\src\main\java\com\taskflow\service\TaskService.java

<details>
<summary>145 lines</summary>

```
package com.taskflow.service;

import com.taskflow.dto.TaskRequest;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.exception.TaskStateException;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.mapper.TaskMapper;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * TaskService — la capa de negocio de las tareas. Orquesta; las reglas viven en Task (título 3-120,
 * projectId obligatorio, dueDate no en el pasado al crear, no DONE sin assignee).
 *
 * Piezas clave:
 *   - crear(...) recibe el projectId del PATH (no un id fijo) y arma la entidad con el TaskMapper
 *     (que pasa por la factory Task.crear).
 *   - reemplazar (PUT), eliminar (DELETE) y cambiarStatus (PATCH); completar(...) DELEGA en
 *     cambiarStatus.
 *   - cambiarStatus TRADUCE la checked TaskValidationException que lanza setStatus a la unchecked
 *     TaskStateException -> el advice la mapea a 422. Sin tocar el dominio.
 *
 * La existencia del proyecto al crear (404) se valida en el controller (contra ProjectService): este
 * service no depende del repositorio de proyectos, y así el filtrado por proyecto vive solo en
 * ProjectService.tareasDe.
 */
@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Crea una tarea a partir del DTO de entrada y el projectId del PATH. El TaskMapper pasa por la
     * factory Task.crear (status=TODO, id=null, regla "dueDate no en el pasado"); el repositorio le
     * asigna el id en save. La checked sube al advice si el título o la fecha no cumplen (-> 400).
     */
    public Task crear(TaskRequest request, Long projectId) throws TaskValidationException {
        Task nueva = TaskMapper.aEntidadNueva(request, projectId);
        return repository.save(nueva);
    }

    /**
     * Reemplazo COMPLETO (PUT): busca la tarea (404 si no existe), y arma una tarea rehidratada con el
     * MISMO id, conservando status y projectId actuales; el resto viene del request. save hace upsert.
     */
    public Task reemplazar(Long id, TaskRequest request) throws TaskValidationException {
        Task actual = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        Task reemplazo = TaskMapper.aReemplazo(id, actual.getStatus(), actual.getProjectId(), request);
        return repository.save(reemplazo);
    }

    /**
     * Elimina una tarea (DELETE): existe -> borra (204 en el controller); no existe -> TaskNotFound
     * -> el advice responde 404. El findById previo hace explícito el "no existe" (vía Optional).
     */
    public void eliminar(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        repository.deleteById(id);
    }

    /**
     * Cambia el estado (PATCH): busca la tarea (404 si no existe) y aplica setStatus, que puede lanzar
     * la CHECKED TaskValidationException por la regla "no DONE sin assignee". La CAPTURA y la TRADUCE
     * a la unchecked TaskStateException -> el advice la mapea a 422. El dominio (setStatus) no se toca.
     */
    public Task cambiarStatus(Long id, TaskStatus nuevoStatus) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        try {
            task.setStatus(nuevoStatus);
        } catch (TaskValidationException e) {
            throw new TaskStateException(e.getMessage());
        }
        return repository.save(task);
    }

    /**
     * Completa una tarea (la pasa a DONE). DELEGA en cambiarStatus(id, DONE): un
     * solo camino para "cambiar de estado", que además hereda la traducción a 422.
     */
    public Task completar(Long id) {
        return cambiarStatus(id, TaskStatus.DONE);
    }

    /**
     * Lista todas las tareas ordenadas con la estrategia POR_URGENCIA (reuso LITERAL de la Strategy de
     * TaskOrders): vencidas primero, luego prioridad HIGH->LOW, luego fecha ascendente (nulls al final).
     */
    public List<Task> listar() {
        return repository.findAll().stream()
                .sorted(TaskOrders.POR_URGENCIA)
                .toList();
    }

    /** Filtra las tareas por estado (?status=). '==' entre enums es seguro. */
    public List<Task> porEstado(TaskStatus status) {
        return repository.findAll().stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    /** Busca una tarea por id: delega en el repositorio y deja subir el Optional TAL CUAL. */
    public Optional<Task> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /** Filtro por prioridad (?priority=): MISMO patrón que porEstado. */
    public List<Task> porPrioridad(Priority priority) {
        return repository.findAll().stream()
                .filter(t -> t.getPriority() == priority)
                .toList();
    }

    /**
     * Lista las tareas vencidas de todos los proyectos: usa la regla del dominio Task.estaVencida()
     * y las ordena por fecha ascendente con TaskOrders.POR_FECHA.
     */
    public List<Task> vencidas() {
        return repository.findAll().stream()
                .filter(Task::estaVencida)
                .sorted(TaskOrders.POR_FECHA)
                .toList();
    }

    /**
     * Lista las tareas sin responsable de todos los proyectos: reutiliza el predicado
     * ReportService.SIN_ASIGNAR y ordena por TaskOrders.POR_FECHA.
     */
    public List<Task> sinResponsable() {
        return repository.findAll().stream()
                .filter(ReportService.SIN_ASIGNAR)
                .sorted(TaskOrders.POR_FECHA)
                .toList();
    }

    /**
     * Busca tareas cuyo título contiene el fragmento q (case-insensitive). Antes de buscar
     * se recorta q con strip(); si q es null o queda vacío, lanza TaskValidationException con el
     * mensaje esperado por la especificación.
     */
    public List<Task> buscarPorTitulo(String q) throws TaskValidationException {
        if (q == null || q.strip().isEmpty()) {
            throw new TaskValidationException("El parámetro 'q' es obligatorio.");
        }
        String trimmed = q.strip();
        return repository.findByTitleContainingIgnoreCase(trimmed).stream()
                .sorted(TaskOrders.POR_TITULO)
                .toList();
    }
}
```

</details>


---

<sub>46s</sub>

### Copilot

Plan: comparar la diff con la spec y listar hallazgos por la checklist del revisor. Abajo la tabla de hallazgos, casos sin test y veredicto.

| # | Gravedad | Archivo:línea | Punto de la checklist | Qué pasa | Qué harías |
|---|---|---|---|---|---|
| 1 | Bloquea | src/main/java/com/taskflow/controller/ProjectController.java: ~136 | 1 (Alcance) | La diff modifica ProjectController (cambio en @PreAuthorize: #a0). La especificación restringe cambios a TaskService, TaskController y las dos clases de test nuevas. | Revertir el cambio en ProjectController; dejar sólo las modificaciones permitidas. |
| 2 | Bloquea | src/main/java/com/taskflow/security/ProjectSecurity.java: ~35 | 1,6 (Alcance, Comentarios) | Se añadieron System.out.println debug y cambios no solicitados (null-handling). Modificar beans de seguridad está fuera de alcance y los println son ruido en producción. | Revertir las impresiones de depuración y cualquier cambio no justificado; si se necesita null-handling, mover a commit separado con justificación y tests. |
| 3 | Corregir | src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java: new | 7 (Tests que prueban algo) | El test verifica que service ordena y que repo fue llamado con "api", y que null/blank lanzan excepción, pero NO verifica que el repositorio nunca se llame en los casos null/blank (la spec lo exige). | Añadir verify(repo, never()) para los casos null y "   " para cumplir la especificación. |
| 4 | Sugerencia | src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java: new | 7 (Tests) | El slice test cubre GET /tasks/search?q=api -> 200 y GET /tasks/search sin q -> 400 con mensaje; usa @WebMvcTest y mock de TaskService como pide la spec. | OK. Pequeña limpieza: el try/catch alrededor de creación de Task no es necesario. |
| 5 | Sugerencia | src/main/java/com/taskflow/service/TaskService.java: ~144 | 3,4,5 (Reglas, Orden, Respuesta) | buscarPorTitulo implementa strip(), lanza TaskValidationException con el mensaje exacto, llama repository.findByTitleContainingIgnoreCase(trimmed) y ordena con TaskOrders.POR_TITULO; firma y throws coinciden con la spec. | OK. No cambios. |
| 6 | Sugerencia | src/main/java/com/taskflow/controller/TaskController.java: searchTasks method | 4,5 (Contrato / Convenciones) | El endpoint @GetMapping("/tasks/search") usa @RequestParam(name="q", required=false), delega al service y mapea con TaskMapper.aResponse — conforme a spec. | OK. No cambios. |

Casos sin test
- Falta en BusquedaTareasServiceTest la verificación de que el repo NUNCA es llamado cuando q es null o sólo espacios. (Es obligatorio según spec.)
- Ningún otro caso exigido por la spec está sin test: ordenamiento, trim y slice controller se cubren.

Veredicto: CAMBIOS PEDIDOS

Resumen breve de acciones recomendadas
1. Revertir modificaciones en ProjectController y ProjectSecurity (salvo que haya justificación aceptada).  
2. En el unit test, añadir verify(repo, never()) para los casos null/blank.  
3. Mantener TaskService.buscarPorTitulo y TaskController.searchTasks tal como están (son conformes).

---

<sub>Generated by [GitHub Copilot CLI](https://github.com/features/copilot/cli)</sub>
