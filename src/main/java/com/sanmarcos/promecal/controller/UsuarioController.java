package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.exception.*;
import com.sanmarcos.promecal.model.dto.UsuarioDTO;
import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.model.dto.UsuarioVistaDTO;
import com.sanmarcos.promecal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    //Endpoint para obtener la lista de usuario
    @GetMapping
    public ResponseEntity<List<UsuarioListaDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioListaDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();

        // Validamos si no se encontraron usuarios
        if (usuarios.isEmpty()) {
            throw new UsuariosNoEncontradosException("No se encontraron usuarios en el sistema.");
        }

        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para guardar un usuario
    @PostMapping
    public ResponseEntity<String> insertarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        usuarioService.insertarUsuario(usuarioDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Endpoint para obtener los detalles de un usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioVistaDTO> obtenerDetallesUsuario(@PathVariable Long id) {
        UsuarioVistaDTO usuarioVistaDTO = usuarioService.obtenerUsuarioPorId(id);
        return new ResponseEntity<>(usuarioVistaDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioVistaDTO usuarioVistaDTO) {
        // Llamada al servicio para actualizar el usuario
        usuarioService.actualizarUsuario(id, usuarioVistaDTO);
        return new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        // Llamada al servicio para eliminar el usuario
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();  // Respuesta 204 No Content
    }



}
