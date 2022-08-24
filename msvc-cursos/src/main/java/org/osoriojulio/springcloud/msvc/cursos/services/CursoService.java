package org.osoriojulio.springcloud.msvc.cursos.services;

import org.osoriojulio.springcloud.msvc.cursos.models.Usuario;
import org.osoriojulio.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);

    void eliminarCursoUsuarioPorId(Long id);


    //TODO LOGICA DE NEGOCIO DESDE OTRO MICROSERVICIO
    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId, String token);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId, String token);
    Optional<Usuario> eliminarUsuarioDelCurso(Usuario usuario, Long cursoId, String token);

    Optional<Curso> porIdConUsuarios(Long id, String token);
}
