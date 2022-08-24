package org.osoriojulio.springcloud.msvc.cursos.clients;

import org.osoriojulio.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id, @RequestHeader(value="Authorization", required = true) String token);

    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario, @RequestHeader(value="Authorization", required = true) String token);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids, @RequestHeader(value = "Authorization", required = true) String token);


}
