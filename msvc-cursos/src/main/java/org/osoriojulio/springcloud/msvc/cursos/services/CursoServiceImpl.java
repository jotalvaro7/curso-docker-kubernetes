package org.osoriojulio.springcloud.msvc.cursos.services;

import org.osoriojulio.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.osoriojulio.springcloud.msvc.cursos.models.Usuario;
import org.osoriojulio.springcloud.msvc.cursos.models.entity.Curso;
import org.osoriojulio.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.osoriojulio.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }


    //TODO LOGICA DE NEGOCIO PARA REALIZAR LA COMUNICACION CON EL MSVC USUARIOS
    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> optional = cursoRepository.findById(cursoId);
        if(optional.isPresent()){
            Usuario usuarioMsvc = client.detalle(usuario.getId());

            Curso curso = optional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> optional = cursoRepository.findById(cursoId);
        if(optional.isPresent()){
            Usuario usuarioNuevoMsvc = client.crear(usuario);

            Curso curso = optional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuarioDelCurso(Usuario usuario, Long cursoId) {
        Optional<Curso> optional = cursoRepository.findById(cursoId);
        if(optional.isPresent()){
            Usuario usuarioMsvc = client.detalle(usuario.getId());

            Curso curso = optional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> optionalCurso = cursoRepository.findById(id);
        if(optionalCurso.isPresent()){
            Curso curso = optionalCurso.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios().stream()
                        .map(cursoUsuario -> cursoUsuario.getUsuarioId())
                        .collect(Collectors.toList());

                List<Usuario> usuarios = client.obtenerAlumnosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }
}
