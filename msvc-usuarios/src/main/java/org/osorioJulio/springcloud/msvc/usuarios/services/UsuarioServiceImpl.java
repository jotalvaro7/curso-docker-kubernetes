package org.osorioJulio.springcloud.msvc.usuarios.services;

import org.osorioJulio.springcloud.msvc.usuarios.clients.CursoClienteRest;
import org.osorioJulio.springcloud.msvc.usuarios.models.entity.Usuario;
import org.osorioJulio.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoClienteRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
        client.eliminarCursoUsuario(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorIds(Iterable<Long> ids) {
        return (List<Usuario>) usuarioRepository.findAllById(ids);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
