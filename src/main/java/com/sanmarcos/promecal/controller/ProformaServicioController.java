package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.exception.ArchivoVacioException;
import com.sanmarcos.promecal.exception.TipoArchivoInvalidoException;
import com.sanmarcos.promecal.model.dto.*;
import com.sanmarcos.promecal.service.ProformaServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/proformaservicio")
public class ProformaServicioController {
    @Autowired
    ProformaServicioService proformaServicioService;
    //Endpoint para obtener la lista de proforma de servicios
    @GetMapping
    public ResponseEntity<List<ProformaServicioListaDTO>> obtenerTodosLasProformas() {
        // Llamar al servicio para obtener las proformas
        List<ProformaServicioListaDTO> proformas = proformaServicioService.obtenerTodosLasProformas();
        return new ResponseEntity<>(proformas, HttpStatus.OK);
    }

    // Endpoint para guardar un cliente
    @PostMapping
    public ResponseEntity<String> insertarProformaServicio(@RequestBody @Valid ProformaServicioDTO proformaServicioDTO) {
        // Llamar al servicio para insertar la proforma
        proformaServicioService.insertarProformaServicio(proformaServicioDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Endpoint para obtener los detalles de una proforma de servicio por id
    @GetMapping("/{id}")
    public ResponseEntity<ProformaServicioDTO> obtenerDetallesProformaServicio(@PathVariable Long id) {
        // Llamamos al servicio para obtener los detalles de la proforma de servicio por id
        ProformaServicioDTO proformaServicioDTO = proformaServicioService.obtenerProformaServicioPorId(id);
        return new ResponseEntity<>(proformaServicioDTO, HttpStatus.OK);
    }

    // Endpoint para actualizar una proforma de servicio
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarProformaServicio(@PathVariable Long id, @RequestBody @Valid ProformaServicioDTO proformaServicioDTO) {
        // Llamamos al servicio para actualizar la proforma
        proformaServicioService.actualizarProformaServicio(id, proformaServicioDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Eliminar una proforma de servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProformaServicio(@PathVariable Long id) {
        proformaServicioService.eliminarProformaServicio(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content si la eliminación es exitosa
    }

    // Buscar proformas de Servicio por Cliente(dni)
    @GetMapping("/cliente/{dni}")
    public ResponseEntity<List<ProformaServicioListaDTO>> obtenerProformaServicioPorCliente(@PathVariable String dni) {
        List<ProformaServicioListaDTO> proformas = proformaServicioService.obtenerProformaServicioPorCliente(dni);
        return ResponseEntity.ok(proformas);  // Retorna 200 OK con la lista de proformas
    }

    @PostMapping("/{id}/pago")
    public ResponseEntity<String> registrarPago(@PathVariable Long id, @RequestPart(value = "file") MultipartFile file) throws Exception {
        // Verificar que el archivo no esté vacío
        if (file.isEmpty()) {
            throw new ArchivoVacioException("El archivo está vacío");
        }

        // Verificar que el archivo sea un PDF
        if (!Objects.requireNonNull(file.getContentType()).equalsIgnoreCase("application/pdf")) {
            throw new TipoArchivoInvalidoException("El archivo debe ser un PDF");
        }

        // Crear archivo temporal para almacenar el archivo recibido
        File tempFile = File.createTempFile("boleta_", ".pdf");
        file.transferTo(tempFile);

        // Registrar el pago en el servicio
        proformaServicioService.registrarPago(id, tempFile);

        return ResponseEntity.status(HttpStatus.OK).body("Pago registrado con éxito");
    }

}
