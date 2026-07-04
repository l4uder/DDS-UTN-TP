package ar.edu.utn.frba.dds.donatrack.donaciones.service;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.DonanteMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.List;

public class DonanteService {

  private final DonanteRepository repository = DonanteRepository.getInstancia();

  public List<Donante> listar(String tipo) {
    List<Donante> todos = repository.buscarTodos();
    if (tipo == null) {
      return todos;
    }
    return switch (tipo.toUpperCase()) {
      case "HUMANA" -> todos.stream().filter(d -> d instanceof PersonaHumana).toList();
      case "JURIDICA" -> todos.stream().filter(d -> d instanceof PersonaJuridica).toList();
      default -> throw new DomainValidationException(
          "Tipo invalido: " + tipo + " (humana o juridica)");
    };
  }

  public Donante obtener(String id) {
    return repository.buscarPorId(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("No existe donante con id " + id));
  }

  public Donante crear(DonanteRequest request) {
    Donante donante = DonanteMapper.aDominio(request);
    repository.guardarDonante(donante);
    return donante;
  }

  public Donante actualizar(String id, DonanteRequest request) {
    obtener(id);
    Donante actualizado = DonanteMapper.aDominio(request);
    actualizado.setId(id);
    repository.guardarDonante(actualizado);
    return actualizado;
  }

  public void eliminar(String id) {
    obtener(id);
    repository.eliminar(id);
  }

}
