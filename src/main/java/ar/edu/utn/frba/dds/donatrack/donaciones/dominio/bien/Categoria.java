package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

public class Categoria {
  private String nombre;

  public Categoria(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public Boolean esIgual(Categoria categoria) {
    return this.nombre.equalsIgnoreCase(categoria.getNombre());
  }
}
