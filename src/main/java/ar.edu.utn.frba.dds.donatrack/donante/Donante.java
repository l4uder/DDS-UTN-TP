package ar.edu.utn.frba.dds.donatrack.donante;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Donante {
  private UUID idDonante;
  private MedioDeContacto medioDeContactoPred;
  private List<MedioDeContacto> contactos;
  private List<RegistroEntrega> entregas;

  public Donante(MedioDeContacto medioDeContactoPred, List<MedioDeContacto> contactos) {
    this.idDonante = UUID.randomUUID();
    this.medioDeContactoPred = medioDeContactoPred;
    this.contactos = new ArrayList<>(contactos);
    this.entregas = new ArrayList<>();
  }

  public void cambiarContactoPred(MedioDeContacto contacto) {
    this.medioDeContactoPred = contacto;
  }
}
