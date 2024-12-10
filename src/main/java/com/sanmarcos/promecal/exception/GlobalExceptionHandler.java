package com.sanmarcos.promecal.exception;

import com.sanmarcos.promecal.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de excepciones para recursos no encontrados
    @ExceptionHandler({
            ProformaServicioNotFoundException.class,
            OrdenTrabajoNoEncontradaException.class,
            ClienteNoEncontradoException.class,
            HistorialNoEncontradoException.class,
            DocumentoNoEncontradoException.class,
            NoDataFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    // Manejo de excepciones relacionadas con el usuario
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNotFoundException(UsuarioNoEncontradoException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            UsuariosNoEncontradosException.class,
            UsuarioInvalidException.class,
            DatosInvalidosException.class
    })
    public ResponseEntity<ErrorResponse> handleUsuarioException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);  // O cualquier código HTTP que consideres adecuado
    }

    // Manejo de excepciones de solicitud incorrecta
    @ExceptionHandler({
            ProformaServicioException.class,
            InvalidPriceException.class,
            ArchivoVacioException.class,
            TipoArchivoInvalidoException.class,
            ContraseniaIncorrectaException.class,
            FechaInvalidaException.class,
            RangoFechaInvalidoException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    // Manejo de excepciones de conflicto
    @ExceptionHandler({
            PagoYaRegistradoException.class,
            ClienteYaExisteException.class,
            DniDuplicadoException.class,
            NumeroSerieDuplicadoException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(GeneracionPDFException.class)
    public ResponseEntity<String> handleGeneracionPDFException(GeneracionPDFException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al generar el PDF: " + ex.getMessage());
    }

    // Manejo de excepciones internas del servidor
    @ExceptionHandler({DocumentoEliminacionException.class, InformeDiagnosticoException.class})
    public ResponseEntity<ErrorResponse> handleDocumentoEliminacionException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Manejo de excepciones generales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Método para construir la respuesta de error
    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }
}

