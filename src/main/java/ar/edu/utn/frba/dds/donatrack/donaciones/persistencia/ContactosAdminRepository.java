package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;

import java.util.*;

public final class ContactosAdminRepository {
  private static final ContactosAdminRepository INSTANCE = new ContactosAdminRepository();
  private final List<MedioContacto> storeContactosAdmins;

  private ContactosAdminRepository() {
    storeContactosAdmins = new ArrayList<>();
  }

  public static ContactosAdminRepository getInstancia() {
    return INSTANCE;
  }

  public void agregar(MedioContacto medioContacto) {
    this.storeContactosAdmins.add(medioContacto);
  }

  public List<MedioContacto> buscarTodos() {
    return storeContactosAdmins;
  }

}
