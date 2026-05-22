package ar.edu.utn.frba.dds.donatrack.donante;

import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.util.ArrayList;
import java.util.List;


public abstract class Donante {
  protected MedioContacto medioDeContactoPred;
  protected List<MedioContacto> contactosSecundarios;
  protected List<RegistroEntrega> entregas;

  public Donante(MedioContacto medioDeContacto, List<MedioContacto> contactosSecundarios) {
    if (medioDeContacto == null) {
      throw new IllegalArgumentException("El medio de contacto principal no puede ser null");
    }
    this.medioDeContactoPred = medioDeContacto;

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

  public List<MedioContacto> getContactosSecundarios() {
    return contactosSecundarios;
  }

  abstract public boolean esElMismo(Donante otroDonante);

  abstract public void actualizar(Donante otroDonante);
}
