package ar.edu.utn.frba.dds.donatrack.donaciones.service;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.Asignador;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.algoritmos.CompatibilidadSemantica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.algoritmos.PrioridadSubAtendidos;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.asignacion.AsignacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.RankingRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.List;

public class AsignacionService {

  private static final int MAXIMO_CANDIDATOS = 10;

  private final DonacionRepository donaciones = DonacionRepository.getInstancia();
  private final BeneficiarioRepository beneficiarios = BeneficiarioRepository.getInstancia();
  private final RankingRepository rankings = RankingRepository.getInstancia();

  public Ranking ejecutarMatchmaking(String donacionId) {
    Donacion donacion = obtenerDonacion(donacionId);
    if (donacion.getEstadoActual() != TipoEstadoDonacion.EN_DEPOSITO) {
      throw new CambioDeEstadoNoPermitidoException(
          "El matchmaking solo puede ejecutarse sobre una donacion en deposito");
    }

    Asignador asignador = new Asignador();
    asignador.agregarAlgoritmo(new CompatibilidadSemantica());
    asignador.agregarAlgoritmo(new PrioridadSubAtendidos());

    List<Beneficiario> candidatos = asignador
        .asignar(donacion, beneficiarios.buscarTodos())
        .stream().limit(MAXIMO_CANDIDATOS).toList();

    Ranking ranking = new Ranking(donacion.getId(), candidatos);
    rankings.guardar(ranking);
    return ranking;
  }

  public Ranking obtenerRanking(String donacionId) {
    obtenerDonacion(donacionId);
    return rankings.buscarPorDonacion(donacionId)
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "La donacion " + donacionId + " no tiene un ranking generado"));
  }

  public Donacion confirmarAsignacion(String donacionId, AsignacionRequest request) {
    if (request == null || request.beneficiarioId() == null || request.beneficiarioId().isBlank()) {
      throw new DomainValidationException("El body necesita 'beneficiarioId'");
    }
    Donacion donacion = obtenerDonacion(donacionId);
    Beneficiario beneficiario = beneficiarios.buscarPorId(request.beneficiarioId())
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "No existe beneficiario con id " + request.beneficiarioId()));

    donacion.confirmarAsignacion(beneficiario);
    donaciones.guardarDonacion(donacion);
    beneficiarios.guardarBeneficiario(beneficiario);
    rankings.eliminar(donacionId);
    return donacion;
  }

  private Donacion obtenerDonacion(String id) {
    return donaciones.buscarPorId(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("No existe donacion con id " + id));
  }

}
