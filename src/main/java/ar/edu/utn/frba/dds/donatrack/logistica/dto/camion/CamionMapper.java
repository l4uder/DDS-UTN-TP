package ar.edu.utn.frba.dds.donatrack.logistica.dto.camion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

public class CamionMapper {

  private CamionMapper() {
  }

  public static Camion aDominio(CamionRequest request) {
    if (request.patente() == null || request.patente().isBlank()) {
      throw new DomainValidationException("El campo 'patente' es obligatorio");
    }
    validarCapacidades(request);
    return new Camion(
        request.patente().trim(),
        request.capacidadVolumen(),
        request.altura(),
        request.capacidadCarga());
  }

  public static void actualizarDominio(Camion camion, CamionRequest request) {
    if (request.patente() != null && !request.patente().trim().equals(camion.getPatente())) {
      throw new DomainValidationException(
          "La patente no se puede modificar; eliminar el camion y crear uno nuevo");
    }
    validarCapacidades(request);
    camion.actualizarDatos(
        request.capacidadVolumen(),
        request.altura(),
        request.capacidadCarga());
  }

  public static CamionResponse aResponse(Camion camion) {
    return new CamionResponse(
        camion.getPatente(),
        camion.getCapacidadVolumen(),
        camion.getAltura(),
        camion.getCapacidadCarga());
  }

  private static void validarCapacidades(CamionRequest request) {
    validarPositivo(request.capacidadVolumen(), "capacidadVolumen");
    validarPositivo(request.altura(), "altura");
    validarPositivo(request.capacidadCarga(), "capacidadCarga");
  }

  private static void validarPositivo(Float valor, String campo) {
    if (valor == null) {
      throw new DomainValidationException("El campo '" + campo + "' es obligatorio");
    }
    if (valor <= 0) {
      throw new DomainValidationException("El campo '" + campo + "' debe ser mayor a 0");
    }
  }

}
