# Proyecto final · Semana 6 · GitHub Copilot

**Alumno:** `Martin Gonzalez Rico` · **Usuario de GitHub:** `MartinGR3`

## 1. Qué construí

| | Feature | Especificación |
|---|---|---|
| [`x`] | `GET /tasks/search?q=` — buscar tareas por título | [`specs/search.md`](../specs/search.md) |

## 2. El pull request

- **URL del PR (mergeado):** `https://github.com/MartinGR3/taskflow-copilot-MartinGR3/pull/5`
- **Commit del merge en `main`:** `a1efa34 (HEAD -> main, origin/main, origin/HEAD) Merge pull request #5 from MartinGR3/feature/search`
- **Comentarios de Copilot code review:** `3`

## 3. Cómo lo hice

| Paso | Qué hice | Evidencia |
|---|---|---|
| Rama y spec | `git switch -c feature/search` y copié la spec a `specs/` | `git log --oneline main..feature/search` (antes del merge) |
| Implementación | `copilot -p "/crear-endpoint-taskflow …"` con `gpt-5-mini` | `semana6/sesion-implementacion.md` (tiene la línea `Skill "crear-endpoint-taskflow" loaded successfully`) |
| Revisión | agente `revisor` sobre `semana6/proyecto-final.diff` | `semana6/revision.md` (termina con `Veredicto:`) |
| Tests | `mvn test` en verde | `Tests run: 80, Failures: 0, Errors: 0, Skipped: 0` |
| Comprobación REST | `verificar.ps1` con `casos-search.ps1` | sección 5 de este documento |
| Code review | Revisé y pedí al agente que corrigiera los comentarios de Copilot | la pestaña *Files changed* del PR |

## 4. Qué hizo el agente y qué corregí yo

| # | Qué hizo mal el agente (archivo) | Quién lo detectó | Cómo quedó corregido |
|---|---|---|---|
| 1 | `Se añadió un caso especial para projectId == null en ProjectSecurity.java, fuera del alcance de search.` | `Copilot code review` | `Regresé ProjectController.java y ProjectSecurity.java a su versión de 50e3ce9 (git checkout 50e3ce9 -- …) en el commit cb370a5. mvn clean test pasa los 80 tests.`
| 2 | `El test no comprobaba que el repositorio no recibiera interacciones ante entradas inválidas (BusquedaTareasServiceTest.java).` | `Copilot code review` | `Se sustituyó la verificación por verifyNoInteractions(repo).`
| 3 | `El test del controlador solo validaba los datos del primer elemento (BusquedaTareasControllerTest.java).` | `Copilot code review` |`Se añadieron aserciones para el ID y título del segundo elemento.`
| 4	|`Cambió #id por #a0 en el @PreAuthorize de ProjectController.java, fuera del alcance de la spec.`	| `revisor`	| `Revertido en el commit cb370a5 (versión de 50e3ce9).`
| 5 |	`Añadió System.out.println de depuración y el if de projectId == null en ProjectSecurity.java, fuera del alcance.` |	`revisor` |	`Revertido en el commit cb370a5 (versión de 50e3ce9).`
| 6 |	`Faltaba verify(repo, never()) para q null y "   " en BusquedaTareasServiceTest.java (lo exige la spec).`	| `revisor` |	`Agregado en el commit 99c3112.`

**Lo que el agente hizo bien a la primera** (una o dos líneas): `El agente reutilizó el repositorio y el mapper existentes para implementar la búsqueda por título, incluyendo el recorte de espacios y el orden alfabético. La funcionalidad quedó cubierta con tests unitarios y de controlador.`

## 5. Comprobaciones REST

```text
Repositorio: C:\Users\marti\taskflow-copilot-MartinGR3
URL de la app: http://127.0.0.1:8080
Empaquetando con Maven (mvn -q package -DskipTests), tarda unos segundos...
App arrancando (PID 18356). Esperando a que /info responda...
App lista en 9 s.
[OK]    GET /tasks/overdue devuelve solo la tarea 7
[OK]    GET /tasks/unassigned devuelve las tareas 4 y 6
[OK]    GET /projects/1/summary
[OK]    GET /projects/2/summary
[OK]    GET /projects/3/summary
[OK]    GET /projects/99/summary responde 404
[OK]    GET /projects/1/summary sin token responde 401
[OK]    GET /tasks/search?q=api devuelve 9 y 5, en ese orden
[OK]    GET /tasks/search?q=API devuelve lo mismo (no distingue mayúsculas)
[OK]    GET /tasks/search?q=zzz devuelve 200 con []
[OK]    GET /tasks/search?q=(espacios) responde 400 con el mensaje de la spec
[OK]    GET /tasks/search sin q responde 400 con el mensaje de la spec
[OK]    GET /tasks/search sin token responde 401
App detenida (PID 18356).
[OK]    App apagada: el puerto 8080 ya no responde
RESULTADO: 14/14 OK
```

## 6. Créditos de la semana

| Qué | AI credits |
|---|---|
| Usados en octubre según github.com (incluye semanas anteriores si usaste Copilot antes) | `404.37` |
| Implementación con la skill (`AI Credits` del PF-2) | `No logre encontrar este valor.` |
| Revisión del `revisor` (`AI Credits` del PF-3) | `1.59 + 2.62 =4.21` |
| Correcciones del PF-4 y del PF-6, si hubo (`AI Credits`) | `4.21 y 1.48` |
