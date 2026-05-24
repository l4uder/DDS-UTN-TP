package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Categoria;
import ar.edu.utn.frba.dds.donatrack.donacion.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.necesidades.NecesidadExtraordinaria;
import ar.edu.utn.frba.dds.donatrack.necesidades.NecesidadRecurrente;
import ar.edu.utn.frba.dds.donatrack.necesidades.Periodo;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class NecesidadTest {
  @Test
  public void necesidadExtraordinariaEsSatisfecha(){
    Categoria alimentos = new Categoria("Alimentos");
    Subcategoria arroz = new Subcategoria("arroz", alimentos);
    NecesidadExtraordinaria necesidad1 = new NecesidadExtraordinaria(
        arroz,
        "descripcion",
        30);
    necesidad1.recibirBienes(34);
    assertTrue(necesidad1.esSatisfecha());
  }
  @Test
  public void necesidadRecurrenteEsSatisfecha(){
    Categoria muebleria = new Categoria("Muebleria");
    Subcategoria sillas = new Subcategoria("sillas", muebleria);
    NecesidadRecurrente necesidad2 = new NecesidadRecurrente(
        sillas,
        "23 sillas",
        10,
        Periodo.SEMANAL);
    necesidad2.recibirBienes(23);
    assertTrue(necesidad2.esSatisfecha());
  }
}
