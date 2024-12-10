package com.sanmarcos.promecal.service;
import com.sanmarcos.promecal.exception.*;
import com.sanmarcos.promecal.model.dto.ProformaServicioDTO;
import com.sanmarcos.promecal.model.dto.ProformaServicioListaDTO;
import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import com.sanmarcos.promecal.model.entity.ProformaServicio;
import com.sanmarcos.promecal.repository.ClienteRepository;
import com.sanmarcos.promecal.repository.OrdenTrabajoRepository;
import com.sanmarcos.promecal.repository.ProformaServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProformaServicioService {
    private final ProformaServicioRepository proformaServicioRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final ClienteRepository clienteRepository;
    private final DriveService driveService;
    @Autowired
    public ProformaServicioService(ProformaServicioRepository proformaServicioRepository,
                                   OrdenTrabajoRepository ordenTrabajoRepository,
                                   ClienteRepository clienteRepository,
                                   DriveService driveService) {
        this.proformaServicioRepository = proformaServicioRepository;
        this.ordenTrabajoRepository = ordenTrabajoRepository;
        this.clienteRepository = clienteRepository;
        this.driveService = driveService;
    }

    // Obtener todas las proformas de servicio
    public List<ProformaServicioListaDTO> obtenerTodosLasProformas() {
        List<ProformaServicio> proformas = proformaServicioRepository.findAll();

        // Validación: Si no se encuentran proformas, lanzar una excepción personalizada
        if (proformas.isEmpty()) {
            throw new ProformaServicioNotFoundException("No se encontraron proformas de servicio.");
        }
        return proformaServicioRepository.findAll().stream().map(proformaServicio -> {
            ProformaServicioListaDTO proformaServicioListaDTO = new ProformaServicioListaDTO();
            proformaServicioListaDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
            proformaServicioListaDTO.setId(proformaServicio.getId());
            proformaServicioListaDTO.setFecha(proformaServicio.getFecha());
            proformaServicioListaDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
            proformaServicioListaDTO.setEstadoPago(proformaServicio.getEstadoPago());
            proformaServicioListaDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
            proformaServicioListaDTO.setCodigo_ordentrabajo(proformaServicio.getOrdenTrabajo().getCodigo());
            proformaServicioListaDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
            return proformaServicioListaDTO;
        }).collect(Collectors.toList());
    }

    public void insertarProformaServicio(ProformaServicioDTO proformaServicioDTO) {
        // Validación de los datos de entrada
        if (proformaServicioDTO.getFecha() == null || proformaServicioDTO.getPrecioServicio() == null) {
            throw new ProformaServicioException("Los datos de la proforma de servicio son incompletos");
        }

        ProformaServicio proformaServicio = new ProformaServicio();
        proformaServicio.setFecha(proformaServicioDTO.getFecha());
        proformaServicio.setPrecioServicio(proformaServicioDTO.getPrecioServicio());
        proformaServicio.setEstadoPago(proformaServicioDTO.getEstadoPago());
        proformaServicio.setDetalleServicio(proformaServicioDTO.getDetalleServicio());
        proformaServicio.setCondicionesContratacion(proformaServicioDTO.getCondicionesContratacion());
        proformaServicio.setTiempoEstimadoEntrega(proformaServicioDTO.getTiempoEstimadoEntrega());

        // Buscar la Orden de Trabajo por código
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findByCodigo(proformaServicioDTO.getCodigo_ordentrabajo())
                .orElseThrow(() -> new OrdenTrabajoNoEncontradaException("Orden de Trabajo no encontrada"));

        proformaServicio.setOrdenTrabajo(ordenTrabajo);
        proformaServicio.setBoletaUrl(null);

        // Guardar la proforma de servicio
        proformaServicioRepository.save(proformaServicio);
    }

    // Obtener proforma de servicio por ID
    public ProformaServicioDTO obtenerProformaServicioPorId(Long id) {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id)
                .orElseThrow(() -> new ProformaServicioNotFoundException("Proforma de Servicio no encontrada con id: " + id));

        ProformaServicioDTO proformaServicioDTO = new ProformaServicioDTO();
        proformaServicioDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
        proformaServicioDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
        proformaServicioDTO.setEstadoPago(proformaServicio.getEstadoPago());
        proformaServicioDTO.setFecha(proformaServicio.getFecha());
        proformaServicioDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
        proformaServicioDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
        proformaServicioDTO.setCodigo_ordentrabajo(proformaServicio.getOrdenTrabajo().getCodigo());

        return proformaServicioDTO;
    }

    // Metodo para actualizar una proforma de servicio
    public void actualizarProformaServicio(Long id, ProformaServicioDTO proformaServicioDTO) {
        // Buscar la proforma de servicio
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id)
                .orElseThrow(() -> new ProformaServicioNotFoundException("Proforma de Servicio no encontrada"));

        // Validar el precio del servicio
        if (proformaServicioDTO.getPrecioServicio() <= 0) {
            throw new InvalidPriceException("El precio del servicio debe ser mayor que cero");
        }

        proformaServicio.setFecha(proformaServicioDTO.getFecha());
        proformaServicio.setPrecioServicio(proformaServicioDTO.getPrecioServicio());
        proformaServicio.setEstadoPago(proformaServicioDTO.getEstadoPago());
        proformaServicio.setDetalleServicio(proformaServicioDTO.getDetalleServicio());
        proformaServicio.setTiempoEstimadoEntrega(proformaServicioDTO.getTiempoEstimadoEntrega());
        proformaServicio.setCondicionesContratacion(proformaServicioDTO.getCondicionesContratacion());
        proformaServicio.setBoletaUrl(null);

        // Buscar la orden de trabajo asociada
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findByCodigo(proformaServicioDTO.getCodigo_ordentrabajo())
                .orElseThrow(() -> new OrdenTrabajoNoEncontradaException("Orden de trabajo no encontrada"));

        proformaServicio.setOrdenTrabajo(ordenTrabajo);

        // Guardar la proforma actualizada
        proformaServicioRepository.save(proformaServicio);
    }

    // Eliminar una proforma de servicio
    public void eliminarProformaServicio(Long id) {
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id)
                .orElseThrow(() -> new ProformaServicioNotFoundException("Proforma de Servicio no encontrada"));

        proformaServicioRepository.delete(proformaServicio);
    }

    // Obtener proformas de servicio por cliente
    public List<ProformaServicioListaDTO> obtenerProformaServicioPorCliente(String dni) {
        // Validar que el DNI no esté vacío o nulo
        if (dni == null || dni.isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        }

        // Buscar el cliente por DNI
        Cliente cliente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado"));

        // Buscar las órdenes de trabajo asociadas al cliente
        List<OrdenTrabajo> ordenesDeTrabajo = ordenTrabajoRepository.findByCliente(cliente);

        // Si no hay órdenes de trabajo para el cliente, lanzar excepción
        if (ordenesDeTrabajo.isEmpty()) {
            throw new ProformaServicioNotFoundException("No se encontraron proformas de servicio para este cliente");
        }

        // Mapear las órdenes de trabajo a DTOs de proforma
        return ordenesDeTrabajo.stream()
                .map(ordenTrabajo -> {
                    ProformaServicio proformaServicio = proformaServicioRepository.findByOrdenTrabajo(ordenTrabajo);
                    ProformaServicioListaDTO proformaServicioListaDTO = new ProformaServicioListaDTO();

                    if (proformaServicio != null) {
                        proformaServicioListaDTO.setDetalleServicio(proformaServicio.getDetalleServicio());
                        proformaServicioListaDTO.setId(proformaServicio.getId());
                        proformaServicioListaDTO.setFecha(proformaServicio.getFecha());
                        proformaServicioListaDTO.setPrecioServicio(proformaServicio.getPrecioServicio());
                        proformaServicioListaDTO.setEstadoPago(proformaServicio.getEstadoPago());
                        proformaServicioListaDTO.setCondicionesContratacion(proformaServicio.getCondicionesContratacion());
                        proformaServicioListaDTO.setCodigo_ordentrabajo(proformaServicio.getOrdenTrabajo().getCodigo());
                        proformaServicioListaDTO.setTiempoEstimadoEntrega(proformaServicio.getTiempoEstimadoEntrega());
                    }
                    return proformaServicioListaDTO;
                })
                .filter(dto -> dto.getId() != null)  // Filtrar los DTOs con ID null (si no hay proforma asociada)
                .collect(Collectors.toList());
    }

    // Registrar el pago de una proforma
    public void registrarPago(Long id, File tempFile) {
        // Buscar la Proforma de Servicio por ID
        ProformaServicio proformaServicio = proformaServicioRepository.findById(id)
                .orElseThrow(() -> new ProformaServicioNotFoundException("Proforma de Servicio no encontrada"));

        // Validar que el estado de pago sea diferente a "PAGADO"
        if ("PAGADO".equals(proformaServicio.getEstadoPago())) {
            throw new PagoYaRegistradoException("El pago ya ha sido registrado para esta proforma");
        }

        // Actualizar el estado del pago a "PAGADO"
        proformaServicio.setEstadoPago("PAGADO");

        // Subir el archivo a Drive y obtener la URL
        String boletaUrl = driveService.uploadPdfToDrive(tempFile, "proforma");
        proformaServicio.setBoletaUrl(boletaUrl);

        // Guardar la proforma con la nueva URL de la boleta
        proformaServicioRepository.save(proformaServicio);
    }
}
