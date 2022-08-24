package org.osoriojulio.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import org.osoriojulio.springcloud.msvc.cursos.models.Usuario;
import org.osoriojulio.springcloud.msvc.cursos.models.entity.Curso;
import org.osoriojulio.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id,  @RequestHeader(value = "Authorization", required = true) String token){
        Optional<Curso> curso = cursoService.porIdConUsuarios(id, token);
        if(curso.isPresent()){
            return ResponseEntity.ok(curso.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> guardar(@Valid @RequestBody Curso curso, BindingResult result){
        if(result.hasErrors()){
            return validar(result);
        }

        Curso cursoDb = cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return validar(result);
        }

        Optional<Curso> o = cursoService.porId(id);
        if (o.isPresent()){
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> o = cursoService.porId(id);
        if(o.isPresent()){
            cursoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //TODO METODOS PARA REALIZAR LA COMUNICACION CON EL OTRO MICROSERVICIO
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId, @RequestHeader(value="Authorization", required = true) String token){
        Optional<Usuario> optionalUsuario;
        try{
            optionalUsuario = cursoService.asignarUsuario(usuario, cursoId, token);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por" +
                            " el id o error en la comunicacion: " + e.getMessage()));
        }
        if(optionalUsuario.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUsuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId, @RequestHeader(value="Authorization", required = true) String token){
        Optional<Usuario> optionalUsuario;
        try{
            optionalUsuario = cursoService.crearUsuario(usuario, cursoId, token);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario o" +
                            " error en la comunicacion: " + e.getMessage()));
        }
        if(optionalUsuario.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUsuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId, @RequestHeader(value="Authorization", required = true) String token){
        Optional<Usuario> optionalUsuario;
        try{
            optionalUsuario = cursoService.eliminarUsuarioDelCurso(usuario, cursoId, token);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por" +
                            " el id o error en la comunicacion: " + e.getMessage()));
        }
        if(optionalUsuario.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(optionalUsuario.get());
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuario(@PathVariable Long id){
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
