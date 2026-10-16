package com.taskflow.slice;

import com.taskflow.controller.ProjectController;
import com.taskflow.dto.ProjectSummaryResponse;
import com.taskflow.model.Project;
import com.taskflow.security.JwtAuthenticationFilter;
import com.taskflow.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectSummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getResumen_proyectoExistente_devuelve200YJson() throws Exception {
        Project p = new Project(2L, "App Móvil", "d", 2L, null);
        ProjectSummaryResponse dto = new ProjectSummaryResponse(2L, "App Móvil", 4,
                Map.of("TODO", 1, "IN_PROGRESS", 2, "DONE", 1), 1);

        when(projectService.buscarPorId(2L)).thenReturn(Optional.of(p));
        when(projectService.resumenDe(p)).thenReturn(dto);

        mockMvc.perform(get("/projects/2/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(2))
                .andExpect(jsonPath("$.projectName").value("App Móvil"))
                .andExpect(jsonPath("$.totalTasks").value(4))
                .andExpect(jsonPath("$.byStatus.TODO").value(1))
                .andExpect(jsonPath("$.byStatus.IN_PROGRESS").value(2))
                .andExpect(jsonPath("$.byStatus.DONE").value(1))
                .andExpect(jsonPath("$.overdue").value(1));
    }

    @Test
    void getResumen_proyectoInexistente_devuelve404() throws Exception {
        when(projectService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/projects/99/summary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
