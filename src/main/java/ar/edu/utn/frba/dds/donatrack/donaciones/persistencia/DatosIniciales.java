package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class DatosIniciales implements WithSimplePersistenceUnit {

  public static void init() {
    new DatosIniciales().comenzar();
  }

  public void comenzar() {
    BeneficiarioRepository repoBeneficiarios = BeneficiarioRepository.getInstancia();
    Beneficiario eric = new Beneficiario("ericH", "siempre viva, Springfield", List.of(new CorreoDeContato("correo@gmail.com", true)));

    beginTransaction();
    repoBeneficiarios.guardar(eric);
    commitTransaction();
  }

}
