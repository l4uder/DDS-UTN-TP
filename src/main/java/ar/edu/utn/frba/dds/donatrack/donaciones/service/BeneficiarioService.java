package ar.edu.utn.frba.dds.donatrack.donaciones.service;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.beneficiario.BeneficiarioMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.beneficiario.BeneficiarioRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.comun.ContactoMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.List;

public class BeneficiarioService {

  private final BeneficiarioRepository repository = BeneficiarioRepository.getInstancia();

  public List<Beneficiario> listar() {
    return repository.buscarTodos();
  }

  public Beneficiario obtener(String id) {
    Beneficiario beneficiario = this.repository.buscarPorId(id);
    if (beneficiario == null) {
      throw new RecursoNoEncontradoException("No existe beneficiario con id " + id);
    }

    return beneficiario;
  }

  public Beneficiario crear(BeneficiarioRequest request) {
    Beneficiario beneficiario = BeneficiarioMapper.aDominio(request);
    repository.guardarBeneficiario(beneficiario);
    return beneficiario;
  }

  public Beneficiario actualizar(String id, BeneficiarioRequest request) {
    Beneficiario beneficiario = obtener(id);
    BeneficiarioMapper.validar(request);
    beneficiario.actualizarDatos(
        request.razonSocial(),
        request.direccion(),
        ContactoMapper.aDominio(request.contactos()));
    repository.guardarBeneficiario(beneficiario);
    return beneficiario;
  }

  public void eliminar(String id) {
    obtener(id);
    repository.eliminar(id);
  }

}
