package com.taskflow.dto;

import java.util.Map;

/**
 * DTO de salida para el resumen de un proyecto.
 */
public record ProjectSummaryResponse(Long projectId,
                                    String projectName,
                                    int totalTasks,
                                    Map<String, Integer> byStatus,
                                    int overdue) {
}
