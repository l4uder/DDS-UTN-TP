package ar.edu.utn.frba.dds.donatrack.donante;

import java.util.List;

public class PersonaHumana extends Donante{
  private String nombre;
  private String apellido;
  private int edad;
  private TipoDocumento tipoDocumento;
  private String documento;
  private Genero genero;
  private String direccion;

  public PersonaHumana(String nombre, String apellido,
                       TipoDocumento tipoDocumento,
                       int edad, String documento, Genero genero,
                       String direccion, MedioDeContacto medioContPred,
                       List<MedioDeContacto> contactos) {
    super(medioContPred, contactos);
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
    this.tipoDocumento = tipoDocumento;
    this.documento = documento;
    this.genero = genero;
    this.direccion = direccion;
  }
}
