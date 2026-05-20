package ar.edu.utn.frba.dds.donatrack.clasificacion;

import java.util.List;
import java.util.UUID;

public class Categoria {
  private UUID id_categoria;
  private String nombre;
  private List<Subcategoria> categorias;

  public Categoria(String nombre){
    this.id_categoria = UUID.randomUUID();
    this.nombre = nombre;
    //this.categorias = categorias;
  }
}
