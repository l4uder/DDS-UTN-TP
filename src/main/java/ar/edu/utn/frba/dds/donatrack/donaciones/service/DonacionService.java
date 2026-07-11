package ar.edu.utn.frba.dds.donatrack.donaciones.service;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoEntregaExitosa;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoEntregaFallida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoInicioDeRuta;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion.DonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion.DonacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.server.AppEventBus;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoRequest;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;

import java.time.LocalDate;
import java.util.List;

public class DonacionService {

  private final DonacionRepository repository = DonacionRepository.getInstancia();

  public List<Donacion> listar(String estado) {
    if (estado == null) {
      return repository.buscarTodos();
    }
    return repository.buscarPorEstado(parseEstado(estado));
  }

  public Donacion obtener(String id) {
    return repository.buscarPorId(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("No existe donacion con id " + id));
  }

  public Donacion crear(DonacionRequest request) {
    List<Donante> donantes = request.donanteIds().stream()
      .map(id -> DonanteRepository.getInstancia().buscarPorId(id)
      .orElseThrow(() -> new DomainValidationException("Donante no encontrado con ID: " + id)))
      .toList();

    Donacion donacion = DonacionMapper.aDominio(request, donantes);
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public Donacion actualizar(String id, DonacionRequest request) {
    Donacion donacion = obtener(id);
    donacion.reemplazarBienes(DonacionMapper.aBienes(request.bienes()));
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public void eliminar(String id) {
    obtener(id);
    repository.eliminar(id);
  }
  // TODO pasar a endpoints individuales
  public Donacion cambiarEstado(String id, CambioEstadoRequest request) {
    if (request == null || request.estado() == null) {
      throw new DomainValidationException("El body necesita 'estado'");
    }
    Donacion donacion = obtener(id);
    switch (parseEstado(request.estado())) {
      case ASIGNACION_REALIZADA -> throw new DomainValidationException(
          "No se puede asignar por este medio, la asignacion requiere un beneficiario");
      case LISTA_PARA_ENTREGAR -> donacion.confirmarRuta();
      case EN_DEPOSITO -> donacion.confirmarRecepcionDeposito();
      case VENCIDA -> donacion.marcarVencida();
    }
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public Donacion cambiarEstadoEntregada(String id, String idCamion) {
    Donacion donacion = obtener(id);
    donacion.confirmarEntrega();
    AppEventBus.getInstance().post(new EventoEntregaExitosa(donacion, LocalDate.now(), idCamion));
    return donacion;
  }

  public Donacion cambiarEstadoErrorEntrega(String id, String observacion) {
    Donacion donacion = obtener(id);
    donacion.notificarEntregaFallida(observacion);
    AppEventBus.getInstance().post(new EventoEntregaFallida(observacion, donacion, LocalDate.now()));
    return donacion;
  }

  public Donacion cambiarEstadoEnTraslado(String id, String linkMapa) {
    Donacion donacion = obtener(id);
    donacion.confirmarTrasladoEnCurso();
    AppEventBus.getInstance().post(new EventoInicioDeRuta(donacion, LocalDate.now(), linkMapa));
    return donacion;
  }

  public List<EstadoDonacion> listarEstados(String id) {
    return obtener(id).getHistorialEstados();
  }

  private TipoEstadoDonacion parseEstado(String estado) {
    try {
      return TipoEstadoDonacion.valueOf(estado.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("Estado invalido: " + estado);
    }
  }

}
