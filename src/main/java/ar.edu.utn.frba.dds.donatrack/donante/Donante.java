package ar.edu.utn.frba.dds.donatrack.donante;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Donante {
  private UUID idDonante;
  private MedioContacto medioDeContactoPred;
  private List<MedioContacto> contactosSecundarios;
  private List<RegistroEntrega> entregas;

  public Donante(MedioContacto medioDeContactoPred, List<MedioContacto> contactos) {
    this.idDonante = UUID.randomUUID();
    this.medioDeContactoPred = medioDeContactoPred;
    if(medioDeContactoPred == null)
          throw new IllegalArgumentException("El medio de contacto principal no puede ser null");
    this.contactosSecundarios = new ArrayList<>(contactos);
    this.entregas = new ArrayList<>();
  }

  public void cambiarContactoPred(MedioContacto contacto) {
    this.medioDeContactoPred = contacto;
  }

  public List<MedioContacto> getMediosContacto(tipoContacto tipoContacto) {
      List<MedioContacto> todos = new ArrayList<>();
      todos.add(medioDeContactoPred);
      todos.addAll(contactosSecundarios);

      List<MedioContacto> filtradoPorTipo = todos.stream().filter(c-> c.getTipo().equals(tipoContacto)).toList();

      return filtradoPorTipo;
  }
}
