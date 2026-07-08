package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donaciones.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.donaciones.dominio.donante.RegistroEntrega;
import java.util.ArrayList;
import java.util.List;

public class RegistroEntregaBuilder {
  private String descripcion;
  private List<Bien> bienes;
  private Donante donantePrueba = new PersonaHumanaBuilder()
    .conNombre("Juan")
    .conApellido("Pérez")
    .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
    .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
    .build();

  public RegistroEntregaBuilder() {
    this.bienes = new ArrayList<>();
  }

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
    return new RegistroEntrega(donantePrueba, descripcion, bienes);
  }
}
