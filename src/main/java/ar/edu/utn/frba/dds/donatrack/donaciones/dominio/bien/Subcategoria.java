package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

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

  public Boolean esIgual(Subcategoria otraSubcategoria) {
    return this.nombre.equalsIgnoreCase(otraSubcategoria.getNombre())
          && this.categoria.esIgual(otraSubcategoria.getCategoria());
  }
}