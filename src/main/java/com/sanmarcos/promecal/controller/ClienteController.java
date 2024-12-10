package com.sanmarcos.promecal.controller;
import com.sanmarcos.promecal.model.dto.ClienteDTO;
import com.sanmarcos.promecal.model.dto.ClienteListaDTO;
import com.sanmarcos.promecal.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    //Endpoint para obtener la lista de clientes
    @GetMapping
    public List<ClienteListaDTO> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    //Endpoint para guardar un cliente
    @PostMapping
    public ResponseEntity<String> insertarCliente(@RequestBody ClienteDTO clienteDTO) {
        clienteService.insertarCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente registrado con éxito.");
    }
    //Endpoint para obtener los detalles de un cliente por id
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerDetallesCliente(@PathVariable Long id) {
        ClienteDTO clienteDTO = clienteService.obtenerClientePorId(id);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        clienteService.actualizarCliente(id, clienteDTO);
        return ResponseEntity.ok("Cliente actualizado con éxito.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();  // Respuesta 204 No Content
    }


}
