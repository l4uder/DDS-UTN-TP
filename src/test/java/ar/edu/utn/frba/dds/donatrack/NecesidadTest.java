package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.NecesidadExtraordinaria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.NecesidadRecurrente;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Periodo;
import org.junit.jupiter.api.Test;

public class NecesidadTest {
  @Test
  public void necesidadExtraordinariaEsSatisfecha(){
    Categoria alimentos = new Categoria("Alimentos");
    Subcategoria arroz = new Subcategoria("arroz", alimentos);
    NecesidadExtraordinaria necesidad1 = new NecesidadExtraordinaria(
        arroz,
        UnidadMedida.KILOGRAMOS,
        "descripcion",
        30);
    necesidad1.recibirBienes(34);
    assertTrue(necesidad1.estaSatisfecha());
  }
  @Test
  public void necesidadRecurrenteEsSatisfecha(){
    Categoria muebleria = new Categoria("Muebleria");
    Subcategoria sillas = new Subcategoria("sillas", muebleria);
    NecesidadRecurrente necesidad2 = new NecesidadRecurrente(
        sillas,
        UnidadMedida.SIN_UNIDAD,
        "23 sillas",
        10,
        Periodo.SEMANAL);
    necesidad2.recibirBienes(23);
    assertTrue(necesidad2.estaSatisfecha());
  }
}
