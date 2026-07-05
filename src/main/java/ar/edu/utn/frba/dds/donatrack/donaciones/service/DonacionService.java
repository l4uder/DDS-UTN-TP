package ar.edu.utn.frba.dds.donatrack.donaciones.service;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion.DonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion.DonacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
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
    Donacion donacion = DonacionMapper.aDominio(request);
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

  private TipoEstadoDonacion parseEstado(String estado) {
    try {
      return TipoEstadoDonacion.valueOf(estado.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("Estado invalido: " + estado);
    }
  }

}
