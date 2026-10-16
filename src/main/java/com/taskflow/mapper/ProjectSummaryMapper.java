package com.taskflow.mapper;

import com.taskflow.dto.ProjectSummaryResponse;
import com.taskflow.model.Project;

import java.util.Map;

/**
 * Mapeo manual para ProjectSummaryResponse.
 */
public final class ProjectSummaryMapper {

    private ProjectSummaryMapper() { }

    public static ProjectSummaryResponse aResponse(Project p, int totalTasks, Map<String, Integer> byStatus, int overdue) {
        return new ProjectSummaryResponse(p.getId(), p.getName(), totalTasks, byStatus, overdue);
    }
}
