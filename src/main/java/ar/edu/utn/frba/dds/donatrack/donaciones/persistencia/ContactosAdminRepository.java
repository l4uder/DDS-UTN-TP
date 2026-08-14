package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;

import java.util.*;

public final class AdministratorRepository {
  private static final AdministratorRepository INSTANCE = new AdministratorRepository();
  private final List<MedioContacto> administradores;

  private AdministratorRepository() {
    administradores = new ArrayList<>();
  }

  public static AdministratorRepository getInstancia() {
    return INSTANCE;
  }

  public List<MedioContacto> buscarTodos() {
    return administradores;
  }
}
