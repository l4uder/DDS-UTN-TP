package ar.edu.utn.frba.dds.donatrack.donante;

import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.util.ArrayList;
import java.util.List;


public abstract class Donante {
  private MedioContacto medioDeContactoPred;
  private List<MedioContacto> contactosSecundarios;
  private List<RegistroEntrega> entregas;

  public Donante(MedioContacto medioDeContactoPred, List<MedioContacto> contactosSecundarios) {
    if (medioDeContactoPred == null) {
      throw new IllegalArgumentException("El medio de contacto principal no puede ser null");
    }
    this.medioDeContactoPred = medioDeContactoPred;

    this.contactosSecundarios = contactosSecundarios != null
        ? new ArrayList<>(contactosSecundarios)
        : new ArrayList<>();
    this.entregas = new ArrayList<>();
  }

  public void cambiarContactoPred(MedioContacto contacto) {
    this.medioDeContactoPred = contacto;
  }

  public List<MedioContacto> getMediosContacto(TipoContacto tipoContacto) {
    List<MedioContacto> todos = new ArrayList<>();
    todos.add(medioDeContactoPred);
    todos.addAll(contactosSecundarios);

    return todos.stream().filter(c -> c.getTipo().equals(tipoContacto)).toList();
  }

  public void agregarContactoSecundario(MedioContacto contacto) {
    this.contactosSecundarios.add(contacto);
  }

  public MedioContacto getMedioDeContactoPred() {
    return medioDeContactoPred;
  }
}
