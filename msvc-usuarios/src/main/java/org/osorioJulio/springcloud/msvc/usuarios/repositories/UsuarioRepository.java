package org.osorioJulio.springcloud.msvc.usuarios.repositories;

import org.osorioJulio.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;


public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    //TODO 3 alternativas para realizar una busqueda
    Optional<Usuario> findByEmail(String email);

    @Query("select u from Usuario u where u.email=?1")
    Optional<Usuario> buscarPorEmail(String email);

    boolean existsByEmail(String email);
}
