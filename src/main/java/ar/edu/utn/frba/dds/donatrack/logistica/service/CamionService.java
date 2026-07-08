package ar.edu.utn.frba.dds.donatrack.logistica.service;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.camion.CamionMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.camion.CamionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.List;

public class CamionService {

  private final CamionRepository repository = CamionRepository.getInstancia();

  public List<Camion> listar() {
    return repository.buscarTodos();
  }

  public Camion obtener(String patente) {
    return repository.buscarPorPatente(patente)
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "No existe camion con patente " + patente));
  }

  public Camion crear(CamionRequest request) {
    Camion camion = CamionMapper.aDominio(request);
    if (repository.buscarPorPatente(camion.getPatente()).isPresent()) {
      throw new DomainValidationException(
          "Ya existe un camion con patente " + camion.getPatente());
    }
    repository.guardar(camion);
    return camion;
  }

  public Camion actualizar(String patente, CamionRequest request) {
    Camion camion = obtener(patente);
    CamionMapper.actualizarDominio(camion, request);
    repository.guardar(camion);
    return camion;
  }

  public void eliminar(String patente) {
    obtener(patente);
    repository.eliminar(patente);
  }

}
