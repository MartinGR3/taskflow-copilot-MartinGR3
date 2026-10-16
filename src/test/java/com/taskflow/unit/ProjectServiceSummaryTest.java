package com.taskflow.unit;

import com.taskflow.dto.ProjectSummaryResponse;
import com.taskflow.model.Priority;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import com.taskflow.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceSummaryTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void resumenProyecto_conTareas_yVencida() throws Exception {
        Project p = new Project(2L, "App Móvil", "d", 2L, null);
        Task t1 = new Task(5L, "T-1", "d", TaskStatus.TODO, Priority.MED, 2L, null, null);
        Task t2 = new Task(6L, "T-2", "d", TaskStatus.IN_PROGRESS, Priority.MED, 2L, 1L, LocalDate.now().minusDays(1));
        Task t3 = new Task(7L, "T-3", "d", TaskStatus.DONE, Priority.MED, 2L, 1L, LocalDate.now().minusDays(2));

        when(taskRepository.findByProjectId(2L)).thenReturn(List.of(t1, t2, t3));

        ProjectSummaryResponse res = projectService.resumenDe(p);

        assertEquals(3, res.totalTasks());
        assertEquals(1, res.byStatus().get("TODO"));
        assertEquals(1, res.byStatus().get("IN_PROGRESS"));
        assertEquals(1, res.byStatus().get("DONE"));
        assertEquals(1, res.overdue());
    }

    @Test
    void resumenProyecto_sinTareas_devuelveCeros() throws Exception {
        Project p = new Project(3L, "Empty", "d", 2L, null);
        when(taskRepository.findByProjectId(3L)).thenReturn(List.of());

        ProjectSummaryResponse res = projectService.resumenDe(p);

        assertEquals(0, res.totalTasks());
        assertEquals(0, res.byStatus().get("TODO"));
        assertEquals(0, res.byStatus().get("IN_PROGRESS"));
        assertEquals(0, res.byStatus().get("DONE"));
        assertEquals(0, res.overdue());
    }
}
