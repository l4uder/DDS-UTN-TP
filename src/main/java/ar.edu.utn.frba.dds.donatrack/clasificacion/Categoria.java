package ar.edu.utn.frba.dds.donatrack.clasificacion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Categoria {
  private UUID idCategoria;
  private String nombre;

  public Categoria(String nombre){
    this.idCategoria = UUID.randomUUID();
    this.nombre = nombre;
  }
}
