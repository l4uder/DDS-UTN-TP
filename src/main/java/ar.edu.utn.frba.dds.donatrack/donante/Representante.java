package ar.edu.utn.frba.dds.donatrack.donante;

import java.util.ArrayList;
import java.util.List;

public class Representante {
  private String nombre;
  private String apellido;
  private int edad;
  private TipoDocumento tipoDocumento;
  private String documento;
  private Genero genero;
  private String direccion;
  private List<MedioContacto> contactos;

  public Representante(String nombre, String apellido,
                       TipoDocumento tipoDocumento,
                       int edad, String documento, Genero genero,
                       String direccion, List<MedioContacto> contactos){
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
    this.tipoDocumento = tipoDocumento;
    this.documento = documento;
    this.genero = genero;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
  }
}
