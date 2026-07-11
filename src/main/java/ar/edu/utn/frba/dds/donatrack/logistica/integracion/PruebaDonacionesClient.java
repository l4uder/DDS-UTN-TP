package ar.edu.utn.frba.dds.donatrack.logistica.integracion;

import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioDeEstadosDonacionLogistica;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoEntregadaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoErrorEntregaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoInicioRutaRequest;

/**
 * Main de prueba manual. Requiere DonacionesApp corriendo en el puerto 7070.
 *
 * Ejecución:
 * java -cp target/donatrack-jar-with-dependencies.jar
 *   ar.edu.utn.frba.dds.donatrack.logistica.integracion.PruebaDonacionesClient
 */
public class PruebaDonacionesClient {

  public static void main(String[] args) {
    DonacionesClient client = new DonacionesClient();

    System.out.println("=== PruebaDonacionesClient ===");
    System.out.println("Consultando donaciones en ASIGNACION_REALIZADA...");

    try {
      var donaciones = client.buscarDonacionesAsignadas();
      System.out.println("Donaciones encontradas: " + donaciones.size());
      donaciones.forEach(d -> System.out.printf(
          "  - id=%s | desc=%s | beneficiario=%s (%s)%n",
          d.getId(),
          d.getDescripcion(),
          d.getBeneficiario().getRazonSocial(),
          d.getBeneficiario().getDireccion()
      ));

      if (!donaciones.isEmpty() && args.length > 0) {
        String donacionId = donaciones.get(0).getId();
        var nuevoEstado = CambioDeEstadosDonacionLogistica.valueOf(args[0]);
        System.out.println();
        System.out.println("Cambiando estado de " + donacionId + " a " + nuevoEstado + "...");
        switch (nuevoEstado){
          case ENTREGADA -> client.cambiarEstadoDonacion(donacionId, new CambioEstadoEntregadaRequest("1"));
          case INICIO_RUTA -> client.cambiarEstadoDonacion(donacionId, new CambioEstadoInicioRutaRequest("https://mapa.map/123"));
          case ERROR_ENTREGA -> client.cambiarEstadoDonacion(donacionId, new CambioEstadoErrorEntregaRequest("El camion pincho una rueda"));
        }
        System.out.println("Estado cambiado correctamente.");
      } else if (!donaciones.isEmpty()) {
        System.out.println();
        System.out.println("(Para probar cambiarEstadoDonacion, pasá el nuevo estado como argumento)");
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
