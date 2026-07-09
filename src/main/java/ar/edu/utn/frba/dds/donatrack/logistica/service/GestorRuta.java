package ar.edu.utn.frba.dds.donatrack.logistica.service;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.*;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.orquestadorlogistica.OrquestadorLogistica;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.orquestadorlogistica.ResultadoPlanificacion;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.planificacion.CallbackPlanificacionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorRuta {
  private final RutaRepository rutaRepository;
  private final CamionRepository camionRepository;
  private final EntregaRepository entregaRepository;

  public GestorRuta(RutaRepository rutaRepository, CamionRepository camionRepository,
                    EntregaRepository entregaRepository) {
    this.rutaRepository = rutaRepository;
    this.camionRepository = camionRepository;
    this.entregaRepository = entregaRepository;
  }

  public List<Ruta> listar() {
    return rutaRepository.buscarTodas();
  }

  public Ruta obtener(String id) {
    return rutaRepository.buscarPorId(id);
  }

  public void asignarChofer(String id, Chofer chofer) {
    Ruta ruta = rutaRepository.buscarPorId(id);
    ruta.asignarChofer(chofer);
    rutaRepository.guardar(ruta);
  }

  public void iniciarRecorrido(String id) {
    Ruta ruta = rutaRepository.buscarPorId(id);
    ruta.iniciarRecorrido();
    rutaRepository.guardar(ruta);
  }

  public List<Ruta> procesarCallback(CallbackPlanificacionRequest request) {
    LocalDate fecha = LocalDate.parse(request.fecha());

    Map<Camion, List<Entrega>> entregasPorCamion = new HashMap<>();
    request.entregasPorPatente().forEach((patente, idsEntregas) -> {
      Camion camion = camionRepository.buscarPorPatente(patente)
          .orElseThrow(() -> new RecursoNoEncontradoException("Camión no encontrado: " + patente));
      List<Entrega> entregas = idsEntregas.stream()
          .map(entregaRepository::buscarPorId)
          .toList();
      entregasPorCamion.put(camion, entregas);
    });

    List<Entrega> sinAsignar = request.entregasSinAsignar().stream()
        .map(entregaRepository::buscarPorId)
        .toList();

    ResultadoPlanificacion resultado = new ResultadoPlanificacion(entregasPorCamion, sinAsignar);
    OrquestadorLogistica orquestador = new OrquestadorLogistica(
        new ArrayList<>(entregasPorCamion.keySet()), new ArrayList<>()
    );

    List<Ruta> rutas = orquestador.procesarResultadoPlanificacion(resultado, fecha);
    rutas.forEach(rutaRepository::guardar);
    return rutas;
  }
}