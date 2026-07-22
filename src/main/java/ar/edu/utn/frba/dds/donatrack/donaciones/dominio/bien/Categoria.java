package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

import lombok.Getter;

@Getter
public class Categoria {
  private String nombre;

  public Categoria(String nombre) {
    this.nombre = nombre;
  }

  public Boolean esIgual(Categoria categoria) {
    return this.nombre.equalsIgnoreCase(categoria.getNombre());
  }

}
