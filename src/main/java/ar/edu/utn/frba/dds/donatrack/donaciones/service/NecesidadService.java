package ar.edu.utn.frba.dds.donatrack.donaciones.service;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.List;
import java.util.UUID;

public class NecesidadService {

  private final BeneficiarioRepository repository = BeneficiarioRepository.getInstancia();

  public List<Necesidad> listar(String beneficiarioId) {
    return obtenerBeneficiario(beneficiarioId).getNecesidades();
  }

  public Necesidad obtener(String beneficiarioId, String necesidadId) {
    Beneficiario beneficiario = obtenerBeneficiario(beneficiarioId);
    return obtenerNecesidad(beneficiario, necesidadId);
  }

  public Necesidad crear(String beneficiarioId, Necesidad necesidad) {
    Beneficiario beneficiario = obtenerBeneficiario(beneficiarioId);
    necesidad.setId(UUID.randomUUID().toString());
    beneficiario.agregarNecesidad(necesidad);
    repository.guardar(beneficiario);
    return necesidad;
  }

  public void guardar(String beneficiarioId) {
    repository.guardar(obtenerBeneficiario(beneficiarioId));
  }

  public void eliminar(String beneficiarioId, String necesidadId) {
    Beneficiario beneficiario = obtenerBeneficiario(beneficiarioId);
    Necesidad necesidad = obtenerNecesidad(beneficiario, necesidadId);
    beneficiario.eliminarNecesidad(necesidad);
    repository.guardar(beneficiario);
  }

  private Beneficiario obtenerBeneficiario(String id) {
    Beneficiario beneficia = repository.buscarPorId(id);
    if (beneficia == null) throw new RecursoNoEncontradoException("No existe beneficiario: " + id);

    return beneficia;
  }

  private Necesidad obtenerNecesidad(Beneficiario beneficiario, String necesidadId) {
    return beneficiario.buscarNecesidad(necesidadId)
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "El beneficiario no tiene una necesidad con id " + necesidadId));
  }

}
