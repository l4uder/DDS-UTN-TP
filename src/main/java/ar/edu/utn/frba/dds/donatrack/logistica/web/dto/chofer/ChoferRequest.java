package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.chofer;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Chofer;

public record ChoferRequest(String nombre, String apellido, String licenciaConducir) {
  public Chofer aDominio() {
    return new Chofer(nombre, apellido, licenciaConducir);
  }
}