package com.sanmarcos.promecal.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenTrabajoDTO {
    @NotBlank(message = "El DNI del cliente es obligatorio.")
    private String dni;
    private String nombrecompleto;
    @NotNull(message = "La fecha es obligatoria.")
    private LocalDateTime fecha;
    @NotBlank(message = "La descripción no puede estar vacía.")
    private String descripcion;

    private String modelo;

    private String marca;

    private Boolean rajaduras;

    private Boolean manchas;

    private Boolean golpes;

    private String documentourl;

}
