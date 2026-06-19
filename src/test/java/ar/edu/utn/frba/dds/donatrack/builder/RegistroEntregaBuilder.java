package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.RegistroEntrega;
import java.util.ArrayList;
import java.util.List;

public class RegistroEntregaBuilder {
  private String descripcion = "Descripcion default";
  private List<Bien> bienes = new ArrayList<>();

  public RegistroEntregaBuilder conDescripcion(String descripcion) {
    this.descripcion = descripcion;
    return this;
  }

  public RegistroEntregaBuilder conBien(Bien bien) {
    this.bienes.add(bien);
    return this;
  }

  public RegistroEntregaBuilder conBienes(List<Bien> bienes) {
    this.bienes.addAll(bienes);
    return this;
  }

  public RegistroEntrega build() {
    return new RegistroEntrega(descripcion, bienes);
  }
}
