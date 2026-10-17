Comentario 1 — High

## Comentario 1 — High

**Texto:** No autorizar proyectos cuando el identificador es nulo.

Esta rama concede autorización cuando no hay identificador con el que comprobar la propiedad. Un `null` no equivale a un proyecto inexistente: `ProjectService.eliminar(null)` llama a `findById(null)`, no genera el 404 anunciado. El uso actual de `#a0` evita depender del nombre del parámetro, pero no requiere este permiso adicional. Retira esta rama, ajena al alcance de `specs/search.md:79–85`, y conserva el permiso para proyectos inexistentes solo después de consultar un id válido.

**Archivo:** `src/main/java/com/taskflow/security/ProjectSecurity.java`

**Líneas:** 38–42

## Comentario 2 — Medium

**Texto:** Validar los datos de todos los elementos de la respuesta.

Solo se comprueban los datos del primer elemento: una respuesta que duplique esa tarea también pasaría. `specs/search.md:59–60` exige los ids y títulos de ambas tareas en orden. Añade las aserciones del segundo elemento para comprobar la lista completa.

**Archivo:** `src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java`

**Líneas:** 53–56

**Estado:** Las aserciones del segundo elemento ya están añadidas en la rama actual (líneas 57–58).

## Comentario 3 — Medium

**Texto:** Verificar que el repositorio no recibe ninguna interacción.

`anyString()` no coincide con `null`, así que esta verificación no detectaría una llamada a `findByTitleContainingIgnoreCase(null)` antes de lanzar la excepción. Tampoco comprueba otras interacciones con el repositorio. Usa `verifyNoInteractions(repo)` para cubrir la ausencia de llamadas exigida en `specs/search.md:54`.

**Archivo:** `src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java`

**Líneas:** 49–52

