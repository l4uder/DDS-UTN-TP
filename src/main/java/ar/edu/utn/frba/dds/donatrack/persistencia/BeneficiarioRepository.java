package ar.edu.utn.frba.dds.donatrack.persistencia;

import ar.edu.utn.frba.dds.donatrack.dominio.beneficiario.Beneficiario;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioRepository {
  private static BeneficiarioRepository INSTANCE = new BeneficiarioRepository();
  private List<Beneficiario> beneficiarios;

  private BeneficiarioRepository() {
    beneficiarios = new ArrayList<>();
  }

  public static BeneficiarioRepository getInstancia() {
    return INSTANCE;
  }

  public void guardarBeneficiario(Beneficiario beneficiario) {
    this.beneficiarios.add(beneficiario);
  }

  public List<Beneficiario> buscarTodos() {
    return this.beneficiarios;
  }
}
