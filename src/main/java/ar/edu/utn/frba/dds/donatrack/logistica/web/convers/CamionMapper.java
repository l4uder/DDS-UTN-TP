package ar.edu.utn.frba.dds.donatrack.logistica.web.convers;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.camion.ActualizarCamionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.camion.CamionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.camion.CamionResponse;
import java.util.List;

public class CamionMapper {
  
  public static Camion aDominio (CamionRequest request) {
    return new Camion(
        request.patente(),
        request.capacidadVolumen(),
        request.altura(),
        request.capacidadCarga()
    );
  }

  public static CamionResponse aDto (Camion camion) {
    return new CamionResponse(
        camion.getPatente(),
        camion.getCapacidadVolumen(),
        camion.getAltura(),
        camion.getCapacidadCarga()
    );
  }

  public static List<CamionResponse> aDto (List<Camion> camiones) {
    return camiones.stream().map(CamionMapper::aDto).toList();
  }

  public static void actualizarDesdeRequest (Camion camion, Gps nuevoGps, ActualizarCamionRequest request){
    Float volumenMerge = request.capacidadVolumen() != null
        ? request.capacidadVolumen() : camion.getCapacidadVolumen();

    Float alturaMerge = request.altura() != null
        ? request.altura() : camion.getAltura();

    Float cargaMerge = request.capacidadCarga() != null
        ? request.capacidadCarga() : camion.getCapacidadCarga();

    Gps gpsMerge = request.gpsImei() != null ? nuevoGps : camion.getGps();

    camion.actualizarDatos(volumenMerge, alturaMerge, cargaMerge, gpsMerge);
  }
  
}
