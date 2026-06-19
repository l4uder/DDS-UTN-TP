package ar.edu.utn.frba.dds.donatrack.donacion;

public class Subcategoria {
  private String nombre;
  private Categoria categoria;

  public Subcategoria(String nombre,
                      Categoria categoria) {
    this.nombre = nombre;
    this.categoria = categoria;
  }

  public String getNombre() {
    return this.nombre;
  }


}