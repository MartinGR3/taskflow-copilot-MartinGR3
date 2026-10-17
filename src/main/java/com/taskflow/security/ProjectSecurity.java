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
