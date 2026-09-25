package ar.edu.utn.frba.dds.donatrack.donacion;

import static org.junit.jupiter.api.Assertions.assertTrue;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Frecuencia;
import org.junit.jupiter.api.Test;

public class NecesidadTest {
  @Test
  public void necesidadExtraordinariaEsSatisfecha(){
    Categoria alimentos = new Categoria("Alimentos");
    Subcategoria arroz = new Subcategoria("arroz", alimentos);
    Necesidad necesidad1 = Necesidad.crearNecesidadExtraordinaria(
        "descripcion",
        arroz,
        UnidadMedida.KILOGRAMOS,
        30);
    necesidad1.recibirBienes(34);
    assertTrue(necesidad1.estaSatisfecha());
  }
  @Test
  public void necesidadRecurrenteEsSatisfecha(){
    Categoria muebleria = new Categoria("Muebleria");
    Subcategoria sillas = new Subcategoria("sillas", muebleria);
    Necesidad necesidad2 = Necesidad.crearNecesidadRecurrente(
        "23 sillas",
        sillas,
        UnidadMedida.UNIDADES,
        10,
        Frecuencia.SEMANAL);
    necesidad2.recibirBienes(23);
    assertTrue(necesidad2.estaSatisfecha());
  }

}
