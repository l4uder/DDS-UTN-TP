package ar.edu.utn.frba.dds.donatrack.clasificacion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Subcategoria {
  private UUID idSubcategoria;
  private String nombre;
  private Categoria categoria;

  public Subcategoria(String nombre, Categoria categoria) {
    this.idSubcategoria = UUID.randomUUID();
    this.nombre = nombre;
    this.categoria = categoria;
  }

  public String getNombre() {
    return this.nombre;
  }
}