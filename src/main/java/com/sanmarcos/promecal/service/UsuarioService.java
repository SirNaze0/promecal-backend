package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.exception.*;
import com.sanmarcos.promecal.model.dto.UsuarioDTO;
import com.sanmarcos.promecal.model.dto.UsuarioListaDTO;
import com.sanmarcos.promecal.model.dto.UsuarioVistaDTO;
import com.sanmarcos.promecal.model.entity.Usuario;
import com.sanmarcos.promecal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear un nuevo usuario
    public void insertarUsuario(UsuarioDTO usuariodto) {
        // Validaciones de campos sin lanzar excepciones, solo en caso de que falte algún campo
        if (usuariodto.getNombreusuario() == null || usuariodto.getNombreusuario().isEmpty()) {
            throw new UsuarioInvalidException("El nombre de usuario no puede estar vacío");
        }

        if (usuariodto.getContrasena() == null || usuariodto.getContrasena().isEmpty()) {
            throw new UsuarioInvalidException("La contraseña no puede estar vacía");
        }

        if (usuariodto.getCorreoelectronico() == null || usuariodto.getCorreoelectronico().isEmpty()) {
            throw new UsuarioInvalidException("El correo electrónico no puede estar vacío");
        }

        // Validación de formato del correo electrónico
        if (!isEmailValid(usuariodto.getCorreoelectronico())) {
            throw new UsuarioInvalidException("El correo electrónico no tiene un formato válido");
        }

        // Crear y guardar el nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombreusuario(usuariodto.getNombreusuario());
        usuario.setContrasena(passwordEncoder.encode(usuariodto.getContrasena()));
        usuario.setCorreoelectronico(usuariodto.getCorreoelectronico());
        usuario.setNombrecompleto(usuariodto.getNombrecompleto());
        usuario.setRol(usuariodto.getRol());

        usuarioRepository.save(usuario);
    }
    // Metodo para validar el formato del correo electrónico
    private boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"; // Expresión regular básica para correo electrónico
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    // Obtener todos los usuarios
    public List<UsuarioListaDTO> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Validamos si la lista de usuarios está vacía
        if (usuarios.isEmpty()) {
            throw new UsuariosNoEncontradosException("No se encontraron usuarios en el sistema.");
        }

        // Mapeamos los usuarios a DTOs
        return usuarios.stream().map(usuario -> {
            UsuarioListaDTO usuarioListaDTO = new UsuarioListaDTO();
            usuarioListaDTO.setNombreusuario(usuario.getNombreusuario());
            usuarioListaDTO.setNombrecompleto(usuario.getNombrecompleto());
            usuarioListaDTO.setCorreoelectronico(usuario.getCorreoelectronico());
            usuarioListaDTO.setRol(usuario.getRol());
            usuarioListaDTO.setId(usuario.getId());
            return usuarioListaDTO;
        }).collect(Collectors.toList());
    }
    // Obtener un usuario por ID
    public UsuarioVistaDTO obtenerUsuarioPorId(Long id) {
        // Buscar el usuario por ID, lanzar excepción personalizada si no se encuentra
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + id + " no encontrado"));

        // Convertir el usuario a un DTO
        UsuarioVistaDTO usuarioVistaDTO = new UsuarioVistaDTO();
        usuarioVistaDTO.setNombrecompleto(usuario.getNombrecompleto());
        usuarioVistaDTO.setCorreoelectronico(usuario.getCorreoelectronico());
        usuarioVistaDTO.setRol(usuario.getRol());
        usuarioVistaDTO.setNombreusuario(usuario.getNombreusuario());
        return usuarioVistaDTO;
    }
    public void actualizarUsuario(Long id, UsuarioVistaDTO usuarioVistaDTO) {
        // Buscar el usuario por ID, lanzando excepción si no se encuentra
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + id + " no encontrado"));

        // Validar que el correo electrónico no esté vacío y tenga un formato correcto
        if (usuarioVistaDTO.getCorreoelectronico() == null || usuarioVistaDTO.getCorreoelectronico().isEmpty()) {
            throw new DatosInvalidosException("El correo electrónico no puede estar vacío.");
        }
        if (!usuarioVistaDTO.getCorreoelectronico().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new DatosInvalidosException("El formato del correo electrónico no es válido.");
        }

        // Actualizar los campos del usuario
        usuario.setNombreusuario(usuarioVistaDTO.getNombreusuario());
        usuario.setCorreoelectronico(usuarioVistaDTO.getCorreoelectronico());
        usuario.setNombrecompleto(usuarioVistaDTO.getNombrecompleto());
        usuario.setRol(usuarioVistaDTO.getRol());

        // Guardar el usuario actualizado
        usuarioRepository.save(usuario);
    }

    // Eliminar Usuario
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + id + " no encontrado"));
        usuarioRepository.delete(usuario);
    }


}
