package com.taskflow.unit;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import com.taskflow.service.TaskOrders;
import com.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusquedaTareasServiceTest {

    @Mock
    private TaskRepository repo;

    @InjectMocks
    private TaskService service;

    @Test
    void buscarPorTitulo_convierteYOrdena_porTitulo() throws Exception {
        Task t1 = new Task(5L, "Optimizar consultas de la API", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, LocalDate.now());
        Task t2 = new Task(9L, "Documentar la API con Swagger", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, LocalDate.now());
        // repo devuelve en orden no alfabético
        when(repo.findByTitleContainingIgnoreCase("api")).thenReturn(List.of(t1, t2));

        List<Task> resultado = service.buscarPorTitulo("  api ");

        // deben venir ordenadas por título (Documentar... antes de Optimizar...)
        assertThat(resultado).containsExactly(t2, t1);
        verify(repo).findByTitleContainingIgnoreCase("api");
    }

    @Test
    void buscarPorTitulo_null_o_vacio_lanzaValidation() {
        assertThrows(TaskValidationException.class, () -> service.buscarPorTitulo(null));
        assertThrows(TaskValidationException.class, () -> service.buscarPorTitulo("   "));
        // repository nunca se llama en estos casos
        org.mockito.Mockito.verify(repo, org.mockito.Mockito.never()).findByTitleContainingIgnoreCase(org.mockito.Mockito.anyString());
    }
}
