package com.sanmarcos.promecal.service;
import com.sanmarcos.promecal.exception.FechaInvalidaException;
import com.sanmarcos.promecal.exception.GeneracionPDFException;
import com.sanmarcos.promecal.exception.NumeroSerieDuplicadoException;
import com.sanmarcos.promecal.exception.OrdenTrabajoNoEncontradaException;
import com.sanmarcos.promecal.model.dto.InformeDiagnosticoDTO;
import java.io.IOException;
import com.sanmarcos.promecal.model.entity.InformeDiagnostico;
import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import com.sanmarcos.promecal.repository.InformeDiagnosticoRepository;
import com.sanmarcos.promecal.repository.OrdenTrabajoRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class InformeDiagnosticoService {
    @Autowired
    InformeDiagnosticoRepository informeDiagnosticoRepository;
    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;
    @Autowired
    private DriveService driveService;
    @Autowired
    private EmailService emailService;

    public void insertarInformeDiagnostico(InformeDiagnosticoDTO informeDiagnosticoDTO, File file) throws IOException {
        // Validar si el orden de trabajo existe
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(informeDiagnosticoDTO.getId_ordenTrabajo())
                .orElseThrow(() -> new OrdenTrabajoNoEncontradaException("Orden de trabajo con ID " + informeDiagnosticoDTO.getId_ordenTrabajo() + " no encontrada"));

        // Validar si el número de serie es único
        if (informeDiagnosticoRepository.existsByNumeroSerie(informeDiagnosticoDTO.getNumeroSerie())) {
            throw new NumeroSerieDuplicadoException("El número de serie " + informeDiagnosticoDTO.getNumeroSerie() + " ya está registrado en otro informe diagnóstico.");
        }

        // Validar que la fecha no sea nula
        if (informeDiagnosticoDTO.getFecha() == null) {
            throw new FechaInvalidaException("La fecha del informe diagnóstico no puede ser nula.");
        }

        // Crear el Informe Diagnóstico
        InformeDiagnostico informeDiagnostico = new InformeDiagnostico();
        informeDiagnostico.setFecha(informeDiagnosticoDTO.getFecha());
        informeDiagnostico.setEstadoActual(informeDiagnosticoDTO.getEstadoActual());
        informeDiagnostico.setNumeroSerie(informeDiagnosticoDTO.getNumeroSerie());
        informeDiagnostico.setProblemasEncontrados(informeDiagnosticoDTO.getProblemasEncontrados());
        informeDiagnostico.setFactibilidadReparacion(informeDiagnosticoDTO.getFactibilidadReparacion());
        informeDiagnostico.setRecomendaciones(informeDiagnosticoDTO.getRecomendaciones());
        informeDiagnostico.setDiagnosticoTecnico(informeDiagnosticoDTO.getDiagnosticoTecnico());
        informeDiagnostico.setCodigoOrdenTrabajo(ordenTrabajo.getCodigo());
        informeDiagnostico.setEquipoIrreparable(informeDiagnosticoDTO.getEquipoirreparable());

        // Subir el archivo si hay
        if (file == null) {
            informeDiagnostico.setObservacionesAdicionales("No hay");
        } else {
            informeDiagnostico.setObservacionesAdicionales(driveService.uploadPdfToDrive(file, "observaciones"));
        }

        // Generar el PDF del informe
        File pdfCreado = generarPDF(informeDiagnostico);

        informeDiagnosticoRepository.save(informeDiagnostico);
        String name = driveService.uploadPdfToDrive(pdfCreado, "informe");
        System.out.println(name);
        emailService.enviarCorreo("Jefferson.asencios@unmsm.edu.pe", name);

        // Intentar eliminar el archivo PDF inmediatamente después de enviarlo
        try {
            if (pdfCreado.exists()) {
                boolean eliminado = pdfCreado.delete();
                if (eliminado) {
                    System.out.println("El archivo PDF se eliminó exitosamente.");
                } else {
                    System.out.println("No se pudo eliminar el archivo PDF.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al intentar eliminar el archivo PDF: " + e.getMessage());
        }
    }
    public File generarPDF(InformeDiagnostico informeDiagnostico) throws IOException {
        // Crear un archivo temporal
        File tempFile = File.createTempFile("informe_", ".pdf");

        // Obtener el nombre del archivo temporal
        String fileName = tempFile.getName(); // Esto te dará algo como "informe_12345.pdf"

        // Extraer la parte entre "informe_" y ".pdf"
        String nameWithoutPrefixAndSuffix = fileName.substring("informe_".length(), fileName.lastIndexOf(".pdf"));

        File file = new File("informe_" + nameWithoutPrefixAndSuffix + ".pdf");

        // Crear un nuevo documento PDF
        PDDocument document = new PDDocument();

        try {
            // Crear una nueva página
            PDPage page = new PDPage();
            document.addPage(page);

            // Crear un flujo de contenido en la página
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(100, 750);  // Posición en la página

            // Agregar el título al PDF
            contentStream.showText("Informe Diagnóstico");
            contentStream.newLine();

            // Establecer la fuente para el contenido
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Agregar los datos del informe
            contentStream.showText("Fecha: " + informeDiagnostico.getFecha());
            contentStream.newLine();
            contentStream.showText("Estado Actual: " + informeDiagnostico.getEstadoActual());
            contentStream.newLine();
            contentStream.showText("Número de Serie: " + informeDiagnostico.getNumeroSerie());
            contentStream.newLine();
            contentStream.showText("Factibilidad de Reparación: " + informeDiagnostico.getFactibilidadReparacion());
            contentStream.newLine();
            contentStream.showText("Recomendaciones: " + informeDiagnostico.getRecomendaciones());
            contentStream.newLine();
            contentStream.showText("Diagnóstico Técnico: " + informeDiagnostico.getDiagnosticoTecnico());
            contentStream.newLine();
            contentStream.showText("Observaciones Adicionales: " + informeDiagnostico.getObservacionesAdicionales());
            contentStream.newLine();

            // Cerrar el flujo de contenido
            contentStream.endText();
            contentStream.close();

            // Guardar el documento en el archivo
            document.save(file);
            document.close();

            System.out.println("PDF creado exitosamente");

        } catch (IOException e) {
            throw new GeneracionPDFException("No se pudo crear el archivo PDF debido a un error con el sistema de archivos.");
        } catch (Exception e) {
            throw new GeneracionPDFException("Ocurrió un error inesperado al generar el PDF.");
        }

        return file; // Retorna el archivo creado
    }

}
