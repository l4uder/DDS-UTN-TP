package ar.edu.utn.frba.dds.donatrack.clasificacion;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class Subcategoria {
  private UUID id_Subcategoria;
  private String nombre;
  private Categoria categoria;

  public Subcategoria(String nombre,
                      Categoria categoria
  ) {
    this.id_Subcategoria = UUID.randomUUID();
    this.nombre = nombre;
    this.categoria = categoria;
  }
}