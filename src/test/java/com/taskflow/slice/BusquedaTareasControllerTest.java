package com.taskflow.slice;

import com.taskflow.controller.TaskController;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.security.JwtAuthenticationFilter;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class BusquedaTareasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void get_search_con_q_devuelve200YLista() throws Exception {
        LocalDate hoy = LocalDate.now();
        try {
            Task a = new Task(9L, "Documentar la API con Swagger", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, hoy);
            Task b = new Task(5L, "Optimizar consultas de la API", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, hoy);
            when(taskService.buscarPorTitulo("api")).thenReturn(List.of(a, b));

            mockMvc.perform(get("/tasks/search?q=api").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(9))
                    .andExpect(jsonPath("$[0].title").value("Documentar la API con Swagger"))
                    .andExpect(jsonPath("$[1].id").value(5))
                    .andExpect(jsonPath("$[1].title").value("Optimizar consultas de la API"));
        } catch (TaskValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get_search_sin_q_devuelve400ConMensaje() throws Exception {
        when(taskService.buscarPorTitulo(null)).thenThrow(new TaskValidationException("El parámetro 'q' es obligatorio."));

        mockMvc.perform(get("/tasks/search").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El parámetro 'q' es obligatorio."));
    }
}
