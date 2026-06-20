package ar.edu.utn.frba.dds.donatrack.dominio.bien;

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

  public Categoria getCategoria() {
    return this.categoria;
  }
}