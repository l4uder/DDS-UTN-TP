package ar.edu.utn.frba.dds.donatrack.logistica.integracion;

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
        String nuevoEstado = args[0];
        System.out.println();
        System.out.println("Cambiando estado de " + donacionId + " a " + nuevoEstado + "...");
        client.cambiarEstadoDonacion(donacionId, nuevoEstado);
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
