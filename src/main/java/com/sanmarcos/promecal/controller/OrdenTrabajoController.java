package com.sanmarcos.promecal.controller;

import com.sanmarcos.promecal.exception.*;
import com.sanmarcos.promecal.model.dto.*;
import com.sanmarcos.promecal.service.OrdenTrabajoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/ordentrabajo")
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;
    // Endpoint para obtener la lista de órdenes de trabajo con múltiples filtros opcionales
    @GetMapping
    public ResponseEntity<List<OrdenTrabajoListaDTO>> obtenerOrdenesTrabajo(
            @RequestParam(required = false) LocalDateTime fechaInicio,
            @RequestParam(required = false) LocalDateTime fechaFin,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String codigo) {
        List<OrdenTrabajoListaDTO> ordenes = ordenTrabajoService
                .obtenerOrdenesTrabajoConFiltros(fechaInicio, fechaFin, dni, modelo, codigo);
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/codigos")
    public ResponseEntity<List<String>> obtenerCodigos() {
        List<String> codigos = ordenTrabajoService.obtenerCodigos();
        return ResponseEntity.ok(codigos);
    }
    //Endpoint para guardar una orden de trabajo
    @PostMapping
    public ResponseEntity<String> insertarOrdenTrabajo(
            @RequestPart("orden") @Valid OrdenTrabajoDTO ordenTrabajoDTO,
            @RequestPart("file") MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new ArchivoVacioException("Debe proporcionar un archivo válido.");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new TipoArchivoInvalidoException("El archivo debe ser de tipo PDF.");
        }

        // Crear archivo temporal y delegar la lógica al servicio
        File tempFile = File.createTempFile("documento_remision_", ".pdf");
        try {
            file.transferTo(tempFile);
            ordenTrabajoService.insertarOrdenTrabajo(ordenTrabajoDTO, tempFile);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete(); // Eliminar archivo temporal
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Orden de trabajo creada exitosamente.");
    }

    // Eliminar una orden de trabajo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrdenTrabajo(@PathVariable Long id) {
        ordenTrabajoService.eliminarOrdenTrabajo(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para obtener los detalles de una orden de trabajo por id
    @GetMapping("/{id}")
    public ResponseEntity<OrdenTrabajoVistaDTO> obtenerDetallesOrdenTrabajo(@PathVariable Long id) {
        OrdenTrabajoVistaDTO ordenTrabajoVistaDTO = ordenTrabajoService.obtenerOrdenTrabajoPorId(id);
        return ResponseEntity.ok(ordenTrabajoVistaDTO);
    }
    //Endpoint para obtener el historial de modificaciones de un orden de trabajo
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<OrdenTrabajoHistorialDTO>> obtenerHistorialDeOrden(@PathVariable Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        List<OrdenTrabajoHistorialDTO> historial = ordenTrabajoService.obtenerHistorialDeOrden(id);
        return ResponseEntity.ok(historial);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarOrdenTrabajo(@PathVariable Long id,
                                                         @RequestPart("orden") OrdenTrabajoDTO ordenTrabajoDTO,
                                                         @RequestPart(value = "file", required = false) MultipartFile file) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la orden de trabajo debe ser mayor a cero.");
        }

        // Si no se ha subido un archivo, pasamos un valor null
        File tempFile = null;
        if (file != null && !file.isEmpty()) {
            // Si el archivo no está vacío, validamos que sea un PDF
            if (!Objects.requireNonNull(file.getContentType()).equalsIgnoreCase("application/pdf")) {
                throw new TipoArchivoInvalidoException("El archivo debe ser un PDF");
            }
            try {
                // Crear archivo temporal si se ha subido un archivo
                tempFile = File.createTempFile("documento_remision_", ".pdf");
                file.transferTo(tempFile);
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar el archivo: " + e.getMessage());
            }
        }

        // Llamar al servicio, pasando null si no se ha subido archivo
        ordenTrabajoService.actualizarOrdenTrabajo(id, ordenTrabajoDTO, tempFile);

        return ResponseEntity.ok("Orden de trabajo actualizada correctamente.");
    }

}
