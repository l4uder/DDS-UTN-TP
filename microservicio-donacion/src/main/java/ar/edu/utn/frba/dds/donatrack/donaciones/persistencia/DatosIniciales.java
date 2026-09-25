package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContacto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class DatosIniciales implements WithSimplePersistenceUnit {

  public static void init() {
    new DatosIniciales().comenzar();
  }

  public void comenzar() {
    BeneficiarioRepository repoBeneficiarios = BeneficiarioRepository.getInstancia();
    CategoriaRepository repoCategorias = CategoriaRepository.getInstancia();
    SubcategoriaRepository repoSubcategorias = SubcategoriaRepository.getInstancia();

    Beneficiario eric = new Beneficiario("ericH", "siempre viva, Springfield", List.of(new CorreoDeContacto("correo@gmail.com", true)));
    Categoria alimentos = new Categoria("alimentos");
    Subcategoria lacteos = new Subcategoria("lacteos", alimentos);
    Subcategoria frutas = new Subcategoria("frutas", alimentos);

    beginTransaction();
    repoBeneficiarios.guardar(eric);
    repoCategorias.guardar(alimentos);
    repoSubcategorias.guardar(lacteos);
    repoSubcategorias.guardar(frutas);
    commitTransaction();
  }

}
