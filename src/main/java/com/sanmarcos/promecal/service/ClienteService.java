package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.exception.ClienteNoEncontradoException;
import com.sanmarcos.promecal.exception.ClienteYaExisteException;
import com.sanmarcos.promecal.exception.DniDuplicadoException;
import com.sanmarcos.promecal.model.dto.ClienteDTO;
import com.sanmarcos.promecal.model.dto.ClienteListaDTO;
import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public void insertarCliente(ClienteDTO clienteDTO) {
        // Validar si el DNI ya está registrado
        if (clienteRepository.existsByDni(clienteDTO.getDni())) {
            throw new ClienteYaExisteException("El cliente con DNI " + clienteDTO.getDni() + " ya está registrado.");
        }
        // Mapear DTO a entidad
        Cliente cliente = new Cliente();
        cliente.setDni(clienteDTO.getDni());
        cliente.setCelular(clienteDTO.getCelular());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setNombreCompleto(clienteDTO.getNombrecompleto());
        // Guardar en la base de datos
        clienteRepository.save(cliente);
    }

    // Obtener todos los clientes
    public List<ClienteListaDTO> obtenerTodosLosClientes() {
        return clienteRepository.findAll().stream().map(cliente -> {
            ClienteListaDTO clientelistadto = new ClienteListaDTO();
            clientelistadto.setDni(cliente.getDni());
            clientelistadto.setCelular(cliente.getCelular());
            clientelistadto.setDireccion(cliente.getDireccion());
            clientelistadto.setNombrecompleto(cliente.getNombreCompleto());
            clientelistadto.setId(cliente.getId());
            return clientelistadto;
        }).collect(Collectors.toList());
    }

    // Obtener un cliente por ID
    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() ->
                new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado"));
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setDni(cliente.getDni());
        clienteDTO.setCelular(cliente.getCelular());
        clienteDTO.setDireccion(cliente.getDireccion());
        clienteDTO.setNombrecompleto(cliente.getNombreCompleto());
        return clienteDTO;
    }
    public void actualizarCliente(Long id, ClienteDTO clienteDTO) {
        // Buscar el cliente existente
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado."));
        boolean isModified = false;
        // Validar si el DNI puede cambiar
        if (clienteDTO.getDni() != null && !cliente.getDni().equals(clienteDTO.getDni())) {
            if (clienteRepository.existsByDni(clienteDTO.getDni())) {
                throw new DniDuplicadoException("El DNI " + clienteDTO.getDni() + " ya está registrado por otro cliente.");
            }
            cliente.setDni(clienteDTO.getDni()); // Actualizar solo si es diferente
            isModified = true;
        }
        // Validar y actualizar nombre completo si es diferente
        if (clienteDTO.getNombrecompleto() != null && !clienteDTO.getNombrecompleto().equals(cliente.getNombreCompleto())) {
            cliente.setNombreCompleto(clienteDTO.getNombrecompleto());
            isModified = true;
        }
        // Validar y actualizar dirección si es diferente
        if (clienteDTO.getDireccion() != null && !clienteDTO.getDireccion().equals(cliente.getDireccion())) {
            cliente.setDireccion(clienteDTO.getDireccion());
            isModified = true;
        }
        // Validar y actualizar celular si es diferente
        if (clienteDTO.getCelular() != null && !clienteDTO.getCelular().equals(cliente.getCelular())) {
            cliente.setCelular(clienteDTO.getCelular());
            isModified = true;
        }
        // Guardar los cambios solo si hubo alguna modificación
        if (isModified) {
            clienteRepository.save(cliente);
        }
    }

    public void eliminarCliente(Long id) {
        // Verificar si el cliente existe
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado."));
        // Eliminar el cliente
        clienteRepository.delete(cliente);
    }

}
